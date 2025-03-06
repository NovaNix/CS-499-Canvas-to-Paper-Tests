package io.github.csgroup.quizmaker.utils.stores.readable;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.utils.stores.StoreListener;

/**
 * A default abstract implementation of a {@link ReadableStore}. Useful for writing implementations of ReadableStores.<br>
 * <br>
 * Automatically handles adding and removing listeners, and has a utility function for firing change events. 
 * @param <T> the type of value stored inside
 * @author Michael Nix
 */
public abstract class AbstractReadableStore<T> implements ReadableStore<T>
{
	
	@Override
	public abstract T get();
	
	protected List<StoreListener<T>> listeners = new ArrayList<StoreListener<T>>();
	
	@Override
	public void addListener(StoreListener<T> listener)
	{
		this.listeners.add(listener);
	}
	
	@Override
	public void removeListener(StoreListener<T> listener)
	{
		this.listeners.remove(listener);
	}
	
	@Override
	public int getListenerCount()
	{
		return this.listeners.size();
	}
	
	/**
	 * Updates all of the listeners for this store with a new value.
	 * @param value the new value of the store
	 */
	protected synchronized void fireChange(T value)
	{
		// We make a copy of the list to allow for listeners to unsubscribe during the event call
		// This is kinda inefficient, it could be made more efficient by putting unsubscribe requests to a buffer
		// and then removing them from the buffer once we're done looping through, but this might cause threading issues
		// For now this shouldn't be much of a performance hit, but it's something we should look into if we run into problems
		// TODO consider optimizing
		for (var listener : new ArrayList<StoreListener<T>>(listeners))
		{
			listener.onStoreChange(this, value);
		}
	}
	
}
