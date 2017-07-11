package hr.fer.zemris.java.hw04.collections;


/**
 * Razred koji predstavlja program koji demonstrira rad sa kolekcijom koja je
 * primjerak razreda {@link SimpleHashtable}.
 *
 * @author Davor Češljaš
 */
public class SimpleHashtableDemo {

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
		examMarks.put("Jasna", 5);
		examMarks.put("Kristina", null);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		examMarks.remove(3);
		//examMarks.remove("Kristina");
		//System.out.println(examMarks.get(null));
		System.out.println(examMarks.containsValue(null));
		// query collection:
		Integer kristinaGrade = examMarks.get("Kristina");
		System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
		// What is collection's size? Must be four!
		System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4
		System.out.println(examMarks);
	}

}
