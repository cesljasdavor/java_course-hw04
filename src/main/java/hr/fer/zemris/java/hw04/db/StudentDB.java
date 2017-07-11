package hr.fer.zemris.java.hw04.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.QueryTokenType;

/**
 * Razred koji predstavlja simulator baze podataka. Razred od korisnika očekuje
 * da unese upit na bazu podataka. Upit na bazu podataka počinje sa ključnom
 * riječju {@value #QUERY}. Nakon ključne riječi query korisnik može dobiti
 * vrijednosti atributa {@link QueryLexer#JMBAG},{@link QueryLexer#FIRST_NAME}
 * ili {@link QueryLexer#LAST_NAME}. Operatore koje može koristiti su mogu se
 * pronaći u dokumentaciju enumeracije {@link QueryTokenType#OPERATOR}. Svaki od
 * izraza može biti odvojen samo binarnim operatorom AND (I). Ukoliko se
 * ispituje sa jednakošću samo jmbag kod rješenje dohvaća u O(1)(+ stilizacija
 * ispisa) zbog toga što je jmbag index u bazi podataka(npr . query jmbag =
 * "0036486648"). Za sve ostale upite složenost je O(n). U slučaju da se baza ne
 * može učitati iz datoteke baca se {@link IOException}
 * 
 * @see StudentDatabase
 * @see StudentRecord
 * @see QueryParser
 * @see QueryLexer
 * 
 * @author Davor Češljaš
 */
public class StudentDB {

	/** Konstanta koja predstavlja nize znakova "query" */
	private static final String QUERY = "query";

	/** Konstanta koja predstavlja nize znakova "exit" */
	private static final String EXIT_SEQUENCE = "exit";

	/** Konstanta koja predstavlja nize znakova "> " */
	private static final String PROMPT_SYMBOL = "> ";

