package io.github.csgroup.quizmaker.qti.export.serializers.question;

import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.questions.MatchingQuestion;
import io.github.csgroup.quizmaker.qti.export.serializers.LabelSerializer;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Serializes a {@link MatchingQuestion} into a QTI {@code <item>} XML element.
 * 
 * @author Sarah Singhirunnusorn 
 */
public class MatchingQuestionSerializer implements QuestionSerializer<MatchingQuestion>
{
	private final LabelSerializer labelSerializer = new LabelSerializer();

	/**
	 * Converts a {@link MatchingQuestion} into a {@code <item>} XML structure. 
	 * 
	 * @param d the XML document
	 * @param question the question object
	 * @return the generated {@code <item>}
	 */
	@Override
	public Element serialize(Document d, MatchingQuestion question)
	{
		Element item = d.createElement("item");
		item.setAttribute("ident", question.getId());
		item.setAttribute("title", question.getTitle());

		// Metadata Block
		Element itemMetadata = d.createElement("itemmetadata");
		Element qtiMetadata = d.createElement("qtimetadata");

		Element questionType = d.createElement("qtimetadatafield");
		Element typeLabel = d.createElement("fieldlabel");
		typeLabel.setTextContent("question_type");
		Element typeEntry = d.createElement("fieldentry");
		typeEntry.setTextContent("matching_question");
		questionType.appendChild(typeLabel);
		questionType.appendChild(typeEntry);

		Element points = d.createElement("qtimetadatafield");
		Element pointsLabel = d.createElement("fieldlabel");
		pointsLabel.setTextContent("points_possible");
		Element pointsEntry = d.createElement("fieldentry");
		pointsEntry.setTextContent(String.format("%.2f", question.getPoints()));
		points.appendChild(pointsLabel);
		points.appendChild(pointsEntry);

		Element idrefField = d.createElement("qtimetadatafield");
		Element idrefLabel = d.createElement("fieldlabel");
		idrefLabel.setTextContent("assessment_question_identifierref");
		Element idrefEntry = d.createElement("fieldentry");
		idrefEntry.setTextContent(question.getId());
		idrefField.appendChild(idrefLabel);
		idrefField.appendChild(idrefEntry);

		qtiMetadata.appendChild(questionType);
		qtiMetadata.appendChild(points);
		qtiMetadata.appendChild(idrefField);
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

		// Right-side values
		Set<String> rightTexts = new LinkedHashSet<>();
		for (MatchingAnswer answer : question.getAnswers()) 
		{
			rightTexts.add(answer.getRight().asText());
		}

		Map<String, String> rightChoiceIds = new LinkedHashMap<>();
		int rightId = 1000;
		for (String rightText : rightTexts) 
		{
			rightChoiceIds.put(rightText, String.valueOf(rightId));
			rightId++;
		}

		Element sharedRenderChoice = d.createElement("render_choice");
		for (Map.Entry<String, String> entry : rightChoiceIds.entrySet()) 
		{
			Element choice = d.createElement("response_label");
			choice.setAttribute("ident", entry.getValue());

			Element mat = d.createElement("material");
			Element mattextChoice = d.createElement("mattext");
			mattextChoice.setTextContent(entry.getKey());
			mat.appendChild(mattextChoice);
			choice.appendChild(mat);
			sharedRenderChoice.appendChild(choice);
		}

		// Left-side response_lids
		Map<String, String> correctMatches = new LinkedHashMap<>();
		for (MatchingAnswer answer : question.getAnswers()) 
		{
			String leftId = "response_" + answer.getId();
			String leftText = answer.getLeft().asText();
			String rightText = answer.getRight().asText();

			Element responseLid = d.createElement("response_lid");
			responseLid.setAttribute("ident", leftId);
			responseLid.setAttribute("rcardinality", "Single");

			Element material = d.createElement("material");
			Element mattextLeft = d.createElement("mattext");
			mattextLeft.setAttribute("texttype", "text/plain");
			mattextLeft.setTextContent(leftText);
			material.appendChild(mattextLeft);
			responseLid.appendChild(material);

			responseLid.appendChild(sharedRenderChoice.cloneNode(true));
			presentation.appendChild(responseLid);

			String correctRightId = rightChoiceIds.get(rightText);
			correctMatches.put(leftId, correctRightId);
		}
		item.appendChild(presentation);

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

		double perMatch = question.getPoints() / (double) correctMatches.size();
		for (Map.Entry<String, String> match : correctMatches.entrySet()) 
		{
			Element respcondition = d.createElement("respcondition");

			Element conditionvar = d.createElement("conditionvar");
			Element varequal = d.createElement("varequal");
			varequal.setAttribute("respident", match.getKey());
			varequal.setTextContent(match.getValue());
			conditionvar.appendChild(varequal);

			Element setvar = d.createElement("setvar");
			setvar.setAttribute("action", "Add");
			setvar.setTextContent(String.format("%.2f", perMatch));

			respcondition.appendChild(conditionvar);
			respcondition.appendChild(setvar);
			resprocessing.appendChild(respcondition);
		}
		item.appendChild(resprocessing);

		return item;
	}
	
}
