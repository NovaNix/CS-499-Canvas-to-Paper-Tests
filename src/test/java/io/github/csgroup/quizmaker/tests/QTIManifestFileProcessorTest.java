package io.github.csgroup.quizmaker.tests;

import io.github.csgroup.quizmaker.qti.QTIDataFileMapping;
import io.github.csgroup.quizmaker.qti.QTIManifestFileProcessor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * Test to verify that the manifest files are being parsed and the file mappings 
 * of the quiz assessment and quiz info are extracted properly. <br>
 * - Verifies that the QTIManifestFileProcessor accurately extracts the file 
 * paths of the quiz assessment and info files  <br>
 * - Ensures that the number of extracted file paths is what is expected
 * - Ensures that any missing quiz assessment or quiz info files are handled properly 
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIManifestFileProcessorTest 
{
        private QTIManifestFileProcessor manifestProcessor;
        private File testQtiZipFile;

        /**
        * <b>Setup Method:</b> <br> 
        * - Loads the QTI ZIP file <br>
        * - Copies the ZIP file to a temporary location for processing
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
        * - Extracts the quiz assessment and quiz info file paths<br>
        * - Matches the number of extracted file paths with the expected value
        */
        @Test
        public void testParseManifestFile() throws IOException
        {
                assertTrue(testQtiZipFile.exists(), "Test QTI ZIP file should exist.");

                // Processes the QTI ZIP file
                List<QTIDataFileMapping> mappings = manifestProcessor.processQTIFile(testQtiZipFile.getAbsolutePath());

                // Prints the extracted file mappings
                System.out.println("The Extracted QTI Data File Mappings:");
                for (QTIDataFileMapping mapping : mappings) 
                {
                        System.out.println("Assessment File: " + mapping.quizAssessmentFile);
                        System.out.println("Info File: " + (mapping.quizInfoFile != null ? mapping.quizInfoFile : "NULL"));
                }

                // Checks the expected number of extracted mappings
                assertEquals(3, mappings.size(), "Should extract 3 quiz assessments.");    
        }
}

