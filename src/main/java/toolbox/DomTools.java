package toolbox;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.apache.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.*;



/**
 * La classe {@link DomTools} contient des outils pour manipuler des arbres DOM.
 * @author Ludovic WALLE
 */
public class DomTools {



	/**
	 * @param namespaceName Nom de l'espace de nom par d�faut.
	 * @param namespaceUri URI de l'espace de nom par d�faut.
	 */
	public DomTools(String namespaceName, String namespaceUri) {

		try {
			builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(true);
			namespaces = builderFactory.newDocumentBuilder().getDOMImplementation().createDocument(namespaceUri, namespaceName + ":namespaceHolder", null).getDocumentElement();
			declareNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			declareNamespace(namespaceName, namespaceUri);
		} catch (Exception exception) {
			throw new ExceptionInInitializerError(exception);
		}
	}



	/**
	 * Compte les noeuds correspondant au XPath indiqu� dans l'arbre DOM indiqu�.
	 * @param node Noeud de d�part dans l'arbre DOM.
	 * @param xPath Chemin.
	 * @return Le nombre de noeuds correspondant au XPath indiqu� dans l'arbre DOM indiqu�.
	 * @throws TransformerException
	 */
	public int countNodes(Node node, String xPath) throws TransformerException {
		return XPathAPI.selectNodeList(node, xPath, namespaces).getLength();
	}



