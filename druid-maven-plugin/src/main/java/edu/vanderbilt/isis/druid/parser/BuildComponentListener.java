
package edu.vanderbilt.isis.druid.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.maven.plugin.logging.Log;
import org.slf4j.Logger;
import org.stringtemplate.v4.STGroup;

import edu.vanderbilt.isis.druid.generator.Contract;
import edu.vanderbilt.isis.druid.generator.Generator;
import edu.vanderbilt.isis.druid.generator.GeneratorException;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.BuildComponentContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.MultiTemplateContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.PartConstructionRuleContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.PartsListContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.ProgContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.SimpleCopyContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.SimpleTemplateContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.StatementContext;
import edu.vanderbilt.isis.druid.parser.ComponentManifestParser.SubsetContext;

/**
 * The actual generator code.
 * 
 * @see GenerateMojo.java for a description of the fields.
 */
public class BuildComponentListener extends ComponentManifestBaseListener {
    final private ComponentManifestParser parser;
    final private Generator generator;
    final private Contract contract;
    final private Logger logger;

    private String templateFileName = null;

    public BuildComponentListener(final ComponentManifestParser parser, final Contract contract,
            final Generator generator) {
        this.logger = generator.getLogger();
        this.parser = parser;
        this.contract = contract;
        this.generator = generator;
    }

    @Override
    public void enterEveryRule(ParserRuleContext arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitEveryRule(ParserRuleContext arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitErrorNode(ErrorNode arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitTerminal(TerminalNode arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterSubset(SubsetContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitSubset(SubsetContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterBuildComponent(BuildComponentContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitBuildComponent(BuildComponentContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterStatement(StatementContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitStatement(StatementContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterSimpleTemplate(SimpleTemplateContext ctx) {
        final TerminalNode templateFileNode = ctx.FILE_PATH();
        final String full = templateFileNode.getText();
        this.templateFileName = full.replaceAll("^\"|\"$", "");
    }

    @Override
    public void exitSimpleTemplate(SimpleTemplateContext ctx) {
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
    public void enterPartConstructionRule(PartConstructionRuleContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitPartConstructionRule(PartConstructionRuleContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterProg(ProgContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitProg(ProgContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterPartsList(PartsListContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitPartsList(PartsListContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterMultiTemplate(MultiTemplateContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitMultiTemplate(MultiTemplateContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void enterSimpleCopy(SimpleCopyContext ctx) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitSimpleCopy(SimpleCopyContext ctx) {
        // TODO Auto-generated method stub

    }

}
