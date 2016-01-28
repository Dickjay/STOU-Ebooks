package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

public class Model_Publisher_Detail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int CID;
	private final String Name;
	private final String Image;
	private final String eBooks;
	private final String Downloads;
	private final String Fans;
	private final String Detail;
	private final Boolean AlreadyFan;
	private String Url;

	public Model_Publisher_Detail(PListObject plistObject) {
		Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();
		this.CID = Integer.parseInt(map.get("CID").toString());
		this.Name = map.get("Name").toString();
		this.Image = map.get("Image").toString();
		this.eBooks = map.get("eBooks").toString();
		this.Downloads = map.get("Downloads").toString();
		this.Fans = map.get("Fans").toString();
		this.Detail = map.get("Detail").toString();
		this.AlreadyFan = Boolean.parseBoolean(map.get("AlreadyFan").toString());
		this.Url = map.get("Url").toString();
	}

	public int getCID() {
		return this.CID;
	}

	public String getName() {
		return this.Name;
	}

	public String getImage() {

		return StaticUtils.escapeHTML(Image);

	}

	public String geteBooks() {
		return this.eBooks;
	}

	public String getDownloads() {
		return this.Downloads;
	}

	public String getFans() {
		return this.Fans;
	}

	public String getDetail() {
		return this.Detail;
	}

	public Boolean getAlreadyFan() {
		return this.AlreadyFan;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		Url = Url.substring(Url.indexOf("/"), Url.length());
		return Url;
	}

	/**
	 * @param url
	 *            the url to set
	 */

}
