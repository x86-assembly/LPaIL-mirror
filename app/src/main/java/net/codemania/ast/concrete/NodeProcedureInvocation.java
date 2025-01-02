package net.codemania.ast.concrete;

import net.codemania.FilePosition;
import net.codemania.ast.node_types.ASTNodeExpression;
import net.codemania.ast.node_types.ASTNodeGeneric;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

import java.util.List;

public record NodeProcedureInvocation( String procedureName, List<ASTNodeExpression> args,
				       FilePosition position ) implements ASTNodeGeneric
{
@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
