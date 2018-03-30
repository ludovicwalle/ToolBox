package toolbox;

import java.io.*;
import java.net.*;
import java.util.*;



/**
 * La classe {@link ClassLoadCollector} regroupe des m�thodes permettant de collecter et journaliser les noms des classes qui appellent la m�thode {@link #collect()}.<br>
 * Cette classe est utilis�e lors de l'�x�cution des applications.<br>
 * {@link #ENABLE} permet de d'activer ou d�sactiver la collecte.<br>
 * Les noms de classes collect�s sont journalis�s par envoi � une servlet de journalisation. L'URL de cette servlet est d�finie par d�faut � {@value #SERVLET_DEFAULT_URL}. Il est possible d'en
 * sp�cifier une autre dans la variable d'environnement {@value #SERVLET_URL_ENVIRONMENT_VARIABLE_NAME} ou la propri�t� syst�me du m�me nom (prioritaire). Si cette variable n'a pas de valeur, la
 * journalisation n'est pas effectu�e. Il est aussi possible d'ajouter ou de supprimer des journaliseurs applicativement par les m�thodes {@link #addLogger(String)}, {@link #removeLogger(String)} et
 * {@link #removeAllLoggers()}.<br>
 * La collecte des noms de classes peut s'effectuer seule, sans la journalisation.
 * @author Ludovic WALLE
 */
public abstract class ClassLoadCollector {



	/**
	 * Ajoute un journaliseur de noms de classes utilisant l'URL indiqu�e.<br>
	 * Si un journaliseur existe d�j� pour l'URL indiqu�e, la m�thode ne fait rien.
	 * @param url URL du journaliseur de nom de classes � supprimer.
	 * @throws IOException Si la servlet n'est pas accessible.
	 * @throws MalformedURLException Si l'URL indiqu�e est mal form�e.
	 */
	public static void addLogger(String url) throws MalformedURLException, IOException {
		synchronized (loggers) {
			if (!loggers.containsKey(url)) {
				ClassLoadLogger logger;
				loggers.put(url, logger = new ClassLoadLogger(url));
				logger.setDaemon(true);
				logger.start();
			}
		}
	}



	/**
	 * Collecte le nom de la classe appelant cette m�thode.
	 */
	public static void collect() {
		StackTraceElement[] stackTrace;
		String classNameToLog;

		stackTrace = Thread.currentThread().getStackTrace();
		/* les deux premi�res cases contiennent: */
		/* Thread.currentThread().getStackTrace()[0] == java.lang.Thread.currentThread().getStackTrace() */
		/* Thread.currentThread().getStackTrace()[1] == cette m�thode */
		/* le nom recherch� est � l'index 2 */
		stackTrace = Arrays.copyOfRange(stackTrace, 2, stackTrace.length);
		classNameToLog = stackTrace[0].getClassName();
		synchronized (loggers) {
			for (ClassLoadLogger logger : loggers.values()) {
				logger.log(stackTrace);
			}
		}
		if (!COLLECTED_CLASSES_NAMES.add(classNameToLog)) {
			synchronized (LOGGED_MORE_THAN_ONCE_CLASSES_NAMES) {
				LOGGED_MORE_THAN_ONCE_CLASSES_NAMES.add(classNameToLog);
			}
		}
	}



	/**
	 * Retourne un tableau contenant tous les noms de classes collect�s.
	 * @return Un tableau contenant tous les noms de classes collect�s.
	 */
	public static String[] getCollectedClassesNames() {
		synchronized (COLLECTED_CLASSES_NAMES) {
			return COLLECTED_CLASSES_NAMES.toArray(new String[COLLECTED_CLASSES_NAMES.size()]);
		}
	}



	/**
	 * Retourne un tableau contenant tous les noms de classes collect�s plus d'une fois.
	 * @return Un tableau contenant tous les noms de classes collect�s plus d'une fois.
	 */
	public static String[] getCollectedMoreThanOnceClassesNames() {
		synchronized (COLLECTED_CLASSES_NAMES) {
			return LOGGED_MORE_THAN_ONCE_CLASSES_NAMES.toArray(new String[LOGGED_MORE_THAN_ONCE_CLASSES_NAMES.size()]);
		}
	}



