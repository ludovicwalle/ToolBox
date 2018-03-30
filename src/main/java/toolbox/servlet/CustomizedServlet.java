package toolbox.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import toolbox.*;



/**
 * La classe {@link CustomizedServlet} étend la classe {@link HttpServlet} en ajoutant des fonctionnalités.<br>
 * <u><i>Vérification des paramètres, par méthode HTTP</i></u><br>
 * Les constructeurs prennent en argument la description des paramètres de la servlet. Cette description peut comporter d'une part les la liste des paramètres, avec pour chacun une description, et
 * d'autre part un modèle spécifiant les combinaisons de paramètres valides. La syntaxe du modèle est inspiré de celle des modèles de contenus des éléments XML, avec quelques différences:
 * <ul>
 * <li>les groupes ne peuvent pas être répétitifs, mais uniquement facultatifs (pas de + ou * après les parenthèses fermantes);
 * <li>les "," sont remplacées par des "&amp;";
 * <li>les paramètres peuvent être valués (MonParamètre="MaValeur"), et dans ce cas une valeur doit être précisée partout où ils apparaissent dans le modèle de contenu, et ils ne peuvent pas être
 * répétitifs.
 * </ul>
 * Si la vérification des paramètres échoue, une page d'erreur 400 est générée et il n'y a pas d'appel à {@link #doPost(HttpServletRequest, HttpServletResponse)},
 * {@link #doGet(HttpServletRequest, HttpServletResponse)} ou autre. Cela permet de ne pas avoir à se soucier de la gestion de la cohérence et de la validité des paramètres dans ces méthodes,
 * puisqu'elles ne sont appelée que si les paramètres sont corrects.<br>
 * <br>
 * Pour des raisons de lisibilité, les paramètres devraient être codés en anglais, lisibles, au format CamelCase avec la première lettre en majuscule, ainsi que les valeurs prédéfinies (valeurs de
 * cases à cocher, ...).<br>
 * <br>
 * <u><i>Aide</i></u><br>
 * Un paramètre facultatif {@value #HELP} est systématiquement ajouté à ceux indiqués dans les constructeurs, exclusif avec tous les autres, et permet d'afficher une page standard décrivant les
 * paramètres de la servlet. La valeur du paramètre est ignorée, seule sa présence est prise en compte. L'encodage de la page d'aide est ISO-8859-1.<br>
 * <br>
 * Il n'y a pas de traitement particulier des exceptions.<br>
 * <br>
 * @author Ludovic WALLE
 */
public abstract class CustomizedServlet extends HttpServlet {



	/**
	 * @param textualDescription Description textuelle de la servlet indépendante de la méthode.
	 * @param methodsDescriptions Descriptions des méthodes de la servlet (peut être <code>null</code>).
	 */
	public CustomizedServlet(String textualDescription, MethodDescription... methodsDescriptions) {
		this.textualDescription = (textualDescription == null) ? "" : textualDescription;
		for (MethodDescription methodDescription : methodsDescriptions) {
			if (methodDescription != null) {
				for (Method method : methodDescription.getMethods()) {
					if (this.methodsDescriptionsByMethod.containsKey(method)) {
						throw new RuntimeException("La méthode \"" + method + "\" est décrite plusieurs fois.");
					}
					this.methodsDescriptionsByMethod.put(method, methodDescription);
				}
			}
		}
	}



