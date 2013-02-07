
package edu.vanderbilt.isis.druid.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import edu.vanderbilt.isis.druid.parser.BuildComponentListener;
import edu.vanderbilt.isis.druid.parser.ComponentManifestLexer;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser;
import edu.vanderbilt.isis.druid.parser.ContractXmlParser;

/**
 * The actual generator code.
 * 
 * @see DruidMojo for a description of the fields.
 */
public class Generator {
    final private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public Generator(final Logger logger) {
        this.logger = logger;
    }

    private File contractFile = new File("contract.xml");

    private String templateJarName = null;
    private String templateFileName = null;
    private String templateKey;
    private String templateManifestFileName;

    private File skelOutputDir;
    private File baseOutputDir;
    private boolean isSkeleton;
    private Each each;

    public void setSkelOutputDir(final File val) {
        this.skelOutputDir = val;
    }

    public void setBaseOutputDir(final File val) {
        this.baseOutputDir = val;
    }

    /**
     * This method sets the base class for the string template group files.
     * There may be other ways to accomplish this, such as using the
     * getGroupDir() method.
     * 
     * @param templateJarName
     * @throws GeneratorException
     */
    public void setTemplateJarName(String templateJarName) throws GeneratorException {
        this.templateJarName = templateJarName;
        final Thread ct = Thread.currentThread();
        final ClassLoader pcl = ct.getContextClassLoader();
        URL[] nurl;
        try {
            nurl = new URL[] {
                    new URL("file://" + templateJarName)
            };
        } catch (MalformedURLException ex) {
            throw new GeneratorException("could not load template jar", ex);
        }
        final URLClassLoader ucl = new URLClassLoader(nurl, pcl);
        ct.setContextClassLoader(ucl);
    }

    /**
     * The template key points not to a single template but to a group of
     * templates which work together to construct a component. The key is used
     * in conjunction with the template manifest.
     * 
     * @param val
     */
    public void setTemplateKey(String val) {
        this.templateKey = val;
    }

    /**
     * When a single template is to be used.
     * 
     * @param val
     */
    public void setTemplateFileName(final String val) {
        this.templateFileName = val;
    }

    public void setTemplateManifestFileName(final String val) {
        this.templateManifestFileName = val;
    }

    public void setContractPath(final File contract) {
        this.contractFile = contract;
    }

    public void setSkeleton(boolean skeleton) {
        this.isSkeleton = skeleton;
    }

    public void setSkeleton(String skeleton) {
        this.setSkeleton(Boolean.valueOf(skeleton));
    }

    public void setEach(String each) throws GeneratorException {
        final Each val = Each.getValue(each);
        this.setEach(val);
    }

    public void setEach(Each each) {
        this.each = each;
    }

    public static enum Each {
        /** the tables/relations */
        RELATION("relation"),
        /** the messages */
        MESSAGE("message"),
        /** the contract, only one */
        CONTRACT("contract");

        public final String val;

        private Each(String val) {
            this.val = val;
        }

        public static Each getValue(String val) throws GeneratorException {
            if (Each.RELATION.val.equalsIgnoreCase(val)) {
                return Each.RELATION;
            }
            if (Each.MESSAGE.val.equalsIgnoreCase(val)) {
                return Each.MESSAGE;
            }
            if (Each.CONTRACT.val.equalsIgnoreCase(val)) {
                return Each.CONTRACT;
            }
            throw new GeneratorException("unknown each type" + val);
        }
    }

    /**
     * A utility function for loading template group files.
     * 
     * @param logger
     * @param templateFileName
     * @return null if not able to load the file
     * @throws GeneratorException
     */
    private STGroup getGroup(final Logger logger, final String templateFileName)
            throws GeneratorException {
        if (this.templateJarName == null) {
            final File templateFile = new File(templateFileName);
            if (!templateFile.isFile()) {
                throw new GeneratorException("template file path is not found "
                        + templateFile);
            }
            return new STGroupFile(templateFileName, "US-ASCII", '<', '>');
        }
        return new STGroupFile(templateFileName, "US-ASCII", '<', '>');
    }

