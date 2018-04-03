package toolbox.parallel;



/**
 * Ce package regroupe des classes permettant de parall�liser l'�x�cution de taches. Les classes sont calqu�es m�taphoriquement sur une entreprise.
 * <ul>
 * <li>{@link Mission} contient les caract�ristiques d'une t�che � effectuer.
 * <li>{@link Employee} regroupe les caract�ristiques communes � tous les types d'employ�s.
 * <li>{@link Worker} est un type d'employ� dont le r�le est d'effectuer les t�ches qui lui sont attribu�es. Il ne doit y en avoir qu'un.
 * <li>{@link Missionner} est un type d'employ� dont le r�le est de distribuer les t�ches aux ouvriers.
 * <li>{@link Enterprise} fait fonctionner le tout.
 * </ul>
 * Le nombre d'ouvriers est limit�, souvent inf�rieur au nombre de t�ches � effectuer, mais ils travaillent en parall�le, chacun sur sa t�che.<br>
 * L'entreprise ferme lorsque toutes les t�ches ont �t� effectu�es. Il est possible de suspendre temporairement l'entreprise, de la fermer volontairement, d'embaucher et de d�baucher
 * dynamiquement des ouvriers, de suivre l'�tat d'avancement du travail.<br>
 * <br>
 * Il faut d�river les classes {@link Mission}, {@link Missionner} et {@link Worker} pour les adapter au travail � r�aliser.
 * @author Ludovic WALLE
 */
