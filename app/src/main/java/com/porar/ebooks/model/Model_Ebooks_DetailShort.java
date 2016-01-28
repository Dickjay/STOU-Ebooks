package com.porar.ebooks.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;
import android.annotation.SuppressLint;
import android.util.Log;

import com.porar.ebooks.utils.StaticUtils;

public class Model_Ebooks_DetailShort implements Serializable, Comparable<Model_Ebooks_DetailShort> {
	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	final int BID;
	final String Publisher;
	final String Name;
	final String Writers;
	final int Pages;
	final float FileSize;
	final float Rating;
	final int CID;
	final String PublisherName;
	final String DisplayWithImage;
	final String Logo;

	String datetime;

	public Model_Ebooks_DetailShort(PListObject plistObject) {
		Map<java.lang.String, PListObject> map = ((Dict) plistObject).getConfigMap();
		this.BID = Integer.parseInt(map.get("BID").toString());
		this.Publisher = map.get("Publisher").toString();
		this.Name = map.get("Name").toString();
		this.Writers = map.get("Writers").toString();
		this.Pages = Integer.parseInt(map.get("Pages").toString());
		this.FileSize = Float.parseFloat(map.get("FileSize").toString());
		this.Rating = Float.parseFloat(map.get("Rating").toString());
		this.CID = Integer.parseInt(map.get("CID").toString());
		this.PublisherName = map.get("PublisherName").toString();
		this.DisplayWithImage = map.get("DisplayWithImage").toString();
		this.Logo = map.get("Logo").toString();
	}

	public int getBID() {
		return BID;
	}

	public String getPublisher() {
		return Publisher;
	}

	public String getName() {
		return Name;
	}

	public float getRating() {
		return Rating;
	}

	public String getWriters() {
		return Writers;
	}

	public int getPages() {
		return Pages;
	}

	public float getFileSize() {
		return FileSize;
	}

	public int getCID() {
		return CID;
	}

	public String getPublisherName() {
		return PublisherName;
	}

	public String getDisplayWithImage() {
		return DisplayWithImage;
	}

	public String getLogo() {
		
			return StaticUtils.escapeHTML(Logo);
		
	}

	@Override
	public int compareTo(Model_Ebooks_DetailShort another) {
		if (this.getBID() < another.getBID()) {
			return -1;
		} else if (this.getBID() == another.getBID()) {
			return 0;
		} else {
			return 1;
		}
	}

	public String getDateTime() {
		return datetime;
	}

	@SuppressLint("SimpleDateFormat")
	public void setDateTime() {
		Date date = new Date();
		SimpleDateFormat df2 = new SimpleDateFormat();
		// String pattern = "yyyy-MM-dd";
		// df2.applyPattern(pattern);

		this.datetime = df2.format(date);
		Log.v("", "/" + datetime);
	}

}
