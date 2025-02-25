package io.github.csgroup.quizmaker.data.labels;

import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * A basic text component of a {@link Label}. 
 * 
 * @author Michael Nix
 */
public class LabelText extends LabelPart
{

	private String contents;
	private Type type = Type.plain;
	
	public LabelText(String text)
	{
		this.contents = text;
	}
	
	public LabelText(String text, Type type)
	{
		this.contents = text;
		this.type = type;
	}
	
	@Override
	public String asText() 
	{
		return contents;
	}
	
	public String getText()
	{
		return contents;
	}
	
	public Type getType()
	{
		return type;
	}
	
	@Override
	public LabelPart clone() 
	{	
		return new LabelText(contents, type);
	}
	
	public enum Type
	{
		plain, html
	}

	@Override
	public Document getDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getParentElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeSet getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStartOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEndOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getElementIndex(int offset) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getElementCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Element getElement(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
