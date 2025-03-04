package io.github.csgroup.quizmaker.utils.stores;

public class DefaultStore<T> extends AbstractStore<T>
{

	private T value;
	
	public DefaultStore(T defaultValue)
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
