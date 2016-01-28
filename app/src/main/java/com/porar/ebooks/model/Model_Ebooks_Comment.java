package com.porar.ebooks.model;

import java.util.Hashtable;

public class Model_Ebooks_Comment{
	private String name;
	private String rating;
	private String dateTime;
	private String display;
	private String blockquote;
	
	public Model_Ebooks_Comment(Hashtable<String, String> item){
		name = item.get("name");
		rating = item.get("rating");
		dateTime = item.get("time");
		display = item.get("display");
		blockquote = item.get("blockquote");
	}

	public String getName() {
		return name;
	}

	public String getRating() {
		return rating;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getDisplay() {
		return display;
	}
	
	public String getBlockquote() {
		return blockquote;
	}
}
