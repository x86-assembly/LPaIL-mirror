package net.codemania.lexing.exceptions;

import net.codemania.FilePosition;

public class LexerUnexpectedCharacterException extends LexingException
{
public final char found, expected;

public LexerUnexpectedCharacterException ( char found, FilePosition pos )
{
	super( String.format( "Unexpected character '%c' at %s", found, pos ) );
	this.found = found;
	this.expected = '\0';
}

public LexerUnexpectedCharacterException ( char found, char expected, FilePosition pos )
{
	super( String.format( "Unexpected character '%c' at %s, expected '%c'", found, pos, expected ) );
	this.found = found;
	this.expected = expected;
}

}
