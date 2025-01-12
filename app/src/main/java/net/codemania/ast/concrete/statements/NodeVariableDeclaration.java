package net.codemania.ast.concrete.statements;

import net.codemania.ast.node_types.INodeVariableDeclaration;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

public record NodeVariableDeclaration( String name ) implements INodeVariableDeclaration
{
@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
