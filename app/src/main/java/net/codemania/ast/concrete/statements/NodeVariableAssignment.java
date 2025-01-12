package net.codemania.ast.concrete.statements;

import net.codemania.ast.node_types.INodeExpression;
import net.codemania.ast.node_types.INodeVariableDeclaration;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

public record NodeVariableAssignment( String name,
				      INodeExpression value ) implements INodeVariableDeclaration
{
@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
