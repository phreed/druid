
package edu.vanderbilt.isis.druid.generator;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.util.FileUtils;

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

    private void assertFilesEqual(final String msg, final File expected, final File actual) {
        try {
            assertEquals(msg, FileUtils.fileRead(expected), FileUtils.fileRead(actual));
        } catch (IOException ex) {
            fail("file is missing " + ex.getLocalizedMessage());
        }
    }

    static final private String unitTestDirName = "src/test/resources/unit/druid-maven-plugin-generate-test";
    static private final File expectedDir = new File("src/test/resources/locator/");
    static private final File actualDir = new File("target/test-harness/druid-maven-plugin-generate-test/");

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
        
        final File expected = new File(expectedDir, "AndroidManifest.xml");
        final File actual = new File(actualDir, "AndroidManifest.xml");
        assertFilesEqual("manifests differ", expected, actual); 
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
