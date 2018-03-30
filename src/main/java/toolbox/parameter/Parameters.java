package toolbox.parameter;

import java.util.*;



/**
 * La classe {@link Parameters} contient une liste de paramètres, dans l'ordre de leur spécification.
 * @author Ludovic WALLE
 */
public abstract class Parameters {



	/**
	 * @param model
	 */
	public Parameters(Model model) {
		this.model = model;
	}



	/**
	 * Ajoute le paramètre indiqué à la fin de la liste de paramètres.
	 * @param parameter Paramètre.
	 * @throws ParameterException_UnknownParameter
	 */
	public void add(Parameter parameter) throws ParameterException_UnknownParameter {
		@SuppressWarnings("hiding") Vector<Parameter> parameters;

		if (!model.hasMold(parameter.getMold())) {
			throw new ParameterException_UnknownParameter(parameter.getName(), null);
		}
		if (parametersByName.containsKey(parameter.getName())) {
			parametersByName.get(parameter.getName()).add(parameter);
		} else {
			parameters = new Vector<>();
			parameters.add(parameter);
			parametersByName.put(parameter.getName(), parameters);
		}
		this.parameters.add(parameter);
	}



	/**
	 * Ajoute le paramètre indiqué à la fin de la liste de paramètres.
	 * @param name Nom de paramètre.
	 * @param rawValue Valeur brute.
	 * @throws ParameterException_InvalidValue
	 * @throws ParameterException_UnknownParameter
	 */
	public void add(String name, String rawValue) throws ParameterException_InvalidValue, ParameterException_UnknownParameter {
		add(model.getMold(name).mold(rawValue));
	}



	/**
	 * Vérifie que la liste de paramètres est conforme au modèle.
	 * @throws ParameterException
	 */
	public void check() throws ParameterException {
		model.check(this);
	}



	/**
	 * Teste si au moins un paramètre issu du moule indiqué est spécifié.
	 * @param mold Moule de paramètre.
	 * @return <code>true</code> si au moins un paramètre avec le moule indiqué est spécifié, <code>false</code> sinon.
	 */
	public boolean exists(Mold mold) {
		return parametersByName.containsKey(mold.getName());
	}



	/**
	 * Teste si au moins un paramètre avec le nom indiqué est spécifié.
	 * @param name Nom de paramètre.
	 * @return <code>true</code> si au moins un paramètre avec le nom indiqué est spécifié, <code>false</code> sinon.
	 */
	public boolean exists(String name) {
		return parametersByName.containsKey(name);
	}



	/**
	 * Retourne le nombre de paramètres spécifiés.
	 * @return Le nombre de paramètres spécifiés.
	 */
	public int getCount() {
		return parameters.size();
	}



	/**
	 * Retourne le nombre de paramètres issu du moule indiqué spécifiés.
	 * @param mold Moule de paramètre.
	 * @return Le nombre de paramètres issu du moule indiqué spécifiés.
	 * @throws ParameterException_UnknownParameter
	 */
	public int getCount(Mold mold) throws ParameterException_UnknownParameter {
		return getCount(mold.getName());
	}



	/**
	 * Retourne le nombre de paramètres portant le nom indiqué spécifiés.
	 * @param name Nom de paramètre.
	 * @return Le nombre de paramètres portant le nom indiqué spécifiés.
	 * @throws ParameterException_UnknownParameter
	 */
	public int getCount(String name) throws ParameterException_UnknownParameter {
		@SuppressWarnings("hiding") Vector<Parameter> parameters;

		if ((parameters = parametersByName.get(name)) != null) {
			return parameters.size();
		} else if (model.hasMold(name)) {
			return 0;
		} else {
			throw new ParameterException_UnknownParameter(name, model.getMolds());
		}
	}



	/**
	 * Retourne le paramètre à l'index indiqué.
	 * @param index Index.
	 * @return Le paramètre à l'index indiqué.
	 */
	public Parameter getParameter(int index) {
		return parameters.get(index);
	}



