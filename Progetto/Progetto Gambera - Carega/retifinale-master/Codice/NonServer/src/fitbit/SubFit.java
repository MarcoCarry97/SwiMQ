package fitbit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import tools.PubConsumer;
import tools.Topic;

import java.util.Date;
import java.util.Random;

public class SubFit {

	private Date date = new Date();
	private PubConsumer consumer;
	String clientId = Long.toString(date.getTime()) + "-sub";
	private MqttClient mqttClient;
	private Random randomId = new Random();
	static Integer fitbitId;
	public static final int MIN = 10000;
	public static final int MAX = 99999;

	public SubFit(PubConsumer consumer, MqttClient client) {
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

			mqttClient.setCallback(new ExecFit(consumer));
			fitbitId = randomId.nextInt(MAX - MIN) + MIN;
			System.out.println(fitbitId);
			mqttClient.subscribe(Topic.FITBIT + "/" + fitbitId.toString());
			System.out.println("Subscriber is now listening to " + Topic.FITBIT + "/" + fitbitId.toString());

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String args[]) throws MqttException {
		MqttClient client = new MqttClient(Topic.BROKER_URL, new Date(System.currentTimeMillis()).toString());
		PubConsumer pub = new PubConsumer(client, 1);
		client.connect();
		new Thread(pub).start();
		final SubFit subfit = new SubFit(pub, client);
		subfit.start();
	}

}
