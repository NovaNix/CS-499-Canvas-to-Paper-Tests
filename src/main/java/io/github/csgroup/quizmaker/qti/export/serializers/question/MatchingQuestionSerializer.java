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
		
		// Add <itemmetadata> with question_type and points_possible
		Element itemMetadata = d.createElement("itemmetadata");
		Element qtiMetadata = d.createElement("qtimetadata");

		// question_type = matching_question
		Element questionType = d.createElement("qtimetadatafield");
		Element typeLabel = d.createElement("fieldlabel");
		typeLabel.setTextContent("question_type");
		Element typeEntry = d.createElement("fieldentry");
		typeEntry.setTextContent("matching_question");
		questionType.appendChild(typeLabel);
		questionType.appendChild(typeEntry);

		// points_possible = total points
		Element points = d.createElement("qtimetadatafield");
		Element pointsLabel = d.createElement("fieldlabel");
		pointsLabel.setTextContent("points_possible");
		Element pointsEntry = d.createElement("fieldentry");
		pointsEntry.setTextContent(String.format("%.2f", question.getPoints()));
		points.appendChild(pointsLabel);
		points.appendChild(pointsEntry);

		// Assemble and attach metadata
		qtiMetadata.appendChild(questionType);
		qtiMetadata.appendChild(points);
		itemMetadata.appendChild(qtiMetadata);
		item.appendChild(itemMetadata);

		// ==========================
		// <presentation> block
		// ==========================
		Element presentation = d.createElement("presentation");

		// Add the question prompt
		Element promptMaterial = labelSerializer.asElement(d, question.getLabel());
		presentation.appendChild(promptMaterial);

		// Extract right-side unique options
		Set<String> rightTexts = new LinkedHashSet<>();
		for (MatchingAnswer answer : question.getAnswers())
		{
			rightTexts.add(answer.getRight().asText());
		}

		// Assign a unique ID to each right-side value
		Map<String, String> rightChoiceIds = new LinkedHashMap<>();
		int rightId = 1000;
		for (String rightText : rightTexts)
		{
			rightChoiceIds.put(rightText, String.valueOf(rightId));
			rightId++;
		}

		// Build a reusable <render_choice> block containing all right-side values
		Element sharedRenderChoice = d.createElement("render_choice");
		for (Map.Entry<String, String> entry : rightChoiceIds.entrySet())
		{
			Element choice = d.createElement("response_label");
			choice.setAttribute("ident", entry.getValue());

			Element mat = d.createElement("material");
			Element mattext = d.createElement("mattext");
			mattext.setTextContent(entry.getKey());

			mat.appendChild(mattext);
			choice.appendChild(mat);
			sharedRenderChoice.appendChild(choice);
		}

		// Map left-side prompt IDs to their correct right-side match IDs
		Map<String, String> correctMatches = new LinkedHashMap<>();

		// Create a response_lid for each left-side match
		for (MatchingAnswer answer : question.getAnswers())
		{
			String leftId = "response_" + answer.getId();
			String leftText = answer.getLeft().asText();
			String rightText = answer.getRight().asText();

			Element responseLid = d.createElement("response_lid");
			responseLid.setAttribute("ident", leftId);
			responseLid.setAttribute("rcardinality", "Single");

			Element material = d.createElement("material");
			Element mattext = d.createElement("mattext");
			mattext.setAttribute("texttype", "text/plain");
			mattext.setTextContent(leftText);
			material.appendChild(mattext);
			responseLid.appendChild(material);

			// Clone the shared render_choice block for each <response_lid>
			responseLid.appendChild(sharedRenderChoice.cloneNode(true));
			presentation.appendChild(responseLid);

			// Store correct response for scoring
			String correctRightId = rightChoiceIds.get(rightText);
			correctMatches.put(leftId, correctRightId);
		}

		item.appendChild(presentation);

		// ==========================
		// <resprocessing> block
		// ==========================
		Element resprocessing = d.createElement("resprocessing");

		// <outcomes>
		Element outcomes = d.createElement("outcomes");
		Element decvar = d.createElement("decvar");
		decvar.setAttribute("maxvalue", "100");
		decvar.setAttribute("minvalue", "0");
		decvar.setAttribute("varname", "SCORE");
		decvar.setAttribute("vartype", "Decimal");
		outcomes.appendChild(decvar);
		resprocessing.appendChild(outcomes);

		// Add a <respcondition> for each correct match
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
