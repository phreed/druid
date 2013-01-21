
package edu.vanderbilt.isis.druid.generator;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class GeneratorMojoTest extends AbstractMojoTestCase
{
    /** {@inheritDoc} */
    protected void setUp() throws Exception {
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
        final File pom = getTestFile("src/test/resources/unit/project-to-test/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        GeneratorMojo myMojo = (GeneratorMojo) lookupMojo("generate", pom);
        assertNotNull(myMojo);
        myMojo.execute();
    }
}
