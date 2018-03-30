package toolbox;

import java.util.*;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;



/**
 * La classe {@link MailTools} contient des méthodes statiques d'usage général relatives à l'envoi de méls.
 * @author Ludovic WALLE
 */
public abstract class MailTools {



	/**
	 * Envoit un mail en "<code>text/html; charset=ISO8859-1</code>".
	 * @param from Expéditeur.
	 * @param to Destinataire.
	 * @param subject Sujet.
	 * @param htmlText Texte du mél en "<code>text/html; charset=ISO8859-1</code>".
	 * @param attachmentsFilesNames Noms des éventuels fichiers joints.
	 * @throws MessagingException En cas d'erreur d'envoi du mél.
	 */
	public static void sendHtmlMail(String from, String to, String subject, String htmlText, String... attachmentsFilesNames) throws MessagingException {
		sendMail(from, to, subject, htmlText, "text/html; charset=ISO8859-1", attachmentsFilesNames);
	}



	/**
	 * Envoit un mail.
	 * @param from Expéditeur.
	 * @param to Destinataire.
	 * @param subject Sujet.
	 * @param text Texte du mél.
	 * @param textContentType Type et encodage du texte du mél ("<code>text/plain; charset=ISO8859-1</code>", "<code>text/html; charset=UTF-8</code>", ...).
	 * @param attachmentsFilesNames Noms des éventuels fichiers joints.
	 * @throws MessagingException En cas d'erreur d'envoi du mél.
	 */
	public static void sendMail(String from, String to, String subject, String text, String textContentType, String... attachmentsFilesNames) throws MessagingException {
		MimeMessage mail;
		MimeBodyPart part;
		Multipart parts;

		mail = new MimeMessage(Session.getDefaultInstance(new Properties()));
		if (attachmentsFilesNames.length == 0) {
			mail.setContent(text, textContentType);
		} else {
			parts = new MimeMultipart();

			part = new MimeBodyPart();
			part.setContent(text, textContentType);
			part.setHeader("Content-Transfer-Encoding", "quoted-printable");
			parts.addBodyPart(part);

			for (String attachmentFileName : attachmentsFilesNames) {
				part = new MimeBodyPart();
				part.setDataHandler(new DataHandler(new FileDataSource(attachmentFileName)));
				part.setFileName(attachmentFileName);
				parts.addBodyPart(part);
			}
			mail.setContent(parts);
		}

		mail.setFrom(new InternetAddress(from));
		mail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		mail.setSubject(subject);
		mail.setSentDate(new java.util.Date());
		Transport.send(mail);
	}



	/**
	 * Envoit un mail en <code>text/plain; charset=ISO8859-1</code>.
	 * @param from Expéditeur.
	 * @param to Destinataire.
	 * @param subject Sujet.
	 * @param plainText Texte du mél en <code>text/plain; charset=ISO8859-1</code>.
	 * @param attachmentsFilesNames Noms des éventuels fichiers joints.
	 * @throws MessagingException En cas d'erreur d'envoi du mél.
	 */
	public static void sendPlainTextMail(String from, String to, String subject, String plainText, String... attachmentsFilesNames) throws MessagingException {
		sendMail(from, to, subject, plainText, "text/plain; charset=ISO8859-1", attachmentsFilesNames);
	}



}
