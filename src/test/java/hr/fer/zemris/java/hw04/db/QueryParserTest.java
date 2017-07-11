package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.lexer.QueryLexerException;

public class QueryParserTest {

	@Test(expected = QueryLexerException.class)
	public void testiranjePredanNull(){
		new QueryParser(null);
	}
	
	@Test(expected = QueryParserException.class)
	public void testiranjePredanPrazanString(){
		new QueryParser("");
	}
	
	@Test(expected = QueryParserException.class)
	public void testiranjePredanStringSBjelinama(){
		new QueryParser("  \t \r    \t");
	}
	
	@Test
	public void predanDirektanUpit() {
		QueryParser parser = new QueryParser("jmbag = \"0036486648\"");
		assertEquals(parser.isDirectQuery(), true);
	}
	
	@Test
	public void predanNormalanUpitJmbag() {
		QueryParser parser = new QueryParser("jmbag LIKE \"0036486648\"");
		assertEquals(parser.isDirectQuery(), false);
		assertEquals(parser.getQuery().size(), 1);
	}
	
	@Test
	public void predanNormalanUpitPrezime() {
		QueryParser parser = new QueryParser("lastName >= \"A\"");
		assertEquals(parser.isDirectQuery(), false);
		assertEquals(parser.getQuery().size(), 1);
	}
	
	@Test
	public void predanNormalanUpitIme() {
		QueryParser parser = new QueryParser("lastName != \"C\"");
		assertEquals(parser.isDirectQuery(), false);
		assertEquals(parser.getQuery().size(), 1);
	}
	
	@Test(expected = QueryParserException.class)
	public void predanKriviUpitSamoAnd(){
		new QueryParser("AND ");
	}
	
	@Test(expected = QueryParserException.class)
	public void predanAndNaKrivomMjestu(){
		new QueryParser("jmbag LIKE =\"0000000001\" AND ");
	}

	@Test
	public void predanSlozenUpit() {
		QueryParser parser = new QueryParser("jmbag=\"0036486648\"  and lastName < \"Davor\"");
		assertEquals(parser.isDirectQuery(), false);
		assertEquals(parser.getQuery().size(), 2);
	}
	
	@Test(expected = QueryParserException.class)
	public void predanSlozenUpitBezAnd() {
		new QueryParser("jmbag=\"0036486648\"  lastName < \"Davor\"");
	}
	
	@Test
	public void predanSlozenUpitSBjelinama() {
		QueryParser parser = new QueryParser("jmbag =   \"0036486648\" \t and \t\r lastName < \"Davor\"");
		assertEquals(parser.isDirectQuery(), false);
		assertEquals(parser.getQuery().size(), 2);
	}
	
	@Test
	public void predanSlozenUpitSaSvimPoljima() {
		QueryParser parser = new QueryParser("\r lastName <=\t\"Češljaš\"\t AnD\t jmbag\t= \"0036486648\"      aNd     firstName \tLIKE \t  \"D*\"");
		assertEquals(parser.isDirectQuery(), false);
		assertEquals(parser.getQuery().size(), 3);
	}
	
	@Test(expected = QueryParserException.class)
	public void predanSlozenUpitSaSvimPoljimaPogresan() {
		new QueryParser("\r lastName <=\t\"Češljaš\"\t AnD\t jmbag\t= \"0036486648\"      aNd     firstName \tLIKE \t  \"D*\" AND");
	}
}
