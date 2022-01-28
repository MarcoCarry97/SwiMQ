package com.example.mqttdroid;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.*;
import java.util.Date;

public class Publisher implements Runnable {

    public static final String BROKER_URL = "tcp://193.206.55.23:1883";

    private MqttClient client;

    private Date date = new Date();


    public Publisher() {

        //We have to generate a unique Client id.
        String clientId = Long.toString(date.getTime()) + "-pub";


        try {
            Log.e("ok","ok");
            client = new MqttClient(BROKER_URL, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
/*
    public void start() {

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(client.getTopic("retilab/LWT"), "I'm gone :(".getBytes(), 0, false);

            client.connect(options);

            //Publish data forever
            while (true) {

                publishTime();

                Thread.sleep(2000);


            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    private void publishTime() throws MqttException {
        final MqttTopic timeTopic = client.getTopic("");
	date = new Date();
        final String time = Long.toString(date.getTime());

        timeTopic.publish(new MqttMessage(time.getBytes()));

        System.out.println("Published data. Topic: " + timeTopic.getName() + "  Message: " + time);
    }

    @Override
    public void run() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill(client.getTopic("retilab/LWT"), "I'm gone :(".getBytes(), 0, false);

            client.connect(options);

            //Publish data forever
            while (true) {

                publishTime();

                Thread.sleep(2000);


            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /*public static void main(String args[]) {
        final Publisher publisher = new Publisher();
        publisher.start();
    }*/
}
