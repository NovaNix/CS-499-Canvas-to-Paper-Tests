package io.github.csgroup.quizmaker.qti.mapping;

import java.util.*;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;

import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.response.ConditionVar;
import io.github.csgroup.quizmaker.qti.model.assessment.response.RespCondition;
import io.github.csgroup.quizmaker.qti.model.assessment.response.ResponseProcessing;
import io.github.csgroup.quizmaker.qti.model.assessment.response.VarEqual;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.RenderChoice;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseLid;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseLabel;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseStr;

/**
 * Maps QTI answer data from a parsed {@link Item} into application-specific answer objects,<br>
 * including {@link SimpleAnswer}, {@link BlankAnswer}, and {@link MatchingAnswer}.
 * <p>
 * This class supports multiple formats such as multiple choice, true/false, fill-in-the-blank,<br>
 * drop-downs, short answer, and matching questions.
 * 
 * @author Sarah Singhirunnusorn
 */
public class AnswerMapper
{
	
	/**
	 * Maps multiple choice and true/false questions into {@link SimpleAnswer}s.
	 * 
	 * @param item the QTI item containing response labels
	 * @return a list of all the answer options
	 */
	public static List<SimpleAnswer> mapSimpleAnswers(Item item)
	{
		List<SimpleAnswer> answers = new ArrayList<>();

		List<ResponseLid> responseLids = item.getPresentation().getResponseLids();
		if (responseLids == null)
		{
			return answers;
		}

		for (ResponseLid lid : responseLids)
		{
			if (lid.getRenderChoice() == null || lid.getRenderChoice().getResponseLabels() == null)
			{
				continue;
			}

			for (ResponseLabel label : lid.getRenderChoice().getResponseLabels())
			{
				int id = Integer.parseInt(label.getIdent());
				Label mappedLabel = MaterialMapper.toLabel(label.getMaterials().get(0));
				SimpleAnswer answer = new SimpleAnswer(id, mappedLabel);
				answers.add(answer);
			}
		}

		return answers;
	}

	/**
	 * Maps blank-style questions (fill in the blank, short answer, drop-downs) into {@link BlankAnswer}s.
	 * 
	 * @param item the QTI item to process
	 * @return a map of respident → BlankAnswer
	 */
	public static Map<String, BlankAnswer> mapBlankAnswers(Item item)
	{
		Map<String, BlankAnswer> blanks = new HashMap<>();
		Map<String, List<String>> correctAnswers = extractCorrectAnswersByRespident(item);

		if (item.getPresentation() != null && item.getPresentation().getResponseLids() != null)
		{
			for (ResponseLid lid : item.getPresentation().getResponseLids())
			{
				String respident = lid.getIdent();
				BlankAnswer answer = new BlankAnswer(respident.hashCode());

				List<String> correctVals = correctAnswers.get(respident);
				if (correctVals != null && !correctVals.isEmpty())
				{
					answer.setAnswer(new Label(correctVals.get(0)));
				}

				RenderChoice choice = lid.getRenderChoice();
				if (choice != null && choice.getResponseLabels() != null)
				{
					for (ResponseLabel label : choice.getResponseLabels())
					{
						Label distractor = MaterialMapper.toLabel(label.getMaterials().get(0));
						answer.addDistractor(distractor);
					}
				}

				blanks.put(respident, answer);
			}
		}

		if (item.getPresentation() != null && item.getPresentation().getResponseStrs() != null)
		{
			for (ResponseStr str : item.getPresentation().getResponseStrs())
			{
				String respident = str.getIdent();
				BlankAnswer answer = new BlankAnswer(respident.hashCode());

				List<String> correctVals = correctAnswers.get(respident);
				if (correctVals != null && !correctVals.isEmpty())
				{
					answer.setAnswer(new Label(correctVals.get(0)));
				}

				blanks.put(respident, answer);
			}
		}

		return blanks;
	}

