package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.qti.importing.QTIZipManager;
import io.github.csgroup.quizmaker.qti.manifest.QTIManifestFileProcessor;
import io.github.csgroup.quizmaker.qti.manifest.QTIDataFileMapping;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test to verify that the manifest files are being parsed and the file mappings of the quiz assessment and quiz metadata are extracted properly. <br>
 * - Verifies that the QTIManifestFileProcessor accurately extracts the file paths of the quiz assessment and metadata files.  <br>
 * - Ensures that the number of extracted file paths matches the number that is expected. <br>
 * - Ensures that missing quiz assessment or quiz metadata files are handled properly.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIManifestFileProcessorTest 
{
	
	private static final Logger logger = LoggerFactory.getLogger(QTIManifestFileProcessorTest.class);
        
	private QTIZipManager zipManager;
	private QTIManifestFileProcessor manifestProcessor;
    private Path extractedDir;
	
    /**
    * <b>Setup Method:</b> <br> 
    * - Loads the QTI ZIP file <br>
    * - Copies the ZIP file to a temporary location for processing
	* 
	* @throws IOException if the test file cannot be created.
    */
    @BeforeEach
    public void setUp() throws IOException, URISyntaxException 
    {
		zipManager = new QTIZipManager();
        manifestProcessor = new QTIManifestFileProcessor();

        URL resourceUrl = getClass().getClassLoader().getResource("qti_zip_file_test.zip");
        assertNotNull(resourceUrl, "QTI ZIP file is missing");
		File zipFile = new File(resourceUrl.toURI());

        extractedDir = zipManager.extractQTIFile(zipFile.getAbsolutePath());
		assertTrue(Files.exists(extractedDir), "Extracted directory does not exist.");
		logger.info("Extracted test QTI package to: {}", extractedDir.toAbsolutePath());
    }

    /**
    * <b>Test Method:</b> <br>
    * - Extracts the quiz assessment and quiz metadata file paths <br>
    * - Matches the number of extracted file paths with the expected value <br>
	* - Ensures that the metadata files are properly identified
    */
    @Test
    public void testParseManifestFile() throws IOException, Exception
    {

        List<QTIDataFileMapping> mappings = manifestProcessor.processQTIFile(extractedDir);
		
		// Check for file mappings
		assertNotNull(mappings, "Returned mappings should NOT be null.");
		
		// Verify the expected number of extracted mappings
		assertEquals(3, mappings.size(), "Should extract 3 quiz assessments.");
		
		logger.info("The Extracted QTI Data File Mappings:");
		for (QTIDataFileMapping mapping : mappings)
		{
			logger.info("Assessment File: {}", mapping.getQuizAssessmentFile());
			logger.info("Metadata File: {}", mapping.hasMetadataFile() ? mapping.getQuizMetadataFile() : "NULL");
			
			if (mapping.hasMetadataFile())
			{
				assertNotNull(mapping.getQuizMetadataFile(), "Metadata file should NOT be null if 'hasMetadataFile() is true.");
			}
			else 
			{
				assertNull(mapping.getQuizMetadataFile(), "Metadata file should be null if 'hasMetadataFile() is false.");
			}	
		}
    }
}

