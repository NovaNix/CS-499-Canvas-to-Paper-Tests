package io.github.csgroup.quizmaker.qti;

/**
 * This class pairs the quiz assessment file with its corresponding quiz 
 * info file for each quiz data file in the QTI folder. <br>
 * - Quiz Assessment file: contains quiz questions, answers, and scoring policy <br>
 * - Quiz Info file: contains details about the quiz (quiz title and the number of allowed attempts)
 * 
 * @author Sarah Singhirunnusorn
 */
public class QTIDataFileMapping 
{
    
        public final String quizAssessmentFile; 
        public final String quizInfoFile;       
    
        /**
        * Constructs a new QTIDataFileMapping object.
        * 
        * @param quizAssessmentFile The file path to the quiz assessment XML file.
        * @param quizInfoFile The file path to the quiz info XML file.
        */
        public QTIDataFileMapping(String quizAssessmentFile, String quizInfoFile)
        {
                this.quizAssessmentFile = quizAssessmentFile;
                this.quizInfoFile = quizInfoFile;
        }
    
        /**
        * Checks if the quiz data folder contains a quiz info file.
        * 
        * @return true if a quiz info file exists, false otherwise. 
        */
        public boolean hasInfoFile()
        {
                return quizInfoFile != null;
        }
}
