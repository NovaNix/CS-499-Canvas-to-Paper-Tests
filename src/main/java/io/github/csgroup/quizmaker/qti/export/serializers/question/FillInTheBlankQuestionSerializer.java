package io.github.csgroup.quizmaker.qti.export.serializers.question;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.qti.export.serializers.LabelSerializer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Serializes a {@link FillInTheBlankQuestion} into a QTI {@code <item>} XML element.
 *  <p>
 * This class handles both short answer style and drop-down style fill-in-the-blank questions.<br>
 * In Canvas QTI, these map to {@code short_answer_question} and {@code fill_in_multiple_blanks_question}.
 * 
 * @author Sarah Singhirunnusorn
 */
public class FillInTheBlankQuestionSerializer implements QuestionSerializer<FillInTheBlankQuestion> 
{
	private final LabelSerializer labelSerializer = new LabelSerializer();
	
	/**
	 * Converts a {@link FillInTheBlankQuestion} into a {@code <item>} XML structure. 
	 * 
	 * @param d the XML document
	 * @param question the question object
	 * @return the generated {@code <item>}
	 */
	@Override
	public Element serialize(Document d, FillInTheBlankQuestion question)
	{
		Element item = d.createElement("item");
		item.setAttribute("ident", question.getId());
		item.setAttribute("title", question.getTitle());

		// Metadata Block
		Element itemMetadata = d.createElement("itemmetadata");
		Element qtiMetadata = d.createElement("qtimetadata");

		String questionType = determineQuestionType(question);
		appendMetadataField(d, qtiMetadata, "question_type", questionType);
		appendMetadataField(d, qtiMetadata, "points_possible", String.format(Locale.US, "%.2f", question.getPoints()));
		appendMetadataField(d, qtiMetadata, "assessment_question_identifierref", question.getId());

		itemMetadata.appendChild(qtiMetadata);
		item.appendChild(itemMetadata);

		// Presentation Block
		Element presentation = d.createElement("presentation");

		// Add question prompt
		Element promptMaterial = labelSerializer.asElement(d, question.getLabel());
		Element mattext = (Element) promptMaterial.getElementsByTagName("mattext").item(0);
		if (mattext != null)
		{
			mattext.setAttribute("texttype", "text/html");
		}
		presentation.appendChild(promptMaterial);

		Map<String, List<String>> correctAnswers = new LinkedHashMap<>();
		List<String> originalAnswerIds = new ArrayList<>();
		int idCounter = 1000;

		boolean isShortAnswer = isShortAnswerQuestion(question);

		for (String tag : question.getTags())
		{
			BlankAnswer blank = question.getAnswer(tag);
			if (blank == null)
				continue;

			String safeTag = tag.replaceAll("\\W+", "_");

			if (isShortAnswer)
			{
				Element response = d.createElement("response_str");
				response.setAttribute("ident", "response1");
				response.setAttribute("rcardinality", "Single");

				Element renderFib = d.createElement("render_fib");
				Element responseLabel = d.createElement("response_label");
				responseLabel.setAttribute("ident", "answer1");
				responseLabel.setAttribute("rshuffle", "No");

				renderFib.appendChild(responseLabel);
				response.appendChild(renderFib);
				presentation.appendChild(response);

				List<String> shortAnswers = new ArrayList<>();
				shortAnswers.add(blank.getAnswer().asText());
				for (Label distractor : blank.getDistractors())
				{
					shortAnswers.add(distractor.asText());
				}

				correctAnswers.put("response1", shortAnswers);
			}
			else
			{
				Element response = d.createElement("response_lid");
				response.setAttribute("ident", "response_" + safeTag);
				response.setAttribute("rcardinality", "Single");

				Element material = d.createElement("material");
				Element mattextLabel = d.createElement("mattext");
				mattextLabel.setTextContent(tag);
				material.appendChild(mattextLabel);
				response.appendChild(material);

				Element renderChoice = d.createElement("render_choice");
				List<String> correctIds = new ArrayList<>();

				Label correctOption = blank.getAnswer();

				String choiceId = String.valueOf(idCounter++);
				Element choice = d.createElement("response_label");
				choice.setAttribute("ident", choiceId);

				Element choiceMaterial = d.createElement("material");
				Element choiceText = d.createElement("mattext");
				choiceText.setAttribute("texttype", "text/plain");
				choiceText.setTextContent(correctOption.asText());
				choiceMaterial.appendChild(choiceText);
				choice.appendChild(choiceMaterial);

				renderChoice.appendChild(choice);

				correctIds.add(choiceId);
				originalAnswerIds.add(choiceId);

				response.appendChild(renderChoice);
				presentation.appendChild(response);
				correctAnswers.put("response_" + safeTag, correctIds);
			}
		}
		item.appendChild(presentation);

		if (!originalAnswerIds.isEmpty())
		{
			appendMetadataField(d, qtiMetadata, "original_answer_ids", String.join(",", originalAnswerIds));
		}

		// Resprocessing Block
		Element resprocessing = d.createElement("resprocessing");

		Element outcomes = d.createElement("outcomes");
		Element decvar = d.createElement("decvar");
		decvar.setAttribute("maxvalue", "100");
		decvar.setAttribute("minvalue", "0");
		decvar.setAttribute("varname", "SCORE");
		decvar.setAttribute("vartype", "Decimal");
		outcomes.appendChild(decvar);
		resprocessing.appendChild(outcomes);

		double perBlank = 100.0 / correctAnswers.size();
		boolean firstCondition = true;

		for (Map.Entry<String, List<String>> entry : correctAnswers.entrySet())
		{
			Element respcondition = d.createElement("respcondition");
			if (isShortAnswer && firstCondition)
			{
				respcondition.setAttribute("continue", "No");
				firstCondition = false;
			}

			Element conditionvar = d.createElement("conditionvar");

			for (String value : entry.getValue())
			{
				Element varequal = d.createElement("varequal");
				varequal.setAttribute("respident", entry.getKey());
				varequal.setTextContent(value);
				conditionvar.appendChild(varequal);
			}
			respcondition.appendChild(conditionvar);

			Element setvar = d.createElement("setvar");
			setvar.setAttribute("action", "Add");
			setvar.setTextContent(String.format(Locale.US, "%.2f", perBlank));

			respcondition.appendChild(setvar);
			resprocessing.appendChild(respcondition);
		}
		item.appendChild(resprocessing);

		return item;
	}

