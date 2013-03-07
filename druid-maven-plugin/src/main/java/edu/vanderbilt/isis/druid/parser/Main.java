
package edu.vanderbilt.isis.druid.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Test driver program
 */
class Main {

    @SuppressWarnings("unused")
    private static boolean makeDot = false;

    static XMLLexer lexer;

    public static void main(String[] args)
    {
        try
        {
            lexer = new XMLLexer(null);

            if (args.length > 0)
            {
                int s = 0;

                if (args[0].startsWith("-dot"))
                {
                    makeDot = true;
                    s = 1;
                }
                // Recursively parse each directory, and each file on the
                // command line
                //
                for (int i = s; i < args.length; i++)
                {
                    parse(new File(args[i]));
                }
            }
            else
            {
                System.err
                        .println("Usage: java -jar druid-maven-plugin-2.0.0-jar-with-dependencies.jar <directory | filename.dmo>");
            }
        } catch (Exception ex)
        {
            System.err.println("ANTLR demo parser threw exception:");
            ex.printStackTrace();
        }
    }

    public static void parse(File source) throws Exception
    {
        try
        {
            if (source.isDirectory())
            {
                System.out.println("Directory: " + source.getAbsolutePath());
                String files[] = source.list();

                for (int i = 0; i < files.length; i++)
                {
                    parse(new File(source, files[i]));
                }
            }
            else
            {
                String sourceFile = source.getName();

                if (sourceFile.length() > 3)
                {
                    String suffix = sourceFile.substring(sourceFile.length() - 4).toLowerCase();

                    if (suffix.compareTo(".dmo") == 0)
                    {
                        parseSource(source.getAbsolutePath());
                    }
                }
            }
        } catch (Exception ex)
        {
            System.err.println("ANTLR demo parser caught error on file open:");
            ex.printStackTrace();
        }

    }

    public static void parseSource(String source) throws Exception
    {
        try
        {
            final InputStream rawInput = new FileInputStream(source);
            final ANTLRInputStream input = new ANTLRInputStream(rawInput);

            XMLLexer lexer = new XMLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            @SuppressWarnings("unused")
            final XMLParser parser = new XMLParser(tokens);
        } catch (FileNotFoundException ex)
        {
            System.err.println("\n  !!The file " + source + " does not exist!!\n");
        } catch (Exception ex)
        {
            System.err.println("Parser threw an exception:\n\n");
            ex.printStackTrace();
        }
    }

}
