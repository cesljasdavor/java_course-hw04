package hr.fer.zemris.java.hw04.db;

/**
 * Sučelje predstavlja strategiju koja dohvaća vrijednost pojedinog atributa iz
 * primjerka razreda {@link StudentRecord}. Sučelje nudi jednu jedinu metodu
 * {@link #get(StudentRecord)} te se time svrstava u funkcijska sučelja.
 * 
 * @see StudentRecord
 * 
 * @author Davor Češljaš
 */
@FunctionalInterface
public interface IFieldValueGetter {

	/**
	 * Metoda dohvaća vrijednost jedne od članskih varijabli razreda
	 * {@link StudentRecord}
	 *
	 * @param record
	 *            primjerak razreda {@link StudentRecord} čiji se atribut
	 *            dohvaća
	 * @return vrijednost određenog atributa(ovisno o implementaciji)
	 */
	public String get(StudentRecord record);
}
