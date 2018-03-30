package toolbox.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import toolbox.*;



/**
 * La classe {@link Page} implémente une page HTML de message de servlet (page d'erreur, d'aide, ...).
 * @author Ludovic WALLE
 */
public class Page {



	/**
	 * @param title Titre de la page non encodé.
	 * @param url URL à laquelle cette page se réfère.
	 */
	private Page(String title, String url) {
		appendHeader(title, url);
	}



	/**
	 * Ajoute un texte.
	 * @param text Texte déjà encodé à ajouter.
	 * @return Cette page.
	 */
	public Page append(String text) {
		page.append("		<p class=\"p1\">" + text + "</p>\n");
		return this;
	}



	/**
	 * Ajoute le pied de page.
	 * @return Cette page.
	 */
	private Page appendFooter() {
		page.append("" + //
		    "		<hr/>\n" + //
		    "	</body>\n" + //
		    "</html>\n");
		return this;
	}



	/**
	 * Ajoute l'entête.
	 * @param title Titre de la page non encodé.
	 * @param url URL à laquelle cette page se réfère.
	 * @return Cette page.
	 */
	private Page appendHeader(String title, String url) {
		page.append("" + //
		    "<!DOCTYPE html>\n" + // optionnel en HTML5, mais nécessaire pour les navigateurs qui ne le connaissent pas
		    "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + //
		    "	<head>\n" + //
		    "		<meta http-equiv=\"Content-Script-Type\" content=\"text/javascript\"/>\n" + //
		    "		<meta http-equiv=\"Content-Style-Type\" content=\"text/css\"/>\n" + //
		    "		<title>" + CustomizedServlet.encodeForHtml(title) + "</title>\n" + //
		    "		<style type=\"text/css\">\n" + //
		    "			h1 {font-family:Tahoma,Arial,sans-serif; color: white; background-color: #525D76; font-size: x-large}\n" + //
		    "			h2 {font-family:Tahoma,Arial,sans-serif; color: white; background-color: #525D76; font-size: medium}\n" + //
		    "			body {font-family:Tahoma,Arial,sans-serif; color: black; background-color: white; font-size: small}\n" + //
		    "			p {font-family:Tahoma,Arial,sans-serif; color: black;background-color: white; font-size: small; margin-bottom: 0cm}\n" + //
		    "			hr {color: #525D76}\n" + //
		    "			ul {margin-top: 0cm; margin-bottom: 0cm}\n" + //
		    "			.b {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:small;font-weight:bolder}\n" + //
		    "			.p1 {padding-left: 1cm}\n" + //
		    "			.p2 {padding-left: 2cm}\n" + //
		    "			.u {font-family:Tahoma,Arial,sans-serif;background-color:white;color:black;font-size:small;text-decoration:underline;}\n" + //
		    "		</style>\n" + //
		    "	</head>\n" + //
		    "	<body>\n" + //
		    "		<h1>" + CustomizedServlet.encodeForHtml(title) + "</h1>\n" + //
		    "		<hr/>\n" + //
		    "		<p><span class=\"b\">URL</span> " + CustomizedServlet.encodeForHtml(url) + "</p>\n");
		return this;
	}



	/**
	 * Ajoute une ligne horizontale.
	 * @return Cette page.
	 */
	public Page appendHr() {
		page.append("		<hr/>\n");
		return this;
	}



	/**
	 * Ajoute une entrée dans une liste à puce.
	 * @param entry Entrée non encodée dans la liste à puce.
	 * @param text Textes non encodés de l'entrée dans la liste à puce. Le premier apparaît sur la même ligne que l'entrée, les suivants sur les lignes suivantes.
	 * @return Cette page.
	 */
	public Page appendItem(String entry, String... text) {
		page.append(indentation + "		<li>");
		if (entry.isEmpty()) {
			page.append("&#160;");
		} else {
			page.append("<span class=\"u\">" + CustomizedServlet.encodeForHtml(entry) + "</span>");
		}
		if (text.length > 0) {
			page.append(": " + CustomizedServlet.encodeForHtml(text[0]));
			for (int i = 1; i < text.length; i++) {
				page.append("<br/>\n" + CustomizedServlet.encodeForHtml(text[i]));
			}
		}
		page.append("</li>\n");
		return this;
	}



	/**
	 * Commence une liste à puce.
	 * @return Cette page.
	 */
	public Page appendListStart() {
		page.append(indentation + "		<ul>\n");
		indentation += "	";
		return this;
	}



	/**
	 * Termine une liste à puce.
	 * @return Cette page.
	 */
	public Page appendListStop() {
		indentation = indentation.substring(1);
		page.append(indentation + "		</ul>\n");
		return this;
	}



	/**
	 * Ajoute une section avec du texte.
	 * @param title Titre non encodé de la section.
	 * @param text Textes non encodés de l'entrée dans la liste à puce. Le premier apparaît sur la même ligne que l'entrée, les suivants sur les lignes suivantes.
	 * @return Cette page.
	 */
	public Page appendSection(String title, String... text) {
		page.append("		<p><span class=\"b\">" + CustomizedServlet.encodeForHtml(title) + "</span>");
		if (text.length > 0) {
			page.append(" " + CustomizedServlet.encodeForHtml(text[0]));
			for (int i = 1; i < text.length; i++) {
				page.append("<br/>\n" + CustomizedServlet.encodeForHtml(text[i]));
			}
		}
		page.append("</p>\n");
		return this;
	}



