package hr.fer.zemris.java.hw04.collections;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable.TableEntry;

public class SimpleHashtableTest {
	
	@Test
	public void testiranjeUnosaUTablicu() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test", -42);
		assertEquals(table.size(), 1);
	}

	@Test
	public void testiranjeMetodeSize() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		assertEquals(table.size(), 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjeUnosaKljucNull() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put(null, -42);
	}

	@Test
	public void testiranjeUnosaVrijednostNull() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test", null);
		table.put("A", 2);
		assertEquals(table.containsValue(null), true);
	}

	@Test
	public void testiranjeDohvataUnosa() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		assertEquals(table.get("Test3"), Integer.valueOf(3));
	}

	@Test
	public void testiranjeDohvataNepostojecegUnosa() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		assertEquals(table.get("Nema me!"), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjePogresanDohvat() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.get(null);
	}

	@Test
	public void testiranjeSadrzajaTablice() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test4", null);
		table.put("Test3", 3);
		assertEquals(table.containsKey("Test1"), true);
		assertEquals(table.containsValue(2), true);
		assertEquals(table.containsKey("Nema me!"), false);
		assertEquals(table.containsValue(42), false);
		assertEquals(table.containsValue(null), true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjeSadrziLiTablicaKljucNull() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		table.containsKey(null);
	}

	@Test
	public void testiranjeUklanjanjaIzListe() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		assertEquals(table.containsKey("Test1"), true);
		table.remove("Test1");
		assertEquals(table.containsKey("Test1"), false);
	}

	@Test
	public void testiranjeUklanjanjaIzListePredanNull() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		// ništa se nije dogodilo
		assertEquals(table.size(), 4);
		table.remove(null);
		assertEquals(table.size(), 4);
	}

	@Test
	public void testiranjeUklanjanjaIzListePredanKriviArgument() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		// ništa se nije dogodilo
		assertEquals(table.size(), 4);
		table.remove(5);
		assertEquals(table.size(), 4);
	}

	@Test
	public void testiranjeMetodeClear() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		// ništa se nije dogodilo
		table.clear();
		assertEquals(table.size(), 0);
	}

	// testiranje iteratora. Savjetuje se pokretanje IteratorDemo

	@SuppressWarnings("unused")
	@Test
	public void testiranjeNormalnaIteracija() {
		// samo da se pokaže da neće puknuti nikakav exception
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		for (TableEntry<String, Integer> entry : table) {
		}
	}

	@Test(expected = NoSuchElementException.class)
	public void testiranjeIteratoraBezHasNext() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		Iterator<TableEntry<String, Integer>> iterator = table.iterator();
		while (true) {
			iterator.next();
		}
	}

	@Test
	public void testiranjeIteratoraUklanjenjeUnosaIspravno() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		Iterator<TableEntry<String, Integer>> iterator = table.iterator();
		iterator.next();
		iterator.remove();
		assertEquals(table.size(), 3);
	}
	
	@Test(expected=ConcurrentModificationException.class)
	public void testiranjeIteratoraUklanjenjeUnosaNeispravno() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		for (TableEntry<String, Integer> entry : table) {
			table.remove(entry.getKey());
		}
	}
	
	@Test(expected = IllegalStateException.class)
	public void testiranjeIteratoraVisestrukoUklanjanje() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		//dva puta uklanjanje istog elementa
		Iterator<TableEntry<String, Integer>> iterator = table.iterator();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}
	
	@Test(expected = ConcurrentModificationException.class)
	public void testiranjeParalelnogRadaDvaIteratoraNeispravno() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Test1", 1);
		table.put("Test2", 2);
		table.put("Test3", 3);
		table.put("Test4", 4);
		// dva iteratora ne komuniciraju medusobno pa opet puca ConcurrentModificationException
		Iterator<TableEntry<String, Integer>> iterator = table.iterator();
		Iterator<TableEntry<String, Integer>> iterator2 = table.iterator();
		iterator2.next();
		iterator2.remove();
		iterator.hasNext();
	}


}
