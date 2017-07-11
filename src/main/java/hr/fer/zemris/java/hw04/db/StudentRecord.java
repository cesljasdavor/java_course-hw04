package hr.fer.zemris.java.hw04.db;

/**
 * Razred koji predstavlja jednu n-torku unutar primjerka razreda
 * {@link StudentDatabase}. Razred je nepromijenjiv te nudi samo metode za
 * dohvat atributa:
 * <ul>
 * <li>{@link #getFirstName()}</li>
 * <li>{@link #getLastName()}</li>
 * <li>{@link #getGrade()}</li>
 * <li>{@link #getJmbag()}</li>
 * </ul>
 * 
 * Razred također nadjačava metode:
 * <ul>
 * <li>{@link #toString()}</li>
 * <li>{@link #hashCode()}</li>
 * <li>{@link #equals(Object)}</li>
 * </ul>
 * 
 * Razred nudi točno jedan konstruktor
 * {@link #StudentRecord(String, String, String, int)}
 * 
 * @see StudentDatabase
 * 
 * @author Davor Češljaš
 */
public class StudentRecord {

	/** Vrijednost atributa "jmbag" za ovu n-torku unutar baze podataka */
	private String jmbag;

	/** Vrijednost atributa "lastName" za ovu n-torku unutar baze podataka */
	private String lastName;

	/** Vrijednost atributa "firstName" za ovu n-torku unutar baze podataka */
	private String firstName;

	/** Vrijednost atributa "grade" za ovu n-torku unutar baze podataka */
	private int grade;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstruktor
	 * postavlja vrijednosti svih atributa na predane vrijednosti :
	 * <b>jmbag</b>, <b>lastName</b>, <b>firstName</b> i <b>grade</b>
	 *
	 * @param jmbag
	 *            Vrijednost atributa "jmbag" za ovu n-torku unutar baze
	 *            podataka
	 * @param lastName
	 *            Vrijednost atributa "lastName" za ovu n-torku unutar baze
	 *            podataka
	 * @param firstName
	 *            Vrijednost atributa "firstName" za ovu n-torku unutar baze
	 *            podataka
	 * @param grade
	 *            Vrijednost atributa "grade" za ovu n-torku unutar baze
	 *            podataka
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int grade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.grade = grade;
	}

	/**
	 * Metoda dohvaća vrijednost atributa "jmbag" za ovu n-torku unutar baze
	 * podataka
	 *
	 * @return vrijednost atributa "jmbag" za ovu n-torku unutar baze podataka
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Metoda dohvaća vrijednost atributa "lastName" za ovu n-torku unutar baze
	 * podataka
	 *
	 * @return vrijednost atributa "lastName" za ovu n-torku unutar baze
	 *         podataka
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Metoda dohvaća vrijednost atributa "firstName" za ovu n-torku unutar baze
	 * podataka
	 *
	 * @return vrijednost atributa "firstName" za ovu n-torku unutar baze
	 *         podataka
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Metoda dohvaća vrijednost atributa "grade" za ovu n-torku unutar baze
	 * podataka
	 *
	 * @return vrijednost atributa "grade" za ovu n-torku unutar baze podataka
	 */
	public int getGrade() {
		return grade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Jmbag: " + jmbag + " Ime i prezime: " + firstName + " " + lastName + " Konačna ocjena: " + grade;
	}
}
