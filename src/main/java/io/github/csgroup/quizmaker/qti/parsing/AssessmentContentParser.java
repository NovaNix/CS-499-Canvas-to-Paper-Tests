package io.github.csgroup.quizmaker.qti.parsing;

import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Questestinterop;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses a QTI assessment content file (e.g., {@code gxxxxx.xml}) into a structured <br> 
 * {@link Assessment} object. <br>
 * <p>
 * Uses JAXB for XML binding and is responsible for transforming the raw XML into a structured Java object.
 * 
 * @author Sarah Singhirunnusorn
 */
public class AssessmentContentParser 
{
	private static final Logger logger = LoggerFactory.getLogger(AssessmentContentParser.class);

	/**
	* Parses the provided QTI assessment XML file quiz and returns the quiz content elements.
	*
	* @param assessmentFile The Content file to be parsed.
	* @return Parsed {@link Assessment} object.
	* @throws JAXBException if the file cannot be parsed.
	*/
	public Assessment parse(File assessmentFile) throws JAXBException 
	{
		
		if (assessmentFile == null || !assessmentFile.exists()) 
		{
			logger.warn("Assessment Content file is missing or does not exist. Skipping assessment content parsing.");
			return null;
        }

		// Initialize JAXB context
		JAXBContext context = JAXBContext.newInstance(Questestinterop.class);
		// Create an unmarshaller from the context
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		// Unmarshal to the wrapper and extract to Assessment
		Questestinterop root = (Questestinterop) unmarshaller.unmarshal(assessmentFile);
		Assessment assessment = root.getAssessment();
		
		// Parse the XML file and cast the result to Assessment
		return assessment;
    }
}
