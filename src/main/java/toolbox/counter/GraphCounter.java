package toolbox.counter;

import java.awt.*;
import java.util.*;

import javax.swing.*;



/**
 * La classe {@link GraphCounter} définit un compteur avec affichage graphique.
 */

public class GraphCounter extends Counter {



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean cancel() {
		graph.frame.dispose();
		return super.cancel();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public boolean delegate_show() {
		graph.add(getElapsed(), getCount());
		graph.frame.setTitle(toString());
		return true;
	}



	/**
	 * Graphe.
	 */
	private final Graph graph = new Graph(1000, 150);



	/**
	 * La classe {@link Graph}
	 * @author Ludovic WALLE
	 */
	private class Graph extends JPanel {



		/**
		 * @param width
		 * @param height
		 */
		public Graph(final int width, final int height) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setPreferredSize(new Dimension(width, height));
			scrollPane = new JScrollPane(this, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.getHorizontalScrollBar().setAutoscrolls(true);
			frame.add(scrollPane);
			frame.pack();
			frame.setVisible(true);
		}



		/**
		 * Ajoute un point au graphique.
		 * @param time Temps.
		 * @param value Valeur.
		 */
		public void add(final double time, final int value) {
			synchronized (points) {
				points.add(lastPoint = new GraphPoint(time, value));
			}
			if (firstPoint == null) {
				firstPoint = lastPoint;
			} else if (lastPoint.time > getWidth()) {
				setPreferredSize(new Dimension((int) lastPoint.time, getHeight()));
				setSize(new Dimension((int) lastPoint.time, getHeight()));
				if (!scrollPane.getHorizontalScrollBar().getModel().getValueIsAdjusting()) {
					xOffset = (int) lastPoint.time - getWidth();
					scrollPane.getHorizontalScrollBar().setValue((int) lastPoint.time);
				}
			}
			frame.setTitle(this.toString());
			repaint();
		}



		/**
		 * Calcule la position verticale.
		 * @param height Hauteur de la fenètre.
		 * @param point Point du graphe.
		 * @return La position verticale.
		 */
		private int computeY(final int height, final GraphPoint point) {
			if (getExpected() > 0) {
				return height - 1 - (((point.value * height) - 1) / getExpected());
			} else if (lastPoint.value > 0) {
				return height - 1 - (((point.value * height) - 1) / lastPoint.value);
			} else {
				return height - 1;
			}
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public void paint(final Graphics graphics) {
			int height;
			int x1 = -1;
			int y1 = -1;
			int x2 = -1;
			int y2 = -1;

			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, getWidth(), getHeight());
			if ((getElapsed() > 0) && (getCount() > 0) && (getExpected() > 0)) {
				if (origin == -1) {
					origin = getElapsed();
				} else {
					graphics.setColor(Color.LIGHT_GRAY);
					graphics.drawLine((int) origin, getHeight() - 1, (int) (((getExpected() * (getElapsed() - origin)) / getCount()) + origin), 0);
				}
			}
			graphics.setColor(Color.RED);
			height = getHeight();
			synchronized (points) {
				for (GraphPoint point : points) {
					if ((int) point.time >= (xOffset - 1)) {
						if (x1 == -1) {
							x1 = (int) point.time - xOffset;
							y1 = computeY(height, point);
						} else {
							x2 = (int) point.time - xOffset;
							y2 = computeY(height, point);
							graphics.drawLine(x1, y1, x2, y2);
							x1 = x2;
							y1 = y2;
						}
					}
				}
			}
		}



		/**
		 * Premier point du graphique.
		 */
		private GraphPoint firstPoint;



		/**
		 * Fenètre principale.
		 */
		private final JFrame frame;



		/**
		 * Dernier point du graphique.
		 */
		private GraphPoint lastPoint;



		/**
		 * Origine de la ligne.
		 */
		private double origin = -1;



		/**
		 * Points du graphique.
		 */
		private final Vector<GraphPoint> points = new Vector<>();



		/**
		 * Panneau défilable.
		 */
		private final JScrollPane scrollPane;



		/**
		 * Position à afficher.
		 */
		private int xOffset;



		/**
		 * La classe {@link GraphPoint}
		 * @author Ludovic WALLE
		 */
		private class GraphPoint {



			/**
			 * @param time
			 * @param value
			 */
			public GraphPoint(final double time, final int value) {
				this.time = time;
				this.value = value;
			}



			/**
			 * Temps.
			 */
			public final double time;



			/**
			 * Valeur.
			 */
			public final int value;


		}



	}



}
