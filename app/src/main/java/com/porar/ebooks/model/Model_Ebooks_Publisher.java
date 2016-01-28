package com.porar.ebooks.model;

import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

public class Model_Ebooks_Publisher implements Comparable<Model_Ebooks_Publisher>{
	private int CID;
	private String name;
	private String company;
	private String website;
	private String image;
	private int CounteBooks;
	private String detail;
	
	public Model_Ebooks_Publisher(PListObject plistObject){
		Map<java.lang.String, PListObject> map = ((Dict)plistObject).getConfigMap();
		this.CID = Integer.parseInt(map.get("CID").toString());
		this.name = map.get("Name").toString();
		this.company = map.get("Company").toString();
		this.website = map.get("Website").toString();
		this.image = map.get("Image").toString();
		this.CounteBooks = Integer.parseInt( map.get("CounteBooks").toString() );
		this.detail = map.get("Detail").toString();
	}

	public int getCID() {
		return CID;
	}

	public String getName() {
		return name;
	}

	public String getCompany() {
		return company;
	}

	public String getWebsite() {
		return website;
	}

	public String getImage() {
		return image;
	}

	public int getCounteBooks() {
		return CounteBooks;
	}

	public String getDetail() {
		return detail;
	}

	public int compareTo(Model_Ebooks_Publisher another) {
		if(this.getCID() < another.getCID()){
			return -1;
		}else if(this.getCID() == another.getCID()){
			return 0;
		}else{
			return 1;
		}
	}
	
}
