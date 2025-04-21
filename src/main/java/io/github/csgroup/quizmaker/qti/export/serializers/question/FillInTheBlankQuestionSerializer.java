package io.github.csgroup.quizmaker.qti.export.serializers.question;

import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.qti.export.serializers.LabelSerializer;
import java.util.List;
import java.util.Locale;
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

		// <presentation> block
		Element presentation = d.createElement("presentation");

		// Add the main prompt
		Element promptMaterial = labelSerializer.asElement(d, question.getLabel());
		presentation.appendChild(promptMaterial);

		// Create <response_lid> blocks for dropdowns, or <response_str> for short answers
		for (String tag : question.getTags())
		{
			BlankAnswer answer = question.getAnswer(tag);
			if (answer == null)
				continue;

			String safeTag = tag.replaceAll("\\W+", "_"); // Sanitize tag for identifier

			// If distractors exist → it's a dropdown-style blank
			if (!answer.getDistractors().isEmpty())
			{
				Element response = d.createElement("response_lid");
				response.setAttribute("ident", "response_" + safeTag);
				response.setAttribute("rcardinality", "Single");

				Element material = d.createElement("material");
				Element mattext = d.createElement("mattext");
				mattext.setTextContent(tag);
				material.appendChild(mattext);
				response.appendChild(material);

				Element renderChoice = d.createElement("render_choice");

				for (var option : answer.getAllOptions())
				{
					Element choice = d.createElement("response_label");
					choice.setAttribute("ident", option.asText());

					Element mat = d.createElement("material");
					Element mattextOption = d.createElement("mattext");
					mattextOption.setAttribute("texttype", "text/plain");
					mattextOption.setTextContent(option.asText());
					mat.appendChild(mattextOption);

					choice.appendChild(mat);
					renderChoice.appendChild(choice);
				}

				response.appendChild(renderChoice);
				presentation.appendChild(response);
			}
			else
			{
				// If no distractors → it's a short answer
				Element response = d.createElement("response_str");
				response.setAttribute("ident", "response_" + safeTag);
				response.setAttribute("rcardinality", "Single");

				Element renderFib = d.createElement("render_fib");
				Element responseLabel = d.createElement("response_label");
				responseLabel.setAttribute("ident", "answer_" + safeTag);
				responseLabel.setAttribute("rshuffle", "No");

				renderFib.appendChild(responseLabel);
				response.appendChild(renderFib);
				presentation.appendChild(response);
			}
		}

		item.appendChild(presentation);

		// <resprocessing> block
		Element resprocessing = d.createElement("resprocessing");

		Element outcomes = d.createElement("outcomes");
		Element decvar = d.createElement("decvar");
		decvar.setAttribute("maxvalue", "100");
		decvar.setAttribute("minvalue", "0");
		decvar.setAttribute("varname", "SCORE");
		decvar.setAttribute("vartype", "Decimal");
		outcomes.appendChild(decvar);
		resprocessing.appendChild(outcomes);

		// Add <respcondition> per blank
		List<String> tags = question.getTags();
		double perBlank = question.getPoints() / (double) tags.size();

		for (String tag : tags)
		{
			BlankAnswer answer = question.getAnswer(tag);
			if (answer == null)
				continue;

			String safeTag = tag.replaceAll("\\W+", "_");

			Element respcondition = d.createElement("respcondition");
			respcondition.setAttribute("continue", "No");

			Element conditionvar = d.createElement("conditionvar");

			// Accept all correct values (distractor + correct)
			for (var option : answer.getAllOptions())
			{
				Element varequal = d.createElement("varequal");
				varequal.setAttribute("respident", "response_" + safeTag);
				varequal.setTextContent(option.asText());
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
}
