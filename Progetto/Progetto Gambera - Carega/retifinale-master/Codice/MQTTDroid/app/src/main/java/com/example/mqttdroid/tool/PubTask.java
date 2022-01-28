package com.example.mqttdroid.tool;

import android.os.AsyncTask;
import android.util.Log;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PubTask extends AsyncTask<HashMap<String,Object>, String,String>
{
    private MqttAndroidClient client;
    private String topic;

    public PubTask(MqttAndroidClient client,String topic)
    {
        this.client=client;
        this.topic=topic;
    }

    @Override
    protected String doInBackground(HashMap<String,Object>... maps)
        {
        try {
            //String res="";
            //for(String s:strings) res+=s+" ";
            JSONObject json=new JSONObject();
            for(Map.Entry<String,Object> entry:maps[0].entrySet())
            {
                json.put(entry.getKey(),entry.getValue());
                Log.d(entry.getKey(),entry.getValue().toString());
            }
            Log.d("MAP", maps.toString());
            if(client==null) Log.e("CLIENT","NULL");
            else Log.e("CLIENT","OK");
            IMqttToken token=client.publish(topic,new MqttMessage(json.toString().getBytes()));
            Log.e("TOKEN",(token==null) ? "NULL" : "OK");
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.d("PUB","OK");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.d("PUB","NONE");
                }
            });
            Log.d("PUB","FINISH");
        } catch (Exception e) {
            Log.e("ERROR",e.getMessage());
            e.printStackTrace();
        }
            // Toast.makeText(MainActivity.this,"IT WORKS!!!",Toast.LENGTH_LONG).show();

        return null;
    }
}
