package io.github.csgroup.quizmaker.qti.model.assessment.structure;

import io.github.csgroup.quizmaker.qti.mapping.AnswerMapper.NumericRange;
import io.github.csgroup.quizmaker.qti.model.assessment.metadata.ItemMetadata;
import io.github.csgroup.quizmaker.qti.model.assessment.metadata.QTIMetadataField;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.MatText;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.Presentation;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseLabel;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.ResponseLid;
import io.github.csgroup.quizmaker.qti.model.assessment.response.ConditionVar;
import io.github.csgroup.quizmaker.qti.model.assessment.response.ConditionVarNot;
import io.github.csgroup.quizmaker.qti.model.assessment.response.RespCondition;
import io.github.csgroup.quizmaker.qti.model.assessment.response.ResponseProcessing;
import io.github.csgroup.quizmaker.qti.model.assessment.response.VarEqual;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a single {@code <item>} element in a QTI {@code <section>}. Contains metadata, presentation<br>
 * content, and scoring logic.
 * <p>
 * Provides utilities for retrieving question types, answer choices, correct answers, and matching<br>
 * pairs used in matching questions.
 * 
 * @author Sarah Singhirunnusorn
 */
public class Item 
{

	private String ident;
	private String title;
	private ItemMetadata itemMetadata;
	private Presentation presentation;
	private ResponseProcessing resprocessing;

	@XmlAttribute(name = "ident")
	public void setIdent(String ident) 
	{
		this.ident = ident;
	}

	@XmlAttribute(name = "title")
	public void setTitle(String title) 
	{
		this.title = title;
	}

