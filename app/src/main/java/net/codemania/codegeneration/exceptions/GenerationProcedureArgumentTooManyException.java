package net.codemania.codegeneration.exceptions;

import net.codemania.FilePosition;

public class GenerationProcedureArgumentTooManyException extends GenerationProcedureArgumentException
{
public GenerationProcedureArgumentTooManyException ( String name, int found, int wanted, FilePosition pos )
{
	super( "Too many arguments passed into procedure \"%s\" at %s. Expected %d but found %d".formatted( name, pos, wanted, found ) );
}
}
