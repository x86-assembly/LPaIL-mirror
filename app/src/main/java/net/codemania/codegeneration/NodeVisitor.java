package net.codemania.codegeneration;

import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.NodeProcedureInvocation;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.codegeneration.exceptions.GenerationException;

public interface NodeVisitor
{
String visit ( NodeProcedureInvocation node ) throws GenerationException;

String visit ( NodeASTRoot node );

String visit ( NodeExpressionLiteralInteger node );
}
