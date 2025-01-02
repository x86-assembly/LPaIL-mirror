package net.codemania.parsing;

import net.codemania.TokenStream;
import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.NodeProcedureInvocation;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.ast.node_types.ASTNodeExpression;
import net.codemania.lexing.Token;
import net.codemania.lexing.TokenType;
import net.codemania.lexing.exceptions.LexingException;
import net.codemania.parsing.exceptions.ParserUnexpectedSymbolException;
import net.codemania.parsing.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;

public class Parser
{

private TokenStream tokenStream;

public Parser ( TokenStream stream )
{
	this.tokenStream = stream;
}

public NodeASTRoot parse () throws LexingException, ParsingException
{
	readNext();
	NodeASTRoot root = new NodeASTRoot();
	while ( tokenStream.hasMoreTokens() ) {
		root.children().add( switch ( current.getType() ) {
			case Tilde -> parseProcedureInvocation();
			default -> throw new ParserUnexpectedSymbolException( current );
		} );
	}


	return root;
}

private NodeProcedureInvocation parseProcedureInvocation () throws LexingException, ParserUnexpectedSymbolException
{
	consume( TokenType.Tilde );
	// for now brackets are Required
	consume( TokenType.BracketSquOpen );

	List<ASTNodeExpression> args = new ArrayList<>();
	Token argument = consume( TokenType.IntegerLiteral );
	args.add( new NodeExpressionLiteralInteger( (int) argument.getVal(), argument.getPos() ) );

	consume( TokenType.BracketSquClose );
	consume( TokenType.ArrowThinLeft );
	Token label = consume( TokenType.Label );
	consume( TokenType.Semicolon );

	return new NodeProcedureInvocation( (String) label.getVal(), args, label.getPos() );
}

private Token current;

private Token readNext () throws LexingException
{
	return ( this.current = tokenStream.nextToken() );
}

private Token consume ( TokenType type ) throws LexingException, ParserUnexpectedSymbolException
{
	Token t = this.current;
	if ( t.getType() != type ) {
		throw new ParserUnexpectedSymbolException( this.current, type );
	}
	readNext();
	return t;
}

}
