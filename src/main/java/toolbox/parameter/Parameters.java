package toolbox.parameter;

import java.util.*;



/**
 * La classe {@link Parameters} contient une liste de param�tres, dans l'ordre de leur sp�cification.
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
	 * Ajoute le param�tre indiqu� � la fin de la liste de param�tres.
	 * @param parameter Param�tre.
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
	 * Ajoute le param�tre indiqu� � la fin de la liste de param�tres.
	 * @param name Nom de param�tre.
	 * @param rawValue Valeur brute.
	 * @throws ParameterException_InvalidValue
	 * @throws ParameterException_UnknownParameter
	 */
	public void add(String name, String rawValue) throws ParameterException_InvalidValue, ParameterException_UnknownParameter {
		add(model.getMold(name).mold(rawValue));
	}



	/**
	 * V�rifie que la liste de param�tres est conforme au mod�le.
	 * @throws ParameterException
	 */
	public void check() throws ParameterException {
		model.check(this);
	}



	/**
	 * Teste si au moins un param�tre issu du moule indiqu� est sp�cifi�.
	 * @param mold Moule de param�tre.
	 * @return <code>true</code> si au moins un param�tre avec le moule indiqu� est sp�cifi�, <code>false</code> sinon.
	 */
	public boolean exists(Mold mold) {
		return parametersByName.containsKey(mold.getName());
	}



	/**
	 * Teste si au moins un param�tre avec le nom indiqu� est sp�cifi�.
	 * @param name Nom de param�tre.
	 * @return <code>true</code> si au moins un param�tre avec le nom indiqu� est sp�cifi�, <code>false</code> sinon.
	 */
	public boolean exists(String name) {
		return parametersByName.containsKey(name);
	}



	/**
	 * Retourne le nombre de param�tres sp�cifi�s.
	 * @return Le nombre de param�tres sp�cifi�s.
	 */
	public int getCount() {
		return parameters.size();
	}



	/**
	 * Retourne le nombre de param�tres issu du moule indiqu� sp�cifi�s.
	 * @param mold Moule de param�tre.
	 * @return Le nombre de param�tres issu du moule indiqu� sp�cifi�s.
	 * @throws ParameterException_UnknownParameter
	 */
	public int getCount(Mold mold) throws ParameterException_UnknownParameter {
		return getCount(mold.getName());
	}



	/**
	 * Retourne le nombre de param�tres portant le nom indiqu� sp�cifi�s.
	 * @param name Nom de param�tre.
	 * @return Le nombre de param�tres portant le nom indiqu� sp�cifi�s.
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
	 * Retourne le param�tre � l'index indiqu�.
	 * @param index Index.
	 * @return Le param�tre � l'index indiqu�.
	 */
	public Parameter getParameter(int index) {
		return parameters.get(index);
	}



	/**
	 * Retourne le param�tre issu du moule indiqu�, si il a �t� sp�cifi�, <code>null</code> sinon.
	 * @param mold Moule de param�tre.
	 * @return Le param�tre si il a �t� sp�cifi�, <code>null</code> sinon.
	 * @throws ParameterException_MoreThanOneValue
	 * @throws ParameterException_UnknownParameter
	 */
	public Parameter getParameter(Mold mold) throws ParameterException_MoreThanOneValue, ParameterException_UnknownParameter {
		return getParameter(mold.getName());
	}



	/**
	 * Retourne le param�tre dont le nom est indiqu�, si il a �t� sp�cifi�, <code>null</code> sinon.
	 * @param name Nom de param�tre.
	 * @return Le param�tre si il a �t� sp�cifi�, <code>null</code> sinon.
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
	 * Retourne un tableau contenant les param�tres sp�cifi�s.<br>
	 * Le tableau est vide si il n'y en a pas, jamais <code>null</code>.
	 * @return Un tableau contenant les param�tres sp�cifi�s.
	 */
	public Parameter[] getParameters() {
		return parameters.toArray(new Parameter[parameters.size()]);
	}



	/**
	 * Retourne un tableau contenant les param�tres sp�cifi�s issus du moule indiqu�. Le tableau est vide si il n'y en a pas, jamais <code>null</code>.
	 * @param mold Moule de param�tre.
	 * @return Un tableau contenant les param�tres sp�cifi�s issus du moule indiqu�.
	 * @throws ParameterException_UnknownParameter
	 */
	public Parameter[] getParameters(Mold mold) throws ParameterException_UnknownParameter {
		return getParameters(mold.getName());
	}



	/**
	 * Retourne un tableau contenant les param�tres sp�cifi�s portant le nom indiqu�.<br>
	 * Le tableau est vide si il n'y en a pas, jamais <code>null</code>.
	 * @param name Nom de param�tre.
	 * @return Un tableau contenant les param�tres sp�cifi�s portant le nom indiqu�.
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
	 * Retourne le mod�le de param�tres.
	 * @return Le mod�le de param�tres.
	 */
	public Model getParametersModel() {
		return model;
	}



	/**
	 * Mod�le de param�tres.
	 */
	private final Model model;



	/**
	 * Valeurs (cl�: nom du param�tre; valeur: param�tre (peut �tre vide, mais ne doit pas �tre <code>null</code>, ni contenir de <code>null</code>s).
	 */
	private final Vector<Parameter> parameters = new Vector<>();



	/**
	 * Valeurs (cl�: nom du param�tre; valeur: param�tre (peut �tre vide, mais ne doit pas �tre <code>null</code>, ni contenir de <code>null</code>s).
	 */
	private final SortedMap<String, Vector<Parameter>> parametersByName = new TreeMap<>();



}
