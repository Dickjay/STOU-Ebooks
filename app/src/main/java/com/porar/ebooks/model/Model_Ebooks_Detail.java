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

public class Model_Ebooks_Detail implements Serializable, Comparable<Model_Ebooks_Detail> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int BID;
	private final String Publisher;
	private final String Name;
	private final String Writers;
	private final String ISBN;
	private final String Detail;
	private final String PublishDate;
	private String PrintOutDate;
	private final int Pages;
	private final float FileSize;
	private final int NumRatings;
	private final float Rating;
	private final float ApplePrice;
	private final String Price;
	private final String Cover;
	private final String CoverM;
	private final String CoverL;
	private final String MoreImage1;
	private final String MoreImage2;
	private final String MoreImage1L;
	private final String MoreImage2L;
	private final int CID;
	private int CatID;
	private final String PublisherName;
	private final String CategoryName;
	private String SubCategoryName;
	private final String DisplayWithImage;
	private final String Logo;
	private boolean AlreadyInCart;
	private final int SRID;
	private final String Quota;
	private final float ListPrice;
	private final float OurPrice;
	private final float SeriesPrice;
	private String datetime;
	private String BCode = "";

	public Model_Ebooks_Detail(PListObject plistObject) {
		Map<java.lang.String, PListObject> map = ((Dict) plistObject).getConfigMap();
		this.BID = Integer.parseInt(map.get("BID").toString());
		this.Publisher = map.get("Publisher").toString();
		this.Name = map.get("Name").toString();
		this.Writers = map.get("Writers").toString();
		this.ISBN = map.get("ISBN").toString();
		this.Detail = map.get("Detail").toString();
		this.PublishDate = map.get("PublishDate").toString();
		this.PrintOutDate = map.get("PrintOutDate").toString();
		this.Pages = Integer.parseInt(map.get("Pages").toString());
		this.FileSize = Float.parseFloat(map.get("FileSize").toString());
		this.NumRatings = Integer.parseInt(map.get("NumRatings").toString());
		this.Rating = Float.parseFloat(map.get("Rating").toString());
		this.ApplePrice = Float.parseFloat(map.get("ApplePrice").toString());
		this.Price = map.get("Price").toString();
		this.Cover = map.get("Cover").toString();
		this.CoverM = map.get("CoverM").toString();
		this.CoverL = map.get("CoverL").toString();
		this.MoreImage1 = map.get("MoreImage1").toString();
		this.MoreImage2 = map.get("MoreImage2").toString();
		this.MoreImage1L = map.get("MoreImage1L").toString();
		this.MoreImage2L = map.get("MoreImage2L").toString();
		// if(this.Cover=="" || this.Cover==null){
		// this.Cover = this.CoverM;
		// }
		this.SRID = Integer.parseInt(map.get("SRID").toString());
		this.CID = Integer.parseInt(map.get("CID").toString());
		this.CatID = Integer.parseInt(map.get("CatID").toString());
		this.PublisherName = map.get("PublisherName").toString();
		this.CategoryName = map.get("CategoryName").toString();
		this.SubCategoryName = map.get("SubCategoryName").toString();
		this.DisplayWithImage = map.get("DisplayWithImage").toString();
		this.Logo = map.get("Logo").toString();
		this.AlreadyInCart = Boolean.parseBoolean(map.get("AlreadyInCart").toString());
		this.Quota = map.get("Quota").toString();
		this.SeriesPrice = Float.parseFloat(map.get("SeriesPrice").toString());
		this.ListPrice = Float.parseFloat(map.get("ListPrice").toString());
		this.OurPrice = Float.parseFloat(map.get("OurPrice").toString());
		try{
			this.BCode = map.get("BCode").toString();
		}catch (NullPointerException e){
			this.BCode = "";
			e.printStackTrace();
		}

	}

	public String getBCode() {
		return BCode;
	}

	public void setBCode(String BCode) {
		this.BCode = BCode;
	}

	public int getBID() {
		return BID;
	}

	public float getSeriesPrice() {
		return SeriesPrice;
	}

	public float getListPrice() {
		return ListPrice;
	}

	public float getOurPrice() {
		return OurPrice;
	}

	public String getQuota() {
		return Quota;
	}

	public String getPublisher() {
		return Publisher;
	}

	public int getNumRatings() {
		return NumRatings;
	}

	public String getName() {
		return Name;
	}

	public float getRating() {
		return Rating;
	}

	public String getPrice() {
		return Price;
	}

	public String getCover() {
		
			return StaticUtils.escapeHTML(Cover);
		
	}

	public String getWriters() {
		return Writers;
	}

	public String getISBN() {
		return ISBN;
	}

	public String getDetail() {
		return Detail;
	}

	public String getPublishDate() {
		return PublishDate;
	}

	public int getPages() {
		return Pages;
	}

	public float getFileSize() {
		return FileSize;
	}

	public float getApplePrice() {
		return ApplePrice;
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

	public String getCoverM() {
		return StaticUtils.escapeHTML(CoverM);
	}

	public String getCoverL() {
		return StaticUtils.escapeHTML(CoverL);
	}

	public String getMoreImage1() {
		return StaticUtils.escapeHTML(MoreImage1);
	}

	public String getMoreImage2() {
		return StaticUtils.escapeHTML(MoreImage2);
	}

	public String getMoreImage1L() {
		return StaticUtils.escapeHTML(MoreImage1L);
	}

	public String getMoreImage2L() {
		return StaticUtils.escapeHTML(MoreImage2L);
	}

	@Override
	public int compareTo(Model_Ebooks_Detail another) {
		if (this.getBID() < another.getBID()) {
			return -1;
		} else if (this.getBID() == another.getBID()) {
			return 0;
		} else {
			return 1;
		}
	}

	public boolean getAlreadyInCart() {
		return this.AlreadyInCart;
	}

	public void setAlreadyInCart(boolean AlreadyInCart) {
		this.AlreadyInCart = AlreadyInCart;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public String getSubCategoryName() {
		return SubCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		SubCategoryName = subCategoryName;
	}

	public String getPrintOutDate() {
		return PrintOutDate;
	}

	public void setPrintOutDate(String printOutDate) {
		PrintOutDate = printOutDate;
	}

	public int getCatID() {
		return CatID;
	}

	public void setCatID(int catID) {
		CatID = catID;
	}

	public int getSRID() {
		return SRID;
	}

	public String getDateTime() {
		return datetime;
	}

	@SuppressLint("SimpleDateFormat")
	public void setDateTime() {
		Date date = new Date();
		SimpleDateFormat df2 = new SimpleDateFormat();
//		String pattern = "yyyy-MM-dd";
//		df2.applyPattern(pattern);

		this.datetime = df2.format(date);
		Log.v("", "/" + datetime);
	}

}
