package io.github.csgroup.quizmaker.data.utils;

import java.util.UUID;

public final class DataUtils 
{

	private DataUtils() {}
	
	public static String generateId()
	{
		return UUID.randomUUID().toString();
	}
	
}
