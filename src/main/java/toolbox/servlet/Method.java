package toolbox.servlet;



/**
 * La classe {@link Method} recense les méthodes HTTP implémentés.
 * @author Ludovic WALLE
 */
public enum Method {
	/** Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.7"}. */
	DELETE,
	/** Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.3"}. */
	GET,
	/** Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.4"}. */
	HEAD,
	/** Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.5"}. */
	POST,
	/** Voir {@link "http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.6"}. */
	PUT
}
