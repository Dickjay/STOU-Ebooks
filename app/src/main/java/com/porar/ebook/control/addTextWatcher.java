package com.porar.ebook.control;

import android.text.Editable;
import android.text.TextWatcher;

public class addTextWatcher implements TextWatcher {
	IntefaceAfterTextChange afterTextChange;

	public void addInteface(IntefaceAfterTextChange intefaceAfterTextChange) {
		afterTextChange = intefaceAfterTextChange;
	}

	@Override
	public void afterTextChanged(Editable et) {
		afterTextChange.AfterTextChange(et.toString());
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	public interface IntefaceAfterTextChange {
		void AfterTextChange(String str);
	}
}
