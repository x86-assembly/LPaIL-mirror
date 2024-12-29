package net.codemania.cli;

import java.io.PrintStream;

public class Logger
{

private static Logger logger = null;

public PrintStream info, warn, err;
public boolean shouldLogInfo, shouldLogWarn, shouldLogErr;

private ANSII ansii;

private Logger ()
{
	logger = this;
	this.info = System.out; // might also be changed to default of `System.err`
	this.warn = System.err;
	this.err = System.err;
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
	this.info.print( "[INFO] " );
	this.info.println( x );
}

public void logWarning ( Object x )
{
	if ( !shouldLogWarn ) return;
	this.warn.print( ansii.format( ANSII.YELLOW ) );
	this.warn.print( "[WARN] " );
	this.warn.print( ansii.reset() );
	this.warn.println( x );
}

public void logError ( Object x )
{
	if ( !shouldLogErr ) return;
	this.err.print( ansii.format( ANSII.BACKGR, ANSII.RED ) );
	this.err.print( "[ERROR]" );
	this.err.print( ansii.format( ANSII.RED ) );
	this.err.print( ' ' );
	this.err.print( x );
	this.err.println( ansii.reset() );
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
