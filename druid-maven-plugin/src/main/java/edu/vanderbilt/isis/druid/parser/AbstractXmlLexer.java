
package edu.vanderbilt.isis.druid.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;

/**
 * This is the super class for the XMLlexer. It is extended by the lexer class
 * generated from XMLLexer.g. Do not place code and declarations in the XMLLexer .g
 * files, use a superclass like this and place all the support methods and error
 * overrides etc in the super class. This way you will keep the lexer grammar
 * clean.
 */
public abstract class AbstractXmlLexer extends Lexer

{
    public AbstractXmlLexer() {
    }

    public AbstractXmlLexer(CharStream input) {
        super(input);
    }
}
