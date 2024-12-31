package net.codemania.lexing.exceptions;

import net.codemania.FilePosition;

public class LexerMissingValueException extends LexingException
{

public LexerMissingValueException ( String name, String missing, FilePosition pos )
{
	super( "%s at %s is missing %s".formatted( name, pos, missing ) );
}

}
