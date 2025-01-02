package net.codemania.ast.concrete.expression;

import net.codemania.FilePosition;
import net.codemania.ast.node_types.ASTNodeExpression;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

public record NodeExpressionLiteralInteger( int value,
					    FilePosition position ) implements ASTNodeExpression
{

@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
