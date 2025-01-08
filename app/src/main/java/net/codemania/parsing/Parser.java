package net.codemania.parsing;

import net.codemania.TokenStream;
import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralString;
import net.codemania.ast.concrete.procedure.NodeProcedureDeclaration;
import net.codemania.ast.concrete.procedure.NodeProcedureDefinition;
import net.codemania.ast.concrete.procedure.NodeProcedureInvocation;
import net.codemania.ast.node_types.INodeExpression;
import net.codemania.ast.node_types.INodeProcedure;
import net.codemania.ast.node_types.INodeStatement;
import net.codemania.lexing.BlockType;
import net.codemania.lexing.Token;
import net.codemania.lexing.TokenType;
import net.codemania.lexing.exceptions.LexingException;
import net.codemania.parsing.exceptions.ParserUnexpectedSymbolException;
import net.codemania.parsing.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;

public class Parser
{

private final TokenStream tokenStream;

public Parser ( TokenStream stream )
{
	this.tokenStream = stream;
}

public NodeASTRoot parse () throws LexingException, ParsingException
{
	readNext(); // initialize the lexer
	NodeASTRoot root = new NodeASTRoot();
	while ( tokenStream.hasMoreTokens() ) {
		root.children().add( switch ( current.getType() ) {
			//case Tilde -> parseProcedureInvocation();
			case KwProcedure -> parseProcedure();
			default -> throw new ParserUnexpectedSymbolException( current );
		} );
	}


	return root;
}


private INodeProcedure parseProcedure () throws ParserUnexpectedSymbolException, LexingException
{
	consume( TokenType.KwProcedure );
	String name = (String) consume( TokenType.Label ).getVal();
	// procedure declaration
	if ( !current.is( TokenType.Colon ) ) {
		consume( TokenType.Semicolon );
		return new NodeProcedureDeclaration( name );
	}


	// procedure definition
	consume( TokenType.Colon );
	List<INodeStatement> statements = new ArrayList<>();
	while ( !( current.is( TokenType.END ) && current.getVal().equals( BlockType.PROCEDURE ) ) ) {
		statements.add( parseStatement() );
	}
	consume( TokenType.END );
	consume( TokenType.Semicolon );
	return new NodeProcedureDefinition( name, statements );

}

public INodeStatement parseStatement () throws LexingException, ParserUnexpectedSymbolException
{
	INodeStatement statement = switch ( current.getType() ) {
		case Tilde -> parseProcedureInvocation();
		default -> throw new ParserUnexpectedSymbolException( current );
	};
	return statement;
}

private NodeProcedureInvocation parseProcedureInvocation () throws LexingException, ParserUnexpectedSymbolException
{
	// ~[arg1, arg2] -> .label;
	consume( TokenType.Tilde );
	// for now brackets are Required
	consume( TokenType.BracketSquOpen );

	List<INodeExpression> args = new ArrayList<>();
	while ( current.getType() != TokenType.BracketSquClose ) {
		args.add( parseExpression() );

		try {
			consume( TokenType.Comma );// trailing commas are permitted
		} catch ( ParserUnexpectedSymbolException e ) {
			break;
		}
	}
	consume( TokenType.BracketSquClose );
	consume( TokenType.ArrowThinRight );
	Token label = consume( TokenType.Label );
	consume( TokenType.Semicolon );

	return new NodeProcedureInvocation( (String) label.getVal(), args, label.getPos() );
}

private INodeExpression parseExpression () throws ParserUnexpectedSymbolException, LexingException
{
	// for now tokens are a single expression
	INodeExpression parsed = switch ( current.getType() ) {
		case IntegerLiteral ->
			new NodeExpressionLiteralInteger( (int) current.getVal(), current.getPos() );
		case String ->
			new NodeExpressionLiteralString( (String) current.getVal(), current.getPos() );
		default -> throw new ParserUnexpectedSymbolException( current );
	};
	readNext();
	return parsed;

	//args.add( new NodeExpressionLiteralInteger( (int) argument.getVal(), argument.getPos()
	// ) );
	//return null;
}


private Token current;
private Token lastNotNull;

private Token readNext () throws LexingException
{
	this.current = tokenStream.nextToken();
	if ( this.current != null ) this.lastNotNull = this.current;
	return this.current;
}

private Token consume ( TokenType type ) throws LexingException, ParserUnexpectedSymbolException
{
	Token t = this.current;
	if ( t == null ) {
		throw new ParserUnexpectedSymbolException( TokenType.EOF, type, lastNotNull.getPos() );
	}
	if ( t.getType() != type ) {
		throw new ParserUnexpectedSymbolException( t, type );
	}
	readNext();
	return t;
}

}
