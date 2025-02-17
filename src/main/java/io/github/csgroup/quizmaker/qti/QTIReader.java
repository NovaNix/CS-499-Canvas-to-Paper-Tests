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
        private final QTIManifestFileProcessor manifestProcessor;
	
	public QTIReader()
	{
                this.manifestProcessor = new QTIManifestFileProcessor();	
	}
	
        /**
         * Reads the QTI file, extracts the manifest file, and processes the quiz content.
         * 
         * @param qtiZipPath The path to the QTI Zip file.
         * @return A QTIContents object containing the quiz assessments and info.
         * @throws IOException if the QTI file cannot be extracted.
         */
	public QTIContents readQTIFile(String qtiZipPath) throws IOException
	{
                logger.info("Reading QTI file from path: {}", qtiZipPath);
                
                // Process the QTI ZIP file and extract the file mappings
                List<QTIDataFileMapping> mappings = manifestProcessor.processQTIFile(qtiZipPath);
                
                // Store the extracted data into a QTIContents object
                QTIContents qtiContents = new QTIContents();
                for (QTIDataFileMapping mapping : mappings)
                {
                        qtiContents.addQuiz(mapping);
                }
		
                logger.info("Successfully extracted {} quizzes from QTI file.", mappings.size());
		return qtiContents;
	}
	
}
