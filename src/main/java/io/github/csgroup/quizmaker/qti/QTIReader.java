package io.github.csgroup.quizmaker.qti;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An object responsible for reading QTI files and extracting the information contained inside
 * 
 * @author 
 */
public class QTIReader 
{
    
	public static final Logger logger = LoggerFactory.getLogger(QTIReader.class);
        private final QTIImportManager importManager;
	private final QTIManifestFileProcessor manifestProcessor;
	
	public QTIReader()
	{
                this.importManager = new QTIImportManager();
		this.manifestProcessor = new QTIManifestFileProcessor();	
	}
	
	/**
	* Reads the QTI files, extracts the data files, and retrieves the file mappings of the quiz assessment and metadata files
	* 
	* @param qtiZipPath The path to the QTI ZIP file.
	* @return A list of QTIDataFileMapping objects that represents the extracted file paths.
	* @throws IOException if the QTI file cannot be properly extracted.
	*/
	public List<QTIDataFileMapping> readFile(String qtiZipPath) throws IOException, Exception
	{
		logger.info("Reading QTI file from path: {}", qtiZipPath);
		
		// Extract the files from the QTI Zip file
		Path extractedQTIPackage = importManager.extractQTIFile(qtiZipPath);
		if (extractedQTIPackage == null)
		{
			logger.error("Failed to extract QTI file.");
			return null;
		}
		
		// Process the manifest file
		List<QTIDataFileMapping> mappings = manifestProcessor.processQTIFile(extractedQTIPackage.toString());
		if (mappings == null || mappings.isEmpty())
		{
			logger.warn("No quizzes found in the QTI file.");
			return null;
		}
		
		// Return extracted file paths
		return mappings;
	}	
}
