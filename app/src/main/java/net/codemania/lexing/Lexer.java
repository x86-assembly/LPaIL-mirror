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

private char readNext ()
{
	// what was read
	int read;
	try {
		read = reader.read();
	} catch ( IOException e ) {
		Logger.warn( "IOException while reading file! inserting EOF instead!" );
		read = -1;
	}
	this.current = (char) read;
	if ( read == -1 ) return (char) -1;
	if ( read == '\n' ) {
		line++;
		column = 1;
	} else {column++;}
	return this.current;

}

private void consume ( char c ) throws LexerUnexpectedCharacterException
{
	if ( current != c ) {
		throw new LexerUnexpectedCharacterException( current, c, pos() );
	}
	readNext();
}

private void consume ( String s ) throws LexerUnexpectedCharacterException
{
	for ( char c : s.toCharArray() ) {
		consume( c );
	}
}

private FilePosition pos ()
{
	return new FilePosition( line, column, name );
}

private int skipWhitespace ()
{
	int consumed = 0;
	while ( Character.isWhitespace( current ) ) {
		readNext();
		consumed++;
	}
	return consumed;
}


public List<Token> lexAll () throws LexerUnexpectedCharacterException
{
	readNext();

	List<Token> tokens = new LinkedList<>();

	while ( current != (char) -1 ) {
		skipWhitespace();
		Token t;
		tokens.add( t = switch ( current ) {
			case '~' -> lexSimpleToken( TokenType.Tilde );
			case '[' -> lexSimpleToken( TokenType.BracketSquOpen );
			case ']' -> lexSimpleToken( TokenType.BracketSquClose );
			case ';' -> lexSimpleToken( TokenType.Semicolon );
			// more complex tokens
			case '-' -> lexMinus();
			case 'i' -> lexIntegerLiteral();
			case '.' -> lexLabel();

			default -> null;
		} );
		Logger.info( "Parsed " + t );
	}
	return tokens;
}

// assumes `current` has already been validated
private Token lexSimpleToken ( TokenType type )
{
	Token t = new Token( type, pos() );
	readNext();
	return t;
}

private Token lexMinus () throws LexerUnexpectedCharacterException
{
	// save position so the tokens position will point to the start,
	// and not the end
	Token t = new Token( TokenType.ArrowThinLeft, pos() );
	consume( "->" );
	//Todo for now only '->' possible, but things like arithmetic binary minus
	// will change that
	return t;
}

private Token lexIntegerLiteral () throws LexerUnexpectedCharacterException
{
	//TODO for now only 'i15' is possible
	Token t = new Token( TokenType.IntegerLiteral, pos() );
	consume( "i15" );
	return t;
}

private Token lexLabel () throws LexerUnexpectedCharacterException
{
	FilePosition tokenStart = pos();
	consume( '.' );
	StringBuilder sb = new StringBuilder();
	while ( Character.isLowerCase( current ) ) {
		sb.append( current );
		readNext();
	}
	return new Token( TokenType.Label, sb.toString(), tokenStart );
}
}
