package io.github.csgroup.quizmaker.qti.parsing;

import io.github.csgroup.quizmaker.qti.model.AssessmentMetadata;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

/**
 * This class parses the <code>assessment_meta.xml</code> file into an {@link AssessmentMetadata} object.<br>
 * Uses JAXB for XML binding and is responsible for transforming the raw XML into a structured Java object. 
 * 
 * @author Sarah Singhirunnusorn
 */
public class AssessmentMetadataParser 
{
    
	/**
	* Parses the <code>assessment_meta.xml</code> file and returns the quiz metadata elements.
	*
	* @param metadataFile The Metadata file to be parsed.
	* @return Parsed {@link AssessmentMetadata} object.
	* @throws JAXBException if the file cannot be parsed.
	*/
	public AssessmentMetadata parse(File metadataFile) throws JAXBException 
	{	
		// Initialize JAXB context
		JAXBContext context = JAXBContext.newInstance(AssessmentMetadata.class);
		// Create an unmarshaller from the context
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		// Parse the XML file and cast the result to AssessmentMetadata
		return (AssessmentMetadata) unmarshaller.unmarshal(metadataFile);
	}    
}
