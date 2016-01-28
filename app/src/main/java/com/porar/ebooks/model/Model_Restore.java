package com.porar.ebooks.model;

import java.io.Serializable;

import com.porar.ebooks.utils.StaticUtils;

public class Model_Restore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String BID;
	String Name;
	String CoverFileNameS;

	public Model_Restore(String BID, String Name, String CoverFileNameS) {

		this.BID = BID;
		this.Name = Name;
		this.CoverFileNameS = CoverFileNameS;
	}

	public String getBID() {
		return BID;
	}

	public String getName() {
		return Name;
	}

	public String getCoverFileNameS() {
		//

		if (CoverFileNameS.contains(" ")) {
			return StaticUtils.escapeHTML(CoverFileNameS);
		} else {
			return CoverFileNameS;
		}

	}
}