	/**
	 * Maps matching questions into {@link MatchingAnswer} objects.
	 * 
	 * @param item the QTI item to process
	 * @return a list of correct matching pairs
	 */
	public static List<MatchingAnswer> mapMatchingAnswers(Item item)
	{
		List<MatchingAnswer> matches = new ArrayList<>();

		Map<String, Label> leftLabels = new HashMap<>();
		Map<String, Map<String, Label>> rightOptionsByRespident = new HashMap<>();

		if (item.getPresentation() != null && item.getPresentation().getResponseLids() != null)
		{
			for (ResponseLid lid : item.getPresentation().getResponseLids())
			{
				String respident = lid.getIdent();
				Label left = MaterialMapper.toLabel(lid.getMaterial());
				leftLabels.put(respident, left);

				Map<String, Label> rightOptions = new HashMap<>();
				if (lid.getRenderChoice() != null)
				{
					for (ResponseLabel label : lid.getRenderChoice().getResponseLabels())
					{
						String id = label.getIdent();
						Label right = MaterialMapper.toLabel(label.getMaterials().get(0));
						rightOptions.put(id, right);
					}
				}
				
				rightOptionsByRespident.put(respident, rightOptions);
			}
		}

		ResponseProcessing responseProcessing = item.getResponseProcessing();
		if (responseProcessing != null && responseProcessing.getResponseConditions() != null)
		{
			for (RespCondition condition : responseProcessing.getResponseConditions())
			{
				ConditionVar var = condition.getConditionVar();
				if (var != null && var.getVarEquals() != null)
				{
					for (VarEqual v : var.getVarEquals())
					{
						String respident = v.getRespIdent();
						String matchId = v.getValue();

						Label left = leftLabels.get(respident);
						Label right = null;

						if (respident != null && rightOptionsByRespident.containsKey(respident))
						{
							right = rightOptionsByRespident.get(respident).get(matchId);
						}

						if (left != null && right != null)
						{
							int id = (respident + ":" + matchId).hashCode();
							matches.add(new MatchingAnswer(id, left, right));
						}
					}
				}
			}
		}

		return matches;
	}

	/**
	 * Extracts a list of correct answer IDs from a QTI {@link Item}'s response processing block.
	 * Useful for simple/multiple-choice questions.
	 * 
	 * @param item the QTI item
	 * @return list of correct answer IDs
	 */
	public static List<String> getCorrectAnswerIds(Item item)
	{
		Set<String> correctIds = new HashSet<>();

		ResponseProcessing responseProcessing = item.getResponseProcessing();
		if (responseProcessing == null || responseProcessing.getResponseConditions() == null)
		{
			return new ArrayList<>();
		}

		for (RespCondition condition : responseProcessing.getResponseConditions())
		{
			ConditionVar var = condition.getConditionVar();
			if (var != null && var.getVarEquals() != null)
			{
				for (VarEqual v : var.getVarEquals())
				{
					String value = v.getValue();
					if (value != null && !value.isBlank())
					{
						correctIds.add(value.trim());
					}
				}
			}
		}

		return new ArrayList<>(correctIds);
	}

	/**
	 * Extracts correct answers grouped by respident for blank and matching questions.
	 * 
	 * @param item the QTI item
	 * @return a map of respident to list of correct values
	 */
	private static Map<String, List<String>> extractCorrectAnswersByRespident(Item item)
	{
		Map<String, List<String>> map = new HashMap<>();

		ResponseProcessing responseProcessing = item.getResponseProcessing();
		if (responseProcessing != null && responseProcessing.getResponseConditions() != null)
		{
			for (RespCondition condition : responseProcessing.getResponseConditions())
			{
				ConditionVar var = condition.getConditionVar();
				if (var != null && var.getVarEquals() != null)
				{
					for (VarEqual v : var.getVarEquals())
					{
						String id = v.getRespIdent();
						String value = v.getValue();
						
						if (id != null && value != null)
						{
							map.computeIfAbsent(id, k -> new ArrayList<>()).add(value);
						}
					}
				}
			}
		}

		return map;
	}
}




