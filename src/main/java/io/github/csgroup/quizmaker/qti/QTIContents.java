package io.github.csgroup.quizmaker.qti;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.QuestionBank;
import io.github.csgroup.quizmaker.data.Quiz;

/**
 * A container for the contents extracted from a QTI file
 * 
 * @author Michael Nix
 */
public class QTIContents 
{
	public final List<QuestionBank> banks = new ArrayList<QuestionBank>();
	public final List<Quiz> quizzes = new ArrayList<Quiz>();
        
        /**
         * Adds a quiz to the QTI contents.
         * 
         * @param mapping The QTI data file mapping that contains the file 
         * paths to the quiz assessment and quiz info
         */
        public void addQuiz(QTIDataFileMapping mapping)
        {
                // TODO parse quiz title and question bank title from the mapping
            
                String quizTitle = "Default Quiz Title";    // Placeholder
                String bankTitle = "Default Question Bank"; // Placeholder
            
                Quiz quiz = new Quiz(quizTitle);
                quizzes.add(quiz);
            
                QuestionBank bank = new QuestionBank(bankTitle);
                banks.add(bank);
        }
        
        /**
         * Retrieves a list of quizzes extracted from the QTI file.
         * 
         * @return A list of Quiz objects.
         */
        public List<Quiz> getQuizzes() 
        {
                return quizzes;
        }
        
        /**
         * Retrieves a list of question banks extracted from the QTI file.
         * 
         * @return A list of QuestionBank objects.
         */
        public List<QuestionBank> getQuestionBanks() 
        {
                return banks;
        }
        
	// TODO add additional information extracted from the file if necessary
	
	// TODO consider adding statistics or debugging info (number of questions loaded, number of sections, etc)
	
	// TODO add helper methods 
	
	// TODO consider converting this to a record
}
