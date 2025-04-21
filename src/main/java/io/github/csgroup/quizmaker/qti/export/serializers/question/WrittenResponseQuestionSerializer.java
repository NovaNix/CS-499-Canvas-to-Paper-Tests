package io.github.csgroup.quizmaker.qti.export.serializers.question;

import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.qti.export.serializers.LabelSerializer;
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
		Element item = d.createElement("item");
		item.setAttribute("ident", question.getId());
		item.setAttribute("title", question.getTitle());

		// <itemmetadata>
		Element itemMetadata = d.createElement("itemmetadata");
		Element qtiMetadata = d.createElement("qtimetadata");
		appendMetadataField(d, qtiMetadata, "question_type", "essay_question");
		appendMetadataField(d, qtiMetadata, "points_possible", String.valueOf(question.getPoints()));
		itemMetadata.appendChild(qtiMetadata);
		item.appendChild(itemMetadata);

		// <presentation>
		Element presentation = d.createElement("presentation");

		// <material>: prompt
		Element promptMaterial = labelSerializer.asElement(d, question.getLabel());
		presentation.appendChild(promptMaterial);

		// <response_str>: free response input
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
		item.appendChild(presentation);

		// <resprocessing>
		Element resprocessing = d.createElement("resprocessing");

		Element outcomes = d.createElement("outcomes");
		Element decVar = d.createElement("decvar");
		decVar.setAttribute("maxvalue", "100");
		decVar.setAttribute("minvalue", "0");
		decVar.setAttribute("varname", "SCORE");
		decVar.setAttribute("vartype", "Decimal");
		outcomes.appendChild(decVar);
		resprocessing.appendChild(outcomes);

		// Optional feedback block if instructor wrote an answer
		if (hasUserAnswer(question))
		{
			Element respCondition = d.createElement("respcondition");
			respCondition.setAttribute("continue", "Yes");

			Element conditionVar = d.createElement("conditionvar");
			conditionVar.appendChild(d.createElement("other"));
			respCondition.appendChild(conditionVar);

			Element feedback = d.createElement("displayfeedback");
			feedback.setAttribute("feedbacktype", "Response");
			feedback.setAttribute("linkrefid", "general_fb");
			respCondition.appendChild(feedback);

			resprocessing.appendChild(respCondition);
		}

		// Always include base respcondition to finalize processing
		Element baseRespCondition = d.createElement("respcondition");
		baseRespCondition.setAttribute("continue", "No");

		Element baseConditionVar = d.createElement("conditionvar");
		baseConditionVar.appendChild(d.createElement("other"));
		baseRespCondition.appendChild(baseConditionVar);

		resprocessing.appendChild(baseRespCondition);
		item.appendChild(resprocessing);

		// <itemfeedback> block if answer is provided
		if (hasUserAnswer(question))
		{
			Element feedback = d.createElement("itemfeedback");
			feedback.setAttribute("ident", "general_fb");

			Element flowMat = d.createElement("flow_mat");
			Element material = d.createElement("material");

			Element mattext = d.createElement("mattext");
			mattext.setAttribute("texttype", "text/html");
			mattext.setTextContent(question.getAnswer().asText());

			material.appendChild(mattext);
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
