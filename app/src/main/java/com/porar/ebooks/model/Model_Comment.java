package com.porar.ebooks.model;

import java.util.Map;


public class Model_Comment {
	
	
	// <id>260692</id>
	// <user_id>62843</user_id>
	// <name>Truelifedev2 Dev</name>
	// <display_name>Truelifedev2</display_name>
	// <avatar> http://image.platform.truelife.com/62843/avatar?key=1&amp;w=70&amp;h=70 </avatar>
	// <message>yi</message>
    //	<link>http://mylife.truelife.com/?uid=62843</link>
	//  <is_like>false</is_like>
	// <created_time>2012-10-31T15:05:28+07:00</created_time>

	private final  String id;
	private  String user_id;
	private  String name;
	private  String display_name;
	private  String avatar;
	private  String message;
	private  String link;
	private  String is_like;
	private  String created_time;
	
	public Model_Comment(Map<String, String> map_comment) {
		
		this.id  = map_comment.get("id").toString();
		this.user_id = map_comment.get("user_id").toString();
		this.name = map_comment.get("name").toString();
		this.display_name = map_comment.get("display_name").toString();
		this.avatar = map_comment.get("avatar").toString();
		this.message = map_comment.get("message").toString();
		this.link = map_comment.get("link").toString();
		this.is_like = map_comment.get("is_like").toString();
		this.created_time = map_comment.get("created_time").toString();
	}

	public String getId() {
		return id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIs_like() {
		return is_like;
	}

	public void setIs_like(String is_like) {
		this.is_like = is_like;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

}
