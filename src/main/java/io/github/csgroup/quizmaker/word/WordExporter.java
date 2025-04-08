package io.github.csgroup.quizmaker.word;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.*;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion.ResponseLength;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;

/**
 * Responsible for taking a {@link Quiz} and turning it into a .docx file <br>
 * Currently only has support for examples used inside the function, but does
 * write correctly to a .docx file
 * 
 * @author Samuel Garcia 
 */
public class WordExporter 
{
	public static final Logger logger = LoggerFactory.getLogger(WordExporter.class);

	public WordExporter()
	{
		
	}
	
	/**
	 * Manages the exporting of a quiz through LabelWriter and QuestionWriter calls.
	 * @param isKey If the quiz to be exported is a key
	 * @throws IOException If the quiz is unable to be exported.
	 */
	public void exportTest(/*Quiz quiz, Path template, Path destination,*/ boolean isKey) throws IOException //Can only use isKey for now for testing
	{
		try (XWPFDocument document = new XWPFDocument()) 
		{
			LabelWriter labelWriter = new LabelWriter(document);
			QuestionWriter questionWriter = new QuestionWriter(document, false);

			//for (Question q : GeneratedQuiz().quizArray) { Code that will be used once the Generated Quiz function is available
				//labelWriter.write(q.getLabel());
				//questionWriter.write(q, isKey);
			//}

			//These are example labels that should be used to test the label writer
			//These should be removed or moved to a test file when functionality of the label writer has been fully tested
    		
			var example = new Label("This is a test.");
			var example2 = new Label("This is a test2.");
			var example3 = new Label("This is a test3.");
			var multiline = new Label("Line 1\nLine 2\nLine 3");
			var multiline2 = new Label("Line 1\nLine 2\nLine 3");
			var basicHTML = new Label("<p>This should be HTML</p>", Label.Type.html);
			var boldHTML = new Label("<p>This should be <b>bold</b></p>", Label.Type.html);
			var complexHTML = new Label("<p>Mixing <i>and <b>matching</b></i></p>", Label.Type.html);
			var list = new Label("<p>Here is a list:</p>\r\n <ul>\r\n <li>Item 1</li>\r\n <li>Item 2</li>\r\n </ul>", Label.Type.html);
			var pictureURL = new Label("<p>This is a paragraph with an <b>URL</b> image.</p>\r\n <img src=\"https://lh3.googleusercontent.com/pw/AP1GczPmeRoi75Dtry37Stw5IZP6Rmu69s5CkIMfuxTf_gnppZ3IV9pCxF-58rYQ2Oc0sxRa6In1ORqpGzb9LcaxBug3h7Q9WEMiaHjI1ArYmG4Y1gp68LA8aDr7lLig0aZM1x-qj11o-gyJYl9VtHYKAdMb=w517-h919-s-no-gm\"/>\r\n", Label.Type.html);
			var pictureFile = new Label("<p>This is a paragraph with a <b>local</b> image.</p>\r\n <img src=\"" + Paths.get("src","test","resources","testImage.JPG") + "\"/>\r\n", Label.Type.html);
			var table = new Label("<table>\r\n <tr><td>Row 1, Col 1</td><td>Row 1, Col 2</td></tr>\r\n <tr><td>Row 2, Col 1</td><td>Row 2, Col 2</td></tr>\r\n </table>", Label.Type.html);
    		
			labelWriter.write(example);
			labelWriter.write(example2);
			labelWriter.write(multiline);
			labelWriter.write(multiline2);
			labelWriter.write(basicHTML);
			labelWriter.write(boldHTML);
			labelWriter.write(example3);
			labelWriter.write(complexHTML);
			labelWriter.write(list);
			labelWriter.write(pictureURL);
			labelWriter.write(pictureFile);
			labelWriter.write(table);
    		
    		
			// Here are some example questions to test the code with
			// These should be removed or moved to a test file eventually
			
			// Written Response Question
			var writtenResponse = new WrittenResponseQuestion("Written Test", 0);
			writtenResponse.setLabel(new Label("This is a written response"));
			writtenResponse.setAnswer("And this should be the answer!");
			writtenResponse.setResponseLength(ResponseLength.Line);
			questionWriter.writeWrittenResponse(writtenResponse, 1);
			
			// Fill in the Blank Question
	        FillInTheBlankQuestion fitb = new FillInTheBlankQuestion("Java was created by [0].", 5);
	        fitb.setAnswer("0", new BlankAnswer(1, "James Gosling"));
	        questionWriter.writeFillBlank(fitb, 2);
	        
	        // Matching Question
	        MatchingQuestion match = new MatchingQuestion("Q4", "Match Concepts", 4);
	        match.setLabel(new Label("Match the programming concepts to their definitions:"));
	        match.addAnswer(new MatchingAnswer(1, "Encapsulation", "Bundling data with methods"));
	        match.addAnswer(new MatchingAnswer(2, "Inheritance", "Acquiring properties from a parent class"));
	        match.addAnswer(new MatchingAnswer(3, "Abstraction", "Hiding implementation details"));
	        questionWriter.writeMatching(match, 3);
	        
	        // Multiple Choice Question
	        MultipleChoiceQuestion mc = new MultipleChoiceQuestion("Q3", "Java Collection Types", 4);
	        mc.setLabel(new Label("Which of the following are part of the Java Collections Framework?"));
	        mc.addAnswer(new SimpleAnswer(1, "HashMap"), true);
	        mc.addAnswer(new SimpleAnswer(2, "ArrayList"), true);
	        mc.addAnswer(new SimpleAnswer(3, "Thread"), false);
	        mc.addAnswer(new SimpleAnswer(4, "File"), false);
	        questionWriter.writeMultipleChoice(mc, 4);
	        
			try (FileOutputStream out = new FileOutputStream(Paths.get(System.getProperty("user.dir"), "output.docx").toFile())) {
				document.write(out);
			}
			logger.info("Quiz exported successfully to: {}", Paths.get(System.getProperty("user.dir"), "output.docx"));
		} catch (IOException e) {
			logger.error("Failed to export document to: {}", Paths.get(System.getProperty("user.dir"), "output.docx"));
			throw new IOException("Failed to export document to: " + Paths.get(System.getProperty("user.dir"), "output.docx"), e); //This should throw up to the UI
		}
	}
}
