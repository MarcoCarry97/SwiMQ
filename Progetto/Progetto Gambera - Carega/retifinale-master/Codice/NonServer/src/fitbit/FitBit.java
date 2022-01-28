package fitbit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import pool.SensorListener;
import tools.Topic;

public class FitBit {
	private Date date = new Date();
	String clientId = Long.toString(date.getTime()) + "-sub";
	private int pulse;
	private float calorie;
	private int stroke;
	private ArrayList<Integer> pulses;
	private ArrayList<Float> calories;
	private ArrayList<Integer> strokes;
	private SensorListener sensors;

	public FitBit() {
		pulses = new ArrayList<Integer>();
		calories = new ArrayList<Float>();
		strokes = new ArrayList<Integer>();
	}

	public String runFitBit(JSONObject json) throws MqttException {
		Integer numVasche = 0;
		final Integer done[] = new Integer[1];
		done[0] = 0;
		MqttClient client = new MqttClient(Topic.BROKER_URL, clientId);
		client.connect();
		sensors = new SensorListener(client, "retilab/swim/sensor/+", new MqttCallback() {

			@Override
			public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
				// TODO Auto-generated method stub
				done[0]++;
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void connectionLost(Throwable arg0) {
				// TODO Auto-generated method stub
			}
		});
		new Thread(sensors).start();
		for (Integer i = 1; i < 5; i++) {
			String vascaString = (String) json.get("exe" + i.toString());
			String[] tokens = vascaString.split("-");
			numVasche += Integer.parseInt(tokens[1]);

		}
		while (done[0] < numVasche) {
			setParams();
			pulses.add(pulse);
			calories.add(calorie);
			strokes.add(stroke);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		double avgPulse = pulses.stream().mapToInt(val -> val).average().orElse(0.0);
		float avgCalorie = (float) calories.stream().mapToDouble(val -> val).average().orElse(0.0);
		double avgStrokes = strokes.stream().mapToInt(val -> val).average().orElse(0.0);
		JSONObject result = new JSONObject();
		result.put("pulse", avgPulse);
		result.put("calorie", avgCalorie);
		result.put("stroke", avgStrokes);
		result.put("date", json.getLong("date"));
		result.put("exe1", json.get("exe1"));
		result.put("exe2", json.get("exe2"));
		result.put("exe3", json.get("exe3"));
		result.put("exe4", json.get("exe4"));
		result.put("coach", json.get("coach"));
		LocalDate currentDate = LocalDate.now();
		String email = (String) json.getString("email");
		File check = new File("root/swimmer/" + email + "/" + currentDate.toString() + ".json");
		if (!check.exists()) {
			try (FileWriter file = new FileWriter("root/swimmer/" + email + "/" + currentDate.toString() + ".json")) {
				file.write(result.toString());
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	private void setParams() {
		int max = 150;
		int min = 120;
		pulse = new Random().nextInt(max - min) + min;
		calorie = new Random().nextInt(max - min) + min / 5;
		stroke = new Random().nextInt(max - min) + min / 7;
	}
}
