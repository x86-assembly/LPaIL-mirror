package net.codemania.ast.concrete;

import net.codemania.ast.node_types.ASTNodeGeneric;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;

import java.util.ArrayList;
import java.util.List;

public record NodeASTRoot( List<ASTNodeGeneric> children ) implements ASTNodeGeneric
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
