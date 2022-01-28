package com.example.mqttdroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mqttdroid.R;
import com.example.mqttdroid.Topic;
import com.example.mqttdroid.tool.PubTask;
import com.example.mqttdroid.tool.SubTask;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private TextView pulse,calories,strokes;
    private TextView messagePulse,messageCal,messageStroke;
    private Button button;
    private String email;
    private final int PULSE=35;
    private final float STROKE=25;
    private final float CALORIES=125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Bundle bundle=getIntent().getExtras();
        email=bundle.getString("email");
        try {
            client=new MqttAndroidClient(PostActivity.this,"tcp://193.206.55.23:1883", String.valueOf(System.currentTimeMillis()));
            client.connect();
            pulse=findViewById(R.id.oldPulse);
            calories=findViewById(R.id.oldCalorie);
            strokes=findViewById(R.id.oldStroke);
            messageCal=findViewById(R.id.newCalorie);
            messagePulse=findViewById(R.id.newPulse);
            messageStroke=findViewById(R.id.newStroke);
            button=findViewById(R.id.sub);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String,Object> map=new HashMap<String, Object>();
                    map.put("email",email);
                    PubTask pub=new PubTask(client, Topic.POST);
                    pub.execute(map);
                    SubTask sub=new SubTask(client, PostActivity.this, Topic.POST + "/" + email, new IMqttMessageListener() {
                        @Override
                        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                            String res=new String(mqttMessage.getPayload());
                            JSONObject json=new JSONObject(res);
                            Log.d("JSON",json.toString());
                            final int cal=json.getInt("calorie");
                            final int stroke=json.getInt("stroke");
                            final int pulses=json.getInt("pulse");
                            Log.d("SUB UI","READY");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    calories.setText(String.valueOf(cal));
                                    strokes.setText(String.valueOf(stroke));
                                    pulse.setText(String.valueOf(pulses));
                                    messageCal.setText(cal>=CALORIES ? "GOOD" : "BAD");
                                    messagePulse.setText(pulses>=PULSE ? "GOOD" : "BAD");
                                    messageStroke.setText(stroke>=STROKE ? "GOOD" : "BAD");
                                }
                            });
                        }
                    });
                    sub.execute();
                }
            });
        }
        catch (Exception e)
        {

        }
    }
}