	/**
	 * Cette méthode est une alternative à {@link #doDelete(HttpServletRequest, HttpServletResponse)} mais avec des paramètres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu
	 * de {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de paramètres simplifient la récupération de fichier et la manipulation des paramètres.<br>
	 * La méthode {@link #doDelete(HttpServletRequest, HttpServletResponse)} est surchargée pour faire appel à cette méthode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.7"}.
	 * @param request Données d'entrée de la servlet.
	 * @param response Données de sortie de la servlet.
	 * @throws ServletException Voir {@link #doDelete(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doDelete(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doDelete(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doDelete(request, response);
	}



	/**
	 * Implémentation par défaut dans {@link CustomizedServlet}: appel de {@link #doDelete(CustomizedRequest, CustomizedResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDelete((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette méthode est une alternative à {@link #doGet(HttpServletRequest, HttpServletResponse)} mais avec des paramètres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de paramètres simplifient la récupération de fichier et la manipulation des paramètres.<br>
	 * La méthode {@link #doGet(HttpServletRequest, HttpServletResponse)} est surchargée pour faire appel à cette méthode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.3"}.
	 * @param request Données d'entrée de la servlet.
	 * @param response Données de sortie de la servlet.
	 * @throws ServletException Voir {@link #doGet(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doGet(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doGet(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}



	/**
	 * Implémentation par défaut dans {@link CustomizedServlet}: appel de {@link #doGet(HttpServletRequest, HttpServletResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette méthode est une alternative à {@link #doHead(HttpServletRequest, HttpServletResponse)} mais avec des paramètres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de paramètres simplifient la récupération de fichier et la manipulation des paramètres.<br>
	 * La méthode {@link #doHead(HttpServletRequest, HttpServletResponse)} est surchargée pour faire appel à cette méthode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.3"}.
	 * @param request Données d'entrée de la servlet.
	 * @param response Données de sortie de la servlet.
	 * @throws ServletException Voir {@link #doHead(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doHead(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doHead(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doHead(request, response);
	}



	/**
	 * Implémentation par défaut dans {@link CustomizedServlet}: appel de {@link #doHead(HttpServletRequest, HttpServletResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHead((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette méthode est une alternative à {@link #doPost(HttpServletRequest, HttpServletResponse)} mais avec des paramètres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de paramètres simplifient la récupération de fichier et la manipulation des paramètres.<br>
	 * La méthode {@link #doPost(HttpServletRequest, HttpServletResponse)} est surchargée pour faire appel à cette méthode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.5"}.
	 * @param request Données d'entrée de la servlet.
	 * @param response Données de sortie de la servlet.
	 * @throws ServletException Voir {@link #doPost(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doPost(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doPost(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}



	/**
	 * Implémentation par défaut dans {@link CustomizedServlet}: appel de {@link #doPost(HttpServletRequest, HttpServletResponse)}.<br>
	 * {@inheritDoc}
	 */
	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost((CustomizedRequest) request, (CustomizedResponse) response);
	}



	/**
	 * Cette méthode est une alternative à {@link #doPut(HttpServletRequest, HttpServletResponse)} mais avec des paramètres de type {@link CustomizedRequest} et {@link CustomizedResponse} au lieu de
	 * {@link HttpServletRequest} et {@link HttpServletResponse}. Ces types de paramètres simplifient la récupération de fichier et la manipulation des paramètres.<br>
	 * La méthode {@link #doPut(HttpServletRequest, HttpServletResponse)} est surchargée pour faire appel à cette méthode.<br>
	 * Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.6"}.
	 * @param request Données d'entrée de la servlet.
	 * @param response Données de sortie de la servlet.
	 * @throws ServletException Voir {@link #doPut(HttpServletRequest, HttpServletResponse)}.
	 * @throws IOException Voir {@link #doPut(HttpServletRequest, HttpServletResponse)}.
	 */
	protected void doPut(CustomizedRequest request, CustomizedResponse response) throws ServletException, IOException {
		super.doPut(request, response);
	}



	/**
	 * Implémentation par défaut dans {@link CustomizedServlet}: appel de {@link #doPut(HttpServletRequest, HttpServletResponse)}.<br>
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
						LOGGER.warn("Méthode " + method + ", erreur dans les paramètres de l'URL: " + computeURL(customizedRequest));
						page.sendPage(HttpServletResponse.SC_BAD_REQUEST, customizedResponse);
					} else if (customizedRequest.getParameter(HELP.getName()) != null) {
						Page.help(computeURL(customizedRequest), textualDescription, new TreeSet<>(methodsDescriptionsByMethod.values())).sendPage(customizedResponse);
					} else {
						LOGGER.debug(method + ": " + computeURL(customizedRequest));
						super.service(customizedRequest, customizedResponse);
					}
				}
			} catch (Throwable exception) {
				// erreur d'exécution de la servlet
				LOGGER.error(computeURL(request) + " a généré une exception:", exception);
				Page.error(computeURL(customizedRequest), FormatTools.formatException(exception).split("\n")).sendPage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, customizedResponse);
				getServletContext().log(computeURL(request) + " a généré une exception:", exception);
			}
		} catch (RuntimeException exception) {
			// erreur sur Method.valueOf(...);
			LOGGER.error(computeURL(request) + " a généré une exception:", exception);
			Page.invalidMethod(computeURL(customizedRequest), customizedRequest.getMethod()).sendPage(HttpServletResponse.SC_METHOD_NOT_ALLOWED, customizedResponse);
			getServletContext().log(computeURL(request) + " a généré une exception:", exception);
		}
	}



	/**
	 * Facilité pour décrire les combinaisons de paramètres autorisées correspondant aux paramètres indiqués connectés avec des "&".<br>
	 * Aucun connecteur n'est inséré avant les fragments "?", "+" ou "*". Des parenthèses sont ajoutées si nécessaire.
	 * @param fragments Fragments à connecter (indicateurs de quantité ("?", "+" ou "*"), chaines, ou paramètres).
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées correspondant aux paramètres indiqués connectés avec des "&".
	 */
	protected static String and(Object... fragments) {
		return booleanConnector(" & ", fragments);
	}



	/**
	 * Retourne le fragment de chaine décrivant les combinaisons de paramètres autorisées correspondant aux paramètres indiqués connectés avec le connecteur indiqué.<br>
	 * Aucun connecteur n'est inséré avant les fragments "?", "+" ou "*". Des parenthèses sont ajoutées si nécessaire.
	 * @param connector Connecteur booléen.
	 * @param fragments Fragments à connecter.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées correspondant aux paramètres indiqués connectés avec le connecteur indiqué.
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
	 * @param request Données en entrée de la servlet.
	 * @return L'URL jusqu'au contexte.
	 */
	public static String computeContextURL(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}



	/**
	 * Calcule l'URL jusqu'à la servlet.
	 * @param request Données en entrée de la servlet.
	 * @return L'URL jusqu'à la servlet.
	 */
	public static String computeServletURL(HttpServletRequest request) {
		return computeContextURL(request) + request.getServletPath();
	}



	/**
	 * Calcule l'URL complète.
	 * @param request Données en entrée de la servlet.
	 * @return L'URL complète.
	 */
	public static String computeURL(HttpServletRequest request) {
		String queryString;

		queryString = request.getQueryString();
		return computeServletURL(request) + (((queryString == null) || queryString.isEmpty()) ? "" : "?" + queryString);
	}



	/**
	 * Retourne une chaine correspondant à la chaine indiquée mais où tous les caractères <code>'</code>, <code>"</code>, <code>&amp;</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 * supérieur ou égal à 128, ou strictement inférieur à 32 sauf TAB, CR et LF on été remplacés par des références de caractères.<br>
	 * Cela garantit une interprétation correcte par tous les navigateurs, configurés avec n'importe quel encodage qui soit un sur ensemble de l'US-ASCII (ISO-8859-1, Windows-1252, UTF-8, ...).
	 * @param string Chaine à transformer.
	 * @return Une chaine correspondant à la chaine indiquée mais où tous les caractères <code>'</code>, <code>"</code>, <code>&amp;</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 *         supérieur ou égal à 128, ou strictement inférieur à 32 on été remplacés par des références de caractères.
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
	 * Retourne une chaine correspondant à la chaine indiquée mais où tous les caractères <code>'</code>, <code>"</code>, <code>\\</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 * supérieur ou égal à 128, ou strictement inférieur à 32 sauf TAB, CR et LF on été remplacés par des <code>&#92;uXXXX</code>, ou <code>null</code> si la chaine indiquée est <code>null</code>.<br>
	 * Cela garantit une interprétation correcte par tous les navigateurs, configurés avec n'importe quel encodage qui soit un sur ensemble de l'US-ASCII (ISO-8859-1, Windows-1252, UTF-8, ...).
	 * @param string Chaine à transformer (peut être <code>null</code>).
	 * @return Une chaine correspondant à la chaine indiquée mais où tous les caractères <code>'</code>, <code>"</code>, <code>&amp;</code>, <code>></code>, <code>&lt;</code>, ou dont le code est
	 *         supérieur ou égal à 128, ou strictement inférieur à 32 sauf TAB, CR et LF on été remplacés par des <code>&#92;uXXXX</code>, ou <code>null</code> si la chaine indiquée est
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
	 * Facilité pour décrire les combinaisons de paramètres autorisées.<br>
	 * Retourne le fragment de chaine décrivant les combinaisons de paramètres autorisées correspondant au paramètre indiqué avec comme valeur une de celles indiquées.
	 * @param parameter Paramètre.
	 * @param valuesAndLabels Valeurs du paramètre.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées correspondant au paramètre indiqué avec comme valeur une de celles indiquées.
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
	 * Facilité pour ajouter un élément obligatoire non répétitif.<br>
	 * @param parameter Paramètre à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme obligatoire non répétitif.
	 */
	protected static String one(Parameter parameter) {
		return parameter.getName();
	}



	/**
	 * Facilité pour ajouter un élément obligatoire non répétitif.<br>
	 * @param fragment Fragment à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme obligatoire non répétitif.
	 */
	protected static String one(String fragment) {
		return fragment;
	}



	/**
	 * Facilité pour ajouter un élément obligatoire répétitif.<br>
	 * @param parameter Paramètre à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme obligatoire répétitif.
	 */
	protected static String oneOrMore(Parameter parameter) {
		return parameter.getName() + "+";
	}



	/**
	 * Facilité pour ajouter un élément obligatoire répétitif.<br>
	 * @param fragment Fragment à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme obligatoire répétitif.
	 */
	protected static String oneOrMore(String fragment) {
		return fragment + "+";
	}



	/**
	 * Facilité pour décrire les combinaisons de paramètres autorisées correspondant aux paramètres indiqués connectés avec des "|".<br>
	 * Aucun connecteur n'est inséré avant les fragments "?", "+" ou "*". Des parenthèses sont ajoutées si nécessaire.
	 * @param fragments Fragments à connecter (indicateurs de quantité ("?", "+" ou "*"), chaines, ou paramètres).
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées correspondant aux paramètres indiqués connectés avec des "|".
	 */
	protected static String xor(Object... fragments) {
		return booleanConnector(" | ", fragments);
	}



	/**
	 * Facilité pour ajouter un élément facultatif répétitif.<br>
	 * @param parameter Paramètre à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme facultatif répétitif.
	 */
	protected static String zeroOrMore(Parameter parameter) {
		return parameter.getName() + "*";
	}



	/**
	 * Facilité pour ajouter un élément facultatif répétitif.<br>
	 * @param fragment Fragment à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme facultatif répétitif.
	 */
	protected static String zeroOrMore(String fragment) {
		return fragment + "*";
	}



	/**
	 * Facilité pour ajouter un élément facultatif non répétitif.<br>
	 * @param parameter Paramètre à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme facultatif non répétitif.
	 */
	protected static String zeroOrOne(Parameter parameter) {
		return parameter.getName() + "?";
	}



	/**
	 * Facilité pour ajouter un élément facultatif non répétitif.<br>
	 * @param fragment Fragment à quantifier.
	 * @return Le fragment de chaine décrivant les combinaisons de paramètres autorisées, marqué comme facultatif non répétitif.
	 */
	protected static String zeroOrOne(String fragment) {
		return fragment + "?";
	}



	/**
	 * Description des méthodes de la servlet, par méthode.
	 */
	private final SortedMap<Method, MethodDescription> methodsDescriptionsByMethod = new TreeMap<>();



	/**
	 * Description de la servlet indépendante de la méthode.
	 */
	private final String textualDescription;



	/**
	 * Nom de la propriété de session contenant les informations de description de la session.
	 */
	public static final String CUSTOMIZED_SESSION_INFORMATION = "SessionInformation";



	/**
	 * Paramètre pour l'aide.
	 */
	public static final BooleanParameter HELP = new BooleanParameter("Help", "Affiche l'aide sur les paramètres de la servlet.");



	/**
	 * Logger pour la servlet.
	 */
	private static final Logger LOGGER = Logger.getLogger(CustomizedServlet.class);



	/**
	 * La classe {@link CustomizedSessionInformation} contient des informations décrivant la session.
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
