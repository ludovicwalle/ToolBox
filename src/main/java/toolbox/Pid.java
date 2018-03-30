package toolbox;

import java.lang.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.*;

import sun.management.*;



/**
 * La classe {@link Pid} permet de récupérer le numéro du processus.
 * @author Ludovic WALLE
 */
public class Pid {



	/**
	 * Retourne le numéro du processus, ou <code>null</code> si il n'est pas disponible.
	 * @return Le numéro du processus, ou <code>null</code> si il n'est pas disponible.
	 */
	@SuppressWarnings("restriction") public static Integer getPid() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		VMManagement vmManagement;
		Field jvm;
		Method method;

		if (!pidComputed) {
			synchronized (OtherTools.class) {
				if (!pidComputed) {
					try {
						jvm = runtime.getClass().getDeclaredField("jvm");
						jvm.setAccessible(true);
						vmManagement = (VMManagement) jvm.get(runtime);
						method = vmManagement.getClass().getDeclaredMethod("getProcessId");
						method.setAccessible(true);
						pid = (Integer) method.invoke(vmManagement);
					} catch (Exception exception) {
						pid = null;
					}
					pidComputed = true;
				}
			}
		}
		return pid;
	}



	/**
	 * Numéro du processus de la JVM, ou <code>null</code> si il n'est pas disponible, significatif uniquement si {@link #pidComputed} est <code>true</code>.
	 */
	private static Integer pid;



	/**
	 * Indicateur de numéro du processus de la JVM déjà calculé.
	 */
	private static boolean pidComputed = false;



}
