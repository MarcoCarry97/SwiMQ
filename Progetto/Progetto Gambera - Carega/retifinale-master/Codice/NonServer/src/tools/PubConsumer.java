package tools;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PubConsumer extends MessageQueue implements Runnable {
	// private int length;
	private MqttClient client;

	public PubConsumer(MqttClient client, int length) {
		this.client = client;
		this.numeroMessaggi = length;
	}

	@Override
	public void run() {

		while (true) {
			if (isEmpty()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				Message mex = receive();
				try {
					client.publish(mex.getTopic(), new MqttMessage(mex.getBody().getBytes()));
				} catch (MqttException e) {
					e.printStackTrace();
				}

			}
		}

	}

}
