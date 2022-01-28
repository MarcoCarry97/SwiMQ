package com.example.mqttdroid.tool;

import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class SubTask extends AsyncTask<String,String,JSONObject>
{
    private MqttAndroidClient client;
    private String topic;
    private Context context;
    private IMqttMessageListener listener;
    //private TextView textView;

    public SubTask(MqttAndroidClient client, Context context,String topic,IMqttMessageListener listener)
    {
        this.client=client;
        this.context=context;
        this.topic=topic;
        this.listener=listener;
        //this.textView=textView;
    }

    @Override
    protected JSONObject doInBackground(String... strings)
    {
        final boolean[] prepared = {false};
        final JSONObject[] result = {new JSONObject()};
        try {
            //client.subscribe(topic,1);
            //client.wait(1000);
            Log.d("SUB","ATTEND");
           client.subscribe(topic, 1,listener);
           // while (!prepared[0]);
        } catch (MqttException e) {
            Log.e("SUB:","NONE");
            e.printStackTrace();
        }
        return result[0];
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
         //if(values[0]!=null) textView.setText(values[0]);
         Toast.makeText(context,values[0],Toast.LENGTH_SHORT).show();
        Log.d("Value:",values[0]);
    }
}
