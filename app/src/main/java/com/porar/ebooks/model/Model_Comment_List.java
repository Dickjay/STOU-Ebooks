package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

public class Model_Comment_List implements Serializable,Comparable<String> {
	
	private static final long serialVersionUID = 1L;
	private final String Name;
	private final int Rating;
	private final String Comment;
	private final String PictrueUrl;
	private final String UpdateDateTime;
	
	public Model_Comment_List( PListObject plistObject) {
		Map<String, PListObject> map = ((Dict)plistObject).getConfigMap();
		this.Name = map.get("Name").toString();
		this.Rating = Integer.parseInt(map.get("Rating").toString());
		this.Comment = map.get("Comment").toString();
		this.PictrueUrl = map.get("PictureUrl").toString();
		this.UpdateDateTime = map.get("UpdateDateTime").toString();
	}
	public String getName(){
		return this.Name;
	}
	
	public int getRating(){
		return this.Rating;
	}
	
	public String getComment(){
		return this.Comment;
	}
	
	public String getPictrueUrl(){
		return PictrueUrl;
	}
	public String getUpdateDateTime(){
		return this.UpdateDateTime;
	}
	@Override
	public int compareTo(String another) {
		// TODO Auto-generated method stub
		return 0;
	}
}
