package io.github.csgroup.quizmaker.events;

/**
 * Represents an object that has a list that can be listened to for changes.
 * @param <T> The type of object inside of the list
 */
public interface ListListenable<T> 
{

	/**
	 * Subscribes a new listener to changes to this list
	 * @param listener the new listener
	 */
	public void addListListener(ListUpdateListener<T> listener);
	
	/**
	 * Unsubscribes a listener from changes to this list
	 * @param listener the listener to remove
	 */
	public void removeListListener(ListUpdateListener<T> listener);
	
}
