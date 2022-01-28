package com.example.mqttdroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mqttdroid.R;
import com.example.mqttdroid.Topic;
import com.example.mqttdroid.tool.Exercise;
import com.example.mqttdroid.tool.PubTask;
import com.example.mqttdroid.tool.Training;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.HashMap;

public class FitActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private EditText edit;
    private Button button;
    private Training training;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit);
        try
        {
            client=new MqttAndroidClient(FitActivity.this,"tcp://193.206.55.23:1883",String.valueOf(System.currentTimeMillis()));
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill("retilab/swim", "I'm gone :(".getBytes(), 0, false);
            client.connect(options);
            Bundle bundle=getIntent().getExtras();
            training=bundle.getParcelable("training");
            email=bundle.getString("email");
            edit=findViewById(R.id.fit);
            button=findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String,Object> map=new HashMap<String, Object>();
                    map.put("coach",training.getCoach().getId());
                    map.put("email",email);
                    map.put("date",training.getDate().getTime());
                    ArrayList<Exercise> exes=training.getExercises();
                    for(int i=0;i<exes.size();i++)
                        map.put("exe"+(i+1),exes.get(i).getStyle()+"-"+exes.get(i).getNumLength());
                    PubTask pub=new PubTask(client, (Topic.FITBIT+"/"+edit.getText().toString()));
                    pub.execute(map);
                    Intent intent=new Intent(FitActivity.this, GoodActivity.class);
                    intent.putExtra("fit",edit.getText().toString());
                    intent.putExtra("email",email);
                    startActivity(intent);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
