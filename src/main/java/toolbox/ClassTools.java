package toolbox;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;
import java.util.regex.*;
import java.util.zip.*;

import com.simontuffs.onejar.*;



/**
 * La classe {@link ClassTools} impl�mente des m�thodes relatives � l'emplacement de stockage des classes et � leurs d�pendances. Ces m�thodes fonctionennent de fa�on statique, par introspection. Il
 * n'est donc pas n�cessaire que le code analys� fasse partie de l'application.<br>
 * Une localisation est exprim�e par un tableau de noms de fichiers, correspondant aux imbrications r�cursives de fichiers zip (dont jar).
 * @author Ludovic WALLE
 */
public abstract class ClassTools {



	/**
	 * Collecte r�cursivement les noms et localisations des classes d'un jar.
	 * @param locationsByNames Noms des classes collect�es (cl�: nom de la classe; valeur: localisation).
	 * @param stream Zip de d�part pour la collecte.
	 * @param location Localisation du jar.
	 * @throws IOException
	 */
	private static void collectClasses(SortedMap<String, Location> locationsByNames, ZipInputStream stream, String... location) throws IOException {
		String name;
		Matcher matcher;
		ZipEntry entry;

		while ((entry = stream.getNextEntry()) != null) {
			if ((matcher = CLASS_FILENAMES_PATTERN.matcher(entry.getName())).matches()) {
				name = computeClassName(matcher.group(1));
				if (!locationsByNames.containsKey(name)) {
					locationsByNames.put(name, new Location(location));
				}
			} else if ((matcher = ONEJAR_JARNAMES_PATTERN.matcher(entry.getName())).matches()) {
				/* onejar, on suppose que pour trouver une classe, on parcourt les jars dans l'ordre o� ils apparaissent dans le onejar, et qu'on prend la premi�re occurrence rencontr�e */
				collectClasses(locationsByNames, new ZipInputStream(stream), ArraysTools.append(location, entry.getName()));
			}
		}
	}



	/**
	 * Collecte r�cursivement les noms et localisations des classes d'un r�pertoire de classes.
	 * @param locationsByNames Noms des classes collect�es (cl�: nom de la classe; valeur: localisation).
	 * @param file Point de d�part pour la collecte.
	 * @param location Nom du r�pertoire de classe.
	 * @throws IOException
	 */
	private static void collectClasses(String location, File file, Map<String, Location> locationsByNames) throws IOException {
		Matcher matcher;

		if (file.isFile()) {
			if ((matcher = CLASS_FILENAMES_PATTERN.matcher(file.getCanonicalPath().substring(location.length() + 1))).matches()) {
				locationsByNames.put(computeClassName(matcher.group(1)), new Location(location));
			}
		} else {
			for (File subFile : file.listFiles()) {
				collectClasses(location, subFile, locationsByNames);
			}
		}
	}



	/**
	 * Collecte r�cursivement les contenus correspondants � la localisation indiqu�e.
	 * @param contents Contenus extraits.
	 * @param stream Flux compress�.
	 * @param location Localisation.
	 * @throws IOException
	 */
	@SuppressWarnings("resource") private static void collectContent(Set<byte[]> contents, ZipInputStream stream, String... location) throws IOException {
		ZipEntry entry;

		if ((location == null) || (location.length == 0)) {
			contents.add(StreamTools.readFully(stream));
		} else if (location.length == 1) {
			while ((entry = stream.getNextEntry()) != null) {
				if (entry.getName().equals(location[0])) {
					contents.add(StreamTools.readFully(stream));
				}
			}
		} else {
			while ((entry = stream.getNextEntry()) != null) {
				if (entry.getName().equals(location[0])) {
					collectContent(contents, new ZipInputStream(stream), Arrays.copyOfRange(location, 1, location.length));
				}
			}
		}
	}



