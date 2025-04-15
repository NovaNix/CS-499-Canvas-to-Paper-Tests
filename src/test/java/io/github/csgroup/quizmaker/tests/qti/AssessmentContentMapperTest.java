package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.mapping.QuestionMapper;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Questestinterop;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Section;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test verifies the functionality of the Assessment Content Mapper.
 * <p>
 * Verifies that a Quiz object is populated with the correct number of mapped questions.
 * 
 * @author Sarah Singhirunnusorn
 */
public class AssessmentContentMapperTest
{
	
	private static final Logger logger = LoggerFactory.getLogger(AssessmentContentMapperTest.class);

	@Test
	void testMapToQuizFromRealQTIContentFile_withDebug() throws Exception
	{
		File file = new File("src/test/resources/g9951ec51c6f36aca6d6092ce983e753c.xml");
		
		JAXBContext context = JAXBContext.newInstance(Questestinterop.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Questestinterop source = (Questestinterop) unmarshaller.unmarshal(file);

		Quiz quiz = new Quiz("demo-quiz", "Demo Quiz", new Label("Auto-generated for test", Label.Type.html));

		Assessment assessment = source.getAssessment();
		List<Item> allItems = collectItemsFromSections(assessment.getSections());

		List<Question> mapped = new ArrayList<>();
		List<String> skipped = new ArrayList<>();

		for (Item item : allItems)
		{
			Question q = QuestionMapper.mapQuestion(item);
			if (q != null)
			{
				mapped.add(q);
				quiz.addQuestion(q);
				logger.info("Mapped: {} ({})", item.getIdent(), item.getQuestionType());
			}
			else
			{
				skipped.add(item.getIdent());
				logger.warn("Skipped: {} ({})", item.getIdent(), item.getQuestionType());
			}
		}

		logger.info("Total mapped: {}", mapped.size());
		logger.info("Total skipped: {}", skipped.size());

		assertEquals(13, allItems.size(), "Expected 13 total items");
		assertEquals(13, mapped.size(), "Expected 13 successfully mapped questions");

		assertNotNull(quiz);
		assertEquals(13, quiz.getQuestions().size(), "Quiz should contain 13 mapped questions");
	}
	
	// Utility to recursively gather all items
	private List<Item> collectItemsFromSections(List<Section> sections)
	{
		List<Item> items = new ArrayList<>();
		if (sections == null) return items;

		for (Section section : sections)
		{
			if (section.getItems() != null)
				items.addAll(section.getItems());

			if (section.getSubsections() != null)
				items.addAll(collectItemsFromSections(section.getSubsections()));
		}

		return items;
	}
}


