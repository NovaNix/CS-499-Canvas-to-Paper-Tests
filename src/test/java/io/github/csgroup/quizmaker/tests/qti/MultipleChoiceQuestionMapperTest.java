package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.qti.mapping.QuestionMapper;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.parsing.AssessmentContentParser;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test to verify the correct mapping of Canvas-style multiple choice,
 * true/false, and multiple answers questions into {@link MultipleChoiceQuestion}
 * and {@link SimpleAnswer} internal types.
 * <p>
 * This test prints each question's prompt, point value, answer choices (with IDs),
 * and correct answer(s) for manual review or logging.
 * 
 * @author 
 */
public class MultipleChoiceQuestionMapperTest
{

	/**
	 * Parses the provided QTI XML file and verifies that each multiple-choice-style question
	 * is correctly mapped into a {@link MultipleChoiceQuestion}, with valid {@link SimpleAnswer} objects.
	 * Prints the answer choices and correct answers for each item.
	 */
	@Test
	public void printMappedMultipleChoiceQuestions() throws JAXBException
	{
		// Load the test QTI file from resources
		File file = new File("src/test/resources/g8c13480e86b1d8e66054eeff6d690df9.xml");
		AssessmentContentParser parser = new AssessmentContentParser();
		Assessment assessment = parser.parse(file);

		assertNotNull(assessment);
		assertFalse(assessment.getSections().isEmpty());

		int questionNumber = 1;

		for (Item item : assessment.getSections().get(0).getItems())
		{
			String type = item.getQuestionType();

			if (!type.equals("multiple_choice_question") &&
				!type.equals("true_false_question") &&
				!type.equals("multiple_answers_question"))
			{
				continue; // Skip non-matching types
			}

			Question q = QuestionMapper.mapQuestion(item);
			assertNotNull(q);
			assertTrue(q instanceof MultipleChoiceQuestion);

			MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;

			// Print header
			System.out.println("--------------------------------------------------");
			System.out.println("Question " + questionNumber++);
			System.out.println("Title: " + mcq.getTitle());
			System.out.println("Type: " + type);
			System.out.println("Points: " + mcq.getPoints());
			System.out.println("Prompt: " + mcq.getLabel().asText());

			// Print all answers
			System.out.println("Answer Choices:");
			for (SimpleAnswer answer : mcq.getAnswers())
			{
				System.out.println("- ID: " + answer.getId() + ", Text: " + answer.getLabel().asText());
			}

			// Print correct answers
			System.out.println("Correct Answer(s):");
			for (SimpleAnswer answer : mcq.getCorrectAnswers())
			{
				System.out.println("- ID: " + answer.getId() + ", Text: " + answer.getLabel().asText());
			}
		}
	}
}

