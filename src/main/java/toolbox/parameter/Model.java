package toolbox.parameter;

import java.util.*;



/**
 * La classe {@link Model} repr�sente les combinaisons de param�tres valides. Il contient aussi les informations relatives aux param�tres, telles que leur nom et les contraintes sur leur valeur.<br>
 * <br>
 * <u><i>V�rification du mod�le</i></u><br>
 * Les constructeurs prennent en argument les moules des param�tres et/ou une chaine d�crivant les combinaisons valides. La syntaxe de cette chaine est inspir�e de celle des mod�les de contenus des
 * �l�ments XML, avec quelques diff�rences:
 * <ul>
 * <li>les groupes ne peuvent pas �tre r�p�titifs, mais uniquement facultatifs (pas de + ou * apr�s les parenth�ses fermantes);
 * <li>les "," sont remplac�es par des "&";
 * <li>les param�tres peuvent �tre valu�s (MonParam�tre="MaValeur"), et dans ce cas une valeur doit �tre pr�cis�e partout o� ils apparaissent dans le mod�le de contenu, et ils ne peuvent pas �tre
 * r�p�titifs.
 * </ul>
 * L'ordre des param�tres n'a pas d'importance pour la v�rification, ni dans le mod�le, ni dans les param�tres eux-m�mes. Par exemple, le mod�les (A & B*) est identique au mod�le (B* & A), et les
 * ensembles de param�tres A, A B, B A ou B B A B y sont tous conformes.<br>
 * <br>
 * Les noms des param�tres doivent �tre constitu�s uniquement des caract�res: {@value AbstractMold#NAMES_CHARS}. Pour des raisons de lisibilit� et de coh�rence, les noms des param�tres ainsi que les
 * �ventuelles valeurs pr�d�finies devraient �tre cod�s en anglais, lisibles, au format CamelCase avec la premi�re lettre en majuscule.<br>
 * <br>
 * <u><b>Param�tres r�serv�s</b></u><br>
 * Les param�tres reserv�s peuvent �tre sp�cifi�s mais ne doivent pas apparaitre dans le mod�le.<br>
 * <u><i>Aide</i></u><br>
 * Le param�tre facultatif {@value #HELP} est syst�matiquement ajout� � ceux indiqu�s dans les constructeurs, exclusif avec tous les autres, et permet d'afficher une page standard d�crivant les
 * param�tres et les combinaisons autoris�es.<br>
 * <u><i></i></u><br>
 * @author Ludovic WALLE
 */
@SuppressWarnings("unused")
public abstract class Model {



	/**
	 * Les param�tres seront consid�r�s comme tous obligatoires et non r�p�titifs, ou tous facultatifs non r�p�titifs.
	 * @param description Description pour l'aide (peut �tre <code>null</code> ou vide, ne devrait pas contenir de fin de ligne).
	 * @param required Indication d'obligation de pr�sence des param�tres: tous obligatoire si <code>true</code>, tous facultatifs si <code>false</code>.
	 * @param molds Moules de chacun des param�tres.
	 */
	public Model(String description, boolean required, Mold... molds) {
		this(description, prepareCombinationsString(required, molds), molds);
	}



	/**
	 * Les param�tres seront consid�r�s comme tous obligatoires ou tous facultatifs, et aucun r�p�titif.
	 * @param description Description pour l'aide.
	 * @param combinationsString Chaine d�crivant les combinaisons de param�tres autoris�es.
	 */
	public Model(String description, String combinationsString) {
		this(description, combinationsString, prepareParameters(combinationsString));
	}



