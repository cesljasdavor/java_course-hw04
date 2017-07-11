package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

/**
 * Razred predstavlja sučelje za upite na bazu podataka. Razred koristi
 * primjerke razreda {@link StudentRecord} kako bi spremio n-torke iz određene
 * datoteke čiji se reci kao lista {@link List} primjeraka razreda
 * {@link String} predaje konstruktoru {@link #StudentDatabase(List)} Razred
 * nudi sljedeće metode
 * <ul>
 * <li>{@link #forJMBAG(String)}</li>
 * <li>{@link #filter(IFilter)}</li>
 * <li>nadjačanu metodu {@link #toString()}</li>
 * </ul>
 * 
 * @see StudentRecord
 * @see IFilter
 * @author Davor Češljaš
 */
public class StudentDatabase {

	/**
	 * Konstanta koja predstavlja poziciju unutar predanog redka na kojoj se
	 * mora nalaziti primjerak razreda {@link String} koji predstavlja "jmbag"
	 * primjerka razreda {@link StudentRecord}
	 */
	public static final int JMBAG_INDEX = 0;

	/**
	 * Konstanta koja predstavlja poziciju unutar predanog redka na kojoj se
	 * mora nalaziti primjerak razreda {@link String} koji predstavlja
	 * "lastName" primjerka razreda {@link StudentRecord}
	 */
	public static final int LAST_NAME_INDEX = 1;

	/**
	 * Konstanta koja predstavlja poziciju unutar predanog redka na kojoj se
	 * mora nalaziti primjerak razreda {@link String} koji predstavlja
	 * "firstName" primjerka razreda {@link StudentRecord}
	 */
	public static final int FIRST_NAME_INDEX = 2;

	/**
	 * Konstanta koja predstavlja poziciju unutar predanog redka na kojoj se
	 * mora nalaziti primjerak razreda {@link String} koji predstavlja "grade"
	 * primjerka razreda {@link StudentRecord}
	 */
	public static final int GRADE_INDEX = 3;

	/**
	 * Konstanta koja predstavlja znak s kojim se poziva metoda
	 * {@link String#split(String)} nad predanim redkom
	 */
	private static String SPLIT_CHAR = "\t";

	/**
	 * Predstavlja kolekciju indeksiranu po ključu. (Slično indeksu u bazi
	 * podataka)
	 */
	private SimpleHashtable<String, StudentRecord> jmbagIndex;

	/** Predstavlja kolekciju svih studenata u bazi podataka */
	private List<StudentRecord> students;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstruktor će
	 * obaviti parsiranje svih redaka te iz svakog redaka ,ukoliko je to moguće,
	 * stvoriti primjerak razreda {@link StudentRecord}.
	 *
	 * @param entries
	 *            Redci iz kojih se parsiraju primjerci razreda
	 *            {@link StudentRecord}
	 *
	 * @throws IllegalArgumentException
	 *             ukoliko parsiranje određenog redka ne uspije ili primjerak
	 *             razreda {@link StudentRecord} sa određenim jmbagom već
	 *             postoji
	 */
	public StudentDatabase(List<String> entries) {
		if (entries == null) {
			throw new IllegalArgumentException("Ne mogu izraditi bazu studenata, predali ste null!");
		}
		dbCreate(entries);
	}

	/**
	 * Pomoćna metoda koja parsira predane redke u primjerke razreda
	 * {@link StudentRecord}
	 *
	 * @param entries
	 *            Redci iz kojih se parsiraju primjerci razreda
	 *            {@link StudentRecord}
	 * @throws IllegalArgumentException
	 *             ukoliko parsiranje određenog redka ne uspije ili primjerak
	 *             razreda {@link StudentRecord} sa određenim jmbagom već
	 *             postoji
	 */
	private void dbCreate(List<String> entries) {
		jmbagIndex = new SimpleHashtable<>(entries.size());
		students = new ArrayList<>();

		for (String entry : entries) {
			String[] splitted = entry.split(SPLIT_CHAR);
			StudentRecord newStudentRecord = createStudentRecord(splitted);
			String jmbag = newStudentRecord.getJmbag();
			if (jmbagIndex.containsKey(jmbag)) {
				throw new IllegalArgumentException("Jmbag " + newStudentRecord.getJmbag() + " već postoji!");
			}
			students.add(newStudentRecord);
			jmbagIndex.put(jmbag, newStudentRecord);
		}
	}

	/**
	 * Pomoćna metoda koja iz razdvojenog redka stvara i vraća primjerak razreda
	 * {@link StudentRecord}
	 *
	 * @param studentData
	 *            razdvojeni redak
	 * @return novi primjerak razreda {@link StudentRecord} nastao iz predanog
	 *         redka
	 */
	private StudentRecord createStudentRecord(String[] studentData) {
		if (studentData.length != 4) {
			throw new IllegalArgumentException("Ne mogu kreirati studenta iz " + studentData.length + " elemenata");
		}

		int grade;
		try {
			grade = Integer.parseInt(studentData[GRADE_INDEX]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Kao ocjena studenta predano je " + studentData[3]);
		}

		return new StudentRecord(studentData[JMBAG_INDEX], studentData[LAST_NAME_INDEX], studentData[FIRST_NAME_INDEX],
				grade);
	}

	/**
	 * Metoda koja za predani <b>jmbag </b> vraća primjerak razreda
	 * {@link StudentRecord} koji je u bazi podataka spremljen pod tim
	 * <b>jmbag</b>om ili <code>null</code> ukoliko isti ne postoji. Također
	 * metoda za predani argument <code>null</code> vraća <code>null</code>.
	 * Složenost metode je O(1).
	 *
	 * @param jmbag
	 *            traženog primjerka razreda {@link StudentRecord}
	 * @return primjerak razreda {@link StudentRecord} ili <code>null</code>
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return jmbagIndex.get(jmbag);
	}

	/**
	 * Metoda koja pomoćnu konkretne strategije sučelja {@link IFilter} čija se
	 * metoda {@link IFilter#accepts(StudentRecord)} poziva nad svakim elementom
	 * puni {@link List} primjeraka razreda {@link StudentRecord} te referencu
	 * na istu vraća kroz povratnu vrijednost.
	 *
	 * @param filter
	 *            konkretna strategija sučelja {@link IFilter} koja se poziva
	 *            nad svakim primjerkom razreda {@link StudentRecord} unutar
	 *            ovog razreda
	 * @return {@link List} primjeraka razreda {@link StudentRecord} koji su
	 *         zadovoljili {@link IFilter#accepts(StudentRecord)}
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> filteredRecords = new ArrayList<>();

		for (StudentRecord record : students) {
			if (filter.accepts(record)) {
				filteredRecords.add(record);
			}
		}

		return filteredRecords;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--------------------------------------------------------------------------\n");
		for (StudentRecord record : students) {
			sb.append(record).append("\n--------------------------------------------------------------------------\n");
		}
		sb.append("Veličina baze podataka: " + jmbagIndex.size());
		return sb.toString();
	}
}
