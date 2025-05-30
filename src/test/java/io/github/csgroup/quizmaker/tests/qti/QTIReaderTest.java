package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.QTIContents;
import io.github.csgroup.quizmaker.qti.QTIReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test to verify that the QTIReader class correctly extracts and processes QTI files. <br>
 * - Ensures that QTIReader extracts the QTI ZIP file.<br>
 * - Ensures that QTIReader processes the manifest and extracts file mappings.<br>
 * - Verifies that the expected quiz assessments and metadata files exist.<br>
 * - Checks proper handling of missing or invalid files.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIReaderTest 
{

	private static final Logger logger = LoggerFactory.getLogger(QTIReaderTest.class);

	private QTIReader qtiReader;
	private File testQtiZipFile;
	private Path tempDirectory;

	/**
	* <b>Setup Method:</b><br>
	* - Loads the test QTI ZIP file <br>
	* - Verifies the test QTI ZIP file
	*/
	@BeforeEach
	public void setUp() throws IOException 
	{
		logger.info("Setting up QTIReaderTest...");

		qtiReader = new QTIReader();

		// Load test QTI ZIP file
		URL resourceUrl = getClass().getClassLoader().getResource("group-3-project-quiz-export.zip");
		assertNotNull(resourceUrl, "ERROR!! QTI ZIP file is missing.");

		testQtiZipFile = new File(resourceUrl.getPath());
		assertTrue(testQtiZipFile.exists(), "ERROR!! Test QTI ZIP file should exist.");

		logger.info("Test QTI ZIP file located at: {}", testQtiZipFile.getAbsolutePath());
	}

	/**
	* <b>Test:</b><br>
	* - Ensures QTIReader extracts and processes the QTI file correctly
	*/
	@Test
	public void testReadQTIFile() throws Exception 
	{
		logger.info("Running QTIReader Test: Reading and Parsing QTI File");

		QTIContents qtiContents = qtiReader.readFile(testQtiZipFile.getAbsolutePath());
		assertNotNull(qtiContents, "ERROR!! QTIContents should not be null.");
		assertTrue(qtiContents.quizzes.size() > 0, "ERROR!! No quizzes were extracted from the QTI file.");

		logger.info("QTIContents successfully created with " + qtiContents.quizzes.size() + " quiz(zes).");

		for (Quiz quiz : qtiContents.quizzes)
		{
			logger.info("Parsed Quiz:");
			logger.info(" - ID: ", quiz.getId());
			logger.info(" - Title: ", quiz.getTitle());
			logger.info(" - Description: ", quiz.getDescription().asText());
		}
	}

	/**
	* <b>Test:</b><br>
	* - Ensures that the QTIReader properly handles a missing or invalid QTI file
	 */
	@Test
	public void testMissingQTIFile() 
	{
		logger.info("Testing QTIReader: Missing QTI File");

		Exception exception = assertThrows(IOException.class, () -> {qtiReader.readFile("non_existent_file.zip");});
		assertTrue(exception.getMessage().contains("ZIP file not found"), "ERROR!! Expected IOException for missing QTI file.");

		logger.info("QTIReader correctly handled missing QTI file.");
	}

	/**
	* <b>Cleanup Method:</b><br>
	* - Deletes the temporary test directory after each test
	*/
	@AfterEach
	public void tearDown() throws IOException 
	{
		if (tempDirectory != null && Files.exists(tempDirectory)) 
		{
			Files.walk(tempDirectory)
				.sorted((p1, p2) -> -p1.compareTo(p2)) 
				.map(Path::toFile)
				.forEach(File::delete);
			logger.info("Cleaned up temporary test directory.");
		}
    }
}




















