package io.github.csgroup.quizmaker.qti.mapping;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.RenderChoice;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseLabel;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseLid;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseStr;
import io.github.csgroup.quizmaker.qti.model.assessment.response.ConditionVar;
import io.github.csgroup.quizmaker.qti.model.assessment.response.ConditionVarNot;
import io.github.csgroup.quizmaker.qti.model.assessment.response.RespCondition;
import io.github.csgroup.quizmaker.qti.model.assessment.response.VarEqual;
import io.github.csgroup.quizmaker.qti.model.assessment.response.VarGt;
import io.github.csgroup.quizmaker.qti.model.assessment.response.VarGte;
import io.github.csgroup.quizmaker.qti.model.assessment.response.VarLt;
import io.github.csgroup.quizmaker.qti.model.assessment.response.VarLte;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maps QTI answer data from a parsed {@link Item} into the application-specific answer objects.
 * <p>
 * Supports mapping for:
 * <ul>
 *     <li>Blank Answer</li>
 *     <li>Matching Answer</li>
 *     <li>SimpleAnswer</li>
 * </ul>
 * 
 * This class is used by {@link QuestionMapper} to create corresponding answer models.
 * 
 * @author Sarah Singhirunnusorn
 */
public class AnswerMapper
{
	
	/**
	 * Represents a numeric range (min and max values) for numerical questions.
	 */
	public static record NumericRange(String respident, String min, String max)
	{
		@Override
		public String toString()
		{
			return "between " + min + " and " + max;
		}
	}

	/**
	 * Maps multiple choice and true/false questions into {@link SimpleAnswer} objects.
	 *
	 * @param item the QTI item
	 * @return a list of answer choices
	 */
	public static List<SimpleAnswer> mapSimpleAnswers(Item item)
	{
		List<SimpleAnswer> answers = new ArrayList<>();
		if (item.getPresentation() == null) return answers;

		List<ResponseLid> lids = item.getPresentation().getResponseLids();
		if (lids == null) return answers;

		for (ResponseLid lid : lids)
		{
			RenderChoice choice = lid.getRenderChoice();
			if (choice == null || choice.getResponseLabels() == null) continue;

			for (ResponseLabel label : choice.getResponseLabels())
			{
				int id = Integer.parseInt(label.getIdent());
				Label text = MaterialMapper.toLabel(label.getMaterials().get(0));
				answers.add(new SimpleAnswer(id, text));
			}
		}
		return answers;
	}

	/**
	 * Maps fill-in-the-blank or short-answer responses into {@link BlankAnswer} objects.
	 *
	 * @param item the QTI item
	 * @return map of respident to blank answer
	 */
	public static Map<String, BlankAnswer> mapBlankAnswers(Item item)
	{
		Map<String, BlankAnswer> blanks = new HashMap<>();
		Map<String, List<String>> corrects = extractCorrectAnswersByRespident(item);

		if (item.getPresentation() == null)
			return blanks;

		// Build a map of response ID → display text from <response_label>
		Map<String, String> idToText = new HashMap<>();
		if (item.getPresentation().getResponseLids() != null)
		{
			for (ResponseLid lid : item.getPresentation().getResponseLids())
			{
				if (lid.getRenderChoice() != null && lid.getRenderChoice().getResponseLabels() != null)
				{
					for (ResponseLabel label : lid.getRenderChoice().getResponseLabels())
					{
						String id = label.getIdent();
						String text = MaterialMapper.toLabel(label.getMaterials().get(0)).asText();
						idToText.put(id, text);
					}
				}
			}
		}

		// Handle <response_lid>
		if (item.getPresentation().getResponseLids() != null)
		{
			for (ResponseLid lid : item.getPresentation().getResponseLids())
			{
				String respId = lid.getIdent();
				BlankAnswer blank = new BlankAnswer(respId.hashCode());

				List<String> values = corrects.get(respId);
				Set<String> known = new HashSet<>();

				if (values != null && !values.isEmpty())
				{
					String first = values.get(0);
					String display = idToText.getOrDefault(first, first);
					blank.setAnswer(new Label(display));
					known.add(first);

					for (int i = 1; i < values.size(); i++)
					{
						String alt = values.get(i);
						String altDisplay = idToText.getOrDefault(alt, alt);
						blank.addDistractor(new Label(altDisplay));
						known.add(alt);
					}
				}

				// Add remaining unused options as distractors
				for (String id : idToText.keySet())
				{
					if (!known.contains(id))
					{
						blank.addDistractor(new Label(idToText.get(id)));
					}
				}

				blanks.put(respId, blank);
			}
		}

		// Handle <response_str>
		if (item.getPresentation().getResponseStrs() != null)
		{
			for (ResponseStr str : item.getPresentation().getResponseStrs())
			{
				String respId = str.getIdent();
				BlankAnswer blank = new BlankAnswer(respId.hashCode());

				List<String> values = corrects.get(respId);
				if (values != null && !values.isEmpty())
				{
					blank.setAnswer(new Label(values.get(0)));

					for (int i = 1; i < values.size(); i++)
					{
						blank.addDistractor(new Label(values.get(i)));
					}
				}

				blanks.put(respId, blank);
			}
		}

		return blanks;
	}

