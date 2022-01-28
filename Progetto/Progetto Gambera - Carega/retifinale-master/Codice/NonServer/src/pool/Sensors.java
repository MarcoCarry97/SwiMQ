package pool;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import tools.Topic;

public class Sensors {

	public static final int MIN = 1000;
	public static final int MAX = 7000;
	Random randomSensor = new Random();

	private MqttClient client;

	public Sensors() {
		String clientId = "sensor-pub";
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
			String topic = Topic.SENSOR_1;
			while (true) {
				try {
					int sensor = randomSensor.nextInt(MAX - MIN) + MIN;
					publishSensors(sensor, topic);
					topic = (topic.equals(Topic.SENSOR_1)) ? Topic.SENSOR_2 : Topic.SENSOR_1;
				} catch (Exception e) {
					// TODO: handle exception
					client.connect();
				}
			}
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}// fine start

	private void publishSensors(int sensor, String topic) throws MqttException, InterruptedException {
		final MqttTopic sensor1Topic = client.getTopic(topic);
		String sensore = "sono arrivato al sensore";
		sensor1Topic.publish(new MqttMessage(sensore.getBytes()));
		System.out.println("Published data. Topic: " + sensor1Topic.getName() + "  Message: " + sensore);
		Thread.sleep(sensor);
	}

	public static void main(String args[]) {
		final Sensors sensors = new Sensors();
		sensors.start();
	}

}
