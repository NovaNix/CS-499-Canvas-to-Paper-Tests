package io.github.csgroup.quizmaker.qti;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.csgroup.quizmaker.data.Label;
import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;
import io.github.csgroup.quizmaker.data.answers.BlankAnswer;
import io.github.csgroup.quizmaker.data.answers.MatchingAnswer;
import io.github.csgroup.quizmaker.data.answers.SimpleAnswer;
import io.github.csgroup.quizmaker.data.questions.FillInTheBlankQuestion;
import io.github.csgroup.quizmaker.data.questions.MatchingQuestion;
import io.github.csgroup.quizmaker.data.questions.MultipleChoiceQuestion;
import io.github.csgroup.quizmaker.data.questions.WrittenResponseQuestion;
import io.github.csgroup.quizmaker.data.quiz.BankSelection;

/**
 * An object responsible for reading QTI files and extracting the information contained inside
 * 
 * @author 
 */
public class QTIReader 
{

	public static final Logger logger = LoggerFactory.getLogger(QTIReader.class);
	
	public QTIReader()
	{
		
	}
	
	public QTIContents readFile(Path path)
	{
		
		// TODO implement
		return getTempTestContents();
	}
	
	private QTIContents getTempTestContents()
	{
		// Temporary QTI contents return
		
		QTIContents contents = new QTIContents();
		
		// Test Banks
		
		QuestionBank bank1 = new QuestionBank("Test Bank 1");
		
		var writtenResponse = new WrittenResponseQuestion("Written Test", 0);
		writtenResponse.setLabel(new Label("This is a written response"));
		writtenResponse.setAnswer("And this should be the answer!");
		
		bank1.add(writtenResponse);
		
		var fitbQuestion = new FillInTheBlankQuestion("FITB Question", 0);
		fitbQuestion.setLabel(new Label("Fill in the [b] question! Do your [2]!"));
		fitbQuestion.setAnswer("b", new BlankAnswer(0, "blank"));
		fitbQuestion.setAnswer("2", new BlankAnswer(1, "best"));
		
		bank1.add(fitbQuestion);
		
		QuestionBank bank2 = new QuestionBank("Test Bank 2");
		
		var multipleChoice = new MultipleChoiceQuestion("Multichoice", 0);
		multipleChoice.setLabel(new Label("What is rotten?"));
		multipleChoice.addAnswer(new SimpleAnswer(0, "Egg"), true);
		multipleChoice.addAnswer(new SimpleAnswer(1, "Potato"), false);
		multipleChoice.addAnswer(new SimpleAnswer(2, "Cookie Dough"), false);
		multipleChoice.addAnswer(new SimpleAnswer(3, "Snow"), false);
		
		bank2.add(multipleChoice);
		
		contents.banks.add(bank1);
		contents.banks.add(bank2);
		
		// Test Quizzes
		
		Quiz testQuiz = new Quiz("Test Quiz 1");
		
		var matching = new MatchingQuestion("Matching Test", 2.0f);
		matching.setLabel(new Label("Match the following parts:"));
		matching.addAnswer(new MatchingAnswer(0, "Banana", "Potassium"));
		matching.addAnswer(new MatchingAnswer(1, "Potato", "Potato"));
		matching.addAnswer(new MatchingAnswer(2, "Cat", "Meow"));
		
		testQuiz.addQuestion(matching);
		
		// Add a bank to the quiz
		
		testQuiz.addBank(new BankSelection(bank1, 2, 2.0f));
		
		contents.quizzes.add(testQuiz);
		
		return contents;
	}
	
}
