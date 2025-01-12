package net.codemania.codegeneration.x86_assembly;

import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralString;
import net.codemania.ast.concrete.expression.NodeExpressionVariable;
import net.codemania.ast.concrete.procedure.NodeProcedureDeclaration;
import net.codemania.ast.concrete.procedure.NodeProcedureDefinition;
import net.codemania.ast.concrete.statements.NodeProcedureInvocation;
import net.codemania.ast.concrete.statements.NodeVariableAssignment;
import net.codemania.ast.concrete.statements.NodeVariableDeclaration;
import net.codemania.ast.node_types.INode;
import net.codemania.ast.node_types.INodeExpression;
import net.codemania.ast.node_types.INodeStatement;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;
import net.codemania.codegeneration.exceptions.GenerationUnresolvedProcedureNameException;
import net.codemania.codegeneration.exceptions.GenerationUnresolvedVariableNameException;

import java.util.*;

public class Generator implements NodeVisitor
{

private int labelCounter = 0;
private StringJoiner dataSegment = new StringJoiner( "\n" ).add( "segment .data" );
private StringJoiner bssSegment = new StringJoiner( "\n" ).add( "segment .bss" );
private StringJoiner textSegment = new StringJoiner( "\n" ).add( "segment .text" );
private Set<String> knownSymbols = new HashSet<>();
private Set<String> definedSymbols = new HashSet<>();
private Context ctx;

public Generator ()
{
	this( new Context( new HashMap<>(), 0 ) );
}

public Generator ( Context context )
{
	this.ctx = context;
}

public String generateRootNode ( NodeASTRoot node )
{
	StringJoiner finalProgram = new StringJoiner( "\n" );
	StringJoiner sj = new StringJoiner( "\n" );
	sj.add( "global main" );

	for ( INode child : node.children() ) {
		try {sj.add( child.accept( this ) );} catch ( GenerationException e ) {
			throw new RuntimeException( e );
		}
	}
	textSegment.merge( sj );

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
	sj.add( "\tmov eax, 0" ); // something with floats?
	sj.add( "\tcall " + name );
	if ( node.returnVar() != null ) {
		String returnName = node.returnVar();
		int offset;
		if ( ctx.variables.containsKey( returnName ) ) {
			offset = ctx.variables.get( returnName );
		} else {offset = newVariable( returnName );}
		sj.add( "\tmov QWORD [rbp-%d], rax\t; return into $%s".formatted( offset, returnName ) );
	}
	return sj.toString();

}

@Override
public String visit ( NodeProcedureDeclaration node )
{
	knownSymbols.add( node.label() );
	return "";
}

private int newVariable ( String name )
{
	// why do all records have to be final??
	this.ctx = new Context( ctx.variables, ctx.stackSize + 8 );
	ctx.variables.put( name, ctx.stackSize );
	return ctx.stackSize;
}

@Override
public String visit ( NodeVariableDeclaration node )
{
	newVariable( node.name() );
	return "";
}

@Override
public String visit ( NodeVariableAssignment node ) throws GenerationException
{
	StringJoiner sj = new StringJoiner( "\n" );
	sj.add( node.value().accept( this ) );
	sj.add( "\tmov QWORD [rbp-%d], rax".formatted( newVariable( node.name() ) ) );

	return sj.toString();
}

@Override
public String visit ( NodeProcedureDefinition node ) throws GenerationException
{
	knownSymbols.add( node.label() );
	definedSymbols.add( node.label() );
	StringJoiner statements = new StringJoiner( "\n" );
	this.ctx = new Context( new HashMap<>(), 0 );
	for ( INodeStatement stmt : node.statements() ) {
		statements.add( stmt.accept( this ) );
	}
	StringJoiner sj = new StringJoiner( "\n" );
	sj.add( node.label() + ":" );
	sj.add( "\tpush rsp" );
	sj.add( "\tmov rbp, rsp" );
	int stackSize = ctx.stackSize;
	// 16 byte align the stack to support c function calls
	sj.add( "\tsub rsp, " + ( stackSize + 16 - ( stackSize % 16 ) ) );
	sj.merge( statements );
	sj.add( "\n\tmov rsp, rbp" );
	sj.add( "\tpop rbp" );
	sj.add( "\tmov rax, 0" );
	sj.add( "\tret" );
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
	String string = node.value() + '\0';
	for ( char c : string.toCharArray() ) {
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
public String visit ( NodeExpressionVariable node ) throws GenerationException
{
	if ( !this.ctx.variables.containsKey( node.name() ) ) {
		throw new GenerationUnresolvedVariableNameException( node.name(), node.pos() );
	}
	String placeholder = "\tmov rax, [rbp-%d]\t; var: $%s";
	return placeholder.formatted( ctx.variables.get( node.name() ), node.name() );
}

/*
 * variables: name -> rbp offset
 */
private record Context( Map<String, Integer> variables, int stackSize )
{

}

}