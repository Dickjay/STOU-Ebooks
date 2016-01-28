package com.porar.ebooks.event;

import plist.xml.PList;

public interface OnLoadResultFromHttpPost {
	public abstract void  complateResult(String result);
	public abstract void  complateResultPlist(PList plist);
	
}
