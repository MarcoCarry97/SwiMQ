package pool;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.*;

import tools.Topic;

public class Temperature {

	public static final String TOPIC = "retilab/temperature";
	public static final int MIN = 18;
	public static final int MAX = 28;
	Random random = new Random();

	private MqttClient client;

	public Temperature() {
		String clientId = "temperature-pub";
		try {
			client = new MqttClient(Topic.BROKER_URL, clientId);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}// fine costruttore

	private void start() {
		try {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic("retilab/LWT"), "I'm gone :(".getBytes(), 0, false);
			client.connect(options);

			// Publish data forever
			while (true) {
				publishThemperature();
				Thread.sleep(5000);
			}
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}// fine start

	private void publishThemperature() throws MqttException {
		final MqttTopic timeTopic = client.getTopic(TOPIC);
		int temperatura = random.nextInt(MAX - MIN) + MIN;
		final String celsiusu = Long.toString((long) temperatura);

		timeTopic.publish(new MqttMessage(celsiusu.getBytes()));

		System.out.println("Published data. Topic: " + timeTopic.getName() + "  Message: " + celsiusu);
	}

	public static void main(String args[]) {
		final Temperature temperatura = new Temperature();
		temperatura.start();
	}

}
