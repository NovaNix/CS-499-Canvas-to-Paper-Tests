package io.github.csgroup.quizmaker.qti;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * This class parses the manifest file (imsmanifest.xml).
 * Returns a parsed Document object that can be further processed 
 * to extract the file paths of the quiz assessment and quiz info.
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
        * @throws RuntimeException if the XML file cannot be read or parsed correctly.
        */
        public Document parseManifestFile(File manifestFile) 
        {
                try 
                {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(manifestFile);

                        // Normalize the document to prevent formatting inconsistencies
                        doc.getDocumentElement().normalize();

                        return doc;   
                } 
                catch (Exception e) 
                {
                        throw new RuntimeException("ERROR!! Failed to parse imsmanifest.xml: " + manifestFile.getAbsolutePath(), e);
                }
        }
}


