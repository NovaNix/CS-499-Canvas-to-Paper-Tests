package io.github.csgroup.quizmaker.qti.export.serializers.question;

import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.qti.export.serializers.LabelSerializer;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Serializes a {@link MultipleChoiceQuestion} into a QTI {@code <item>} element.
 * <p>
 * This includes the question prompt, answer choices, and correct response(s).<br>
 * Used for multiple choice, true/false, and multiple answers question types.
 * 
 * @author Sarah Singhirunnusorn
 */
public class MultipleChoiceQuestionSerializer implements QuestionSerializer<MultipleChoiceQuestion> 
{
	private final LabelSerializer labelSerializer = new LabelSerializer();
	
	/**
	 * Converts a {@link MultipleChoiceQuestion} into a {@code <item>} XML element.
	 * @param d the XML document
	 * @param question the question object 
	 * @return the generated {@code <item>} element 
	 */
	@Override
	public Element serialize(Document d, MultipleChoiceQuestion question)
	{
		Element item = d.createElement("item");
		item.setAttribute("ident", question.getId());
		item.setAttribute("title", question.getTitle());

		// <presentation> block
		Element presentation = d.createElement("presentation");

		// Add question prompt
		Element promptMaterial = labelSerializer.asElement(d, question.getLabel());
		presentation.appendChild(promptMaterial);

		// <response_lid> with answer choices
		Element response = d.createElement("response_lid");
		response.setAttribute("ident", "response1");

		// Determine cardinality: Single or Multiple
		boolean isMultiple = question.getCorrectAnswers().size() > 1;
		response.setAttribute("rcardinality", isMultiple ? "Multiple" : "Single");

		Element renderChoice = d.createElement("render_choice");

		for (SimpleAnswer answer : question.getAnswers())
		{
			Element choice = d.createElement("response_label");
			choice.setAttribute("ident", String.valueOf(answer.getId()));

			Element material = d.createElement("material");
			Element mattext = d.createElement("mattext");
			mattext.setAttribute("texttype", "text/html");
			mattext.setTextContent("<div><p>" + answer.getLabel().asText() + "</p></div>");

			material.appendChild(mattext);
			choice.appendChild(material);
			renderChoice.appendChild(choice);
		}

		response.appendChild(renderChoice);
		presentation.appendChild(response);
		item.appendChild(presentation);

		// <resprocessing> block
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

		// <respcondition> for correct answers
		Element respcondition = d.createElement("respcondition");
		respcondition.setAttribute("continue", "No");

		Element conditionvar = d.createElement("conditionvar");

		List<SimpleAnswer> correctAnswers = question.getCorrectAnswers();

		if (correctAnswers.size() == 1)
		{
			// Single correct answer → <varequal>
			Element varequal = d.createElement("varequal");
			varequal.setAttribute("respident", "response1");
			varequal.setTextContent(String.valueOf(correctAnswers.get(0).getId()));
			conditionvar.appendChild(varequal);
		}
		else if (!correctAnswers.isEmpty())
		{
			// Multiple correct answers → <and> block
			Element and = d.createElement("and");

			for (SimpleAnswer answer : correctAnswers)
			{
				Element varequal = d.createElement("varequal");
				varequal.setAttribute("respident", "response1");
				varequal.setTextContent(String.valueOf(answer.getId()));
				and.appendChild(varequal);
			}

			// Prevent incorrect answers using <not> (if needed)
			List<SimpleAnswer> incorrectAnswers = question.getAnswers();
			incorrectAnswers.removeAll(correctAnswers);

			for (SimpleAnswer answer : incorrectAnswers)
			{
				Element not = d.createElement("not");
				Element varequal = d.createElement("varequal");
				varequal.setAttribute("respident", "response1");
				varequal.setTextContent(String.valueOf(answer.getId()));
				not.appendChild(varequal);
				and.appendChild(not);
			}

			conditionvar.appendChild(and);
		}

		respcondition.appendChild(conditionvar);

		Element setvar = d.createElement("setvar");
		setvar.setAttribute("action", "Set");
		setvar.setTextContent("100");
		respcondition.appendChild(setvar);

		resprocessing.appendChild(respcondition);
		item.appendChild(resprocessing);

		return item;
	}
}
