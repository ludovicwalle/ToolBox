package fr.inist.toolbox;

import java.io.*;

import org.junit.*;

import toolbox.*;



/**
 * La classe {@link DelimitedReaderTest} implémente les tests sur les méthodes de la classe {@link ReTools}.
 */
@SuppressWarnings({"unused", "resource", "static-method"})
public class DelimitedReaderTest {



	/** */
	@Test(expected = NullPointerException.class) public void testDelimitedReader_Reader_0a() {
		new DelimitedReader(null);
	}



	/** */
	@Test public void testDelimitedReader_Reader_0b() {
		new DelimitedReader(new StringReader("abc"));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_1a() throws IOException {
		Assert.assertEquals("abc", new DelimitedReader(new StringReader("abc")).read("a", true, "c", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_1b() throws IOException {
		Assert.assertEquals("bc", new DelimitedReader(new StringReader("abc")).read("a", false, "c", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_1c() throws IOException {
		Assert.assertEquals("ab", new DelimitedReader(new StringReader("abc")).read("a", true, "c", false));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_1d() throws IOException {
		Assert.assertEquals("b", new DelimitedReader(new StringReader("abc")).read("a", false, "c", false));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_2a() throws IOException {
		Assert.assertEquals("abc", new DelimitedReader(new StringReader("abc")).read("a", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_2b() throws IOException {
		Assert.assertEquals("abc", new DelimitedReader(new StringReader("abc")).read("", true, "c", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_2c() throws IOException {
		Assert.assertEquals("abc", new DelimitedReader(new StringReader("abc")).read("", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_2d() throws IOException {
		Assert.assertEquals("abc", new DelimitedReader(new StringReader("abc")).read("", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_3a() throws IOException {
		DelimitedReader reader = new DelimitedReader(new StringReader("abc"));

		reader.read();
		Assert.assertEquals("bc", reader.read("", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_3b() throws IOException {
		DelimitedReader reader = new DelimitedReader(new StringReader("abc"));

		reader.read();
		reader.read();
		reader.read();
		Assert.assertEquals("", reader.read("", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_3c() throws IOException {
		DelimitedReader reader = new DelimitedReader(new StringReader("abc"));

		reader.read();
		reader.read();
		reader.read();
		Assert.assertEquals("", reader.read("", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_3d() throws IOException {
		DelimitedReader reader = new DelimitedReader(new StringReader("abc"));

		Assert.assertEquals("abc", reader.read("", true, "", true));
		Assert.assertEquals("", reader.read("", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_4a() throws IOException {
		Assert.assertNull(new DelimitedReader(new StringReader("abc")).read("d", true, "c", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_4b() throws IOException {
		Assert.assertNull(new DelimitedReader(new StringReader("abc")).read("a", true, "d", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_4c() throws IOException {
		Assert.assertNull(new DelimitedReader(new StringReader("abc")).read("a", true, "a", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_4d() throws IOException {
		Assert.assertNull(new DelimitedReader(new StringReader("abc")).read("aa", true, "c", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_4e() throws IOException {
		Assert.assertNull(new DelimitedReader(new StringReader("abc")).read("c", true, "a", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_4f() throws IOException {
		Assert.assertNull(new DelimitedReader(new StringReader("abc")).read("d", true, "", true));
	}



	/**
	 * @throws IOException
	 */
	@Test public void testRead_Reader_4g() throws IOException {
		Assert.assertNull(new DelimitedReader(new StringReader("abc")).read("", true, "d", true));
	}



}
