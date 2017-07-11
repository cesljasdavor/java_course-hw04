package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw04.db.lexer.QueryToken;
import hr.fer.zemris.java.hw04.db.lexer.QueryTokenType;

/**
 * Razred koji predstavlja sintaksni analizator. Razred sadrži primjerak razreda
 * {@link QueryLexer} te nad njim poziva {@link QueryLexer#nextToken()} i gradi
 * generativno stablo. Razred prilikom gradnje stabla koristi razred
 * {@link ConditionalExpression}.Razred započinje parsiranje prilikom
 * inicijalizacije (poziva konstruktora). Razred nudi ukupno 3 metode:
 * <ul>
 * <li>{@link #isDirectQuery()}</li>
 * <li>{@link #getQueriedJMBAG()}</li>
 * <li>{@link #getQuery()}</li>i>
 * </ul>
 * 
 * @see QueryLexer
 * @see ConditionalExpression
 * @author Davor Češljaš
 * 
 */
public class QueryParser {

	/** Primjerak leksičkog analizatora koji se koristi prilikom parsiranja. */
	private QueryLexer lexer;

	/** Lista uvjeta dobivenih parsiranje ulaznog niza */
	private List<ConditionalExpression> query;

	/**
	 * Ukoliko se u upitu koristilo samo polje "jmbag" i operator "=" moguć je
	 * dohvat samo jednog primjerka rezreda {@link StudentRecord} koji odgovara
	 * tom jmbagu (jmbag je unikatan!)
	 */
	private ConditionalExpression directQuery;

	/**
	 * Konstruktor koji stvara primjerak razreda {@link QueryLexer} i predaje mu
	 * predani upit <b>queryContent</b> bez riječi "query". Nakon uspješnog
	 * stvaranja leksičkog analizatora kreće sintaksna analiza unutar koje se
	 * gradi generativno stablo.
	 *
	 * @param queryContent
	 *            upit koji je potrebno parsirati u generativno stablo
	 * 
	 * @throws QueryLexerException
	 *             ukoliko ne uspije leksička analiza
	 * @throws QueryParserException
	 *             ukoliko ne uspije sintaksna analiza
	 */
	public QueryParser(String queryContent) {
		lexer = new QueryLexer(queryContent);
		query = new ArrayList<>();

		parseQuery();
		testAndSetDirectQuery();
	}

	/**
	 * Metoda koja provjerava je li upit direktan, odnosno je li upit oblika
	 * jmbag="..."
	 *
	 * @return <b>true</b> ukoliko je, <b>false</b> inače
	 */
	public boolean isDirectQuery() {
		return directQuery != null;
	}

	/**
	 * Metoda koja vraća vrijednost jmbaga ukoliko je upit direktan. Ukoliko
	 * upit nije direktan baca se {@link IllegalStateException}
	 *
	 * @return jmbag koji je predan kao literal u upitu
	 * 
	 * @throws IllegalStateException
	 *             ukoliko upit nije direktan
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) {
			throw new IllegalStateException("Nisam učitao direktan upit!");
		}
		return directQuery.getStringLiteral();
	}

	/**
	 * Metoda koja dohvaća {@link List} svih primjeraka razreda
	 * {@link ConditionalExpression} koji su sadržani u predanom upitu
	 *
	 * @return {@link List} svih primjeraka razreda
	 *         {@link ConditionalExpression} koji su sadržani u predanom upitu
	 */
	public List<ConditionalExpression> getQuery() {
		return query;
	}

	/**
	 * Pomoćna metoda koja provjerava je li predani upit direktan
	 */
	private void testAndSetDirectQuery() {
		if (query.size() == 1) {
			ConditionalExpression expression = query.get(0);
			// moguća je jedankost po identitetu jer EQUALS i JMBAG
			// postoje na samo jednom mjestu u memoriji
			if (expression.getComparisonOperator() == ComparisonOperators.EQUALS
					&& expression.getFieldGetter() == FieldValueGetters.JMBAG) {
				directQuery = expression;
			}
		}
	}

	/**
	 * Pomoćna metoda koja parsira predani niz znakova koristeći primjerak
	 * razreda {@link QueryLexer} u {@link List} svih primjeraka razreda
	 * {@link ConditionalExpression} koji se iz upita mogu izvaditi
	 * 
	 * @throws QueryParserException
	 *             ukoliko se pokuša parsirati pogrešan upit
	 */
	private void parseQuery() {
		lexer.nextToken();
		while (true) {
			QueryTokenType type = lexer.getCurrentToken().getType();
			if (type == QueryTokenType.EOF) {
				if (isQueryEmpty()) {
					throw new QueryParserException("Predali ste prazan upit na bazu podataka!");
				}
				break;
			}

			if (!isQueryEmpty()) {
				// ovdje mora biti and, ako nije baciti će se exception u
				// daljnim koracima
				lexer.nextToken();
			}
			parseConditionalExpression();
		}
	}

	/**
	 * Pomoćna metoda koja provjerava sadrži li {@link List} primjeraka razreda
	 * {@link ConditionalExpression} iti jedan element
	 *
	 * @return <b>true</b> ukoliko {@link List} primjeraka razreda
	 *         {@link ConditionalExpression} sadrži barem jedan element
	 */
	private boolean isQueryEmpty() {
		return query.size() == 0;
	}

