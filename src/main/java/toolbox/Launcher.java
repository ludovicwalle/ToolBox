package toolbox;

import java.io.*;



/**
 * La classe {@link Launcher} lance une commande externe.
 * @author Ludovic WALLE
 */
public abstract class Launcher {



	/**
	 * Lance une commande externe en connectant les flux standards � ceux de ce processus, c'est � dire que tout ce qui arrive sur {@link System#in} est envoy� sur le le flux d'entr�e standard de la
	 * commande externe, et que tout ce que la commande externe g�n�re sur les flux de sortie et d'erreur standards est envoy� respectivement sur {@link System#out} et {@value System#err}.
	 * @param commandAndParameters Commande externe � ex�cuter, avec ses param�tres �ventuels.
	 * @throws InterruptedException En cas d'interruption du thread courant.
	 * @throws IOException En cas d'erreur d'entr�e sortie.
	 */
	public static void launch(String... commandAndParameters) throws InterruptedException, IOException {
		Process process;

		process = Runtime.getRuntime().exec(commandAndParameters);
		new Connector(process.getErrorStream(), System.err).start();
		new Connector(process.getInputStream(), System.out).start();
		new Connector(System.in, process.getOutputStream()).start();
		process.waitFor();
	}



	/**
	 * La classe {@link Connector} transferre tout ce qui provient d'un flux d'entr�e � un flux de sortie.
	 * @author Ludovic WALLE
	 */
	private static class Connector extends Thread {



		/**
		 * @param input Flux d'entr�e.
		 * @param output Flux de sortie.
		 */
		public Connector(InputStream input, OutputStream output) {
			this.input = input;
			this.output = output;
		}



		/**
		 * {@inheritDoc}
		 */
		@Override public void run() {
			byte[] bytes = new byte[1024];
			int len;
			try {
				while ((len = input.read(bytes)) > 0) {
					output.write(bytes, 0, len);
				}
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
		}



		/**
		 * Flux d'entr�e.
		 */
		private final InputStream input;



		/**
		 * Flux de sortie.
		 */
		private final OutputStream output;



	}



}