	@XmlElement(name = "itemmetadata", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setItemMetadata(ItemMetadata itemMetadata) 
	{
		this.itemMetadata = itemMetadata;
	}

	@XmlElement(name = "presentation", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setPresentation(Presentation presentation) 
	{
		this.presentation = presentation;
	}

	@XmlElement(name = "resprocessing", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setResprocessing(ResponseProcessing resprocessing) 
	{
		this.resprocessing = resprocessing;
	}

	public String getIdent() 
	{
		return ident;
	}

	public String getTitle() 
	{
		return title;
	}

	public ItemMetadata getItemMetadata() 
	{
		return itemMetadata;
	}

	public Presentation getPresentation() 
	{
		return presentation;
	}

	public ResponseProcessing getResponseProcessing() 
	{
		return resprocessing;
	}

	/**
	 * Returns the question type of a question as specified in the metadata.
	 * 
	 * @return question type label (e.g., "multiple_choice_question")
	 */
	public String getQuestionType() 
	{
		if (itemMetadata != null && itemMetadata.getQtimetadata() != null && itemMetadata.getQtimetadata().getQtimetadatafield() != null) 
		{
			for (QTIMetadataField field : itemMetadata.getQtimetadata().getQtimetadatafield()) 
			{
				String label = field.getFieldlabel();
				if ("question_type".equalsIgnoreCase(label) || "qmd_questiontype".equalsIgnoreCase(label)) 
				{
					return field.getFieldentry();
				}
			}
		}
		
		return null;
	}

	/**
	 * Returns a mapping of response identifiers to their display text.
	 * 
	 * @return map of response ID to text
	 */
	public Map<String, String> getResponseIdToTextMap() 
	{
		Map<String, String> map = new LinkedHashMap<>();
		if (presentation != null && presentation.getResponseLids() != null) 
		{
			for (ResponseLid responseLid : presentation.getResponseLids()) 
			{
				if (responseLid.getRenderChoice() != null) 
				{
					for (ResponseLabel label : responseLid.getRenderChoice().getResponseLabels()) 
					{
						map.put(label.getIdent(), label.getDisplayText());
					}
				}
			}
		}
		
		return map;
	}

	/**
	 * Retrieves a list of formatted correct answer strings.
	 * 
	 * @return list of correct answers
	 */
	public List<String> getCorrectAnswers() 
	{
		List<String> correctAnswers = new ArrayList<>();
		Map<String, String> idToText = getResponseIdToTextMap();

		if (resprocessing != null && resprocessing.getResponseConditions() != null) 
		{
			for (RespCondition condition : resprocessing.getResponseConditions()) 
			{
				if (condition.getConditionVar() != null) 
				{
					List<String> ids = extractAnswerIdsFromConditionVar(condition.getConditionVar());
					for (String id : ids) 
					{
						String display = idToText.getOrDefault(id, "(Literal Answer) " + id);
						correctAnswers.add(id + ": " + display);
					}
				}
			}
		}

		return correctAnswers;
	}

	private List<String> extractAnswerIdsFromConditionVar(ConditionVar conditionVar) 
	{
		Set<String> include = new LinkedHashSet<>();
		Set<String> exclude = new HashSet<>();
		
		collectAnswerIds(conditionVar, include, exclude);
		include.removeAll(exclude);
		
		return new ArrayList<>(include);
	}

	private void collectAnswerIds(ConditionVar var, Set<String> include, Set<String> exclude) 
	{
		if (var.getVarEquals() != null) 
		{
			for (VarEqual ve : var.getVarEquals()) 
			{
				include.add(ve.getValue());
			}
		}

		if (var.getAnd() != null) 
		{
			if (var.getAnd().getVarEquals() != null) 
			{
				for (VarEqual ve : var.getAnd().getVarEquals()) 
				{
					include.add(ve.getValue());
				}
			}
			if (var.getAnd().getConditionVars() != null) 
			{
				for (ConditionVar sub : var.getAnd().getConditionVars()) 
				{
					collectAnswerIds(sub, include, exclude);
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
							exclude.add(ve.getValue());
						}
					}
					if (not.getConditionVar() != null) 
					{
						exclude.addAll(extractAnswerIdsFromConditionVar(not.getConditionVar()));
					}
				}
			}
		}

		if (var.getOr() != null && var.getOr().getConditionVars() != null) 
		{
			for (ConditionVar sub : var.getOr().getConditionVars()) 
			{
				collectAnswerIds(sub, include, exclude);
			}
		}

		if (var.getNot() != null) 
		{
			if (var.getNot().getVarEquals() != null) 
			{
				for (VarEqual ve : var.getNot().getVarEquals()) 
				{
					exclude.add(ve.getValue());
				}
			}
			if (var.getNot().getConditionVar() != null) 
			{
				exclude.addAll(extractAnswerIdsFromConditionVar(var.getNot().getConditionVar()));
			}
		}
	}

	/**
	 * Returns the list of matching pairs for matching questions.
	 * 
	 * @return list of "left → right" formatted match strings
	 */
	public List<String> getMatchingPairs() 
	{
		List<String> pairs = new ArrayList<>();
		if (!"matching_question".equalsIgnoreCase(getQuestionType())) return pairs;

		Map<String, String> leftMap = getLeftSideLabels();
		Map<String, String> rightMap = getResponseIdToTextMap();

		if (resprocessing != null && resprocessing.getResponseConditions() != null) 
		{
			for (RespCondition condition : resprocessing.getResponseConditions()) 
			{
				if (condition.getConditionVar() != null && condition.getConditionVar().getVarEquals() != null) 
				{
					for (VarEqual ve : condition.getConditionVar().getVarEquals()) 
					{
						String lhsId = ve.getRespIdent();
						String rhsId = ve.getValue();
						String lhsText = leftMap.getOrDefault(lhsId, lhsId);
						String rhsText = rightMap.getOrDefault(rhsId, rhsId);
						pairs.add(lhsText + " → " + rhsText);
					}
				}
			}
		}
		
		return pairs;
	}

	private Map<String, String> getLeftSideLabels() 
	{
		Map<String, String> map = new LinkedHashMap<>();
		if (presentation != null && presentation.getResponseLids() != null) 
		{
			for (ResponseLid lid : presentation.getResponseLids()) 
			{
				if (lid.getIdent() != null && lid.getMaterial() != null && lid.getMaterial().getMattext() != null) 
				{
					String left = lid.getMaterial().getMattext().stream()
							.map(MatText::getText)
							.collect(Collectors.joining(" "));
					map.put(lid.getIdent(), left);
				}
			}
		}
		
		return map;
	}
	
	/**
	 * Returns a list of human-readable numeric ranges for numerical questions,<br>
	 * based on the QTI <vargte>, <varlte>, <vargt>, and <varlt> tags.
	 * 
	 * @return list of range descriptions (e.g., "between 1.0 and 10.0")
	 */
	public List<NumericRange> getNumericRanges()
	{
		List<NumericRange> ranges = new ArrayList<>();

		if (resprocessing == null || resprocessing.getResponseConditions() == null)
		{
			return ranges;
		}

		for (RespCondition cond : resprocessing.getResponseConditions())
		{
			ConditionVar var = cond.getConditionVar();
			if (var != null)
			{
				ranges.addAll(collectNumericRanges(var));
			}
		}

		return ranges;
	}

	private List<NumericRange> collectNumericRanges(ConditionVar var)
	{
		List<NumericRange> result = new ArrayList<>();
		if (var == null) return result;

		String respId = null, min = null, max = null;

		if (var.getVarGte() != null)
		{
			for (var gte : var.getVarGte()) 
			{ 
				respId = gte.getRespIdent(); min = gte.getValue(); 
			}
		}
		if (var.getVarGt() != null)
		{
			for (var gt : var.getVarGt()) 
			{ 
				respId = gt.getRespIdent(); min = gt.getValue(); 
			}
		}
		if (var.getVarLte() != null)
		{
			for (var lte : var.getVarLte()) 
			{ 
				respId = lte.getRespIdent(); max = lte.getValue(); 
			}
		}
		if (var.getVarLt() != null)
		{
			for (var lt : var.getVarLt()) 
			{ 
				respId = lt.getRespIdent(); max = lt.getValue(); 
			}
			
		}
		if (respId != null && min != null && max != null)
		{
			result.add(new NumericRange(respId, min, max));
		}

		// Handle nested logic
		if (var.getAnd() != null && var.getAnd().getConditionVars() != null)
		{
			for (ConditionVar sub : var.getAnd().getConditionVars())
			{
				result.addAll(collectNumericRanges(sub));
			}
		}
		if (var.getOr() != null && var.getOr().getConditionVars() != null)
		{
			for (ConditionVar sub : var.getOr().getConditionVars())
			{
				result.addAll(collectNumericRanges(sub));
			}
		}
		if (var.getNot() != null && var.getNot().getConditionVar() != null)
		{
			result.addAll(collectNumericRanges(var.getNot().getConditionVar()));
		}

		return result;
	}
	
	/**
	 * Returns a list of literal (exact) numeric answers found in <varequal> blocks.<br>
	 * These are typically used in numerical questions where the value must match exactly.
	 * 
	 * @return list of numeric values as strings (e.g., ["5.20", "2"])
	 */
	public List<String> getExactNumericAnswers()
	{
		List<String> values = new ArrayList<>();

		if (resprocessing != null && resprocessing.getResponseConditions() != null)
		{
			for (RespCondition cond : resprocessing.getResponseConditions())
			{
				ConditionVar var = cond.getConditionVar();
				if (var != null)
				{
					List<String> extracted = collectExactAnswers(var);
					for (String value : extracted)
					{
						if (value.matches("-?\\d+(\\.\\d+)?")) // basic numeric check
						{
							values.add(value);
						}
					}
				}
			}
		}

		return values;
	}

	private List<String> collectExactAnswers(ConditionVar var)
	{
		List<String> result = new ArrayList<>();

		if (var.getVarEquals() != null)
		{
			for (VarEqual ve : var.getVarEquals())
			{
				String val = ve.getValue();
				if (val != null && !val.isBlank())
				{
					result.add(val.trim());
				}
			}
		}

		if (var.getAnd() != null)
		{
			if (var.getAnd().getVarEquals() != null)
			{
				for (VarEqual ve : var.getAnd().getVarEquals())
				{
					String val = ve.getValue();
					if (val != null && !val.isBlank())
					{
						result.add(val.trim());
					}
				}
			}

			if (var.getAnd().getConditionVars() != null)
			{
				for (ConditionVar sub : var.getAnd().getConditionVars())
				{
					result.addAll(collectExactAnswers(sub));
				}
			}
		}

		if (var.getOr() != null)
		{
			if (var.getOr().getVarEquals() != null)
			{
				for (VarEqual ve : var.getOr().getVarEquals())
				{
					String val = ve.getValue();
					if (val != null && !val.isBlank())
					{
						result.add(val.trim());
					}
				}
			}

			if (var.getOr().getConditionVars() != null)
			{
				for (ConditionVar sub : var.getOr().getConditionVars())
				{
					result.addAll(collectExactAnswers(sub));
				}
			}
		}

		if (var.getNot() != null && var.getNot().getConditionVar() != null)
		{
			result.addAll(collectExactAnswers(var.getNot().getConditionVar()));
		}

		return result;
	}

	/**
	 * Returns a map of blank identifiers to their list of answer choices.<br>
	 * Used for displaying drop-down answer options for each individual blank.
	 * 
	 * @return a map where each key is a blank ID and each value is the list of choices for that blank
	 */
	public Map<String, List<String>> getBlankChoicesByResponseId()
	{
		Map<String, List<String>> map = new LinkedHashMap<>();
		if (presentation != null && presentation.getResponseLids() != null)
		{
			for (ResponseLid lid : presentation.getResponseLids())
			{
				String key = lid.getIdent();
				List<String> choices = new ArrayList<>();
				if (lid.getRenderChoice() != null && lid.getRenderChoice().getResponseLabels() != null)
				{
					for (ResponseLabel label : lid.getRenderChoice().getResponseLabels())
					{
						choices.add(label.getDisplayText());
					}
				}
				map.put(key, choices);
			}
		}
		return map;
	}
	
	/**
	 * Retrieves the number of points possible for a question. 
	 * <p>
	 * If this element is not present or invalid, it will return as 0.0 as a fallback method.
	 * 
	 * @return points possible as a double (defaults to 0.0) 
	 */
	public Double getPointsPossible()
	{
		if (itemMetadata != null && itemMetadata.getQtimetadata() != null && itemMetadata.getQtimetadata().getQtimetadatafield() != null)
		{
			for (QTIMetadataField field : itemMetadata.getQtimetadata().getQtimetadatafield())
			{
				if ("points_possible".equalsIgnoreCase(field.getFieldlabel()))
				{
					String value = field.getFieldentry();
					if (value != null && !value.trim().isEmpty())
					{
						return Double.parseDouble(value.trim());
					}
				}
			}	
		}
		
		return null;
	}
	
	// TODO: implement logic for identifying bank-sourced questions.

	@Override
	public String toString() 
	{
		return "Item{" +
				"ident='" + ident + '\'' +
				", title='" + title + '\'' +
				", metadata=" + itemMetadata +
				", presentation=" + presentation +
				", resprocessing=" + resprocessing +
				'}';
	}
}
