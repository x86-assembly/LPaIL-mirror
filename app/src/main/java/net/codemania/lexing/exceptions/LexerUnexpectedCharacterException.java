package net.codemania.lexing.exceptions;

import net.codemania.FilePosition;

public class LexerUnexpectedCharacterException extends LexingException
{
public final char found, expected;
public final String reason;

public LexerUnexpectedCharacterException ( char found, String expected, FilePosition pos )
{
	super( String.format( "Unexpected character '%c' at %s, %s", found, pos, expected ) );
	this.found = found;
	this.expected = '\0';
	this.reason = expected;
}

public LexerUnexpectedCharacterException ( char found, char expected, FilePosition pos )
{
	super( String.format( "Unexpected character '%c' at %s, expected '%c'", found, pos, expected ) );
	this.found = found;
	this.expected = expected;
	this.reason = "Unexpected character";
}

}
