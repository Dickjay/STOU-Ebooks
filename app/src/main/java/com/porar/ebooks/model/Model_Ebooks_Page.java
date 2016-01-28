package com.porar.ebooks.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Model_Ebooks_Page implements Comparable<Model_Ebooks_Page>,Serializable{
	private int BID;
	private String Name;
	private int CurrentPage;
	private int customerId;
	private String saveFilePath;
	
	public Model_Ebooks_Page(int customerId,int bookId,String bookName,int pageNumber,String filePath){
		this.BID = bookId;
		this.Name = bookName;
		this.CurrentPage = pageNumber;
		this.customerId = customerId;
		this.saveFilePath = filePath;
	}

	public int compareTo(Model_Ebooks_Page another) {
		if(this.getCurrentPagesNumber() < another.getCurrentPagesNumber()){
			return -1;
		}else if(this.getCurrentPagesNumber() == another.getCurrentPagesNumber()){
			return 0;
		}else{
			return 1;
		}
	}

	public void setBID(int bID) {
		BID = bID;
	}

	public int getBID() {
		return BID;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getName() {
		return Name;
	}

	public void setCurrentPagesNumber(int pagesAmount) {
		CurrentPage = pagesAmount;
	}

	public int getCurrentPagesNumber() {
		return CurrentPage;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	public String getSaveFilePath() {
		return saveFilePath;
	}
}
