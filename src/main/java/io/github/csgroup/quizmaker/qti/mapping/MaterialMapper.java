package io.github.csgroup.quizmaker.qti.mapping;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.MatText;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.Material;
import io.github.csgroup.quizmaker.utils.HTMLUtils;
import java.util.List;

/**
 * Maps QTI {@link Material} and {@link MatText} elements to the {@link Label} object.
 * <p>
 * This class handles conversion of QTI material content, including decoding HTML/XML entities<br>
 * and translating the {@code texttype} attribute into the appropriate {@link Label.Type}.
 * <p>
 * Usage:
 * <pre>
 *     Label label = MaterialMapper.toLabel(material);
 * </pre>
 * 
 * @author Sarah Singhirunnusorn
 */
public class MaterialMapper
{
	
	/**
	 * Converts a QTI {@link Material} element into a {@link Label}.<br>
	 * If the material contains multiple {@link MatText} elements, their contents are joined with a newline.
	 * 
	 * @param material the QTI Material element to convert
	 * @return a {@link Label} representing the material’s content, or an empty Label if null or empty
	 */
	public static Label toLabel(Material material)
	{
		if (material == null || material.getMattext() == null || material.getMattext().isEmpty())
		{
			return new Label("");
		}

		List<MatText> matTexts = material.getMattext();

		// Concatenate all mattext values with newlines
		StringBuilder contentBuilder = new StringBuilder();
		String texttype = null;

		for (MatText matText : matTexts)
		{
			if (matText == null || matText.getText() == null)
				continue;

			if (texttype == null && matText.getTexttype() != null)
			{
				texttype = matText.getTexttype();
			}

			if (contentBuilder.length() > 0)
			{
				contentBuilder.append("\n");
			}

			String decoded = HTMLUtils.removeEntities(matText.getText());
			contentBuilder.append(decoded);
		}

		Label.Type labelType = determineLabelType(texttype);
		return new Label(contentBuilder.toString(), labelType);
	}

	/**
	 * Converts a QTI {@link MatText} element directly into a {@link Label}.
	 * 
	 * @param matText the QTI MatText element to convert
	 * @return a {@link Label} representing the text content, or an empty Label if null
	 */
	public static Label toLabel(MatText matText)
	{
		if (matText == null || matText.getText() == null)
		{
			return new Label("");
		}

		String decoded = HTMLUtils.removeEntities(matText.getText());
		Label.Type labelType = determineLabelType(matText.getTexttype());

		return new Label(decoded, labelType);
	}

	/**
	 * Determines the {@link Label.Type} based on a given QTI {@code texttype} string.
	 * 
	 * @param texttype the {@code <texttype>} attribute from a {@link MatText} element
	 * @return the corresponding {@link Label.Type}, defaulting to {@code plain} if unknown
	 */
	private static Label.Type determineLabelType(String texttype)
	{
		if ("text/html".equalsIgnoreCase(texttype))
		{
			return Label.Type.html;
		}
		else
		{
			return Label.Type.plain;
		}
	}
}