	/**
	 * Retourne le paramètre issu du moule indiqué, si il a été spécifié, <code>null</code> sinon.
	 * @param mold Moule de paramètre.
	 * @return Le paramètre si il a été spécifié, <code>null</code> sinon.
	 * @throws ParameterException_MoreThanOneValue
	 * @throws ParameterException_UnknownParameter
	 */
	public Parameter getParameter(Mold mold) throws ParameterException_MoreThanOneValue, ParameterException_UnknownParameter {
		return getParameter(mold.getName());
	}



	/**
	 * Retourne le paramètre dont le nom est indiqué, si il a été spécifié, <code>null</code> sinon.
	 * @param name Nom de paramètre.
	 * @return Le paramètre si il a été spécifié, <code>null</code> sinon.
	 * @throws ParameterException_MoreThanOneValue
	 * @throws ParameterException_UnknownParameter
	 */
	public Parameter getParameter(String name) throws ParameterException_MoreThanOneValue, ParameterException_UnknownParameter {
		@SuppressWarnings("hiding") Vector<Parameter> parameters;

		if ((parameters = parametersByName.get(name)) != null) {
			if (parameters.size() == 1) {
				return parameters.get(0);
			} else {
				throw new ParameterException_MoreThanOneValue(name);
			}
		} else if (model.hasMold(name)) {
			return null;
		} else {
			throw new ParameterException_UnknownParameter(name, model.getMolds());
		}
	}



	/**
	 * Retourne un tableau contenant les paramètres spécifiés.<br>
	 * Le tableau est vide si il n'y en a pas, jamais <code>null</code>.
	 * @return Un tableau contenant les paramètres spécifiés.
	 */
	public Parameter[] getParameters() {
		return parameters.toArray(new Parameter[parameters.size()]);
	}



	/**
	 * Retourne un tableau contenant les paramètres spécifiés issus du moule indiqué. Le tableau est vide si il n'y en a pas, jamais <code>null</code>.
	 * @param mold Moule de paramètre.
	 * @return Un tableau contenant les paramètres spécifiés issus du moule indiqué.
	 * @throws ParameterException_UnknownParameter
	 */
	public Parameter[] getParameters(Mold mold) throws ParameterException_UnknownParameter {
		return getParameters(mold.getName());
	}



	/**
	 * Retourne un tableau contenant les paramètres spécifiés portant le nom indiqué.<br>
	 * Le tableau est vide si il n'y en a pas, jamais <code>null</code>.
	 * @param name Nom de paramètre.
	 * @return Un tableau contenant les paramètres spécifiés portant le nom indiqué.
	 * @throws ParameterException_UnknownParameter
	 */
	public Parameter[] getParameters(String name) throws ParameterException_UnknownParameter {
		@SuppressWarnings("hiding")
		Vector<Parameter> parameters;

		if ((parameters = parametersByName.get(name)) != null) {
			return parameters.toArray(new Parameter[parameters.size()]);
		} else if (model.hasMold(name)) {
			return null;
		} else {
			throw new ParameterException_UnknownParameter(name, model.getMolds());
		}
	}



	/**
	 * Retourne le modèle de paramètres.
	 * @return Le modèle de paramètres.
	 */
	public Model getParametersModel() {
		return model;
	}



	/**
	 * Modèle de paramètres.
	 */
	private final Model model;



	/**
	 * Valeurs (clé: nom du paramètre; valeur: paramètre (peut être vide, mais ne doit pas être <code>null</code>, ni contenir de <code>null</code>s).
	 */
	private final Vector<Parameter> parameters = new Vector<>();



	/**
	 * Valeurs (clé: nom du paramètre; valeur: paramètre (peut être vide, mais ne doit pas être <code>null</code>, ni contenir de <code>null</code>s).
	 */
	private final SortedMap<String, Vector<Parameter>> parametersByName = new TreeMap<>();



}
