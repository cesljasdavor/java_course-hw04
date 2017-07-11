package hr.fer.zemris.java.hw04.db.lexer;

import hr.fer.zemris.java.hw04.db.StudentDatabase;

/**
 * Enumeracija koja predstavlja tip tokena koji je primjerak razreda
 * {@link SmartToken}. Moguće vrijednosti:
 * <ul>
 * <li>{@link QueryTokenType#FIELD_NAME}</li>
 * <li>{@link QueryTokenType#STRING}</li>
 * <li>{@link QueryTokenType#OPERATOR}</li>
 * <li>{@link QueryTokenType#AND}</li>
 * <li>{@link QueryTokenType#EOF}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public enum QueryTokenType {

	/**
	 * Predstavlja tip tokena koji predstavlja naziv jednog od atributa unutar
	 * sučelja baze podataka modelirane razredom {@link StudentDatabase}.
	 */
	FIELD_NAME,

	/**
	 * Predstavlja tip tokena koji predstavlja string literal koji se nalazi
	 * unutar "(string literal)"
	 */
	STRING,

	/**
	 * Predstavlja tip tokena jednog od mogućih operatora '>', '<', '<=', '>=',
	 * '=', '!=' i "LIKE"
	 */
	OPERATOR,

	/** Predstavlja tip tokena binarnog operator I */
	AND,

	/** Predstavlja tip tokena koji predstavlja kraj ulaznog niza. */
	EOF
}
