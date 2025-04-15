package io.github.csgroup.quizmaker.qti.model.assessment.metadata;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * Represents the {@code <itemmetadata>} section of a QTI assessment item.<br>
 * This class wraps around the {@link QTIMetadata} element which holds metadata fields<br>
 * such as question type, point value, shuffle settings, and other metadata definitions<br>
 * specific to the assessment item.
 * 
 * @author Sarah Singhirunnusorn
 */
public class ItemMetadata 
{

	private QTIMetadata qtimetadata;

	@XmlElement(name = "qtimetadata", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setQtimetadata(QTIMetadata qtimetadata) 
	{
		this.qtimetadata = qtimetadata;
	}

	public QTIMetadata getQtimetadata() 
	{
		return qtimetadata;
	}

	@Override
	public String toString() 
	{
		return "ItemMetadata{" +
				"qtimetadata=" + qtimetadata +
				'}';
	}
}
