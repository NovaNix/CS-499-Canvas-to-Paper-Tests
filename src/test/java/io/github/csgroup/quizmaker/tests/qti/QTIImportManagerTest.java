package io.github.csgroup.quizmaker.tests.qti;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.zip.ZipException;

import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.qti.importing.QTIZipManager;

/**
 * Test to verify that the QTI files are unzipped and extracted properly. <br>
 * - Ensures that the ZIP file exists <br>
 * - Checks that the temporary directory is created and contains the extracted files. <br>
 * - The temporary directories are properly deleted after the tests. <br>
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIImportManagerTest 
{
    
	private static final Logger logger = LoggerFactory.getLogger(QTIImportManagerTest.class);
	
	// path to the test QTI ZIP file
	private static final String TestZipPATH = "/qti_zip_file_test.zip"; 
    
	// store the path to the temporary directory
	private Path extractedTestFilePath;
	private QTIZipManager manager;
    
    
	/**
	* <b>Setup Method:</b> <br> 
	* - Checks the existence of the ZIP file <br>
	* - Extracts the ZIP file <br>
	* - Stores the path to the temporary directory for testing <br>
	*/
	@BeforeEach
	void setUp() throws IOException, ZipException, URISyntaxException 
	{
		manager = new QTIZipManager();
        
		URL resourceUrl = getClass().getResource(TestZipPATH);
		assertNotNull(resourceUrl, "ERROR!! Test ZIP File is MISSING");
        
		File testZip = new File(resourceUrl.toURI());
		System.out.println("Checking the TEST ZIP File at: " + testZip.getAbsolutePath());

		assertTrue(testZip.exists(), "ERROR!! Test ZIP file is missing! File expected at: " + testZip.getAbsolutePath());
		extractedTestFilePath = manager.extractQTIFile(testZip.getAbsolutePath());
	}
    
	/**
	* <b>Take-down Method:</b> <br>
	* - Deletes the temporary directory and the extracted files
	*/
	@AfterEach
	void takeDown() 
	{
		if (extractedTestFilePath != null) 
		{
			deleteTempDirectory(extractedTestFilePath.toFile());
		}
	}
    
	/**
	* <b>Test Method:</b> <br>
	* - Ensures that the path to the temporary directory is valid <br>
	* - Checks that the temporary directory exists <br>
	* - Ensures that the extracted files are in the temporary directory <br> 
	*/
	@Test
	void testZipExtraction() 
	{
		assertNotNull(extractedTestFilePath, "ERROR!! Temporary directory path does NOT exist.");
        
		File temporary_directory = extractedTestFilePath.toFile();
		assertTrue(temporary_directory.exists(), "ERROR!! Temporary directory does NOT exist.");
		assertTrue(temporary_directory.isDirectory(), "ERROR!! The extracted path should be a directory.");

		File[] extracted_Files = temporary_directory.listFiles();
        
		assertNotNull(extracted_Files, "ERROR!! No files were extracted.");
		assertTrue(extracted_Files.length > 0, "ERROR!! Temporary directory is EMPTY (contains zero files).");
        
		logger.info("List of Extracted Files:");
		for (File file : extracted_Files) 
		{
			logger.info(" - {}", file.getName());
		}
	}

	/**
	* Deletes the created temporary directory.
	* 
	* @param directory The directory to delete.
	*/
	private void deleteTempDirectory(File directory) 
	{
		if (directory.exists()) 
		{
			File[] files = directory.listFiles();
			if (files != null) 
			{
				for (File file : files) 
				{
					deleteTempDirectory(file);
				}
			}
			directory.delete();
		}
	}
}