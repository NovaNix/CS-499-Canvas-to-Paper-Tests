package io.github.csgroup.quizmaker.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * A Queue of objects that randomly selects the order of objects
 * 
 * @param <T> the type stored inside of the bag
 * @author Michael Nix
 */
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
	
	/**
	 * @return the next object in the bag. This object will be removed from the queue
	 */
	public T next()
	{
		return bag.poll();
	}
	
	/**
	 * @return the next object in the bag, without removing it from the bag
	 */
	public T peek()
	{
		return bag.peek();
	}
	
	
	
}
