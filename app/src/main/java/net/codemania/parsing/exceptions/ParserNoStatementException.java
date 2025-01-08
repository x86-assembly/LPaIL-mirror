package net.codemania.parsing.exceptions;

import net.codemania.FilePosition;

public class ParserNoStatementException extends ParsingException
{
public ParserNoStatementException ( FilePosition pos )
{
	super( "No statement at " + pos );
}
}
