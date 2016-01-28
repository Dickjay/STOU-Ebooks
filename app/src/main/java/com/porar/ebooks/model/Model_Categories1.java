package com.porar.ebooks.model;

import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

public class Model_Categories1 {

	private int CatID = 0;
	private int SCatID = 0;
	private String Name = "เอกสารการสอน";
	private String Detail = "หนังสือทั้งหมดทุกหมวดในคลังหนังสือ";
	private String PictureURL = "images/allcat.png";

	public Model_Categories1() {
		// TODO Auto-generated constructor stub
	}

	public Model_Categories1(PListObject plistObject) {
		Map<java.lang.String, PListObject> map = ((Dict) plistObject).getConfigMap();
		this.CatID = java.lang.Integer.parseInt(map.get(CatID).toString());
		this.SCatID = java.lang.Integer.parseInt(map.get(SCatID).toString());
		this.Name = map.get(Name).toString();
		this.Detail = map.get(Detail).toString();
		this.PictureURL = map.get(PictureURL).toString();
	}

	public void setCatID(int catID) {
		this.CatID = catID;
	}

	public int getCatID() {
		return this.CatID;
	}

	public void setSCatID(int sCatID) {
		this.SCatID = sCatID;
	}

	public int getSCatID() {
		return this.SCatID;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getName() {
		return this.Name;
	}

	public void setDetail(String detail) {
		this.Detail = detail;
	}

	public String getDetail() {
		return this.Detail;
	}

	public void setPictureURL(String pictureURL) {
		this.PictureURL = pictureURL;
	}

	public String getPictureURL() {

		return this.PictureURL;
	}
}