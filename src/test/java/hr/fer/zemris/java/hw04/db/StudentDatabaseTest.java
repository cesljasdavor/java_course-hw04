package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class StudentDatabaseTest {

	@Test(expected = IllegalArgumentException.class)
	public void testiranjeKonstruktoraPredaNull() throws Exception {
		new StudentDatabase(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjePraznaDatoteka() throws Exception {
		new StudentDatabase(ucitajDatoteku("./src/test/resources/prazanfile.txt"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjeNisuTaboviUDatoteci() throws Exception {
		new StudentDatabase(ucitajDatoteku("./src/test/resources/nisuTabovi.txt"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testiranjePonovljeniJmbag() throws Exception {
		new StudentDatabase(ucitajDatoteku("./src/test/resources/istiJmbag.txt"));
	}

	@Test
	public void standardnaDatoteka() throws Exception {
		StudentDatabase db = new StudentDatabase(ucitajDatoteku("./src/main/resources/database.txt"));
		assertEquals(db.filter(r -> true).size(), 63);
	}

	@Test
	public void testiranjeDirektnogUpit() throws Exception {
		StudentDatabase db = new StudentDatabase(ucitajDatoteku("./src/main/resources/database.txt"));
		QueryParser parser = new QueryParser("jmbag = \"0000000006\"");
		StudentRecord record1 = new StudentRecord("0000000006", "Cvrlje", "Ivan", 3);
		StudentRecord record2 = db.forJMBAG(parser.getQueriedJMBAG());
		assertEquals(record1, record2);
	}

	@Test
	public void testiranjeJmbagIPrezime() throws Exception {
		StudentDatabase db = new StudentDatabase(ucitajDatoteku("./src/main/resources/database.txt"));
		QueryParser parser = new QueryParser("jmbag = \"0000000006\" AND lastName != \"Cvrlje\"");
		for (StudentRecord record : db.filter(r -> true)) {
			assertEquals(new QueryFilter(parser.getQuery()).accepts(record), false);
		}
	}

	@Test
	public void testiranjeImeIPrezime() throws Exception {
		StudentDatabase db = new StudentDatabase(ucitajDatoteku("./src/main/resources/database.txt"));
		QueryParser parser = new QueryParser("firstName >= \"Brez\" AND lastName < \"C\"");
		StudentRecord record = db.forJMBAG("0000000005");
		assertEquals(new QueryFilter(parser.getQuery()).accepts(record), true);
	}
	
	@Test
	public void testiranjePrezimeLike() throws Exception {
		StudentDatabase db = new StudentDatabase(ucitajDatoteku("./src/main/resources/database.txt"));
		QueryParser parser = new QueryParser("lastName LIKE \"Ž*ć\"");
		StudentRecord record = db.forJMBAG("0000000063");
		assertEquals(new QueryFilter(parser.getQuery()).accepts(record), true);
	}
	
	@Test
	public void testiranjeJmbagLike() throws Exception {
		StudentDatabase db = new StudentDatabase(ucitajDatoteku("./src/main/resources/database.txt"));
		QueryParser parser = new QueryParser("jmbag LIKE \"*01\"");
		StudentRecord record = db.forJMBAG("0000000001");
		assertEquals(new QueryFilter(parser.getQuery()).accepts(record), true);
	}
	
	private List<String> ucitajDatoteku(String naziv) throws IOException {
		return Files.readAllLines(Paths.get(naziv));
	}

}
