package toolbox.counter;

/**
 * La classe {@link GraphCounter} définit un {@link Counter} sans affichage.
 */
public class SilentCounter extends Counter {



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean delegate_show() {
		return false;
	}



}
