package io.github.csgroup.quizmaker.utils.stores;

public interface Store<T>
{

	public T get();
	public void set(T value);
	
	public void addListener(StoreListener<T> listener);
	public void removeListener(StoreListener<T> listener);
	
}
