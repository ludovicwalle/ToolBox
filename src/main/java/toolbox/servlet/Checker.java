package toolbox.servlet;

import java.util.*;
import java.util.Map.*;



/**
 * La classe {@link Checker} implémente des fonctionnalités permettant de vérifier les paramètres des servlets.<br>
 * La syntaxe des chaines décrivant les combinaisons de paramètres autorisées est similaire à celle des modèles de contenus XML, sauf que les paramètres sont séparés par des '&amp;' au lieu de ',' et
 * que les groupes ne peuvent pas être répétitifs (il ne peut pas y avoir de '+' ou '*' après une parenthèse fermante).<br>
 * D'autre part, une valeur peut être précisée pour les paramètres. La valeur doit être délimitée par des guillemets, et utilise '\' comme caractère d'échappement (tout caractère précédé de '\' est
 * pris littéralement). Il doit y avoir un égal entre le nom du paramètre et la valeur. Exemple: MyParameter="MyValue1". Des contraintes s'appliquent aux paramètres dont la valeur est précisée: elle
 * doit avoir une valeur précisée partout où elle apparaît dans le modèle de contenu, et ne peut plus être répétitive.<br>
 * Exemple: ((A="a1"&amp;B)|(A="a2"&amp;C))<br>
 * Aucun chemin dans l'automate entre l'élément initial et un élément final ne doit comporter plusieurs fois le même nom de paramètre, qu'il soit avec un valeur ou non.
 * @author Ludovic WALLE
 */
public class Checker {



	/**
	 * Les paramètres seront considérés comme tous obligatoires et non répétitifs.
	 * @param required Indication d'obligation de présence des paramètres: tous obligatoire si <code>true</code>, tous facultatifs si <code>false</code>.
	 * @param parameters Description des paramètres.
	 */
	public Checker(boolean required, Parameter... parameters) {
		this(prepareCombinationsString(required, parameters), parameters);
	}



	/**
	 * Les paramètres seront déduits de la chaine de combinaisons, et seront tous de la classe {@link Parameter}.
	 * @param combinationsString Chaine décrivant les combinaisons de paramètres autorisées.
	 */
	public Checker(String combinationsString) {
		this(combinationsString, prepareParameters(combinationsString));
	}



