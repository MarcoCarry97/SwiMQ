package com.example.mqttdroid.tool;

import android.os.Parcel;
import android.os.Parcelable;

public class Coach extends User
{
	public Coach(String id) {
		this.id = id;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	public Coach(Parcel in)
	{
		id=in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(id);
	}

	public static final Parcelable.Creator<Coach> CREATOR
			= new Parcelable.Creator<Coach>()
	{
		public Coach createFromParcel(Parcel in)
		{
			return new Coach(in);
		}

		public Coach[] newArray(int size) {
			return new Coach[size];
		}
	};
}
