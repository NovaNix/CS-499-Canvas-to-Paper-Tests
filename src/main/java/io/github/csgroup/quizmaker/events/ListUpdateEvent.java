package io.github.csgroup.quizmaker.events;

import io.github.csgroup.quizmaker.utils.ListUpdateType;

/**
 * A generic event that represents an event fired due to an update to a list<br>
 * <br>
 * This interface can be applied to any event to allow it to be used generically.
 * @param <T> The type of the object contained in the list
 * 
 * @author Michael Nix
 */
public interface ListUpdateEvent<T>
{

	/**
	 * @return What action was performed on the list
	 */
	public ListUpdateType getAction();
	
	/**
	 * @return What item was added or removed from the list
	 */
	public T getModified();
	
	/**
	 * @return The index of the modified item
	 */
	public int getIndex();
	
}
