package net.codemania.lexing.exceptions;

import net.codemania.FilePosition;

public class LexerStringMalformedEscapeSequenceException extends LexingException
{

public LexerStringMalformedEscapeSequenceException ( String found, String desc, FilePosition pos )
{


	super( "Malformed string escape sequence \"%s\" at %s: %s".formatted( found, pos, desc ) );
}

}
