package toolbox.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import toolbox.*;



/**
 * La classe {@link CustomizedServlet} �tend la classe {@link HttpServlet} en ajoutant des fonctionnalit�s.<br>
 * <u><i>V�rification des param�tres, par m�thode HTTP</i></u><br>
 * Les constructeurs prennent en argument la description des param�tres de la servlet. Cette description peut comporter d'une part les la liste des param�tres, avec pour chacun une description, et
 * d'autre part un mod�le sp�cifiant les combinaisons de param�tres valides. La syntaxe du mod�le est inspir� de celle des mod�les de contenus des �l�ments XML, avec quelques diff�rences:
 * <ul>
 * <li>les groupes ne peuvent pas �tre r�p�titifs, mais uniquement facultatifs (pas de + ou * apr�s les parenth�ses fermantes);
 * <li>les "," sont remplac�es par des "&amp;";
 * <li>les param�tres peuvent �tre valu�s (MonParam�tre="MaValeur"), et dans ce cas une valeur doit �tre pr�cis�e partout o� ils apparaissent dans le mod�le de contenu, et ils ne peuvent pas �tre
 * r�p�titifs.
 * </ul>
 * Si la v�rification des param�tres �choue, une page d'erreur 400 est g�n�r�e et il n'y a pas d'appel � {@link #doPost(HttpServletRequest, HttpServletResponse)},
 * {@link #doGet(HttpServletRequest, HttpServletResponse)} ou autre. Cela permet de ne pas avoir � se soucier de la gestion de la coh�rence et de la validit� des param�tres dans ces m�thodes,
 * puisqu'elles ne sont appel�e que si les param�tres sont corrects.<br>
 * <br>
 * Pour des raisons de lisibilit�, les param�tres devraient �tre cod�s en anglais, lisibles, au format CamelCase avec la premi�re lettre en majuscule, ainsi que les valeurs pr�d�finies (valeurs de
 * cases � cocher, ...).<br>
 * <br>
 * <u><i>Aide</i></u><br>
 * Un param�tre facultatif {@value #HELP} est syst�matiquement ajout� � ceux indiqu�s dans les constructeurs, exclusif avec tous les autres, et permet d'afficher une page standard d�crivant les
 * param�tres de la servlet. La valeur du param�tre est ignor�e, seule sa pr�sence est prise en compte. L'encodage de la page d'aide est ISO-8859-1.<br>
 * <br>
 * Il n'y a pas de traitement particulier des exceptions.<br>
 * <br>
 * @author Ludovic WALLE
 */
public abstract class CustomizedServlet extends HttpServlet {



	/**
	 * @param textualDescription Description textuelle de la servlet ind�pendante de la m�thode.
	 * @param methodsDescriptions Descriptions des m�thodes de la servlet (peut �tre <code>null</code>).
	 */
	public CustomizedServlet(String textualDescription, MethodDescription... methodsDescriptions) {
		this.textualDescription = (textualDescription == null) ? "" : textualDescription;
		for (MethodDescription methodDescription : methodsDescriptions) {
			if (methodDescription != null) {
				for (Method method : methodDescription.getMethods()) {
					if (this.methodsDescriptionsByMethod.containsKey(method)) {
						throw new RuntimeException("La m�thode \"" + method + "\" est d�crite plusieurs fois.");
					}
					this.methodsDescriptionsByMethod.put(method, methodDescription);
				}
			}
		}
	}



