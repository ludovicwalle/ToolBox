package fr.inist.toolbox;

import toolbox.*;

/**
 * La classe {@link EditDistanceTest}
 * @author Ludovic WALLE
 */
public class EditDistanceTest {

	/**
	 * Programme de test.
	 * @param args
	 */
	public static void main(String[] args) {
		int distance;
		int explainedDistance;
		String[][] pairs = new String[][]{ //
		//
		// {"4814paris", "128paris"}, //
		// {"01informatiqueparis1986", "01informatiquemagazine"}, //
		// {"01referencesparis", "01hebdo"}, //
		    {"2aiparis", "128paris"}, //
		    {"abcd", "abc"}, //
		    {"ab", "a"}, //
		    {"ab", "b"}, //
		    {"abcd", "abcd"}, //
		    {"abcd", "bcd"}, //
		    {"abcd", "ad"}, //
		    {"acuiteparis", "actesparis3"}, //
		    {"abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstvwxyz"}, //
		    {"actabotanicaindica", "actabotanicafennica"}, //
		};
		boolean[] flags0;
		boolean[] flags1;

		for (String[] pair : pairs) {
			distance = EditDistance.compute(pair[0], pair[1], 6);
			flags0 = new boolean[pair[0].length()];
			flags1 = new boolean[pair[1].length()];
			explainedDistance = EditDistance.explain(pair[0], pair[1], 6, flags0, flags1);
			System.out.println();
			System.out.println("#" + pair[0] + " ? " + pair[1] + " = " + distance + " / " + explainedDistance);
			if (distance < EditDistance.TOO_FAR) {
				System.out.println("#" + EditDistance.mark(pair[0], flags0, flags1));
				System.out.println("#" + EditDistance.mark(pair[1], flags1, flags0));
			}
			System.out.println();
		}
	}




}
