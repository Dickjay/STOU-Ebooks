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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import plist.xml.PListObject;
import plist.xml.PListObjectType;



import android.util.Log;

/**
 * Represents a simple plist date elements.
 */
public class Date extends PListObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3846688440069431376L;

	private static final java.lang.String TAG = "Date";

	/**
	 * The parsed date object.
	 */
	protected java.util.Date date;

	/**
	 * Used for parsing ISO dates.
	 */
	private SimpleDateFormat iso8601Format;

	public Date() {
		setType(PListObjectType.DATE);
		iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#getValue()
	 */
	public java.util.Date getValue() {
		return (java.util.Date) date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
	 * (java.lang.Object)
	 */
	public void setValue(java.util.Date val) {
		this.date = val;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.longevitysoft.android.xml.plist.domain.IPListSimpleObject#setValue
	 * (java.lang.String)
	 */
	public void setValue(java.lang.String val) {
		// sniff date
		if (null == val || val.length() < 1) {
			this.date = null;
			return;
		}
		Scanner scanner = new java.util.Scanner(val).useDelimiter("-");
		if (scanner.hasNextInt()) {
			try {
				this.date = iso8601Format.parse(val);
			} catch (ParseException e) {
				Log.e(TAG, new StringBuilder("#setValue - error parsing val=")
						.append(val).toString(), e);
			}
		} else {
			this.date = new java.util.Date(java.util.Date.parse(val.trim()));
		}
	}
	
	@Override
	public java.lang.String toString(){
		return this.iso8601Format.toString();
	}
}