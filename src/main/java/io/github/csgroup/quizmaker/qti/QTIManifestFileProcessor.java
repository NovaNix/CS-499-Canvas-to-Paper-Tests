package io.github.csgroup.quizmaker.qti;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class extracts the file paths of the quiz assessment and quiz info from 
 * the parsed manifest file.
 * Returns a list of file mappings that link each assessment file to its 
 * corresponding info file
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIManifestFileProcessor 
{
    
        private static final Logger logger = LoggerFactory.getLogger(QTIManifestFileProcessor.class);
        private final QTIManifestFileParser manifestParser;
        private final QTIImportManager importManager;

        public QTIManifestFileProcessor() 
        {
                this.manifestParser = new QTIManifestFileParser();
                this.importManager = new QTIImportManager();
        }

        /**
        * Extracts the QTI file and processes the manifest file.
        *
        * @param qtiZipPath The path to the QTI ZIP file.
        * @return A list of QTIDataFileMapping objects that represent the assessment and quiz info file paths.
        * @throws IOException if the QTI file cannot be extracted.
        * @throws RuntimeException if the manifest file is missing.
        */
        public List<QTIDataFileMapping> processQTIFile(String qtiZipPath) throws IOException 
        {
                // Unzip the QTI file
                Path extractedQTIPackage = importManager.extractQTIFile(qtiZipPath);

                // Locate the manifest file
                File manifestFile = new File(extractedQTIPackage.toFile(), "imsmanifest.xml");

                if (!manifestFile.exists()) 
                {
                        throw new RuntimeException("ERROR!! imsmanifest.xml not found in extracted directory: " + extractedQTIPackage);
                }

                logger.info("Processing imsmanifest.xml from extracted directory: {}", extractedQTIPackage);
                Document doc = manifestParser.parseManifestFile(manifestFile);
                return extractFileMappings(doc);
        }

        /**
        * Extracts the quiz assessment and quiz info file paths from the parsed manifest document.
        *
        * @param doc The parsed Document object representing the manifest file.
        * @return A list of file mappings that link the assessment files to their corresponding quiz info files.
        */
        private List<QTIDataFileMapping> extractFileMappings(Document doc) 
        {
                NodeList resources = doc.getElementsByTagName("resource");
                List<QTIDataFileMapping> quizFiles = new ArrayList<>();

                for (int i = 0; i < resources.getLength(); i++) 
                {
                        Element resource = (Element) resources.item(i);
                        String type = resource.getAttribute("type");

                        // Identifies the assessment files
                        if ("imsqti_xmlv1p2".equals(type)) 
                        {
                                NodeList fileNodes = resource.getElementsByTagName("file");

                                if (fileNodes.getLength() == 0) 
                                {
                                        logger.warn("No <file> element found for resource '{}'. Skipping...", resource.getAttribute("identifier"));
                                        continue; 
                                }

                                String assessmentFilePath = ((Element) fileNodes.item(0)).getAttribute("href");
                                String infoFilePath = getInfoFilePath(resource, resources);

                                quizFiles.add(new QTIDataFileMapping(assessmentFilePath, infoFilePath));
                        }
                }

                return quizFiles;
        }

        /**
        * Retrieves the file path of the quiz info associated with a given quiz assessment file.
        *
        * @param assessmentResource The XML element representing the quiz assessment file.
        * @param resources The list of all the resources in the manifest file.
        * @return The quiz info file path if found, or null if there is not an info file.
        */
        private String getInfoFilePath(Element assessmentResource, NodeList resources) 
        {
                String assessmentIdentifier = assessmentResource.getAttribute("identifier");   
            
                if (assessmentIdentifier == null || assessmentIdentifier.isEmpty()) 
                {
                        logger.warn("Assessment resource has no identifier attribute.");
                        return null;
                }
            
                NodeList dependencies = assessmentResource.getElementsByTagName("dependency");

                if (dependencies.getLength() == 0) 
                {
                        logger.warn("No <dependency> found for assessment identifier '{}'", assessmentIdentifier);
                        return null;
                }
            
                String metadataIdentifierRef = ((Element) dependencies.item(0)).getAttribute("identifierref");

                if (metadataIdentifierRef == null || metadataIdentifierRef.isEmpty()) 
                {
                        logger.warn("Dependency for '{}' has no identifier", assessmentIdentifier);
                        return null;
                }
            
                for (int i = 0; i < resources.getLength(); i++) 
                {
                    Element resource = (Element) resources.item(i);
                    String resourceIdentifier = resource.getAttribute("identifier");

                    if (metadataIdentifierRef.equals(resourceIdentifier)) 
                    {
                        NodeList fileNodes = resource.getElementsByTagName("file");

                        if (fileNodes.getLength() > 0) 
                        {
                            String infoPath = ((Element) fileNodes.item(0)).getAttribute("href");
                            logger.info("Found info file: {}", infoPath);
                            return infoPath;
                        }
                    }
                }

                logger.warn("No corresponding quiz info file found for assessment identifier '{}'", assessmentIdentifier);
                return null;
        }
}



