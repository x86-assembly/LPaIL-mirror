package net.codemania.ast.concrete.procedure;

import net.codemania.ast.node_types.INodeProcedure;
import net.codemania.ast.node_types.INodeStatement;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

import java.util.List;

public record NodeProcedureDeclarationDefinition( String label,
						  List<INodeStatement> statements ) implements INodeProcedure
{

@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
