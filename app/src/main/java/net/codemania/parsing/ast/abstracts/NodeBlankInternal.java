package net.codemania.parsing.ast.abstracts;

import net.codemania.parsing.ast.types.ASTInternalNode;
import net.codemania.parsing.ast.types.ASTNode;

import java.util.ArrayList;
import java.util.List;

public abstract class NodeBlankInternal implements ASTInternalNode
{
private List<ASTNode> children;

public NodeBlankInternal ()
{
	this.children = new ArrayList<>();
}

public List<ASTNode> getChildren ()
{
	return this.children;
}

}
