package net.codemania.parsing.ast;

import net.codemania.parsing.ast.abstracts.NodeExpression;
import net.codemania.parsing.ast.types.ASTInternalNode;

import java.util.List;

public record NodeProcedureInvocation( String procedureName,
				       List<NodeExpression> args ) implements ASTInternalNode
{

}
