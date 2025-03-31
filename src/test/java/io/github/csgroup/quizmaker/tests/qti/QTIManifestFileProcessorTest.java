package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.qti.importing.QTIZipManager;
import io.github.csgroup.quizmaker.qti.manifest.QTIDataFileMapping;
import io.github.csgroup.quizmaker.qti.manifest.QTIManifestFileProcessor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        
	private QTIManifestFileProcessor manifestProcessor;
        private File testQtiZipFile;
	
        /**
        * <b>Setup Method:</b> <br> 
        * - Loads the QTI ZIP file <br>
        * - Copies the ZIP file to a temporary location for processing
	* 
	* @throws IOException if the test file cannot be created.
        */
        @BeforeEach
        public void setUp() throws IOException 
        {
                manifestProcessor = new QTIManifestFileProcessor();

                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("qti_zip_file_test.zip");
                assertNotNull(inputStream, "QTI ZIP file is missing");

                testQtiZipFile = File.createTempFile("sample_qti", ".zip");
                Files.copy(inputStream, testQtiZipFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
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
                assertTrue(testQtiZipFile.exists(), "Test QTI ZIP file should exist.");

                // Processes the QTI ZIP file
		QTIZipManager zipManager = new QTIZipManager();
		Path extractedPath = zipManager.extractQTIFile(testQtiZipFile.getAbsolutePath());
                List<QTIDataFileMapping> mappings = manifestProcessor.processQTIFile(testQtiZipFile.toPath());
		
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

