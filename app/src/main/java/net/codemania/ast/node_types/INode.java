package net.codemania.ast.node_types;

import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

public interface INode
{
String accept ( NodeVisitor visitor ) throws GenerationException;
}
