package hr.fer.zemris.java.hw04.db;

/**
 * Razred (biblioteka) koji kroz statičke konstante nudi konkretne strategije
 * sučelja {@link IFieldValueGetter}. Razred nudi ukupno 3 statičke konstante
 * (implementacije):
 * <ul>
 * <li>{@link #FIRST_NAME}</li>
 * <li>{@link #LAST_NAME}</li>
 * <li>{@link #JMBAG}</li>i>
 * </ul>
 * 
 * @see IFieldValueGetter
 * 
 * @author Davor Češljaš
 */
public class FieldValueGetters {

	/**
	 * Konkretna strategija sučelja {@link IFieldValueGetter} koja dohvaća
	 * primjerak razreda {@link String} koji je spremljen kao atribut
	 * "firstName" unutar primjerka razreda {@link StudentRecord}.
	 */
	public static final IFieldValueGetter FIRST_NAME = record -> record.getFirstName();

	/**
	 * Konkretna strategija sučelja {@link IFieldValueGetter} koja dohvaća
	 * primjerak razreda {@link String} koji je spremljen kao atribut
	 * "lastName" unutar primjerka razreda {@link StudentRecord}.
	 */
	public static final IFieldValueGetter LAST_NAME = record -> record.getLastName();

	/**
	 * Konkretna strategija sučelja {@link IFieldValueGetter} koja dohvaća
	 * primjerak razreda {@link String} koji je spremljen kao atribut
	 * "jmbag" unutar primjerka razreda {@link StudentRecord}.
	 */
	public static final IFieldValueGetter JMBAG = record -> record.getJmbag();
}
