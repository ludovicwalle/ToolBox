package fr.inist.toolbox;

import static org.junit.Assert.*;

import java.util.*;
import java.util.regex.*;

import org.junit.*;

import toolbox.*;



/**
 * La classe {@link ToolsTest} implémente les tests de la classe {@link ArraysTools}.
 * @author Ludovic WALLE
 */
@SuppressWarnings("static-method")
public class ToolsTest {



	/** */
	@Test(expected = NullPointerException.class) public void testAppend_intArray_intArray_0() {
		ArraysTools.append((int[]) null, (int[]) null);
	}



	/** */
	@Test(expected = NullPointerException.class) public void testAppend_intArray_intArray_1() {
		ArraysTools.append(null, new int[]{});
	}



	/** */
	@Test(expected = NullPointerException.class) public void testAppend_intArray_intArray_2() {
		ArraysTools.append(new int[]{}, null);
	}



	/** */
	@Test public void testAppend_intArray_intArray_3() {
		assertArrayEquals(new int[]{1, 2, 3, 4}, ArraysTools.append(new int[]{1, 2}, new int[]{3, 4}));
	}



	/** */
	@Test public void testAppend_intArray_intArray_4() {
		assertArrayEquals(new int[]{1, 2}, ArraysTools.append(new int[]{1, 2}, new int[]{}));
	}



	/** */
	@Test public void testAppend_intArray_intArray_5() {
		assertArrayEquals(new int[]{3, 4}, ArraysTools.append(new int[]{}, new int[]{3, 4}));
	}



	/** */
	@Test(expected = NullPointerException.class) public void testAppend_ObjectArray_ObjectArray_00() {
		ArraysTools.append((Object[]) null, (Object[]) null);
	}



	/** */
	@Test(expected = NullPointerException.class) public void testAppend_ObjectArray_ObjectArray_01() {
		ArraysTools.append((Object[]) null, new Object[]{});
	}



	/** */
	@Test(expected = NullPointerException.class) public void testAppend_ObjectArray_ObjectArray_02() {
		ArraysTools.append(new Object[]{}, (Object[]) null);
	}



	/** */
	@Test public void testAppend_ObjectArray_ObjectArray_03() {
		assertArrayEqualsAndSameType(new Object[]{}, ArraysTools.append(new Object[]{}, new Object[]{}));
	}



	/** */
	@Test(expected = ArrayStoreException.class) public void testAppend_ObjectArray_ObjectArray_04() {
		ArraysTools.append(new Integer[]{}, (Object[]) new String[]{});
	}



	/** */
	@Test(expected = ArrayStoreException.class) public void testAppend_ObjectArray_ObjectArray_05() {
		ArraysTools.append(new Integer[]{1, 2}, new String[]{"a", "b"});
	}



	/** */
	@Test public void testAppend_ObjectArray_ObjectArray_06() {
		assertArrayEqualsAndSameType(new Object[]{1, 2, "a", "b"}, ArraysTools.append(new Object[]{1, 2}, new Object[]{"a", "b"}));
	}



	/** */
	@Test public void testAppend_ObjectArray_ObjectArray_07() {
		assertArrayEqualsAndSameType(new Object[]{new int[]{1}, new int[]{2}, "a", "b"}, ArraysTools.append(new Object[]{new int[]{1}, new int[]{2}}, new Object[]{"a", "b"}));
	}



	/** */
	@Test public void testAppend_ObjectArray_ObjectArray_08() {
		assertArrayEqualsAndSameType(new Object[]{new int[]{1}, new int[]{2}, "a", "b"}, ArraysTools.append(new Object[]{new int[]{1}, new int[]{2}}, "a", "b"));
	}



	/** */
	@Test public void testAppend_ObjectArray_ObjectArray_09() {
		assertArrayEqualsAndSameType(new String[]{"a", "b", "c", "d"}, ArraysTools.append(new String[]{"a", "b"}, "c", "d"));
	}



	/** */
	@Test public void testAppend_ObjectArray_ObjectArray_10() {
		assertArrayEqualsAndSameType(new String[]{"a", "b", "c"}, ArraysTools.append(new String[]{"a", "b"}, "c"));
	}



