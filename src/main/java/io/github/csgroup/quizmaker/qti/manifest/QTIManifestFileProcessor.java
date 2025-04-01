package io.github.csgroup.quizmaker.qti.manifest;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class extracts the file paths of the quiz assessment and quiz metadata from the parsed manifest file.
 * Returns a list of file mappings that link each assessment file to its corresponding metadata file
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIManifestFileProcessor 
{

	private static final Logger logger = LoggerFactory.getLogger(QTIManifestFileProcessor.class);
	private final QTIManifestFileParser manifestParser;

	public QTIManifestFileProcessor() 
	{
		this.manifestParser = new QTIManifestFileParser();
	}

	/**
	 * Processes the manifest <code>imsmanifest.xml</code> file.
	 *
	 * @param extractedQTIPackage The path to the QTI ZIP file.
	 * @return A list of QTIDataFileMapping objects that represent the quiz assessment and metadata file paths.
	 * @throws Exception if the QTI file cannot be extracted or processed.
	 * @throws RuntimeException if the manifest file is missing.
	 */
	public List<QTIDataFileMapping> processQTIFile(Path extractedQTIPackage) throws Exception 
	{
		// Unzip and extract the QTI file
		if(extractedQTIPackage == null)
		{
			logger.error("Extraction failed. No valid directoy found.");
			return new ArrayList<>();
		}
		logger.info("Extracted QTI files to: {}", extractedQTIPackage.toAbsolutePath());

		// Locate the manifest file
		File manifestFile = new File(extractedQTIPackage.toFile(), "imsmanifest.xml");
		if (!manifestFile.exists()) 
		{
			throw new RuntimeException("ERROR!! imsmanifest.xml not found in the temporary directory: " + extractedQTIPackage);
		}
		logger.info("Processing imsmanifest.xml from the temporary directory: {}", extractedQTIPackage);

		// Check for null manifest parsing
		Document doc = manifestParser.parseManifestFile(manifestFile);
		if (doc == null)
		{
			logger.error("Manifest file parsing failed. Document is null.");
			return new ArrayList<>();
		}

		return extractFileMappings(doc);
	}

	/**
	 * Extracts the quiz assessment and quiz metadata file paths from the parsed manifest document.
	 *
	 * @param doc The parsed Document object representing the manifest file.
	 * @return A list of file mappings that link the assessment files to their corresponding metadata files.
	 */
	private List<QTIDataFileMapping> extractFileMappings(Document doc) 
	{
		NodeList resources = doc.getElementsByTagName("resource");
		List<QTIDataFileMapping> quizFiles = new ArrayList<>();

		for (int i = 0; i < resources.getLength(); i++) 
		{
			Element resource = (Element) resources.item(i);
			String type = resource.getAttribute("type");

			// Identify the quiz assessment files
			if ("imsqti_xmlv1p2".equals(type)) 
			{
				NodeList fileNodes = resource.getElementsByTagName("file");

				if (fileNodes.getLength() == 0) 
				{
					logger.warn("No <file> element found for resource '{}'. Skipping...", resource.getAttribute("identifier"));
					continue; 
				}

				String assessmentFilePath = ((Element) fileNodes.item(0)).getAttribute("href");
				String metadataFilePath = getMetadataFilePath(resource, resources);

				QTIDataFileMapping mapping = new QTIDataFileMapping(assessmentFilePath, metadataFilePath);
				quizFiles.add(mapping);

				if (mapping.hasMetadataFile())
				{
					logger.info("Processing metadata file: {}", mapping.getQuizMetadataFile());
				}
				else 
				{
					logger.warn("Skipping metadata processing for assessment: {}", mapping.getQuizAssessmentFile());
				}
			}
		}

		if (quizFiles.isEmpty())
		{
			logger.warn("No quiz assessment files found in the manifest files.");
		}

		return quizFiles;
	}

	/**
	 * Retrieves the file path of the quiz metadata associated with a given quiz assessment file.
	 *
	 * @param assessmentResource The XML element representing the quiz assessment file.
	 * @param resources The list of all resources in the manifest file.
	 * @return The quiz metadata file path if found, or null if there is no metadata file.
	 */
	private String getMetadataFilePath(Element assessmentResource, NodeList resources) 
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
		if (metadataIdentifierRef == null || metadataIdentifierRef.trim().isEmpty()) 
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
					String metadataPath = ((Element) fileNodes.item(0)).getAttribute("href");
					logger.info("Found metadata file: {}", metadataPath);
					return metadataPath;
				}
			}
		}

		logger.warn("No corresponding quiz metadata file found for assessment identifier '{}'", assessmentIdentifier);
		return null;		
	}
}



