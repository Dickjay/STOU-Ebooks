package com.porar.ebooks.model;

import java.io.Serializable;

import com.porar.ebooks.stou.R;

public class Model_Setting implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawable_backgroundStyle = R.drawable.bg_main_default;
	private int drawable_backgroundStyleSmall = R.drawable.bg_main_default_ss;

	
	public int getDrawable_backgroundStyle() {
		return drawable_backgroundStyle;
	}

	
	public void setDrawable_backgroundStyle(int drawable_backgroundStyle) {
		this.drawable_backgroundStyle = drawable_backgroundStyle;
	}


	/**
	 * @return the drawable_backgroundStyleSmall
	 */
	public int getDrawable_backgroundStyleSmall() {
		return drawable_backgroundStyleSmall;
	}


	/**
	 * @param drawable_backgroundStyleSmall the drawable_backgroundStyleSmall to set
	 */
	public void setDrawable_backgroundStyleSmall(int drawable_backgroundStyleSmall) {
		this.drawable_backgroundStyleSmall = drawable_backgroundStyleSmall;
	}


	
	
	
}
