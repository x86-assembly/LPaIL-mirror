package net.codemania.ast.concrete.expression;

import net.codemania.FilePosition;
import net.codemania.ast.node_types.INodeExpression;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

public record NodeExpressionVariable( String name, FilePosition pos ) implements INodeExpression
{

@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
