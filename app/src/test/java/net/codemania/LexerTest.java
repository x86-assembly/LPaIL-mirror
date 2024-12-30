package net.codemania;

import net.codemania.lexing.Lexer;
import net.codemania.lexing.Token;
import net.codemania.lexing.exceptions.LexerUnexpectedCharacterException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

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
	System.out.println( "Successfully lexed a basic file. Tokens:" );
	for ( Token t : tokens ) {
		System.out.print( t.toString() + ", " );
	}

}
}
