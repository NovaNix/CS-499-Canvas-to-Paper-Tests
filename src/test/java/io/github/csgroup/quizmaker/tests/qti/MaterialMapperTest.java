package io.github.csgroup.quizmaker.tests.qti;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.qti.mapping.MaterialMapper;
import io.github.csgroup.quizmaker.qti.model.assessment.presentation.Material;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Assessment;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Item;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Questestinterop;
import io.github.csgroup.quizmaker.qti.model.assessment.structure.Section;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * This test verifies the functionality of the Material Mapper.
 * 
 * @author Sarah Singhirunnusorn
 */
public class MaterialMapperTest
{
	@Test
	void testParseAndMapMaterialFromRealQTIFile() throws Exception
	{
		// Load and parse the real XML file
		File file = new File("src/test/resources/g9951ec51c6f36aca6d6092ce983e753c.xml");

		JAXBContext context = JAXBContext.newInstance(Questestinterop.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Questestinterop root = (Questestinterop) unmarshaller.unmarshal(file);
		Assessment assessment = root.getAssessment();

		// Avoid shadowing the 'root' variable name
		Section mainSection = assessment.getSections().get(0);
		Item item = mainSection.getItems().get(0); // "What color is an apple?"
		Material material = item.getPresentation().getMaterials().get(0);

		// Use the MaterialMapper
		Label label = MaterialMapper.toLabel(material);

		// Assert output is correct
		assertEquals("<div><p>What color is an apple?</p></div>", label.asText().trim());
		assertEquals(Label.Type.html, label.getType());
	}
}
