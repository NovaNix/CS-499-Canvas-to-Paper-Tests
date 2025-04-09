package io.github.csgroup.quizmaker.qti.model.assessment.structure;

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
