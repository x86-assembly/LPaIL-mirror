package net.codemania.parsing.exceptions;

import net.codemania.FilePosition;
import net.codemania.lexing.Token;
import net.codemania.lexing.TokenType;

public class ParserUnexpectedSymbolException extends ParsingException
{

public final TokenType found, expected;
public final FilePosition position;

public ParserUnexpectedSymbolException ( Token found, TokenType expected )
{
	this( found.getType(), expected, found.getPos() );
}

public ParserUnexpectedSymbolException ( TokenType found, TokenType expected, FilePosition pos )
{
	super( "Unexpected symbol \"%s\" while parsing %s, expected \"%s\"".formatted( found, pos, expected ) );
	this.found = found;
	this.expected = expected;
	this.position = pos;
}

public ParserUnexpectedSymbolException ( Token found )
{
	this( found.getType(), found.getPos() );
}

public ParserUnexpectedSymbolException ( TokenType found, FilePosition pos )
{
	super( "Unexpected symbol \"%s\" while parsing %s" );
	this.found = found;
	this.expected = null;
	this.position = pos;
}
}