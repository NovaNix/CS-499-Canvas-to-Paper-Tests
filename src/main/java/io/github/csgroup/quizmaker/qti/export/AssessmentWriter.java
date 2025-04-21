package io.github.csgroup.quizmaker.qti.export;

import io.github.csgroup.quizmaker.data.Question;
import org.w3c.dom.Document;

import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.qti.export.serializers.question.QuestionSerializer;
import io.github.csgroup.quizmaker.qti.export.utils.QTIFileWriter;
import io.github.csgroup.quizmaker.qti.export.utils.QuestionSerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Converts a {@link Quiz} into a QTI assessment file
 * 
 * @author Michael Nix, Sarah Singhirunnusorn
 */
public class AssessmentWriter extends QTIFileWriter
{
	private static final Logger logger = LoggerFactory.getLogger(AssessmentWriter.class);
	private Quiz quiz;
	
	public AssessmentWriter(Quiz q)
	{
		super();
		
		this.quiz = q;
		
		writeAssessment(getDocument(), quiz);
	}
	
	/**
	 * Writes the assessment file to the document
	 * @param d the document to write to
	 * @param q the quiz to write the assessment data of
	 * @author Sarah Singhirunnusorn
	 */
	private void writeAssessment(Document d, Quiz q)
	{
		// <questestinterop> root element
		Element root = d.createElement("questestinterop");
		root.setAttribute("xmlns", "http://www.imsglobal.org/xsd/ims_qtiasiv1p2");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://www.imsglobal.org/xsd/ims_qtiasiv1p2 http://www.imsglobal.org/xsd/ims_qtiasiv1p2p1.xsd");
		d.appendChild(root);

		// <assessment> inside <questestinterop>
		Element assessment = d.createElement("assessment");
		assessment.setAttribute("ident", q.getId());
		assessment.setAttribute("title", q.getTitle());
		root.appendChild(assessment);

		// <section> block
		Element section = d.createElement("section");
		section.setAttribute("ident", "root_section");
		assessment.appendChild(section);
		
		// Serialize each question
		for (Question question : q.getQuestions())
		{
			QuestionSerializer<Question> serializer = (QuestionSerializer<Question>) QuestionSerializerFactory.getSerializer(question);
			if (serializer != null)
			{
				Element item = serializer.serialize(d, question);
				section.appendChild(item);
			}
			else
			{
				logger.warn("No serializer found for question type: {}", question.getClass().getSimpleName());
			}
		}
	}
}
