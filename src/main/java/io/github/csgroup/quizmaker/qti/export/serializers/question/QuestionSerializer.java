package io.github.csgroup.quizmaker.qti.export.serializers.question;

import io.github.csgroup.quizmaker.data.Question;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A generic interface for serializing a {@link Question} into a QTI {@code <item>} XML element.
 * <p>
 * Implementations of this interface convert each {@code Question} subclass into a valid QTI<br>
 * item element, including metadata, prompt, and response declarations.
 * 
 * @author Sarah Singhirunnusorn
 */
public interface QuestionSerializer<T extends Question>
{
	/**
	 * Converts the given question into a QTI {@code <item>} XML element.
	 * 
	 * @param d the XML document
	 * @param question the question object
	 * @return the generated {@code <item>} XML element
	 */
	Element serialize(Document d, T question);
}
