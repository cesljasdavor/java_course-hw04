package hr.fer.zemris.java.hw04.db;

/**
 * Razred koji predstavlja jedan uvjetni izraz unutar jednog upita na bazu
 * podataka koja je učitana kao primjerak razreda {@link StudentDatabase}.
 * Razred također predstavlja i nezavršni znak gramatike prilikom parsiranja
 * primjerkom razreda {@link QueryParser} Razred je nepromjenjiv i nudi 3 metode
 * za dohvat članskih varijabli:
 * <ul>
 * <li>{@link #getComparisonOperator()}</li>
 * <li>{@link #getFieldGetter()}</li>
 * <li>{@link #getStringLiteral()}</li>
 * </ul>
 * 
 * Razred nudi jedan konstruktor koji prima vrijednost svake od pojedinih
 * članskih varijabli
 * {@link #ConditionalExpression(IFieldValueGetter, String, IComparisonOperator)}
 * 
 * @see IComparisonOperator
 * @see ComparisonOperators
 * @see IFieldValueGetter
 * @see FieldValueGetters
 * @see StudentDatabase
 * @see QueryParser
 * 
 * @author Davor Češljaš
 */
public class ConditionalExpression {

	/**
	 * Primjerak konkretne strategije {@link IFieldValueGetter}. Neke već
	 * implementirane strategije nalaze se unutar razreda (biblioteke)
	 * {@link FieldValueGetters}
	 */
	private IFieldValueGetter fieldGetter;

	/**
	 * String literal koji se uobičajno dobiva iz korisnikova upita. String se
	 * koristi prilikom usporedbe atributa sa {@link #fieldGetter} koristeći
	 * metodu {@link IComparisonOperator#satisfied(String, String)} od
	 * {@link #comparisonOperator}
	 */
	private String stringLiteral;

	/**
	 * Primjerak konkretne strategije {@link IComparisonOperator}. Neke već
	 * implementirane strategije nalaze se unutar razreda (biblioteke)
	 * {@link ComparisonOperators}
	 */
	private IComparisonOperator comparisonOperator;

	/**
	 * Konstruktor koji se koristi prilikom inicijalizacije primjerka ovog
	 * razreda. Moguće je dohvatiti konkretne strategije
	 * {@link IFieldValueGetter} i {@link IComparisonOperator} kroz razreda
	 * {@link FieldValueGetters} i {@link ComparisonOperators}
	 *
	 * @param fieldGetter
	 *            primjerak konkrente strategije {@link IFieldValueGetter}
	 * @param stringLiteral
	 *            string literal
	 * @param comparisonOperator
	 *            primjerak konkrente strategije {@link IComparisonOperator}
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral,
			IComparisonOperator comparisonOperator) {
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Metoda koja dohvaća primjerak konkrente strategije
	 * {@link IFieldValueGetter} koji je spremljen unutar primjerka ovog razreda
	 *
	 * @return primjerak konkrente strategije {@link IFieldValueGetter} koji je
	 *         spremljen unutar primjerka ovog razreda
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Metoda koja dohvaća string literal spremljen unutar primjerka ovog
	 * razreda
	 *
	 * @return string literal spremljen unutar primjerka ovog razreda
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Metoda koja dohvaća primjerak konkrente strategije
	 * {@link IComparisonOperator} koji je spremljen unutar primjerka ovog
	 * razreda
	 *
	 * @return primjerak konkrente strategije {@link IComparisonOperator} koji
	 *         je spremljen unutar primjerka ovog razreda
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
}