	/**
	 * Cette m�thode est une alternative � {@link #doDelete(HttpServletRequest, HttpServletResponse)} mais avec des param�tres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu
	 * de {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de param�tres simplifient la r�cup�ration de fichier et la manipulation des param�tres.<br>
	 * La m�thode {@link #doDelete(HttpServletRequest, HttpServletResponse)} est surcharg�e pour faire appel � cette m�thode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.7"}.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param response Donn�es de sortie de la servlet.
	 * @throws ServletException Voir {@link #doDelete(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doDelete(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doDelete(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doDelete(request, response);
	}



	/**
	 * Impl�mentation par d�faut dans {@link CustomizedServlet}: appel de {@link #doDelete(CustomizedRequest, CustomizedResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDelete((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette m�thode est une alternative � {@link #doGet(HttpServletRequest, HttpServletResponse)} mais avec des param�tres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de param�tres simplifient la r�cup�ration de fichier et la manipulation des param�tres.<br>
	 * La m�thode {@link #doGet(HttpServletRequest, HttpServletResponse)} est surcharg�e pour faire appel � cette m�thode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.3"}.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param response Donn�es de sortie de la servlet.
	 * @throws ServletException Voir {@link #doGet(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doGet(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doGet(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}



	/**
	 * Impl�mentation par d�faut dans {@link CustomizedServlet}: appel de {@link #doGet(HttpServletRequest, HttpServletResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette m�thode est une alternative � {@link #doHead(HttpServletRequest, HttpServletResponse)} mais avec des param�tres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de param�tres simplifient la r�cup�ration de fichier et la manipulation des param�tres.<br>
	 * La m�thode {@link #doHead(HttpServletRequest, HttpServletResponse)} est surcharg�e pour faire appel � cette m�thode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.3"}.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param response Donn�es de sortie de la servlet.
	 * @throws ServletException Voir {@link #doHead(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doHead(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doHead(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doHead(request, response);
	}



	/**
	 * Impl�mentation par d�faut dans {@link CustomizedServlet}: appel de {@link #doHead(HttpServletRequest, HttpServletResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHead((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette m�thode est une alternative � {@link #doPost(HttpServletRequest, HttpServletResponse)} mais avec des param�tres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de param�tres simplifient la r�cup�ration de fichier et la manipulation des param�tres.<br>
	 * La m�thode {@link #doPost(HttpServletRequest, HttpServletResponse)} est surcharg�e pour faire appel � cette m�thode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.5"}.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param response Donn�es de sortie de la servlet.
	 * @throws ServletException Voir {@link #doPost(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doPost(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doPost(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}



	/**
	 * Impl�mentation par d�faut dans {@link CustomizedServlet}: appel de {@link #doPost(HttpServletRequest, HttpServletResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette m�thode est une alternative � {@link #doPut(HttpServletRequest, HttpServletResponse)} mais avec des param�tres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de param�tres simplifient la r�cup�ration de fichier et la manipulation des param�tres.<br>
	 * La m�thode {@link #doPut(HttpServletRequest, HttpServletResponse)} est surcharg�e pour faire appel � cette m�thode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.6"}.
	 * @param request Donn�es d'entr�e de la servlet.
	 * @param response Donn�es de sortie de la servlet.
	 * @throws ServletException Voir {@link #doPut(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doPut(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doPut(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doPut(request, response);
	}



	/**
	 * Impl�mentation par d�faut dans {@link CustomizedServlet}: appel de {@link #doPut(HttpServletRequest, HttpServletResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPut((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Page page;
		CustomizedRequest customizedRequest;
		CustomizedResponse customizedResponse;
		Checker checker;
		Method method = null;
		HttpSession session;

		request.getQueryString();
		customizedRequest = new CustomizedRequest(request);
		customizedResponse = new CustomizedResponse(response);
		try {
			if (((session = request.getSession(false)) != null) && (session.getAttribute(CUSTOMIZED_SESSION_INFORMATION) == null)) {
				session.setAttribute(CUSTOMIZED_SESSION_INFORMATION, new CustomizedSessionInformation(request));
			}
			method = Method.valueOf(customizedRequest.getMethod());
			try {
				if (!methodsDescriptionsByMethod.containsKey(method)) {
					Page.notHandledMethod(computeURL(customizedRequest), customizedRequest.getMethod(), methodsDescriptionsByMethod.keySet()).sendPage(HttpServletResponse.SC_METHOD_NOT_ALLOWED, customizedResponse);
				} else {
					checker = methodsDescriptionsByMethod.get(Method.valueOf(customizedRequest.getMethod())).getChecker();
					if ((page = checker.check(computeURL(customizedRequest), customizedRequest.getParameterMap())) != null) {
						LOGGER.warn("M�thode " + method + ", erreur dans les param�tres de l'URL: " + computeURL(customizedRequest));
						page.sendPage(HttpServletResponse.SC_BAD_REQUEST, customizedResponse);
					} else if (customizedRequest.getParameter(HELP.getName()) != null) {
						Page.help(computeURL(customizedRequest), textualDescription, new TreeSet<>(methodsDescriptionsByMethod.values())).sendPage(customizedResponse);
					} else {
						LOGGER.debug(method + ": " + computeURL(customizedRequest));
						super.service(customizedRequest, customizedResponse);
					}
				}
			} catch (Throwable exception) {
				// erreur d'ex�cution de la servlet
				LOGGER.error(computeURL(request) + " a g�n�r� une exception:", exception);
				Page.error(computeURL(customizedRequest), FormatTools.formatException(exception).split("\n")).sendPage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, customizedResponse);
				getServletContext().log(computeURL(request) + " a g�n�r� une exception:", exception);
			}
		} catch (RuntimeException exception) {
			// erreur sur Method.valueOf(...);
			LOGGER.error(computeURL(request) + " a g�n�r� une exception:", exception);
			Page.invalidMethod(computeURL(customizedRequest), customizedRequest.getMethod()).sendPage(HttpServletResponse.SC_METHOD_NOT_ALLOWED, customizedResponse);
			getServletContext().log(computeURL(request) + " a g�n�r� une exception:", exception);
		}
	}



	/**
	 * Facilit� pour d�crire les combinaisons de param�tres autoris�es correspondant aux param�tres indiqu�s connect�s avec des "&".<br>
	 * Aucun connecteur n'est ins�r� avant les fragments "?", "+" ou "*". Des parenth�ses sont ajout�es si n�cessaire.
	 * @param fragments Fragments � connecter (indicateurs de quantit� ("?", "+" ou "*"), chaines, ou param�tres).
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es correspondant aux param�tres indiqu�s connect�s avec des "&".
	 */
	protected static String and(Object... fragments) {
		return booleanConnector(" & ", fragments);
	}



