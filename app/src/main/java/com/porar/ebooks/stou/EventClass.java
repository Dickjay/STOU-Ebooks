package com.porar.ebooks.stou;

import java.util.EventObject;

public class EventClass extends EventObject{
	private static final long serialVersionUID = 1L;
	private String eventName = "";
	public EventClass(Object source,String eventName) {
		super(source);
		this.eventName = eventName;
	}
	public String getEventName() {
		return eventName;
	}
}