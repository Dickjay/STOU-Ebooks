package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

public class Model_Publisher implements Comparable<Model_Publisher>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int CID = 0;
	private String Name = null;
	private String Company = null;
	private String Website = null;
	private String Image = null;
	private int CounteBooks = 0;
	private String Detail = null;
	private int Fans = 0;

	public Model_Publisher(PListObject plistObject) {
		super();

		Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();
		this.CID = Integer.parseInt(map.get("CID").toString());
		this.Name = map.get("Name").toString();
		this.Company = map.get("Company").toString();
		this.Website = map.get("Website").toString();
		this.Image = map.get("Image").toString();
		this.CounteBooks = Integer.parseInt(map.get("CounteBooks").toString());
		this.Detail = map.get("Detail").toString();
		this.Fans = Integer.parseInt(map.get("Fans").toString());
	}

	public void setCID(int CID) {
		this.CID = CID;
	}

	public int getCID() {
		return this.CID;
	}

	public int getFans() {
		return this.Fans;
	}

	public String getName() {
		return this.Name;
	}

	public String getCompany() {
		return this.Company;
	}

	public String getWebsite() {
		return this.Website;
	}

	public String getImage() {

		return StaticUtils.escapeHTML(Image);

	}

	public void setImage(String Image) {
		this.Image = Image;
	}

	public int getCounteBooks() {
		return this.CounteBooks;
	}

	public String getDetail() {
		return this.Detail;
	}

	@Override
	public int compareTo(Model_Publisher another) {
		int compareCID = another.getCID();

		// descending order //return compareCID - this.CID;
		// ascending order
		return this.CID - compareCID;
	}

	public static Comparator<Model_Publisher> comparator_Name_ASC = new Comparator<Model_Publisher>() {

		@Override
		public int compare(Model_Publisher model1, Model_Publisher model2) {

			String Name1 = model1.getName().toUpperCase();
			String Name2 = model2.getName().toUpperCase();

			// descending order //return Name2.compareTo(Name1);
			// ascending order
			return Name1.compareTo(Name2);
		}
	};

}
