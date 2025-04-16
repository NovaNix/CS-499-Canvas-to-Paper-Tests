package io.github.csgroup.quizmaker.qti.export.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents something that can be turned into a QTI XML element
 * 
 * @author Michael Nix
 */
public interface QTISerializable 
{

	/**
	 * Converts this element to a QTI XML element
	 * @param d the XML document
	 * @return this object as an XML element
	 */
	public Element asQTIElement(Document d);
	
}
