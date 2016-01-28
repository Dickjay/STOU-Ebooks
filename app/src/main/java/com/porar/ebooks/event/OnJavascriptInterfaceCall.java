package com.porar.ebooks.event;

public interface OnJavascriptInterfaceCall {
	public abstract void onImageError(String url);
	public abstract void onImageClick(String url);
	public abstract void onMessage(String title,String msg,String msg2);
}
