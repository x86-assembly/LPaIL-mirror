package net.codemania;

import net.codemania.cli.Logger;
import net.codemania.lexing.Lexer;
import net.codemania.lexing.RomanNumeralParser;
import net.codemania.lexing.Token;
import net.codemania.lexing.exceptions.LexerUnexpectedCharacterException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTest
{
@Test
void canLexBasicFile ()
{
	File file = new File( "./src/test/resources/test.lpl" );
	Lexer lx;
	try {
		lx = new Lexer( new BufferedReader( new FileReader( file ) ), "canLexBasicFileTest" );
	} catch ( FileNotFoundException e ) {
		throw new RuntimeException( e );
	}

	List<Token> tokens;
	try {
		tokens = lx.lexAll();
	} catch ( LexerUnexpectedCharacterException e ) {
		System.out.println( e.getMessage() );
		throw new AssertionError( String.format( "Failed to lex file %s: %s", file.getName(), e.getMessage() ) );
	}
	Logger.info( "Successfully lexed a basic file. Tokens:" );
	StringJoiner j = new StringJoiner( ", " );
	for ( Token t : tokens ) {
		j.add( t.toString() );
	}
	Logger.info( j.toString() );

}

@Test
void romanNumeralTest () throws LexerUnexpectedCharacterException
{

	assertEquals( RomanNumeralParser.parseNumeral( "I" ), 1 );
	assertEquals( RomanNumeralParser.parseNumeral( "II" ), 2 );
	assertEquals( RomanNumeralParser.parseNumeral( "III" ), 3 );
	assertEquals( RomanNumeralParser.parseNumeral( "IV" ), 4 );
	assertEquals( RomanNumeralParser.parseNumeral( "V" ), 5 );
	assertEquals( RomanNumeralParser.parseNumeral( "VI" ), 6 );
	assertEquals( RomanNumeralParser.parseNumeral( "VII" ), 7 );
	assertEquals( RomanNumeralParser.parseNumeral( "VIII" ), 8 );
	assertEquals( RomanNumeralParser.parseNumeral( "IX" ), 9 );
	assertEquals( RomanNumeralParser.parseNumeral( "X" ), 10 );
	assertEquals( RomanNumeralParser.parseNumeral( "XI" ), 11 );
	assertEquals( RomanNumeralParser.parseNumeral( "XII" ), 12 );
	assertEquals( RomanNumeralParser.parseNumeral( "XIII" ), 13 );
	assertEquals( RomanNumeralParser.parseNumeral( "XIV" ), 14 );
	assertEquals( RomanNumeralParser.parseNumeral( "XV" ), 15 );
	assertEquals( RomanNumeralParser.parseNumeral( "XVI" ), 16 );
	assertEquals( RomanNumeralParser.parseNumeral( "XVII" ), 17 );
	assertEquals( RomanNumeralParser.parseNumeral( "XVIII" ), 18 );
	assertEquals( RomanNumeralParser.parseNumeral( "XIX" ), 19 );
	assertEquals( RomanNumeralParser.parseNumeral( "XX" ), 20 );
	assertEquals( RomanNumeralParser.parseNumeral( "XXI" ), 21 );
	assertEquals( RomanNumeralParser.parseNumeral( "XXII" ), 22 );
	assertEquals( RomanNumeralParser.parseNumeral( "XXIII" ), 23 );
	assertEquals( RomanNumeralParser.parseNumeral( "XXIV" ), 24 );
	assertEquals( RomanNumeralParser.parseNumeral( "XXV" ), 25 );
	assertEquals( RomanNumeralParser.parseNumeral( "XXX" ), 30 );
	assertEquals( RomanNumeralParser.parseNumeral( "XXXV" ), 35 );
	assertEquals( RomanNumeralParser.parseNumeral( "XL" ), 40 );
	assertEquals( RomanNumeralParser.parseNumeral( "XLV" ), 45 );
	assertEquals( RomanNumeralParser.parseNumeral( "L" ), 50 );
	assertEquals( RomanNumeralParser.parseNumeral( "LV" ), 55 );
	assertEquals( RomanNumeralParser.parseNumeral( "LX" ), 60 );
	assertEquals( RomanNumeralParser.parseNumeral( "LXV" ), 65 );
	assertEquals( RomanNumeralParser.parseNumeral( "LXX" ), 70 );
	assertEquals( RomanNumeralParser.parseNumeral( "LXXV" ), 75 );
	assertEquals( RomanNumeralParser.parseNumeral( "LXXX" ), 80 );
	assertEquals( RomanNumeralParser.parseNumeral( "LXXXV" ), 85 );
	assertEquals( RomanNumeralParser.parseNumeral( "XC" ), 90 );
	assertEquals( RomanNumeralParser.parseNumeral( "XCV" ), 95 );
	assertEquals( RomanNumeralParser.parseNumeral( "C" ), 100 );
	assertEquals( RomanNumeralParser.parseNumeral( "CV" ), 105 );
	assertEquals( RomanNumeralParser.parseNumeral( "CX" ), 110 );
	assertEquals( RomanNumeralParser.parseNumeral( "CXV" ), 115 );
	assertEquals( RomanNumeralParser.parseNumeral( "CXX" ), 120 );
	assertEquals( RomanNumeralParser.parseNumeral( "CXXV" ), 125 );
	assertEquals( RomanNumeralParser.parseNumeral( "CXXX" ), 130 );
	assertEquals( RomanNumeralParser.parseNumeral( "CXXXV" ), 135 );
	assertEquals( RomanNumeralParser.parseNumeral( "CXL" ), 140 );
	assertEquals( RomanNumeralParser.parseNumeral( "CL" ), 150 );
	assertEquals( RomanNumeralParser.parseNumeral( "CLXXV" ), 175 );
	assertEquals( RomanNumeralParser.parseNumeral( "CC" ), 200 );
	assertEquals( RomanNumeralParser.parseNumeral( "CCXXV" ), 225 );
	assertEquals( RomanNumeralParser.parseNumeral( "CCL" ), 250 );
	assertEquals( RomanNumeralParser.parseNumeral( "CCLXXV" ), 275 );
	assertEquals( RomanNumeralParser.parseNumeral( "CCC" ), 300 );
	assertEquals( RomanNumeralParser.parseNumeral( "CCCXXV" ), 325 );
	assertEquals( RomanNumeralParser.parseNumeral( "CCCL" ), 350 );


}
}
