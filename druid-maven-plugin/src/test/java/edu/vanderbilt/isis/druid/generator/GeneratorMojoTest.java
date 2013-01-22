
package edu.vanderbilt.isis.druid.generator;

import java.io.File;
import java.io.InputStream;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class GeneratorMojoTest extends AbstractMojoTestCase
{ 
    
    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        System.out.println("the file of interest: ");
        System.out.println( getPluginDescriptorLocation() );
        final String name = "/" + getPluginDescriptorLocation();
        getClass().getResource(name);
        final InputStream is = getClass().getResourceAsStream( "/" + getPluginDescriptorLocation() );
        super.setUp();
    }

    /** {@inheritDoc} */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * @throws Exception if any
     */
    public void testSomething() throws Exception
    {
        final File pom = getTestFile("src/test/resources/unit/druid-maven-plugin-generate-test/plugin-config.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        GeneratorMojo myMojo = (GeneratorMojo) lookupMojo("generate", pom);
        assertNotNull(myMojo);
        myMojo.execute();
    }
}
