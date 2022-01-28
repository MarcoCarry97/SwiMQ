package tools;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import nonserver.Executor;

import java.util.Date;

public class Subscriber {
	private Date date = new Date();
	private PubConsumer consumer;

	public static final String TOPIC = "retilab/swim/+";
	String clientId = Long.toString(date.getTime()) + "-sub";
	private MqttClient mqttClient;

	public Subscriber(PubConsumer consumer, MqttClient client) {

		try {
			this.consumer = consumer;
			mqttClient = client;

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void start() {
		try {
			mqttClient.setCallback(new Executor(consumer));
			mqttClient.connect();
			final String topic = TOPIC;
			mqttClient.subscribe(topic);
			System.out.println("Subscriber is now listening to " + topic);

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String args[]) throws MqttException {
		MqttClient client = new MqttClient(Topic.BROKER_URL, new Date(System.currentTimeMillis()).toString());
		PubConsumer pub = new PubConsumer(client, 1);
		new Thread(pub).start();
		final Subscriber subscriber = new Subscriber(pub, client);
		subscriber.start();
	}

}
