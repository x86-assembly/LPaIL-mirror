package net.codemania.lexing.exceptions;

import net.codemania.FilePosition;

public class LexerUnknownKeywordException extends LexingException
{

public LexerUnknownKeywordException ( String kw, FilePosition pos )
{
	super( "Unknown keyword `%s` at %s".formatted( kw, pos ) );
}

}
