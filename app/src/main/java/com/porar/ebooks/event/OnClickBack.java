package com.porar.ebooks.event;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class OnClickBack  implements OnClickListener{

	Activity context;
	public OnClickBack(Activity context) {
		this.context =  context;
		
	}
	@Override
	public void onClick(View v) {
		System.gc(); 
		context.finish();
		
	}
	
	
}
