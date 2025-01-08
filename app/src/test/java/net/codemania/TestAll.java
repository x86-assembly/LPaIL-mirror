package net.codemania;

import net.codemania.cli.Logger;
import net.codemania.codegeneration.x86_assembly.Generator;
import net.codemania.lexing.Lexer;
import net.codemania.lexing.exceptions.LexingException;
import net.codemania.parsing.Parser;
import net.codemania.parsing.exceptions.ParsingException;
import org.junit.jupiter.api.Test;

public class TestAll
{
@Test
public void testexample () throws LexingException, ParsingException
{
	Logger.getLogger().useColor( false );

	String program = """
		PROC .printf;
		PROC .main:
			~["Hello, World!\\n"]->.printf;
		END-PROC;
		""";
	Logger.info( new Parser( new Lexer( program ) ).parse() );
	Logger.info( "--------" );
	Logger.info( "Compiling program\n```\n%s\n```".formatted( program ) );
	Logger.info( "\n" + new Generator().generateRootNode( new Parser( new Lexer( program ) ).parse() ) );
}
}
