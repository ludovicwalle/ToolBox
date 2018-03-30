package fr.inist.toolbox;

import static org.junit.Assert.*;

import org.junit.*;

import toolbox.*;



/**
 * La classe {@link ScannerTest} implémente les tests de la classe {@link Scanner}.
 * @author Ludovic WALLE
 */
@SuppressWarnings({"unused", "static-method"})
public class ScannerTest {



	/** */
	@Test public void testForward_0() {
		final Scanner scanner = new Scanner("");

		assertEquals(0, scanner.getIndex());
		for (int i = 0; i < 2; i++) {
			assertEquals(i, scanner.getIndex());
			scanner.move(1);
		}
	}



	/** */
	@Test public void testForward_1() {
		final Scanner scanner = new Scanner("abc");

		assertEquals(0, scanner.getIndex());
		for (int i = 0; i < 5; i++) {
			assertEquals(i, scanner.getIndex());
			scanner.move(1);
		}
	}



	/** */
	@Test public void testForwardIfCurrentCharIs_char_0() {
		final Scanner scanner = new Scanner("abc");

		assertEquals(0, scanner.getIndex());
		assertFalse(scanner.moveIf(scanner.currentCharIsOneOf('A'), 1));
		assertEquals(0, scanner.getIndex());
		assertTrue(scanner.moveIf(scanner.currentCharIsOneOf('a'), 1));
		assertEquals(1, scanner.getIndex());
		assertFalse(scanner.moveIf(scanner.currentCharIsOneOf('B'), 1));
		assertEquals(1, scanner.getIndex());
		assertTrue(scanner.moveIf(scanner.currentCharIsOneOf('b'), 1));
		assertEquals(2, scanner.getIndex());
		assertFalse(scanner.moveIf(scanner.currentCharIsOneOf('C'), 1));
		assertEquals(2, scanner.getIndex());
		assertTrue(scanner.moveIf(scanner.currentCharIsOneOf('c'), 1));
		assertEquals(3, scanner.getIndex());
		{
			boolean raised = false;
			try {
				scanner.moveIf(scanner.currentCharIsOneOf('d'), 1);
			} catch (StringIndexOutOfBoundsException exception) {
				raised = true;
			}
			assertTrue(raised);
		}
		assertEquals(3, scanner.getIndex());
	}



	/** */
//	@Test public void testForwardThenGetCurrentChar_0() {
//		final Scanner scanner = new Scanner("abc");
//
//		assertEquals(0, scanner.getOffset());
//		assertEquals('b', scanner.getString().charAt(++scanner.getIndex()));
//		assertEquals(1, scanner.getOffset());
//		assertEquals('c', scanner.getString().charAt(++scanner.getIndex()));
//		assertEquals(2, scanner.getOffset());
//		{
//			boolean raised = false;
//			try {
//				scanner.getString().charAt(++scanner.getIndex());
//			} catch (StringIndexOutOfBoundsException exception) {
//				raised = true;
//			}
//			assertTrue(raised);
//		}
//		assertEquals(3, scanner.getOffset());
//	}



	/** */
	@Test public void testGetCharsToParseCount_0() {
		final Scanner scanner = new Scanner("abc");

		assertEquals(3, scanner.getCharsToParseCount());
		scanner.move(1);
		assertEquals(2, scanner.getCharsToParseCount());
		scanner.move(1);
		assertEquals(1, scanner.getCharsToParseCount());
		scanner.move(1);
		assertEquals(0, scanner.getCharsToParseCount());
		scanner.move(1);
		assertEquals(-1, scanner.getCharsToParseCount());
		scanner.move(1);
		assertEquals(-2, scanner.getCharsToParseCount());
		assertEquals(5, scanner.getIndex());
	}



	/** */
	@Test public void testGetCurrentChar_0() {
		final Scanner scanner = new Scanner("");
		{
			boolean raised = false;
			try {
				scanner.getCurrentChar();
			} catch (StringIndexOutOfBoundsException exception) {
				raised = true;
			}
			assertTrue(raised);
		}
	}



