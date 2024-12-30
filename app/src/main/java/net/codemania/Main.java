package net.codemania;

import net.codemania.cli.Logger;
import net.codemania.lexing.Lexer;
import net.codemania.lexing.Token;
import net.codemania.lexing.exceptions.LexerUnexpectedCharacterException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Main
{
public static void main ( String[] args )
{
	System.exit( mainWrapper( args ) );
}

public static BufferedReader getFileReader ( File file )
{
	BufferedReader reader;
	try {
		reader = new BufferedReader( new FileReader( file ) );
	} catch ( FileNotFoundException e ) {
		Logger.err( String.format( "File %s not found!", file.getName() ) );
		return null;
	}
	return reader;
}

public static int mainWrapper ( String[] args )
{


	if ( args.length != 1 ) {
		Logger.err( "Too " + ( args.length > 1 ? "many": "few" ) + " arguments (expected `lpail <filename>`)" );
		System.exit( 1 );
		return 1;
	}

	File file = new File( args[0] );
	BufferedReader reader = getFileReader( file );
	if ( reader == null ) {
		return 2;
	}
	Lexer lexer = new Lexer( reader, file.getName() );
	List<Token> tokens;
	try {
		tokens = lexer.lexAll();
		for ( Token t : tokens ) {
			Logger.info( t );
		}
	} catch ( LexerUnexpectedCharacterException e ) {
		Logger.err( "Failed to lex: " + e.getMessage() );
	}


	return 0;
}

}
