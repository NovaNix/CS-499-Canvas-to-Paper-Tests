package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.qti.mapping.AnswerMapper;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Questestinterop;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Section;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This test verifies the functionality of the Answer Mapper.
 * 
 * @author Sarah Singhirunnusorn
 */
public class AnswerMapperTest
{
	
	@Test
	void testMapSimpleAnswersFromQTIFile() throws Exception
	{
		Item item = getItemByIndex(0); // MCQ: What color is an apple?

		List<SimpleAnswer> answers = AnswerMapper.mapSimpleAnswers(item);
		List<String> correctIds = AnswerMapper.getCorrectAnswerIds(item);

		assertEquals(4, answers.size(), "Should have 4 answer choices");
		assertTrue(correctIds.contains("510"), "Correct answer ID should be 510");

		for (SimpleAnswer answer : answers)
		{
			assertNotNull(answer.getLabel());
			assertFalse(answer.getLabel().asText().isBlank());
		}
	}

	@Test
	void testMapBlankAnswersFromQTIFile() throws Exception
	{
		Item item = getItemByIndex(2); // Short answer: e.g., What is 2 + 2?

		Map<String, BlankAnswer> blanks = AnswerMapper.mapBlankAnswers(item);
		assertEquals(1, blanks.size(), "Should have one blank input");

		for (Map.Entry<String, BlankAnswer> entry : blanks.entrySet())
		{
			assertNotNull(entry.getKey());
			assertNotNull(entry.getValue().getAnswer());
			
			assertFalse(entry.getValue().getAnswer().asText().isBlank());
		}
	}

	@Test
	void testMapMatchingAnswersFromQTIFile() throws Exception
	{
		Item item = getItemByIndex(6); // Matching question

		List<MatchingAnswer> matches = AnswerMapper.mapMatchingAnswers(item);
		assertEquals(4, matches.size(), "Should return 4 matching pairs");

		for (MatchingAnswer match : matches)
		{
			assertNotNull(match.getLeft());
			assertNotNull(match.getRight());
			
			assertFalse(match.getLeft().asText().isBlank());
			assertFalse(match.getRight().asText().isBlank());
		}
	}

	// Utility: load and return the nth <item> from the first section
	private Item getItemByIndex(int index) throws Exception
	{
		File file = new File("src/test/resources/g9951ec51c6f36aca6d6092ce983e753c.xml");
		
		JAXBContext context = JAXBContext.newInstance(Questestinterop.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Questestinterop root = (Questestinterop) unmarshaller.unmarshal(file);

		Assessment assessment = root.getAssessment();
		Section section = assessment.getSections().get(0);
		
		return section.getItems().get(index);
	}
}

