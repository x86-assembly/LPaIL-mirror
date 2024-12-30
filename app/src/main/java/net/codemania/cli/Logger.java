package net.codemania.cli;

import java.io.PrintStream;

public class Logger
{

private static Logger logger = null;

public PrintStream infoStream, warnStream, errStream;
public boolean shouldLogInfo, shouldLogWarn, shouldLogErr;

private ANSII ansii;

private Logger ()
{
	logger = this;
	this.infoStream = System.out; // might also be changed to default of `System.err`
	this.warnStream = System.err;
	this.errStream = System.err;
	this.shouldLogInfo = this.shouldLogWarn = this.shouldLogErr = true;
	this.ansii = new ANSII( true );
}

public boolean hasColor ()
{
	return ansii.enabled;
}

public void useColor ()
{
	useColor( true );
}

public void useColor ( boolean use )
{
	this.ansii.enabled = use;
}


public void logInfo ( Object x )
{
	if ( !shouldLogInfo ) return;
	this.infoStream.print( "[INFO] " );
	this.infoStream.println( x );
}

public void logWarning ( Object x )
{
	if ( !shouldLogWarn ) return;
	this.warnStream.print( ansii.format( ANSII.YELLOW ) );
	this.warnStream.print( "[WARN] " );
	this.warnStream.print( ansii.reset() );
	this.warnStream.println( x );
}

public void logError ( Object x )
{
	if ( !shouldLogErr ) return;
	this.errStream.print( ansii.format( ANSII.BACKGR, ANSII.RED ) );
	this.errStream.print( "[ERROR]" );
	this.errStream.print( ansii.format( ANSII.RED ) );
	this.errStream.print( ' ' );
	this.errStream.print( x );
	this.errStream.println( ansii.reset() );
}

// Not sure if this should be public, but for now it is
public static Logger getLogger ()
{
	if ( logger == null ) {
		new Logger();
	}
	return logger;
}

public static void info ( Object x )
{
	getLogger().logInfo( x );
}

public static void warn ( Object x )
{
	getLogger().logWarning( x );
}

public static void err ( Object x )
{
	getLogger().logError( x );
}


private class ANSII
{
	public ANSII ( boolean enabled )
	{
		this.enabled = enabled;
	}

	private boolean enabled = true;

	private String format ( char mode, char color )
	{
		return enabled ? "\033[" + mode + ";3" + color + 'm': "";
	}

	private String format ( char color )
	{
		return format( REGULAR, color );
	}

	private static final char REGULAR = '0';
	private static final char BOLD = '1';
	private static final char ULINED = '4';
	private static final char BACKGR = '7';


	private static final char BLACK = '0';
	private static final char RED = '1';
	private static final char GREEN = '2';
	private static final char YELLOW = '3';
	private static final char BLUE = '4';
	private static final char PURPLE = '5';
	private static final char CYAN = '6';
	private static final char WHITE = '7';

	private String reset ()
	{
		return enabled ? "\033[0m": "";
	}
}
}
