package com.porar.ebooks.event;


public interface OnFetchURLListener {
	public abstract void onFetchStart(String apiURL,int currentIndex);
	public abstract void onFetchComplete(String apiURL,int currentIndex,String result);
	public abstract void onFetchError(String apiURL,int currentIndex,Exception e);
	public abstract void onTimeOut(String apiURL,int currentIndex);
	public abstract void onAllTaskDone();
}
