package com.example.mqttdroid.tool;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class Exercise implements Serializable {
	
	private int numLength;
	private String style;

	public Exercise(String type,int num)
	{
		style=type;
		numLength=num;
	}

	public int getNumLength() {
		return numLength;
	}
	
	public void setNumLength(int numLength) {
		this.numLength = numLength;
	}
	
	public String getStyle() {
		return style;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}

	/*@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeInt(numLength);
		parcel.writeString(style);
	}

	public static final Parcelable.Creator<Exercise> CREATOR
			= new Creator<Exercise>() {
		public Exercise createFromParcel(Parcel in)
		{
			return new Exercise(in);
		}

		public Exercise[] newArray(int size) {
			return new Exercise[size];
		}
	};*/

	public Exercise(Parcel in)
	{
		numLength=in.readInt();
		style=in.readString();
	}

	@NonNull
	@Override
	public String toString() {
		return style+"-"+numLength;
	}
}
