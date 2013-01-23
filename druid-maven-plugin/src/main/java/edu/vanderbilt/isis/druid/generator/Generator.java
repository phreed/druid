
package edu.vanderbilt.isis.druid.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * The actual generator code.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class Generator {
    final private Logger logger;

    public Generator(final Logger logger) {
        this.logger = logger;
    }

    private File contractFile = new File("contract.xml");

    private File templateFile = null;
    private String templateKey;
    private File templateFileManifest;

    private File outputPath;
    private boolean isSkeleton;
    private Each each;

    public void setOutputDir(final File val) {
        this.outputPath = val;
    }

    public void setTemplateKey(String val) {
        this.templateKey = val;
    }

    public void setTemplateFile(final File val) {
        this.templateFile = val;
    }

    public void setTemplateFileManifest(final File val) {
        this.templateFileManifest = val;
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
        /** the messages */
        NONE("none");

        public final String val;

        private Each(String val) {
            this.val = val;
        }

        public static Each getValue(String val) throws GeneratorException {
            if (Each.RELATION.val.equals(val))
                return Each.RELATION;
            if (Each.MESSAGE.val.equals(val))
                return Each.MESSAGE;
            if (Each.NONE.val.equals(val))
                return Each.NONE;
            throw new GeneratorException("unknown each type" + val);
        }
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
    public STGroup getGroup() throws GeneratorException {
        STGroup.trackCreationEvents = true;

        /**
         * It may be that the template-file is given explicitly. If that is the
         * case then it should be used.
         */
        if (this.templateFile != null) {
            return getGroupUtil(logger, this.templateFile);
        }

        if (!this.templateFileManifest.isFile()) {
            logger.error("could not find template manifest {}", this.templateFileManifest);
            return null;
        }
        final STGroupFile manifestStg;
        try {
            manifestStg = new STGroupFile(this.templateFileManifest.getCanonicalPath(),
                    "US-ASCII", '<', '>');
        } catch (IOException ex) {
            logger.error("could not open template manifest file {}", this.templateFileManifest, ex);
            return null;
        }

        final ST stGroup = manifestStg.getInstanceOf("GROUP_PATH");
        stGroup.add("key", this.templateKey);
        final String templateGroupFileName = stGroup.render();
        final File templateGroupFile = new File(templateGroupFileName);
        return getGroupUtil(logger, templateGroupFile);
    }

    /**
     * A utility function for loading template group files.
     * 
     * @param logger
     * @param templateGroupFileName
     * @return null if not able to load the file
     */
    private static STGroup getGroupUtil(final Logger logger, final File templateGroupFileName) {
        if (!templateGroupFileName.isFile()) {
            logger.error("template file path is not found {}", templateGroupFileName);
            return null;
        }
        try {
            return new STGroupFile(templateGroupFileName.getCanonicalPath(),
                    "US-ASCII", '<', '>');
        } catch (IOException ex) {
            logger.error("could not open file {}", templateGroupFileName, ex);
            return null;
        }
    }

    /**
     * The build acquires a template group and a contract from which it builds
     * an output file. The output file has a name and a body both of which are
     * generated via a template.
     * 
     * @return
     * @throws GeneratorException
     */
    public boolean build() throws GeneratorException {
        final STGroup stg = getGroup();
        if (stg == null) {
            throw new GeneratorException("no group file loaded");
        }
        final Contract contract = Contract.newInstance(logger, this.contractFile);

        final ST stFileName = stg.getInstanceOf("PATH");
        if (stFileName == null) {
            throw new GeneratorException("no path template provided");
        }
        stFileName.add("delimiter", File.pathSeparator);
        stFileName.add("directory", this.outputPath);
        stFileName.add("contract", contract.root);
        stFileName.add("isSkeleton", this.isSkeleton);
        
        final ST stFileBody = stg.getInstanceOf("BODY");
        if (stFileBody == null) {
            throw new GeneratorException("no body template provided");
        }
        stFileBody.add("contract", contract);
        
        if (this.each.equals(Each.NONE)) {
            final String outputFileName = stFileName.render();
            final File outputFile = new File(outputFileName);
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
            return true;
        }
        final List<?> all;
        switch (this.each) {
            case RELATION:
                all = contract.root.getRelations();
                break;
            case MESSAGE:
            default:
                throw new GeneratorException("unknown each type:" + this.each);
        }
        for (Object item : all) {
            stFileName.add("item", item);
            final String outputFileName = stFileName.render();
            final File outputFile = new File(outputFileName);
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
        return true;
    }

}
