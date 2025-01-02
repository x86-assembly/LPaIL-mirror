package net.codemania;

import net.codemania.cli.Logger;
import net.codemania.codegeneration.x86_assembly.Generator;
import net.codemania.lexing.Lexer;
import net.codemania.parsing.Parser;
import org.junit.jupiter.api.Test;

public class TestAll
{
@Test
public void testexample ()
{
	String program = "~[irXLII]->.exit;";
	Logger.info( "Compiling program `%s`:".formatted( program ) );
	String prog = "\n";
	try {
		prog += new Generator().generateRootNode( new Parser( new Lexer( program ) ).parse() );
	} catch ( Exception e ) {
		throw new RuntimeException( e );
	}
	Logger.info( prog );
}
}
