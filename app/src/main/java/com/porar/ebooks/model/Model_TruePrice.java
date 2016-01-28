package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

@SuppressWarnings("serial")
public class Model_TruePrice implements Serializable {

	/**
	 * 
	 * <key>BID</key><string>9307</string>
	 * <key>Name</key><string>จอมใจแห่งชีค</string>
	 * <key>Price</key><string>209</string>
	 * <key>ListPrice</key><string>300</string>
	 * <key>OurPrice</key><string>209</string>
	 * 
	 */
	private String BID;

	private final String Name;
	private final String Price;
	private final String ListPrice;
	private final String OurPrice;

	public Model_TruePrice(PListObject plistObject) {
		Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();

		this.Name = map.get("Name").toString();
		this.Price = map.get("Price").toString();
		this.ListPrice = map.get("ListPrice").toString();
		this.OurPrice = map.get("OurPrice").toString();

	}

	/**
	 * @return the bID
	 */
	public String getBID() {
		return BID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return Price;
	}

	/**
	 * @return the listPrice
	 */
	public String getListPrice() {
		return ListPrice;
	}

	/**
	 * @return the ourPrice
	 */
	public String getOurPrice() {
		return OurPrice;
	}

}
