package com.example.mqttdroid.tool;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mqttdroid.FitBit;

public class Swimmer extends User {
	private String fitbit;

	public String getFitbit() {
		return fitbit;
	}

	public void setFitbit(String fitbit) {
		this.fitbit = fitbit;
	}
	
	public Swimmer(String id) {
		this.id=id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeString(id);
		parcel.writeString(fitbit);
	}

	public static final Parcelable.Creator<Swimmer> CREATOR
			= new Creator<Swimmer>() {
		public Swimmer createFromParcel(Parcel in)
		{
			return new Swimmer(in);
		}

		public Swimmer[] newArray(int size) {
			return new Swimmer[size];
		}
	};

	public Swimmer(Parcel in)
	{
		id=in.readString();
		fitbit=in.readString();
	}
}
