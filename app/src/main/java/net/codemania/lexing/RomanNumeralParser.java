package net.codemania.lexing;

import net.codemania.FilePosition;
import net.codemania.lexing.exceptions.LexerUnexpectedCharacterException;

import java.util.ArrayList;
import java.util.List;

public class RomanNumeralParser
{

public static int parseNumeral ( String unparsed ) throws LexerUnexpectedCharacterException
{
	return parseNumeral( unparsed, null );
}

public static int parseNumeral ( String unparsed, FilePosition pos ) throws LexerUnexpectedCharacterException
{
	if ( pos == null ) {
		pos = new FilePosition( -1, 0, "Roman numeral" );
	}
	List<Integer> values = new ArrayList<>( unparsed.length() );
	for ( char c : unparsed.toCharArray() ) {
		int value = switch ( c ) {
			case 'M' -> 1000;
			case 'D' -> 500;
			case 'C' -> 100;
			case 'L' -> 50;
			case 'X' -> 10;
			case 'V' -> 5;
			case 'I' -> 1;
			default ->
				throw new LexerUnexpectedCharacterException( c, "Roman " + "numeral", pos );
		};
		values.add( value );
	}

	// 2475	MMCDLXXV
	// 1000 1000 100 500 50 10 10 5


	List<Integer> result = new ArrayList<>( values );
	while ( values.size() != 1 ) {
		result = new ArrayList<>();
		for ( int i = 0; i < values.size(); i++ ) {
			int item = values.get( i );
			if ( i == ( values.size() - 1 ) ) { // if last

				result.add( item );
				continue;
			}

			if ( item < values.get( i + 1 ) ) {
				result.add( values.get( i + 1 ) - item );
				i++;
			} else {
				result.add( item );
			}

		}
		if ( result.size() != 1 ) {
			int first = result.getFirst();
			result.remove( 0 );
			result.set( 0, result.get( 0 ) + first );
		}
		values = new ArrayList<>( result );


	}

	return values.getFirst();

}

}
