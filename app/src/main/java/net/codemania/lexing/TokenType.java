package net.codemania.lexing;

public enum TokenType
{
	Tilde,
	BracketSquOpen,
	BracketSquClose,
	IntegerLiteral,
	ArrowThinRight,
	ArrowThickRight,
	Label, // lowercase, usually procedure or variable names
	Semicolon,
	Colon,
	Comma,
	String,
	// Keywords
	KwProcedure,

	//
	EOF,

}