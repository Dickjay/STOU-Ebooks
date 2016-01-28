package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

public class Model_Publisher_Fan implements Comparable<Model_Publisher_Fan>, Serializable {

	private static final long serialVersionUID = 1L;
	private int CID = 0;
	private String Name = null;
	private String Image = null;
	private int TotalUpdate = 0;
	private boolean ReadFlag = false;

	public Model_Publisher_Fan(PListObject plistObject) {
		super();

		Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();
		this.CID = Integer.parseInt(map.get("CID").toString());
		this.Name = map.get("Name").toString();
		this.Image = map.get("Image").toString();
		this.TotalUpdate = Integer.parseInt(map.get("TotalUpdate").toString());
		this.ReadFlag = Boolean.parseBoolean(map.get("Image").toString());
	}

	public void setCID(int CID) {
		this.CID = CID;
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

	public void setImage(String Image) {
		this.Image = Image;
	}

	@Override
	public int compareTo(Model_Publisher_Fan another) {
		int compareCID = another.getCID();

		// descending order //return compareCID - this.CID;
		// ascending order
		return this.CID - compareCID;
	}

	/**
	 * @return the totalUpdate
	 */
	public int getTotalUpdate() {
		return TotalUpdate;
	}

	/**
	 * @param totalUpdate
	 *            the totalUpdate to set
	 */
	public void setTotalUpdate(int totalUpdate) {
		TotalUpdate = totalUpdate;
	}

	/**
	 * @return the readFlag
	 */
	public boolean isReadFlag() {
		return ReadFlag;
	}

	/**
	 * @param readFlag
	 *            the readFlag to set
	 */
	public void setReadFlag(boolean readFlag) {
		ReadFlag = readFlag;
	}

	public static Comparator<Model_Publisher_Fan> comparator_Name_ASC = new Comparator<Model_Publisher_Fan>() {

		@Override
		public int compare(Model_Publisher_Fan model1, Model_Publisher_Fan model2) {

			String Name1 = model1.getName().toUpperCase();
			String Name2 = model2.getName().toUpperCase();

			// descending order //return Name2.compareTo(Name1);
			// ascending order
			return Name1.compareTo(Name2);
		}
	};

}
