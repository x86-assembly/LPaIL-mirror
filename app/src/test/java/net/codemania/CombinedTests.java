package net.codemania;

import net.codemania.ast.node_types.INode;
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
	INode n;
	try {
		n = new Parser( new Lexer( "~[irIM]->.exit;" ) ).parse();
	} catch ( LexingException | ParsingException e ) {
		throw new RuntimeException( e );
	}
	System.out.println( n );
}
}
