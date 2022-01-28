package pool;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SensorListener implements Runnable {
	private MqttClient client;
	private String topic;
	private MqttCallback callback;

	public SensorListener(MqttClient client, String topic, MqttCallback mqttCallback) {
		this.client = client;
		this.topic = topic;
		this.callback = mqttCallback;
	}

	@Override
	public void run() {
		try {
			client.setCallback(callback);
			client.subscribe(topic);
			System.out.println("Subscriber is now listening to " + topic);

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