	/**
	 * Utility method to append a QTI metadata field to the metadata block.
	 * 
	 * @param d the document 
	 * @param parent the QTI metadata parent element
	 * @param field the metadata key
	 * @param value the metadata value
	 */
	private void appendMetadataField(Document d, Element parent, String field, String value)
	{
		Element fieldNode = d.createElement("qtimetadatafield");

		Element label = d.createElement("fieldlabel");
		label.setTextContent(field);
		fieldNode.appendChild(label);

		Element entry = d.createElement("fieldentry");
		entry.setTextContent(value);
		fieldNode.appendChild(entry);

		parent.appendChild(fieldNode);
	}

	/**
	 * Utility method to determine the Canvas question type for a {@link FillInTheBlankQuestion}.
	 * 
	 * @param question the question to determine
	 * @return the Canvas {@code question_type} string
	 */
	private String determineQuestionType(FillInTheBlankQuestion question)
	{
		if (isShortAnswerQuestion(question))
		{
			return "short_answer_question";
		}
		else if (isDropdownQuestion(question))
		{
			return "multiple_dropdowns_question";
		}
		else
		{
			return "fill_in_multiple_blanks_question";
		}
	}

	/**
	 * Checks whether the question should be treated as a {@code short_answer_question}.
	 * 
	 * @param question the question to determine
	 * @return {@code true} if the question is a short answer question, otherwise {@code false} 
	 */
	private boolean isShortAnswerQuestion(FillInTheBlankQuestion question)
	{
		return question.getTags().size() == 1 && question.getTags().contains("0");
	}
	
	/**
	 * Checks whether the question should be treated as a {@code multiple_dropdowns_question}.
	 * 
	 * @param question the question to determine
	 * @return {@code true} if the question is a drop down question, otherwise {@code false} 
	 */
	private boolean isDropdownQuestion(FillInTheBlankQuestion question)
	{
		for (String tag : question.getTags())
		{
			BlankAnswer blank = question.getAnswer(tag);
			if (blank == null)
				continue;

			if (blank.getDistractors().isEmpty())
			{
				return true;
			}
		}
		return false;
	}
	
}
