package net.codemania;

import net.codemania.ast.node_types.ASTNodeGeneric;
import net.codemania.lexing.Lexer;
import net.codemania.lexing.exceptions.LexingException;
import net.codemania.parsing.Parser;
import net.codemania.parsing.exceptions.ParsingException;
import org.junit.jupiter.api.Test;

public class CombinedTests
{

@Test
public void lexAndParseSimpleProcedureInvocation ()
{
	ASTNodeGeneric n;
	try {
		n = new Parser( new Lexer( "~[irIM]->.exit;" ) ).parse();
	} catch ( LexingException e ) {
		throw new RuntimeException( e );
	} catch ( ParsingException e ) {
		throw new RuntimeException( e );
	}
	System.out.println( n );
}
}
