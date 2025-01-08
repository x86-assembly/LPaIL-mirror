package net.codemania.codegeneration;

import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralString;
import net.codemania.ast.concrete.procedure.NodeProcedureDeclaration;
import net.codemania.ast.concrete.procedure.NodeProcedureDefinition;
import net.codemania.ast.concrete.procedure.NodeProcedureInvocation;
import net.codemania.codegeneration.exceptions.GenerationException;

public interface NodeVisitor
{
String visit ( NodeProcedureInvocation node ) throws GenerationException;

String visit ( NodeASTRoot node );

String visit ( NodeExpressionLiteralInteger node );

String visit ( NodeExpressionLiteralString node );

String visit ( NodeProcedureDeclaration node );

String visit ( NodeProcedureDefinition node ) throws GenerationException;
}
