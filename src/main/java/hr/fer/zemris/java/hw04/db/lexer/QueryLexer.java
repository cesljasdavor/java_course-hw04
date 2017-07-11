package hr.fer.zemris.java.hw04.db.lexer;

import hr.fer.zemris.java.hw04.db.QueryParser;

/**
 * Razred koji predstavlja leksički analizator koji se koristi prilikom
 * parsiranja primjerkom razreda{@link QueryParser} . Ovaj analizator ulazni niz
 * znakova analizira na sljedeća dva načina:
 * <ol>
 * <li>ukoliko analizator pročita znak " on sve naredne znakove do ponovne
 * pojave tog znaka tumači kao token tipa {@link QueryTokenType#STRING}</li>
 * <li>U suprotnom analizator ostale znakove tumači kao:
 * <ul>
 * <li>{@link QueryTokenType#FIELD_NAME} ako pročita niz "lastName", "firstName"
 * ili "jmbag"</li>
 * <li>{@link QueryTokenType#OPERATOR} ako pročita '>', '<', '<=', '>=', '=',
 * '!=' ili "LIKE"</li>
 * <li>{@link QueryTokenType#AND} ukoliko pročita niz "AND" neovisno o veličini
 * slova</li>
 * </ul>
 * </li>
 * </ol>
 * 
 * @see QueryTokenType
 * @see QueryParser
 * 
 * @author Davor Češljaš
 */
public class QueryLexer {

	/** Konstanta koja predstavlja znak " */
	private static final char QUOUTE = '\"';

	/** Konstanta koja predstavlja znak '!' */
	private static final char EXCLAMATION_MARK = '!';

	/** Konstanta koja predstavlja znak '=' */
	private static final char EQUALS = '=';

	/** Konstanta koja predstavlja nize znakova "lastName" */
	public static final String LAST_NAME = "lastName";

	/** Konstanta koja predstavlja nize znakova "firstName" */
	public static final String FIRST_NAME = "firstName";

	/** Konstanta koja predstavlja nize znakova "jmbag" */
	public static final String JMBAG = "jmbag";

	/** Konstanta koja predstavlja nize znakova "LIKE" */
	public static final String LIKE = "LIKE";

	/** Konstanta koja predstavlja nize znakova "AND" */
	private static final String AND = "AND";

	/** Ulazni niz znakova koji se leksički analizira */
	private char[] data;

	/** Trenutni pozicija u ulaznom nizu znakova */
	private int currentIndex;

	/** Zadnje izvađeni token */
	private QueryToken currentToken;

	/**
	 * Konstruktor koji iz ulaznog teksta inicijalizira ulazni niz znakova .
	 *
	 * @param query
	 *            ulazni tekst koji je potrebno leksički analizirati
	 * @throws QueryLexerException
	 *             ukoliko je kao <b>query</b> predan <b>null</b>
	 * 
	 */
	public QueryLexer(String query) {
		if (query == null) {
			throw new QueryLexerException("Leksičkom analizatoru predan je null");
		}
		data = query.toCharArray();
	}

	/**
	 * Analizator iz predanog ulaznog teksta pokušava izvaditi sljedeći token.
	 * Način vađenja sljedećeg tokena ovisi o ulaznom nizu.. Metoda ujedino
	 * ažurira trenutni token. Izvađeni token sada je ponovo moguće dohvatiti
	 * pozivom metode {@link #getToken()}
	 *
	 * @return sljedeći token iz ulaznog niza
	 * 
	 * @throws QueryLexerException
	 *             ukoliko sljedeći token nije moguće izvaditi, jer znakovi u
	 *             ulaznom nizu ne odgovaraju niti jednom tipu tokena
	 */
	public QueryToken nextToken() {
		extractToken();
		return currentToken;
	}

	/**
	 * Metoda koja dohvaća zadnje izvađeni token. Može vratiti <b>null</b>
	 * ukoliko analiza nije započela, odnosno nikada nije pozvana metoda
	 * {@link #nextToken()}
	 *
	 * @return zadnje izvađeni token ili <b>null</b>
	 */
	public QueryToken getCurrentToken() {
		return currentToken;
	}

	/**
	 * Pomoćna metoda koja ovisno o ulaznom nizu vrši vađenje sljedećeg tokena .
	 * Ako uspije izvađeni token će biti postavljen kao trenutni token
	 * 
	 * @throws QueryLexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractToken() {
		if (currentToken != null && currentToken.getType() == QueryTokenType.EOF) {
			throw new QueryLexerException("Nemam više tokena!");
		}

		skipWhitespaces();

		// jesmo li na kraju
		if (isEOF()) {
			currentToken = new QueryToken(QueryTokenType.EOF, null);
			return;
		}

		// ovdje nisam imao nigdje potrebu za stanjem, odnosno nisam ga nigdje
		// ispitivao
		if (data[currentIndex] == QUOUTE) {
			// preskoči "
			currentIndex++;
			extractString();
		} else {
			extractQuery();
		}

	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena ukoliko leksički
	 * analizator nije pročitao znak "
	 * 
	 * @throws QueryLexerException
	 *             ukoliko vađenje sljedećeg tokena nije uspjelo
	 */
	private void extractQuery() {
		char c = data[currentIndex];
		if (isBeginingOfOperator(c)) {
			// pokušaj vađenja operatora
			extractOperator();
		} else if (Character.isLetter(c)) {
			// pokušaj vađenja lastName, firstName ili jmbag
			extractFieldNameLikeOrAnd();
		} else {
			throw new QueryLexerException("Leksička analiza nije uspjela za znak '" + c + "'!");
		}
	}