	/**
	 * Maps matching questions into {@link MatchingAnswer} pairs.
	 *
	 * @param item the QTI item
	 * @return list of matching pairs
	 */
	public static List<MatchingAnswer> mapMatchingAnswers(Item item)
	{
		List<MatchingAnswer> matches = new ArrayList<>();
		Map<String, Label> lefts = new HashMap<>();
		Map<String, Map<String, Label>> rights = new HashMap<>();

		if (item.getPresentation() != null && item.getPresentation().getResponseLids() != null)
		{
			for (ResponseLid lid : item.getPresentation().getResponseLids())
			{
				lefts.put(lid.getIdent(), MaterialMapper.toLabel(lid.getMaterial()));

				Map<String, Label> options = new HashMap<>();
				for (ResponseLabel label : lid.getRenderChoice().getResponseLabels())
				{
					options.put(label.getIdent(), MaterialMapper.toLabel(label.getMaterials().get(0)));
				}
				rights.put(lid.getIdent(), options);
			}
		}

		if (item.getResponseProcessing() != null)
		{
			for (RespCondition cond : item.getResponseProcessing().getResponseConditions())
			{
				ConditionVar var = cond.getConditionVar();
				if (var != null && var.getVarEquals() != null)
				{
					for (VarEqual v : var.getVarEquals())
					{
						Label left = lefts.get(v.getRespIdent());
						Label right = rights.get(v.getRespIdent()).get(v.getValue());
						if (left != null && right != null)
						{
							int id = (v.getRespIdent() + ":" + v.getValue()).hashCode();
							matches.add(new MatchingAnswer(id, left, right));
						}
					}
				}
			}
		}

		return matches;
	}

	/**
	 * Returns a list of correct answer IDs for simple and multiple choice questions.
	 *
	 * @param item the QTI item
	 * @return list of correct answer IDs
	 */
	public static List<String> getCorrectAnswerIds(Item item)
	{
		return new ArrayList<>(getCorrectAnswerIdSet(item));
	}

	/**
	 * Returns a set of correct answer IDs by traversing condition logic.
	 *
	 * @param item the QTI item
	 * @return set of correct answer IDs
	 */
	public static Set<String> getCorrectAnswerIdSet(Item item)
	{
		Set<String> result = new HashSet<>();
		if (item.getResponseProcessing() != null)
		{
			for (RespCondition cond : item.getResponseProcessing().getResponseConditions())
			{
				result.addAll(collectCorrectAnswersIgnoringNot(cond.getConditionVar()));
			}
		}
		return result;
	}

	/**
	 * Parses all numeric ranges from the response processing logic.
	 *
	 * @param item the QTI item
	 * @return list of numeric ranges
	 */
	public static List<NumericRange> getNumericRanges(Item item)
	{
		List<NumericRange> ranges = new ArrayList<>();
		if (item.getResponseProcessing() == null) return ranges;

		for (RespCondition cond : item.getResponseProcessing().getResponseConditions())
		{
			ranges.addAll(collectNumericRanges(cond.getConditionVar()));
		}
		return ranges;
	}
	
