package io.github.csgroup.quizmaker.qti.importing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages the unzipping of the QTI files and stores the extracted files in a temporary directory. 
 * Returns the path to the temporary directory where the extracted files are stored. 
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIZipManager 
{
    
	private final Logger logger = LoggerFactory.getLogger(QTIZipManager.class);    

	/**
	* Extracts the QTI files into a temporary directory.
	*
	* @param zipFilePath The path to the QTI ZIP file.
	* @return The path to the temporary directory that contains the extracted files.
	*/
	public Path extractQTIFile(String zipFilePath) throws IOException, ZipException 
	{
		File zipFile = new File(zipFilePath);
		
		if (!zipFile.exists()) 
		{
			logger.error("ERROR!! ZIP file does not exist: {}", zipFilePath);
			throw new IOException("ZIP file not found: " + zipFilePath);
		}

		// Creates a temporary directory
		Path tempDirectory = Files.createTempDirectory("qti_temp_");
		String extractedFilePath = tempDirectory.toAbsolutePath().toString();
            
		logger.info("Extracting QTI ZIP file to: {}", extractedFilePath);

		// Extracts the contents of the QTI ZIP file
		unzipFile(zipFilePath, extractedFilePath);

		logger.info("QTI ZIP file extracted successfully to: {}", extractedFilePath);
		return tempDirectory;    // returns path to the temporary directory
	}
    
	/**
	 * Unzips the QTI Zip File.
	 * 
	 * @param zipFilePath The path to the QTI Zip file.
	 * @param extractedFilePath The path to the directory where the files will be extracted.
	 */
	private void unzipFile(String zipFilePath, String extractedFilePath) throws IOException, ZipException 
	{
		try (ZipFile zipFile = new ZipFile(zipFilePath))
		{
			zipFile.extractAll(extractedFilePath);
			
			logger.info("Unzipping success, extracted to: {}", extractedFilePath);
		}	
	}
}