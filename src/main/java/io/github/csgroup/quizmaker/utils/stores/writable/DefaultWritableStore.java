package io.github.csgroup.quizmaker.utils.stores.writable;

public class DefaultWritableStore<T> extends AbstractWritableStore<T>
{

	private T value;
	
	public DefaultWritableStore(T defaultValue)
	{
		this.value = defaultValue;
	}
	
	@Override
	protected void setValue(T value) 
	{
		this.value = value;
	}

	@Override
	public T get() 
	{
		return value;
	}

}
