package io.github.csgroup.quizmaker.qti.export.serializers.question;

import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.qti.export.serializers.LabelSerializer;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Serializes a {@link WrittenResponseQuestion} into a QTI {@code <item>} XML element.
 * 
 * @author Sarah Singhirunnusorn
 */
public class WrittenResponseQuestionSerializer implements QuestionSerializer<WrittenResponseQuestion>
{
	private final LabelSerializer labelSerializer = new LabelSerializer();

	/**
	 * Converts a {@link WrittenResponseQuestion} into a {@code <item>} XML structure. 
	 * 
	 * @param d the XML document
	 * @param question the question object
	 * @return the generated {@code <item>}
	 */
	@Override
    public Element serialize(Document d, WrittenResponseQuestion question) 
	{
		String answerText = question.getAnswer() != null ? question.getAnswer().asText().trim() : "";

		boolean isRange = answerText.matches("(?i).*\\d+\\.?\\d*\\s*(to|-)\\s*\\d+\\.?\\d*.*");
		boolean isExact = answerText.matches("^\\d+(\\.\\d+)?(\\s+or\\s+\\d+(\\.\\d+)?)*$");
		boolean isCombo = answerText.contains("or") && answerText.contains("to");
		boolean isNumeric = isExact || isRange || isCombo;

		Element item = d.createElement("item");
		item.setAttribute("ident", question.getId());
		item.setAttribute("title", question.getTitle());

		// Metadata Block
		Element itemMetadata = d.createElement("itemmetadata");
		Element qtiMetadata = d.createElement("qtimetadata");
		appendMetadataField(d, qtiMetadata, "question_type", isNumeric ? "numerical_question" : "essay_question");
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

		Element response;
		if (isNumeric) 
		{
			response = d.createElement("response_num");
			response.setAttribute("ident", "response1");
			response.setAttribute("rcardinality", "Single");

			Element renderFib = d.createElement("render_fib");
			renderFib.setAttribute("fibtype", "Decimal");
			response.appendChild(renderFib);
		} 
		else 
		{
			response = d.createElement("response_str");
			response.setAttribute("ident", "response1");
			response.setAttribute("rcardinality", "Single");

			Element renderFib = d.createElement("render_fib");
			Element responseLabel = d.createElement("response_label");
			responseLabel.setAttribute("ident", "answer1");
			responseLabel.setAttribute("rshuffle", "No");
			renderFib.appendChild(responseLabel);
			response.appendChild(renderFib);
		}
		presentation.appendChild(response);
		item.appendChild(presentation);

		// Resprocessing Block
		Element resprocessing = d.createElement("resprocessing");
		Element outcomes = d.createElement("outcomes");
		Element decVar = d.createElement("decvar");
		decVar.setAttribute("maxvalue", "100");
		decVar.setAttribute("minvalue", "0");
		decVar.setAttribute("varname", "SCORE");
		decVar.setAttribute("vartype", "Decimal");
		outcomes.appendChild(decVar);
		resprocessing.appendChild(outcomes);

		if (isNumeric && !answerText.isBlank()) 
		{
			Element respCondition = d.createElement("respcondition");
			respCondition.setAttribute("continue", "No");

			Element conditionVar = d.createElement("conditionvar");

			if (isCombo) 
			{
				Element orBlock = d.createElement("or");

				// Exact Answer Format
				String exact = answerText.split("or")[0].trim();
				Element varequal = d.createElement("varequal");
				varequal.setAttribute("respident", "response1");
				varequal.setTextContent(exact);
				orBlock.appendChild(varequal);

				// Range Answer Format
				Matcher m = Pattern.compile("(?i)(?:between\\s+)?(\\d+(\\.\\d+)?)\\s*(to|and|-)\\s*(\\d+(\\.\\d+)?)").matcher(answerText);
				if (m.find()) 
				{
					Element and = d.createElement("and");

					Element gte = d.createElement("vargte");
					gte.setAttribute("respident", "response1");
					gte.setTextContent(m.group(1));

					Element lte = d.createElement("varlte");
					lte.setAttribute("respident", "response1");
					lte.setTextContent(m.group(4));

					and.appendChild(gte);
					and.appendChild(lte);
					orBlock.appendChild(and);
				}

				conditionVar.appendChild(orBlock);
			} 
			else if (isRange) 
			{
				Matcher m = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(to|-)\\s*(\\d+(\\.\\d+)?)").matcher(answerText);
				if (m.find()) 
				{
					Element gte = d.createElement("vargte");
					gte.setAttribute("respident", "response1");
					gte.setTextContent(m.group(1));

					Element lte = d.createElement("varlte");
					lte.setAttribute("respident", "response1");
					lte.setTextContent(m.group(4));

					conditionVar.appendChild(gte);
					conditionVar.appendChild(lte);
				}
			} 
			else 
			{ 
				for (String value : answerText.split("or")) 
				{
					Element varequal = d.createElement("varequal");
					varequal.setAttribute("respident", "response1");
					varequal.setTextContent(value.trim());
					conditionVar.appendChild(varequal);
				}
			}

			Element setVar = d.createElement("setvar");
			setVar.setAttribute("action", "Set");
			setVar.setTextContent("100");

			respCondition.appendChild(conditionVar);
			respCondition.appendChild(setVar);
			resprocessing.appendChild(respCondition);
		}
		item.appendChild(resprocessing);

		// For essay answers -> itemfeedback block
		if (!isNumeric && hasUserAnswer(question)) 
		{
			Element feedback = d.createElement("itemfeedback");
			feedback.setAttribute("ident", "general_fb");

			Element flowMat = d.createElement("flow_mat");
			Element material = d.createElement("material");

			Element mattextFeedback = d.createElement("mattext");
			mattextFeedback.setAttribute("texttype", "text/html");
			mattextFeedback.setTextContent(question.getAnswer().asText());

			material.appendChild(mattextFeedback);
			flowMat.appendChild(material);
			feedback.appendChild(flowMat);
			item.appendChild(feedback);
		}

		return item;
	}

	/**
	 * Utility method to check whether the user has provided an answer comment.
	 * 
	 * @param question the written response question
	 * @return true if the answer is non-null and non-blank
	 */
	private boolean hasUserAnswer(WrittenResponseQuestion question)
	{
		return question.getAnswer() != null && !question.getAnswer().asText().isBlank();
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
	
}
