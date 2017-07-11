package hr.fer.zemris.java.hw04.collections;

import java.util.Iterator;

/**
 * Razred koji predstavlja program koji demonstrira rad {@link Iterator}a od
 * primjerka razreda {@link SimpleHashtable}
 * 
 * @author Davor Češljaš
 */
public class IteratorDemo {

	/**
	 * Metoda koja započinje izvođenje programa
	 *
	 * @param args
	 *            ovdje se ne koristi
	 */
	public static void main(String[] args) {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}

		System.out.println();

		for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
			for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
				System.out.printf("(%s => %d) - (%s => %d)%n", pair1.getKey(), pair1.getValue(), pair2.getKey(),
						pair2.getValue());
			}
		}
		/*
		 * Može se izvesti samo jedna pogreška. Odkomentiraj jedan po jedan.
		 * PAZI! ako odkomentiras 2 iterator odavde, 3 i 4 iterator neće baciti
		 * iznimku jer je "Ivana" već vani
		 */

		// Iterator<SimpleHashtable.TableEntry<String, Integer>> iterator =
		// examMarks.iterator();
		// while(true) {
		// System.out.println(iterator.next());
		// }

		// Iterator<SimpleHashtable.TableEntry<String, Integer>> iter =
		// examMarks.iterator();
		// while (iter.hasNext()) {
		// SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
		// if (pair.getKey().equals("Ivana")) {
		// iter.remove(); // sam iterator kontrolirano uklanja trenutni
		// // element
		// }
		// }

		// Iterator<SimpleHashtable.TableEntry<String, Integer>> iter2 =
		// examMarks.iterator();
		// while (iter2.hasNext()) {
		// SimpleHashtable.TableEntry<String, Integer> pair = iter2.next();
		// if (pair.getKey().equals("Ivana")) {
		// iter2.remove();
		// iter2.remove();
		// }
		// }

		// Iterator<SimpleHashtable.TableEntry<String, Integer>> iter3 =
		// examMarks.iterator();
		// while (iter3.hasNext()) {
		// SimpleHashtable.TableEntry<String, Integer> pair = iter3.next();
		// if (pair.getKey().equals("Ivana")) {
		// examMarks.remove("Ivana");
		// }
		// }

		System.out.println();

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter4 = examMarks.iterator();
		while (iter4.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter4.next();
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter4.remove();
		}
		System.out.printf("Veličina: %d%n", examMarks.size());
	}

}
