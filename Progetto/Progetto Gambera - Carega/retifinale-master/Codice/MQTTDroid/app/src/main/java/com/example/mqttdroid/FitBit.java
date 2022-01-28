package com.example.mqttdroid;

import com.example.mqttdroid.tool.Exercise;
import com.example.mqttdroid.tool.Swimmer;
import com.example.mqttdroid.tool.Training;

import java.util.Random;


public class FitBit implements Runnable
{ //implementare runable
	private int pulse;
	private float calorie;
	private int stroke;
	private Training training;
	private Swimmer swimmer;
	
	//aggiungere tempo
	
	public synchronized int getPulse()
	{
		return pulse;
	}

	public synchronized float getCalorie()
	{
		return calorie;
	}
	
	public synchronized int getStroke()
	{
		return stroke;
	}
	
	
	
	@Override
	public void run()
	{
		//String type=training.getType();
		int num=training.getExerciseNum();
		int[] pulses=new int[num];
		float[] calories=new float[num];
		int[] strokes=new int[num];
		float a=(float) 0.6;
		int index=0;
		for(Exercise exe:training.getExercises())
		{
			int len=exe.getNumLength();
			for(int i=0;i<len;i++)
			{
				attend();
				setParams(null);
				if(index==0)
				{
					pulses[index]=pulse;
					calories[index]=calorie;
					strokes[index]=stroke;
				}
				else
				{
					pulses[index]=(int) (pulses[index]*a+pulse*(1-a));
					calories[index]=(int) (calories[index]*a+calorie*(1-a));
					strokes[index]=(int) (strokes[index]*a+stroke*(1-a));
				}
				try{Thread.sleep(1000);} 
				catch (InterruptedException e)
				{	e.printStackTrace();}
			}
			index++;
		}
		int avgPulse=0, avgStroke=0;
		float avgCalorie=0;
		for(int i=0;i<num;i++)
		{
			avgPulse+=pulses[i]/num;
			avgStroke+=strokes[i]/num;
			avgCalorie+=calories[i]/num;
		}
		/*training.putPulse(swimmer.getId(),avgPulse);
		training.putCalorie(swimmer.getId(),avgCalorie);
		training.putStroke(swimmer.getId(),avgStroke);*/
		send(training,Topic.ROUGH);
	}
	
	private void setParams(String type)
	{
		int max=0;
		switch(type)
		{
			case("Speed"): max=140; break;
			case("Breath"): max=100; break;
			case("Resistance"): max=160; break;
			default: max=80;
		}
		pulse=new Random(max).nextInt();
		calorie=new Random(max).nextFloat()/10;
		stroke=new Random(max).nextInt()/10;
	}
	
	private void send(Training train,String topic)
	{
		//invio training al broker
	}
	
	private void attend()
	{
		//attesa del sensore
	}
}
