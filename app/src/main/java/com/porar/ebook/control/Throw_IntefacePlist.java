package com.porar.ebook.control;

import plist.xml.PList;
import android.app.ProgressDialog;

public interface  Throw_IntefacePlist {
	public abstract void PList(PList resultPlist,ProgressDialog pd);
	public abstract void PList_Detail_Comment(PList Plistdetail,PList Plistcomment,ProgressDialog pd);
	public abstract void PList_TopPeriod(PList Plist1,PList Plist2,ProgressDialog pd);
	
	public abstract void StartLoadPList();
}
