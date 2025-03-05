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
	
	/**
	 * Updates all of the listeners for this store with a new value.
	 * @param value the new value of the store
	 */
	protected void fireChange(T value)
	{
		for (var listener : listeners)
		{
			listener.onStoreChange(this, value);
		}
	}
	
}
