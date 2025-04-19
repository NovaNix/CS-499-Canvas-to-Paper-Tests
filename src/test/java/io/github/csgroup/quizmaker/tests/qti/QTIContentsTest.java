 package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.QTIContents;
import io.github.csgroup.quizmaker.qti.QTIReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This test verifies that the QTIReader properly populates the QTI contents with quizzes.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIContentsTest
{
	
	private static final Logger logger = LoggerFactory.getLogger(QTIContentsTest.class);

	private QTIReader qtiReader;
	private File testQtiZipFile;
	private Path tempDirectory;

	@BeforeEach
	public void setUp() throws IOException 
	{
		logger.info("Setting up QTIContentsTest...");

		qtiReader = new QTIReader();

		URL resourceUrl = getClass().getClassLoader().getResource("QTI_numerial_questions.zip");
		assertNotNull(resourceUrl, "ERROR!! QTI ZIP file is missing.");

		testQtiZipFile = new File(resourceUrl.getPath());
		assertTrue(testQtiZipFile.exists(), "ERROR!! Test QTI ZIP file should exist.");

		logger.info("Test QTI ZIP file located at: {}", testQtiZipFile.getAbsolutePath());
	}

	@Test
	public void testQTIContentsStructure() throws Exception 
	{
		logger.info("Running QTIContents Structure Test...");

		QTIContents contents = qtiReader.readFile(testQtiZipFile.getAbsolutePath());

		assertNotNull(contents, "QTIContents should not be null.");
		assertNotNull(contents.quizzes, "QTIContents.quizzes list should not be null.");
		assertTrue(contents.quizzes.size() > 0, "QTIContents.quizzes should contain at least one quiz.");
		logger.info("Total quizzes found in QTI ZIP: {}", contents.quizzes.size());

		Set<String> seenQuizIds = new HashSet<>();
		for (Quiz quiz : contents.quizzes)
		{
			assertTrue(seenQuizIds.add(quiz.getId()), "Duplicate quiz ID detected: " + quiz.getId());

			assertNotNull(quiz, "Quiz object should not be null.");
			assertNotNull(quiz.getId(), "Quiz ID should not be null.");
			assertNotNull(quiz.getTitle(), "Quiz title should not be null.");
			assertNotNull(quiz.getDescription(), "Quiz description should not be null.");
			assertNotNull(quiz.getQuestions(), "Quiz question list should not be null.");

			logger.info("------------------------------------");
			logger.info("Quiz ID: {}", quiz.getId());
			logger.info("Quiz Title: {}", quiz.getTitle());
			logger.info("Description: {}", quiz.getDescription().asText());
			logger.info("Questions: {}", quiz.getQuestionCount());

			int number = 1;
			for (var question : quiz.getQuestions())
			{
				logger.info("\tQuestion {}:", number++);
				logger.info("\t - Type: {}", question.getClass().getSimpleName());
				logger.info("\t - Title: {}", question.getTitle());
				logger.info("\t - Points: {}", question.getPoints());
				logger.info("\t - Prompt: {}", question.getLabel().asText());

				if (question instanceof io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion mcq)
				{
					logger.info("\t - Answer choices:");
					for (var answer : mcq.getAnswers())
					{
						String marker = mcq.isCorrect(answer) ? " (Correct)" : "";
						logger.info("\t\t• {}{}", answer.asText(), marker);
					}
				}
				else if (question instanceof io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion fib)
				{
					logger.info("\t - Answer Blanks:");

					List<String> tags = fib.getTags();
					if (tags.isEmpty() && fib.getAnswer("answer") != null)
					{
						tags = List.of("answer");
					}

					for (String tag : tags)
					{
						var answer = fib.getAnswer(tag);
						if (answer != null)
						{
							String label = tag.equalsIgnoreCase("answer") && tags.size() == 1
								? "Short Answer Question Blank"
								: tag;
							logger.info("\t\tBlank: {}", label);
							logger.info("\t\t - Correct Answer: {}", answer.getAnswer().asText());

							Set<String> allChoices = new LinkedHashSet<>();
							allChoices.add(answer.getAnswer().asText());
							for (var distractor : answer.getDistractors())
							{
								allChoices.add(distractor.asText());
							}

							logger.info("\t\t - Answer Choices: {}", String.join(", ", allChoices));
						}
						else
						{
							logger.info("\t\tBlank: {} → N/A", tag);
						}
					}
				}
				else if (question instanceof io.github.csgroup.quizmaker.data.questions.MatchingQuestion match)
				{
					logger.info("\t - Matching Pairs:");
					for (var pair : match.getAnswers())
					{
						logger.info("\t\t{} → {}", pair.getLeft(), pair.getRight());
					}
				}
				else if (question instanceof io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion wrq)
				{
					String answerText = (wrq.getAnswer() != null) ? wrq.getAnswer().asText() : null;
					if (answerText != null && !answerText.isBlank())
					{
						logger.info("\t - Answer: {}", answerText);
					}
					else
					{
						logger.info("\t - Answer: (Written response, no predefined answer)");
					}
				}
				else
				{
					logger.info("\t - Answer format not yet handled for: {}", question.getClass().getSimpleName());
				}
			}
		}
	}

	@AfterEach
	public void tearDown() throws IOException 
	{
		if (tempDirectory != null && Files.exists(tempDirectory)) 
		{
			Files.walk(tempDirectory)
				.sorted((p1, p2) -> -p1.compareTo(p2))
				.map(Path::toFile)
				.forEach(File::delete);
			logger.info("Cleaned up temporary test directory.");
		}
	}
}
