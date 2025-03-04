package io.github.csgroup.quizmaker.qti;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * This class parses the manifest file (imsmanifest.xml) and returns a structured Document object.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIManifestFileParser 
{
    
        private static final Logger logger = LoggerFactory.getLogger(QTIManifestFileParser.class);

        /**
        * Parses the manifest file and returns it as a structured Document object.
        *
        * @param manifestFile The manifest file.
        * @return A Document object that represents the XML structure.
        * @throws Exception if the imsmanifest.xml file cannot be read or parsed properly.
        */
        public Document parseManifestFile(File manifestFile) throws Exception
        {
	    logger.info("Parsing manifest file: {}", manifestFile.getAbsolutePath());
	    
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(manifestFile);
	    
	    // Normalize the Document to ensure a consistent, formatted structure   
            doc.getDocumentElement().normalize();
	    
	    logger.info("Successfully parsed manifest file: {}", manifestFile.getAbsolutePath());
	    return doc;
        }
}


