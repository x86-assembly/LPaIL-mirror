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

	// ENDS
	/*
	 * End of any block like procedures or if statements
	 * Has the type of statement it ends as it's value
	 */
	END,

	EOF,

}