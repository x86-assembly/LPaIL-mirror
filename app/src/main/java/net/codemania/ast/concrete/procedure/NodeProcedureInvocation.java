package net.codemania.ast.concrete.procedure;

import net.codemania.FilePosition;
import net.codemania.ast.node_types.INodeExpression;
import net.codemania.ast.node_types.INodeStatement;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

import java.util.List;

public record NodeProcedureInvocation( String procedureName, List<INodeExpression> args,
				       FilePosition position ) implements INodeStatement
{
@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
