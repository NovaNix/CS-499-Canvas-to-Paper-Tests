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
import java.util.List;
import java.util.Map;
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

			case "fill_in_the_blank_question":
			case "short_answer_question":
			case "numerical_question":
			case "fill_in_multiple_blanks_question":
			case "multiple_dropdowns_question":
				return mapBlank(item);

			case "matching_question":
				return mapMatching(item);

			case "essay_question":
				return mapWrittenResponse(item);

			default:
				logger.warn("Unsupported question_type '{}' in item: {}", type, item.getIdent());
				return null;
		}
	}

	/**
	 * Maps a QTI {@code multiple_choice_question} or {@code true_false_question} into a {@link MultipleChoiceQuestion}.
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
		List<String> correctIds = AnswerMapper.getCorrectAnswerIds(item);

		MultipleChoiceQuestion question = new MultipleChoiceQuestion(prompt.asText());
		applyMetadata(question, item, prompt);

		for (SimpleAnswer answer : answers)
		{
			boolean correct = correctIds.contains(String.valueOf(answer.getId()));
			question.addAnswer(answer, correct);
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

		FillInTheBlankQuestion question = new FillInTheBlankQuestion("");
		applyMetadata(question, item, prompt);

		List<String> tags = question.getTags();
		for (String tag : tags)
		{
			BlankAnswer answer = blanks.get(tag);
			if (answer != null)
			{
				question.setAnswer(tag, answer);
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

		MatchingQuestion question = new MatchingQuestion("");
		applyMetadata(question, item, prompt);

		for (MatchingAnswer answer : matches)
		{
			question.addAnswer(answer);
		}

		return question;
	}

	/**
	 * Maps a QTI {@code essay_question} or similar open-ended question into a {@link WrittenResponseQuestion}.
	 * 
	 * @param item the parsed QTI {@code <item>}
	 * @return a {@link WrittenResponseQuestion} with its label set
	 */
	private static WrittenResponseQuestion mapWrittenResponse(Item item)
	{
		Label prompt = MaterialMapper.toLabel(item.getPresentation().getMaterials().get(0));

		WrittenResponseQuestion question = new WrittenResponseQuestion("");
		applyMetadata(question, item, prompt);

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


