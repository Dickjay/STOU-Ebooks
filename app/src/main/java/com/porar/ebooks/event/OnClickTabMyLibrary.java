package com.porar.ebooks.event;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class OnClickTabMyLibrary  implements OnClickListener{

	Context context;
	String type;
	public OnClickTabMyLibrary(Context context,String type) {
		this.context = context;
		this.type = type;
	}
	@Override
	public void onClick(View v) {
		if (type == "News") {
			setPage(type);
		}else if(type == "Favorites"){
			setPage(type);
		}
		setDefaultEnableType(v.getId());
	}
	

	
	
	private void setDefaultEnableType(int id) {

	

	}
	public abstract void setPage(String Type);

}
