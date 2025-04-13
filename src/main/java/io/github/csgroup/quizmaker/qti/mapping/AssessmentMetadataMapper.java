package io.github.csgroup.quizmaker.qti.mapping;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.model.AssessmentMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class converts a parsed {@link AssessmentMetadata} into the {@link Quiz} object.
 * Ensures missing or optional metadata fields are handled with default values.
 *  
 * @author Sarah Singhirunnusorn
 */
public class AssessmentMetadataMapper 
{
	
	private static final Logger logger = LoggerFactory.getLogger(AssessmentMetadataMapper.class);
	
	/**
	 * Maps a parsed {@link AssessmentMetadata} object to a {@link Quiz} object.
	 * 
	 * @param metadata The parsed metadata from a QTI <code>assessment_meta.xml</code> file.
	 * @return A {@link Quiz} object populated with the extracted metadata.
	 */
	public static Quiz mapToQuiz(AssessmentMetadata metadata)
	{
		if (metadata == null) 
		{
            logger.warn("AssessmentMetadata is null. Returning null Quiz.");
            return null;
        }
		
		String id = metadata.getIdentifier();
        String title = metadata.getTitle();
        String rawDescription = metadata.getQuizDescription();
		
		if (id == null) 
		{
            id = "";
            logger.warn("Quiz identifier was MISSING. Defaulting to empty string.");
        }
		
		if (title == null) 
		{
            title = "Unnamed Quiz";
            logger.warn("Quiz title was MISSING. Defaulting to 'Unnamed Quiz'.");
        }
		
		if (rawDescription == null)
		{
			rawDescription = "";
            logger.info("Quiz description was MISSING. Using empty description.");
		}
		
		Label description = new Label(rawDescription, Label.Type.html);

        Quiz quiz = new Quiz(id, title, description);
        logger.info("Mapped Quiz → ID: '{}', Title: '{}'", id, title);
		
		// Map points possible
		// Map due date
		
		return quiz;
	}
}
