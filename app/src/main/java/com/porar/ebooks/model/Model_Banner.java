package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

import com.porar.ebooks.utils.StaticUtils;

@SuppressWarnings("serial")
public class Model_Banner implements Serializable {

	/**
	 * <key>NID</key><string>1</string>
	 * <key>Topic</key><string>THAILAND NO.1 BESTSELLER</string>
	 * <key>ImageFile</key><string>hilight-1.jpg</string>
	 * <key>UpdateDateTime</key><string>15 มิ.ย. 55 11:30</string>
	 * 
	 * http://www.ebooks.in.th/images/true/hilight/hilight-1@2x.jpg
	 * 
	 * @param plistObject
	 */
	private final String NID;

	private final String Topic;
	private final String ImageFile;
	private final String UpdateDateTime;

	public Model_Banner(PListObject plistObject) {
		Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();

		this.NID = map.get("NID").toString();
		this.Topic = map.get("Topic").toString();
		this.ImageFile = map.get("ImageFile").toString();
		this.UpdateDateTime = map.get("UpdateDateTime").toString();

	}

	public String getNID() {
		return NID;
	}

	public String getTopic() {
		return Topic;
	}

	public String getImageFile() {
		return StaticUtils.escapeHTML(ImageFile);
	}

	public String getUpdateDateTime() {
		return UpdateDateTime;
	}

}
