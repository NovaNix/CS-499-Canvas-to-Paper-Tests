package io.github.csgroup.quizmaker.qti.mapping;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Questestinterop;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Section;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class maps the content of a parsed QTI {@link Questestinterop} object into a {@link Quiz},<br>
 * populating it with fully mapped {@link Question}(s) using the {@link QuestionMapper}.
 * <p>
 * If no valid items are found, the resulting {@link Quiz} will contain no questions but will not be null.
 * 
 * @author Sarah Singhirunnusorn
 */
public class AssessmentContentMapper
{
	
	private static final Logger logger = LoggerFactory.getLogger(AssessmentContentMapper.class);

	/**
	 * Populates and returns the given {@link Quiz} with questions parsed from a QTI {@link Questestinterop}.
	 *
	 * @param assessment the parsed assessment object
	 * @param quiz the quiz to populate (usually already populated with the quiz title, ID, description)
	 * @return the same {@link Quiz} object with added questions
	 */
	public static Quiz mapToQuiz(Assessment assessment, Quiz quiz)
	{
		if (assessment == null)
		{
			logger.warn("ERROR! Cannot populate quiz: Assessment is null.");
			return quiz;
		}

		List<Item> allItems = collectItemsFromSections(assessment.getSections());
		logger.info("Mapping {} QTI item(s) into quiz: {}", allItems.size(), quiz.getId());

		int skipped = 0;

		for (Item item : allItems)
		{
			Question question = QuestionMapper.mapQuestion(item);

			if (question != null)
			{
				quiz.addQuestion(question);
			}
			else
			{
				skipped++;
				logger.warn("Skipped unmappable item: {}", item.getIdent());
			}
		}

		logger.info("Finished mapping. Total: {}, Added: {}, Skipped: {}", allItems.size(), quiz.getQuestionCount(), skipped);
		return quiz;
	}

	/**
	 * Recursively collects all {@link Item}s from a list of {@link Section}s, including nested sections.
	 * 
	 * @param sections the top-level sections
	 * @return a list of all items across all sections
	 */
	private static List<Item> collectItemsFromSections(List<Section> sections)
	{
		List<Item> items = new ArrayList<>();

		if (sections == null) return items;

		for (Section section : sections)
		{
			if (section.getItems() != null)
			{
				items.addAll(section.getItems());
			}
			
			if (section.getSubsections() != null && !section.getSubsections().isEmpty())
			{
				items.addAll(collectItemsFromSections(section.getSubsections()));
			}
		}

		return items;
	}
}
