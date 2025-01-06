package net.codemania.lexing;

import net.codemania.FilePosition;

import java.util.Objects;

public class Token
{

private TokenType type;
private Object val;
private FilePosition pos;

public Token ( TokenType type, Object value, FilePosition pos )
{
	this.type = type;
	this.val = value;
	this.pos = pos;
}

public Token ( TokenType type, FilePosition pos )
{
	this( type, null, pos );
}

public FilePosition getPos ()
{
	return this.pos;
}

public void setPos ( FilePosition pos )
{
	this.pos = pos;
}

public boolean is ( TokenType equalType )
{
	return this.type == equalType;
}

public boolean is ( Token token )
{
	return this.type == token.getType();
}

public TokenType getType ()
{
	return type;
}

public void setType ( TokenType type )
{
	this.type = type;
}

public Object getVal ()
{
	return val;
}

public void setVal ( Object val )
{
	this.val = val;
}

@Override
public String toString ()
{
	return this.type.name() + ( this.val == null ? "": ( "(" + this.val + ")" ) );

}

@Override
public boolean equals ( Object o )
{
	if ( o == null || getClass() != o.getClass() ) return false;
	Token token = (Token) o;
	return type == token.type && Objects.equals( val, token.val );
}
}