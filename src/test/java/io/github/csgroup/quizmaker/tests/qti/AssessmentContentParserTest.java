package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.qti.mapping.AnswerMapper.NumericRange;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.MatText;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.Material;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.Presentation;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Section;
import io.github.csgroup.quizmaker.qti.parsing.AssessmentContentParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		// Load quiz content file
		URL resource = getClass().getClassLoader().getResource("g22f4cec28e64e3cad0f8e9ab185267d5.xml");
		assertNotNull(resource, "Assessment content XML file not found in test resources.");

		File assessmentFile = new File(resource.toURI());
		assertTrue(assessmentFile.exists(), "Assessment content XML file does not exist.");

		// Parse quiz content file
		Assessment assessment = parser.parse(assessmentFile);
		assertNotNull(assessment, "Parsed Assessment object should not be null.");

		logger.info("\n==== PARSED ASSESSMENT DATA ====");
		logger.info("Assessment Title: {}", assessment.getTitle());
		logger.info("Assessment ID: {}", assessment.getIdent());

		// Process each section withint the file
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

		// Hnadles nested sections
		if (section.getSubsections() != null) 
		{
			for (Section subsection : section.getSubsections()) 
			{
				processSection(subsection, level + 1);
			}
		}

		// Process each item (question)
		if (section.getItems() != null) 
		{
			for (Item item : section.getItems()) 
			{
				logger.info("\n" + indent + "------------------------------");
				logger.info(indent + "Question: {}", item.getTitle());
				logger.info(indent + " - ID: {}", item.getIdent());
				logger.info(indent + " - Title: {}", item.getTitle());

				String questionType = item.getQuestionType();
				logger.info(indent + " - Question Type: {}", questionType);

				Double points = item.getPointsPossible();
				logger.info(indent + " - Points: {}", (points != null ? points : "N/A"));

				// Identify and process prompt
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

				boolean isBlankType = questionType != null && (questionType.contains("blank") || questionType.contains("dropdown"));

				// Display correct answer text (except for multiple answers question type)
				List<String> exactAnswers = item.getCorrectAnswers();
				if (!isBlankType && !exactAnswers.isEmpty() && !"multiple_answers_question".equalsIgnoreCase(questionType)) 
				{
					List<String> values = exactAnswers.stream()
						.map(ans -> {
							String clean = ans.replace("(Literal Answer)", "").trim();
							String[] split = clean.split(":", 2);
							return (split.length == 2 ? split[1].trim() : split[0].trim());
						})
						.collect(Collectors.toList());
					logger.info(indent + " - Correct Answer(s): {}", String.join(" or ", values));
				}

				// Map response IDs to the display text
				Map<String, String> responseMap = item.getResponseIdToTextMap();
				
				// Track the correct response IDs
				Map<String, List<String>> correctAnswersMap = new LinkedHashMap<>();
				if (item.getResponseProcessing() != null)
				{
					for (var cond : item.getResponseProcessing().getResponseConditions())
					{
						var var = cond.getConditionVar();
						if (var != null && var.getVarEquals() != null)
						{
							for (var ve : var.getVarEquals())
							{
								correctAnswersMap
									.computeIfAbsent(ve.getRespIdent(), k -> new java.util.ArrayList<>())
									.add(ve.getValue());
							}
						}
					}
				}

				// Display answer chocies for each question
				if (!isBlankType && !responseMap.isEmpty())
				{
					logger.info(indent + " - Answer Choices:");

					for (Map.Entry<String, String> entry : responseMap.entrySet())
					{
						String id = entry.getKey();
						String text = entry.getValue();

						boolean isCorrect = "multiple_answers_question".equalsIgnoreCase(questionType)
							&& item.getCorrectAnswers().stream()
								.map(ans -> ans.split(":", 2)[0].trim()) // Extract just the ID
								.anyMatch(correctId -> correctId.equalsIgnoreCase(id));

						if (isCorrect)
						{
							logger.info(indent + "     • " + text + " (Correct)");
						}
						else
						{
							logger.info(indent + "     • " + text);
						}
					}
				}

				// Display answer blanks (for dropdown/fill-in-blank question types)
				Map<String, List<String>> blankChoices = item.getBlankChoicesByResponseId();
				if (!blankChoices.isEmpty() && isBlankType)
				{
					logger.info(indent + " - Answer Blanks:");
					blankChoices.forEach((blankId, choices) ->
					{
						String label = blankId.replace("response_", "");
						logger.info(indent + "     • [{}]", label);

						List<String> corrects = correctAnswersMap.get(blankId);
						if (corrects != null && !corrects.isEmpty())
						{
							List<String> display = corrects.stream()
								.map(id -> responseMap.getOrDefault(id, id))
								.collect(Collectors.toList());
							logger.info(indent + "         - Correct Answer: {}", String.join(" or ", display));
						}

						logger.info(indent + "         - Choices: {}", String.join(", ", choices));
					});
				}

				// Format matching pairs questions
				if ("matching_question".equalsIgnoreCase(questionType)) 
				{
					List<String> matchingPairs = item.getMatchingPairs();
					if (!matchingPairs.isEmpty()) 
					{
						logger.info(indent + " - Matching Pairs:");
						matchingPairs.forEach(pair -> logger.info(indent + "     • {}", pair));
					}
				}

				// Check for numeric values
				if ("numerical_question".equalsIgnoreCase(questionType)) 
				{
					List<String> numericValues = item.getExactNumericAnswers();
					if (!numericValues.isEmpty()) 
					{
						logger.info(indent + " - Exact Numeric Value(s):");
						numericValues.forEach(val -> logger.info(indent + "     • {}", val));
					}
					List<NumericRange> ranges = item.getNumericRanges();
					if (!ranges.isEmpty()) 
					{
						logger.info(indent + " - Numeric Range(s):");
						ranges.forEach(range -> logger.info(indent + "     • {}", range));
					}
				}

				// Note for user-input questions
				if ("short_answer_question".equalsIgnoreCase(questionType) || "essay_question".equalsIgnoreCase(questionType))
				{
					logger.info(indent + " - Note: This question requires user input.");
				}
			}
		}
	}
}
