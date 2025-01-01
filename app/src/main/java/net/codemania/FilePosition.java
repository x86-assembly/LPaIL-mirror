package net.codemania;

public record FilePosition( int line, int col, String filename )
{

@Override
public String toString ()
{
	StringBuilder sb = new StringBuilder( "at " );
	if ( line == -1 ) {
		sb.append( "unknown position" );
	} else {
		sb.append( "line " ).append( line );
		sb.append( " column " ).append( col );
	}

	if ( filename != null ) sb.append( " in \"" ).append( filename ).append( '"' );
	return sb.toString();
}
}
