package toolbox.json;

import toolbox.json.JsonObject.*;



/**
 * La classe {@link JsonRuntimeException} d�finit une exception pouvant �tre g�n�r�e lors d'acc�s � des �l�ments Json, lorsque les options de pr�sence ou de valeur ({@link Option}) ne sont pas
 * respect�es.
 * @author Ludovic WALLE
 */
public class JsonRuntimeException extends RuntimeException {



	/**
	 * @param message Message.
	 */
	public JsonRuntimeException(String message) {
		super(message);
	}



}