	/**
	 * Retourne le nom de la classe contenant le main initial.
	 * @return Le nom de la classe contenant le main initial.
	 */
	public static String getMainClassName() {
		return MAIN_CLASS_NAME;
	}



	/**
	 * Teste si le nom de classe indiqu� a �t� collect�.
	 * @param className Nom de classe.
	 * @return <code>true</code> si le nom de classe a �t� collect�, <code>false</code> sinon.
	 */
	public static boolean isLogged(String className) {
		return COLLECTED_CLASSES_NAMES.contains(className);
	}



	/**
	 * Teste si le nom de classe indiqu� a �t� collect� plus d'une fois.
	 * @param className Nom de classe.
	 * @return <code>true</code> si le nom de classe indiqu� a �t� collect� plus d'une fois, <code>false</code> sinon.
	 */
	public static boolean isLoggedMoreThanOnce(String className) {
		return LOGGED_MORE_THAN_ONCE_CLASSES_NAMES.contains(className);
	}



	/**
	 * Supprime le journaliseur de noms de classes dont l'URL est indiqu�e de l'ensemble des journaliseurs.
	 * @param logger Journaliseur de nom de classes � supprimer.
	 */
	private static void registerLoggerTermination(ClassLoadLogger logger) {
		try {
			synchronized (loggers) {
				try {
					if (loggers.remove(logger.getServletUrl()) != logger) {
						throw new RuntimeException("Le journaliseur de classe correspondant � l'URL " + logger.getServletUrl() + " n'est pas celui attendu.");
					}
				} catch (NullPointerException exception) {
					throw new RuntimeException(exception);
				}
			}
		} catch (NullPointerException exception) {
			// appel depuis le gestionnaire de fin de traitement
		}
	}



	/**
	 * Supprime tous les journaliseurs de noms de classes.
	 */
	public static void removeAllLoggers() {
		@SuppressWarnings("hiding")
		Set<ClassLoadLogger> loggers;

		synchronized (ClassLoadCollector.loggers) {
			loggers = new HashSet<>(ClassLoadCollector.loggers.values());
			ClassLoadCollector.loggers.clear();
		}
		removeAllLoggers(loggers);
	}



	/**
	 * Supprime tous les journaliseurs de noms de classes indiqu�s.
	 * @param loggers Journaliseurs de noms de classes.
	 */
	private static void removeAllLoggers(@SuppressWarnings("hiding") Collection<ClassLoadLogger> loggers) {
		for (ClassLoadLogger logger : loggers) {
			logger.terminate();
			try {
				logger.join();
			} catch (InterruptedException exception) {
				throw new RuntimeException(exception);
			}
		}
	}



	/**
	 * Supprime le journaliseur de noms de classes dont l'URL est indiqu�e.<br>
	 * Si aucun journaliseur ne correspond � l'URL indiqu�e, la m�thode ne fait rien.
	 * @param url URL du journaliseur de nom de classes � supprimer.
	 */
	public static void removeLogger(String url) {
		ClassLoadLogger logger = null;

		if ((logger = loggers.remove(url)) != null) {
			logger.terminate();
			try {
				logger.join();
			} catch (InterruptedException exception) {
				throw new RuntimeException(exception);
			}
		}
	}



	/**
	 * Ensemble des noms de classe collect�s.
	 */
	private final static SortedSet<String> COLLECTED_CLASSES_NAMES = new TreeSet<>();



	/**
	 * Indicateur permettant d'activer ou de d�sactiver ce module.
	 */
	private static final boolean ENABLE = false;



