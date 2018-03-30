package toolbox.servlet;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;

import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

import toolbox.*;



/**
 * La classe {@link CustomizedRequest} étend la classe {@link HttpServletRequest}, simplifiant le traitement des données encodées en multipart/form-data par attachement. Les méthodes
 * {@link #getParameterMap()} , {@link #getParameterNames()}, {@link #getParameter(String)}, {@link #getParameterValues(String)} et {@link #getQueryString()} subissent une correction d'encodage des
 * noms et des valeurs des paramètres.<br>
 * @author Ludovic WALLE
 */
public class CustomizedRequest extends HttpServletRequestWrapper {



	/**
	 * @param request Données d'entrée de la servlet.
	 * @throws IOException
	 */
	public CustomizedRequest(HttpServletRequest request) throws IOException {
		super(request);

		Map<String, Vector<String>> map = new HashMap<>();
		Vector<String> strings;
		Vector<FilePart> parts;
		String name;

		this.attachmentsMap = new HashMap<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			for (Entry<String, String[]> entry : super.getParameterMap().entrySet()) {
				map.put(entry.getKey(), strings = new Vector<>());
				for (String string : entry.getValue()) {
					strings.add(string);
				}
			}
			try {
				for (FileItem item : new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request)) {
					if (item.isFormField()) {
						if (!map.containsKey(item.getFieldName())) {
							map.put(item.getFieldName(), new Vector<String>());
						}
						map.get(item.getFieldName()).add(item.getString());
					} else {
						this.attachmentsMap.put(item.getName(), new FilePart(item));
					}
				}
			} catch (FileUploadException exception) {
				throw new IOException(exception);
			}
			this.valuesMap = new HashMap<>(map.size());
			for (Entry<String, Vector<String>> entry : map.entrySet()) {
				this.valuesMap.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
			}
		} else {
			this.valuesMap = super.getParameterMap();
		}
		this.names = this.valuesMap.keySet().toArray(new String[this.valuesMap.size()]);
		if (request.getCharacterEncoding() == null) {
			/* Heuristique: les paramètres qui arrivent pourraient être considérés comme encodés en Windows-1252 alors qu'ils le seraient en UTF-8 => corriger si ça ne provoque pas d'erreur d'encodage */
			boolean possible = true;

			for (String[] values : this.valuesMap.values()) {
				for (String value : values) {
					if (!value.contains("\u00EF\u00BF\u00BD") && (new String(value.getBytes("ISO-8859-1"), "UTF-8").indexOf('\uFFFD') != -1)) {
						possible = false;
					}
				}
			}
			for (FilePart part : this.attachmentsMap.values()) {
				if (!part.getName().contains("\u00EF\u00BF\u00BD") && (new String(part.getName().getBytes("ISO-8859-1"), "UTF-8").indexOf('\uFFFD') != -1)) {
					possible = false;
				}
			}
			if (possible) {
				for (String[] values : this.valuesMap.values()) {
					for (int i = 0; i < values.length; i++) {
						values[i] = new String(values[i].getBytes("ISO-8859-1"), "UTF-8");
					}
				}
				parts = new Vector<>();
				parts.addAll(this.attachmentsMap.values());
				this.attachmentsMap.clear();
				for (FilePart part : parts) {
					name = new String(part.getName().getBytes("ISO-8859-1"), "UTF-8");
					this.attachmentsMap.put(name, new FilePart(part, name));
				}
			}
		}
	}



	/**
	 * Retourne l'unique fichier attaché, ou <code>null</code> si il n'y en a pas.
	 * @return L'unique fichier attaché, ou <code>null</code> si il n'y en a pas.
	 */
	public FilePart getFilePart() {
		if (attachmentsMap.size() > 1) {
			throw new RuntimeException("Il y a plus d'un fichier attaché.");
		}
		return attachmentsMap.values().iterator().next();
	}



	/**
	 * Retourne une collection contenant les fichiers attachés.
	 * @return Une collection contenant les fichiers attachés.
	 */
	public Collection<FilePart> getFileParts() {
		return attachmentsMap.values();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public String getParameter(String name) {
		String[] values;

		if ((values = valuesMap.get(name)) == null) {
			return null;
		} else {
			return values[0];
		}
	}



	/**
	 * {@inheritDoc}
	 */

	@Override public Map<String, String[]> getParameterMap() {
		return valuesMap;
	}



	/**
	 * {@inheritDoc}
	 */

	@Override public Enumeration<String> getParameterNames() {
		return new ArrayEnumeration<>(names);
	}



	/**
	 * {@inheritDoc}
	 */

	@Override public String[] getParameterValues(String name) {
		return valuesMap.get(name);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public FilePart getPart(String name) {
		return attachmentsMap.get(name);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override public Collection<Part> getParts() {
		Vector<Part> parts = new Vector<>();

		for (Part part : attachmentsMap.values()) {
			parts.add(part);
		}
		return parts;
	}



	/**
	 * {@inheritDoc}<br>
	 * La valeur est calculée d'après les paramètres.
	 */
	@Override public String getQueryString() {
		return computeQueryString(valuesMap);
	}



	/**
	 * Calcule le paramètre QUERY_STRING.
	 * @param parameters Paramètres.
	 * @return Le paramètre QUERY_STRING calculé.
	 */
	public static String computeQueryString(Map<String, String[]> parameters) {
		StringBuilder builder = new StringBuilder();
		String separator = "";

		try {
			for (Entry<String, String[]> values : parameters.entrySet()) {
				if (values.getValue().length == 0) {
					builder.append(String.format("%s%s", separator, values.getKey()));
					separator = "+";
				} else {
					for (String value : values.getValue()) {
						if (value.isEmpty()) {
							builder.append(String.format("%s%s", separator, values.getKey()));
						} else {
							builder.append(String.format("%s%s=%s", separator, values.getKey(), URLEncoder.encode(value, "UTF-8").replaceAll("%5C", "\\\\").replaceAll("%5B", "[").replaceAll("%5D", "]").replaceAll("%7C", "|")));
						}
						separator = "&";
					}
				}
			}
			return builder.toString();
		} catch (UnsupportedEncodingException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Attachements.
	 */
	private final Map<String, FilePart> attachmentsMap;



	/**
	 * Noms des paramètres de la servlet.
	 */
	private final String[] names;



	/**
	 * Valeurs des paramètres de la servlet.
	 */
	private final Map<String, String[]> valuesMap;



}
