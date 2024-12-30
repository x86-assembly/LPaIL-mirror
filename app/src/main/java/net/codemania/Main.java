package net.codemania;

import net.codemania.cli.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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

	System.out.println( ( new File( "." ) ).getAbsolutePath() );

	if ( args.length != 1 ) {
		Logger.err( "Too " + ( args.length > 1 ? "many": "few" ) + " arguments (expected `lpail <filename>`)" );
		System.exit( 1 );
		return 1;
	}

	BufferedReader reader = getFileReader( new File( args[0] ) );
	if ( reader == null ) {
		return 2;
	}


	return 0;
}

}
