package toolbox.counter;

/**
 * La classe {@link GraphCounter} d�finit un {@link Counter} sans affichage.
 */
public class SilentCounter extends Counter {



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean delegate_show() {
		return false;
	}



}
