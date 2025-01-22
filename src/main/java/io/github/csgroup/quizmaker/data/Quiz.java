package io.github.csgroup.quizmaker.data;

import io.github.csgroup.quizmaker.data.utils.DataUtils;

public class Quiz 
{

	private final String id;
	private String title;
	
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
	
}
