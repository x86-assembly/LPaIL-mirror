package net.codemania.parsing.ast;

import net.codemania.parsing.ast.types.ASTNode;

import java.util.ArrayList;
import java.util.List;

public record NodeASTRoot( List<ASTNode> children ) implements ASTNode
{
public NodeASTRoot ()
{
	this( new ArrayList<>() );
}

}
