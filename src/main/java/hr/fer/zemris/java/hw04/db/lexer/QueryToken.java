package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Pomoćni razred koji se koristi prilikom leksičke analize ulaznog niza
 * primjerkom razreda {@link QueryLexer}. Razred predstavlja jedan token (leksičku
 * jedinku). Primjerci ovog razreda su nepromjenjivi.
 * 
 * @author Davor Češljaš
 */
public class QueryToken {
	
	/** Tip leksičke jedinke */
	private QueryTokenType type;
	
	/** Vrijednost leksičke jedinke */
	private String value;
	
	
	/**
	 * Konstruktor koji inicijalizira atribute leksičke jedinke
	 *
	 * @param type
	 *            tip leksičke jedinke
	 * @param value
	 *            vrijednost leksičke jedinke
	 */
	public QueryToken(QueryTokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Dohvaća tip leksičke jedinke
	 *
	 * @return tip leksičke jedinke
	 */
	public QueryTokenType getType() {
		return type;
	}
	
	/**
	 * Dohvaća vrijednost leksičke jedinke
	 *
	 * @return vrijednost leksičke jedinke
	 */
	public String getValue() {
		return value;
	}
	
	
}
