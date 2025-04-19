package io.github.csgroup.quizmaker.qti.mapping;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.data.questions.MatchingQuestion;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps a parsed QTI {@link Item} to its {@link Question} subclass.
 * <p>
 * This class supports the following question types:
 * <ul>
 *     <li>multiple_choice_question</li>
 *     <li>true_false_question</li>
 *     <li>fill_in_the_blank_question</li>
 *     <li>short_answer_question</li>
 *     <li>numerical_question</li>
 *     <li>fill_in_multiple_blanks_question</li>
 *     <li>multiple_drop-downs_question</li>
 *     <li>matching_question</li>
 *     <li>essay_question</li>
 * </ul>
 * Unsupported or missing question types are logged and skipped.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QuestionMapper
{

	private static final Logger logger = LoggerFactory.getLogger(QuestionMapper.class);
	
	// Used when no bracketed tag is found in the prompt.
	// This acts as a default identifier for short answer responses.
	private static final String FALLBACK_TAG = "answer";
	
	/**
	 * Determines the question type based on the QTI {@code question_type} metadata and applies<br>
	 * the appropriate mapping method.
	 * 
	 * @param item the parsed QTI {@code <item>}
	 * @return the corresponding Question object, or {@code null} if the item type is unsupported or invalid
	 */
	public static Question mapQuestion(Item item)
	{
		String type = item.getQuestionType();
		
		if (type == null || type.isBlank())
		{
			logger.warn("Skipping item with missing question_type: {}", item.getIdent());
			return null;
		}

		switch (type)
		{
			case "multiple_choice_question":
			case "true_false_question":
			case "multiple_answers_question":
				return mapMultipleChoice(item);
			
			case "short_answer_question":
				return mapShortAnswer(item);

			case "fill_in_the_blank_question":
			case "fill_in_multiple_blanks_question":
			case "multiple_dropdowns_question":
				return mapBlank(item);

			case "matching_question":
				return mapMatching(item);

			case "numerical_question":
			case "essay_question":
				return mapWrittenResponse(item);

			default:
				logger.warn("Unsupported question_type '{}' in item: {}", type, item.getIdent());
				return null;
		}
	}

	/**
	 * Maps a QTI {@code multiple_choice_question}, {@code true_false_question}, or {@code multiple_answers_question} into a {@link MultipleChoiceQuestion}.
	 * <p>
	 * Uses {@link AnswerMapper} to populate answer choices and detect the correct one(s).
	 * 
	 * @param item the parsed QTI {@code <item>}
	 * @return a {@link MultipleChoiceQuestion} with its label and answers set
	 */
	private static MultipleChoiceQuestion mapMultipleChoice(Item item)
	{
		Label prompt = MaterialMapper.toLabel(item.getPresentation().getMaterials().get(0));
		List<SimpleAnswer> answers = AnswerMapper.mapSimpleAnswers(item);
		Set<String> correctIds = new HashSet<>();
		for (String full : item.getCorrectAnswers()) 
		{
			String id = full.split(":")[0].trim();
			correctIds.add(id);
		}
		
		double points = item.getPointsPossible() != null ? item.getPointsPossible() : 0.0;
		MultipleChoiceQuestion question = new MultipleChoiceQuestion(prompt.asText(), (float) points);
		applyMetadata(question, item, prompt);

		for (SimpleAnswer answer : answers)
		{
			boolean correct = correctIds.contains(String.valueOf(answer.getId()));
			question.addAnswer(answer, correct);
		}

		return question;
	}
	
	/**
	 * Maps a QTI {@code short_answer_question} item to a {@link WrittenResponseQuestion}.
	 * 
	 * @param item the parsed QTI {@code <item>}
	 * @return a {@link WrittenResponseQuestion} with prompt and associated blank answers
	 */
	private static WrittenResponseQuestion mapShortAnswer(Item item)
	{
		Label prompt = MaterialMapper.toLabel(item.getPresentation().getMaterials().get(0));
		double points = item.getPointsPossible() != null ? item.getPointsPossible() : 0.0;
		
		WrittenResponseQuestion question = new WrittenResponseQuestion("", (float) points);
		applyMetadata(question, item, prompt);

		List<String> correctAnswers = item.getCorrectAnswers();
		if (!correctAnswers.isEmpty())
		{
			String joined = String.join(" or ", correctAnswers);
			question.setAnswer(joined);
		}
		else
		{
			logger.warn("Short answer question {} has no recognized correct answers", item.getIdent());
		}

		return question;
	}
	
	/**
	 * Maps a QTI blank-style question (e.g., {@code fill_in_the_blank_question}, {@code short_answer_question})<br>
	 * into a {@link FillInTheBlankQuestion}.
	 * <p>
	 * Tags are extracted from the prompt, and matching answers are populated using {@link AnswerMapper}.
	 * 
	 * @param item the parsed QTI {@code <item>}
	 * @return a {@link FillInTheBlankQuestion} with prompt and associated blank answers
	 */
	private static FillInTheBlankQuestion mapBlank(Item item)
	{
		Label prompt = MaterialMapper.toLabel(item.getPresentation().getMaterials().get(0));
		Map<String, BlankAnswer> blanks = AnswerMapper.mapBlankAnswers(item);

		double points = item.getPointsPossible() != null ? item.getPointsPossible() : 0.0;
		FillInTheBlankQuestion question = new FillInTheBlankQuestion("", (float) points);
		applyMetadata(question, item, prompt);

		List<String> tags = question.getTags();
		if (!tags.isEmpty())
		{
			// Use tags extracted from label
			for (String tag : tags)
			{
				BlankAnswer answer = blanks.get(tag);

				// No direct match -> try to find one that ends with the tag
				if (answer == null)
				{
					for (Map.Entry<String, BlankAnswer> entry : blanks.entrySet())
					{
						if (entry.getKey().endsWith(tag))
						{
							answer = entry.getValue();
							break;
						}
					}
				}

				if (answer != null)
				{
					question.setAnswer(tag, answer);
				}
				else
				{
					logger.warn("No matching blank answer found for tag [{}] in item {}", tag, item.getIdent());
				}
			}
		}
		else
		{
			// No tag found in prompt -> treat as short answer fallback
			if (!blanks.isEmpty())
			{
				Map.Entry<String, BlankAnswer> entry = blanks.entrySet().iterator().next();
				question.setAnswer(FALLBACK_TAG, entry.getValue());
			}
		}

		return question;
	}

	/**
	 * Maps a QTI {@code matching_question} into a {@link MatchingQuestion}.
	 * <p>
	 * Matching pairs are determined from response processing and rendered choices.
	 * 
	 * @param item the parsed QTI {@code <item>}
	 * @return a {@link MatchingQuestion} with matching pairs added
	 */
	private static MatchingQuestion mapMatching(Item item)
	{
		Label prompt = MaterialMapper.toLabel(item.getPresentation().getMaterials().get(0));
		List<MatchingAnswer> matches = AnswerMapper.mapMatchingAnswers(item);

		double points = item.getPointsPossible() != null ? item.getPointsPossible() : 0.0;
		MatchingQuestion question = new MatchingQuestion("", (float) points);
		applyMetadata(question, item, prompt);

		for (MatchingAnswer answer : matches)
		{
			question.addAnswer(answer);
		}

		return question;
	}

	/**
	 * Maps a QTI {@code essay_question} or {@code numerical_question} into a {@link WrittenResponseQuestion}.
	 * 
	 * @param item the parsed QTI {@code <item>}
	 * @return a {@link WrittenResponseQuestion} with its label set
	 */
	private static WrittenResponseQuestion mapWrittenResponse(Item item)
	{
		Label prompt = MaterialMapper.toLabel(item.getPresentation().getMaterials().get(0));

		double points = item.getPointsPossible() != null ? item.getPointsPossible() : 0.0;
		WrittenResponseQuestion question = new WrittenResponseQuestion("", (float) points);
		applyMetadata(question, item, prompt);
		
		// For numerical question -> extract answer type: exact answers 
		List<String> exactAnswers = item.getExactNumericAnswers();

		// For numerical question -> extract answer type: range answer
		List<AnswerMapper.NumericRange> ranges = AnswerMapper.getNumericRanges(item);
		List<String> displayParts = new ArrayList<>();

		if (!exactAnswers.isEmpty())
		{
			displayParts.add(String.join(" or ", exactAnswers));
		}

		for (var range : ranges)
		{
			displayParts.add(range.toString());
		}

		if (!displayParts.isEmpty())
		{
			String finalAnswer = String.join(" or ", displayParts);
			question.setAnswer(finalAnswer);
		}
		else 
		{
			question.setAnswer(AnswerMapper.mapEssayAnswer(item));
		}

		return question;
	}
	
	/**
	 * Applies shared metadata fields (title and prompt) to a {@link Question}.
	 *
	 * @param question the question object to populate
	 * @param item the parsed QTI {@code <item>}
	 * @param label the question prompt
	 */
	private static void applyMetadata(Question question, Item item, Label label)
	{
		question.setTitle(item.getTitle());
		question.setLabel(label);
	}
}


