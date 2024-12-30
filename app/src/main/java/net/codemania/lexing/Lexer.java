package net.codemania.lexing;

import net.codemania.FilePosition;
import net.codemania.cli.Logger;
import net.codemania.lexing.exceptions.LexerUnexpectedCharacterException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Lexer
{

private BufferedReader reader;
private String name;

public Lexer ( BufferedReader reader, String name )
{
	this.reader = reader;
	this.name = name;
}

private char current;
private int line = 1, column = 1;

private Character read ()
{
	int red;
	try {
		red = reader.read();
	} catch ( IOException e ) {
		Logger.warn( "IOException while lexing file" );
		red = -1;
	}
	if ( red == -1 ) return null; // found EOF, no point in anything else
	// '\r\n' on windows don't matter because they contain a newline
	if ( red == '\n' ) {
		line++;
		column = 1;
	} else {column++;}

	return ( this.current = (char) red );
}

private void consume ( char c ) throws LexerUnexpectedCharacterException
{
	if ( current != c ) {
		throw new LexerUnexpectedCharacterException( current, c, pos() );
	}
	read();
}

private void consume ( String s ) throws LexerUnexpectedCharacterException
{
	for ( int i = 0; i < s.length(); i++ ) {
		consume( s.charAt( i ) );
	}
}

private void probe ( char c ) throws LexerUnexpectedCharacterException
{
	if ( current != c ) throw new LexerUnexpectedCharacterException( current, c, pos() );
}

private FilePosition pos ()
{
	return new FilePosition( line, column, name );
}


public List<Token> lex () throws LexerUnexpectedCharacterException
{
	List<Token> tokens = new LinkedList<>();
	read();

	while ( current != (char) -1 ) {
		skipWhitespace();
		// switch for simple tokens,
		Token t;
		tokens.add( t = switch ( current ) {
			case '~' -> new Token( TokenType.Tilde, pos() );
			case '[' -> new Token( TokenType.BracketSquOpen, pos() );
			case ']' -> new Token( TokenType.BracketSquClose, pos() );
			case ';' -> new Token( TokenType.Semicolon, pos() );
			// more complex tokens
			case '-' -> lexMinus();
			case 'i' -> lexIntegerLiteral();
			case '.' -> lexLabel();
			default -> throw new LexerUnexpectedCharacterException( current, pos() );
		} );
		Logger.info( "Lexed " + t.getType() );
	}

	return tokens;

}

private Token lexLabel () throws LexerUnexpectedCharacterException
{
	FilePosition spos = pos();
	consume( '.' );
	StringBuilder sb = new StringBuilder();
	while ( Character.isLowerCase( current ) ) {
		sb.append( current );
		read();
	}
	return new Token( TokenType.Label, sb.toString(), spos );

}

// for now only '->' possible, but WILL change in the future with things like
// arithmetic
private Token lexMinus () throws LexerUnexpectedCharacterException
{
	// save the position so it won't point to the end of the token
	FilePosition spos = pos();
	consume( "-" );
	probe( '>' );
	return new Token( TokenType.ArrowThinLeft, spos );

}

private Token lexIntegerLiteral () throws LexerUnexpectedCharacterException
{
	FilePosition spos = pos();
	consume( "i1" );
	probe( '5' ); //TODO actually parse integer literals and not just
	// TODO force it to be 'i15'

	return new Token( TokenType.IntegerLiteral, 15, pos() );
}

private int skipWhitespace ()
{
	int skipped = 0;
	if ( !Character.isWhitespace( current ) ) return skipped;
	while ( Character.isWhitespace( read() ) ) skipped++;
	return skipped;
}


}
