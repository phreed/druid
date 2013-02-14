
package edu.vanderbilt.isis.druid.parser;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;

import edu.vanderbilt.isis.druid.generator.Contract;
import edu.vanderbilt.isis.druid.generator.Generator;
import edu.vanderbilt.isis.druid.generator.GeneratorException;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.BuildComponentContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.MultiTemplateContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.PartConstructionRuleContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.SimpleCopyContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.SimpleTemplateContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.SubsetContext;

/**
 * Parsing the component manifest file and building the parts of the named
 * component.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class BuildComponentListener extends ComponentManifestBaseListener {
    @SuppressWarnings("unused")
    final private ComponentManifestParser parser;
    final private Logger logger;
    final private Generator generator;
    final private Contract contract;
    final private String componentName;
    private boolean isComponentActive = false;
    private String subsetName = null;

    private String templateFileName = null;

    public BuildComponentListener(final ComponentManifestParser parser, final Contract contract,
            final Generator generator, final String componentName) {
        this.logger = generator.getLogger();
        this.parser = parser;
        this.contract = contract;
        this.generator = generator;
        this.componentName = componentName;
    }

    @Override
    public void exitSubset(SubsetContext ctx) {
        this.subsetName = ctx.getText();
    }

    @Override
    public void enterBuildComponent(BuildComponentContext ctx) {
        final String currentComponentName = ctx.ID().getText();
        this.isComponentActive = this.componentName.equalsIgnoreCase(currentComponentName);
    }

    @Override
    public void exitBuildComponent(BuildComponentContext ctx) {
        this.isComponentActive = false;
    }

    @Override
    public void enterSimpleTemplate(SimpleTemplateContext ctx) {
        final TerminalNode templateFileNode = ctx.FILE_PATH();
        final String full = templateFileNode.getText();
        this.templateFileName = full.replaceAll("^\"|\"$", "");
    }

    @Override
    public void exitSimpleTemplate(SimpleTemplateContext ctx) {
        if (!this.isComponentActive) {
            return;
        }
        logger.info("simple context tree = {}", ctx.toStringTree());
        if (this.templateFileName == null) {
            return;
        }
        try {

            this.generator.buildPartUsingTemplate(this.contract, this.templateFileName);
        } catch (GeneratorException ex) {
            this.generator.getLogger().error("could not procees template {}",
                    this.templateFileName, ex);
        }
        return;
    }

    @Override
    public void exitFileOfSkeletonType(ComponentManifestParser.FileOfSkeletonTypeContext ctx) {
        this.generator.setSkeleton(true);
    }

    @Override
    public void enterPartConstructionRule(PartConstructionRuleContext ctx) {
        this.generator.setSkeleton(false);
    }

    @Override
    public void enterMultiTemplate(MultiTemplateContext ctx) {
        final TerminalNode templateFileNode = ctx.FILE_PATH();
        final String full = templateFileNode.getText();
        this.templateFileName = full.replaceAll("^\"|\"$", "");
    }

    @Override
    public void exitMultiTemplate(MultiTemplateContext ctx) {
        if (!this.isComponentActive) {
            return;
        }

        if (this.templateFileName == null) {
            return;
        }
        try {
            logger.info("multi context tree = [{}] subset = {}", ctx.toStringTree(),
                    this.subsetName);
            this.generator.buildPartUsingTemplate(this.contract, this.subsetName,
                    this.templateFileName);
        } catch (GeneratorException ex) {
            this.generator.getLogger().error("could not procees template {}",
                    this.templateFileName, ex);
        }
        return;
    }

    @Override
    public void exitSimpleCopy(SimpleCopyContext ctx) {
        if (!this.isComponentActive) {
            return;
        }
        final String filePath = ctx.FILE_PATH().getText().replaceAll("^\"|\"$", "");
        final String template = ctx.TEMPLATE().getText().replaceAll("^'|'$", "");

        logger.info("simple copy tree = {}", ctx.toStringTree());
        try {
            this.generator.buildPartUsingCopy(this.contract, filePath, template);
        } catch (GeneratorException ex) {
            this.generator.getLogger().error("could not procees file {} with template {}",
                    filePath, template, ex);
        }
        return;
    }

}