	/**
	 * Retourne le fragment de chaine d�crivant les combinaisons de param�tres autoris�es correspondant aux param�tres indiqu�s connect�s avec le connecteur indiqu�.<br>
	 * Aucun connecteur n'est ins�r� avant les fragments "?", "+" ou "*". Des parenth�ses sont ajout�es si n�cessaire.
	 * @param connector Connecteur bool�en.
	 * @param fragments Fragments � connecter.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es correspondant aux param�tres indiqu�s connect�s avec le connecteur indiqu�.
	 */
	private static String booleanConnector(String connector, Object... fragments) {
		StringBuilder builder;
		String separator;
		int count = 0;

		if ((fragments == null) || (fragments.length == 0)) {
			return "";
		} else {
			for (Object fragment : fragments) {
				if ((fragment != null) && !"".equals(fragment) && !"?".equals(fragment) && !"*".equals(fragment) && !"+".equals(fragment)) {
					count++;
				}
			}
			if (count > 0) {
				builder = new StringBuilder();
				separator = "";
				for (Object fragment : fragments) {
					if (fragment != null) {
						if ("?".equals(fragment) || "*".equals(fragment) || "+".equals(fragment)) {
							builder.append((String) fragment);
						} else {
							if (fragment instanceof Parameter) {
								builder.append(separator + ((Parameter) fragment).getName());
							} else {
								builder.append(separator + (String) fragment);
							}
							separator = connector;
						}
					}
				}
				return (separator.isEmpty()) ? builder.toString() : "(" + builder.toString() + ")";
			} else {
				return "";
			}
		}
	}



	/**
	 * Calcule l'URL jusqu'au contexte.
	 * @param request Donn�es en entr�e de la servlet.
	 * @return L'URL jusqu'au contexte.
	 */
	public static String computeContextURL(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}



	/**
	 * Calcule l'URL jusqu'� la servlet.
	 * @param request Donn�es en entr�e de la servlet.
	 * @return L'URL jusqu'� la servlet.
	 */
	public static String computeServletURL(HttpServletRequest request) {
		return computeContextURL(request) + request.getServletPath();
	}



	/**
	 * Calcule l'URL compl�te.
	 * @param request Donn�es en entr�e de la servlet.
	 * @return L'URL compl�te.
	 */
	public static String computeURL(HttpServletRequest request) {
		String queryString;

		queryString = request.getQueryString();
		return computeServletURL(request) + (((queryString == null) || queryString.isEmpty()) ? "" : "?" + queryString);
	}



	/**
	 * Retourne une chaine correspondant � la chaine indiqu�e mais o� tous les caract�res <code>'</code>, <code>"</code>, <code>&amp;</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 * sup�rieur ou �gal � 128, ou strictement inf�rieur � 32 sauf TAB, CR et LF on �t� remplac�s par des r�f�rences de caract�res.<br>
	 * Cela garantit une interpr�tation correcte par tous les navigateurs, configur�s avec n'importe quel encodage qui soit un sur ensemble de l'US-ASCII (ISO-8859-1, Windows-1252, UTF-8, ...).
	 * @param string Chaine � transformer.
	 * @return Une chaine correspondant � la chaine indiqu�e mais o� tous les caract�res <code>'</code>, <code>"</code>, <code>&amp;</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 *         sup�rieur ou �gal � 128, ou strictement inf�rieur � 32 on �t� remplac�s par des r�f�rences de caract�res.
	 */
	public static String encodeForHtml(String string) {
		int stringLength;
		char stringChar;
		StringBuilder escapedBuilder;
		String escaped;

		if (string == null) {
			escaped = null;
		} else {
			escapedBuilder = new StringBuilder();
			stringLength = string.length();
			for (int i = 0; i < stringLength; i++) {
				stringChar = string.charAt(i);
				if (((stringChar >= ((char) 128)) || (((stringChar < ((char) 32)) && (stringChar != '\t') && (stringChar != '\n') && (stringChar != '\r'))) || (stringChar == '\'') || (stringChar == '"') || (stringChar == '<') || (stringChar == '>') || (stringChar == '&'))) {
					escapedBuilder.append(String.format("&#%d;", (int) stringChar));
				} else {
					escapedBuilder.append(stringChar);
				}
			}
			escaped = escapedBuilder.toString();
		}
		return escaped;
	}



