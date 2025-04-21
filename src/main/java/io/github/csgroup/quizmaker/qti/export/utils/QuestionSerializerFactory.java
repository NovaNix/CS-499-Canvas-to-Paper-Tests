package io.github.csgroup.quizmaker.qti.export.utils;

import io.github.csgroup.quizmaker.data.Question;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.data.questions.MatchingQuestion;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.qti.export.serializers.question.FillInTheBlankQuestionSerializer;
import io.github.csgroup.quizmaker.qti.export.serializers.question.MatchingQuestionSerializer;
import io.github.csgroup.quizmaker.qti.export.serializers.question.MultipleChoiceQuestionSerializer;
import io.github.csgroup.quizmaker.qti.export.serializers.question.QuestionSerializer;
import io.github.csgroup.quizmaker.qti.export.serializers.question.WrittenResponseQuestionSerializer;

/**
 * Factory class to return the appropriate {@link QuestionSerializer} implementation
 * for a given {@link Question} instance.
 * <ul>
 *     <li>{@code MultipleChoiceQuestion} → {@link MultipleChoiceQuestionSerializer}</li>
 *     <li>{@code FillInTheBlankQuestion} → {@link FillInTheBlankQuestionSerializer}</li>
 *     <li>{@code MatchingQuestion} → {@link MatchingQuestionSerializer}</li>
 *     <li>{@code WrittenResponseQuestion} → {@link WrittenResponseQuestionSerializer}</li>
 * </ul>
 * 
 * @author Sarah Singhirunnusorn
 */
public class QuestionSerializerFactory 
{
	/**
	 * Returns a {@link QuestionSerializer} appropriate for the given {@link Question}.
	 * 
	 * @param question the question to serialize for
	 * @return a matching {@link QuestionSerializer}, or {@code null} if question type is unsupported
	 */
	public static QuestionSerializer<? extends Question> getSerializer(Question question)
	{
		if (question instanceof MultipleChoiceQuestion)
		{
			return new MultipleChoiceQuestionSerializer();
		}
		else if (question instanceof FillInTheBlankQuestion)
		{
			return new FillInTheBlankQuestionSerializer();
		}
		else if (question instanceof MatchingQuestion)
		{
			return new MatchingQuestionSerializer();
		}
		else if (question instanceof WrittenResponseQuestion)
		{
			return new WrittenResponseQuestionSerializer();
		}
		else
		{
			return null;
		}
	}
}
