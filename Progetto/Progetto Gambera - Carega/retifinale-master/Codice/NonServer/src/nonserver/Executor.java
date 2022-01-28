package nonserver;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import tools.PubConsumer;
import tools.Publisher;
import tools.Topic;

public class Executor implements MqttCallback {

	private MqttClient mqttClient;
	private Date date = new Date();
	String clientId = Long.toString(date.getTime()) + "-sub";
	private JSONObject json;
	private PubConsumer consumer;

	public Executor(PubConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void connectionLost(Throwable cause) {
		try {
			mqttClient = new MqttClient(Topic.BROKER_URL, clientId);
			mqttClient.connect();
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String payload = new String(message.getPayload());
				System.out.println("Payload: " + payload + ", " + "Topic: " + topic);
				String result = null;
				String email = new String();
				Publisher pub;
				switch (topic) {
				case Topic.SIGNUP:
					json = new JSONObject(payload);
					result = SwiMQServer.signUp(json);
					email = json.getString("email");
					pub = new Publisher(consumer, result, Topic.SIGNUP + "/" + email);
					pub.start();
					break;

				case Topic.LOGIN:
					json = new JSONObject(payload);
					result = SwiMQServer.logIn(json);
					email = json.getString("email");
					pub = new Publisher(consumer, result, Topic.LOGIN + "/" + email);
					pub.start();
					break;

				case Topic.CREATE:
					json = new JSONObject(payload);
					result = SwiMQServer.create(json);
					email = json.getString("email");
					pub = new Publisher(consumer, result, Topic.CREATE + "/" + email);
					pub.start();
					break;

				case Topic.NEW_TRAIN:
					json = new JSONObject(payload);
					result = SwiMQServer.newTraining();
					email = json.getString("email");
					pub = new Publisher(consumer, result, Topic.NEW_TRAIN + "/" + email);
					pub.start();
					break;

				case Topic.OLD_TRAIN:
					json = new JSONObject(payload);
					result = SwiMQServer.oldTraining(json);
					email = json.getString("email");
					pub = new Publisher(consumer, result, Topic.OLD_TRAIN + "/" + email);
					pub.start();
					break;

				case Topic.POST:
					json = new JSONObject(payload);
					email = json.getString("email");
					result=SwiMQServer.analyze(json);
					pub = new Publisher(consumer, result, Topic.POST + "/" + email);
					pub.start();
					break;

				default:
					break;
				}
			}
		});
		thread.start();
	}

}
