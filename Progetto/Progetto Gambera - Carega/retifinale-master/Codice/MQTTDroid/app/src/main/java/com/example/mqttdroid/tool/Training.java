package com.example.mqttdroid.tool;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class Training implements Parcelable
{
	private Coach coach;
	private Date date;
	private ArrayList<Exercise> exercise;

	public Training(String email, Date date, ArrayList<Exercise> exercise) {
		this.coach =new Coach(email);
		this.date = date;
		this.exercise = exercise;
	}

	protected Training(Parcel in) {
		coach = in.readParcelable(Coach.class.getClassLoader());
		date=new java.sql.Date(in.readLong());
		exercise =(ArrayList<Exercise>) in.readSerializable();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(coach, flags);
		dest.writeLong(date.getTime());
		dest.writeSerializable(exercise);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Training> CREATOR = new Creator<Training>() {
		@Override
		public Training createFromParcel(Parcel in) {
			return new Training(in);
		}

		@Override
		public Training[] newArray(int size) {
			return new Training[size];
		}
	};

	public ArrayList<Exercise> getExercises()
	{
		return exercise;
	}

	public Exercise getExercise(int index) {
		return exercise.get(index);
	}
	
	public void addExercise(Exercise e) {
		exercise.add(e);
	}
	
	public void removeExercise(int index) {
		exercise.remove(index);
	}
	
	public Coach getCoach() {
		return coach;
	}


	public Date getDate() {
		return date;
	}

	public int getExerciseNum()
	{
		return exercise.size();
	}

	public int getTotalLength()
	{
		int len=0;
		for(Exercise e:exercise)
			len+=e.getNumLength();
		return len*25;
	}

	@NonNull
	@Override
	public String toString() {
		String ret="";
		for(int i=0;i<4;i++)
			ret+=exercise.get(i).toString();
		return coach.getId()+" "+date+" "+ret;
	}
}
