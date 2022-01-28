package com.example.mqttdroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mqttdroid.R;
import com.example.mqttdroid.Topic;
import com.example.mqttdroid.tool.Swimmer;
import com.example.mqttdroid.tool.User;

public class SetActivity extends AppCompatActivity {

    private TextView fitbit;
    private Button set;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        fitbit=findViewById(R.id.fitbit);
        Bundle bundle=getIntent().getExtras();
        final String id=bundle.getString("email");
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=fitbit.getText().toString();
                send(text,id, Topic.FITBIT);
            }
        });
    }

    private void send(String message,String dest, String topic)
    {
        //invio del messaggio al broker.
    }
}