	/** */
	@SuppressWarnings("unchecked") @Test(expected = ArrayStoreException.class) public void testAppend_ObjectArray_ObjectArray_11() {
		ArraysTools.append(new String[]{"a", "b"}, 1, "c");
	}



	/** */
	@Test public void testCharRangeRE_int_int_0() {
		assertEquals("a", ReTools.charRangeRE('a', 'a'));
	}



	/** */
	@Test public void testCharRangeRE_int_int_1() {
		assertEquals("[a-b]", ReTools.charRangeRE('a', 'b'));
	}



	/** */
	@Test public void testCharRangeRE_int_int_2() {
		assertEquals("[a-z]", ReTools.charRangeRE('a', 'z'));
	}



	/** */
	@Test public void testCharRangeRE_int_int_3() {
		assertEquals("[\\u002D-\\u005B]", ReTools.charRangeRE('-', '['));
	}



	/** */
	@Test public void testCharRangeRE_int_int_4() {
		assertEquals("\\u002D", ReTools.charRangeRE('-', '-'));
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_5() {
		ReTools.charRangeRE((char) 0x1234, (char) 0x12345);
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_6() {
		ReTools.charRangeRE((char) -10, 'z');
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_7() {
		ReTools.charRangeRE('z', 'a');
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_0() {
		assertEquals("", ReTools.charRangeRE('a', 'a', 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_1() {
		assertEquals("", ReTools.charRangeRE('a', 'b', 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_10() {
		assertEquals("[a-z]", ReTools.charRangeRE('a', 'z', 1));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_11() {
		assertEquals("[\\u002D-\\u005B]", ReTools.charRangeRE('-', '[', 1));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_12() {
		assertEquals("\\u002D", ReTools.charRangeRE('-', '-', 1));
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_int_13() {
		ReTools.charRangeRE((char) 0x1234, (char) 0x12345, 1);
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_int_14() {
		ReTools.charRangeRE((char) -10, 'z', 1);
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_int_15() {
		ReTools.charRangeRE('z', 'a', 1);
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_16() {
		assertEquals("a{5}", ReTools.charRangeRE('a', 'a', 5));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_17() {
		assertEquals("[a-b]{5}", ReTools.charRangeRE('a', 'b', 5));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_18() {
		assertEquals("[a-z]{5}", ReTools.charRangeRE('a', 'z', 5));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_19() {
		assertEquals("[\\u002D-\\u005B]{5}", ReTools.charRangeRE('-', '[', 5));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_2() {
		assertEquals("", ReTools.charRangeRE('a', 'z', 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_20() {
		assertEquals("\\u002D{5}", ReTools.charRangeRE('-', '-', 5));
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_int_21() {
		ReTools.charRangeRE((char) 0x1234, (char) 0x12345, 5);
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_int_22() {
		ReTools.charRangeRE((char) -10, 'z', 5);
	}



	/** */
	@Test(expected = IllegalArgumentException.class) public void testCharRangeRE_int_int_int_23() {
		ReTools.charRangeRE('z', 'a', 5);
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_3() {
		assertEquals("", ReTools.charRangeRE('-', '[', 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_4() {
		assertEquals("", ReTools.charRangeRE('-', '-', 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_5() {
		assertEquals("", ReTools.charRangeRE((char) 0x1234, (char) 0x12345, 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_6() {
		assertEquals("", ReTools.charRangeRE((char) -10, 'z', 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_7() {
		assertEquals("", ReTools.charRangeRE('z', 'a', 0));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_8() {
		assertEquals("a", ReTools.charRangeRE('a', 'a', 1));
	}



	/** */
	@Test public void testCharRangeRE_int_int_int_9() {
		assertEquals("[a-b]", ReTools.charRangeRE('a', 'b', 1));
	}



	/** */
	@Test public void testConcat_byteArrayArray_0() {
		assertArrayEquals(new byte[]{}, ArraysTools.concat((byte[]) null));
	}



	/** */
	@Test public void testConcat_byteArrayArray_1() {
		assertArrayEquals(new byte[]{}, ArraysTools.concat(new byte[]{}, null));
	}



	/** */
	@Test public void testConcat_byteArrayArray_2() {
		assertArrayEquals(new byte[]{}, ArraysTools.concat(new byte[]{}));
	}



	/** */
	@Test public void testConcat_byteArrayArray_3() {
		assertArrayEquals(new byte[]{}, ArraysTools.concat(new byte[]{}, new byte[]{}));
	}



	/** */
	@Test public void testConcat_byteArrayArray_4() {
		assertArrayEquals(new byte[]{}, ArraysTools.concat(new byte[]{}, new byte[]{}, new byte[]{}));
	}



	/** */
	@Test public void testConcat_byteArrayArray_5() {
		assertArrayEquals(new byte[]{1, 2, 3}, ArraysTools.concat(new byte[]{1, 2, 3}));
	}



	/** */
	@Test public void testConcat_byteArrayArray_6() {
		assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6}, ArraysTools.concat(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}));
	}



	/** */
	@Test public void testConcat_byteArrayArray_7() {
		assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, ArraysTools.concat(new byte[]{1, 2, 3}, new byte[]{4, 5}, new byte[]{}, new byte[]{6, 7, 8}, new byte[]{9}));
	}



	/** */
	@Test(expected = NullPointerException.class) public void testConcat_ObjectArrayArray_00() {
		ArraysTools.concat((Object[]) null);
	}



	/** */
	@Test(expected = NullPointerException.class) public void testConcat_ObjectArrayArray_01() {
		ArraysTools.concat(new Object[]{}, null);
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_02() {
		assertArrayEquals(new Object[]{}, ArraysTools.concat(new Object[]{}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_03() {
		assertArrayEquals(new Object[]{}, ArraysTools.concat(new Object[]{}, new Object[]{}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_04() {
		assertArrayEquals(new Object[]{}, ArraysTools.concat(new Object[]{}, new Object[]{}, new Object[]{}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_05() {
		assertArrayEquals(new Object[]{1, 2, 3}, ArraysTools.concat(new Object[]{1, 2, 3}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_06() {
		assertArrayEquals(new Object[]{1, 2, 3, 4, 5, 6}, ArraysTools.concat(new Object[]{1, 2, 3}, new Object[]{4, 5, 6}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_07() {
		assertArrayEquals(new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, ArraysTools.concat(new Object[]{1, 2, 3}, new Object[]{4, 5}, new Object[]{}, new Object[]{6, 7, 8}, new Object[]{9}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_08() {
		assertArrayEquals(new Object[]{1, 2, 3, "a", "b"}, ArraysTools.concat(new Object[]{1, 2, 3}, new Object[]{"a", "b"}));
	}



	/** */
	@SuppressWarnings({"unchecked"}) @Test(expected = ArrayStoreException.class) public void testConcat_ObjectArrayArray_09() {
		ArraysTools.concat(new Integer[]{1, 2, 3}, new String[]{"a", "b"});
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_10() {
		assertArrayEqualsAndSameType(new String[][]{new String[]{"a", "b"}, new String[]{"c", "d"}}, ArraysTools.concat((Object[]) new String[][]{new String[]{"a", "b"}, new String[]{"c", "d"}}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_11() {
		assertArrayEqualsAndSameType(new String[]{"a", "b", "c", "d"}, ArraysTools.concat(new String[][]{new String[]{"a", "b"}, new String[]{"c", "d"}}));
	}



	/** */
	@Test public void testConcat_ObjectArrayArray_12() {
		assertArrayEqualsAndSameType(new String[]{"a", "b", "c", "d"}, ArraysTools.concat(new String[]{"a", "b"}, new String[]{"c", "d"}));
	}



	/** */
	@Test(expected = NullPointerException.class) public void testContains_ObjectArray_Object_00() {
		ArraysTools.contains(null, null);
	}



	/** */
	@Test(expected = NullPointerException.class) public void testContains_ObjectArray_Object_01() {
		ArraysTools.contains(null, "a");
	}



	/** */
	@Test public void testContains_ObjectArray_Object_02() {
		assertFalse(ArraysTools.contains(new String[]{"a"}, null));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_03() {
		assertTrue(ArraysTools.contains(new String[]{"a"}, "a"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_04() {
		assertFalse(ArraysTools.contains(new String[]{"a"}, "b"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_05() {
		assertTrue(ArraysTools.contains(new String[]{null}, null));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_06() {
		assertFalse(ArraysTools.contains(new String[]{null}, "a"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_07() {
		assertTrue(ArraysTools.contains(new String[]{"a", null, "b"}, null));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_08() {
		assertTrue(ArraysTools.contains(new String[]{"a", null, "b"}, "a"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_09() {
		assertTrue(ArraysTools.contains(new String[]{"a", null, "b"}, "b"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_10() {
		assertFalse(ArraysTools.contains(new String[]{"a", null, "b"}, "c"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_11() {
		assertTrue(ArraysTools.contains(new String[]{null, "a"}, null));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_12() {
		assertTrue(ArraysTools.contains(new String[]{"a", null}, null));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_13() {
		assertFalse(ArraysTools.contains(new String[]{"a", "b", "c"}, null));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_14() {
		assertTrue(ArraysTools.contains(new String[]{"a", "b", "c"}, "a"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_15() {
		assertTrue(ArraysTools.contains(new String[]{"a", "b", "c"}, "b"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_16() {
		assertTrue(ArraysTools.contains(new String[]{"a", "b", "c"}, "c"));
	}



	/** */
	@Test public void testContains_ObjectArray_Object_17() {
		assertFalse(ArraysTools.contains(new String[]{"a", "b", "c"}, "d"));
	}



	/** */
	@Test public void testDigitCount_int_00() {
		assertEquals(2, OtherTools.digitCount(-1));
	}



	/** */
	@Test public void testDigitCount_int_01() {
		assertEquals(1, OtherTools.digitCount(0));
	}



	/** */
	@Test public void testDigitCount_int_02() {
		assertEquals(1, OtherTools.digitCount(1));
	}



	/** */
	@Test public void testDigitCount_int_03() {
		assertEquals(1, OtherTools.digitCount(9));
	}



	/** */
	@Test public void testDigitCount_int_04() {
		assertEquals(2, OtherTools.digitCount(10));
	}



	/** */
	@Test public void testDigitCount_int_05() {
		assertEquals(2, OtherTools.digitCount(11));
	}



	/** */
	@Test public void testDigitCount_int_06() {
		assertEquals(2, OtherTools.digitCount(99));
	}



	/** */
	@Test public void testDigitCount_int_07() {
		assertEquals(3, OtherTools.digitCount(100));
	}



	/** */
	@Test public void testDigitCount_int_09() {
		assertEquals(3, OtherTools.digitCount(999));
	}



	/** */
	@Test public void testDigitCount_int_10() {
		assertEquals(4, OtherTools.digitCount(1000));
	}



	/** */
	@Test public void testDigitCount_int_11() {
		assertEquals(4, OtherTools.digitCount(1001));
	}



	/** */
	@Test public void testDigitsRE_int_0() {
		assertEquals("", ReTools.digitsRE(0));
	}



	/** */
	@Test public void testDigitsRE_int_1() {
		assertEquals("[0-9]", ReTools.digitsRE(1));
	}



	/** */
	@Test public void testDigitsRE_int_2() {
		assertEquals("[0-9]{5}", ReTools.digitsRE(5));
	}



	/** */
	@Test(expected = NullPointerException.class) public void testGetStringFragment_String_int_int_int_00() {
		OtherTools.getStringFragment(null, 0, 0, 0);
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_01() {
		assertEquals("...", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 0, 0, 0));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testGetStringFragment_String_int_int_int_02() {
		OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", -1, 1, 1);
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_03() {
		assertEquals("a...", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 0, 1, 1));
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_04() {
		assertEquals("ab...", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 1, 1, 1));
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_05() {
		assertEquals("...bc...", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 2, 1, 1));
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_09() {
		assertEquals("...xy...", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 24, 1, 1));
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_10() {
		assertEquals("...yz", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 25, 1, 1));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testGetStringFragment_String_int_int_int_11() {
		OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 26, 1, 1);
	}



	/** */
	@Test(expected = RuntimeException.class) public void testGetStringFragment_String_int_int_int_12() {
		OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 27, 1, 1);
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_13() {
		assertEquals("...bcdefghijkmlnopqrstuvwxy...", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 13, 12, 12));
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_14() {
		assertEquals("abcdefghijkmlnopqrstuvwxyz", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 13, 13, 13));
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_15() {
		assertEquals("abcdefghijkmlnopqrstuvwxyz", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 13, 14, 14));
	}



	/** */
	@Test public void testGetStringFragment_String_int_int_int_16() {
		assertEquals("abcdefghijkmlnopqrstuvwxyz", OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 0, 0, 26));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testGetStringFragment_String_int_int_int_17() {
		OtherTools.getStringFragment("abcdefghijkmlnopqrstuvwxyz", 100, 101, -99);
	}



	/** */
	@Test(expected = NullPointerException.class) public void testJavaPatternString_0() {
		ReTools.javaPatternString(null);
	}



	/** @throws Exception */
	@Test(expected = IllegalArgumentException.class) public void testJavaPatternString_1() throws Exception {
		ReTools.javaPatternString(new boolean[0x10001]);
	}



	/***/
	@Test public void testJavaPatternString_2() {
		assertEquals("", ReTools.javaPatternString(new boolean[0x1]));
	}



	/***/
	@Test public void testJavaPatternString_3() {
		boolean[] flags = new boolean[256];

		for (char c = 'a'; c <= 'z'; flags[c++] = true) {
			;
		}
		for (char c = 'A'; c <= 'Z'; flags[c++] = true) {
			;
		}
		assertEquals("[\\u0041-\\u005A\\u0061-\\u007A]", ReTools.javaPatternString(flags));
	}



	/** */
	@Test(expected = NullPointerException.class) public void testNewArray_ObjectArray_int_0() {
		ArraysTools.newArray(null, 0);
	}



	/** */
	@Test public void testNewArray_ObjectArray_int_1() {
		assertArrayEqualsAndSameType(new String[]{}, ArraysTools.newArray(new String[]{}, 0));
	}



	/** */
	@Test public void testNewArray_ObjectArray_int_2() {
		assertArrayEqualsAndSameType(new String[]{null, null, null, null, null}, ArraysTools.newArray(new String[]{"a", "b"}, 5));
	}



	/** */
	@Test(expected = NegativeArraySizeException.class) public void testNewArray_ObjectArray_int_3() {
		ArraysTools.newArray(new String[]{}, -1);
	}



	/** */
	@Test(expected = NegativeArraySizeException.class) public void testNewInitializedArray_boolean_int_0() {
		ArraysTools.newInitializedArray(false, -1);
	}



	/** */
	@Test public void testNewInitializedArray_boolean_int_1() {
		assertArrayEquals(new boolean[]{}, ArraysTools.newInitializedArray(false, 0));
	}



	/** */
	@Test public void testNewInitializedArray_boolean_int_2() {
		assertArrayEquals(new boolean[]{true, true, true, true, true}, ArraysTools.newInitializedArray(true, 5));
	}



	/** */
	@Test public void testNewInitializedArray_boolean_int_3() {
		assertArrayEquals(new boolean[]{false, false, false, false, false}, ArraysTools.newInitializedArray(false, 5));
	}



	/** */
	@Test(expected = NegativeArraySizeException.class) public void testNewInitializedArray_char_int_0() {
		ArraysTools.newInitializedArray('a', -1);
	}



	/** */
	@Test public void testNewInitializedArray_char_int_1() {
		assertArrayEquals(new char[]{}, ArraysTools.newInitializedArray('a', 0));
	}



	/** */
	@Test public void testNewInitializedArray_int_int_2() {
		assertArrayEquals(new char[]{'a', 'a', 'a', 'a', 'a'}, ArraysTools.newInitializedArray('a', 5));
	}



	/** */
	@Test(expected = NegativeArraySizeException.class) public void testNewInitializedArray_Object_int_0() {
		ArraysTools.newInitializedArray('a', -1);
	}



	/** */
	@Test public void testNewInitializedArray_Object_int_1() {
		assertArrayEquals(new Object[]{}, ArraysTools.newInitializedArray("abc", 0));
	}



	/** */
	@Test public void testNewInitializedArray_Object_int_2() {
		assertArrayEquals(new Object[]{"abc", "abc", "abc", "abc", "abc"}, ArraysTools.newInitializedArray("abc", 5));
	}



	/***/
	@Test(expected = NullPointerException.class) public void testNumberRange_00() {
		ReTools.numberRangeRE(null, null);
	}



	/***/
	@Test(expected = NullPointerException.class) public void testNumberRange_01() {
		ReTools.numberRangeRE("0", null);
	}



	/***/
	@Test(expected = NullPointerException.class) public void testNumberRange_02() {
		ReTools.numberRangeRE(null, "0");
	}



	/***/
	@Test(expected = IllegalArgumentException.class) public void testNumberRange_03() {
		ReTools.numberRangeRE("a", "0");
	}



	/***/
	@Test(expected = IllegalArgumentException.class) public void testNumberRange_04() {
		ReTools.numberRangeRE("0", "a");
	}



	/***/
	@Test(expected = IllegalArgumentException.class) public void testNumberRange_05() {
		ReTools.numberRangeRE("9", "0");
	}



	/***/
	@Test(expected = IllegalArgumentException.class) public void testNumberRange_06() {
		ReTools.numberRangeRE("0", "10");
	}



	/***/
	@Test public void testNumberRange_07() {
		assertEquals("", ReTools.numberRangeRE("", ""));
	}



	/***/
	@Test public void testNumberRange_08() {
		assertEquals("[0-9]", ReTools.numberRangeRE("0", "9"));
	}



	/***/
	@Test public void testNumberRange_09() {
		assertEquals("1234567890123456789", ReTools.numberRangeRE("1234567890123456789", "1234567890123456789"));
	}



	/***/
	@Test public void testNumberRange_10() {
		assertEquals("(?:0[0-9]|10)", ReTools.numberRangeRE("00", "10"));
	}



	/***/
	@Test public void testNumberRange_11() {
		int min = 50984;
		int max = 79759;
		Pattern pattern;
		int digits = 5;
		String format = "%0" + digits + "d";
		int limit = (int) Math.pow(10, digits);

		pattern = Pattern.compile(ReTools.numberRangeRE(String.format(format, min), String.format(format, max)));
		for (int i = 0; i < min; i++) {
			assertFalse(pattern.matcher(String.format(format, i)).matches());
		}
		for (int i = min; i <= max; i++) {
			assertTrue(pattern.matcher(String.format(format, i)).matches());
		}
		for (int i = max + 1; i <= limit; i++) {
			assertFalse(pattern.matcher(String.format(format, i)).matches());
		}
	}



	/** */
	@Test public void testUnzip_byteArrayArray_0() {
		ZipTools.unzip(new byte[]{0x78, (byte) 0xDA}, new byte[]{0x4B, 0x4C, 0x4A, 0x06, 0x00}, null, new byte[]{}, new byte[]{0x02, 0x4D, 0x01, 0x27});
	}



	/** */
	@Test public void testUnzip_byteArrayArray_1() {
		assertEquals("", ZipTools.unzip(new byte[]{0x78, (byte) 0xDA, 0x03, 0x00, 0x00, 0x00, 0x00, 0x01}));
	}



	/** */
	@Test public void testUnzip_byteArrayArray_2() {
		assertEquals("", ZipTools.unzip(new byte[]{0x78, (byte) 0xDA, 0x03, 0x00, 0x00, 0x00, 0x00, 0x01}));
	}



	/** */
	@Test public void testUnzip_byteArrayArray_3() {
		assertEquals("abc", ZipTools.unzip(new byte[]{0x78, (byte) 0xDA, 0x4B, 0x4C, 0x4A, 0x06, 0x00, 0x02, 0x4D, 0x01, 0x27}));
	}



	/** */
	@Test public void testUnzip_byteArrayArray_4() {
		assertEquals("abc", ZipTools.unzip(new byte[]{0x78, (byte) 0xDA}, new byte[]{0x4B, 0x4C, 0x4A, 0x06, 0x00}, new byte[]{}, new byte[]{0x02, 0x4D, 0x01, 0x27}));
	}



	/** */
	@Test(expected = RuntimeException.class) public void testUnzip_byteArrayArray_5() {
		ZipTools.unzip(new byte[]{});
	}



	/** */
	@Test(expected = RuntimeException.class) public void testUnzip_byteArrayArray_6() {
		ZipTools.unzip(new byte[]{0x4B, 0x4C, 0x4A, 0x06, 0x00});
	}



	/** */
	@Test(expected = RuntimeException.class) public void testUnzip_byteArrayArray_7() {
		ZipTools.unzip();
	}



	/** */
	@Test() public void testZip_String_0() {
		assertArrayEquals(new byte[]{0x78, (byte) 0xDA, 0x03, 0x00, 0x00, 0x00, 0x00, 0x01}, ZipTools.zip((String) null));
	}



	/** */
	@Test public void testZip_String_1() {
		assertArrayEquals(new byte[]{0x78, (byte) 0xDA, 0x03, 0x00, 0x00, 0x00, 0x00, 0x01}, ZipTools.zip(""));
	}



	/** */
	@Test public void testZip_String_2() {
		assertArrayEquals(new byte[]{0x78, (byte) 0xDA, 0x4B, 0x4C, 0x4A, 0x06, 0x00, 0x02, 0x4D, 0x01, 0x27}, ZipTools.zip("abc"));
	}



	/**
	 * Teste si les tableaux indiqués sont égaux et que leurs types d'éléments sont les mêmes.
	 * @param expected Tableau attendu.
	 * @param actual Tableau obtenu.
	 */
	@Ignore private static void assertArrayEquals(boolean[] expected, boolean[] actual) {
		if (!Arrays.equals(expected, actual)) {
			fail(String.format("array element type differed, expected=%s actual=%s", Arrays.toString(expected), Arrays.toString(actual)));
		}
	}



	/**
	 * Teste si les tableaux indiqués sont égaux.
	 * @param expected Tableau attendu.
	 * @param actual Tableau obtenu.
	 */
	@Ignore private static void assertArrayEquals(byte[] expected, byte[] actual) {
		org.junit.Assert.assertArrayEquals(expected, actual);
	}



	/**
	 * Teste si les tableaux indiqués sont égaux.
	 * @param expected Tableau attendu.
	 * @param actual Tableau obtenu.
	 */
	@Ignore private static void assertArrayEquals(char[] expected, char[] actual) {
		org.junit.Assert.assertArrayEquals(expected, actual);
	}



	/**
	 * Teste si les tableaux indiqués sont égaux.
	 * @param expected Tableau attendu.
	 * @param actual Tableau obtenu.
	 */
	@Ignore private static void assertArrayEquals(int[] expected, int[] actual) {
		org.junit.Assert.assertArrayEquals(expected, actual);
	}



	/**
	 * Teste si les tableaux indiqués sont égaux.
	 * @param expected Tableau attendu.
	 * @param actual Tableau obtenu.
	 */
	@Ignore private static void assertArrayEquals(Object[] expected, Object[] actual) {
		org.junit.Assert.assertArrayEquals(expected, actual);
	}



	/**
	 * Teste si les tableaux indiqués sont égaux et que leurs types d'éléments sont les mêmes.
	 * @param expected Tableau attendu.
	 * @param actual Tableau obtenu.
	 */
	@Ignore private static void assertArrayEqualsAndSameType(Object[] expected, Object[] actual) {
		if (expected.getClass().getComponentType() != actual.getClass().getComponentType()) {
			fail(String.format("array element type differed, expected=%s actual=%s", expected.getClass().getComponentType(), actual.getClass().getComponentType()));
		}
		org.junit.Assert.assertArrayEquals(expected, actual);
	}



}