	/**
	 * Traitement � ex�cuter � la sortie du traitement.
	 */
	private static final Thread EXIT_HANDLER = new Thread() {



		/**
		 * {@inheritDoc}
		 */
		@Override public void run() {
			@SuppressWarnings("hiding")
			Collection<ClassLoadLogger> loggers;

			synchronized (ClassLoadCollector.loggers) {
				loggers = ClassLoadCollector.loggers.values();
				ClassLoadCollector.loggers = null;
			}
			removeAllLoggers(loggers);
		}



	};



	/**
	 * Ensemble des noms de classe collect�s plus d'une fois.
	 */
	private static final SortedSet<String> LOGGED_MORE_THAN_ONCE_CLASSES_NAMES = new TreeSet<>();



	/**
	 * Journaliseurs de noms de classes, ou <code>null</code> lors du traitement de fin.
	 */
	private static volatile Map<String, ClassLoadLogger> loggers = new HashMap<>();



	/**
	 * Nom de la classe contenant le main initial.
	 */
	private static final String MAIN_CLASS_NAME;



	/**
	 * URL par d�faut de la servlet de journalisation.
	 */
	public static final String SERVLET_DEFAULT_URL = "http://walle:51600/RefDoc/Log";



	/**
	 * Nom de la variable d'environnement permettant de sp�cifier l'URL de la servlet de journalisation.<br>
	 * Non d�finie => valeur par d�faut.<br>
	 * D�finie mais sans valeur => pas d'enregistrement. <br>
	 * D�finie avec une valeur => la valeur est l'URL de la servlet de journalisation (exemple: http://localhost:51600/RefDoc/Log).
	 */
	public static final String SERVLET_URL_ENVIRONMENT_VARIABLE_NAME = "CLASS_LOGGER_SERVLET_URL";



	/**
	 * Forcer l'initialisation de la classe {@link ClassLoadLogger}.
	 */
	static {
		@SuppressWarnings("unused") Object forClassLoading = ClassLoadLogger.class;
	}



	/**
	 * Calculer le nom de la classe contenant le main initial.
	 */
	static {
		String topClassName = null;

		if (ENABLE) {
			for (StackTraceElement[] stackTrace : Thread.getAllStackTraces().values()) {
				if ((stackTrace.length > 0) && (stackTrace[stackTrace.length - 1].getMethodName().equals("main") || stackTrace[stackTrace.length - 1].getMethodName().equals("<clinit>"))) {
					if (topClassName != null) {
						throw new RuntimeException("Plusieurs classes possibles pour le d�marrage");
					}
					topClassName = stackTrace[stackTrace.length - 1].getClassName();
				}
			}
		}
		MAIN_CLASS_NAME = topClassName;
	}



	/**
	 * Initialiser le journaliseur par d�faut.
	 */
	static {
		if (ENABLE) {
			String servletUrl;

			if (((servletUrl = System.getProperty(SERVLET_URL_ENVIRONMENT_VARIABLE_NAME)) == null) && ((servletUrl = System.getenv(SERVLET_URL_ENVIRONMENT_VARIABLE_NAME)) == null)) {
				servletUrl = SERVLET_DEFAULT_URL;
			}
			if (!servletUrl.isEmpty()) {
				try {
					addLogger(servletUrl);
				} catch (MalformedURLException exception) {
					throw new RuntimeException(exception);
				} catch (IOException exception) {
					System.err.println("Sp�cifier une autre URL dans la propri�t� syst�me ou la variable d'environnement " + SERVLET_URL_ENVIRONMENT_VARIABLE_NAME + ".");
					System.err.println("D�sactiver l'enregistrement en d�finissant une propri�t� syst�me ou une variable d'environnement " + SERVLET_URL_ENVIRONMENT_VARIABLE_NAME + " sans valeur.");
				}
			}
		}
	}



	/**
	 * Installer un traitement qui interrompt les loggers � la sortie du programme.
	 */
	static {
		if (ENABLE) {
			Runtime.getRuntime().addShutdownHook(EXIT_HANDLER);
		}
	}