	/**
	 * Retourne une chaine correspondant � la chaine indiqu�e mais o� tous les caract�res <code>'</code>, <code>"</code>, <code>\\</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 * sup�rieur ou �gal � 128, ou strictement inf�rieur � 32 sauf TAB, CR et LF on �t� remplac�s par des <code>&#92;uXXXX</code>, ou <code>null</code> si la chaine indiqu�e est <code>null</code>.<br>
	 * Cela garantit une interpr�tation correcte par tous les navigateurs, configur�s avec n'importe quel encodage qui soit un sur ensemble de l'US-ASCII (ISO-8859-1, Windows-1252, UTF-8, ...).
	 * @param string Chaine � transformer (peut �tre <code>null</code>).
	 * @return Une chaine correspondant � la chaine indiqu�e mais o� tous les caract�res <code>'</code>, <code>"</code>, <code>&amp;</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 *         sup�rieur ou �gal � 128, ou strictement inf�rieur � 32 sauf TAB, CR et LF on �t� remplac�s par des <code>&#92;uXXXX</code>, ou <code>null</code> si la chaine indiqu�e est
	 *         <code>null</code>.
	 */
	public static String encodeForJavascript(String string) {
		int stringLength;
		char stringChar;
		StringBuilder escapedBuilder;
		String escaped;

		if (string == null) {
			escaped = null;
		} else {
			escapedBuilder = new StringBuilder();
			stringLength = string.length();
			for (int i = 0; i < stringLength; i++) {
				stringChar = string.charAt(i);
				if (stringChar == '\'') {
					escapedBuilder.append("\\'");
				} else if (stringChar == '"') {
					escapedBuilder.append("\\\"");
				} else if (stringChar == '\\') {
					escapedBuilder.append("\\\\");
				} else if (stringChar == '\b') {
					escapedBuilder.append("\\b");
				} else if (stringChar == '\f') {
					escapedBuilder.append("\\f");
				} else if (stringChar == '\n') {
					escapedBuilder.append("\\n");
				} else if (stringChar == '\r') {
					escapedBuilder.append("\\r");
				} else if (stringChar == '\t') {
					escapedBuilder.append("\\t");
				} else if ((stringChar < ((char) 32)) || (stringChar >= ((char) 128))) {
					escapedBuilder.append(String.format("\\u%04X", (int) stringChar));
				} else {
					escapedBuilder.append(stringChar);
				}
			}
			escaped = escapedBuilder.toString();
		}
		return escaped;
	}



	/**
	 * Facilit� pour d�crire les combinaisons de param�tres autoris�es.<br>
	 * Retourne le fragment de chaine d�crivant les combinaisons de param�tres autoris�es correspondant au param�tre indiqu� avec comme valeur une de celles indiqu�es.
	 * @param parameter Param�tre.
	 * @param valuesAndLabels Valeurs du param�tre.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es correspondant au param�tre indiqu� avec comme valeur une de celles indiqu�es.
	 */
	protected static String is(Parameter parameter, ValueAndLabel... valuesAndLabels) {
		StringBuilder builder;
		String separator;

		if ((valuesAndLabels == null) || (valuesAndLabels.length == 0)) {
			return parameter.getName();
		} else if (valuesAndLabels.length == 1) {
			return parameter.getName() + "=\"" + valuesAndLabels[0].getValue() + "\"";
		} else {
			builder = new StringBuilder();
			separator = "";
			builder.append("(");
			for (ValueAndLabel valueAndLabel : valuesAndLabels) {
				builder.append(separator + parameter.getName() + "=\"" + valueAndLabel.getValue() + "\"");
				separator = " | ";
			}
			builder.append(")");
			return builder.toString();
		}
	}



	/**
	 * Facilit� pour ajouter un �l�ment obligatoire non r�p�titif.<br>
	 * @param parameter Param�tre � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme obligatoire non r�p�titif.
	 */
	protected static String one(Parameter parameter) {
		return parameter.getName();
	}