	/**
	 * @param combinationsString Chaine décrivant les combinaisons de paramètres autorisées.
	 * @param parameters Description des paramètres (nécessaire pour afficher la description du paramètre dans l'aide).
	 */
	public Checker(String combinationsString, Parameter... parameters) {
		ArrayList<Node> parametersNodes = new ArrayList<>();
		ArrayList<Node> parenthesisNodes = new ArrayList<>();
		Node terminalNode;
		Node removedNode;
		Set<Parameter> used = new HashSet<>();
		Set<Parameter> unused;
		StringBuilder builder = new StringBuilder();


		/* ajouter l'option pour l'aide */
		this.combinationsString = "(" + (combinationsString.isEmpty() ? "" : combinationsString + " | ") + CustomizedServlet.HELP.getName() + ")";
		/* vérifier et stocker les paramètres */
		for (Parameter parameter : parameters) {
			if (parametersByName.put(parameter.getName(), parameter) != null) {
				throw new RuntimeException("Le paramètre \"" + parameter.getName() + "\" apparait plusieurs fois dans la liste des paramètres.");
			}
		}
		if (parametersByName.put(CustomizedServlet.HELP.getName(), CustomizedServlet.HELP) != null) {
			throw new RuntimeException("Le paramètre \"" + CustomizedServlet.HELP.getName() + "\" apparait plusieurs fois dans la liste des paramètres.");
		}
		/* construire l'automate */
		initialNode = new Node(null, null, true, false, null);
		terminalNode = new Node(parenthesisNodes, null, false, true, null);
		try {
			parselLevel(0, initialNode, terminalNode, parenthesisNodes, parametersNodes);
		} catch (StringIndexOutOfBoundsException exception) {
			throw new RuntimeException("La syntaxe de la combinaison de paramètres est incorrecte: " + this.combinationsString);
		}
		/* simplifier l'automate */
		while (!parenthesisNodes.isEmpty()) {
			removedNode = parenthesisNodes.remove(0);
			initialNode.bypass(removedNode);
			for (int j = 0; j < parenthesisNodes.size(); parenthesisNodes.get(j++).bypass(removedNode)) {
			}
			for (int j = 0; j < parametersNodes.size(); parametersNodes.get(j++).bypass(removedNode)) {
			}
		}
		/* vérifier l'automate */
		checkAutomaton(initialNode, new HashMap<Checker.Node, Set<String>>());
		/* vérifier le référencement des paramètres énumérés dans l'automate */
		for (Node node : parametersNodes) {
			used.add(node.parameter);
		}
		if (used.size() != parametersByName.size()) {
			unused = new HashSet<>(parametersByName.values());
			unused.removeAll(used);
			for (Parameter parameter : unused) {
				if (builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(parameter.getName());
			}
			throw new RuntimeException("Les paramètres {" + builder.toString() + "} n'apparaissent pas dans la chaine décrivant les combinaisons de paramètres autorisées: " + this.combinationsString);
		}
		// System.out.println(dump(initialNode, parenthesisNodes, parametersNodes));
	}



	/**
	 * Ajoute les informations sur les paramètres de la servlet à la page d'aide indiquée.
	 * @param page Page d'aide à compléter.
	 */
	public void appendHelp(Page page) {
		page.appendSection("Paramètres", "Les paramètres valides sont :");
		page.appendListStart();
		for (Parameter parameter : parametersByName.values()) {
			parameter.appendHelp(page);
		}
		page.appendListStop();
		page.appendSection("Combinaisons", "Les combinaisons de paramètres valides sont : ", combinationsString);
	}



	/**
	 * Vérifie récursivement que la combinaison des paramètres de la servlet est conforme au modèle ou non.
	 * @param current Noeud de l'automate.
	 * @param available Paramètres en entrée non encore utilisés dans l'automate.
	 * @return <code>true</code> si la combinaison est conforme au modèle, <code>false</code> sinon.
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
	 * Teste si les paramètres de la servlet sont conformes au modèle ou non.
	 * @param url URL d'où proviennent les paramètres.
	 * @param map Valeurs des paramètres à tester.
	 * @return <code>null</code> si les paramètres sont conformes au modèle, une page HTML décrivant l'erreur sinon. sinon.
	 */
	public Page check(String url, Map<String, String[]> map) {
		Set<ParameterAndOccurrence> combination = new HashSet<>();
		Parameter parameter;

		/* vérifier la validité des paramètres */
		for (Entry<String, String[]> entry : map.entrySet()) {
			/* vérifier que le paramètre est autorisé */
			if ((parameter = parametersByName.get(entry.getKey())) == null) {
				return Page.unknownParameter(url, entry.getKey(), parametersByName.values().toArray(new Parameter[parametersByName.size()]));
			}
			/* vérifier les valeurs du paramètre */
			for (String value : entry.getValue()) {
				if (!parameter.check(value)) {
					return Page.invalidParameter(url, parameter, value);
				}
			}
			/* mémoriser la présence du paramètre */
			if (valuedByName.get(entry.getKey())) {
				/* paramètre avec valeur dans l'automate, donc monovalué */
				if (entry.getValue().length == 1) {
					combination.add(new ParameterAndOccurrence(parameter, true, entry.getValue()[0]));
				} else {
					return Page.invalidCombination(url, combination.toString(), combinationsString);
				}
			} else {
				combination.add(new ParameterAndOccurrence(parameter, entry.getValue().length == 1, null));
			}
		}
		/* vérifier la combinaison */
		if (check(initialNode, combination)) {
			return null;
		} else {
			return Page.invalidCombination(url, combination.toString(), combinationsString);
		}
	}



	/**
	 * Vérifie un noeud de l'automate, et les noeuds suivants si nécessaire.
	 * @param node Noeud à vérifier.
	 * @param verified Noeuds déjà vérifiés.
	 * @return L'ensemble des noeuds suivants de ce noeud y compris lui même.
	 */
	private Set<String> checkAutomaton(Node node, Map<Node, Set<String>> verified) {
		Set<String> nodeNexts;
		int weight;

		if ((nodeNexts = verified.get(node)) == null) {
			nodeNexts = new HashSet<>();
			for (Node next : node.nexts) {
				nodeNexts.addAll(checkAutomaton(next, verified));
			}
			if ((node != initialNode) && !nodeNexts.add(node.parameter.getName())) {
				throw new RuntimeException("Le paramètre \"" + node.parameter.getName() + "\" apparait plusieurs fois dans les combinaisons de paramètres: " + combinationsString);
			}
			if (node.selfNext && (node.value != null)) {
				throw new RuntimeException("Le paramètre \"" + node.parameter.getName() + "\" est valué et répétitif dans les combinaisons de paramètres: " + combinationsString);
			}
			node.max = 1;
			node.min = node.terminal ? 0 : Integer.MAX_VALUE;
			weight = (node.parameter == null) ? 0 : 1;
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
	 * Analyse récursivement la combinaison de paramètres pour construire l'automate. La méthode analyse un niveau (partie délimitée par une paire de parenthèses) du modèle de contenu.
	 * @param offset Position dans {@link #combinationsString}.
	 * @param firstNode Premier noeud du niveau.
	 * @param lastNode Dernier noeud du niveau.
	 * @param parenthesis Liste des noeuds parentheses.
	 * @param elements Liste des noeuds paramètres.
	 * @return La nouvelle position dans {@link #combinationsString}.
	 */
	private int parselLevel(int offset, Node firstNode, Node lastNode, ArrayList<Node> parenthesis, ArrayList<Node> elements) {
		Node previousNode = firstNode; /* paramètre précédent */
		Node currentNode = null; /* paramètre en cours de traitement dans l'automate */
		Node firstSubNode = null; /* premier element d'un niveau inférieur */
		Node lastSubNode = null; /* dernier element d'un niveau inférieur */
		char separator = '\0'; /* séparateur: initialement '\0', puis '|' ou ',' */
		char currentChar; /* caractère courant */
		boolean subLevel; /* indicateur de sous niveau */
		boolean finished; /* indicateur de niveau fini */
		int initialOffset;
		String name;
		String value;
		StringBuilder builder;
		Boolean valued;

		/* passer les espaces */
		offset = skipSpaces(offset);

		/* vérifier la présence de la parenthèse ouvrante */
		if (combinationsString.charAt(offset) != '(') {
			throw new RuntimeException("Il manque une parenthèse ouvrante: " + combinationsString);
		}

		/* passer la parenthèse ouvrante */
		offset++;

		/* passer les espaces */
		offset = skipSpaces(offset);

		/* tester si le niveau n'est pas vide */
		finished = combinationsString.charAt(offset) == ')';
		if (finished) {
			offset++;
		} else {

			/* traiter jusqu'à la fin du niveau du modèle de contenu */
			while (!finished) {

				if (combinationsString.charAt(offset) == '(') {
					/* traiter récursivement */
					offset = parselLevel(offset, firstSubNode = new Node(parenthesis, null, false, false, null), lastSubNode = new Node(parenthesis, null, false, false, null), parenthesis, elements);
					subLevel = true;
				} else {
					/* extraire un nom et une valeur éventuelle (on les considère comme entourés de parenthèses) */
					firstSubNode = new Node(parenthesis, null, false, false, null);
					lastSubNode = new Node(parenthesis, null, false, false, null);
					initialOffset = offset;
					while ((offset < combinationsString.length()) && (Parameter.NAME_CHARACTERS.indexOf(combinationsString.charAt(offset)) != -1)) {
						offset++;
					}
					if (offset <= initialOffset) {
						throw new RuntimeException("Il doit y avoir un nom ou une parenthèse après '&', '|' ou '(': " + combinationsString);
					}
					name = combinationsString.substring(initialOffset, offset);
					offset = skipSpaces(offset);
					if (combinationsString.charAt(offset) != '=') {
						currentNode = new Node(elements, name, false, false, null);
						if (((valued = valuedByName.put(name, Boolean.FALSE)) != null) && valued) {
							throw new RuntimeException("Le paramètre\"" + name + "\" apparait parfois avec une valeur, parfois sans: " + combinationsString);
						}
					} else {
						offset++;
						if (combinationsString.charAt(offset) != '"') {
							throw new RuntimeException("Il manque des guillemets après un égal: " + combinationsString);
						}
						offset++;
						builder = new StringBuilder();
						while ((offset < combinationsString.length()) && (combinationsString.charAt(offset) != '"')) {
							if ((combinationsString.charAt(offset) == '\\')) {
								offset++;
								if ((offset >= combinationsString.length())) {
									throw new RuntimeException("Il manque un caractère après un '\\': " + combinationsString);
								}
							}
							builder.append(combinationsString.charAt(offset++));
						}
						if ((offset >= combinationsString.length())) {
							throw new RuntimeException("Il manque des guillemets après une valeur: " + combinationsString);
						}
						offset++;
						value = builder.toString();
						if (((valued = valuedByName.put(name, Boolean.TRUE)) != null) && !valued) {
							throw new RuntimeException("Le paramètre\"" + name + "\" apparait parfois avec une valeur, parfois sans: " + combinationsString);
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

				/* établir les liens */
				switch (combinationsString.charAt(offset)) {
				case '?':
					firstSubNode.addNext(lastSubNode);
					offset++;
					break;
				case '+':
					if (subLevel) {
						throw new RuntimeException("Il ne doit pas y avoir de '+' derrière une parenthèse: " + combinationsString);
					} else if (currentNode.value != null) {
						throw new RuntimeException("Il ne doit pas y avoir de '+' derrière un élément avec une valeur: " + combinationsString);
					}
					lastSubNode.addNext(firstSubNode);
					offset++;
					break;
				case '*':
					if (subLevel) {
						throw new RuntimeException("Il ne doit pas y avoir de '*' derrière une parenthèse: " + combinationsString);
					} else if (currentNode.value != null) {
						throw new RuntimeException("Il ne doit pas y avoir de '*' derrière un élément avec une valeur: " + combinationsString);
					}
					firstSubNode.addNext(lastSubNode);
					lastSubNode.addNext(firstSubNode);
					offset++;
					break;
				}

				/* passer les espaces */
				offset = skipSpaces(offset);

				/* traiter le séparateur ou la parenthèse fermante */
				currentChar = combinationsString.charAt(offset);
				if ((currentChar == '&') || (currentChar == '|')) {
					if (separator == '\0') {
						separator = currentChar;
					} else if (currentChar != separator) {
						throw new RuntimeException("Les '&' et les '|' ne doit pas être mélangés au même niveau: " + combinationsString);
					}
					if (separator == '|') {
						currentNode.addNext(lastNode);
					} else if (separator == '&') {
						previousNode = currentNode;
					}
				} else if (currentChar == ')') {
					finished = true;
				} else {
					throw new RuntimeException("Il doit y avoir '&', '|' ou ')' après un nom: " + combinationsString);
				}
				offset++;

				/* passer les espaces */
				offset = skipSpaces(offset);
			}

		}

		/* lier la fin de la séquence à la parenthèse fermante */
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
	 * @param parenthesisNodes Noeuds parenthèses.
	 * @param parametersNodes Noeuds éléments.
	 * @return Une chaine contenant la description de l'automate.
	 * @deprecated Pour le déboguage.
	 */
	@Deprecated @SuppressWarnings({"unused"}) private static String dump(Node initialNode, ArrayList<Node> parenthesisNodes, ArrayList<Node> parametersNodes) {
		StringBuilder dump = new StringBuilder();
		int mapIndex = 0;
		Map<Integer, Integer> map = new HashMap<>();

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
	 * Prépare la chaine décrivant les combinaisons de paramètres autorisées à partir de la description des paramètres.
	 * @param required Indication d'obligation de présence des paramètres (<code>true</code>: tous obligatoires, <code>false</code>: tous facultatifs).
	 * @param parameters Description des paramètres.
	 * @return Une chaine décrivant les combinaisons de paramètres autorisées.
	 */
	private static String prepareCombinationsString(boolean required, Parameter[] parameters) {
		StringBuilder builder = new StringBuilder();

		builder.append("(");
		for (Parameter parameter : parameters) {
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
	 * Prépare les paramètres à partir de la chaine décrivant les combinaisons de paramètres autorisées.<br>
	 * Ce traitement ne fait qu'extraire les chaines de caractères {@link Parameter#NAME_PATTERN} qui ne sont pas entre guillemets de la chaine décrivant les combinaisons de paramètres.
	 * @param combinationsString Chaine décrivant les combinaisons de paramètres autorisées.
	 * @return Un tableau contenant les paramètres extraits.
	 */
	private static Parameter[] prepareParameters(String combinationsString) {
		int iName = -1;
		Set<String> names = new HashSet<>();
		Parameter[] parameters;
		boolean ignore = false;
		int iParameter = 0;

		for (int i = 0; i < combinationsString.length(); i++) {
			if (combinationsString.charAt(i) == '"') {
				ignore = !ignore;
			} else if (combinationsString.charAt(i) == '\\') {
				i++;
			} else if (!ignore && (Parameter.NAME_CHARACTERS.indexOf(combinationsString.charAt(i)) != -1)) {
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
		parameters = new Parameter[names.size()];
		for (String name : names) {
			parameters[iParameter++] = new TextParameter(name, null);
		}
		return parameters;
	}



	/**
	 * Vérifie que ce noeud est compatible avec l'ensemble des paramètres indiqué et si oui, prépare un nouvel ensemble sans ce paramètre.
	 * @param node Noeud à rechercher.
	 * @param available Paramètres disponibles.
	 * @return <code>null</code> si le noeud est incompatible avec les paramètres, les paramètres restant sinon.
	 */
	private static Set<ParameterAndOccurrence> remaining(Node node, Set<ParameterAndOccurrence> available) {
		Set<ParameterAndOccurrence> remaining;

		if ((available.size() < node.min) || (node.max < available.size())) {
			return null;
		} else if (node.parameter == null) {
			return available;
		} else {
			for (ParameterAndOccurrence parameterAndOccurrence : available) {
				if (parameterAndOccurrence.parameter.equals(node.parameter) && ((node.value == null) || node.value.equals(parameterAndOccurrence.value))) {
					if ((!parameterAndOccurrence.unique && !node.selfNext)) {
						/* plusieurs valeurs, mais une seule autorisée */
						return null;
					} else {
						remaining = new HashSet<>(available);
						remaining.remove(parameterAndOccurrence);
						return remaining;
					}
				}
			}
			return null;
		}
	}



	/**
	 * Chaine décrivant les combinaisons de paramètres autorisées.
	 */
	private final String combinationsString;



	/**
	 * Noeud initial de l'automate.
	 */
	private final Node initialNode;



	/**
	 * Ensemble des paramètres accessibles par leur nom.
	 */
	private final SortedMap<String, Parameter> parametersByName = new TreeMap<>();



	/**
	 * Ensemble des indicateurs de valuation accessible par le nom du paramètre.
	 */
	private final SortedMap<String, Boolean> valuedByName = new TreeMap<>();



	/**
	 * La classe {@link Node} correspond à un noeud (état) dans l'automate.<br>
	 * Les noeuds parenthèses ont un nom <code>null</code>. Le noeud initial est un noeud parenthèse.<br>
	 * Les noeuds paramètres contiennent l'éventuelle valeur spécifiée dans la chaine de combinaison. La validité de cette valeur pour ce paramètre est vérifiée.
	 * @author Ludovic WALLE
	 */
	private class Node {



		/**
		 * Crée un nouveau noeud.
		 * @param vector Vecteur dans lequel il faut enregistrer le noeud, ou <code>null</code> si il ne faut pas l'enregistrer.
		 * @param name Nom du paramètre, ou <code>null</code> si le noeud est un noeud parenthèse.
		 * @param initial Indique si le noeud est initial.
		 * @param terminal Indique si le noeud est terminal.
		 * @param value Valeur concernée pour ce noeud, ou <code>null</code> si il n'y a pas de valeur précisée.
		 */
		public Node(ArrayList<Node> vector, String name, boolean initial, boolean terminal, String value) {
			if (name == null) {
				parameter = null;
			} else if ((parameter = parametersByName.get(name)) == null) {
				throw new RuntimeException("Le paramètre \"" + name + "\" n'est pas défini.");
			}
			this.initial = initial;
			this.terminal = terminal;
			if (vector != null) {
				vector.add(this);
			}
			if ((value != null) && !parameter.check(value)) {
				throw new RuntimeException("La valeur \"" + value + "\" n'est pas valide pour le paramètre \"" + name + "\".");
			}
			this.value = value;
		}



		/**
		 * Ajoute à l'automate une transition de ce noeud vers le noeud indiqué. Les transitions d'un noeud vers lui même sont indiquées dans {@link #selfNext}, sauf pour les noeuds parenthèses qui
		 * sont ignorées.
		 * @param node Noeud vers lequel une transition est possible.
		 */
		public void addNext(Node node) {
			if (node == this) {
				if ((parameter == null) || (parameter.getName().length() > 0)) {
					selfNext = true;
				}
			} else if (!nexts.contains(node)) {
				nexts.add(node);
			}
		}



		/**
		 * Remplace la transition de ce noeud vers le noeud indiqué par l'ensemble des transitions partant du noeud indiqué, et fusionne les indicateurs de noeud initial et terminal.
		 * @param node Noeud vers lequel une transition est à remplacer.
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
		 * @param map Renumérotation des noeuds.
		 * @return Une chaine décrivant le noeud.
		 */
		public String dump(Map<Integer, Integer> map) {
			StringBuilder dump = new StringBuilder();

			/* indiquer le nom de l'élément */
			if (parameter == null) {
				dump.append("() " + ((map == null) ? hashCode() : map.get(hashCode())));
			} else {
				dump.append(parameter.getName() + ((value != null) ? ("=\"" + value + "\"") : "") + " " + ((map == null) ? hashCode() : map.get(hashCode())));
			}
			/* indiquer si l'élément est initial */
			if (initial) {
				dump.append(" INITIAL");
			}
			/* indiquer si l'élément est terminal */
			if (terminal) {
				dump.append(" TERMINAL");
			}
			dump.append("[" + min + ".." + max + "]\n");
			/* indiquer le bouclage éventuel */
			if (selfNext) {
				dump.append("    ");
				if ((parameter == null)) {
					dump.append("()" + " " + ((map == null) ? hashCode() : map.get(hashCode())) + "\n");
				} else {
					dump.append(parameter.getName() + " " + ((map == null) ? hashCode() : map.get(hashCode())) + "\n");
				}
			}
			/* indiquer les suivants */
			for (Node next : nexts) {
				dump.append("    ");
				if ((next.parameter == null)) {
					dump.append("()" + ((next.value != null) ? ("=\"" + next.value + "\"") : "") + " " + ((map == null) ? next.hashCode() : map.get(next.hashCode())) + " [" + next.min + ".." + next.max + "]" + "\n");
				} else {
					dump.append(next.parameter.getName() + ((next.value != null) ? ("=\"" + next.value + "\"") : "") + " " + ((map == null) ? next.hashCode() : map.get(next.hashCode())) + " [" + next.min + ".." + next.max + "]" + "\n");
				}

			}
			return dump.toString();
		}



		/**
		 * Affiche un noeud.
		 * @return Une chaine décrivant le noeud.
		 */
		@Override public String toString() {
			return dump(null);
		}



		/**
		 * Indicateur de noeud initial.
		 */
		private boolean initial;



		/**
		 * Nombre maximal de noeuds paramètres après celui ci (y compris celui-ci).
		 */
		private int max;



		/**
		 * Nombre minimal de noeuds paramètres après celui ci (y compris celui-ci).
		 */
		private int min;



		/**
		 * Ensemble des noeuds suivants directement.
		 */
		private final Set<Node> nexts = new HashSet<>();



		/**
		 * Propriétés du paramètre, ou <code>null</code> si le noeud est une parenthèse. Le noeud initial est considéré comme un noeud parenthèse.
		 */
		private final Parameter parameter;



		/**
		 * Indicateur de transition réflexive sur le noeud.
		 */
		private boolean selfNext = false;



		/**
		 * Indicateur de noeud terminal.
		 */
		private boolean terminal;



		/**
		 * Valeur, ou <code>null</code> si il n'y en pas de spécifiée.
		 */
		private final String value;



	}



	/**
	 * La classe {@link ParameterAndOccurrence} associe un paramètre avec une indication d'occurrence, et la valeur si c'est un paramètre valué dans l'automate. Elle est utilisée pour la vérification
	 * des paramètres de la servlet.<br>
	 * @author Ludovic WALLE
	 */
	private static class ParameterAndOccurrence {



		/**
		 * @param parameter Paramètre.
		 * @param unique Indication de paramètre non répétitif.
		 * @param value Valeur si c'est un paramètre valué dans l'automate, <code>null</code> sinon.
		 */
		public ParameterAndOccurrence(Parameter parameter, boolean unique, String value) {
			if ((value != null) && !unique) {
				throw new RuntimeException("Incohérence entre les paramètres.");
			}
			this.parameter = parameter;
			this.unique = unique;
			this.value = value;
		}



		/**
		 * Teste si l'objet indiqué est égal à cet objet.<br>
		 * Il sont égaux si tous leurs champs sont égaux.
		 * @param object Objet à comparer.
		 * @return <code>true</code> si les objets sont égaux, <code>false</code> sinon.
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
			if (parameter == null) {
				if (other.parameter != null) {
					return false;
				}
			} else if (!parameter.equals(other.parameter)) {
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
			result = (prime * result) + ((parameter == null) ? 0 : parameter.hashCode());
			result = (prime * result) + (unique ? 1231 : 1237);
			result = (prime * result) + ((value == null) ? 0 : value.hashCode());
			return result;
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public String toString() {
			return parameter.getName() + (unique ? "" : "+") + ((value != null) ? "=\"" + value.replaceAll("\\\\", "\\\\").replaceAll("\"", "\\\"") + "\"" : "");
		}



		/**
		 * Paramètre.
		 */
		private final Parameter parameter;



		/**
		 * Indication de paramètre non répétitif.
		 */
		private final boolean unique;



		/**
		 * Valeur, ou <code>null</code> pour un paramètre multivalué.
		 */
		private final String value;



	}



}
