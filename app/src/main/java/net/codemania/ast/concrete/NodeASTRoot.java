package net.codemania.ast.concrete;

import net.codemania.ast.node_types.INode;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

import java.util.ArrayList;
import java.util.List;

public record NodeASTRoot( List<INode> children ) implements INode
{
public NodeASTRoot ()
{
	this( new ArrayList<>() );
}

@Override
public String accept ( NodeVisitor visitor ) throws GenerationException
{
	return visitor.visit( this );
}
}