	/**
	 * Envoie la page. La page est déclarée comme encodée en ISO-8859-1.
	 * @param response Données en sortie de la servlet.
	 * @throws IOException En cas d'erreur d'entrée / sortie.
	 */
	public void sendPage(HttpServletResponse response) throws IOException {
		appendFooter();
		response.setContentLength(page.length());
		response.setContentType(MimeType.XHTML + ";charset=" + Charset.ISO_8859_1.toString());
		response.getOutputStream().print(page.toString());
		response.flushBuffer();
	}



	/**
	 * Envoie la page en tant que page d'erreur.
	 * @param httpCode Code HTTP.
	 * @param response Données en sortie de la servlet.
	 * @throws IOException En cas d'erreur d'entrée / sortie.
	 */
	public void sendPage(int httpCode, HttpServletResponse response) throws IOException {
		response.setStatus(httpCode);
		sendPage(response);
	}



	/**
	 * Retourne le texte de la page.
	 * @return Le texte de la page.
	 */
	@Override public String toString() {
		return page.toString();
	}



	/**
	 * Génère une page d'erreur générale.
	 * @param url URL de la servlet.
	 * @param messages Messages d'erreur.
	 * @return La page d'erreur.
	 */
	public static Page error(String url, String... messages) {
		return new Page("Erreur d'exécution", url).appendSection("Erreur", messages);
	}



	/**
	 * Génère une page d'aide sur la servlet.
	 * @param url URL à laquelle se rapporte la page d'aide.
	 * @param textualDescription Description textuelle de la servlet indépendante de la méthode.
	 * @param methodsDescriptions Descriptions des méthodes de la servlet.
	 * @return La page d'aide.
	 */
	public static Page help(String url, String textualDescription, Collection<MethodDescription> methodsDescriptions) {
		Page page;

		page = new Page("Aide", url);
		page.appendSection("Description", textualDescription);
		for (MethodDescription methodDescription : methodsDescriptions) {
			page.appendHr();
			page.appendSection("Méthodes", FormatTools.formatList("", ", ", ".", methodDescription.getMethods()), methodDescription.getTextualDescription());
			methodDescription.getChecker().appendHelp(page);
		}
		return page;
	}



	/**
	 * Génère une page d'erreur pour combinaison de paramètres invalide.
	 * @param url URL d'où provient le paramètre.
	 * @param combination Combinaison invalide.
	 * @param combinations Combinaisons valides.
	 * @return La page d'erreur.
	 */
	public static Page invalidCombination(String url, String combination, String combinations) {
		return new Page("Combinaisons de paramètres invalide", url).appendSection("Erreur", "La combinaison de paramètres " + combination + " est incompatible avec celles autorisées : " + combinations);
	}



	/**
	 * Génère une page d'erreur pour méthode invalide.
	 * @param url URL d'où provient le paramètre.
	 * @param method Méthode.
	 * @return La page d'erreur.
	 */
	public static Page invalidMethod(String url, String method) {
		return new Page("Méthode invalide", url).appendSection("Méthode", method);
	}



	/**
	 * Génère une page d'erreur pour paramètre invalide.
	 * @param url URL d'où provient le paramètre.
	 * @param parameter Paramètre.
	 * @param value Valeur invalide.
	 * @return La page d'erreur.
	 */
	public static Page invalidParameter(String url, Parameter parameter, String value) {
		Page page;

		page = new Page("Valeur de paramètre invalide", url);
		parameter.appendInvalidValue(page, value);
		return page;
	}



	/**
	 * Génère une page d'erreur pour méthode non prise en compte.
	 * @param url URL d'où provient le paramètre.
	 * @param method Méthode.
	 * @param methods Méthodes prises en compte.
	 * @return La page d'erreur.
	 */
	public static Page notHandledMethod(String url, String method, Set<Method> methods) {
		return new Page("Méthode non prise en compte", url).appendSection("Méthode", "La méhode " + method + " n'est pas une de celles autorisées : " + FormatTools.formatList("(", ", ", ")", methods));
	}



	/**
	 * Génère une page d'erreur pour paramètre inconnu.
	 * @param url URL d'où provient le paramètre.
	 * @param name Nom du paramètre inconnu.
	 * @param parameters Paramètres.
	 * @return La page d'erreur.
	 */
	public static Page unknownParameter(String url, String name, Parameter[] parameters) {
		String helpUrl;
		Page page;

		page = new Page("Paramètre inconnu", url);
		page.appendSection("Erreur", "Le paramètre \"" + name + "\" ne fait pas partie des paramètres reconnus :");
		page.appendListStart();
		for (Parameter parameter : parameters) {
			page.appendItem(parameter.getName());
		}
		page.appendListStop();
		if (url.indexOf('?') == -1) {
			helpUrl = url + "?" + CustomizedServlet.HELP.getName() + "=";
		} else {
			helpUrl = url.subSequence(0, url.indexOf('?')) + "?" + CustomizedServlet.HELP.getName() + "=";
		}
		page.appendSection("Aide sur la servlet");
		page.append("<a href=\"" + helpUrl + "\">" + helpUrl + "</a>");
		return page;
	}



	/**
	 * Indentation pour les listes à puce.
	 */
	private String indentation = "";



	/**
	 * Page en cours de génération.
	 */
	private final StringBuilder page = new StringBuilder();



}
