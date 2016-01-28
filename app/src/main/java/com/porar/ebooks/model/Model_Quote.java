package com.porar.ebooks.model;

import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;



public class Model_Quote {

	private String bgID = null;
	private String ebooks_bg_L = null;
	private String ebooks_bg_M = null;
	private String ebooks_bg_S = null;
	private String ebooks_bg_XL = null;
	
	public Model_Quote(PListObject plistObject){
		Map<java.lang.String, PListObject> map = ((Dict)plistObject).getConfigMap();
		
		this.bgID = map.get("bgID").toString();
		this.ebooks_bg_L = map.get("ebooks_bg_L").toString();
		this.ebooks_bg_M = map.get("ebooks_bg_M").toString();
		this.ebooks_bg_S = map.get("ebooks_bg_S").toString();
		this.ebooks_bg_XL = map.get("ebooks_bg_XL").toString();
	}

	/**
	 * @return the bgID
	 */
	public String getBgID() {
		return bgID;
	}

	/**
	 * @param bgID the bgID to set
	 */
	public void setBgID(String bgID) {
		this.bgID = bgID;
	}

	/**
	 * @return the ebooks_bg_L
	 */
	public String getEbooks_bg_L() {
		return ebooks_bg_L;
	}

	/**
	 * @param ebooks_bg_L the ebooks_bg_L to set
	 */
	public void setEbooks_bg_L(String ebooks_bg_L) {
		this.ebooks_bg_L = ebooks_bg_L;
	}

	/**
	 * @return the ebooks_bg_M
	 */
	public String getEbooks_bg_M() {
		return ebooks_bg_M;
	}

	/**
	 * @param ebooks_bg_M the ebooks_bg_M to set
	 */
	public void setEbooks_bg_M(String ebooks_bg_M) {
		this.ebooks_bg_M = ebooks_bg_M;
	}

	/**
	 * @return the ebooks_bg_S
	 */
	public String getEbooks_bg_S() {
		return ebooks_bg_S;
	}

	/**
	 * @param ebooks_bg_S the ebooks_bg_S to set
	 */
	public void setEbooks_bg_S(String ebooks_bg_S) {
		this.ebooks_bg_S = ebooks_bg_S;
	}

	/**
	 * @return the ebooks_bg_XL
	 */
	public String getEbooks_bg_XL() {
		return ebooks_bg_XL;
	}

	/**
	 * @param ebooks_bg_XL the ebooks_bg_XL to set
	 */
	public void setEbooks_bg_XL(String ebooks_bg_XL) {
		this.ebooks_bg_XL = ebooks_bg_XL;
	}

	
	
	
	
}