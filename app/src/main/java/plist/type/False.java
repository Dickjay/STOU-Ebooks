/**
 * Licensed under Creative Commons Attribution 3.0 Unported license.
 * http://creativecommons.org/licenses/by/3.0/
 * You are free to copy, distribute and transmit the work, and 
 * to adapt the work.  You must attribute android-plist-parser 
 * to Free Beachler (http://www.freebeachler.com).
 * 
 * The Android PList parser (android-plist-parser) is distributed in 
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.
 */
package plist.type;

import plist.xml.PListObject;
import plist.xml.PListObjectType;

/**
 * Represents a simple plist false element.
 */
public class False extends PListObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8533886020773567552L;

	public False() {
		setType(PListObjectType.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#getValue()
	 */
	public Boolean getValue() {
		return new Boolean(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
	 * (java.lang.Object)
	 */
	public void setValue(Boolean val) {
		// noop
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
	 * (java.lang.String)
	 */
	public void setValue(java.lang.String val) {
		// noop
	}
	
	@Override
	public java.lang.String toString(){
		return "false";
	}
}