	/**
	 * @param description Description pour l'aide.
	 * @param combinationsString Chaine d�crivant les combinaisons de param�tres autoris�es.
	 * @param molds Moules de chacun des param�tres.
	 */
	public Model(String description, String combinationsString, Mold... molds) {
		ArrayList<Node> parametersNodes = new ArrayList<Node>();
		ArrayList<Node> parenthesisNodes = new ArrayList<Node>();
		Node terminalNode;
		Node removedNode;
		Set<Mold> used = new HashSet<Mold>();
		Set<Mold> unused;
		StringBuilder builder = new StringBuilder();


		this.description = description;
		/* ajouter l'option pour l'aide */
		this.combinationsString = "(" + combinationsString + " | " + Model.HELP.getName() + ")";
		/* v�rifier et stocker les param�tres */
		for (Mold parameter : molds) {
			if (moldsByName.put(parameter.getName(), parameter) != null) {
				throw new RuntimeException("Le param�tre \"" + parameter.getName() + "\" apparait plusieurs fois dans la liste des param�tres.");
			}
		}
		if (moldsByName.put(Model.HELP.getName(), Model.HELP) != null) {
			throw new RuntimeException("Le param�tre \"" + Model.HELP.getName() + "\" apparait plusieurs fois dans la liste des param�tres.");
		}
		/* construire l'automate */
		initialNode = new Node(null, null, true, false, null);
		terminalNode = new Node(parenthesisNodes, null, false, true, null);
		try {
			parselLevel(0, initialNode, terminalNode, parenthesisNodes, parametersNodes);
		} catch (StringIndexOutOfBoundsException exception) {
			throw new RuntimeException("La syntaxe de la combinaison de param�tres est incorrecte: " + this.combinationsString);
		}
		/* simplifier l'automate */
		while (!parenthesisNodes.isEmpty()) {
			removedNode = parenthesisNodes.remove(0);
			initialNode.bypass(removedNode);
			for (int j = 0; j < parenthesisNodes.size(); parenthesisNodes.get(j++).bypass(removedNode)) {
				;
			}
			for (int j = 0; j < parametersNodes.size(); parametersNodes.get(j++).bypass(removedNode)) {
				;
			}
		}
		/* v�rifier l'automate */
		checkAutomaton(initialNode, new HashMap<Node, Set<String>>());
		/* v�rifier que tous les param�tres sont utilis� dans l'automate */
		for (Node node : parametersNodes) {
			used.add(node.mold);
		}
		if (used.size() != moldsByName.size()) {
			unused = new HashSet<Mold>(moldsByName.values());
			unused.removeAll(used);
			for (Mold parameter : unused) {
				if (builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(parameter.getName());
			}
			throw new RuntimeException("Les param�tres suivants n'apparaissent pas dans la chaine d�crivant les combinaisons de param�tres autoris�es: " + builder.toString());
		}
	}



	/**
	 * V�rifie r�cursivement que la combinaison des param�tres est conforme au mod�le ou non.
	 * @param current Noeud de l'automate.
	 * @param available Param�tres en entr�e non encore utilis�s dans l'automate.
	 * @return <code>true</code> si la combinaison est conforme au mod�le, <code>false</code> sinon.
	 */
	private boolean check(Node current, Set<ParameterAndOccurrence> available) {
		Set<ParameterAndOccurrence> remaining;

		if ((remaining = remaining(current, available)) == null) {
			return false;
		} else if (current.terminal && (remaining.size() == 0)) {
			return true;
		}
		for (Node next : current.nexts) {
			if (check(next, remaining)) {
				return true;
			}
		}
		return false;
	}



	/**
	 * V�rifie que l'ensemble de param�tres indiqu� est conforme au mod�le.
	 * @param parameters Ensemble de param�tres � tester.
	 * @throws ParameterException Si l'ensemble de param�tres n'est pas conforme au mod�le.
	 */
	public void check(Parameters parameters) throws ParameterException {
//		Set<ParameterAndOccurrence> combination = new HashSet<ParameterAndOccurrence>();
//
//		/* v�rifier la validit� des param�tres */
//		for (Parameter parameter : parameters.getParameters()) {
//			/* m�moriser la pr�sence du param�tre */
//			if (valuedByName.get(parameter.getName())) {
//				/* param�tre avec valeur dans l'automate, donc monovalu� */
//				if (parameters.getCount(parameter.getName()) == 0) {
//					combination.add(new ParameterAndOccurrence(parameter, true, parameter.getValue()));
//				} else {
//					throw new ParameterException_InvalidCombination(combination.toString(), combinationsString);
//				}
//			} else {
//				combination.add(new ParameterAndOccurrence(parameter, parameter.getValue().length == 1, null));
//			}
//		}
//		/* v�rifier la combinaison */
//		if (!check(initialNode, combination)) {
//			throw new ParameterException_InvalidCombination(combination.toString(), combinationsString);
//		}
	}



	/**
	 * V�rifie un noeud de l'automate, et les noeuds suivants si n�cessaire.
	 * @param node Noeud � v�rifier.
	 * @param verified Noeuds d�ja v�rifi�s.
	 * @return L'ensemble des noeuds suivants de ce noeud y compris lui m�me.
	 */
	private Set<String> checkAutomaton(Node node, Map<Node, Set<String>> verified) {
		Set<String> nodeNexts;
		int weight;

		if ((nodeNexts = verified.get(node)) == null) {
			nodeNexts = new HashSet<String>();
			for (Node next : node.nexts) {
				nodeNexts.addAll(checkAutomaton(next, verified));
			}
			if ((node != initialNode) && !nodeNexts.add(node.mold.getName())) {
				throw new RuntimeException("Le param�tre \"" + node.mold.getName() + "\" apparait plusieurs fois dans les combinaisons de param�tres: " + combinationsString);
			}
			if (node.selfNext && (node.value != null)) {
				throw new RuntimeException("Le param�tre \"" + node.mold.getName() + "\" est valu� et r�p�titif dans les combinaisons de param�tres: " + combinationsString);
			}
			node.max = 1;
			node.min = node.terminal ? 0 : Integer.MAX_VALUE;
			weight = (node.mold == null) ? 0 : 1;
			for (Node nextNode : node.nexts) {
				if (nextNode.min < (node.min - weight)) {
					node.min = weight + nextNode.min;
				}
				if (nextNode.max > (node.max - weight)) {
					node.max = weight + nextNode.max;
				}
			}
			verified.put(node, nodeNexts);
		}
		return nodeNexts;
	}



	/**
	 * Retourne la chaine d�crivant les combinaisons de param�tres autoris�es (contient au moins l'option pour l'aide).
	 * @return La chaine d�crivant les combinaisons de param�tres autoris�es (contient au moins l'option pour l'aide).
	 */
	public String getCombinationsString() {
		return combinationsString;
	}



	/**
	 * Retourne la description pour l'aide.
	 * @return La description pour l'aide.
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * Retourne le moule de param�tre dont le nom est indiqu�.
	 * @param name Nom du param�tre.
	 * @return Le moule de param�tre dont le nom est indiqu�.
	 */
	public Mold getMold(String name) {
		return moldsByName.get(name);
	}



	/**
	 * Retourne le nombre de moules de param�tres, y compris celui de l'aide.
	 * @return Le nombre de moules de param�tres, y compris celui de l'aide.
	 */
	public int getMoldCount() {
		return moldsByName.size();
	}



	/**
	 * Retourne les moules des param�tres, y compris celui de l'aide.
	 * @return Les moules des param�tres, y compris celui de l'aide.
	 */
	public Mold[] getMolds() {
		return (Mold[]) moldsByName.values().toArray();
	}



	/**
	 * Retourne les noms des param�tres, y compris celui de l'aide.
	 * @return Les noms des param�tres, y compris celui de l'aide.
	 */
	public String[] getNames() {
		return (String[]) moldsByName.keySet().toArray();
	}



	/**
	 * Test si le mod�le autorise les param�tres issus du moule indiqu�.
	 * @param mold Moule de param�tre.
	 * @return <code>true</code> si le mod�le autorise les param�tres issus du moule indiqu�, <code>false</code> sinon.
	 */
	public boolean hasMold(Mold mold) {
		return moldsByName.containsKey(mold.getName());
	}



	/**
	 * Test si le mod�le autorise les param�tres portant le nom indiqu�.
	 * @param name Nom du param�tre.
	 * @return <code>true</code> si le mod�le autorise les param�tres portant le nom indiqu�, <code>false</code> sinon.
	 */
	public boolean hasMold(String name) {
		return moldsByName.containsKey(name);
	}



	/**
	 * Analyse r�cursivement la combinaison de param�tres pour construire l'automate. La m�thode analyse un niveau (partie d�limit�e par une paire de parenth�ses) du mod�le de contenu.
	 * @param offset Position dans {@link #combinationsString}.
	 * @param firstNode Premier noeud du niveau.
	 * @param lastNode Dernier noeud du niveau.
	 * @param parenthesis Liste des noeuds parenteses.
	 * @param elements Liste des noeuds param�tres.
	 * @return La nouvelle position dans {@link #combinationsString}.
	 */
	private int parselLevel(int offset, Node firstNode, Node lastNode, ArrayList<Node> parenthesis, ArrayList<Node> elements) {
		Node previousNode = firstNode; /* param�tre pr�c�dent */
		Node currentNode = null; /* param�tre en cours de traitement dans l'automate */
		Node firstSubNode = null; /* premier element d'un niveau inferieur */
		Node lastSubNode = null; /* dernier element d'un niveau inferieur */
		char separator = '\0'; /* s�parateur: initialement '\0', puis '|' ou ',' */
		char currentChar; /* caract�re courant */
		boolean subLevel; /* indicateur de sous niveau */
		boolean finished; /* indicateur de niveau fini */
		int initialOffset;
		String name;
		String value;
		StringBuilder builder;
		Boolean valued;

		/* passer les espaces */
		offset = skipSpaces(offset);

		/* v�rifier la pr�sence de la parenth�se ouvrante */
		if (combinationsString.charAt(offset) != '(') {
			throw new RuntimeException("Il manque une parenth�se ouvrante: " + combinationsString);
		}

		/* passer la parenth�se ouvrante */
		offset++;

		/* passer les espaces */
		offset = skipSpaces(offset);

		/* tester si le niveau n'est pas vide */
		finished = combinationsString.charAt(offset) == ')';
		if (finished) {
			offset++;
		} else {

			/* traiter jusqu'� la fin du niveau du modele de contenu */
			while (!finished) {

				if (combinationsString.charAt(offset) == '(') {
					/* traiter recursivement */
					offset = parselLevel(offset, firstSubNode = new Node(parenthesis, null, false, false, null), lastSubNode = new Node(parenthesis, null, false, false, null), parenthesis, elements);
					subLevel = true;
				} else {
					/* extraire un nom et une valeur �ventuelle (on les consid�re comme entour�s de parenth�ses) */
					firstSubNode = new Node(parenthesis, null, false, false, null);
					lastSubNode = new Node(parenthesis, null, false, false, null);
					initialOffset = offset;
					while ((offset < combinationsString.length()) && (Mold.NAMES_CHARS.indexOf(combinationsString.charAt(offset)) != -1)) {
						offset++;
					}
					if (offset <= initialOffset) {
						throw new RuntimeException("Il doit y avoir un nom ou une parenth�se apr�s '&', '|' ou '(': " + combinationsString);
					}
					name = combinationsString.substring(initialOffset, offset);
					offset = skipSpaces(offset);
					if (combinationsString.charAt(offset) != '=') {
						currentNode = new Node(elements, name, false, false, null);
						if (((valued = valuedByName.put(name, Boolean.FALSE)) != null) && valued) {
							throw new RuntimeException("Le param�tre\"" + name + "\" apparait parfois avec une valeur, parfois sans: " + combinationsString);
						}
					} else {
						offset++;
						if (combinationsString.charAt(offset) != '"') {
							throw new RuntimeException("Il manque des guillemets apr�s un �gal: " + combinationsString);
						}
						offset++;
						builder = new StringBuilder();
						while ((offset < combinationsString.length()) && (combinationsString.charAt(offset) != '"')) {
							if ((combinationsString.charAt(offset) == '\\')) {
								offset++;
								if ((offset >= combinationsString.length())) {
									throw new RuntimeException("Il manque un caract�re apr�s un '\\': " + combinationsString);
								}
							}
							builder.append(combinationsString.charAt(offset++));
						}
						if ((offset >= combinationsString.length())) {
							throw new RuntimeException("Il manque des guillemets apr�s une valeur: " + combinationsString);
						}
						offset++;
						value = builder.toString();
						if (((valued = valuedByName.put(name, Boolean.TRUE)) != null) && !valued) {
							throw new RuntimeException("Le param�tre\"" + name + "\" apparait parfois avec une valeur, parfois sans: " + combinationsString);
						}
						currentNode = new Node(elements, name, false, false, value);
					}
					firstSubNode.addNext(currentNode);
					currentNode.addNext(lastSubNode);
					subLevel = false;
				}
				previousNode.addNext(firstSubNode);
				currentNode = lastSubNode;

				/* passer les espaces */
				offset = skipSpaces(offset);

				/* �tablir les liens */
				switch (combinationsString.charAt(offset)) {
				case '?':
					firstSubNode.addNext(lastSubNode);
					offset++;
					break;
				case '+':
					if (subLevel) {
						throw new RuntimeException("Il ne doit pas y avoir de '+' derri�re une parenth�se: " + combinationsString);
					} else if (currentNode.value != null) {
						throw new RuntimeException("Il ne doit pas y avoir de '+' derri�re un �l�ment avec une valeur: " + combinationsString);
					}
					lastSubNode.addNext(firstSubNode);
					offset++;
					break;
				case '*':
					if (subLevel) {
						throw new RuntimeException("Il ne doit pas y avoir de '*' derri�re une parenth�se: " + combinationsString);
					} else if (currentNode.value != null) {
						throw new RuntimeException("Il ne doit pas y avoir de '*' derri�re un �l�ment avec une valeur: " + combinationsString);
					}
					firstSubNode.addNext(lastSubNode);
					lastSubNode.addNext(firstSubNode);
					offset++;
					break;
				}

				/* passer les espaces */
				offset = skipSpaces(offset);

				/* traiter le s�parateur ou la parenth�se fermante */
				currentChar = combinationsString.charAt(offset);
				if ((currentChar == '&') || (currentChar == '|')) {
					if (separator == '\0') {
						separator = currentChar;
					} else if (currentChar != separator) {
						throw new RuntimeException("Les '&' et les '|' ne doit pas �tre m�lang�s au m�me niveau: " + combinationsString);
					}
					if (separator == '|') {
						currentNode.addNext(lastNode);
					} else if (separator == '&') {
						previousNode = currentNode;
					}
				} else if (currentChar == ')') {
					finished = true;
				} else {
					throw new RuntimeException("Il doit y avoir '&', '|' ou ')' apr�s un nom: " + combinationsString);
				}
				offset++;

				/* passer les espaces */
				offset = skipSpaces(offset);
			}

		}

		/* lier la fin de la s�quence � la parenth�se fermante */
		if (currentNode == null) {
			firstNode.addNext(lastNode);
		} else {
			currentNode.addNext(lastNode);
		}

		return offset;
	}



	/**
	 * Passe les espaces.
	 * @param offset Position dans {@link #combinationsString}.
	 * @return La nouvelle position dans {@link #combinationsString}.
	 */
	private int skipSpaces(int offset) {
		while ((offset < combinationsString.length()) && ((combinationsString.charAt(offset) == ' ') || (combinationsString.charAt(offset) == '\n'))) {
			offset++;
		}
		return offset;
	}



	/**
	 * Affiche l'automate.
	 * @param initialNode Noeud initial de l'automate.
	 * @param parenthesisNodes Noeuds parenth�ses.
	 * @param parametersNodes Noeuds �l�ments.
	 * @return Une chaine contenant la description de l'automate.
	 * @deprecated Pour le d�boguage.
	 */
	@Deprecated private static String dump(Node initialNode, ArrayList<Node> parenthesisNodes, ArrayList<Node> parametersNodes) {
		StringBuilder dump = new StringBuilder();
		int mapIndex = 0;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		map.put(initialNode.hashCode(), mapIndex++);
		for (Node node : parametersNodes) {
			map.put(node.hashCode(), mapIndex++);
		}
		if (parenthesisNodes != null) {
			for (Node node : parenthesisNodes) {
				map.put(node.hashCode(), mapIndex++);
			}
		}
		dump.append(initialNode.dump(map));
		if (parenthesisNodes != null) {
			for (int i = 0; i < parenthesisNodes.size(); i++) {
				dump.append(parenthesisNodes.get(i).dump(map));
			}
		}
		for (int i = 0; i < parametersNodes.size(); i++) {
			dump.append(parametersNodes.get(i).dump(map));
		}
		return dump.toString();
	}



	/**
	 * Pr�pare la chaine d�crivant les combinaisons de param�tres autoris�es � partir de la description des param�tres.
	 * @param required Indication d'obligation de pr�sence des param�tres (<code>true</code>: tous obligatoires, <code>false</code>: tous facultatifs).
	 * @param parameters Description des param�tres.
	 * @return Une chaine d�crivant les combinaisons de param�tres autoris�es.
	 */
	private static String prepareCombinationsString(boolean required, Mold[] parameters) {
		StringBuilder builder = new StringBuilder();

		builder.append("(");
		for (Mold parameter : parameters) {
			if (builder.length() > 1) {
				builder.append(" & ");
			}
			builder.append(parameter.getName());
			if (!required) {
				builder.append("?");
			}
		}
		builder.append(")");
		return builder.toString();
	}



	/**
	 * Pr�pare les param�tres � partir de la chaine d�crivant les combinaisons de param�tres autoris�es.<br>
	 * Ce traitement ne fait qu'extraire les chaines de caract�res {@link AbstractMold#NAMES_CHARS} qui ne sont pas entre guillemets de la chaine d�crivant les combinaisons de param�tres.
	 * @param combinationsString Chaine d�crivant les combinaisons de param�tres autoris�es.
	 * @return Un tableau contenant les param�tres extraits.
	 */
	private static Mold[] prepareParameters(String combinationsString) {
		int iName = -1;
		Set<String> names = new HashSet<String>();
		Mold[] parameters;
		boolean ignore = false;
		int iParameter = 0;

		for (int i = 0; i < combinationsString.length(); i++) {
			if (combinationsString.charAt(i) == '"') {
				ignore = !ignore;
			} else if (combinationsString.charAt(i) == '\\') {
				i++;
			} else if (!ignore && (Mold.NAMES_CHARS.indexOf(combinationsString.charAt(i)) != -1)) {
				if (iName == -1) {
					iName = i;
				}
			} else {
				if (iName != -1) {
					names.add(combinationsString.substring(iName, i));
					iName = -1;
				}
			}
		}
		parameters = new AbstractMold[names.size()];
		for (String name : names) {
			parameters[iParameter++] = new TextMold(name, null);
		}
		return parameters;
	}



	/**
	 * V�rifie que ce noeud est compatible avec l'ensemble des param�tres indiqu� et si oui, pr�pare un nouvel ensemble sans ce param�tre.
	 * @param node Noeud � rechercher.
	 * @param available Param�tres disponibles.
	 * @return <code>null</code> si le noeud est incompatible avec les param�tres, les param�tres restant sinon.
	 */
	private static Set<ParameterAndOccurrence> remaining(Node node, Set<ParameterAndOccurrence> available) {
		Set<ParameterAndOccurrence> remaining;

		if ((available.size() < node.min) || (node.max < available.size())) {
			return null;
		} else if (node.mold == null) {
			return available;
		} else {
			for (ParameterAndOccurrence parameterAndOccurrence : available) {
				if (parameterAndOccurrence.mold.equals(node.mold) && ((node.value == null) || node.value.equals(parameterAndOccurrence.value))) {
					if ((!parameterAndOccurrence.unique && !node.selfNext)) {
						/* plusieurs valeurs, mais une seule autoris�e */
						return null;
					} else {
						remaining = new HashSet<ParameterAndOccurrence>(available);
						remaining.remove(parameterAndOccurrence);
						return remaining;
					}
				}
			}
			return null;
		}
	}



	/**
	 * Chaine d�crivant les combinaisons de param�tres autoris�es (contient au moins l'option pour l'aide).
	 */
	private final String combinationsString;



	/**
	 * Description pour l'aide (peut �tre <code>null</code> ou vide).
	 */
	private final String description;



	/**
	 * Noeud initial de l'automate.
	 */
	private final Node initialNode;



	/**
	 * Ensemble des param�tres accessibles par leur nom.
	 */
	private final SortedMap<String, Mold> moldsByName = new TreeMap<String, Mold>();



	/**
	 * Ensemble des indicateurs de valuation accessible par le nom du param�tre.
	 */
	private final SortedMap<String, Boolean> valuedByName = new TreeMap<String, Boolean>();



	/**
	 * Param�tre pour l'aide.
	 */
	public static final BooleanMold HELP = new BooleanMold("Help", "Affiche cette aide.");



	/**
	 * La classe {@link Node} correspond � un noeud (�tat) dans l'automate.<br>
	 * Les noeuds parenth�ses ont un nom <code>null</code>. Le noeud initial est un noeud parenth�se.<br>
	 * Les noeuds param�tres contiennent l'�ventuelle valeur sp�cifi�e dans la chaine de combinaison. La validit� de cette valeur pour ce param�tre est v�rifi�e.
	 * @author Ludovic WALLE
	 */
	private class Node {



		/**
		 * Cr�e un nouveau noeud.
		 * @param vector Vecteur dans lequel il faut enregistrer le noeud, ou <code>null</code> si il ne faut pas l'enregistrer.
		 * @param name Nom du param�tre, ou <code>null</code> si le noeud est un noeud parenth�se.
		 * @param initial Indique si le noeud est initial.
		 * @param terminal Indique si le noeud est terminal.
		 * @param value Valeur concern�e pour ce noeud, ou <code>null</code> si il n'y a pas de valeur pr�cis�e.
		 */
		public Node(ArrayList<Node> vector, String name, boolean initial, boolean terminal, String value) {
			if (name == null) {
				mold = null;
			} else if ((mold = moldsByName.get(name)) == null) {
				throw new RuntimeException("Le param�tre \"" + name + "\" n'est pas d�fini.");
			}
			this.initial = initial;
			this.terminal = terminal;
			if (vector != null) {
				vector.add(this);
			}
			if ((value != null) && !mold.isValid(value)) {
				throw new RuntimeException("La valeur \"" + value + "\" n'est pas valide pour le param�tre \"" + name + "\".");
			}
			this.value = value;
		}



		/**
		 * Ajoute � l'automate une transition de ce noeud vers le noeud indiqu�. Les transitions d'un noeud vers lui m�me sont indiqu�es dans {@link #selfNext}, sauf pour les noeuds parenth�ses qui
		 * sont ignor�es.
		 * @param node Noeud vers lequel une transition est possible.
		 */
		public void addNext(Node node) {
			if (node == this) {
				if ((mold == null) || (mold.getName().length() > 0)) {
					selfNext = true;
				}
			} else if (!nexts.contains(node)) {
				nexts.add(node);
			}
		}



		/**
		 * Remplace la transition de ce noeud vers le noeud indiqu� par l'ensemble des transitions partant du noeud indiqu�, et fusionne les indicateurs de noeud initial et terminal.
		 * @param node Noeud vers lequel une transition est � remplacer.
		 */
		public void bypass(Node node) {
			if ((this != node) && (nexts.remove(node))) {
				for (Node nextNode : node.nexts) {
					addNext(nextNode);
				}
				terminal |= node.terminal;
				initial |= node.initial;
			}
		}



		/**
		 * Affiche un noeud.
		 * @param map Renum�rotation des noeuds.
		 * @return Une chaine d�crivant le noeud.
		 * @deprecated Pour le d�boguage.
		 */
		@Deprecated private String dump(Map<Integer, Integer> map) {
			StringBuilder dump = new StringBuilder();

			/* indiquer le nom de l'�l�ment */
			if (mold == null) {
				dump.append("() " + ((map == null) ? hashCode() : map.get(hashCode())));
			} else {
				dump.append(mold.getName() + ((value != null) ? ("=\"" + value + "\"") : "") + " " + ((map == null) ? hashCode() : map.get(hashCode())));
			}
			/* indiquer si l'�l�ment est initial */
			if (initial) {
				dump.append(" INITIAL");
			}
			/* indiquer si l'�l�ment est terminal */
			if (terminal) {
				dump.append(" TERMINAL");
			}
			dump.append("[" + min + ".." + max + "]\n");
			/* indiquer le bouclage �ventuel */
			if (selfNext) {
				dump.append("    ");
				if ((mold == null)) {
					dump.append("()" + " " + ((map == null) ? hashCode() : map.get(hashCode())) + "\n");
				} else {
					dump.append(mold.getName() + " " + ((map == null) ? hashCode() : map.get(hashCode())) + "\n");
				}
			}
			/* indiquer les suivants */
			for (Node next : nexts) {
				dump.append("    ");
				if ((next.mold == null)) {
					dump.append("()" + ((next.value != null) ? ("=\"" + next.value + "\"") : "") + " " + ((map == null) ? next.hashCode() : map.get(next.hashCode())) + " [" + next.min + ".." + next.max + "]" + "\n");
				} else {
					dump.append(next.mold.getName() + ((next.value != null) ? ("=\"" + next.value + "\"") : "") + " " + ((map == null) ? next.hashCode() : map.get(next.hashCode())) + " [" + next.min + ".." + next.max + "]" + "\n");
				}

			}
			return dump.toString();
		}



		/**
		 * Affiche un noeud.
		 * @return Une chaine d�crivant le noeud.
		 */
		@Override public String toString() {
			return dump(null);
		}



		/**
		 * Indicateur de noeud initial.
		 */
		private boolean initial;



		/**
		 * Nombre maximal de noeuds param�tres apr�s celui ci (y compris celui-ci).
		 */
		private int max;



		/**
		 * Nombre minimal de noeuds param�tres apr�s celui ci (y compris celui-ci).
		 */
		private int min;



		/**
		 * Propri�t�s du param�tre, ou <code>null</code> si le noeud est une parenth�se. Le noeud initial est consid�r� comme un noeud parenth�se.
		 */
		private final Mold mold;



		/**
		 * Ensemble des noeuds suivants directs.
		 */
		private final Set<Node> nexts = new HashSet<Node>();



		/**
		 * Indicateur de transition r�flexive sur le noeud.
		 */
		private boolean selfNext = false;



		/**
		 * Indicateur de noeud terminal.
		 */
		private boolean terminal;



		/**
		 * Valeur, ou <code>null</code> si il n'y en pas de sp�cifi�e.
		 */
		private final String value;



	}



	/**
	 * La classe {@link ParameterAndOccurrence} associe un moule avec une indication d'occurrence, et la valeur si c'est un param�tre valu� dans l'automate. Elle est utilis�e pour la v�rification des
	 * param�tres de la servlet.<br>
	 * @author Ludovic WALLE
	 */
	private static class ParameterAndOccurrence {



		/**
		 * @param mold Moule de param�tre.
		 * @param unique Indication de param�tre non r�p�titif.
		 * @param value Valeur si c'est un param�tre valu� dans l'automate, <code>null</code> sinon.
		 */
		public ParameterAndOccurrence(Mold mold, boolean unique, String value) {
			if ((value != null) && !unique) {
				throw new RuntimeException("Incoh�rence entre les param�tres.");
			}
			this.mold = mold;
			this.unique = unique;
			this.value = value;
		}



		/**
		 * Teste si l'objet indiqu� est �gal � cet objet.<br>
		 * Il sont �gaux si tous leurs champs sont �gaux.
		 * @param object Objet � comparer.
		 * @return <code>true</code> si les objets sont �gaux, <code>false</code> sinon.
		 */
		@Override public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (object == null) {
				return false;
			}
			if (getClass() != object.getClass()) {
				return false;
			}
			ParameterAndOccurrence other = (ParameterAndOccurrence) object;
			if (mold == null) {
				if (other.mold != null) {
					return false;
				}
			} else if (!mold.equals(other.mold)) {
				return false;
			}
			if (unique != other.unique) {
				return false;
			}
			if (value == null) {
				if (other.value != null) {
					return false;
				}
			} else if (!value.equals(other.value)) {
				return false;
			}
			return true;
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((mold == null) ? 0 : mold.hashCode());
			result = (prime * result) + (unique ? 1231 : 1237);
			result = (prime * result) + ((value == null) ? 0 : value.hashCode());
			return result;
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public String toString() {
			return mold.getName() + (unique ? "" : "+") + ((value != null) ? "=\"" + value.replaceAll("\\\\", "\\\\").replaceAll("\"", "\\\"") + "\"" : "");
		}



		/**
		 * Moule de param�tre.
		 */
		private final Mold mold;



		/**
		 * Indication de param�tre non r�p�titif.
		 */
		private final boolean unique;



		/**
		 * Valeur, ou <code>null</code> pour un param�tre multivalu�.
		 */
		private final String value;



	}



}
