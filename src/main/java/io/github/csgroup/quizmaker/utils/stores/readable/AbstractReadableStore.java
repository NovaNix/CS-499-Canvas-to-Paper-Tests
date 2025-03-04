package io.github.csgroup.quizmaker.utils.stores.readable;

import java.util.ArrayList;
import java.util.List;

import io.github.csgroup.quizmaker.utils.stores.StoreListener;

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
	
	protected void fireChange(T value)
	{
		for (var listener : listeners)
		{
			listener.onStoreChange(this, value);
		}
	}
	
}
