package fitbit;

import java.util.Date;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import tools.*;
import tools.PubConsumer;
import tools.Topic;

public class ExecFit implements MqttCallback {

	private MqttClient mqttClient;
	private JSONObject json;
	private PubConsumer consumer;
	private Date date = new Date();
	String clientId = Long.toString(date.getTime()) + "-sub";

	public ExecFit(PubConsumer consumer) {
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
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		try {
			String payload = new String(message.getPayload());
			System.out.println("Payload: " + payload + ", " + "Topic: " + topic);
			String result = null;
			Publisher pub;
			json = new JSONObject(payload);
			FitBit fit = new FitBit();
			result = fit.runFitBit(json);
			String email = json.getString("email");
			pub = new Publisher(consumer, result, Topic.FITBIT + "/" + SubFit.fitbitId.toString() + "/" + email);
			pub.start();
			System.out.println("PUBLISH");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