	/** */
	@Test public void testGetCurrentChar_1() {
		final Scanner scanner = new Scanner("abc");

		assertEquals('a', scanner.getCurrentChar());
		assertEquals('a', scanner.getCurrentChar());
		scanner.move(1);
		assertEquals('b', scanner.getCurrentChar());
		scanner.move(1);
		assertEquals('c', scanner.getCurrentChar());
		scanner.move(1);
		{
			boolean raised = false;
			try {
				scanner.getCurrentChar();
			} catch (StringIndexOutOfBoundsException exception) {
				raised = true;
			}
			assertTrue(raised);
		}
	}



	/** */
	@Test public void testGetCurrentCharThenForward_0() {
		final Scanner scanner = new Scanner("abc");

		assertEquals('a', scanner.getCurrentCharThenMove(1));
		assertEquals('b', scanner.getCurrentCharThenMove(1));
		assertEquals('c', scanner.getCurrentCharThenMove(1));
		{
			boolean raised = false;
			try {
				scanner.getCurrentCharThenMove(1);
			} catch (StringIndexOutOfBoundsException exception) {
				raised = true;
			}
			assertTrue(raised);
		}
	}



	/** */
	@Test public void testGetCurrentCharThenForwardIf_char_1() {
		final Scanner scanner = new Scanner("abc");

		assertEquals('a', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('b'), 1));
		assertEquals(0, scanner.getIndex());
		assertEquals('a', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('a'), 1));
		assertEquals(1, scanner.getIndex());
		assertEquals('b', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('b'), 1));
		assertEquals(2, scanner.getIndex());
		assertEquals('c', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('b'), 1));
		assertEquals(2, scanner.getIndex());
		assertEquals('c', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('c'), 1));
		assertEquals(3, scanner.getIndex());
		{
			boolean raised = false;
			try {
				scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('a'), 1);
			} catch (StringIndexOutOfBoundsException exception) {
				raised = true;
			}
			assertTrue(raised);
		}
	}



	/** */
	@Test public void testGetCurrentCharThenForwardIf_char_char_0() {
		final Scanner scanner = new Scanner("");
		boolean raised = false;

		try {
			scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('a', 'b'), 1);
		} catch (StringIndexOutOfBoundsException exception) {
			raised = true;
		}
		assertTrue(raised);
	}



	/** */
	@Test public void testGetCurrentCharThenForwardIf_char_char_1() {
		final Scanner scanner = new Scanner("abc");

		assertEquals('a', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('b', 'c'), 1));
		assertEquals(0, scanner.getIndex());
		assertEquals('a', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('a', 'b'), 1));
		assertEquals(1, scanner.getIndex());
		assertEquals('b', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('a', 'b'), 1));
		assertEquals(2, scanner.getIndex());
		assertEquals('c', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('a', 'b'), 1));
		assertEquals(2, scanner.getIndex());
		assertEquals('c', scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('b', 'c'), 1));
		assertEquals(3, scanner.getIndex());
		{
			boolean raised = false;
			try {
				scanner.getCurrentCharThenMoveIf(scanner.currentCharIsOneOf('a', 'b'), 1);
			} catch (StringIndexOutOfBoundsException exception) {
				raised = true;
			}
			assertTrue(raised);
		}
	}



	/** */
	@Test public void testGetOffset_0() {
		final Scanner scanner = new Scanner("");

		assertEquals(0, scanner.getIndex());
		for (int i = 0; i < 2; i++) {
			assertEquals(i, scanner.getIndex());
			scanner.move(1);
		}
	}



	/** */
	@Test public void testGetOffset_1() {
		final Scanner scanner = new Scanner("abc");

		assertEquals(0, scanner.getIndex());
		for (int i = 0; i < 5; i++) {
			assertEquals(i, scanner.getIndex());
			scanner.move(1);
		}
	}



	/** */
	@Test public void testGetString_0() {
		final String string = "";
		final Scanner scanner = new Scanner(string);

		assertEquals(string, scanner.getString());
	}



	/** */
	@Test public void testGetString_1() {
		final String string = "abc";
		final Scanner scanner = new Scanner(string);

		assertEquals(string, scanner.getString());
	}



	/** */
	@Test(expected = NullPointerException.class) public void testScanner_String_0() {
		new Scanner(null);
	}



	/** */
	@Test public void testScanner_String_1() {
		final String string = "";
		final Scanner scanner = new Scanner(string);

		assertEquals(0, scanner.getIndex());
		assertEquals(string, scanner.getString());
	}



	/** */
	@Test public void testScanner_String_2() {
		final String string = "abc";
		final Scanner scanner = new Scanner(string);

		assertEquals(0, scanner.getIndex());
		assertEquals(string, scanner.getString());
	}



	/** */
	@Test public void testSkipSpaces_0() {
		final Scanner scanner = new Scanner("");

		scanner.skipWhitespaces();
		assertEquals(0, scanner.getIndex());
	}



	/** */
	@Test public void testSkipSpaces_1() {
		final Scanner scanner = new Scanner(" ");

		scanner.skipWhitespaces();
		assertEquals(1, scanner.getIndex());
	}



	/** */
	@Test public void testSkipSpaces_2() {
		final Scanner scanner = new Scanner("\t");

		scanner.skipWhitespaces();
		assertEquals(1, scanner.getIndex());
	}



	/** */
	@Test public void testSkipSpaces_3() {
		final Scanner scanner = new Scanner("\n");

		scanner.skipWhitespaces();
		assertEquals(1, scanner.getIndex());
	}



	/** */
	@Test public void testSkipSpaces_4() {
		final Scanner scanner = new Scanner("\r");

		scanner.skipWhitespaces();
		assertEquals(1, scanner.getIndex());
	}



	/** */
	@Test public void testSkipSpaces_5() {
		final Scanner scanner = new Scanner(" \t\n  \t\t\n\n");

		scanner.skipWhitespaces();
		assertEquals(9, scanner.getIndex());
	}



	/** */
	@Test public void testSkipSpaces_6() {
		final Scanner scanner = new Scanner(" \t\n  \t\t\n\n\0xFFFF");

		scanner.skipWhitespaces();
		assertEquals(9, scanner.getIndex());
	}



	/** */
	@Test public void testSkipSpaces_7() {
		final Scanner scanner = new Scanner("a b\tc\nd");

		scanner.skipWhitespaces();
		assertEquals(0, scanner.getIndex());
		scanner.move(1);
		scanner.skipWhitespaces();
		assertEquals(2, scanner.getIndex());
		scanner.move(1);
		scanner.skipWhitespaces();
		assertEquals(4, scanner.getIndex());
		scanner.move(1);
		scanner.skipWhitespaces();
		assertEquals(6, scanner.getIndex());
		scanner.move(4);
		assertTrue(scanner.getCharsToParseCount() < 0);
		scanner.skipWhitespaces();
	}



	/** */
	@Test public void testSubstringFrom_int_0() {
		final Scanner scanner = new Scanner("abcdefghij");

		assertNull(((scanner.getIndex() <= 0) ? null : scanner.getString().substring(0, scanner.getIndex())));
		assertNull(((scanner.getIndex() <= 5) ? null : scanner.getString().substring(5, scanner.getIndex())));
		assertNull(((scanner.getIndex() <= 10) ? null : scanner.getString().substring(10, scanner.getIndex())));
		scanner.move(5);
		assertEquals("abcde", ((scanner.getIndex() <= 0) ? null : scanner.getString().substring(0, scanner.getIndex())));
		assertEquals("cde", ((scanner.getIndex() <= 2) ? null : scanner.getString().substring(2, scanner.getIndex())));
		assertNull(((scanner.getIndex() <= 5) ? null : scanner.getString().substring(5, scanner.getIndex())));
		assertNull(((scanner.getIndex() <= 10) ? null : scanner.getString().substring(10, scanner.getIndex())));
	}



	/** */
	@Test(expected = StringIndexOutOfBoundsException.class) public void testSubstringFrom_int_1() {
		int fromOffset = -5;
		Scanner r = new Scanner("");
		assertNull(((r.getIndex() <= fromOffset) ? null : r.getString().substring(fromOffset, r.getIndex())));
	}



}
