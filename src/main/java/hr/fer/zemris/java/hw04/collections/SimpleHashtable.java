package hr.fer.zemris.java.hw04.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Parametrizirani razred koji predstavlja tablicu raspršenog adresiranja čiji
 * su unosi primjerci razreda {@link TableEntry}. Razred nudi sljedeće metode:
 * <ul>
 * <li>{@link #put(Object, Object)}</li>
 * <li>{@link #get(Object)}</li>
 * <li>{@link #size()}</li>
 * <li>{@link #containsKey(Object)}</li>
 * <li>{@link #containsValue(Object)}</li>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #clear()}</li>
 * <li>{@link #toString()}</li>
 * </ul>
 * Razred također sadrži dva konstruktora:
 * <ul>
 * <li>{@link #SimpleHashtable()}</li>
 * <li>{@link #SimpleHashtable(int)}</li>
 * </ul>
 * Razred također implementira sučelje {@link Iterable} te se time omogućava
 * iteracija po svim unosima unutar tablice raspršemog adresiranja.
 * 
 * @param <K>
 *            razred ključa. Vidi {@link TableEntry}
 * @param <V>
 *            razred vrijednosti. Vidi {@link TableEntry}
 * @see TableEntry
 * @see Iterable
 * 
 * @author Davor Češljaš
 */
@SuppressWarnings("unchecked")
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * Defaultni kapacitet tablice raspršenog adresiranja. Ovoliko će slotove u
	 * tablici raspršenog adresiranja biti alocirano ukoliko se u konstruktoru
	 * ne preda inicijalni kapacitet
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * Maksimalni postotak popunjenosti tablice raspršenog adresiranja. Postotak
	 * se računa kao omjer broja elemenata i kapaciteta tablice raspršenog
	 * adresiranja.
	 */
	private static final double MAXIMUM_AVAILABILITY = 75.0;

	/**
	 * Broj koji se koristi za određivanje kapaciteta u
	 * {@link #SimpleHashtable(int)} konstruktoru. Također to je broj kojim se
	 * množi trenutni kapacitet primjerka razreda {@link SimpleHashtable}
	 * prilikom realokacije, odnosno kada popunjenost prijeđe
	 * {@link #MAXIMUM_AVAILABILITY}
	 */
	private static final int ALLOACTION_FACTOR = 2;

	/** Referenca na tablicu raspršenog adresiranja. */
	private TableEntry<K, V>[] table;

	/** Ukupan broj unosa u tablici raspršenog adresiranja. */
	private int size;

	/**
	 * Broj izmjena u tablici raspršenog adresiranja. Kao izmjena se
	 * podrazumjeva svaki poziv metode {@link #put(Object, Object)} te svaki
	 * poziv metode {@link #remove(Object)} koji mijenjaju broj unosa u tablici
	 * raspršenog adresiranja,
	 */
	private long modificationCount;

	/**
	 * Konstruktor koji inicijalizira novi primjerak ovog razreda. Kapacitet
	 * tablice raspršenog adresiranja postavlja se na <b>prvi veći ili
	 * jednak</b> broj od <b>capacity</b> koji je potencija broja
	 * {@value #ALLOACTION_FACTOR}
	 *
	 * @param capacity
	 *            referentni kapacitet prilikom inicijalizacije novog primjerka
	 *            ovog razreda
	 */
	public SimpleHashtable(int capacity) {
		table = (TableEntry<K, V>[]) new TableEntry[findCapacity(capacity)];
	}

	/**
	 * Konstruktor koji inicijalizira novi primjerak ovog razreda. Kapacitet se
	 * postavlja na defaultni, odnosno na {@value #DEFAULT_CAPACITY}
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Metoda za dodavanje novog unosa u tablicu raspršenog adresiranja. Ukoliko
	 * unos sa ključem <b>key</b> već postoji, tada se ažurira vrijednost tog
	 * unosa na <b>value</b>. NAPOMENA: ključ ne smije poprimiti vrijednost
	 * <code>null</code>, dok vrijednost smije.
	 *
	 * @param key
	 *            ključ novog unosa ili ključ unosa koji se želi ažurirati
	 * @param value
	 *            vrijednost koja se postavlja u tablici raspršenog adresiranja
	 *            pod ključem <b>key</b>
	 * 
	 * @throws IllegalArgumentException
	 *             ukoliko se kao ključ <b>key</b> preda <code>null</code>
	 */
	public void put(K key, V value) {
		// ako entry postoji pronađi ga ako ne u entry je spremljena null
		// referenca
		TableEntry<K, V> entry = findEntry(key);
		if (entry != null) {
			entry.value = value;
			System.out.println("Tu sam");
			return;
		}

		needsToReallocate();
		appendSlotList(new TableEntry<>(key, value, null));
		modificationCount++;
		size++;
	}

	/**
	 * Metoda koja dohvaća vrijednost koja je pridružena ključu <b>key</b>.
	 * Ukoliko takav ključ ne postoji vraća se <code>null</code>. Također
	 * ukoliko se za ključ <b>key</b> preda <code>null</code> povratna
	 * vrijednost biti će <code>null</code>
	 *
	 * @param key
	 *            ključ čija se vrijednost dohvaća
	 * @return vrijednost u tablici raspršenog adresiranja koja je pridružena
	 *         ključu <b>key</b> ili <code>null</code> u iznimnim situacijama
	 *         (vidi opis metode)
	 * @throws IllegalArgumentException
	 *             ukoliko se kao ključ <b>key</b> preda <code>null</code>
	 */
	public V get(Object key) {
		try {
			TableEntry<K, V> entry = findEntry((K) key);
			return entry != null ? entry.value : null;
		} catch (ClassCastException e) {
			// dakle K.class i key.getClass nisu jednaki
			return null;
		}
	}

	/**
	 * Metoda koja vraća ukupan broj unosa u tablici raspršenog adresiranja.
	 *
	 * @return ukupan broj unosa u tablici raspršenog adresiranja.
	 */
	public int size() {
		return size;
	}

	/**
	 * Metoda koja ispituje postojanje ključa <b>key</b> u tablici raspršenog
	 * adresiranja. Metoda odgovor dohvaća u složenosti O(1).
	 *
	 * @param key
	 *            ključ za kojeg se ispituje postojanje u tablici raspršenog
	 *            adresiranja
	 * @return <b>true</b> ako ključ <b>key</b> postoji u tablici raspršenog
	 *         adresiranja, <b>false</b> inače
	 * @throws IllegalArgumentException
	 *             ukolik se kao ključ <b>key</b> preda <code>null</code>
	 */
	public boolean containsKey(Object key) {
		try {
			TableEntry<K, V> entry = findEntry((K) key);
			return entry != null;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Metoda koja ispituje postojanje vrijednosti <b>value</b> u tablici
	 * raspršenog adresiranja. Metoda odgovor dohvaća u složenosti O(n).
	 *
	 * @param value
	 *            vrijednost za koju se ispituje postojanje u tablici raspršenog
	 *            adresiranja
	 * @return <b>true</b> ako vrijednost <b>value</b> postoji u tablici
	 *         raspršenog adresiranja, <b>false</b> inače
	 */
	public boolean containsValue(Object value) {
		for (int i = 0; i < table.length; i++) {
			for (TableEntry<K, V> entry = table[i]; entry != null; entry = entry.next) {
				V entryValue = entry.value;
				if (entryValue == null && value != null) {
					// Inace se baca NullPointerException
					return false;
				}
				if (entryValue == value || entryValue.equals(value)) {
					// riješava i problem kada su entryValue i value oboje null
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metoda koja iz tablice raspršenog adresiranja uklanja unos pod ključem
	 * <b>key</b>. Ukoliko takav unos ne postoji ili je predana vrijednost
	 * <code>null</code> tablica neće biti modificirana
	 *
	 * @param key
	 *            ključ unosa koji se uklanja ukoliko takav unos postoji u
	 *            tablici raspršenog adresiranja
	 */
	public void remove(Object key) {
		if (key == null)
			return;

		K keyToRemove = null;
		try {
			keyToRemove = (K) key;
		} catch (ClassCastException e) {
			return;
		}
		int slot = findSlot(keyToRemove);

		if (removeFromSlot(slot, keyToRemove)) {
			size--;
			modificationCount++;
		}

	}

	/**
	 * Metoda koja provjerava je li tablica raspršenog adresiranja prazna.
	 *
	 * @return <b>true</b> ukoliko je tablica raspršenog adresiranja prazna,
	 *         <b>false</b> inače
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Metoda koja uklanja sve unose iz tablice raspršenog adresiranja.
	 */
	public void clear() {
		// nema smisla alocirati novu tablicu
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		// vrati modification count na nulu
		modificationCount = 0;
		size = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(size);
		sb.append("[");
		int counter = 0;
		for (int i = 0; i < table.length; i++) {
			for (TableEntry<K, V> entry = table[i]; entry != null; entry = entry.next) {
				sb.append(entry);
				if (++counter < size) {
					sb.append(", ");
				}
			}
		}
		return sb.append("]").toString();
	}

	/**
	 * Pomoćna metoda koja dodaje novi unos <b>newEntry</b> na kraj liste, u
	 * slotu kojem bi taj unos trebao pripadati.
	 *
	 * @param newEntry
	 *            novi unos u tablici raspršenog adresiranja
	 */
	private void appendSlotList(TableEntry<K, V> newEntry) {
		int slot = findSlot(newEntry.key);
		TableEntry<K, V> entry = table[slot];

		if (entry == null) {
			// dodavanje noda kao prvog u slotu
			table[slot] = newEntry;
		} else {
			// dodavanje na kraj slota
			for (; entry.next != null; entry = entry.next)
				;
			entry.next = newEntry;
		}
	}

	/**
	 * Pomoćna metoda koja vrši uklanjanje unosa pod ključem <b>key</b> u slotu
	 * <b>slot</b> tablice raspršenog adresiranja.
	 *
	 * @param slot
	 *            slot iz kojeg se uklanja unos
	 * @param key
	 *            ključ pod kojim je unos zapisan u tablici raspršenog
	 *            adresiranja
	 * @return <b>true</b> ukoliko je uklanjanje uspjelo, <b>false</b> inače
	 */
	private boolean removeFromSlot(int slot, K key) {
		TableEntry<K, V> entry = table[slot];
		if (entry != null) {
			if (entry.key.equals(key)) {
				// izbaci prvog iz liste
				table[slot] = entry.next;
				return true;
			}
			// izbaci entry koji nije na početku
			for (; entry.next != null; entry = entry.next) {
				if (entry.next.key.equals(key)) {
					entry.next = entry.next.next;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Pomoćna metoda koja ispituje je li potrebna realokacija tablice
	 * raspršenog adresiranja. Realokacija je potrebna ukoliko popunjenost
	 * prijeđe {@value #MAXIMUM_AVAILABILITY}%. Ukoliko je realokacija potrebna
	 * poziva se metoda {@link #reallocate()}
	 */
	private void needsToReallocate() {
		double availability = ((double) size + 1) * 100 / table.length;

		if (availability < MAXIMUM_AVAILABILITY) {
			return;
		}

		reallocate();
	}

	/**
	 * Pomoćna metoda koja vrši realokaciju tablice raspršenog adresiranja. Nova
	 * tablica {@value #ALLOACTION_FACTOR} puta je veće nego što je trenutna.
	 */
	private void reallocate() {
		// zamjena tablica
		TableEntry<K, V>[] currentTable = table;
		table = (TableEntry<K, V>[]) new TableEntry[table.length * ALLOACTION_FACTOR];

		for (int i = 0; i < currentTable.length; i++) {
			for (TableEntry<K, V> entry = currentTable[i]; entry != null; entry = entry.next) {
				appendSlotList(new TableEntry<>(entry.key, entry.value, null));
			}
		}
	}

	/**
	 * Pomoćna metoda koja vrši pretragu tablice raspršemog adresiranja za unos
	 * koji ima ključ <b>key</b>. Metoda unos ,ukoliko on postoji dobavlja u
	 * složenosti O(1). Ukoliko se element ne pronađe metoda vraća
	 * <code>null</code>. NAPOMENA: ključ ne smije biti <code>null</code>, ali
	 * je za vrijednost legalno postaviti <code>null</code>
	 *
	 * @param key
	 *            ključ koji se pretražuje u tablici raspršenog adresiranja
	 * @return unos pod ključem <b>key</b> ili <code>null</code> ukoliko se
	 *         element ne pronađe @throws IllegalArgumentException ukolik se kao
	 *         ključ <b>key</b> preda <code>null</code>
	 * @throws IllegalArgumentException
	 *             ukoliko se kao ključ <b>key</b> preda <code>null</code>
	 */
	private TableEntry<K, V> findEntry(K key) {
		if (key == null) {
			throw new IllegalArgumentException("Ključ ne smije biti null");
		}

		// pronadi prvi cvor u listi
		int slot = findSlot(key);
		TableEntry<K, V> entry = table[slot];
		if (entry != null) {
			for (; entry != null; entry = entry.next) {
				if (entry.key.equals(key)) {
					return entry;
				}
			}
		}

		// takav entry ne postoji
		return null;
	}

	/**
	 * Pomoćna metoda koja traži kapacitet tablice raspršenog adresiranja iz
	 * referntne vrijednosti <b>requestedCapacity</b>. Kapacitet je <b>prvi veći
	 * ili jednak</b> broj od <b>requestedCapacity</b> koji je potencija broja
	 * {@value #ALLOACTION_FACTOR}.
	 *
	 * @param requestedCapacity
	 *            referentna vrijednost pomoću koje se traži kapacitet tablice
	 *            raspršenog adresiranja
	 * @return kapacitet tablice raspršenog adresiranja
	 */
	private int findCapacity(int requestedCapacity) {
		if (requestedCapacity < 1) {
			throw new IllegalArgumentException(
					"Argument mora biti strogo veći od 0, a vi ste predali: " + requestedCapacity);
		}
		// logaritam po bazi 2 pa prvo veće cijelo
		int pow = (int) Math.ceil(Math.log(requestedCapacity) / Math.log(ALLOACTION_FACTOR));
		return (int) Math.pow(ALLOACTION_FACTOR, pow);
	}

	/**
	 * Pomoćna metoda koja vraća broj slota. Metoda predstavlja funkciju
	 * raspršenog adresiranja
	 *
	 * @param key
	 *            ključ za koji se računa vrijednost funkcije raspršenog
	 *            adresiranja
	 * @return vrijednost funkcije raspršenog adresiranja
	 */
	private int findSlot(K key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Ugnježđeni razred razreda {@link SimpleHashtable} koji implementira
	 * sučelje {@link Iterator}. Razred implementira metode:
	 * <ul>
	 * <li>{@link #hasNext()}</li>
	 * <li>{@link #next()}</li>
	 * <li>{@link #remove()}</li>
	 * </ul>
	 * Ostale metode sučelja {@link Iterator} imaju defaultne implementacije.
	 * 
	 * @see Iterator
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/** Trenutni slot u tablici raspršenog adresiranja. */
		private int tableIndex;

		/** Slijedeći unos koji se vraća pozivom metode {@link #next()}. */
		private TableEntry<K, V> currentEntry;

		/**
		 * Zadnje vraćeni unos iz tablice raspršenog adresiranja pozivom metode
		 * {@link #next()}. Ujedino i unos koji će biti uklonjen pozivom metode
		 * {@link #remove()}
		 */
		private TableEntry<K, V> lastReturned;

		/**
		 * Kopija broja izmjena koji se izvršio nad tablicom raspršenog
		 * adresiranja. Ukoliko se ovaj broj razlikuje od broja izmjena unutar
		 * primjerka razreda {@link SimpleHashtable} svaka metoda ovog iteratora
		 * baciti će {@link ConcurrentModificationException}
		 */
		private long iterModificationCount;

		/**
		 * Konstruktor koji inicijalizira primjerak ovog razreda na početne
		 * vrijednosti, te kopira vrijednost
		 * {@link SimpleHashtable#modificationCount}.
		 */
		public IteratorImpl() {
			// početno namještanje
			for (int i = 0; i < table.length; i++) {
				if (table[i] != null) {
					tableIndex = i;
					currentEntry = table[i];
					break;
				}
			}
			// inicijaliziraj modificationCount
			iterModificationCount = modificationCount;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			checkConsistency();
			return currentEntry != null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public SimpleHashtable.TableEntry<K, V> next() {
			checkConsistency();
			if (currentEntry == null) {
				throw new NoSuchElementException("Nema više elemenata");
			}
			lastReturned = currentEntry;
			if (currentEntry.next != null) {
				currentEntry = currentEntry.next;
			} else if (tableIndex + 1 < table.length) {
				findTableIndex();
			} else {
				// gotova je iteracija
				currentEntry = null;
			}
			return lastReturned;
		}

		/**
		 * Pomoćna metoda koja pronalazi sljedeći slot u tablici raspršenog
		 * adresiranja koja sadrži neki unos.
		 */
		private void findTableIndex() {
			//inicijalizacija indexa na prvi sljedeći
			tableIndex++;
			for (; tableIndex < table.length; tableIndex++) {
				if (table[tableIndex] != null) {
					currentEntry = table[tableIndex];
					break;
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			checkConsistency();
			if (lastReturned == null) {
				throw new IllegalStateException("Već ste izbrisali posljednje predani element!");
			}

			SimpleHashtable.this.remove(lastReturned.key);
			lastReturned = null;
			// ažuriram svoj interni modification count
			iterModificationCount = modificationCount;
		}

		/**
		 * Metoda koja provjerava je li iterator u konzistentnom stanju. Stanje
		 * je konzistentno ukoliko se vrijednosti {@link #iterModificationCount}
		 * i {@link SimpleHashtable#modificationCount} podudaraju.
		 * 
		 * @throws ConcurrentModificationException
		 *             ukoliko stanje iteratora nije konzistentno
		 */
		private void checkConsistency() {
			if (iterModificationCount != modificationCount) {
				throw new ConcurrentModificationException(
						"Ne smijete mijenjati sadržaj primjerka razreda SimpleHashtable dok iterirate po njemu");
			}
		}

	}

	/**
	 * Parametrizirani razred koji predstavlja jedan unos u tablici raspršenog
	 * adresiranja razreda {@link SimpleHashtable}. Svaki unos sadrži ključ i
	 * vrijednost. Razred nudi metode:
	 * <ul>
	 * <li>{@link #getKey()}</li>
	 * <li>{@link #getValue()}</li>
	 * <li>{@link #setValue(Object)}</li>
	 * <li>{@link #toString()}</li>
	 * </ul>
	 *
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 */
	public static class TableEntry<K, V> {

		/** Ključ unosa. */
		private K key;

		/** Vrijednost unosa. */
		private V value;

		/** Referenca na sljedeći čvor unutar liste. */
		private TableEntry<K, V> next;

		/**
		 * Konstruktor razreda koji inicijalizira primjerak ovog razreda na
		 * predane vrijednosti.
		 *
		 * @param key
		 *            ključ unosa
		 * @param value
		 *            vrijednost unosa
		 * @param next
		 *            Referenca na sljedeći čvor unutar liste
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		/**
		 * Metoda koja dohvaća ključ unosa.
		 *
		 * @return ključ unosa
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Metoda koja dohvaća vrijednost unosa.
		 *
		 *
		 * @return vrijednost unosa.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Metoda koja postavlja vrijednost unosa na novu vrijednost
		 * <b>value</b>.
		 *
		 * @param value
		 *            nova vrijednost unosa
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}
	}
}
