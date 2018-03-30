package fr.inist.toolbox;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

import org.junit.*;

import toolbox.*;
import toolbox.ClassTools.*;



/**
 * La classe {@link ClassToolsTest} implémente les tests de la classe {@link ClassLoadCollector}.
 * @author Ludovic WALLE
 */
@SuppressWarnings("static-method")
public class ClassToolsTest {



	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test public void testGetPathes_0() throws IOException {
		for (Entry<String, Vector<Location>> entry : ClassTools.getPathes().entrySet()) {
			System.out.println(entry.getKey());
			for (Location location : entry.getValue()) {
				System.out.println("   " + location);
			}
		}
	}



	/**
	 * Enregistrer l'utilisation de cette classe.
	 */
	static {
		ClassLoadCollector.collect();
	}



}
