package net.codemania.codegeneration;

import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.NodeProcedureInvocation;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.codegeneration.exceptions.GenerationException;
import net.codemania.codegeneration.exceptions.GenerationUnresolvedProcedureNameException;

public interface NodeVisitor
{
String visit ( NodeProcedureInvocation node ) throws GenerationUnresolvedProcedureNameException, GenerationException;

String visit ( NodeASTRoot node );

String visit ( NodeExpressionLiteralInteger node );
}
