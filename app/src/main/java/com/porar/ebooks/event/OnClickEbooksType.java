package com.porar.ebooks.event;

import plist.xml.PList;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.porar.ebooks.stou.R;

public class OnClickEbooksType implements OnClickListener {

	private final Activity context;
	LoadResultEbooks ebooks;
	String type;

	public void setLoadResultEbook(LoadResultEbooks loadResultEbooks) {
		ebooks = loadResultEbooks;
	}

	public OnClickEbooksType(Activity context) {
		this.context = context;
	}

	public void newInstance(String type) {
		this.type = type;
	}

	@Override
	public void onClick(View v) {

		if (type == "N") {
			setDefaultEnableType(v.getId());
			LoadApiEbooks(type);
		} else if (type == "R") {
			setDefaultEnableType(v.getId());
			LoadApiEbooks(type);
		} 
//			else if (type == "F") {
//			setDefaultEnableType(v.getId());
//			LoadApiEbooks(type);
//		} else if (type == "P") {
//			setDefaultEnableType(v.getId());
//			LoadApiEbooks(type);
//		}
	}

	private void LoadApiEbooks(String Type) {
		ebooks.onResult(null, Type);
	}

	public interface LoadResultEbooks {
		void onResult(PList pList, String Type);
	}

	private void setDefaultEnableType(int id) {

		((Button) context.findViewById(R.id.tabview_btn_pnews)).setEnabled(true);
		((Button) context.findViewById(R.id.tabview_btn_precommend)).setEnabled(true);
//		((Button) context.findViewById(R.id.tabview_btn_pfree)).setEnabled(true);
//		((Button) context.findViewById(R.id.tabview_btn_psales)).setEnabled(true);

		if (id == R.id.tabview_btn_pnews) {
			((Button) context.findViewById(R.id.tabview_btn_pnews)).setEnabled(false);
		}
		if (id == R.id.tabview_btn_precommend) {
			((Button) context.findViewById(R.id.tabview_btn_precommend)).setEnabled(false);
		}
//		if (id == R.id.tabview_btn_pfree) {
//			((Button) context.findViewById(R.id.tabview_btn_pfree)).setEnabled(false);
//		}
//		if (id == R.id.tabview_btn_psales) {
//			((Button) context.findViewById(R.id.tabview_btn_psales)).setEnabled(false);
//		}

	}

}
