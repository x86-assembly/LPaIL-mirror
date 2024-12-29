package net.codemania;

import net.codemania.cli.Logger;

public class Main
{
public static void main ( String[] args )
{
	if ( args.length != 1 ) {
		Logger.err( "Too " + ( args.length > 1 ? "many": "few" ) + " arguments (expected `lpail <filename>`)" );
		System.exit( 1 );
		return;
	}

	Logger.info( "Example Info" );
	Logger.warn( "Example Warning" );
	Logger.err( "This is not good" );

}

}
