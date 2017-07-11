package hr.fer.zemris.java.hw04.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Razred predstavlja program koji demonstrira korištenja razreda {@link StudentRecord},
 * {@link StudentDatabase} i {@link ConditionalExpression} te biblioteka
 * {@link FieldValueGetters} i {@link ComparisonOperators} kroz rad nad bazom
 * podataka
 * 
 * @author Davor Češljaš
 */
public class Problem2Demo {

	/**
	 * Metoda od koje započinje izvođenje programa.
	 *
	 * @param args
	 *            ne koristi se u ovom programu
	 * @throws IOException
	 *             Ukoliko nije moguće učitati bazu podataka spremljenu u datoteci
	 */
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./src/main/resources/database.txt"));
		StudentDatabase db = new StudentDatabase(lines);
		StudentRecord record = db.forJMBAG("0000000022");
		System.out.println("First name: " + FieldValueGetters.FIRST_NAME.get(record));
		System.out.println("Last name: " + FieldValueGetters.LAST_NAME.get(record));
		System.out.println("JMBAG: " + FieldValueGetters.JMBAG.get(record));

		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Jur*a",
				ComparisonOperators.LIKE);
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		System.out.println(recordSatisfies);

		System.out.println(db);

		System.out.println(db.filter(r -> true));
		System.out.println(db.filter(r -> false));

	}

}
