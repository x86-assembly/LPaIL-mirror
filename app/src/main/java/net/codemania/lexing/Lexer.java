package net.codemania.lexing;

import net.codemania.FilePosition;
import net.codemania.TokenStream;
import net.codemania.cli.Logger;
import net.codemania.lexing.exceptions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Lexer implements TokenStream
{

private final BufferedReader reader;
private final String name;

public Lexer ( String unbuffered )
{
	this( unbuffered, null );
}

public Lexer ( String unbuffered, String name )
{
	this.reader = new BufferedReader( new StringReader( unbuffered ) );
	if ( name == null ) {
		this.name = unbuffered.substring( 0, Math.min( unbuffered.length(), 50 ) );
	} else {this.name = name;}
	if ( unbuffered.length() > 2048 ) {
		Logger.warn( "Lexing large unbuffered file %s".formatted( this.name ) );
	}
	readNext(); // make sure that 'current' contains the first char

}

public Lexer ( BufferedReader reader, String name )
{
	this.reader = reader;
	this.name = name;
	readNext(); // same
}


public List<Token> lexAll () throws LexingException
{

	List<Token> tokens = new LinkedList<>();

	while ( current != (char) -1 ) {
		tokens.add( nextToken() );
	}
	return tokens;
}

@Override
public Token nextToken () throws LexingException
{
	skipWhitespace();
	if ( !hasMoreTokens() ) {
		return null;
	}
	Token token = switch ( current ) {
		case '~' -> lexSimpleToken( TokenType.Tilde );
		case '[' -> lexSimpleToken( TokenType.BracketSquOpen );
		case ']' -> lexSimpleToken( TokenType.BracketSquClose );
		case ';' -> lexSimpleToken( TokenType.Semicolon );
		case ',' -> lexSimpleToken( TokenType.Comma );
		case ':' -> lexSimpleToken( TokenType.Colon );
		// more complex tokens
		case '-' -> lexMinus();
		case 'i' -> lexIntegerLiteral();
		case '.' -> lexLabel();
		case '"' -> lexString();
		default -> null;

	};
	if ( token != null ) return token;

	if ( Character.isUpperCase( current ) ) return lexKeyword();


	throw new LexerUnexpectedCharacterException( current, "Invalid", pos() );
}


@Override
public boolean hasMoreTokens ()
{
	// if current is eof, no more Tokens are available
	return current != (char) -1;
}

@Override
public void reset () throws IOException
{
	this.reader.reset();
	// reset position
	this.line = 1;
	this.column = 0;
	readNext(); // set current
}

@Override
public void close () throws IOException
{
	this.reader.close();
}


private char current;
// column to zero, because first char has not been read yet
private int line = 1, column = 0;

private char readNext ()
{
	// what was read
	int read;
	try {
		read = reader.read();
	} catch ( IOException e ) {
		Logger.warn( "IOException while reading file! inserting EOF instead! (Tipp: try " + "using `hasMoreTokens()` )" );
		read = -1;
	}
	this.current = (char) read;
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
	if ( !hasMoreTokens() ) return 0;
	int consumed = 0;
	while ( Character.isWhitespace( current ) ) {
		readNext();
		consumed++;
	}
	return consumed;
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
	Token t = new Token( TokenType.ArrowThinRight, pos() );
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

private Token lexIntegerLiteral () throws LexingException
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
	if ( numerals.isEmpty() ) {
		throw new LexerMissingValueException( "Integer literal", "value", pos() );
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

private Token lexString () throws LexingException
{
	FilePosition tokenStart = pos();
	consume( '"' );
	StringBuilder string = new StringBuilder();
	while ( current != '\"' ) {
		if ( current != '\\' ) {
			string.append( current );
			readNext();
			continue;
		}

		readNext();
		string.append( switch ( current ) {
			case '\\' -> '\\';
			case 'n' -> '\n';
			case 'r' -> '\r';
			case 't' -> '\t';
			case 's' -> ' ';
			case 'e' -> (char) 0x1b;
			case '0' -> {
				FilePosition escapeStart = pos();
				int val;
				String escape = "" + readNext() + readNext();
				try {
					val = Integer.parseInt( escape, 8 );
					if ( val < 0 ) {throw new Exception();}
				} catch ( Exception e ) {
					throw new LexerStringMalformedEscapeSequenceException( escape, "Invalid octal character code", escapeStart );
				}
				yield (char) val;
			}
			case 'x' -> {
				FilePosition escapeStart = pos();
				int val;
				String escape = "" + readNext() + readNext();
				try {
					val = Integer.parseInt( escape, 16 );
					if ( val < 0 ) {throw new Exception();}
				} catch ( Exception e ) {
					throw new LexerStringMalformedEscapeSequenceException( escape, "Invalid hexadecimal character code", escapeStart );
				}
				yield (char) val;

			}
			default -> {
				Logger.warn( "Unknown escape '\\%c' at %s, ignoring!".formatted( current, pos() ) );
				yield current;
			}
		} );

		readNext();
	}
	consume( '"' );
	string.append( '\0' ); // null terminate strings for C

	return new Token( TokenType.String, string.toString(), tokenStart );
}

private Token lexKeyword () throws LexingException
{
	StringBuilder sb = new StringBuilder();
	FilePosition startPos = pos();
	// current char should already be uppercase, else this method would not
	// have been called
	do {
		sb.append( current );
		readNext();
	} while ( Character.isUpperCase( current ) );
	String keyword = sb.toString();
	TokenType type = switch ( keyword ) {
		case "PROC" -> TokenType.KwProcedure;
		default -> throw new LexerUnknownKeywordException( keyword, startPos );
	};
	return new Token( type, startPos );
}
}
