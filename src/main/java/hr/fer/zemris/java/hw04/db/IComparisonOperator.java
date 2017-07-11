package hr.fer.zemris.java.hw04.db;

/**
 * Sučelje predstavlja strategiju koja predstavlja bilo koji operator usporedbe.
 * Sučelje nudi jednu jedinu metodu {@link #satisfied(String, String)} te se
 * time svrstava u funkcijska sučelja.
 * 
 * @author Davor Češljaš
 */
@FunctionalInterface
public interface IComparisonOperator {

	/**
	 * Metoda koja ispituje je li uvjet usporedbe zadovoljen.
	 *
	 * @param value1
	 *            vrijednost prvog argumenta usporedbe
	 * @param value2
	 *            vrijednost drugog argumenta usporedbe
	 * @return <b>true</b> ako je uvjet usporedbe ispunjen , <b>false</b> inače.
	 */
	public boolean satisfied(String value1, String value2);
}
