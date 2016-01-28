package com.porar.ebooks.model;

import java.io.Serializable;

import com.porar.ebooks.stou.R;

public class Model_SettingShelf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int drawable_ShelfStyle = R.drawable.shelf_glass2x;
	private int drawable_ShelfStyleSmall = R.drawable.shelf_glass;

	public int getDrawable_ShelfStyle() {
		return drawable_ShelfStyle;
	}

	public void setDrawable_ShelfStyle(int drawable_backgroundStyle) {
		this.drawable_ShelfStyle = drawable_backgroundStyle;
	}

	public int getDrawable_ShelfStyleSmall() {
		return drawable_ShelfStyleSmall;
	}

	public void setDrawable_ShelfStyleSmall(int drawable_ShelfStyleSmall) {
		this.drawable_ShelfStyleSmall = drawable_ShelfStyleSmall;
	}

}
