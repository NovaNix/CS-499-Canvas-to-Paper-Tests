package io.github.csgroup.quizmaker.qti.model.assessment.metadata;

import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Represents the {@code<qtimetadata>} element within an assessment item.<br>
 * This class holds a list of {@link QTIMetadataField} elements, each of which <br>
 * defines a key-value pair of metadata associated with the assessment item.
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIMetadata
{
	
	private List<QTIMetadataField> qtimetadatafield;

	@XmlElement(name = "qtimetadatafield", namespace = "http://www.imsglobal.org/xsd/ims_qtiasiv1p2")
	public void setQtimetadatafield(List<QTIMetadataField> qtimetadatafield) 
	{
		this.qtimetadatafield = qtimetadatafield;
	}

	public List<QTIMetadataField> getQtimetadatafield() 
	{
		return qtimetadatafield;
	}
	
	@Override
	public String toString() 
	{
		return "QTIMetadata{" +
				"fields=" + qtimetadatafield +
				'}';
	}
}
