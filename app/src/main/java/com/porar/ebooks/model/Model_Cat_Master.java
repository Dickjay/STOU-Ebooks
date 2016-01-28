package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

public class Model_Cat_Master implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <key>NID</key><string>1</string> <key>Topic</key><string>THAILAND NO.1
	 * BESTSELLER</string> <key>ImageFile</key><string>hilight-1.jpg</string>
	 * <key>UpdateDateTime</key><string>15 มิ.ย. 55 11:30</string>
	 * 
	 * http://www.ebooks.in.th/images/true/hilight/hilight-1@2x.jpg
	 * 
	 * @param plistObject
	 */

	private String BCode = "0";
	private String MCatID = "0";
	private String BookName = "";
	private String BookCover = "images/allcat.png";
	private String BookPrice = "0";
	private String BookRate = "0";

	/**
	 * @return the bCode
	 */
	public String getBCode() {
		return BCode;
	}

	/**
	 * @param bCode
	 *            the bCode to set
	 */
	public void setBCode(String bCode) {
		BCode = bCode;
	}

	/**
	 * @return the mCatID
	 */
	public String getMCatID() {
		return MCatID;
	}

	/**
	 * @param mCatID
	 *            the mCatID to set
	 */
	public void setMCatID(String mCatID) {
		MCatID = mCatID;
	}

	/**
	 * @return the bookName
	 */
	public String getBookName() {
		return BookName;
	}

	/**
	 * @param bookName
	 *            the bookName to set
	 */
	public void setBookName(String bookName) {
		BookName = bookName;
	}

	/**
	 * @return the bookCover
	 */
	public String getBookCover() {
		return BookCover;
	}

	/**
	 * @param bookCover
	 *            the bookCover to set
	 */
	public void setBookCover(String bookCover) {
		BookCover = bookCover;
	}

	/**
	 * @return the bookPrice
	 */
	public String getBookPrice() {
		return BookPrice;
	}

	/**
	 * @param bookPrice
	 *            the bookPrice to set
	 */
	public void setBookPrice(String bookPrice) {
		BookPrice = bookPrice;
	}

	/**
	 * @return the bookRate
	 */
	public String getBookRate() {
		return BookRate;
	}

	/**
	 * @param bookRate
	 *            the bookRate to set
	 */
	public void setBookRate(String bookRate) {
		BookRate = bookRate;
	}

}