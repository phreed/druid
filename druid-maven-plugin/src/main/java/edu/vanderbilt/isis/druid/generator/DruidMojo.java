package edu.vanderbilt.isis.druid.generator;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;


/**
 * Goal which generates code for Ammo enabled applications.
 * 
 * @goal generate
 * 
 * @phase generate-sources
 */
public class DruidMojo extends AbstractMojo {
    /**
     * The entry point to Aether
     * @component
     */
    private RepositorySystem repoSystem;

    /**
     * The current repository/network configuration of Maven.
     *
     * @parameter default-value="${repositorySystemSession}"
     * @readonly
     */
    private RepositorySystemSession repoSession;

    /**
     * The project's remote repositories to use for the resolution of plugins and their dependencies.
     *
     * @parameter default-value="${project.remotePluginRepositories}"
     * @readonly
     */
    private List<RemoteRepository> remoteRepos;
    
	/**
     * The {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>} of the artifact to resolve.
     * 
     * @parameter expression="${aether.artifactCoords}"
     */
     private String artifactCoords;
	
	
	/**
	 * contractFile : what is the file expressing the data contract.
	 * <p>
	 * This file provides the file name and its path.
	 * 
	 * @parameter expression="${generate.contractFile}"
	 *            default-value="contract.xml""
	 */
	private File contractFile = new File("contract.xml");


	/**
	 * templateFile : the file containing the string-templates to be used
	 * to generate the output.
	 * <dl>
	 * <dt>topBody<dt><dd>the template for generating the content of the file</dd>
	 * <dt>topPath<dt><dd>the template for generating the path to the file</dd>
	 * </dl>
	 * This is optional, if not specified the templateFile is generated
	 * using the template-group found in the templateLookupFile.  
	 * 
	 * @parameter expression="${generate.template}" default-value=""
	 */
	private File templateFile = null;
	
	 /**
     * templateKey : the name of template to be used to generate the output.
     * <p>
     * This is used to select the group template file.
     * 
     * @parameter expression="${generate.template.key}"
     *            default-value="content-descriptor"
     */
    private String templateKey = "content-descriptor";

    /**
    * templateFileManifest : the name of template to be used to generate the output.
    * <p>
    * This is used to select the group template file.
    * 
    * @parameter expression="${generate.template.filemap}"
    *            default-value="template-manifest.stg"
    */
    private File templateFileManifest = new File("template-manifest.stg");
	
	/**
	 * skeleton : has the skeleton target been requested?
	 * <p>
	 * The skeleton is a file which is expected to be modified by the developer.
	 * An example would be a class file which inherits from a corresponding
	 * base. The file name will be different from the base.
	 * 
	 * @parameter expression="${generate.skeleton}" default-value="false"
	 */
	private boolean skeleton = false;

	/**
	 * each : generate a separate file for "each" of these elements.
	 * <p>
	 * <dl>
	 * <dt>relation</dt><dd>one for each of the tables/relations mentioned</dd>
	 * <dt>message</dt><dd>one for each of the messages mentioned</dd>
	 * </dl>
	 * 
	 * @parameter expression="${generate.skeleton}" default-value="none"
	 */
	private String each = Generator.Each.NONE.val;

	/**
	 * outputDirectory : the base path for the generated files.
	 * <p>
	 * This indicates where the generated files should be placed.
	 * 
	 * @parameter expression="${generate.outputDirectory}"
	 *            default-value="${basedir}/target/generated-sources/druid"
	 */
	private File outputDirectory = new File("gen");

	/**
	 * Satisfy the maven execution request.
	 * 
	 */
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log logger = getLog();
       /*
        final Artifact artifact;
        try
        {
            artifact = new DefaultArtifact( artifactCoords );
        }
        catch ( IllegalArgumentException e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }

        final ArtifactRequest request = new ArtifactRequest();
        request.setArtifact( artifact );
        request.setRepositories( remoteRepos );

        getLog().info( "Resolving artifact " + artifact + " from " + remoteRepos );

        final ArtifactResult result;
        try
        {
            result = repoSystem.resolveArtifact( repoSession, request );
        }
        catch ( ArtifactResolutionException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }

        logger.info( "Resolved artifact " + artifact + " to " + result.getArtifact().getFile() + " from "
                           + result.getRepository() );
        */
       
		final Generator generator = new Generator(new MavenLoggerImpl(
				this.getLog(), "druid"));

        generator.setTemplateFile(this.templateFile);
        
		generator.setTemplateKey(this.templateKey);
		generator.setTemplateFileManifest(this.templateFileManifest);

		generator.setContractPath(this.contractFile);
		generator.setOutputDir(this.outputDirectory);
		try {
            generator.setEach(this.each);
        } catch (GeneratorException ex) {
            throw new MojoExecutionException("unknown each", ex);
        }

		generator.setSkeleton(this.skeleton);

		try {
			generator.build();
		} catch (GeneratorException ex) {
			logger.error("build failed", ex);
			throw new MojoExecutionException("problem running code generator", ex);
		}
		return;
	}

}
