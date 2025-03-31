package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.qti.model.AssessmentMetadata;
import io.github.csgroup.quizmaker.qti.parsing.AssessmentMetadataParser;
import java.io.File;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test to verify that the AssessmentMetadataParser correctly parses the metadata elements from the <code>assessment_meta.xml</code> file. <br>
 * - Ensures the assessment metadata file is loaded in properly <br>
 * - Ensures the parsed object is not null <br>
 * - Ensures fields such as title, identifier, points possible, due date, and description are extracted properly <br>
 * 
 * @author Sarah Singhirunnusorn
 */
public class AssessmentMetadataParserTest 
{
    
	private static final Logger logger = LoggerFactory.getLogger(AssessmentMetadataParserTest.class); 
	private AssessmentMetadataParser parser;

	/**
	* <b>Setup Method:</b><br>
	* - Initializes the AssessmentMetadataParser before each test run
	*/
	@BeforeEach
	public void setUp() 
	{
		parser = new AssessmentMetadataParser();
		logger.info("Parser initialized.");
	}

	/**
	* <b>Test Method:</b><br> 
	* - Loads the <code>assessment_meta.xml</code> file <br>
	* - Verifies that the file exists <br>
	* - Parses the file and verifies the metadata fields <br>
	* - Ensures none of the extracted fields are null 
	*
	* @throws Exception if the test file cannot be loaded in or parsed
	*/
	@Test
	public void testParseAssessmentMetadata() throws Exception 
	{
		// Load assessment_meta.xml from test resources
		URL resource = getClass().getClassLoader().getResource("assessment_meta.xml");
		assertNotNull(resource, "ERROR!! assessment_meta.xml file not found in test resources.");
		logger.info("Loaded assessment_meta.xml from: {}", resource.getPath());

		File metadataFile = new File(resource.toURI());
		assertTrue(metadataFile.exists(), "ERROR!! assessment_meta.xml file does not exist.");
		logger.info("assessment_meta.xml exists at path: {}", metadataFile.getAbsolutePath());

		// Parse metadata file
		AssessmentMetadata metadata = parser.parse(metadataFile);
		assertNotNull(metadata, "ERROR!! Parsed metadata should not be null.");
		logger.info("Parsed metadata object created successfully.");

		// Print and assert parsed values
		logger.info("Parsed Assessment Metadata:");
		logger.info(" - Title: " + metadata.getTitle());
		logger.info(" - Identifier: " + metadata.getIdentifier());
		logger.info(" - Points Possible: " + metadata.getPointsPossible());
		logger.info(" - Due Date: " + metadata.getDueDate());
		logger.info(" - Description: " + metadata.getQuizDescription());

		assertNotNull(metadata.getTitle(), "ERROR!! Title should not be null.");
		assertNotNull(metadata.getIdentifier(), "ERROR!! Identifier should not be null.");
		assertNotNull(metadata.getPointsPossible(), "ERROR!! Points Possible should not be null.");
		assertNotNull(metadata.getDueDate(), "ERROR!! Due Date should not be null.");
		assertNotNull(metadata.getQuizDescription(), "ERROR!! Description should not be null.");
	}   
}
