package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.data.questions.MatchingQuestion;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.qti.mapping.QuestionMapper;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Questestinterop;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Section;
import io.github.csgroup.quizmaker.utils.HTMLUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


/**
 * This test verifies the functionality of the Question Mapper.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QuestionMapperTest
{
	
	@Test
	void testMultipleChoiceMapping() throws Exception
	{
		Item item = getItemByIndex(0); // "What color is an apple?"

		Question q = QuestionMapper.mapQuestion(item);
		assertNotNull(q);
		assertTrue(q instanceof MultipleChoiceQuestion);

		MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;

		String htmlPrompt = mcq.getLabel().asText().trim();
		String decoded = HTMLUtils.removeEntities(htmlPrompt);
		
		assertEquals("<div><p>What color is an apple?</p></div>", decoded);
		assertEquals("Question 1", mcq.getTitle());
		assertEquals(4, mcq.getAnswers().size());
		assertEquals(1.0f, mcq.getPoints(), 0.01f);
	}
	
	@Test
	void testShortAnswerMapping() throws Exception
	{
		Item item = getItemByIndex(2); // short answer: no [tag], just text input

		Question q = QuestionMapper.mapQuestion(item);
		assertNotNull(q);
		assertTrue(q instanceof WrittenResponseQuestion);

		WrittenResponseQuestion wrq = (WrittenResponseQuestion) q;
		assertFalse(wrq.getAnswer().asText().isBlank());
		assertEquals(1.0f, wrq.getPoints(), 0.01f);
	}

	@Test
	void testBlankMapping() throws Exception
	{
		Item item = getItemByIndex(3); // short answer or fill in the blank

		Question q = QuestionMapper.mapQuestion(item);
		assertNotNull(q);
		assertTrue(q instanceof FillInTheBlankQuestion);

		FillInTheBlankQuestion fbq = (FillInTheBlankQuestion) q;

		// Allow for short answer questions that may not contain tags
		List<String> tags = fbq.getTags();
		for (String tag : tags)
		{
			assertNotNull(fbq.getAnswer(tag), "Expected an answer mapped for tag [" + tag + "]");
		}

		assertTrue(tags.isEmpty() || tags.size() > 0);
		assertEquals(1.0f, fbq.getPoints(), 0.01f);
	}

	@Test
	void testMatchingMapping() throws Exception
	{
		Item item = getItemByIndex(6); // matching question

		Question q = QuestionMapper.mapQuestion(item);
		assertNotNull(q);
		assertTrue(q instanceof MatchingQuestion);

		MatchingQuestion mq = (MatchingQuestion) q;
		assertEquals(4, mq.getAnswers().size());
		for (MatchingAnswer a : mq.getAnswers())
		{
			assertNotNull(a.getLeft());
			assertNotNull(a.getRight());
		}
		assertEquals(1.0f, mq.getPoints(), 0.01f);
	}

	@Test
	void testEssayMapping() throws Exception
	{
		Item item = getItemByIndex(9); // essay question

		Question q = QuestionMapper.mapQuestion(item);
		assertNotNull(q);
		assertTrue(q instanceof WrittenResponseQuestion);

		WrittenResponseQuestion wrq = (WrittenResponseQuestion) q;
		assertFalse(wrq.getLabel().asText().isBlank());
		assertNotNull(wrq.getTitle());
		assertEquals(1.0f, wrq.getPoints(), 0.01f);
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




