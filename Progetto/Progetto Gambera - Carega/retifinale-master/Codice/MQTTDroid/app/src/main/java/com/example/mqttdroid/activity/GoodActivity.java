package com.example.mqttdroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mqttdroid.R;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class GoodActivity extends AppCompatActivity {

    //private MqttAndroidClient client=new MqttAndroidClient(GoodActivity.this,"193.206.55.23:1883",String.valueOf(System.currentTimeMillis()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);

    }
}