	/**
	 * Pomoćna metoda koja se koristi za parsiranje novog primjerka razreda
	 * {@link ConditionalExpression}
	 */
	private void parseConditionalExpression() {
		IFieldValueGetter fieldGetter = parseFieldName();
		IComparisonOperator comparisonOperator = parseOperator();
		String stringLiteral = parseString();
		query.add(new ConditionalExpression(fieldGetter, stringLiteral, comparisonOperator));
	}

	/**
	 * Pomoćna metoda koja se koristi za parsiranje tokena tipa
	 * {@link QueryTokenType#STRING}
	 *
	 * @return primjerak razreda {@link String} koji se dobije pozivom metode
	 *         {@link QueryToken#getValue()}
	 * @throws QueryParserException
	 *             ukoliko sljedeći primjerak razreda {@link QueryToken} nije
	 *             tipa {@link QueryTokenType#STRING}
	 */
	private String parseString() {
		checkObtainedTokenType(QueryTokenType.STRING);
		String tokenValue = lexer.getCurrentToken().getValue();
		lexer.nextToken();
		return tokenValue;
	}

	/**
	 * Pomoćna metoda koja se koristi za parsiranje tokena tipa
	 * {@link QueryTokenType#OPERATOR}
	 *
	 * @return konkretnu strategiju sučelja {@link IComparisonOperator}
	 * @throws QueryParserException
	 *             ukoliko sljedeći primjerak razreda {@link QueryToken} nije
	 *             tipa {@link QueryTokenType#OPERATOR}
	 */
	private IComparisonOperator parseOperator() {
		checkObtainedTokenType(QueryTokenType.OPERATOR);
		String tokenValue = lexer.getCurrentToken().getValue();
		lexer.nextToken();
		return findComparisonOperator(tokenValue);
	}

	/**
	 * Pomoćna metoda koja pronalazi konketnu strategiju iz
	 * {@link ComparisonOperators} kojoj odgovara predani primjerak razreda
	 * {@link String} <b>value</b>
	 *
	 * @param value
	 *            primjerak razreda {@link String} koji predstavlja operator
	 * @return konketnu strategiju iz {@link ComparisonOperators}
	 */
	private IComparisonOperator findComparisonOperator(String value) {
		// implementacijski detalj: budući da nigdje drugdje ne koristim ove
		// literale , oni nisu odvojeni u konstante
		switch (value) {
		case "<":
			return ComparisonOperators.LESS;
		case "<=":
			return ComparisonOperators.LESS_OR_EQUALS;
		case ">":
			return ComparisonOperators.GREATER;
		case ">=":
			return ComparisonOperators.GREATER_OR_EQUALS;
		case "=":
			return ComparisonOperators.EQUALS;
		case "!=":
			return ComparisonOperators.NOT_EQUALS;
		case QueryLexer.LIKE:
			return ComparisonOperators.LIKE;
		default:
			// ovo se apsolutno nikada ne bi trebalo dogoditi ,ali ...
			throw new QueryParserException("'" + value + "' nije operator");
		}
	}

	/**
	 * Pomoćna metoda koja se koristi za parsiranje tokena tipa
	 * {@link QueryTokenType#FIELD_NAME}
	 *
	 * @return konkretnu strategiju sučelja {@link IFieldValueGetter}
	 * @throws QueryParserException
	 *             ukoliko sljedeći primjerak razreda {@link QueryToken} nije
	 *             tipa {@link QueryTokenType#FIELD_NAME}
	 */
	private IFieldValueGetter parseFieldName() {
		checkObtainedTokenType(QueryTokenType.FIELD_NAME);
		String tokenValue = lexer.getCurrentToken().getValue();
		lexer.nextToken();
		return findFieldValueGetter(tokenValue);
	}

	/**
	 * Pomoćna metoda koja pronalazi konketnu strategiju iz
	 * {@link FieldValueGetters} kojoj odgovara predani primjerak razreda
	 * {@link String} <b>value</b>
	 *
	 * @param value
	 *            primjerak razreda {@link String} koji predstavlja naziv
	 *            atributa
	 * @return konketnu strategiju iz {@link FieldValueGetters}
	 */
	private IFieldValueGetter findFieldValueGetter(String value) {
		switch (value) {
		case QueryLexer.LAST_NAME:
			return FieldValueGetters.LAST_NAME;
		case QueryLexer.FIRST_NAME:
			return FieldValueGetters.FIRST_NAME;
		case QueryLexer.JMBAG:
			return FieldValueGetters.JMBAG;
		default:
			// ovo se apsolutno nikada ne bi trebalo dogoditi ,ali ...
			throw new QueryParserException("'" + value + "' nije pretraživo ime niti jednog polja u bazi podataka.");
		}
	}

	/**
	 * Pomoćna metoda koja ispituje da li je trenutni tip tokena dobiven pozivom
	 * {@link QueryLexer#getCurrentToken()} jednka predanom tipu <b>expected</b>
	 *
	 * @param expected
	 *            primjerak enumeracije {@link QueryTokenType} koji se ispituje
	 * @throws QueryParserException
	 *             ukoliko se tipovi tokena ne podudaraju
	 */
	private void checkObtainedTokenType(QueryTokenType expected) {
		QueryTokenType currentTokenType = lexer.getCurrentToken().getType();
		if (currentTokenType != expected) {
			throw new QueryParserException(
					"Očekivani tip tokena: " + expected + " dobiveni: " + lexer.getCurrentToken().getType());
		}
	}

}
