package tools;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Date;

public class Publisher {

	private String message, topic;

	private MqttClient client;

	private Date date = new Date();
	private PubConsumer consumer;

	public Publisher(PubConsumer consumer, String messaggio, String topic) {
		String clientId = Long.toString(date.getTime()) + "-pub";
		this.message = messaggio;
		this.topic = topic;
		this.consumer = consumer;

		try {

			client = new MqttClient(Topic.BROKER_URL, clientId);

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void start() {

		try {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic("retilab/LWT"), "I'm gone :(".getBytes(), 0, false);
			client.connect(options);
			System.out.println("CLIENT CONNECTED");
			publishUser();

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void publishUser() throws MqttException {
		final MqttTopic userTopic = client.getTopic(topic);
		final String user = message;
		while (consumer.isFull()) {
			try {
				Thread.sleep(500);
				System.out.println("PUB READY");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		consumer.send(new Message(topic, user));
		System.out.println("Published data. Topic: " + userTopic.getName() + "  Message: " + user);

	}

}
