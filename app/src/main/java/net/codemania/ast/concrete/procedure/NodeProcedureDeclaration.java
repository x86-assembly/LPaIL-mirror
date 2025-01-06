package net.codemania.ast.concrete.procedure;

import net.codemania.ast.node_types.INodeProcedure;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

public record NodeProcedureDeclaration( String label ) implements INodeProcedure
{
@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
