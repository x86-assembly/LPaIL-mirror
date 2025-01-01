package net.codemania;

import net.codemania.lexing.Token;
import net.codemania.lexing.exceptions.LexingException;

import java.io.IOException;

public interface TokenStream
{
Token nextToken () throws LexingException;

boolean hasMoreTokens ();

void reset () throws IOException;

void close () throws IOException;
}
