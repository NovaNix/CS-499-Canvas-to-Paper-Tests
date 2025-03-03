package io.github.csgroup.quizmaker.events;

/**
 * 
 * @param <T>
 * 
 * @author Michael Nix
 */
public interface ListUpdateListener<T> 
{

	public void onListUpdate(ListUpdateEvent<T> event);
	
}
