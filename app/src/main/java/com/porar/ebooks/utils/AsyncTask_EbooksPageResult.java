package com.porar.ebooks.utils;

public class AsyncTask_EbooksPageResult {
	private int bookId = 0;
	private String bookName = "";
	private int currentPage = 0;
	private String savePath = "";
	
	public AsyncTask_EbooksPageResult(int bookid,String bookname,int currentpage,String savepath){
		this.bookId = bookid;
		this.bookName = bookname;
		this.currentPage = currentpage;
		this.savePath = savepath;
	}

	public int getBookId() {
		return bookId;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public String getSavePath() {
		return savePath;
	}

	public String getBookName() {
		return bookName;
	}
}
