package net.codemania.lexing;

import net.codemania.FilePosition;
import net.codemania.cli.Logger;
import net.codemania.lexing.exceptions.LexerUnexpectedCharacterException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

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

private void readNext ()
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
	if ( read == '\n' ) {
		line++;
		column = 1;
	} else {column++;}

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
		tokens.add( switch ( current ) {
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


private enum NumberType
{
	Binary,
	Senary,
	Octal,
	Decimal,
	Hexadecimal,
	Roman
}

private Token lexIntegerLiteral () throws LexerUnexpectedCharacterException
{
	// ib0101	binary
	// is425	senary
	// io12 	octal
	// i123		decimal
	// id123	decimal
	// i-123
	// io72		octal
	// ixfe		hex
	// irIVV	roman
	//TODO for now only 'i15' is possible
	Token token = new Token( TokenType.IntegerLiteral, pos() );
	consume( 'i' );
	// mode: binary, senary, octal, decimal, hexadecimal, roman
	NumberType mode = NumberType.Decimal;
	if ( Character.isAlphabetic( current ) ) {
		mode = switch ( current ) {
			case 'b' -> NumberType.Binary;
			case 's' -> NumberType.Senary;
			case 'o' -> NumberType.Octal;
			case 'd' -> NumberType.Decimal;
			case 'x' -> NumberType.Hexadecimal;
			case 'r' -> NumberType.Roman;
			default ->
				throw new LexerUnexpectedCharacterException( current, "invalid integer " + "type", pos() );
		};
		readNext();
	}
	// sign: + -
	boolean isnegative = false;
	if ( current == '+' || current == '-' ) {
		if ( current == '-' ) {
			isnegative = true;
		}
		readNext();
	}

	// read numerals
	Pattern allowedNumerals = Pattern.compile( switch ( mode ) {
		case Binary -> "[01]";
		case Senary -> "[0-5]";
		case Octal -> "[0-7]";
		case Decimal -> "[0-9]";
		case Hexadecimal -> "[0-9a-fA-F]";
		case Roman -> "[IVXLCDM]";
	} );

	StringBuilder numerals = new StringBuilder();
	while ( allowedNumerals.matcher( String.valueOf( current ) ).matches() ) {
		numerals.append( current );
		readNext();
	}
	if ( mode == NumberType.Roman ) {
		token.setVal( ( isnegative ? -1: 1 ) * RomanNumeralParser.parseNumeral( numerals.toString() ) );
	} else {
		token.setVal( ( isnegative ? -1: 1 ) * Integer.parseInt(
			numerals.toString(), switch ( mode ) {
				case Binary -> 2;
				case Senary -> 6;
				case Octal -> 8;
				case Decimal -> 10;
				case Hexadecimal -> 16;
				case Roman -> throw new RuntimeException( "Impossible!" );
			} ) );
	}

	return token;

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
