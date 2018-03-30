package toolbox;

import java.awt.*;



/**
 * La classe {@link ColorTools} contient des m�thodes statiques d'usage g�n�ral relatives aux couleurs.
 * @author Ludovic WALLE
 */
public class ColorTools {



	/**
	 * Calcule une palette contenant le nombre de couleurs indiqu� ne diff�rend que par la teinte (elles ont toutes la m�me saturation et la m�me luminosit�).
	 * @param colorCount Nombre de couleurs dans la palette (>= 0).
	 * @param firstHue Premi�re couleur de la palette ([0..1]).
	 * @param saturation Saturation ([0..1]).
	 * @param brightness Luminosit� ([0..1]).
	 * @return Un tableau de num�ros de couleurs RGB.
	 */
	public static int[] computePallet(int colorCount, float firstHue, float saturation, float brightness) {
		int[] pallet = new int[colorCount];

		for (int i = 0; i < colorCount; i++) {
			pallet[i] = Color.HSBtoRGB((firstHue + ((i * 360f) / colorCount)) / 360f, saturation, brightness) & 0xFFFFFF;
		}
		return pallet;
	}



	/**
	 * Calcule une palette contenant le nombre de couleurs indiqu� ne diff�rend que par la teinte (elles ont toutes la m�me saturation et la m�me luminosit�).
	 * @param colorCount Nombre de couleurs dans la palette (>= 2).
	 * @param color0 Couleur de d�part.
	 * @param color1 Couleur d'arriv�e.
	 * @return Un tableau de num�ros de couleurs RGB.
	 */
	public static int[] computePallet(int colorCount, int color0, int color1) {
		int[] pallet = new int[colorCount];

		for (int i = 0; i < colorCount; i++) {
			for (int shift : new int[]{16, 8, 0}) {
				pallet[i] += ((((color0 >> shift) & 0xFF) * (colorCount - i)) + ((((color1 >> shift) & 0xFF) * i) / (colorCount - 1))) << shift;
			}
		}
		return pallet;
	}



}
