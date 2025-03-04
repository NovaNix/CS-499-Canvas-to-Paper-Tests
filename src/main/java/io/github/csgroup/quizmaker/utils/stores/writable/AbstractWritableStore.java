package io.github.csgroup.quizmaker.utils.stores.writable;

import io.github.csgroup.quizmaker.utils.stores.readable.AbstractReadableStore;

public abstract class AbstractWritableStore<T> extends AbstractReadableStore<T> implements WritableStore<T>
{
	@Override
	public void set(T value)
	{
		setValue(value);
		
		fireChange(value);
	}
	
	protected abstract void setValue(T value);
}
