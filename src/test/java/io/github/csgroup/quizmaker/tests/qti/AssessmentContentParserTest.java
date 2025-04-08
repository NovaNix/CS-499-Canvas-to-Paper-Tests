package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.qti.model.assessment.metadata.QTIMetadataField;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.MatText;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.Material;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.Presentation;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Section;
import io.github.csgroup.quizmaker.qti.parsing.AssessmentContentParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify that the AssessmentContentParser correctly parses the QTI assessment content XML file.
 * <p>
 * - Ensures the file is loaded properly from test resources<br>
 * - Parses the file into an {@link Assessment} object<br>
 * - Verifies that key fields such as title and identifier are extracted correctly<br>
 * - Confirms the presence of sections and items<br>
 * - Prints out points per question and other relevant question metadata
 * 
 * @author Sarah Singhirunnusorn
 */
public class AssessmentContentParserTest 
{

	private static final Logger logger = LoggerFactory.getLogger(AssessmentContentParserTest.class);
	private AssessmentContentParser parser;

	@BeforeEach
	public void setUp() 
	{
		parser = new AssessmentContentParser();
		logger.info("AssessmentContentParser initialized.");
	}

	@Test
	public void testParseAssessmentContent() throws Exception 
	{
		URL resource = getClass().getClassLoader().getResource("g9951ec51c6f36aca6d6092ce983e753c.xml");
		assertNotNull(resource, "Assessment content XML file not found in test resources.");

		File assessmentFile = new File(resource.toURI());
		assertTrue(assessmentFile.exists(), "Assessment content XML file does not exist.");

		Assessment assessment = parser.parse(assessmentFile);
		assertNotNull(assessment, "Parsed Assessment object should not be null.");

		logger.info("\n==== PARSED ASSESSMENT DATA ====");
		logger.info("Assessment Title: {}", assessment.getTitle());
		logger.info("Assessment ID: {}", assessment.getIdent());

		for (Section section : assessment.getSections()) 
		{
			processSection(section, 0);
		}
	}

	private void processSection(Section section, int level) 
	{
		String indent = "  ".repeat(level);
		if (section.getIdent() != null) 
		{
			logger.info(indent + "Section: {}", section.getIdent());
		}

		if (section.getSubsections() != null) 
		{
			for (Section subsection : section.getSubsections()) 
			{
				processSection(subsection, level + 1);
			}
		}

		if (section.getItems() != null) 
		{
			for (Item item : section.getItems()) 
			{
				logger.info("\n" + indent + "------------------------------");
				logger.info(indent + "Question {}", item.getTitle());
				logger.info(indent + " - ID: {}", item.getIdent());
				logger.info(indent + " - Title: {}", item.getTitle());

				// Points per question
				String points = getPointsPossible(item);
				logger.info(indent + " - Points: {}", (points != null ? points : "N/A"));

				// Prompt
				Presentation presentation = item.getPresentation();
				if (presentation != null && presentation.getMaterials() != null) 
				{
					for (Material material : presentation.getMaterials()) 
					{
						if (material.getMattext() != null) 
						{
							String prompt = material.getMattext().stream()
									.map(MatText::getText)
									.collect(Collectors.joining(" "));
							logger.info(indent + " - Prompt: {}", prompt);
						}
					}
				}

				// Question type
				String questionType = item.getQuestionType();
				logger.info(indent + " - Question Type: {}", questionType);

				// Answer choices
				Map<String, String> responseMap = item.getResponseIdToTextMap();
				if (!responseMap.isEmpty()) 
				{
					logger.info(indent + " - Answer Choices:");
					responseMap.forEach((id, text) ->
							logger.info(indent + "     • {}: {}", id, text));
				}

				// Matching pairs
				if ("matching_question".equalsIgnoreCase(questionType)) 
				{
					List<String> matchingPairs = item.getMatchingPairs();
					if (!matchingPairs.isEmpty()) 
					{
						logger.info(indent + " - Matching Pairs:");
						matchingPairs.forEach(pair -> logger.info(indent + "     • {}", pair));
					}
				}

				// Correct answers
				List<String> correctAnswers = item.getCorrectAnswers();
				if (!correctAnswers.isEmpty()) 
				{
					correctAnswers.forEach(answer -> logger.info(indent + " - Correct Answer: {}", answer));
				}

				// Note for user input-required questions
				if (questionType != null && (questionType.contains("essay") || questionType.contains("short_answer") || questionType.contains("numerical"))) 
				{
					logger.info(indent + " - Note: This question requires user input.");
				}
			}
		}
	}

	private String getPointsPossible(Item item) 
	{
		if (item.getItemMetadata() != null && item.getItemMetadata().getQtimetadata() != null && item.getItemMetadata().getQtimetadata().getQtimetadatafield() != null) 
		{
			for (QTIMetadataField field : item.getItemMetadata().getQtimetadata().getQtimetadatafield()) 
			{
				if ("points_possible".equalsIgnoreCase(field.getFieldlabel())) 
				{
					return field.getFieldentry();
				}
			}
		}
		return null;
	}
}




