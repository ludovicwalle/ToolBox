package toolbox.counter;

import java.awt.*;

import javax.swing.*;



/**
 * La classe {@link TitleCounter} définit un compteur avec affichage graphique.
 */

public class TitleCounter extends Counter {



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean cancel() {
		frame.dispose();
		return super.cancel();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean delegate_show() {
		frame.setTitle(this.toString());
		return true;
	}



	/**
	 * Fenetre.
	 */
	private final JFrame frame = new JFrame();



	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1000, 0));
		frame.pack();
		frame.setVisible(true);
	}



}
