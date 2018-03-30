package fr.inist.toolbox.servletTools;

import java.util.*;

import org.junit.*;

import toolbox.servlet.*;



/**
 * La classe {@link CheckerTest} implémente les tests de la classe {@link Checker}.
 * @author Ludovic WALLE
 */
@SuppressWarnings({"unused", "static-method"})
public class CheckerTest {



	/** */
	@Test public void testParametersChecker_a01() {
		new Checker("((A|B)&(C|D)?&E?)", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""), new TextParameter("E", ""));
	}



	/** */
	@Test public void testParametersChecker_a02() {
		new Checker("(((A|B*)&(C+|D)?&E?)|F)", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""), new TextParameter("E", ""), new TextParameter("F", ""));
	}



	/** */
	@Test public void testParametersChecker_a03() {
		new Checker("(((A|B*)&(C+|D)?&E?)|A*)", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""), new TextParameter("E", ""));
	}



	/** */
	@Test public void testParametersChecker_a04() {
		new Checker("()");
	}



	/** */
	@Test public void testParametersChecker_a05() {
		new Checker("((A|B)&(C+|D*)?&E?)", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""), new TextParameter("E", ""));
	}



	/** */
	@Test public void testParametersChecker_a06() {
		new Checker("((A|B)&(C|D)?&E?)");
	}



	/** */
	@Test public void testParametersChecker_a07() {
		new Checker("((A|B=\"b1\")&(C|D)?&E?)");
	}



	/** */
	@Test public void testParametersChecker_a08() {
		new Checker("((A&B)|(A&C))");
	}



	/** */
	@Test public void testParametersChecker_a09() {
		new Checker("((A=\"a1\"&B)|(A=\"a2\"&C))");
	}



	/** */
	@Test public void testParametersChecker_a10() {
		new Checker("(A|A)");
	}



	/** */
	@Test public void testParametersChecker_a11() {
		new Checker("(A=\"a1\")");
	}



	/** */
	@Test public void testParametersChecker_a12() {
		new Checker("((A&B)|(C&A))");
	}



	/** */
	@Test public void testParametersChecker_a13() {
		new Checker("(A=\"a1\"?)");
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b02() {
		new Checker("((A|B)&(C|D)?&A)", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b03() {
		new Checker("((A|B)&(C|D)?&", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b04() {
		new Checker("((A|B)&(C|D)*&E)", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b05() {
		new Checker("((A|B)&(C|D)?&A)", new TextParameter("A", ""), new TextParameter("B", ""), new TextParameter("C", ""), new TextParameter("D", ""));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b06() {
		new Checker("()", new TextParameter("A", ""), new TextParameter("A", ""));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b07() {
		new Checker("((A)");
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b08() {
		new Checker("(A &)");
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b10() {
		new Checker("(A & A)");
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b11() {
		new Checker("(A=\"a1\"&A=\"a2\")");
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b12() {
		new Checker("(A=\"a1\"|A=)");
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b13() {
		new Checker("(A=\"a1\"*)");
	}



	/** */
	@Test(expected = RuntimeException.class) public void testParametersChecker_b14() {
		new Checker("(A=\"a1\"+)");
	}



	/** */
	@Test public void testParametersChecker_c01() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		Assert.assertNull(new Checker("((A|B)&(C|D)?&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c02() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		map.put("D", new String[]{"d1"});
		Assert.assertNull(new Checker("((A|B)&(C|D)?&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c03() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		map.put("D", new String[]{"d1", "d2"});
		Assert.assertNull(new Checker("((A|B)&(C+|D*)?&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c04() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		map.put("B", new String[]{"b1"});
		Assert.assertNotNull(new Checker("((A|B)&(C+|D*)?&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c05() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1", "a2"});
		Assert.assertNotNull(new Checker("((A|B)&(C|D)?&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c06() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("C", new String[]{"c1"});
		Assert.assertNotNull(new Checker("((A|B)&(C|D)?&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c07() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		Assert.assertNotNull(new Checker("((A|B)&(C+|D+)&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c08() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		Assert.assertNull(new Checker("((A & B)?|C?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c09() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		Assert.assertNull(new Checker("((A|B)&(C+|D+)?&E?)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c10() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		map.put("C", new String[]{"c1"});
		Assert.assertNotNull(new Checker("((A=\"a1\"&B)|C)").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_c11() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"a1"});
		map.put("C", new String[]{"c1"});
		Assert.assertNotNull(new Checker("((A=\"a1\"&B)|(A=\"a2\"&C))").check(null, map));
	}



	/** */
	@Test public void testParametersChecker_d01() {
		IntegerParameter A = new IntegerParameter("A", "paramètre a", false, 0, 1000);
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("A", new String[]{"0"});
		Assert.assertNull(new Checker(true, A).check(null, map));
	}



}
