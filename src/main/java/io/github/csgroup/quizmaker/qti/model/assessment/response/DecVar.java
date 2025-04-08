package io.github.csgroup.quizmaker.qti.model.assessment.response;

import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * Represents the {@code <decvar>} element in QTI, which defines a declared variable<br>
 * used for scoring or tracking outcomes.
 * <p>
 * Typically used in {@code <resprocessing>} blocks to declare points or grading logic.
 *
 * @author Sarah Singhirunnusorn
 */
public class DecVar 
{
	
	private Double maxValue;
	private Double minValue;
	private String varName;
	private String varType;

	@XmlAttribute(name = "maxvalue")
	public void setMaxValue(Double maxValue) 
	{
		this.maxValue = maxValue;
	}

	@XmlAttribute(name = "minvalue")
	public void setMinValue(Double minValue) 
	{
		this.minValue = minValue;
	}

	@XmlAttribute(name = "varname")
	public void setVarName(String varName) 
	{
		this.varName = varName;
	}

	@XmlAttribute(name = "vartype")
	public void setVarType(String varType) 
	{
		this.varType = varType;
	}

	public Double getMaxValue() 
	{
		return maxValue;
	}

	public Double getMinValue() 
	{
		return minValue;
	}

	public String getVarName() 
	{
		return varName;
	}

	public String getVarType() 
	{
		return varType;
	}

	@Override
	public String toString() 
	{
		return "DecVar{" +
				"maxValue=" + maxValue +
				", minValue=" + minValue +
				", varName='" + varName + '\'' +
				", varType='" + varType + '\'' +
				'}';
	}
}
