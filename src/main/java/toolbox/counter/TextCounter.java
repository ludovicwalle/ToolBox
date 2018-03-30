package toolbox.counter;

import java.io.*;



/**
 * La classe {@link GraphCounter} définit un {@link Counter} avec affichage texte.
 */
public class TextCounter extends Counter {



	/**
	 * @param output Flux d'affichage du compteur.
	 */
	public TextCounter(PrintStream output) {
		this.output = output;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean delegate_show() {
		if (isStarted()) {
			output.print("     \r" + this.toString());
			return true;
		} else {
			return false;
		}
	}



	/**
	 * Flux d'affichage du compteur.
	 */
	private final PrintStream output;



}
