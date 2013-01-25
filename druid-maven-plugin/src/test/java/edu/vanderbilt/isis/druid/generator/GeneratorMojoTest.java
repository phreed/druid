
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

    static final private String unitTestDirName = "src/test/resources/unit/druid-maven-plugin-generate-test";
    /**
     * @throws Exception if any
     */
    public void testAndroidManifest() throws Exception
    {
        final File pom = getTestFile(unitTestDirName, "android-manifest-test/plugin-config.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        final DruidMojo myMojo = (DruidMojo) lookupMojo("generate", pom);
        assertNotNull("generate code using druid", myMojo);
        myMojo.execute();
    }
    
    public void testContractCreator() throws Exception
    {
        final File pom = getTestFile(unitTestDirName, "contract-creator-test/plugin-config.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        final DruidMojo myMojo = (DruidMojo) lookupMojo("generate", pom);
        assertNotNull("generate code using druid", myMojo);
        myMojo.execute();
    }
}
