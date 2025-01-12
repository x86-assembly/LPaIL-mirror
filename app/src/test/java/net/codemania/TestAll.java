package net.codemania;

import net.codemania.ast.concrete.NodeASTRoot;
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
		PROC .time;
		PROC .srand;
		PROC .rand;
		PROC .printf;
		PROC .div;
		PROC .main:
			SET $_time;
			~[0]->.time->$_time;
			~[$_time]->.srand;
		
			~[]->.rand->$num;
			~["The random number is %d\\n", $num]->.printf;
		
			~[100, 8]->.div->$div_res;
			~["100 / 8 = %d\\n", $div_res]->.printf;
		END-PROC;
		""";
	NodeASTRoot ast = new Parser( new Lexer( program ) ).parse();
	Logger.info( ast );
	Logger.info( "--------" );
	Logger.info( "Compiling program\n```lpail\n%s\n```".formatted( program ) );
	Logger.info( "\n" + new Generator().generateRootNode( ast ) );
}
}
