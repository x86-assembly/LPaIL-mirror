package net.codemania;

import net.codemania.lexing.Lexer;
import net.codemania.lexing.RomanNumeralParser;
import net.codemania.lexing.Token;
import net.codemania.lexing.TokenType;
import net.codemania.lexing.exceptions.LexerUnexpectedCharacterException;
import net.codemania.lexing.exceptions.LexingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LexerTest
{

@Test
public void testBinaryNoSign ()
{
	checkNumeric( 5, "ib101" );
}

@Test
public void testBinaryPlus ()
{
	checkNumeric( 5, "ib+101" );
}

@Test
public void testBinaryMinus ()
{
	checkNumeric( -5, "ib-101" );
}

@Test
public void testSenaryNoSign ()
{
	checkNumeric( 161, "is425" );
}

@Test
public void testSenaryPlus ()
{
	checkNumeric( 161, "is+425" );
}

@Test
public void testSenaryMinus ()
{
	checkNumeric( -161, "is-425" );
}

@Test
public void testOctalNoSign ()
{
	checkNumeric( 10, "io12" );
}

@Test
public void testOctalPlus ()
{
	checkNumeric( 10, "io+12" );
}

@Test
public void testOctalMinus ()
{
	checkNumeric( -10, "io-12" );
}

@Test
public void testDecimalNoSign ()
{
	checkNumeric( 123, "i123" );
}

@Test
public void testDecimalPlus ()
{
	checkNumeric( 123, "i+123" );
}

@Test
public void testDecimalMinus ()
{
	checkNumeric( -123, "i-123" );
}

@Test
public void testHexadecimalNoSign ()
{
	checkNumeric( 255, "ixff" );
}

@Test
public void testHexadecimalPlus ()
{
	checkNumeric( 255, "ix+ff" );
}

@Test
public void testHexadecimalMinus ()
{
	checkNumeric( -255, "ix-ff" );
}

@Test
public void testRomanNoSign ()
{
	checkNumeric( 4, "irIV" );
}

@Test
public void testRomanPlus ()
{
	checkNumeric( 4, "ir+IV" );
}

@Test
public void testRomanMinus ()
{
	checkNumeric( -4, "ir-IV" );
}

private void checkNumeric ( int expected, String s )
{

	Token t;
	try {
		t = new Lexer( s ).nextToken();
	} catch ( LexingException e ) {
		throw new RuntimeException( "Unable to parse \"%s\", %s".formatted( s, e ) );
	}
	assertEquals( TokenType.IntegerLiteral, t.getType() );
	assertEquals( expected, t.getVal() );

}

@Test
public void testTilde ()
{
	assertType( TokenType.Tilde, "~" );
}

@Test
public void testBrSquOpen ()
{
	assertType( TokenType.BracketSquOpen, "[" );
}

@Test
public void testBrSquClose ()
{
	assertType( TokenType.BracketSquClose, "]" );
}

@Test
public void testIntegerLiteral ()
{
	assertType( TokenType.IntegerLiteral, "i123" );
}

@Test
public void testArrowThinLeft ()
{
	assertType( TokenType.ArrowThinRight, "->" );
}

@Test
public void testLabel ()
{
	assertType( TokenType.Label, ".print" );
	assertType( TokenType.Label, ".exit" );
	assertType( TokenType.Label, ".somethingreallyreallyinsanelylong" );
	assertHasValue( "print", ".print" );
}

@Test
public void testSemicolon ()
{
	assertType( TokenType.Semicolon, ";" );
}

private void assertType ( TokenType expected, String s )
{
	Token t;
	try {
		t = new Lexer( s ).nextToken();
	} catch ( LexingException e ) {
		throw new RuntimeException( "Unable to parse \"%s\", %s".formatted( s, e ) );
	}
	assertEquals( expected, t.getType() );
}

private void assertHasValue ( Object expected, String s )
{
	Token t;
	try {
		t = new Lexer( s ).nextToken();
	} catch ( LexingException e ) {
		throw new RuntimeException( "Unable to parse \"%s\", %s".formatted( s, e ) );
	}
	assertEquals( expected, t.getVal() );
}

@Test
public void cantLexEmptyIntegerLiterals ()
{
	assertCantLex( "i" );
	assertCantLex( "ir+" );
	assertCantLex( "ib-" );
	assertCantLex( "i-" );
}

private void assertCantLex ( String invalid )
{
	assertThrows( LexingException.class, new Lexer( invalid )::nextToken );
}


@Test
void romanNumeralTest () throws LexerUnexpectedCharacterException
{

	assertEquals( 1, RomanNumeralParser.parseNumeral( "I" ) );
	assertEquals( 2, RomanNumeralParser.parseNumeral( "II" ) );
	assertEquals( 3, RomanNumeralParser.parseNumeral( "III" ) );
	assertEquals( 4, RomanNumeralParser.parseNumeral( "IV" ) );
	assertEquals( 5, RomanNumeralParser.parseNumeral( "V" ) );
	assertEquals( 6, RomanNumeralParser.parseNumeral( "VI" ) );
	assertEquals( 7, RomanNumeralParser.parseNumeral( "VII" ) );
	assertEquals( 8, RomanNumeralParser.parseNumeral( "VIII" ) );
	assertEquals( 9, RomanNumeralParser.parseNumeral( "IX" ) );
	assertEquals( 10, RomanNumeralParser.parseNumeral( "X" ) );
	assertEquals( 11, RomanNumeralParser.parseNumeral( "XI" ) );
	assertEquals( 12, RomanNumeralParser.parseNumeral( "XII" ) );
	assertEquals( 13, RomanNumeralParser.parseNumeral( "XIII" ) );
	assertEquals( 14, RomanNumeralParser.parseNumeral( "XIV" ) );
	assertEquals( 15, RomanNumeralParser.parseNumeral( "XV" ) );
	assertEquals( 16, RomanNumeralParser.parseNumeral( "XVI" ) );
	assertEquals( 17, RomanNumeralParser.parseNumeral( "XVII" ) );
	assertEquals( 18, RomanNumeralParser.parseNumeral( "XVIII" ) );
	assertEquals( 19, RomanNumeralParser.parseNumeral( "XIX" ) );
	assertEquals( 20, RomanNumeralParser.parseNumeral( "XX" ) );
	assertEquals( 21, RomanNumeralParser.parseNumeral( "XXI" ) );
	assertEquals( 22, RomanNumeralParser.parseNumeral( "XXII" ) );
	assertEquals( 23, RomanNumeralParser.parseNumeral( "XXIII" ) );
	assertEquals( 24, RomanNumeralParser.parseNumeral( "XXIV" ) );
	assertEquals( 25, RomanNumeralParser.parseNumeral( "XXV" ) );
	assertEquals( 30, RomanNumeralParser.parseNumeral( "XXX" ) );
	assertEquals( 35, RomanNumeralParser.parseNumeral( "XXXV" ) );
	assertEquals( 40, RomanNumeralParser.parseNumeral( "XL" ) );
	assertEquals( 45, RomanNumeralParser.parseNumeral( "XLV" ) );
	assertEquals( 50, RomanNumeralParser.parseNumeral( "L" ) );
	assertEquals( 55, RomanNumeralParser.parseNumeral( "LV" ) );
	assertEquals( 60, RomanNumeralParser.parseNumeral( "LX" ) );
	assertEquals( 65, RomanNumeralParser.parseNumeral( "LXV" ) );
	assertEquals( 70, RomanNumeralParser.parseNumeral( "LXX" ) );
	assertEquals( 75, RomanNumeralParser.parseNumeral( "LXXV" ) );
	assertEquals( 80, RomanNumeralParser.parseNumeral( "LXXX" ) );
	assertEquals( 85, RomanNumeralParser.parseNumeral( "LXXXV" ) );
	assertEquals( 90, RomanNumeralParser.parseNumeral( "XC" ) );
	assertEquals( 95, RomanNumeralParser.parseNumeral( "XCV" ) );
	assertEquals( 100, RomanNumeralParser.parseNumeral( "C" ) );
	assertEquals( 105, RomanNumeralParser.parseNumeral( "CV" ) );
	assertEquals( 110, RomanNumeralParser.parseNumeral( "CX" ) );
	assertEquals( 115, RomanNumeralParser.parseNumeral( "CXV" ) );
	assertEquals( 120, RomanNumeralParser.parseNumeral( "CXX" ) );
	assertEquals( 125, RomanNumeralParser.parseNumeral( "CXXV" ) );
	assertEquals( 130, RomanNumeralParser.parseNumeral( "CXXX" ) );
	assertEquals( 135, RomanNumeralParser.parseNumeral( "CXXXV" ) );
	assertEquals( 140, RomanNumeralParser.parseNumeral( "CXL" ) );
	assertEquals( 150, RomanNumeralParser.parseNumeral( "CL" ) );
	assertEquals( 175, RomanNumeralParser.parseNumeral( "CLXXV" ) );
	assertEquals( 200, RomanNumeralParser.parseNumeral( "CC" ) );
	assertEquals( 225, RomanNumeralParser.parseNumeral( "CCXXV" ) );
	assertEquals( 250, RomanNumeralParser.parseNumeral( "CCL" ) );
	assertEquals( 275, RomanNumeralParser.parseNumeral( "CCLXXV" ) );
	assertEquals( 300, RomanNumeralParser.parseNumeral( "CCC" ) );
	assertEquals( 325, RomanNumeralParser.parseNumeral( "CCCXXV" ) );
	assertEquals( 350, RomanNumeralParser.parseNumeral( "CCCL" ) );


}
}