	/**
	 * Facilit� pour ajouter un �l�ment obligatoire non r�p�titif.<br>
	 * @param fragment Fragment � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme obligatoire non r�p�titif.
	 */
	protected static String one(String fragment) {
		return fragment;
	}



	/**
	 * Facilit� pour ajouter un �l�ment obligatoire r�p�titif.<br>
	 * @param parameter Param�tre � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme obligatoire r�p�titif.
	 */
	protected static String oneOrMore(Parameter parameter) {
		return parameter.getName() + "+";
	}



	/**
	 * Facilit� pour ajouter un �l�ment obligatoire r�p�titif.<br>
	 * @param fragment Fragment � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme obligatoire r�p�titif.
	 */
	protected static String oneOrMore(String fragment) {
		return fragment + "+";
	}



	/**
	 * Facilit� pour d�crire les combinaisons de param�tres autoris�es correspondant aux param�tres indiqu�s connect�s avec des "|".<br>
	 * Aucun connecteur n'est ins�r� avant les fragments "?", "+" ou "*". Des parenth�ses sont ajout�es si n�cessaire.
	 * @param fragments Fragments � connecter (indicateurs de quantit� ("?", "+" ou "*"), chaines, ou param�tres).
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es correspondant aux param�tres indiqu�s connect�s avec des "|".
	 */
	protected static String xor(Object... fragments) {
		return booleanConnector(" | ", fragments);
	}



	/**
	 * Facilit� pour ajouter un �l�ment facultatif r�p�titif.<br>
	 * @param parameter Param�tre � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme facultatif r�p�titif.
	 */
	protected static String zeroOrMore(Parameter parameter) {
		return parameter.getName() + "*";
	}



	/**
	 * Facilit� pour ajouter un �l�ment facultatif r�p�titif.<br>
	 * @param fragment Fragment � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme facultatif r�p�titif.
	 */
	protected static String zeroOrMore(String fragment) {
		return fragment + "*";
	}



	/**
	 * Facilit� pour ajouter un �l�ment facultatif non r�p�titif.<br>
	 * @param parameter Param�tre � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme facultatif non r�p�titif.
	 */
	protected static String zeroOrOne(Parameter parameter) {
		return parameter.getName() + "?";
	}



	/**
	 * Facilit� pour ajouter un �l�ment facultatif non r�p�titif.<br>
	 * @param fragment Fragment � quantifier.
	 * @return Le fragment de chaine d�crivant les combinaisons de param�tres autoris�es, marqu� comme facultatif non r�p�titif.
	 */
	protected static String zeroOrOne(String fragment) {
		return fragment + "?";
	}



	/**
	 * Description des m�thodes de la servlet, par m�thode.
	 */
	private final SortedMap<Method, MethodDescription> methodsDescriptionsByMethod = new TreeMap<>();



	/**
	 * Description de la servlet ind�pendante de la m�thode.
	 */
	private final String textualDescription;



	/**
	 * Nom de la propri�t� de session contenant les informations de description de la session.
	 */
	public static final String CUSTOMIZED_SESSION_INFORMATION = "SessionInformation";



	/**
	 * Param�tre pour l'aide.
	 */
	public static final BooleanParameter HELP = new BooleanParameter("Help", "Affiche l'aide sur les param�tres de la servlet.");



	/**
	 * Logger pour la servlet.
	 */
	private static final Logger LOGGER = Logger.getLogger(CustomizedServlet.class);



	/**
	 * La classe {@link CustomizedSessionInformation} contient des informations d�crivant la session.
	 * @author Ludovic WALLE
	 */
	public static class CustomizedSessionInformation {



		/**
		 * @param request
		 */
		public CustomizedSessionInformation(HttpServletRequest request) {
			@SuppressWarnings("hiding") String host;

			try {
				host = InetAddress.getByName(request.getRemoteHost()).getCanonicalHostName();
			} catch (UnknownHostException exception) {
				host = request.getRemoteHost();
			}
			this.host = host;
			this.user = request.getRemoteUser();
		}



		/**
		 * Retourne le nom de la machine distante.
		 * @return Le nom de la machine distante.
		 */
		public String getHost() {
			return host;
		}



		/**
		 * Retourne le nom de l'utilisateur.
		 * @return Le nom de l'utilisateur.
		 */
		public String getUser() {
			return user;
		}



		/**
		 * Nom de la machine distante.
		 */
		private final String host;



		/**
		 * Nom de l'utilisateur.
		 */
		private final String user;



	}



}
