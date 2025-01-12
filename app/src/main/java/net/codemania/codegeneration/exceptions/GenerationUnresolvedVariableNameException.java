package net.codemania.codegeneration.exceptions;

import net.codemania.FilePosition;

public class GenerationUnresolvedVariableNameException extends GenerationUnresolvedReferenceException
{
public GenerationUnresolvedVariableNameException ( String name, FilePosition pos )
{
	super( "Unknown variable \"%s\" at %s".formatted( name, pos ) );

}
}