	/**
	 * Metoda od koje započinje izvođenje programa
	 *
	 * @param args
	 *            ovdje se ne koristi
	 * @throws IOException
	 *             U slučaju da se baza ne može učitati iz datoteke
	 */
	public static void main(String[] args) throws IOException {
		// ako se db ne učita program mora puknuti
		StudentDatabase db = new StudentDatabase(Files.readAllLines(Paths.get("./src/main/resources/database.txt")));

		System.out.println("Dobrodošli u StudentDB program. Program obavlja Vaš upit nad internom bazom podataka.\n"
				+ "Za više detalja pogledajte dokumentaciju programa.");
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print(PROMPT_SYMBOL);

			String input = sc.nextLine().trim();
			if (input.equals(EXIT_SEQUENCE)) {
				System.out.println("Doviđenja!");
				break;
			}

			try {
				QueryParser parser = new QueryParser(removeWordQuery(input));
				executeQuery(db, parser);
			} catch (RuntimeException e) {
				// ispisujemo sve poruke o pogrešci ali ne zaustavljamo program
				System.out.println(e.getMessage());
			}
		}
		sc.close();
	}

	/**
	 * Pomoćna metoda koja izvršava upit
	 *
	 * @param db
	 *            primjerak razreda {@link StudentDatabase} nad kojim se
	 *            izvršava upit
	 * @param parser
	 *            primjerak razreda {@link QueryParser} koji se koristi za
	 *            parsiranje upita
	 */
	private static void executeQuery(StudentDatabase db, QueryParser parser) {
		List<StudentRecord> records = new ArrayList<>();
		if (parser.isDirectQuery()) {
			StudentRecord record = db.forJMBAG(parser.getQueriedJMBAG());
			records.add(record);
		} else {
			for (StudentRecord record : db.filter(new QueryFilter(parser.getQuery()))) {
				records.add(record);
			}
		}
		formatAndDisplayOutput(records);
	}

	/**
	 * Pomoćna metoda koja se koristi za formatirani ispis primjeraka razreda
	 * {@link StudentRecord} koji zadovoljavaju upit
	 * 
	 * @param records
	 *            primjerci razreda {@link StudentRecord} koji zadovoljavaju
	 *            upit
	 * 
	 */
	private static void formatAndDisplayOutput(List<StudentRecord> records) {
		int[] columnsLength = findMaxLengths(records);
		StringBuilder sb = new StringBuilder();
		String header = createHeader(columnsLength);
		// gornje crtice
		sb.append(header);
		for (StudentRecord record : records) {
			String formatedRecord = formatRecord(record, columnsLength);
			sb.append(formatedRecord);
		}
		// donje crtice
		sb.append(header);
		sb.append("Ukupan broj n-torki: " + records.size());
		System.out.println(sb.toString());
	}

	/**
	 * Pomoćna metoda koja formatira pojedini primjerak razreda
	 * {@link StudentRecord}
	 *
	 * @param record
	 *            primjerak razreda {@link StudentRecord} koji se formatira
	 * @param columnsLength
	 *            veličine stupaca za pojedini atribut
	 * @return {@link String} reprezentacija formatirano ispisa primjerka
	 *         razreda {@link StudentRecord}
	 */
	private static String formatRecord(StudentRecord record, int[] columnsLength) {
		return String.format(
				"| %-" + columnsLength[StudentDatabase.JMBAG_INDEX] + "s | %-"
						+ columnsLength[StudentDatabase.LAST_NAME_INDEX] + "s | %-"
						+ columnsLength[StudentDatabase.FIRST_NAME_INDEX] + "s | %"
						+ columnsLength[StudentDatabase.GRADE_INDEX] + "d |\n",
				record.getJmbag(), record.getLastName(), record.getFirstName(), record.getGrade());
	}

	/**
	 * Pomoćna metoda koja stvara zaglavlje(ujedino i podnožje)
	 *
	 * @param columnsLength
	 *            veličine stupaca za pojedini atribut
	 * @return zaglavlje (podnožje)
	 */
	private static String createHeader(int[] columnsLength) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < columnsLength.length; i++) {
			sb.append("+");
			for (int j = 0; j < columnsLength[i] + 2; j++) {
				sb.append("=");
			}
		}
		sb.append("+\n");

		return sb.toString();
	}

	/**
	 * Pomoćna metoda koja iz {@link List} primjeraka razreda
	 * {@link StudentRecord} traži veličine stupaca za pojedini atribut
	 *
	 * @param records
	 *            {@link List} primjeraka razreda {@link StudentRecord}
	 * @return veličine stupaca za pojedini atribut
	 */
	private static int[] findMaxLengths(List<StudentRecord> records) {
		int maxJmbag = 0;
		int maxLastName = 0;
		int maxFirstName = 0;
		for (StudentRecord record : records) {
			int jmbagLength = record.getJmbag().length();
			if (jmbagLength > maxJmbag) {
				maxJmbag = jmbagLength;
			}
			int lastNameLength = record.getLastName().length();
			if (lastNameLength > maxLastName) {
				maxLastName = lastNameLength;
			}
			int firstNameLength = record.getJmbag().length();
			if (firstNameLength > maxFirstName) {
				maxFirstName = firstNameLength;
			}
		}
		return new int[] { maxJmbag, maxLastName, maxFirstName, 1 };
	}

	/**
	 * Pomoćna metoda koja iz upita uklanje ključnu riječ {@value #QUERY}.
	 *
	 * @param line
	 *            upit iz kojeg se uklanja ključna riječ {@value #QUERY}.
	 * @return upit bez ključne riječi {@value #QUERY}.
	 */ 
	private static String removeWordQuery(String line) {
		if (!line.startsWith(QUERY)) {
			throw new IllegalArgumentException("Upit na bazu podataka mora početi riječju 'query'!");
		}
		return line.substring(QUERY.length() + 1, line.length()).trim();
	}
}
