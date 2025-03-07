package io.github.csgroup.quizmaker.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class RandomBag<T> 
{
	
	private Queue<T> bag;
	
	public RandomBag(List<T> contents)
	{
		this(contents, new Random());
	}
	
	public RandomBag(List<T> contents, Random random)
	{
		List<T> shuffled = new ArrayList<T>(contents); // We need a copy of the list to avoid shuffling the original
		Collections.shuffle(shuffled, random);
		
		bag = new ArrayDeque<T>(shuffled);
	}
	
	public T next()
	{
		return bag.poll();
	}
	
	public T peek()
	{
		return bag.peek();
	}
	
	
	
}
