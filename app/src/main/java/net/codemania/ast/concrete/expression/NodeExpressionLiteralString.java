package net.codemania.ast.concrete.expression;

import net.codemania.FilePosition;
import net.codemania.ast.node_types.INodeExpression;
import net.codemania.codegeneration.NodeVisitor;

public record NodeExpressionLiteralString( String value,
					   FilePosition position ) implements INodeExpression
{

@Override
public String accept ( NodeVisitor visitor )
{
	return visitor.visit( this );
}
}
