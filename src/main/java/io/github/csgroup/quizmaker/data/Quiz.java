package io.github.csgroup.quizmaker.data;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.data.quiz.BankSelection;
import io.github.csgroup.quizmaker.data.quiz.GeneratedQuiz;
import io.github.csgroup.quizmaker.data.utils.DataUtils;

/**
 * A dynamically generated quiz that pulls {@link Question Questions} from {@link QuestionBank QuestionBanks}.<br>
 * 
 * This is not completely implemented. Do not use yet. 
 * 
 * @author Michael Nix
 */
public class Quiz 
{

	private final String id;
	private String title;
	
	private final QuestionBank internalQuestions;
	
	private List<BankSelection> banks = new ArrayList<BankSelection>();
	
	public Quiz(String title)
	{
		this.id = DataUtils.generateId();
		this.title = title;
		
		this.internalQuestions = new QuestionBank(title + "-Questions");
	}
	
	public Quiz(String id, String title)
	{
		this.id = id;
		this.title = title;
		
		this.internalQuestions = new QuestionBank(title + "-Questions");
	}
	
	public GeneratedQuiz generate()
	{
		return null;
	}
	
//	public boolean addBank(String id)
//	{
//		return banks.add(id);
//	}
//	
//	public boolean addBank(QuestionBank bank)
//	{
//		return banks.add(bank.getId());
//	}
	
	public void setTitle(String title)
	{
		this.title = title;
		
		this.internalQuestions.setTitle(title + "-Questions");
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getId()
	{
		return id;
	}
	
	@Override
	public String toString()
	{
		return title;
	}
}