	private static List<NumericRange> collectNumericRanges(ConditionVar var)
	{
		List<NumericRange> result = new ArrayList<>();
		if (var == null) return result;

		String resp = null, min = null, max = null;

		if (var.getVarGte() != null)
			for (VarGte v : var.getVarGte()) { resp = v.getRespIdent(); min = v.getValue(); }

		if (var.getVarGt() != null)
			for (VarGt v : var.getVarGt()) { resp = v.getRespIdent(); min = v.getValue(); }

		if (var.getVarLte() != null)
			for (VarLte v : var.getVarLte()) { resp = v.getRespIdent(); max = v.getValue(); }

		if (var.getVarLt() != null)
			for (VarLt v : var.getVarLt()) { resp = v.getRespIdent(); max = v.getValue(); }

		if (resp != null && min != null && max != null)
			result.add(new NumericRange(resp, min, max));

		if (var.getAnd() != null && var.getAnd().getConditionVars() != null)
			for (ConditionVar sub : var.getAnd().getConditionVars())
				result.addAll(collectNumericRanges(sub));

		if (var.getOr() != null && var.getOr().getConditionVars() != null)
			for (ConditionVar sub : var.getOr().getConditionVars())
				result.addAll(collectNumericRanges(sub));

		if (var.getNot() != null && var.getNot().getConditionVar() != null)
			result.addAll(collectNumericRanges(var.getNot().getConditionVar()));

		return result;
	}

	private static List<String> collectCorrectAnswersIgnoringNot(ConditionVar var)
	{
		List<String> include = new ArrayList<>();
		Set<String> exclude = new HashSet<>();

		if (var == null) return include;

		// Collect varequal values directly
		if (var.getVarEquals() != null)
		{
			for (VarEqual ve : var.getVarEquals())
			{
				if (ve.getValue() != null && !ve.getValue().isBlank())
				{
					include.add(ve.getValue().trim());
				}
			}
		}

		// Collect from <and>
		if (var.getAnd() != null)
		{
			if (var.getAnd().getVarEquals() != null)
			{
				for (VarEqual ve : var.getAnd().getVarEquals())
				{
					if (ve.getValue() != null && !ve.getValue().isBlank())
					{
						include.add(ve.getValue().trim());
					}
				}
			}

			if (var.getAnd().getConditionVars() != null)
			{
				for (ConditionVar sub : var.getAnd().getConditionVars())
				{
					include.addAll(collectCorrectAnswersIgnoringNot(sub));
				}
			}

			if (var.getAnd().getNots() != null)
			{
				for (ConditionVarNot not : var.getAnd().getNots())
				{
					if (not.getVarEquals() != null)
					{
						for (VarEqual ve : not.getVarEquals())
						{
							if (ve.getValue() != null && !ve.getValue().isBlank())
							{
								exclude.add(ve.getValue().trim());
							}
						}
					}

					if (not.getConditionVar() != null)
					{
						exclude.addAll(collectCorrectAnswersIgnoringNot(not.getConditionVar()));
					}
				}
			}
		}

		// Collect from <or>
		if (var.getOr() != null)
		{
			if (var.getOr().getVarEquals() != null)
			{
				for (VarEqual ve : var.getOr().getVarEquals())
				{
					if (ve.getValue() != null && !ve.getValue().isBlank())
					{
						include.add(ve.getValue().trim());
					}
				}
			}

			if (var.getOr().getConditionVars() != null)
			{
				for (ConditionVar sub : var.getOr().getConditionVars())
				{
					include.addAll(collectCorrectAnswersIgnoringNot(sub));
				}
			}
		}

		// Collect from top-level <not>
		if (var.getNot() != null)
		{
			if (var.getNot().getVarEquals() != null)
			{
				for (VarEqual ve : var.getNot().getVarEquals())
				{
					if (ve.getValue() != null && !ve.getValue().isBlank())
					{
						exclude.add(ve.getValue().trim());
					}
				}
			}

			if (var.getNot().getConditionVar() != null)
			{
				exclude.addAll(collectCorrectAnswersIgnoringNot(var.getNot().getConditionVar()));
			}
		}

		// Remove excluded values from included ones
		include.removeAll(exclude);

		return include;
	}

	private static Map<String, List<String>> extractCorrectAnswersByRespident(Item item)
	{
		Map<String, List<String>> map = new HashMap<>();
		if (item.getResponseProcessing() == null) return map;

		for (RespCondition cond : item.getResponseProcessing().getResponseConditions())
		{
			ConditionVar var = cond.getConditionVar();
			if (var != null && var.getVarEquals() != null)
			{
				for (VarEqual v : var.getVarEquals())
				{
					map.computeIfAbsent(v.getRespIdent(), k -> new ArrayList<>()).add(v.getValue());
				}
			}
		}
		return map;
	}
	
	/**
	 * Maps a default answer for essay or written response questions.
	 * <p>
	 * This method provides a safe fallback to avoid null errors.
	 *
	 * @param item the QTI item
	 * @return a non-null Label to assign as the answer
	 */
	public static Label mapEssayAnswer(Item item)
	{
		return new Label(""); // Or use: return new Label("This question requires user input.");
	}
	
}
