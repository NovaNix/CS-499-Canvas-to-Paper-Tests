package io.github.csgroup.quizmaker.qti.model.assessment.metadata;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * Represents a single metadata entry within a {@code <qtimetadata>} block.<br>
 * Each field contains a label (key) and an entry (value), which define<br>
 * metadata attributes for a QTI assessment item.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIMetadataField 
{

	private String fieldlabel;
	private String fieldentry;
	
	@XmlElement(name = "fieldlabel", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setFieldlabel(String fieldlabel) 
	{
		this.fieldlabel = fieldlabel;
	}

	@XmlElement(name = "fieldentry", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setFieldentry(String fieldentry) 
	{
		this.fieldentry = fieldentry;
	}

	public String getFieldlabel() 
	{
		return fieldlabel;
	}

	public String getFieldentry()
	{
		return fieldentry;
	}

	@Override
	public String toString() 
	{
		return "QTIMetadataField{" +
				"label='" + fieldlabel + '\'' +
				", entry='" + fieldentry + '\'' +
				'}';
	}
}
