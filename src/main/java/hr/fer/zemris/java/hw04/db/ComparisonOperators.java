package hr.fer.zemris.java.hw04.db;

/**
 * Razred (biblioteka) koji kroz statičke konstante nudi konkretne strategije
 * sučelja {@link IComparisonOperator}. Razred nudi ukupno 7 statičkih konstanti
 * (implementacija):
 * <ul>
 * <li>{@link #LESS}</li>
 * <li>{@link #LESS_OR_EQUALS}</li>
 * <li>{@link #GREATER}</li>
 * <li>{@link #GREATER_OR_EQUALS}</li>
 * <li>{@link #EQUALS}</li>
 * <li>{@link #NOT_EQUALS}</li>
 * <li>{@link #LIKE}</li>
 * </ul>
 * 
 * @see IComparisonOperator
 * 
 * @author Davor Češljaš
 */
public class ComparisonOperators {

	/**
	 * Konkretna strategija sučelja {@link IComparisonOperator} koja uspoređuje
	 * je li primjerak razreda {@link String} <b>v1</b> manji od primjerka
	 * razreda {@link String} <b>v2</b>. Usporedba se vrši uprabom metode
	 * {@link String#compareTo(String)}
	 */
	public static final IComparisonOperator LESS = (v1, v2) -> v1.compareTo(v2) < 0;

	/**
	 * Konkretna strategija sučelja {@link IComparisonOperator} koja uspoređuje
	 * je li primjerak razreda {@link String} <b>v1</b> manji ili jednaka
	 * primjerku razreda {@link String} <b>v2</b>. Usporedba se vrši uprabom
	 * metode {@link String#compareTo(String)}
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) <= 0;

	/**
	 * Konkretna strategija sučelja {@link IComparisonOperator} koja uspoređuje
	 * je li primjerak razreda {@link String} <b>v1</b> veća od primjerka
	 * razreda {@link String} <b>v2</b>. Usporedba se vrši uprabom metode
	 * {@link String#compareTo(String)}
	 */
	public static final IComparisonOperator GREATER = (v1, v2) -> v1.compareTo(v2) > 0;

	/**
	 * Konkretna strategija sučelja {@link IComparisonOperator} koja uspoređuje
	 * je li primjerak razreda {@link String} <b>v1</b> veća ili jednaka
	 * primjerku razreda {@link String} <b>v2</b>. Usporedba se vrši uprabom
	 * metode {@link String#compareTo(String)}
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) >= 0;

	/**
	 * Konkretna strategija sučelja {@link IComparisonOperator} koja uspoređuje
	 * je li primjerak razreda {@link String} <b>v1</b> jednaka primjerku
	 * razreda {@link String} <b>v2</b>. Usporedba se vrši uprabom metode
	 * {@link String#compareTo(String)}
	 */
	public static final IComparisonOperator EQUALS = (v1, v2) -> v1.compareTo(v2) == 0;

	/**
	 * Konkretna strategija sučelja {@link IComparisonOperator} koja uspoređuje
	 * je li primjerak razreda {@link String} <b>v1</b> različit od primjerku
	 * razreda {@link String} <b>v2</b>. Usporedba se vrši uprabom metode
	 * {@link String#compareTo(String)}
	 */
	public static final IComparisonOperator NOT_EQUALS = (v1, v2) -> v1.compareTo(v2) != 0;

	/**
	 * Konkretna strategija sučelja {@link IComparisonOperator} koja uspoređuje
	 * je li primjerak razreda {@link String} <b>v1</b> sličan primjerku razreda
	 * {@link String} <b>v2</b>. Sličnost se određuje pozivima metoda
	 * {@link String#startsWith(String)} i/ili {@link String#endsWith(String)}
	 * ovisno o položaju znaka '*' u <b>v2</b>. Ukoliko znak '*' ne postoji
	 * usporedba se vrši pomoću metode {@link String#equals(Object)}
	 */
	public static final IComparisonOperator LIKE = new IComparisonOperator() {
		private static final String STAR_OPERATOR = "*";

		@Override
		public boolean satisfied(String value1, String value2) {
			int starIndex = value2.indexOf(STAR_OPERATOR);
			if (starIndex == -1) {
				return value1.equals(value2);
			}
			// sada znamo da '*' postoji
			String startString = value2.substring(0, starIndex);
			String endString = value2.substring(starIndex + 1, value2.length());

			if (startString.contains(STAR_OPERATOR) || endString.contains(STAR_OPERATOR)) {
				// ako i nakon izbacivanja '*' i dalje postoji znači da je
				// predao previše '*'
				throw new IllegalArgumentException("Upisali ste pre velik broj znakova '*'");
			}

			if (starIndex == 0) {
				// zvjezdica na početku
				return value1.endsWith(endString);
			} else if (starIndex == value2.length() - 1) {
				// zvjezdica na kraju
				return value1.startsWith(startString);
			}
			// '*' je negdje drugdje, ali treba paziti da je value1 po
			// duljini veći od preostalih znakova '*'
			return value1.length() >= (startString.length() + endString.length()) && value1.startsWith(startString)
					&& value1.endsWith(endString);
		}
	};
}
