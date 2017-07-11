package hr.fer.zemris.java.hw04.db.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueryLexerTest {

	@Test(expected = QueryLexerException.class)
	public void testiranjePredanNull(){
		new QueryLexer(null);
	}
	
	@Test
	public void testiranjePredanPrazanString() throws Exception {
		QueryLexer lexer = new QueryLexer("");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}

	@Test(expected = QueryLexerException.class)
	public void testiranjeVisePozivaZaEOF() throws Exception {
		QueryLexer lexer = new QueryLexer("");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}


	@Test
	public void testiranjePredaneSamoPraznine() throws Exception {
		QueryLexer lexer = new QueryLexer(" \t\r ");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeLastName() throws Exception {
		QueryLexer lexer = new QueryLexer("lastName");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test(expected = QueryLexerException.class)
	public void testiranjeLastNamePogresno() throws Exception {
		QueryLexer lexer = new QueryLexer("last\tName");
		lexer.nextToken();
	}
	
	@Test
	public void testiranjeFirstName() throws Exception {
		QueryLexer lexer = new QueryLexer("firstName");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test(expected = QueryLexerException.class)
	public void testiranjefirstNamePogresno() throws Exception {
		QueryLexer lexer = new QueryLexer("firstnam e");
		lexer.nextToken();
	}
	
	
	@Test
	public void testiranjeJmbag() throws Exception {
		QueryLexer lexer = new QueryLexer("jmbag");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test(expected = QueryLexerException.class)
	public void testiranjeJmbagPogresno() throws Exception {
		QueryLexer lexer = new QueryLexer("JMBag");
		lexer.nextToken();
	}
	
	@Test
	public void testiranjeVece() throws Exception {
		QueryLexer lexer = new QueryLexer(">");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeVeceJednako() throws Exception {
		QueryLexer lexer = new QueryLexer(">=");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeManje() throws Exception {
		QueryLexer lexer = new QueryLexer("<");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}

	@Test
	public void testiranjeManjeJednako() throws Exception {
		QueryLexer lexer = new QueryLexer(">=");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeJednako() throws Exception {
		QueryLexer lexer = new QueryLexer("=");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeNijeJednako() throws Exception {
		QueryLexer lexer = new QueryLexer("!=");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test(expected = QueryLexerException.class)
	public void testiranjeNijeJednakoPogresno() throws Exception {
		QueryLexer lexer = new QueryLexer("!\t=");
		lexer.nextToken();
	}
	
	@Test
	public void testiranjeLike() throws Exception {
		QueryLexer lexer = new QueryLexer("LIKE");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test(expected = QueryLexerException.class)
	public void testiranjeLikePogresno() throws Exception {
		QueryLexer lexer = new QueryLexer("lIkE");
		lexer.nextToken();
	}
	
	@Test
	public void testiranjeStringa() throws Exception {
		QueryLexer lexer = new QueryLexer("\"0036486648\"");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeSamoJednaUsporedba() throws Exception {
		QueryLexer lexer = new QueryLexer("lastName=\"Češljaš\"");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeSamoJednaUsporedbaIBjeline() throws Exception {
		QueryLexer lexer = new QueryLexer(" \t\r  lastName     \t<= \"Češljaš\"\t");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeViseUsporedba() throws Exception {
		QueryLexer lexer = new QueryLexer("lastName<=\"Češljaš\" AND jmbag=\"0036486648\" and firstName LIKE \"D*\"");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.AND);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.AND);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
	
	@Test
	public void testiranjeViseUsporedbaIBjeline() throws Exception {
		QueryLexer lexer = new QueryLexer("\r lastName <=\t\"Češljaš\"\t AnD\t jmbag\t= \"0036486648\"      aNd     firstName \tLIKE \t  \"D*\"");
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.AND);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.AND);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.FIELD_NAME);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.OPERATOR);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.STRING);
		assertEquals(lexer.nextToken().getType(), QueryTokenType.EOF);
	}
}
