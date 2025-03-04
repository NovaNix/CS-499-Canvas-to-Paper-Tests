package io.github.csgroup.quizmaker.utils.stores.readable;

import io.github.csgroup.quizmaker.utils.stores.StoreListener;

public interface ReadableStore<T>
{

	public T get();
	
	public void addListener(StoreListener<T> listener);
	public void removeListener(StoreListener<T> listener);
	
}
