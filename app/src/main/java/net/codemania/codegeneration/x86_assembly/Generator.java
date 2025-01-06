package net.codemania.codegeneration.x86_assembly;

import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralString;
import net.codemania.ast.concrete.procedure.NodeProcedureDeclaration;
import net.codemania.ast.concrete.procedure.NodeProcedureDeclarationDefinition;
import net.codemania.ast.concrete.procedure.NodeProcedureInvocation;
import net.codemania.ast.node_types.INode;
import net.codemania.ast.node_types.INodeExpression;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;
import net.codemania.codegeneration.exceptions.GenerationUnresolvedProcedureNameException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class Generator implements NodeVisitor
{

private int labelCounter = 0;
private StringJoiner dataSegment = new StringJoiner( "\n" ).add( "segment .data" );
private StringJoiner bssSegment = new StringJoiner( "\n" ).add( "segment .bss" );
private StringJoiner textSegment = new StringJoiner( "\n" ).add( "segment .text" );
private Set<String> knownSymbols = new HashSet<>();
private Set<String> definedSymbols = new HashSet<>();

public String generateRootNode ( NodeASTRoot node )
{
	StringJoiner finalProgram = new StringJoiner( "\n" );
	StringBuilder sb = new StringBuilder( "global main\n" );
	sb.append( "main:\n" );
	sb.append( "\tpush rbp\n" );

	for ( INode child : node.children() ) {
		try {sb.append( child.accept( this ) );} catch ( GenerationException e ) {
			throw new RuntimeException( e );
		}
	}
	textSegment.add( sb );
	textSegment.add( "\tpop rbp" );
	textSegment.add( "\tret" );

	for ( String symbol : knownSymbols ) {
		if ( !definedSymbols.contains( symbol ) ) {
			finalProgram.add( "extern " + symbol );
		}
	}

	finalProgram.merge( dataSegment );
	finalProgram.merge( bssSegment );

	finalProgram.merge( textSegment );
	return finalProgram.toString();

}

@Override
public String visit ( NodeProcedureInvocation node ) throws GenerationException
{
	String name = node.procedureName();
	if ( !knownSymbols.contains( name ) ) {
		throw new GenerationUnresolvedProcedureNameException( name, node.position() );
	}

	StringJoiner sj = new StringJoiner( "\n" );
	List<INodeExpression> args = node.args();
	for ( INodeExpression arg : args.reversed() ) {
		// put expression value into rax
		sj.add( arg.accept( this ) );
		sj.add( "\tpush rax" );
	}

	String regs[] = new String[] { "rdi", "rsi", "rdx", "rcx", "r8", "r9" };
	for ( int i = 0; i < Math.min( args.size(), 6 ); i++ ) {
		sj.add( "\tpop " + regs[i] );
	}
	sj.add( "\tmov eax, 0" ); // idk why but c does it
	sj.add( "\tcall " + name );
	return sj.toString();

}


@Override
public String visit ( NodeASTRoot node )
{
	return "";
}

/*
 * Expressions
 *
 * for now there won't be optimisation, so all expressions will store their
 * end result in rax for ease of use.
 */
@Override
public String visit ( NodeExpressionLiteralInteger node )
{
	return "\n\tmov rax, " + node.value();
}

@Override
public String visit ( NodeExpressionLiteralString node )
{
	String label = "lc" + ( labelCounter++ );
	StringBuilder sb = new StringBuilder( "\t%s db ".formatted( label ) );
	boolean mode = false; // is currently a string
	for ( char c : node.value().toCharArray() ) {
		if ( c < ' ' || c > '~' ) // non ascii printable
		{
			if ( mode ) {
				sb.append( "\", " );
				mode = false;
			}
			sb.append( (int) c );
			sb.append( ", " );
		} else {
			if ( !mode ) {
				sb.append( '"' );
				mode = true;
			}
			sb.append( c );
		}
	}
	if ( mode ) sb.append( "\"" );
	dataSegment.add( sb );

	return "\n\tmov rax, " + label;
}

@Override
public String visit ( NodeProcedureDeclaration node )
{
	knownSymbols.add( node.label() );
	return "";
}

@Override
public String visit ( NodeProcedureDeclarationDefinition node )
{
	return "";
}

}