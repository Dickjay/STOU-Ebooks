package com.porar.ebooks.model;

import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

public class Model_Top50 {

	private final int BID ;
	private final String Publisher;
	private final String Name;
	private final int NumRatings;
	private final float Rating;
	private final String Price;
	private final String Cover;
	private final boolean AwardFlag;
	
	public Model_Top50(PListObject plistObject) {
		Map<java.lang.String, PListObject> map = ((Dict)plistObject).getConfigMap();
		this.BID = Integer.parseInt(map.get("BID").toString());
		this.Publisher = map.get("Publisher").toString();
		this.Name = map.get("Name").toString();
		this.NumRatings = Integer.parseInt(map.get("NumRatings").toString());
		this.Rating = Float.parseFloat(map.get("Rating").toString());
		this.Price = map.get("Price").toString();
		this.Cover = map.get("Cover").toString();
		this.AwardFlag = Boolean.parseBoolean(map.get("AwardFlag").toString());
	}
	public int getBID(){
		return this.BID;
	}
	
	public String getPublisher(){
		return this.Publisher;
	}
	
	public String getName(){
		return this.Name;
	}
	
	public int getNumRatings(){
		return this.NumRatings;
	}
	
	public float getRating(){
		return this.Rating;
	}
	
	public String getPrice(){
		return this.Price;
	}
	
	public String getCover(){
		
			return StaticUtils.escapeHTML(Cover);
	
	}
	
	public boolean getAwardFlag(){
		return this.AwardFlag;
	}

}
