package net.codemania.codegeneration;

import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralString;
import net.codemania.ast.concrete.expression.NodeExpressionVariable;
import net.codemania.ast.concrete.procedure.NodeProcedureDeclaration;
import net.codemania.ast.concrete.procedure.NodeProcedureDefinition;
import net.codemania.ast.concrete.statements.NodeProcedureInvocation;
import net.codemania.ast.concrete.statements.NodeVariableAssignment;
import net.codemania.ast.concrete.statements.NodeVariableDeclaration;
import net.codemania.codegeneration.exceptions.GenerationException;

public interface NodeVisitor
{
String visit ( NodeProcedureInvocation node ) throws GenerationException;

String visit ( NodeASTRoot node );

String visit ( NodeExpressionLiteralInteger node );

String visit ( NodeExpressionLiteralString node );

String visit ( NodeProcedureDeclaration node );

String visit ( NodeProcedureDefinition node ) throws GenerationException;

String visit ( NodeVariableAssignment node ) throws GenerationException;

String visit ( NodeVariableDeclaration node );

String visit ( NodeExpressionVariable node ) throws GenerationException;


}
