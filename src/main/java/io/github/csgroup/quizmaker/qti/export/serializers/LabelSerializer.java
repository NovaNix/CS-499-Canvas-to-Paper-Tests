package io.github.csgroup.quizmaker.qti.export.serializers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.xml.XMLSerializer;

/**
 * Serializes a {@link Label} as a QTI Material node
 * 
 * @author Michael Nix
 */
public class LabelSerializer implements XMLSerializer<Label>
{

	@Override
	public Element asElement(Document doc, Label label) 
	{
		Element e = doc.createElement("material");
		
		Element mattext = doc.createElement("mattext");
		mattext.setAttribute("texttype", label.getType().getContentType());
		
		// TODO validate that this removes HTML entities
		mattext.appendChild(doc.createTextNode(label.asText()));
		
		e.appendChild(mattext);
		
		return e;
	}

}
