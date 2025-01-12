package net.codemania.lexing;

public enum TokenType
{
	Tilde,
	BracketSquOpen,
	BracketSquClose,
	IntegerLiteral,
	ArrowThinRight,
	ArrowThickRight,
	Label, // lowercase, like procedure names
	Variable, // Like $a
	Semicolon,
	Colon,
	Comma,
	String,
	// Keywords
	KwProcedure,
	KwSet,

	// ENDS
	/*
	 * End of any block like procedures or if statements
	 * Has the type of statement it ends as it's value
	 */
	END,

	EOF,

}