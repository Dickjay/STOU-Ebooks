package com.porar.ebooks.model;

import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

public class Model_SubScribe  {

	private final  String SRID;
	private final  String Name;
	private final  String Detail;
	private final  String ListQuota;
	private final  String AddedQuota;
	private final  String PRCode;
	private final  String Note;
	private final  String ApplePrice;
	private final  String Price;
	private final  String OurPrice;
	private final  String ListPrice;

	public Model_SubScribe(PListObject plistObject) {
		Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();

		this.SRID = map.get("SRID").toString();
		this.Name = map.get("Name").toString();
		this.Detail = map.get("Detail").toString();
		this.ListQuota = map.get("ListQuota").toString();
		this.AddedQuota = map.get("AddedQuota").toString();
		this.PRCode = map.get("PRCode").toString();
		this.Note = map.get("Note").toString();
		this.ApplePrice = map.get("ApplePrice").toString();
		this.Price = map.get("Price").toString();
		this.OurPrice = map.get("OurPrice").toString();
		this.ListPrice = map.get("ListPrice").toString();

	}

	/**
	 * @return the sRID
	 */
	public String getSRID() {
		return SRID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return Detail;
	}

	/**
	 * @return the listQuota
	 */
	public String getListQuota() {
		return ListQuota;
	}

	/**
	 * @return the leftQuota
	 */
	public String getAddedQuota() {
		return AddedQuota;
	}

	/**
	 * @return the pRCode
	 */
	public String getPRCode() {
		return PRCode;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return Note;
	}

	/**
	 * @return the applePrice
	 */
	public String getApplePrice() {
		return ApplePrice;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return Price;
	}

	/**
	 * @return the ourPrice
	 */
	public String getOurPrice() {
		return OurPrice;
	}

	/**
	 * @return the listPrice
	 */
	public String getListPrice() {
		return ListPrice;
	}

	

}
