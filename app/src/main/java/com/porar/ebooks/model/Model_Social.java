package com.porar.ebooks.model;

import java.io.Serializable;
import java.util.Map;

import plist.type.Dict;
import plist.xml.PListObject;

@SuppressWarnings("serial")
public class Model_Social implements Serializable {

	private final String title;
	private final String description;
	private final String default_message;
	
	public Model_Social(PListObject plistObject) {
		Map<String, PListObject> map = ((Dict) plistObject).getConfigMap();

		this.title = map.get("title").toString();
		this.description = map.get("description").toString();
		this.default_message = map.get("default_message").toString();

	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the default_message
	 */
	public String getDefault_message() {
		return default_message;
	}

	


}