	/**
	 * Collecte r�cursivement les chemins et localisations des fichiers et r�pertoires d'un r�pertoire de classes.
	 * @param location Localisation (chemin du r�pertoire de classes).
	 * @param locationsOfPathes Chemins collect�es avec leur localisation (cl�: chemin de fichier ou de r�pertoire; valeur: localisations de ce chemin, dans l'ordre de la collecte).
	 * @param parentPath Chemin relatif du r�pertoire parent, ou <code>null</code> si c'est la racine de la localisation.
	 * @param file Point de d�part pour la collecte.
	 * @throws IOException
	 */
	private static void collectPathes(Location location, Map<String, Vector<Location>> locationsOfPathes, String parentPath, File file) throws IOException {
		String path;
		Vector<Location> locations;
		ZipInputStream stream;

		if (parentPath == null) {
			path = "";
		} else {
			path = parentPath + file.getName() + (file.isDirectory() ? "/" : "");
			if ((locations = locationsOfPathes.get(path)) == null) {
				locationsOfPathes.put(path, locations = new Vector<>());
			}
			locations.add(location);
		}
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				collectPathes(location, locationsOfPathes, path, subFile);
			}
		} else {
			stream = new ZipInputStream(new FileInputStream(file));
			collectPathes(location, locationsOfPathes, stream);
			stream.close();
		}
	}



	/**
	 * Collecte r�cursivement les chemins et localisations des classes d'un zip suppos�. Si ce n'est pas un zip, la m�thode ne fait rien.
	 * @param location Localisation du zip suppos�.
	 * @param locationsOfPathes Chemins collect�es avec leur localisation (cl�: chemin de fichier ou de r�pertoire; valeur: localisations de ce chemin, dans l'ordre de la collecte).
	 * @param stream Zip.
	 * @throws IOException
	 */
	private static void collectPathes(Location location, Map<String, Vector<Location>> locationsOfPathes, ZipInputStream stream) throws IOException {
		Vector<Location> locations;
		ZipEntry entry;

		try {
			while ((entry = stream.getNextEntry()) != null) {
				if ((locations = locationsOfPathes.get(entry.getName())) == null) {
					locationsOfPathes.put(entry.getName(), locations = new Vector<>());
				}
				locations.add(location);
				if (!entry.isDirectory()) {
					collectPathes(location.append(entry.getName()), locationsOfPathes, new ZipInputStream(stream));
				}
			}
		} catch (ZipException exception) {
			/* ce n'est pas un zip => ignorer */
		}
	}



	/**
	 * Collecte r�cursivement quelles sont parmi les classes indiqu�es celles qui sont des classes racines, c'est � dire sans anc�tre faisant partie des classes indiqu�es.
	 * @param currentClass Classe � tester.
	 * @param classesLocationsByClassesNames Noms des classes � consid�rer (cl�: nom de la classe; valeur: localisation).
	 * @param topClassesLocationsByClassesNames Noms des classes racines (cl�: nom de la classe; valeur: localisation).
	 * @return <code>true</code> si une classe racine a �t� trouv�e, <code>false</code> sinon.
	 */
	private static boolean collectTopClasses(Class<?> currentClass, Map<String, Location> classesLocationsByClassesNames, Map<String, Location> topClassesLocationsByClassesNames) {
		boolean top = false;
		String className;

		if (currentClass == null) {
			return false;
		} else if ((currentClass.getSuperclass() == null) && (currentClass.getEnclosingClass() == null) && (currentClass.getDeclaringClass() == null)) {
			className = currentClass.getCanonicalName();
			if (classesLocationsByClassesNames.containsKey(className)) {
				topClassesLocationsByClassesNames.put(className, classesLocationsByClassesNames.get(className));
				return true;
			} else {
				return false;
			}
		} else {
			top |= collectTopClasses(currentClass.getSuperclass(), classesLocationsByClassesNames, topClassesLocationsByClassesNames);
			top |= collectTopClasses(currentClass.getEnclosingClass(), classesLocationsByClassesNames, topClassesLocationsByClassesNames);
			top |= collectTopClasses(currentClass.getDeclaringClass(), classesLocationsByClassesNames, topClassesLocationsByClassesNames);
			className = currentClass.getCanonicalName();
			if (!top && classesLocationsByClassesNames.containsKey(className)) {
				topClassesLocationsByClassesNames.put(className, classesLocationsByClassesNames.get(className));
				return true;
			} else {
				return top;
			}
		}
	}



	/**
	 * Calcule le nom de la classe correspondant au nom de fichier de classe indiqu�.
	 * @param filePath Chemin du fichier de classe, relatif au r�pertoire de classe.
	 * @return Le nom de la classe.
	 */
	private static String computeClassName(String filePath) {
		return filePath.replaceAll("\\.class\\z", "").replaceAll("/", ".").replaceAll("\\\\", ".");
	}



	/**
	 * Recense les noms et localisations (celle indiqu�e et les �ventuelles autres) des classes � la localisation indiqu�e.
	 * @param location Localisation des classes � recenser (r�pertoire de classes ou zip (dont jar) (une valeur par niveau d'imbrication)).
	 * @return Un ensemble ordonn� de noms de classes avec leur localisation.
	 * @throws IOException
	 */
	public static SortedMap<String, Location> getClassesNamesIn(String... location) throws IOException {
		SortedMap<String, Location> locationsByClassesNames = new TreeMap<>();
		File file;
		ZipInputStream stream;

		if ((location.length == 1) && ((file = new File(location[0])).isDirectory())) {
			collectClasses(file.getCanonicalPath(), file, locationsByClassesNames);
		} else {
			stream = new ZipInputStream(new FileInputStream(location[0]));
			collectClasses(locationsByClassesNames, stream, location);
			stream.close();
		}
		return locationsByClassesNames;
	}



	/**
	 * Extrait les contenus dont les chemins correspondent au mod�le indiqu� de toutes les localisations du classpath.
	 * @param pathPattern Mod�les des chemins.
	 * @param location Localisation (<code>null</code> pour toute localisation).
	 * @return Les contenus extraits, jamais <code>null</code>.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static byte[][] getContents(Pattern pathPattern, Location location) throws URISyntaxException, IOException {
		Set<byte[]> contents = new HashSet<>();
		ZipInputStream stream;
		String path;

		for (Entry<String, Vector<Location>> entry : getPathes().entrySet()) {
			path = entry.getKey();
			if (!path.endsWith("/") && pathPattern.matcher(path).matches()) {
				for (Location _location : entry.getValue()) {
					if ((location == null) || location.equals(_location)) {
						if (_location.isIncludedZip()) {
							stream = new ZipInputStream(new FileInputStream(_location.getFirstName()));
							collectContent(contents, stream, _location.getNextNames(entry.getKey()));
							stream.close();
						} else {
							contents.add(FileTools.readFully(_location.getFirstName() + entry.getKey()));
						}
					}
				}
			}
		}
		return contents.toArray(new byte[contents.size()][]);
	}



	/**
	 * Extrait les contenus dont les chemins correspondent au mod�le indiqu� de toutes les localisations du classpath.
	 * @param pathPattern Mod�les des chemins.
	 * @param location Localisation (rien ou <code>null</code> pour toute localisation).
	 * @return Les contenus extraits, jamais <code>null</code>.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static byte[][] getContents(Pattern pathPattern, String... location) throws URISyntaxException, IOException {
		return getContents(pathPattern, new Location(location));
	}



	/**
	 * Recherche de quelle URI provient la classe indiqu�e dans la JVM en cours d'ex�cution.
	 * @param aClass Classe.
	 * @return La localisation d'o� provient la classe indiqu�e (une chaine vide si la classe est charg�e lors de l'initialisation de la JVM), ou <code>null</code> si on ne sait pas.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static Location getLocation(Class<?> aClass) throws URISyntaxException, IOException {
		Map<String, Location> locationsByClassesNames;
		String className;
		Location location;

		className = aClass.getName();
		if (aClass.getClassLoader() == null) {
			if ((locationsByClassesNames = CLASSES_BY_LOCATION.get(BOOT_CLASSLOADER_LOCATION)) == null) {
				CLASSES_BY_LOCATION.put(BOOT_CLASSLOADER_LOCATION, locationsByClassesNames = new TreeMap<>());
			}
			locationsByClassesNames.put(className, BOOT_CLASSLOADER_LOCATION);
			return BOOT_CLASSLOADER_LOCATION;
		} else if (aClass.getClassLoader() instanceof URLClassLoader) {
			for (URL url : ((URLClassLoader) aClass.getClassLoader()).getURLs()) {
				location = new Location(new File(url.toURI().getPath()).getCanonicalPath());
				if ((locationsByClassesNames = CLASSES_BY_LOCATION.get(location)) == null) {
					CLASSES_BY_LOCATION.put(location, locationsByClassesNames = getClassesNamesIn(location.getNames()));
				}
				if (locationsByClassesNames.containsKey(className)) {
					return locationsByClassesNames.get(className);
				}
			}
		} else if (aClass.getClassLoader() instanceof JarClassLoader) {
			location = new Location(new File(Boot.getMyJarPath()).getCanonicalPath());
			if ((locationsByClassesNames = CLASSES_BY_LOCATION.get(location)) == null) {
				CLASSES_BY_LOCATION.put(location, locationsByClassesNames = getClassesNamesIn(location.getNames()));
			}
			return locationsByClassesNames.get(className);
		} else {
			throw new RuntimeException(aClass.getClassLoader().getClass().getCanonicalName());
		}
		return null;
	}



	/**
	 * Recherche la localisation de la ressource indiqu�e avec le classpath.
	 * @param pattern
	 * @return La localisation d'o� provient la classe indiqu�e (une chaine vide si la classe est charg�e lors de l'initialisation de la JVM), ou <code>null</code> si on ne sait pas.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static Map<String, Vector<Location>> getLocations(Pattern pattern) throws URISyntaxException, IOException {
		Map<String, Vector<Location>> locationsOfPathes = new TreeMap<>();

		for (Entry<String, Vector<Location>> entry : getPathes().entrySet()) {
			if (pattern.matcher(entry.getKey()).matches()) {
				locationsOfPathes.put(entry.getKey(), entry.getValue());
			}
		}
		return locationsOfPathes;
	}



	/**
	 * Recherche les localisations du fichier ou r�pertoire indiqu� avec le classpath.<br>
	 * @param path Chemin, de r�pertoire si il se termine par un "/", de fichier sinon.
	 * @return Les localisations du fichier ou r�pertoire indiqu�, jamais <code>null</code>.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static Vector<Location> getLocations(String path) throws URISyntaxException, IOException {
		return getPathes().get(path);
	}



	/**
	 * Retourne un tableau contenant le nom des classes r�f�renc�es dans les piles d'appels de tous les threads en cours d'ex�cution dont le nom de la m�thode est <code>main</code>.
	 * @return Un tableau du nom des classes contenant un main.
	 */
	public static String[] getMainClassesNames() {
		String[] classesNames = new String[0];
		boolean found;
		String[] newClassesNames;

		for (StackTraceElement[] stackTrace : Thread.getAllStackTraces().values()) {
			for (StackTraceElement stackTraceElement : stackTrace) {
				if (stackTraceElement.getMethodName().equals("main")) {
					found = false;
					for (String className : classesNames) {
						if (className.equals(stackTraceElement.getClassName())) {
						}
					}
					if (!found) {
						newClassesNames = new String[classesNames.length + 1];
						System.arraycopy(classesNames, 0, newClassesNames, 0, classesNames.length);
						newClassesNames[classesNames.length] = stackTraceElement.getClassName();
						classesNames = newClassesNames;
					}
				}
			}
		}
		return classesNames;
	}



	/**
	 * Retourne une chaine contenant le nom des classes r�f�renc�es dans les piles d'appels de tous les threads en cours d'ex�cution dont le nom de la m�thode est <code>main</code>, concat�n�s et
	 * s�par�s avec le s�parateur indiqu�.
	 * @param separator S�parateur � mettre entre les noms des classes.
	 * @return Une chaine contenant le nom des classes r�f�renc�es dans les piles d'appels de tous les threads en cours d'ex�cution dont le nom de la m�thode est <code>main</code>, concat�n�s et
	 *         s�par�s avec le s�parateur indiqu�.
	 */
	public static String getMainClassesNames(String separator) {
		StringBuilder builder = new StringBuilder();

		for (String string : getMainClassesNames()) {
			builder.append(((builder.length() > 0) ? separator : "") + string);
		}
		return builder.toString();
	}



	/**
	 * Retourne le nom de l'unique classe r�f�renc�e dans les piles d'appels de tous les threads en cours d'ex�cution dont le nom de la m�thode est <code>main</code>, ou <code>null</code> si il n'y en
	 * a pas exactement 1.
	 * @return Le nom de l'unique classe r�f�renc�e dans les piles d'appels de tous les threads en cours d'ex�cution dont le nom de la m�thode est <code>main</code>, ou <code>null</code> si il n'y en
	 *         a pas exactement 1.
	 */
	public static String getMainClassName() {
		String[] mainClassesNames;

		mainClassesNames = getMainClassesNames();
		if (mainClassesNames.length != 1) {
			return null;
		} else {
			return mainClassesNames[0];
		}
	}



	/**
	 * Retourne les chemins de tous les fichiers ou r�pertoires accessibles � partir du classpath, directement ou dans des fichiers zip (dont jar), �ventuellement imbriqu�s, avec leur localisations.
	 * <br>
	 * Les localisations sont stock�es dans l'ordre du parcours du classpath et des entr�es des zip. Les s�parateurs utilis�s dans les chemins sont des "/". Les chemins des r�pertoires se terminent
	 * par un "/".
	 * @return Les chemins de tous les fichiers ou r�pertoires accessibles � partir du classpath (cl�: chemin; valeur: vecteur de localisations).
	 * @throws IOException
	 */
	public static Map<String, Vector<Location>> getPathes() throws IOException {
		Map<String, Vector<Location>> locationsOfPathes = new TreeMap<>();
		File file;

		for (String rootLocation : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
			file = new File(rootLocation);
			if (file.exists()) {
				collectPathes(new Location(file.getCanonicalPath() + (file.isDirectory() ? "/" : "")), locationsOfPathes, null, new File(rootLocation));
			}
		}
		return locationsOfPathes;
	}



	/**
	 * Recense les noms des classes racines � la localisation indiqu�e.<br>
	 * Les classes racines (au sens des d�pendances de classes) sont les classes qui sont d�finies � une localisation donn�e, mais ne d�pendent d'aucune autre classe d�finie � cette localisation. Les
	 * classes racines ne seront donc charg�es que si elles sont explicitement r�f�renc�es par ailleurs. Toutes les classes non racines seront charg�es indirectement lors du chargement d'une (au
	 * moins) des classes racine. Donc si toutes les classes racines sont charg�es, alors toutes les classes de la localisation seront aussi charg�es indirectement.
	 * @param location Localisation des classes � recenser (r�pertoire de classes ou jar (une valeur par niveau d'imbrication)).
	 * @return Un ensemble ordonn� de noms de classes, avec leur localisation.
	 * @throws IOException
	 */
	public static SortedMap<String, Location> getTopClassesNames(String... location) throws IOException {
		SortedMap<String, Location> topURIClassesNames = new TreeMap<>();
		Map<String, Location> allClassesNamesInURI;

		try {
			allClassesNamesInURI = getClassesNamesIn(location);
			for (String uriClassName : allClassesNamesInURI.keySet()) {
				collectTopClasses(Class.forName(uriClassName), allClassesNamesInURI, topURIClassesNames);
			}
		} catch (ClassNotFoundException exception) {
			throw new RuntimeException(exception);
		}
		return topURIClassesNames;
	}



	/**
	 * Localisation pour les classes charg�es lors de l'initialisation de la JVM.
	 */
	public static final Location BOOT_CLASSLOADER_LOCATION = new Location();



	/**
	 * Mod�le de syntaxe des noms de fichiers de classes.
	 */
	private static final Pattern CLASS_FILENAMES_PATTERN = Pattern.compile("(.*)\\.class");



	/**
	 * Cache des noms de classes par localisation.
	 */
	private static final Map<Location, Map<String, Location>> CLASSES_BY_LOCATION = new TreeMap<>();



	/**
	 * Mod�le de syntaxe des noms de jars dans un onejar.
	 */
	private static final Pattern ONEJAR_JARNAMES_PATTERN = Pattern.compile("(?:lib|main)/(.*)\\.jar");



	/**
	 * La classe {@link Location} contient une localisation de fichier ou de r�pertoire du syst�me de fichier ou emball� dans un fichier zip (dont jar).
	 * @author Ludovic WALLE
	 */
	public static class Location implements Comparable<Location> {



		/**
		 * @param names Composants de la localisation (ne doit pas �tre <code>null</code> ni contenir de <code>null</code>).
		 */
		public Location(String... names) {
			for (String string : names) {
				if (string == null) {
					throw new NullPointerException();
				}
			}
			this.names = names;
		}



		/**
		 * Cr�e une nouvelle localisation concat�nant les noms de cette localisation et ceux indiqu�s.
		 * @param names Composants de la localisation � ajouter (ne doit pas �tre <code>null</code> ni contenir de <code>null</code>).
		 * @return La nouvelle localisation.
		 */
		public Location append(@SuppressWarnings("hiding") String... names) {
			for (String string : names) {
				if (string == null) {
					throw new NullPointerException();
				}
			}
			return new Location(ArraysTools.concat(this.names, names));
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public int compareTo(Location other) {
			int length;
			int compare;

			length = (names.length < other.names.length) ? names.length : other.names.length;
			for (int i = 0; i < length; i++) {
				if ((compare = names[i].compareTo(other.names[i])) != 0) {
					return compare;
				}
			}
			return names.length - other.names.length;
		}



		/**
		 * {@inheritDoc}
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
			Location other = (Location) object;
			if (!Arrays.equals(names, other.names)) {
				return false;
			}
			return true;
		}



		/**
		 * Retourne le premier nom, ou <code>null</code> si il n'existe pas.
		 * @return Le premier nom, ou <code>null</code> si il n'existe pas.
		 */
		public String getFirstName() {
			return (names.length > 0) ? names[0] : null;
		}



		/**
		 * Retourne les noms.
		 * @return Les noms.
		 */
		public String[] getNames() {
			return names.clone();
		}



		/**
		 * Retourne les noms sauf le premier, concat�n� avec les noms indiqu�s.
		 * @param names Noms � concat�ner.
		 * @return Les noms sauf le premier, concat�n� avec les noms indiqu�s.
		 */
		public String[] getNextNames(@SuppressWarnings("hiding") String... names) {
			for (String string : names) {
				if (string == null) {
					throw new NullPointerException();
				}
			}
			if (this.names.length <= 1) {
				return names.clone();
			} else if (names.length == 0) {
				return Arrays.copyOfRange(this.names, 1, this.names.length);
			} else {
				return ArraysTools.append(Arrays.copyOfRange(this.names, 1, this.names.length), names);
			}
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + Arrays.hashCode(names);
			return result;
		}



		/**
		 * Teste si la localisation d�signe un r�pertoire.
		 * @return <code>true</code> si la localisation d�signe un r�pertoire, <code>false</code> sinon.
		 */
		public boolean isDirectory() {
			return (names.length == 1) && names[0].endsWith("/");
		}



		/**
		 * Teste si la localisation est vide (ne contient aucun nom).
		 * @return <code>true</code> si la localisation est vide, <code>false</code> sinon.
		 */
		public boolean isEmpty() {
			return names.length == 0;
		}



		/**
		 * Teste si la localisation d�signe un fichier (suppos� �tre un zip).
		 * @return <code>true</code> si la localisation d�signe un fichier (suppos� �tre un zip), <code>false</code> sinon.
		 */
		public boolean isFirstLevelZip() {
			return (names.length == 1) && !names[0].endsWith("/");
		}



		/**
		 * Teste si la localisation d�signe un fichier dans un fichier zip.
		 * @return <code>true</code> si la localisation d�signe un fichier dans un fichier zip, <code>false</code> sinon.
		 */
		public boolean isIncludedZip() {
			return (names.length > 1) || !names[0].endsWith("/");
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public String toString() {
			return FormatTools.formatList("", " \u2192 ", "", names);
		}



		/**
		 * Composants de la localisation.
		 */
		private final String[] names;



	}


}
