package io.github.csgroup.quizmaker.data;

import io.github.csgroup.quizmaker.data.utils.DataUtils;

/**
 * A dynamically generated quiz that pulls {@link Question Questions} from {@link QuestionBank QuestionBanks}.<br>
 * 
 * This is not completely implemented. Do not use yet. 
 */
public class Quiz 
{

	private final String id;
	private String title;
	
	/** A list of ids of {@link QuestionBank QuestionBanks} to use to generate the quiz */
	//private List<String> banks; 
	
	public Quiz(String title)
	{
		this.id = DataUtils.generateId();
		this.title = title;
	}
	
	public Quiz(String id, String title)
	{
		this.id = id;
		this.title = title;
	}
	
	public void regenerate()
	{
		
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
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getId()
	{
		return id;
	}
}
