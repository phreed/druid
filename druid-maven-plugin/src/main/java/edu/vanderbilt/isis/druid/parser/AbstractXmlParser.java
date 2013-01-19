package edu.vanderbilt.isis.druid.parser;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

/**
 * The super class of the generated parser. It is extended by the generated
 * code because of the superClass optoin in the .g file.
 *
 * This class contains any helper functions used within the parser
 * grammar itself, as well as any overrides of the standard ANTLR Java
 * runtime methods, such as an implementation of a custom error reporting
 * method, symbol table populating methods and so on.
 *
 * @author Jim Idle - Temporal Wave LLC - jimi@temporal-wave.com
 */
public abstract class AbstractXmlParser extends Parser

{
    /**
     * Create a new parser instance, pre-supplying the input token stream.
     * 
     * @param input The stream of tokens that will be pulled from the lexer
     */
    protected AbstractXmlParser(TokenStream input) {
        super(input);
    }
   
}

