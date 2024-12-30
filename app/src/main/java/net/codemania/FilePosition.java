package net.codemania;

public class FilePosition
{
public final int line, col;
public final String filename;

public FilePosition ( int line, int col, String filename )
{
	this.line = line;
	this.col = col;
	this.filename = filename;
}

@Override
public String toString ()
{
	StringBuilder sb = new StringBuilder();
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