	/**
	 * Pomoćna metoda koja vadi sljedeći token tipa
	 * {@link QueryTokenType#OPERATOR}(i to samo "LIKE"),
	 * {@link QueryTokenType#FIELD_NAME} ili {@link QueryTokenType#AND}
	 * 
	 * @throws QueryLexerException
	 *             ukoliko se niz ne može interpretirati kao token tipa
	 *             {@link QueryTokenType#OPERATOR}(i to samo "LIKE") ,
	 *             {@link QueryTokenType#FIELD_NAME} ili
	 *             {@link QueryTokenType#AND}
	 */
	private void extractFieldNameLikeOrAnd() {
		StringBuilder sb = new StringBuilder();

		while (!isEOF() && Character.isLetter(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		}

		String input = sb.toString();

		if (input.equals(LIKE)) {
			currentToken = new QueryToken(QueryTokenType.OPERATOR, input);
		} else if (input.equalsIgnoreCase(AND)) {
			currentToken = new QueryToken(QueryTokenType.AND, input);
		} else if (input.equals(LAST_NAME) || input.equals(FIRST_NAME) || input.equals(JMBAG)) {
			currentToken = new QueryToken(QueryTokenType.FIELD_NAME, input);
		} else {
			throw new QueryLexerException("Ne mogu leksički analizirati niz: " + input);
		}
	}

	/**
	 * Pomoćna metoda koja vadi sljedeći token tipa
	 * {@link QueryTokenType#OPERATOR}(sve osim "LIKE")
	 * 
	 * @throws QueryLexerException
	 *             ukoliko se niz ne može interpretirati kao token tipa
	 *             {@link QueryTokenType#OPERATOR}(osim "LIKE")
	 */
	private void extractOperator() {
		StringBuilder sb = new StringBuilder();
		char previousChar = data[currentIndex++];
		sb.append(previousChar);

		if (previousChar == EQUALS) {
			currentToken = new QueryToken(QueryTokenType.OPERATOR, sb.toString());
			return;
		}

		// znači gledamo sljedeći znak za <=,>= i !=. Naravno ako nije EOF
		if (!isEOF()
				&& (previousChar == EXCLAMATION_MARK && data[currentIndex] == EQUALS || data[currentIndex] == EQUALS)) {
			currentIndex++;
			currentToken = new QueryToken(QueryTokenType.OPERATOR, sb.append(EQUALS).toString());
			return;
		} else if (previousChar == EXCLAMATION_MARK) {
			// ako smo ovdje to znači da je prošli bio '!' a trenutni nije '='.
			// Takav operator ne postoji"
			throw new QueryLexerException("Znak '!' nije operator");
		}
		// znak '>' ili '<'
		currentToken = new QueryToken(QueryTokenType.OPERATOR, sb.toString());

	}

	/**
	 * Pomoćna metoda koja provjerava započinje li znakom <b>c</b> neki od
	 * mogućih operatora
	 *
	 * @param c
	 *            tznak koji se provjerava
	 * @return <b>true</b> ako znakom <b>true</b> započinje neki od mogućih
	 *         operatora, <b>false</b> inače
	 */
	private boolean isBeginingOfOperator(char c) {
		// ostali charovi nisu konstante jer se koriste samo ovdje
		return c == '<' || c == '>' || c == EQUALS || c == EXCLAMATION_MARK;
	}

	/**
	 * Pomoćna metoda koja vrši vađenje sljedećeg tokena tipa
	 * {@link QueryTokenType#STRING}
	 * 
	 * @throws QueryLexerException
	 *             ukoliko se string nikada ne zatvori
	 */
	private void extractString() {
		StringBuilder sb = new StringBuilder();
		char c;
		// petlja će ujedino i preskočiti '\"' koji neću direktno spremati u
		// string
		while (!isEOF() && ((c = data[currentIndex++]) != QUOUTE)) {
			if (isEOF()) {
				throw new QueryLexerException(
						"Niste zatvorili navodnike ,a leksički analizator je došao do kraja upita");
			}
			sb.append(c);
		}

		currentToken = new QueryToken(QueryTokenType.STRING, sb.toString());
	}

	/**
	 * Pomoćna metoda koja ispituje je li predani znak praznina. Kao praznine se
	 * podrazumjevaju znakovi :
	 * <ul>
	 * <li>'\t'</li>
	 * <li>'\r'</li>
	 * <li>' '</li>
	 * </ul>
	 * 
	 *
	 * @param c
	 *            znak koji se provjerava
	 * @return <b>true </b> ukoliko je <b>c</b> praznina, inače vraća
	 *         <b>false</b>
	 */
	private boolean isWhitespace(char c) {
		// budući da je query jednoredčan predpostavljam da samo ovo spada pod
		// bjeline
		return c == '\t' || c == ' ' || c == '\r';
	}

	/**
	 * Pomoćna metoda koja se koristi za preskakanje praznina u ulaznom nizu
	 */
	private void skipWhitespaces() {
		while (!isEOF() && isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Pomoćna metoda koja ispituje jesmo li došli do kraja ulaznog niza
	 *
	 * @return <b>true</b> ukoliko smo došli do kraj niza <b>false</b> inače
	 */
	private boolean isEOF() {
		return currentIndex >= data.length;
	}
}
