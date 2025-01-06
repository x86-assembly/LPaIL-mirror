package net.codemania.lexing;

public enum TokenType
{
	Tilde,
	BracketSquOpen,
	BracketSquClose,
	IntegerLiteral,
	ArrowThinLeft,
	ArrowThickLeft,
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