	/**
	 * D�clare un espace de nom suppl�mentaire.
	 * @param namespaceName Nom de l'espace de nom.
	 * @param namespaceUri URI de l'espace de nom.
	 * @return Retourne ce parseur DOM.
	 */
	public DomTools declareNamespace(String namespaceName, String namespaceUri) {
		synchronized (namespaces) {
			namespaces.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + namespaceName, namespaceUri);
		}
		return this;
	}



	/**
	 * Supprime tous les noeuds de l'arbre DOM correspondant au XPath indiqu�.
	 * @param node Noeud de d�part dans l'arbre DOM.
	 * @param xPath Chemin.
	 * @throws TransformerException
	 */
	public void delete(Node node, String xPath) throws TransformerException {
		NodeList nodes;

		nodes = XPathAPI.selectNodeList(node, xPath, namespaces);
		for (int i = 0; i < nodes.getLength(); i++) {
			nodes.item(i).getParentNode().removeChild(nodes.item(i));
		}
	}



	/**
	 * Retourne un �l�ment contenant les espaces de noms d�clar�s.
	 * @return Un �l�ment contenant les espaces de noms d�clar�s.
	 */
	public Element getNamespaces() {
		return namespaces;
	}



	/**
	 * Retourne le noeud de l'arbre DOM correspondant au XPath indiqu�, ou <code>null</code> si il n'existe pas.
	 * @param node Noeud de d�part dans l'arbre DOM.
	 * @param xPath Chemin.
	 * @return Le noeud de l'arbre DOM correspondant au XPath indiqu�, ou <code>null</code> si il n'existe pas.
	 * @throws TransformerException
	 */
	public Node getNode(Node node, String xPath) throws TransformerException {
		NodeList nodes;

		nodes = XPathAPI.selectNodeList(node, xPath, namespaces);
		if (nodes.getLength() == 0) {
			return null;
		} else if (nodes.getLength() == 1) {
			return nodes.item(0);
		} else {
			throw new RuntimeException("Il y a plusieurs noeuds \"" + xPath + "\" dans:\n" + DomTools.toString(node));
		}
	}



	/**
	 * Retourne un tableau contenant les noeuds de l'arbre DOM indiqu� correspondant au XPath indiqu�.
	 * @param node Noeud de d�part dans l'arbre DOM.
	 * @param xPath Chemin.
	 * @return Un tableau contenant les noeuds de l'arbre DOM indiqu� correspondant au XPath indiqu�, jamais <code>null</code>.
	 * @throws TransformerException
	 */
	public Node[] getNodes(Node node, String xPath) throws TransformerException {
		NodeList nodeList;
		Node[] nodes;

		nodeList = XPathAPI.selectNodeList(node, xPath, namespaces);
		nodes = new Node[nodeList.getLength()];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = nodeList.item(i);
		}
		return nodes;
	}



	/**
	 * Retourne le contenu textuel du noeud de l'arbre DOM indiqu� correspondant au XPath indiqu�, ou <code>null</code> si il n'existe pas.
	 * @param node Noeud de d�part dans l'arbre DOM.
	 * @param xPath Chemin.
	 * @return Le contenu textuel du noeud de l'arbre DOM indiqu� correspondant au XPath indiqu�, ou <code>null</code> si il n'existe pas.
	 * @throws TransformerException
	 */
	public String getText(Node node, String xPath) throws TransformerException {
		return ((node = getNode(node, xPath)) == null) ? null : node.getTextContent();
	}



	/**
	 * Retourne un tableau contenant le contenu textuel des noeuds de l'arbre DOM indiqu� correspondant au XPath indiqu�.
	 * @param node Noeud de d�part dans l'arbre DOM.
	 * @param xPath Chemin.
	 * @return Un tableau contenant le contenu textuel des noeuds de l'arbre DOM indiqu� correspondant au XPath indiqu�, jamais <code>null</code>.
	 * @throws TransformerException
	 */
	public String[] getTexts(Node node, String xPath) throws TransformerException {
		NodeList nodes;
		String[] texts;

		nodes = XPathAPI.selectNodeList(node, xPath, namespaces);
		texts = new String[nodes.getLength()];
		for (int i = 0; i < nodes.getLength(); i++) {
			texts[i] = nodes.item(i).getTextContent();
		}
		return texts;
	}



	/**
	 * Teste si il existe au moins un noeud correspondant au XPath indiqu� dans l'arbre DOM indiqu�.
	 * @param node Noeud de d�part dans l'arbre DOM.
	 * @param xPath Chemin.
	 * @return Un tableau contenant les noeuds de l'arbre DOM correspondant au XPath indiqu�, jamais <code>null</code>.
	 * @throws TransformerException
	 */
	public boolean hasNode(Node node, String xPath) throws TransformerException {
		return XPathAPI.selectNodeList(node, xPath, namespaces).getLength() > 0;
	}



	/**
	 * Parse les octets indiqu�s.<br>
	 * Cette m�thode peut �tre appel�e simultan�ment par plusieurs threads.
	 * @param bytes Octets � parser.
	 * @return La racine des octets pars�s.
	 * @throws SAXException
	 */
	public Document parse(byte[] bytes) throws SAXException {
		DocumentBuilder builder;
		Document document;

		try {
			synchronized (builders) {
				if (!builders.isEmpty()) {
					builder = builders.pop();
				} else {
					builder = null;
				}
			}
			if (builder == null) {
				builder = builderFactory.newDocumentBuilder();
				builder.setErrorHandler(null);
			}
			document = builder.parse(new ByteArrayInputStream(bytes));
			synchronized (builders) {
				builders.push(builder);
			}
			return document;
		} catch (UnsupportedEncodingException exception) {
			throw new RuntimeException(exception);
		} catch (ParserConfigurationException exception) {
			throw new RuntimeException(exception);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Parse le document xml indiqu�.<br>
	 * Cette m�thode peut �tre appel�e simultan�ment par plusieurs threads.
	 * @param xml Document xml.
	 * @return Le noeud document correspondant au document xml indiqu�.
	 * @throws SAXException
	 */
	public Document parse(String xml) throws SAXException {
		try {
			return parse(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException exception) {
			throw new RuntimeException(exception);
		}
	}



	/**
	 * Retourne le chemin du noeud de l'arbre DOM indiqu�.
	 * @param node Noeud de dans l'arbre DOM.
	 * @return Le chemin du noeud de l'arbre DOM indiqu�.
	 * @throws TransformerException
	 */
	public static String getPath(Node node) throws TransformerException {
		String path = "";

		do {
			path = ((node.getNodeType() == Node.ATTRIBUTE_NODE) ? "@" : "/") + node.getNodeName() + path;
			node = node.getParentNode();
		} while ((node != null) && (node.getNodeType() != Node.DOCUMENT_NODE));
		return path;
	}



	/**
	 * Retourne une chaine contenant le fragment XML correspondant au noeud indiqu� encod� en US-ASCII et sans d�claration XML.<br>
	 * La m�thode retourne une chaine vide si le noeud est <code>null</code>.
	 * @param node Noeud.
	 * @return Une chaine contenant le fragment XML correspondant au noeud indiqu� encod� en US-ASCII et sans d�claration XML.
	 */
	public static String toString(Node node) {
		return toString(node, "");
	}



	/**
	 * Retourne une chaine contenant le r�sultat de l'application de la feuille de style dont le nom est indiqu� au fragment XML correspondant au noeud indiqu�.<br>
	 * La m�thode retourne une chaine vide si le noeud est <code>null</code>.<br>
	 * La m�thode g�re un cache de feuilles de styles et supporte le multithreading.<br>
	 * Un nom de fichier de feuille de style vide est consid�r� comme d�signant une non transformation (rien n'est modifi�), avec un encodage en US-ASCII et pas de d�claration XML.
	 * @param node Noeud.
	 * @param stylesheetFileName Nom du fichier de la feuille de style � appliquer (ne doit pas �tre <code>null</code>).
	 * @return Une chaine contenant le fragment XML correspondant au noeud indiqu� encod� en US-ASCII et sans d�claration XML.
	 */
	public static String toString(Node node, String stylesheetFileName) {
		StringWriter stringWriter;
		Stack<Transformer> transformers;
		Transformer transformer;

		try {
			if (node == null) {
				return "";
			} else {
				synchronized (TRANSFORMERS) {
					if ((transformers = TRANSFORMERS.get(stylesheetFileName)) == null) {
						TRANSFORMERS.put(stylesheetFileName, transformers = new Stack<>());
					}
				}
				synchronized (transformers) {
					if (!transformers.isEmpty()) {
						transformer = transformers.pop();
					} else if (stylesheetFileName.isEmpty()) {
						transformer = TransformerFactory.newInstance().newTransformer();
						transformer.setOutputProperty(OutputKeys.ENCODING, "US-ASCII");
						transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						transformer.setOutputProperty(OutputKeys.METHOD, "xml");
						transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
					} else {
						transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new File(stylesheetFileName)));
					}
				}
				stringWriter = new StringWriter();
				transformer.transform(new DOMSource(node), new StreamResult(stringWriter));
				synchronized (transformers) {
					transformers.push(transformer);
				}
				return stringWriter.toString().replaceAll("\\r\\n", "\n");
			}
		} catch (TransformerException exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}



	/**
	 * Retourne un tableau de chaines contenant les fragments XML correspondant aux noeuds indiqu�s encod�s en US-ASCII et sans d�clarations XML.
	 * @param nodes Liste de noeuds.
	 * @return Un tableau de chaines contenant les fragments XML correspondant aux noeuds indiqu�s encod�s en US-ASCII et sans d�clarations XML.
	 */
	public static String[] toStrings(Node[] nodes) {
		String[] strings;
		int i = 0;

		strings = new String[nodes.length];
		for (Node node : nodes) {
			strings[i++] = toString(node);
		}
		return strings;
	}



	/**
	 * Retourne un tableau de chaines contenant les fragments XML correspondant aux noeuds indiqu�s encod�s en US-ASCII et sans d�clarations XML.
	 * @param nodes Liste de noeuds.
	 * @return Un tableau de chaines contenant les fragments XML correspondant aux noeuds indiqu�s encod�s en US-ASCII et sans d�clarations XML.
	 */
	public static String[] toStrings(NodeList nodes) {
		String[] strings;

		strings = new String[nodes.getLength()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = toString(nodes.item(i));
		}
		return strings;
	}



	/**
	 * Fabrique de builders.
	 */
	private final DocumentBuilderFactory builderFactory;



	/**
	 * Pool de builders.
	 */
	private final Stack<DocumentBuilder> builders = new Stack<>();



	/**
	 * Element contenant les espaces de noms.
	 */
	private final Element namespaces;



	/**
	 * Pools de transformateurs.
	 */
	private static final Map<String, Stack<Transformer>> TRANSFORMERS = new HashMap<>();



}
