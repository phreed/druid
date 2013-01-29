
package edu.vanderbilt.isis.druid.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.slf4j.Logger;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import edu.vanderbilt.isis.druid.parser.ContractXmlParser;

/**
 * The actual generator code.
 * 
 * @see DruidMojo for a description of the fields.
 */
public class Generator {
    final private Logger logger;

    public Generator(final Logger logger) {
        this.logger = logger;
    }

    private File contractFile = new File("contract.xml");

    private String templateJarName = null;
    private String templateFileName = null;
    private String templateKey;
    private String templateManifestFileName;

    private File outputPath;
    private boolean isSkeleton;
    private Each each;

    public void setOutputDir(final File val) {
        this.outputPath = val;
    }

    public void setTemplateJarName(String templateJarName) throws GeneratorException {
        this.templateJarName = templateJarName;
        final Thread ct = Thread.currentThread();
        final ClassLoader pcl = ct.getContextClassLoader();
        URL[] nurl;
        try {
            nurl = new URL[]{ new URL("file://"+templateJarName) };
        } catch (MalformedURLException ex) {
            throw new GeneratorException("could not load template jar", ex);
        }
        final URLClassLoader ucl = new URLClassLoader(nurl, pcl);
        ct.setContextClassLoader(ucl);
    }

    public void setTemplateKey(String val) {
        this.templateKey = val;
    }

    public void setTemplateFileName(final String val) {
        this.templateFileName = val;
    }

    public void setTemplateFileManifestName(final String val) {
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
    public STGroup getTemplateGroup() throws GeneratorException {
        STGroup.trackCreationEvents = true;

        /**
         * It may be that the template-file is given explicitly. If that is the
         * case then it should be used.
         */
        if (this.templateFileName != null) {
            return getGroupUtil(logger, this.templateFileName);
        }

        final File templateManifestFile = new File(this.templateManifestFileName);
        if (!templateManifestFile.isFile()) {
            throw new GeneratorException("could not find template manifest "
                    + this.templateManifestFileName);
        }
        final STGroup manifestStg = getManifestGroup();
        final ST stGroup = manifestStg.getInstanceOf("GROUP_PATH");
        stGroup.add("key", this.templateKey);
        final String templateGroupFileName = stGroup.render();
        return getGroupUtil(logger, templateGroupFileName);
    }

    /**
     * A utility function for loading template group files.
     * 
     * @param logger
     * @param templateFileName
     * @return null if not able to load the file
     * @throws GeneratorException
     */
    private STGroup getGroupUtil(final Logger logger, final String templateFileName)
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

    private STGroup getManifestGroup() throws GeneratorException {
        if (this.templateJarName == null) {
            final File templateManifestFile = new File(templateManifestFileName);
            if (!templateManifestFile.isFile()) {
                throw new GeneratorException("template file path is not found "
                        + templateManifestFile);
            }
            return new STGroupFile(this.templateManifestFileName, "US-ASCII", '<', '>');
        }
        final String urlName = new StringBuilder()
                // .append("jar:file:").append(templateJar.getAbsolutePath()).append("!")
                .append(templateManifestFileName).toString();
        final URL url;
        try {
            url = new URL(urlName);
        } catch (MalformedURLException ex) {
            throw new GeneratorException("bad manifest url", ex);
        }
        final STGroup stg = new STGroupFile(url, "US-ASCII", '<', '>');
        logger.debug("default template group");
        return stg;
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
        final STGroup stg = getTemplateGroup();
        final Contract contract = ContractXmlParser.parseXmlFile(logger, this.contractFile);

        final ST stFileName = stg.getInstanceOf("PATH");
        if (stFileName == null) {
            for (String templateName : stg.getTemplateNames()) {
                logger.error("template name {}", templateName);
            }
            throw new GeneratorException("no \"PATH\" template provided");
        }
        stFileName.add("delimiter", File.separatorChar);
        stFileName.add("directory", this.outputPath);
        stFileName.add("contract", contract);
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
