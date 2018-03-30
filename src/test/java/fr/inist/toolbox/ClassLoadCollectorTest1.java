package fr.inist.toolbox;

import java.io.*;
import java.net.*;

import toolbox.*;



/**
 * La classe {@link ClassLoadCollectorTest} impl�mente les tests de la classe {@link ClassLoadCollector}.
 * @author Ludovic WALLE
 */
public class ClassLoadCollectorTest1 {



	/**
	 * @param args
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws MalformedURLException, IOException {
		ClassLoadCollector.addLogger("http://walle:51600/RefDoc/Log");
	}

	static {
		ClassLoadCollector.collect();
	}


}
