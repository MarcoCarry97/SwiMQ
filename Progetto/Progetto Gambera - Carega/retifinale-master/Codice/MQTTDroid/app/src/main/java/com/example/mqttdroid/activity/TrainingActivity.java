package com.example.mqttdroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mqttdroid.R;
import com.example.mqttdroid.tool.Coach;
import com.example.mqttdroid.tool.Exercise;
import com.example.mqttdroid.tool.PubTask;
import com.example.mqttdroid.tool.Swimmer;
import com.example.mqttdroid.tool.Training;
import com.example.mqttdroid.tool.User;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TrainingActivity extends AppCompatActivity
{
    private TextView coach,type,length,swims,date;
    private Button sub;
    private User user;
    private String email;
    private Training training;
    private MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        try
        {
            Bundle bundle=getIntent().getExtras();
            email=bundle.getString("email");
            //user=new Swimmer(id);
            //String fit=bundle.getString("fitbit");
            //((Swimmer) user).setFitbit(fit);
            training=bundle.getParcelable("training");
            coach=findViewById(R.id.coach);
            coach.setText(training.getCoach().getId());
            length=findViewById(R.id.length);
            int len=training.getTotalLength();
            length.setText(String.valueOf(len));
            date=findViewById(R.id.date);
            date.setText(training.getDate().toString());
            sub=findViewById(R.id.sub);
            Calendar calendar=Calendar.getInstance();
            final int currentYear=calendar.get(Calendar.YEAR);
            final int currentMonth=calendar.get(Calendar.MONTH);
            final int day=calendar.get(Calendar.DAY_OF_MONTH);
            final Date currentDate=new Date(new SimpleDateFormat("dd/MM/yyyy").parse(day+"/"+currentMonth+"/"+currentYear).getTime());
            sub.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   // if(training.getDate().getTime()==currentDate.getTime())
                   //{
                        try {
                            Intent intent=new Intent(TrainingActivity.this,FitActivity.class);
                            intent.putExtra("training",training);
                            intent.putExtra("email",email);
                            Log.d("TRAINING",training.toString());
                            startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                   //}
                    //else Toast.makeText(TrainingActivity.this,"You can only do trainings of today",Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {

        }
    }

    private Training getTraining(Bundle bundle)
    {
        //prendi allenamento da broker;
        return null;
    }


}
