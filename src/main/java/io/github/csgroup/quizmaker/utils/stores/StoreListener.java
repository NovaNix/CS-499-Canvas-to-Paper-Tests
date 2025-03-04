package io.github.csgroup.quizmaker.utils.stores;

public interface StoreListener<T> 
{
	public void onStoreChange(Store<T> store, T value);
}
