package io.github.csgroup.quizmaker.qti.export.utils;

import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * A utility parent class for making classes that write XML files. 
 * 
 * @author Michael Nix
 */
public abstract class QTIFileWriter 
{

	public static final Logger logger = LoggerFactory.getLogger(QTIFileWriter.class);
	
	private static final DocumentBuilder builder;
	
	static 
	{
		// Initialize the DocumentBuilder
		// This needs to be split up because builder is final, and it doesn't like to be directly assigned with the exception
		DocumentBuilder b = null;
		
		try 
		{
			b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error("Failed to initialize DocumentBuilderFactory");
			e.printStackTrace();
		}
		
		builder = b;
	}
	
	private Document doc;
	
	public QTIFileWriter()
	{
		this.doc = builder.newDocument();
	}
	
	protected Document getDocument()
	{
		return doc;
	}
	
	/**
	 * Saves the contents of this writer to a file
	 * @param p the path to the file
	 * @throws TransformerException 
	 */
	public void save(Path p) throws TransformerException
	{
		// Code pulled from https://www.baeldung.com/java-xerces-dom-parsing#saving-a-document
		
		DOMSource dom = new DOMSource(doc);
		
	    Transformer transformer = TransformerFactory.newInstance()
	    		.newTransformer();
		
		// Enable 'INDENT' and set the indent amount for the transformer 
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		

	    StreamResult result = new StreamResult(p.toFile());
	    transformer.transform(dom, result);
	}
	
}
