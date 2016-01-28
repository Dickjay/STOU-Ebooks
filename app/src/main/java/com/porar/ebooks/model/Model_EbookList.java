package com.porar.ebooks.model;

import java.util.Comparator;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

public class Model_EbookList implements Comparable<Model_EbookList> {

	/*
	 * <key>BID</key><string>4584</string>
	 * <key>Publisher</key><string>ร ยธโ€�ร ยธยตร ยธโ€ขร ยธยฐร ยธห�ร ยธยฒร ยธยฃร ยธยฒ</string>
	 * <key>Name</key><string>ร ยธโ€นร ยธยตร ยธยฃร ยธยตร ยนห�ร ยธยขร ยนล’ร ยธยฃร ยธยฑร ยธ๏ฟฝร ยธล ร ยธยธร ยธโ€�ร ยธล ร ยนโ€�ร ยธยฅร ยธยกร ยนฦ’ร ยธห� ร ยธโ€�ร ยนโ€ฐร ยธยงร ยธยขร ยนโ€�ร ยธยญร ยธยฃร ยธยฑร ยธ๏ฟฝ</string>
	 * <key>NumRatings</key><integer>1</integer>
	 * <key>Rating</key><real>5.00</real>
	 * <key>Price</key><string>$4.99</string>
	 * <key>Cover</key><string>covers.jpg</string>
	 * <key>AwardFlag</key><false />
	 */
	private final int BID;
	private final String Publisher;
	private final String Name;
	private final int NumRatings;
	private final float Rating;
	private final String Price;
	private final String Cover;
	private final boolean AwardFlag;
	private final String IVR;
	private final String IVRStatus;
	private final String PriceTHB;

	public Model_EbookList(PListObject plistObject) {
		Map<java.lang.String, PListObject> map = ((Dict) plistObject).getConfigMap();
		this.BID = Integer.parseInt(map.get("BID").toString());
		this.Publisher = map.get("Publisher").toString();
		this.Name = map.get("Name").toString();
		this.NumRatings = Integer.parseInt(map.get("NumRatings").toString());
		this.Rating = Float.parseFloat(map.get("Rating").toString());
		this.Price = map.get("Price").toString();
		this.Cover = map.get("Cover").toString();
		this.AwardFlag = Boolean.parseBoolean(map.get("AwardFlag").toString());
		this.IVR = map.get("IVR").toString();
		this.IVRStatus = map.get("IVRStatus").toString();
		this.PriceTHB = map.get("PriceTHB").toString();
	}

	public int getBID() {
		return this.BID;
	}

	public String getPublisher() {
		return this.Publisher;
	}

	public String getName() {
		return this.Name;
	}

	public int getNumRatings() {
		return this.NumRatings;
	}

	public float getRating() {
		return this.Rating;
	}

	public String getPrice() {
		return this.Price;
	}

	public String getCover() {

			return StaticUtils.escapeHTML(Cover);

	}

	public boolean getAwardFlag() {
		return this.AwardFlag;
	}

	@Override
	public int compareTo(Model_EbookList another) {
		if (this.getBID() < another.getBID()) {
			return -1;
		} else if (this.getBID() == another.getBID()) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @return the iVR
	 */
	public String getIVR() {
		return IVR;
	}

	/**
	 * @return the iVRStatus
	 */
	public String getIVRStatus() {
		return IVRStatus;
	}

	/**
	 * @return the priceTHB
	 */
	public String getPriceTHB() {
		return PriceTHB;
	}

	public static Comparator<Model_EbookList> comparator_ebooklist_ASC = new Comparator<Model_EbookList>() {

		@Override
		public int compare(Model_EbookList model1, Model_EbookList model2) {

			String Name1 = model1.getName().toUpperCase();
			String Name2 = model2.getName().toUpperCase();

			// descending order
			// return Name2.compareTo(Name1);

			// ascending order
			return Name1.compareTo(Name2);
		}
	};
	public static Comparator<Model_EbookList> comparator_ebooklist_DES = new Comparator<Model_EbookList>() {

		@Override
		public int compare(Model_EbookList model1, Model_EbookList model2) {

			String Name1 = model1.getName().toUpperCase();
			String Name2 = model2.getName().toUpperCase();

			// descending order
			return Name2.compareTo(Name1);

			// ascending order
			// return Name1.compareTo(Name2);
		}
	};

}
