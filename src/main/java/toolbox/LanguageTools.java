package toolbox;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import com.cybozu.labs.langdetect.*;



/**
 * La classe {@link LanguageTools} regroupe des outils relatifs aux langues.
 * @author Ludovic WALLE
 */
public abstract class LanguageTools {



	/**
	 * Retourne le code langue le plus probable du texte de la chaine indiquée, ou <code>null</code> si il est impossible à déterminer.
	 * @param string Chaine.
	 * @return Le code langue le plus probable du texte de la chaine indiquée, ou <code>null</code> si il est impossible à déterminer.
	 * @throws LangDetectException
	 */
	public static String guessLanguage(String string) throws LangDetectException {
		return guessLanguage(string, null);
	}



	/**
	 * Retourne le code langue le plus probable du texte de la chaine indiquée, ou <code>null</code> si il est impossible à déterminer.
	 * @param string Chaine.
	 * @param probabilities Probabilités attendue des langues (peut être <code>null</code>).
	 * @return Le code langue le plus probable du texte de la chaine indiquée, ou <code>null</code> si il est impossible à déterminer.
	 * @throws LangDetectException
	 */
	public static String guessLanguage(String string, HashMap<String, Double> probabilities) throws LangDetectException {
		Detector detector;

		if ((string == null) || string.isEmpty()) {
			return null;
		} else {
			detector = DetectorFactory.create();
			detector.append(string);
			if (probabilities != null) {
				detector.setPriorMap(probabilities);
			}
			try {
				detector.detect();
				detector.getProbabilities();
				return detector.detect();
			} catch (LangDetectException exception) {
				return null;
			}
		}
	}



	/**
	 * Initialiser.
	 */
	static {
		List<String> profiles = new Vector<>();

		try {
			for (byte[] content : ClassTools.getContents(Pattern.compile("profiles/[^/]+"), ClassTools.getLocation(DetectorFactory.class))) {
				profiles.add(new String(content));
			}
			DetectorFactory.loadProfile(profiles);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} catch (LangDetectException exception) {
			throw new RuntimeException(exception);
		} catch (Throwable exception) {
			System.err.println(FormatTools.formatException(exception));
			throw new RuntimeException(exception);
		}
	}



}
