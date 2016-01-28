package com.porar.ebooks.event;

import plist.xml.PList;

public interface OnFetchAPIListener {
	
	public abstract void onFetchStart(String apiURL,int currentIndex);
	public abstract void onFetchComplete(String apiURL,int currentIndex,PList result);
	public abstract void onFetchError(String apiURL,int currentIndex,Exception e);
	public abstract void onTimeOut(String apiURL,int currentIndex);
	public abstract void onAllTaskDone();
	
}
