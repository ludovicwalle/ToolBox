package fr.inist.toolbox;

import java.io.*;
import java.net.*;

import org.junit.*;

import toolbox.*;



/**
 * La classe {@link ClassLoadCollectorTest} implémente les tests de la classe {@link ClassLoadCollector}.
 * @author Ludovic WALLE
 */
@SuppressWarnings("static-method")
public class ClassLoadCollectorTest {



	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test(expected = MalformedURLException.class) public void testAddLogger_String_0() throws MalformedURLException, IOException {
		ClassLoadCollector.addLogger(null);
	}



	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test(expected = MalformedURLException.class) public void testAddLogger_String_1() throws MalformedURLException, IOException {
		ClassLoadCollector.addLogger("");
	}



	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test(expected = MalformedURLException.class) public void testAddLogger_String_2() throws MalformedURLException, IOException {
		ClassLoadCollector.addLogger("mqsldkj");
	}



	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test public void testAddLogger_String_3() throws MalformedURLException, IOException {
		ClassLoadCollector.addLogger("http://bidon:51600/RefDoc/Log");
	}



	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Test public void testAddLogger_String_4() throws MalformedURLException, IOException {
		ClassLoadCollector.addLogger("http://walle:51600/RefDoc/Log");
		ClassLoadCollector.collect();
	}



	/** */
	@Test public void testCollect_0() {
		ClassLoadCollector.collect();
	}



	/** */
	@BeforeClass public static void beforeClass() {
		System.setProperty(ClassLoadCollector.SERVLET_URL_ENVIRONMENT_VARIABLE_NAME, "");
	}



}
