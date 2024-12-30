package net.codemania.lexing;

public enum TokenType
{
	Tilde,
	BracketSquOpen,
	BracketSquClose,
	IntegerLiteral,
	ArrowThinLeft,
	ArrowThickLeft,
	Identifier, // uppercase
	Label, // lowercase, usually procedure or variable names
	Semicolon,

}