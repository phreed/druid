
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
    static private final File expectedProviderDir = new File("src/test/resources/locator/src/main/java/com/walkernation/db/provider/");
    static private final File expectedLayoutDir = new File("src/test/resources/locator/res/layout/");
    
    static private final File actualDir = new File("target/test-harness/druid-maven-plugin-generate-test/");
    static private final File actualProviderDir = new File("target/test-harness/druid-maven-plugin-generate-test/transapps/pli/provider/");
    static private final File actualLayoutDir = new File("target/test-harness/druid-maven-plugin-generate-test/res/layout/");
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
        
        final String fileName = "AndroidManifest.xml";
        final File expected = new File(expectedDir, fileName);
        final File actual = new File(actualDir, fileName);
        assertFilesEqual("manifests differ", expected, actual); 
    }
    
     /**
     * @throws Exception if any
     */
    public void testContentDescriptor() throws Exception
    {
        final File pom = getTestFile(unitTestDirName, "content-descriptor-test/plugin-config.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        final DruidMojo myMojo = (DruidMojo) lookupMojo("generate", pom);
        assertNotNull("generate code using druid", myMojo);
        myMojo.execute();
        
       final File expected = new File(expectedProviderDir, "ContentDescriptor.java");
       final File actual = new File(actualProviderDir, "ContentDescriptor.java");
       assertFilesEqual("manifests differ", expected, actual); 
    }

    /**
     * @throws Exception if any
     */
    public void testUIListFragmentLayout() throws Exception
    {
        final File pom = getTestFile(unitTestDirName, "ui-list-fragment-layout-test/plugin-config.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        final DruidMojo myMojo = (DruidMojo) lookupMojo("generate", pom);
        assertNotNull("generate code using druid", myMojo);
        myMojo.execute();
        
        final File expected = new File(expectedLayoutDir, "location_listview.xml");
        final File actual = new File(actualLayoutDir, "locations_listview.xml");
        assertFilesEqual("manifests differ", expected, actual); 
    }
    
    /**
     * @throws Exception if any
     */
    public void testUIListActivity() throws Exception
    {
        final File pom = getTestFile(unitTestDirName, "ui-list-activity-test/plugin-config.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        final DruidMojo myMojo = (DruidMojo) lookupMojo("generate", pom);
        assertNotNull("generate code using druid", myMojo);
        myMojo.execute();
        
//         final File expected = new File(expectedLayoutDir, "location_listview.xml");
//         final File actual = new File(actualLayoutDir, "locations_listview.xml");
//         assertFilesEqual("manifests differ", expected, actual); 
    }
    
    public void testContractCreator() throws Exception
    {
        final File pom = getTestFile(unitTestDirName, "contract-creator-test/plugin-config.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        final DruidMojo myMojo = (DruidMojo) lookupMojo("generate", pom);
        assertNotNull("generate code using druid", myMojo);
        myMojo.execute();
        
        final String fileName = "src/main/java/transapps/pli/orm/LocationsCreator.java";
        final File expected = new File(expectedDir, fileName);
        final File actual = new File(actualDir, fileName);
        assertFilesEqual("manifests differ", expected, actual); 
    }
}