	/**
	 * La classe {@link ClassLoadLogger} impl�mente un journaliseur de noms de classes asynchrone.
	 * @author Ludovic WALLE
	 */
	private static class ClassLoadLogger extends Thread {



		/**
		 * @param servletUrl URL de la servlet qui journalisera les noms de classes.
		 * @throws IOException Si la servlet n'est pas accessible.
		 * @throws MalformedURLException Si l'URL indiqu�e est mal form�e.
		 */
		public ClassLoadLogger(String servletUrl) throws MalformedURLException, IOException {
			new URL(servletUrl + "?Help").openConnection();
			this.servletUrl = servletUrl;
			this.stackTraces = new Vector<>();
		}



		/**
		 * Extrait la premi�re des piles d'ex�cution de classes dont il faut journaliser le nom, si elle existe.
		 * @return La premi�re des piles d'ex�cution de classes dont il faut journaliser le nom, ou <code>null</code> si il n'y en a pas.
		 */
		private StackTraceElement[] getNextStackTrace() {
			synchronized (stackTraces) {
				if (!stackTraces.isEmpty()) {
					return stackTraces.remove(0);
				} else {
					return null;
				}
			}
		}



		/**
		 * Retourne l'URL de la servlet de journalisation..
		 * @return L'URL de la servlet de journalisation..
		 */
		public String getServletUrl() {
			return servletUrl;
		}



		/**
		 * Journalise le nom de la classe dont a pile d'ex�cution est indiqu�e.
		 * @param stackTrace Pile d'ex�cution de la classe dont il faut journaliser le nom.
		 */
		public void log(StackTraceElement[] stackTrace) {
			synchronized (stackTraces) {
				stackTraces.add(stackTrace);
				stackTraces.notifyAll();
			}
		}



		/**
		 * {@inheritDoc}<br>
		 */
		@Override public void run() {
			String classNameToLog;
			StackTraceElement[] stackTrace;

			try {
				do {
					synchronized (stackTraces) {
						try {
							stackTraces.wait();
						} catch (InterruptedException exception) {
						}
					}
					while ((stackTrace = getNextStackTrace()) != null) {
						classNameToLog = stackTrace[0].getClassName();
						try {
							new URL(getServletUrl() + "?Message=" + URLEncoder.encode("[" + InetAddress.getLocalHost().getCanonicalHostName() + "|" + System.getProperty("user.name") + "|" + System.getProperty("user.dir") + "|" + MAIN_CLASS_NAME + "] => " + classNameToLog, "UTF-8") + "&Level=TRACE").openConnection().getInputStream();
						} catch (Throwable exception) {
							System.err.println("Impossible d'enregistrer le chargement des classes: " + exception);
							System.err.println("V�rifier que l'URL " + getServletUrl() + " est bien active ou sp�cifier une autre URL dans la propri�t� syst�me ou la variable d'environnement " + SERVLET_URL_ENVIRONMENT_VARIABLE_NAME + " ou d�sactiver l'enregistrement en d�finissant une propri�t� syst�me ou une variable d'environnement " + SERVLET_URL_ENVIRONMENT_VARIABLE_NAME + " sans valeur.");
							System.err.println("Attention, l'enregistrement du chargement des classes permet de recenser automatiquement les applications utilisant l'API. Si l'enregistrement se fait ailleurs ou est supprim�, l'application ne sera plus consid�r�e comme utilisatrice de l'API.");
							return;
						}
					}
				} while (!terminate);
			} finally {
				registerLoggerTermination(this);
			}
		}



		/**
		 * Termine le journaliseur.
		 */
		public void terminate() {
			terminate = true;
			synchronized (stackTraces) {
				stackTraces.notifyAll();
			}
		}



		/**
		 * URL de la servlet de journalisation.
		 */
		private final String servletUrl;



		/**
		 * Ensemble des piles de traces � journaliser.
		 */
		private final Vector<StackTraceElement[]> stackTraces;



		/**
		 * Indicateur de journalisation � terminer.
		 */
		private boolean terminate = false;



	}



}
