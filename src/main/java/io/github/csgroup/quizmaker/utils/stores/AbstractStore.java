package io.github.csgroup.quizmaker.utils.stores;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStore<T> implements Store<T>
{
	@Override
	public void set(T value)
	{
		setValue(value);
		
		fireChange(value);
	}
	
	protected abstract void setValue(T value);
	
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
