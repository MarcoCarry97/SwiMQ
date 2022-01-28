package com.example.mqttdroid.tool;

import android.os.Parcelable;

public abstract class User implements Parcelable
{
	//protected String pwd;
	protected String id; //forse è una mail

	/*public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}*/
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
