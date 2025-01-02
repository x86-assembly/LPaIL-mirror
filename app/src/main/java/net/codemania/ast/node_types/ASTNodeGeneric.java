package net.codemania.ast.node_types;

import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

public interface ASTNodeGeneric
{
String accept ( NodeVisitor visitor ) throws GenerationException;
}