    /**
     * The main worker which generates the source files from the contract.
     * Decide which template group is being used. There is a template manifest
     * which contains a list of rules for generating file names and templates.
     * <p>
     * <ol>
     * <li>Load the appropriate template group.</li>
     * <li>Determine the file to be written.</li>
     * <li>Build the body of the output file.</li>
     * <li>Write the file.</li>
     * </ol>
     * 
     * @return did the build work?
     * @throws GeneratorException
     */
    public void build() throws GeneratorException {
        final Contract contract = ContractXmlParser.parseXmlFile(getLogger(), this.contractFile);
        STGroup.trackCreationEvents = true;

        /**
         * It may be that the template-file is given explicitly. If that is the
         * case then it should be used.
         */
        if (this.templateFileName != null) {
            if (Each.CONTRACT.equals(this.each)) {
                buildPartUsingTemplate(contract, this.templateFileName);
            } else {
                buildPartUsingTemplate(contract, this.each, this.templateFileName);
            }
            return;
        }

        /**
         * If the template file is not given explicitly then the template key
         * will be used.
         */
        if (this.templateKey == null) {
            throw new GeneratorException("no template key");
        }
        InputStream inputStream = null;
        try {
            if (this.templateJarName == null) {

                final File templateManifestFile = new File(this.templateManifestFileName);
                if (!templateManifestFile.isFile()) {
                    throw new GeneratorException("could not find template manifest "
                            + this.templateManifestFileName);
                }
                try {
                    inputStream = new FileInputStream(templateManifestFile);
                } catch (FileNotFoundException e) {
                    throw new GeneratorException("could not open component manifest file "
                            + this.templateManifestFileName);
                }
            } else {
                final Thread ct = Thread.currentThread();
                final ClassLoader pcl = ct.getContextClassLoader();
                inputStream = pcl.getResourceAsStream(this.templateManifestFileName);
            }

            final ANTLRInputStream input;
            try {
                input = new ANTLRInputStream(inputStream);
            } catch (IOException e) {
                throw new GeneratorException(
                        "could not open component manifest file as an antlr stream "
                                + this.templateManifestFileName);
            }
            final ComponentManifestLexer lexer = new ComponentManifestLexer(input);
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final ComponentManifestParser parser = new ComponentManifestParser(tokens);
            final ParseTree tree = parser.prog();
            final ParseTreeWalker walker = new ParseTreeWalker();
            final BuildComponentListener extractor =
                    new BuildComponentListener(parser, contract, this, this.templateKey);
            walker.walk(extractor, tree);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new GeneratorException(
                            "could not close manifest file "
                                    + this.templateManifestFileName);
                }
            }
        }
    }

    /**
     * The build template acquires a template group and a contract from which it
     * builds an output file. The output file has a name and a body both of
     * which are generated via a template.
     * 
     * @return
     * @throws GeneratorException
     */
    public void buildPartUsingTemplate(final Contract contract, final String eachName,
            final String templateFileName) throws GeneratorException {
        final Each forEach = Each.getValue(eachName);
        buildPartUsingTemplate(contract, forEach, templateFileName);
    }

    private ST initPartUsingTemplate(final STGroup stg, final Contract contract, final ST stFileName)
            throws GeneratorException {

        if (stFileName == null) {
            for (String templateName : stg.getTemplateNames()) {
                getLogger().error("template name {}", templateName);
            }
            throw new GeneratorException("no \"PATH\" template provided");
        }
        stFileName.add("delimiter", File.separatorChar);
        stFileName.add("directory", (this.isSkeleton ? this.skelOutputDir : this.baseOutputDir));
        stFileName.add("contract", contract);
        stFileName.add("isSkeleton", this.isSkeleton);
        return stFileName;
    }

    /**
     * generate the output file (part) using a string template.
     * 
     * @param contract
     * @param templateFileName
     * @throws GeneratorException
     */
    public void buildPartUsingTemplate(final Contract contract, final String templateFileName)
            throws GeneratorException {

        final STGroup stg = getGroup(getLogger(), templateFileName);
        final ST stFileName = stg.getInstanceOf("PATH");
        initPartUsingTemplate(stg, contract, stFileName);

        final ST stFileBody = stg.getInstanceOf("BODY");
        if (stFileBody == null) {
            throw new GeneratorException("no body template provided");
        }
        stFileBody.add("contract", contract);

        final String outputFileName = stFileName.render();
        logger.info("building {} using {} single", outputFileName, templateFileName);
        final File outputFile = new File(outputFileName);
        if (this.isSkeleton && outputFile.exists()) {
            return;
        }
        final File outputDir = outputFile.getParentFile();
        outputDir.mkdirs();

        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(outputFile));
            os.write(stFileBody.render().getBytes());
        } catch (FileNotFoundException ex) {
            throw new GeneratorException("problem writing output file", ex);
        } catch (IOException ex) {
            throw new GeneratorException("problem writing output file", ex);
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException ex) {
                    throw new GeneratorException("problem closing output file", ex);
                }
        }
        return;

    }

    public void buildPartUsingTemplate(final Contract contract, final Each forEach,
            final String templateFileName) throws GeneratorException {

        final STGroup stg = getGroup(getLogger(), templateFileName);
        final ST stFileName = stg.getInstanceOf("PATH");
        initPartUsingTemplate(stg, contract, stFileName);

        final ST stFileBody = stg.getInstanceOf("BODY");
        if (stFileBody == null) {
            throw new GeneratorException("no body template provided");
        }
        stFileBody.add("contract", contract);

        final List<?> all;
        switch (forEach) {
            case RELATION:
                all = contract.root.getRelations();
                break;
            case MESSAGE:
            default:
                throw new GeneratorException("unknown each type:" + forEach);
        }
        for (Object item : all) {
            stFileName.add("item", item);
            final String outputFileName = stFileName.render();
            logger.info("building {} using {} for a {}", outputFileName, templateFileName, forEach);
            final File outputFile = new File(outputFileName);
            if (this.isSkeleton && outputFile.exists()) {
                continue;
            }

            final File outputDir = outputFile.getParentFile();
            outputDir.mkdirs();

            BufferedOutputStream os = null;
            try {
                stFileBody.add("item", item);
                os = new BufferedOutputStream(new FileOutputStream(outputFile));
                os.write(stFileBody.render().getBytes());
            } catch (FileNotFoundException ex) {
                throw new GeneratorException("problem writing output file", ex);
            } catch (IOException ex) {
                throw new GeneratorException("problem writing output file", ex);
            } finally {
                if (os != null)
                    try {
                        os.close();
                    } catch (IOException ex) {
                        throw new GeneratorException("problem closing output file", ex);
                    }
            }
        }
    }

    public void buildPartUsingCopy(final Contract contract, final String inputFilePath,
            final String outputPathTemplate) throws GeneratorException {
        final STGroup stg = getGroup(getLogger(), templateFileName);
        final ST stFileName = new ST(outputPathTemplate);
        initPartUsingTemplate(stg, contract, stFileName);

        final String outputFileName = stFileName.render();
        logger.info("building {} using {} copy", outputFileName, inputFilePath);
        final File outputFile = new File(outputFileName);
        if (this.isSkeleton && outputFile.exists()) {
            return;
        }
        final File outputDir = outputFile.getParentFile();
        if (outputDir == null) {
            throw new GeneratorException("could not obtain output directory "
                    + outputFile);
        }
        outputDir.mkdirs();

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (this.templateJarName == null) {
                final File inputFile = new File(inputFilePath);
                if (!inputFile.isFile()) {
                    throw new GeneratorException("could not find template manifest "
                            + this.templateManifestFileName);
                }
                try {
                    inputStream = new FileInputStream(inputFile);
                } catch (FileNotFoundException e) {
                    throw new GeneratorException("could not open input file "
                            + inputFilePath);
                }
            } else {
                final Thread ct = Thread.currentThread();
                final ClassLoader pcl = ct.getContextClassLoader();
                inputStream = pcl.getResourceAsStream(inputFilePath);
            }
            try {
                outputStream = new FileOutputStream(outputFile);
            } catch (FileNotFoundException ex) {
                throw new GeneratorException("problem closing output stream", ex);
            }

            final ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
            final WritableByteChannel outputChannel = Channels.newChannel(outputStream);
            try {
                Generator.fastChannelCopy(inputChannel, outputChannel);
            } catch (IOException ex) {
                throw new GeneratorException("problem closing output stream", ex);
            }

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                throw new GeneratorException("problem closing input stream", ex);
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ex) {
                throw new GeneratorException("problem closing output stream", ex);
            }
        }

    }

    public void buildPartUsingCopy(final Contract contract, final Each forEach,
            final String inputFilePath,
            final String outputPathTemplate) throws GeneratorException {
        final STGroup stg = getGroup(getLogger(), templateFileName);
        final ST stFileName = new ST(outputPathTemplate);
        initPartUsingTemplate(stg, contract, stFileName);

        final List<?> all;
        switch (forEach) {
            case RELATION:
                all = contract.root.getRelations();
                break;
            case MESSAGE:
            default:
                throw new GeneratorException("unknown each type:" + forEach);
        }
        for (Object item : all) {
            stFileName.add("item", item);

            final String outputFileName = stFileName.render();
            logger.info("building {} using {} copy", outputFileName, inputFilePath);
            final File outputFile = new File(outputFileName);
            if (this.isSkeleton && outputFile.exists()) {
                continue;
            }
            final File outputDir = outputFile.getParentFile();
            if (outputDir == null) {
                throw new GeneratorException("could not obtain output directory "
                        + outputFile);
            }
            outputDir.mkdirs();

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                if (this.templateJarName == null) {
                    final File inputFile = new File(inputFilePath);
                    if (!inputFile.isFile()) {
                        throw new GeneratorException("could not find template manifest "
                                + this.templateManifestFileName);
                    }
                    try {
                        inputStream = new FileInputStream(inputFile);
                    } catch (FileNotFoundException e) {
                        throw new GeneratorException("could not open input file "
                                + inputFilePath);
                    }
                } else {
                    final Thread ct = Thread.currentThread();
                    final ClassLoader pcl = ct.getContextClassLoader();
                    inputStream = pcl.getResourceAsStream(inputFilePath);
                }
                try {
                    outputStream = new FileOutputStream(outputFile);
                } catch (FileNotFoundException ex) {
                    throw new GeneratorException("problem closing output stream", ex);
                }

                final ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
                final WritableByteChannel outputChannel = Channels.newChannel(outputStream);
                try {
                    Generator.fastChannelCopy(inputChannel, outputChannel);
                } catch (IOException ex) {
                    throw new GeneratorException("problem closing output stream", ex);
                }

            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException ex) {
                    throw new GeneratorException("problem closing input stream", ex);
                }
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException ex) {
                    throw new GeneratorException("problem closing output stream", ex);
                }
            }
        }

    }

    /**
     * Use NIO to copy input stream to output stream.
     * 
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest)
            throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (src.read(buffer) != -1) {
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }
}
