package net.codemania.codegeneration.x86_assembly;

import net.codemania.ast.concrete.NodeASTRoot;
import net.codemania.ast.concrete.NodeProcedureInvocation;
import net.codemania.ast.concrete.expression.NodeExpressionLiteralInteger;
import net.codemania.ast.node_types.ASTNodeExpression;
import net.codemania.ast.node_types.ASTNodeGeneric;
import net.codemania.codegeneration.NodeVisitor;
import net.codemania.codegeneration.exceptions.GenerationException;
import net.codemania.codegeneration.exceptions.GenerationProcedureArgumentTooManyException;
import net.codemania.codegeneration.exceptions.GenerationUnresolvedProcedureNameException;

import java.util.List;

public class Generator implements NodeVisitor
{

public String generateRootNode ( NodeASTRoot node )
{
	StringBuilder sb = new StringBuilder( "global _start\n" );
	sb.append( "_start:\n" );

	for ( ASTNodeGeneric child : node.children() ) {
		try {sb.append( child.accept( this ) );} catch ( GenerationException e ) {
			throw new RuntimeException( e );
		}
	}

	return sb.toString();

}

@Override
public String visit ( NodeProcedureInvocation node ) throws GenerationException
{
	StringBuilder sb = new StringBuilder();
	String invokedName = node.procedureName();
	// for now the only available procedure is "exit"
	if ( !invokedName.equals( "exit" ) ) {
		throw new GenerationUnresolvedProcedureNameException( invokedName, node.position() );
	}
	// linux syscall registers
	/*
	 * 1: RDI
	 * 2: RSI
	 * 3: RDX
	 * 4: R10
	 * 5: R8
	 * 6: R9
	 */
	List<ASTNodeExpression> args = node.args();
	if ( args.size() > 6 ) {
		throw new GenerationProcedureArgumentTooManyException( invokedName, args.size(), 6, node.position() );
	}

	for ( ASTNodeExpression arg : args ) {
		// should put it in rax
		sb.append( arg.accept( this ) );
		sb.append( "\n\tpush rax" );
	}
	// all args are now on stack
	// so we now pop them off, and into their respective registers
	for ( int i = 0; i < args.size(); i++ ) {
		String[] registers = { "rdi", "rsi", "rdx", "r10", "r8", "r9" };
		sb.append( "\n\tpop " ).append( registers[args.size() - 1 - i] );
		// `args.size() -1`
		// since the args are in reverse order, we need to put the first
		// arg into the last of the used registers, and the last into
		// the first register
	}
	// args should now be properly loaded into rdi-r9
	sb.append( "\n\tmov rax, " ).append( Syscalls.SYS_EXIT.call_no );
	sb.append( "\n\tsyscall" );
	return sb.toString();
}

/**
 *
 */
private enum Syscalls
{
	SYS_EXIT( 60 ),
	;

	public final int call_no;

	Syscalls ( int n ) {this.call_no = n;}
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
}
