
package edu.vanderbilt.isis.druid.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

        final ST stFileBody = stg.getInstanceOf("BODY");
        if (stFileBody == null) {
            throw new GeneratorException("no body template provided");
        }

        final ST stFileName = stg.getInstanceOf("PATH");
        if (stFileName == null) {
            throw new GeneratorException("no path template provided");
        }
        stFileName.add("directory", contract.root.getSponsor().getPath(this.outputPath));
        stFileName.add("isSkeleton", this.isSkeleton);
        stFileName.add("name", contract.root.getName());
        final String outputFileName = stFileName.render();
        final File outputFile = new File(outputFileName);
            final File outputDir = outputFile.getParentFile();
            outputDir.mkdirs();

        BufferedOutputStream os = null;
        try {
            stFileBody.add("contract", contract);
            os = new BufferedOutputStream(new FileOutputStream(outputFile));
            os.write(stFileBody.render().getBytes());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
        return true;
    }
    
}
