package hr.fer.zemris.java.hw04.db;

/**
 * Sučelje predstavlja strategiju koja ovisno o kriteriju filtriranja prihvaća
 * ili odbacuje pojedini primjerak razreda {@link StudentRecord}. Sučelje nudi
 * jednu jedinu metodu {@link #accepts(StudentRecord)} te se time svrstava u
 * funkcijska sučelja.
 * 
 * @author Davor Češljaš
 */
@FunctionalInterface
public interface IFilter {

	/**
	 * Metoda koja ovisno o implementaciji (kriteriju koji je zadan u
	 * implementaciji) prihvaća ili odbacuje primjerak razreda
	 * {@link StudentRecord}
	 *
	 * @param record
	 *            primjerak razreda {@link StudentRecord} koji se ispituje
	 * @return  <b>true</b> ako je kriterij ispunjen , <b>false</b> inače.
	 */
	public boolean accepts(StudentRecord record);
}
