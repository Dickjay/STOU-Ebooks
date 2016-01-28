package com.porar.ebooks.model;

import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

public class Model_SubCategories {
	
	private int CatID = 0;
	private int SCatID = 0;
	private String Name = null;
	private String Detail = null;
	private String PictureUrl = null;
	
	public Model_SubCategories(PListObject plistObject){
		Map<java.lang.String, PListObject> map = ((Dict)plistObject).getConfigMap();
		this.CatID = Integer.parseInt(map.get("CatID").toString());
		this.SCatID = Integer.parseInt(map.get("SCatID").toString());
		this.Name = map.get("Name").toString();
		this.Detail = map.get("Detail").toString();
		this.PictureUrl = map.get("PictureUrl").toString();
	}
	
	public int getCatID() {
		return CatID;
	}

	public int getSCatID() {
		return SCatID;
	}

	public String getName() {
		return Name;
	}

	public String getDetail() {
		return Detail;
	}

	public String getPictureUrl() {
		
			return StaticUtils.escapeHTML(PictureUrl);
		
	}
}
