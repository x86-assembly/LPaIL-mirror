package net.codemania.codegeneration.exceptions;

import net.codemania.FilePosition;

public class GenerationUnresolvedProcedureNameException extends GenerationUnresolvedReferenceException
{
public GenerationUnresolvedProcedureNameException ( String procName, FilePosition pos )
{
	super( "Unknown procedure %s at %s".formatted( procName, pos ) );
}
}
