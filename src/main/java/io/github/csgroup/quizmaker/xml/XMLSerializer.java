package io.github.csgroup.quizmaker.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A converter that takes a Java object and converts it into an XML element
 * @param <T> the object type to be converted
 * @author Michael Nix
 */
public interface XMLSerializer<T> 
{

	/**
	 * Converts a Java object into an XML element
	 * @param doc the XML document
	 * @param obj the object to convert
	 * @return the object as an XML element
	 */
	public Element asElement(Document doc, T obj);
	
}
