package com.example.mqttdroid;

import com.example.mqttdroid.tool.Training;

import java.util.HashMap;

public class Analysis //implements Runnable
{ // implementare runnable
	
	/*private String swimmer;
	private Training current;
	private Training previous;
	private HashMap <String,String> message;
	
	public String getMessage(String index)
	{
		return message.get(index);
	}
	
	public Analysis(String swimmer, Training current, Training previous)
	{
		this.swimmer = swimmer;
		this.current = current;
		this.previous = previous;
		message=new HashMap<String,String>();
		message.put("Stroke", "NOT YET ANALYZED");
		message.put("Pulse", "NOT YET ANALYZED");
		message.put("Speed", "NOT YET ANALYZED");
		message.put("Calorie", "NOT YET ANALYZED");
	}

	@Override
	public void run()
	{
		if(previous == null) {
			message.put("Stroke", "ANALISI IMPOSSIBILE, SERVONO DUE ALLENAMENTI. TORNA IN VASCA BESTIAH");
			message.put("Pulse", "ANALISI IMPOSSIBILE, SERVONO DUE ALLENAMENTI. TORNA IN VASCA BESTIAH");
			message.put("Speed", "ANALISI IMPOSSIBILE, SERVONO DUE ALLENAMENTI. TORNA IN VASCA BESTIAH");
			message.put("Calorie", "ANALISI IMPOSSIBILE, SERVONO DUE ALLENAMENTI. TORNA IN VASCA BESTIAH");
		}
		else {
			if(current.getStroke(swimmer) < (2/3*(previous.getStroke(swimmer)))) {
				message.put("Stroke", "AUMENTA IL NUMERO DI BRACCIATE. ");
			}
			else if(current.getStroke(swimmer) > (3/2*(previous.getStroke(swimmer)))){
				message.put("Stroke", "BRAVO TI STAI IMPEGNANDO, HAI FATTO MOLTE BRACCIATE. ");
			}
			else {
				message.put("Stroke", "BRACCIATE NELLA MEDIA. ");
			}
			
			if(current.getPulse(swimmer) < (1/2*(previous.getPulse(swimmer))) && current.getPulse(swimmer) > (2*(previous.getPulse(swimmer)))) {
				message.put("Pulse", "VISITA UN MEDICO. ");
			}
			else {
				message.put("Pulse", "PULSAZIONI NELLA MEDIA. ");
			}
			
			if(current.getSpeed(swimmer) < (2/3*(previous.getStroke(swimmer)))) {
				message.put("Speed", "NUOTA FORREST, NUOTA. ");
			}
			else if(current.getStroke(swimmer) > (previous.getStroke(swimmer))){
				message.put("Speed", "BRAVO TI STAI IMPEGNANDO, SEI PIU' VELOCE. ");
			}
			else {
				message.put("Speed", "VELOCITA' NELLA MEDIA. ");
			}
			
			if(current.getCalorie(swimmer) < (2/3*(previous.getCalorie(swimmer)))) {
				message.put("Calorie", "NON SARAI MAI IN FORMA PER LA PROVA COSTUME SE BRUCI COSI' POCO. ");
			}
			else if(current.getStroke(swimmer) > (3/2*(previous.getStroke(swimmer)))){
				message.put("Calorie", "BRAVO TI STAI IMPEGNANDO, HAI CONSUMATO PIU' CALORIE. ");
			}
			else {
				message.put("Calorie", "CALORIE CONSUMATE NELLA MEDIA. ");
			}
		}
	} 	*/
	
}
