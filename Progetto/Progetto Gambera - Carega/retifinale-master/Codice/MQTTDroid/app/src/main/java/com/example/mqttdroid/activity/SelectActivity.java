package com.example.mqttdroid.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mqttdroid.R;
import com.example.mqttdroid.Topic;
import com.example.mqttdroid.tool.Exercise;
import com.example.mqttdroid.tool.PubTask;
import com.example.mqttdroid.tool.SubTask;
import com.example.mqttdroid.tool.Tools;
import com.example.mqttdroid.tool.Training;
import com.example.mqttdroid.tool.TrainingAdapter;
import com.example.mqttdroid.tool.User;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectActivity extends AppCompatActivity
{
    private ListView trainings;
    //private User user;
    private MqttAndroidClient client;
    private String email;
    private Tools tools;
    private Button button;
    private ArrayList<Training> trainList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select);
            trainList=new ArrayList<Training>();
            Log.d("CREATE","OK");
            tools=new Tools(SelectActivity.this);
            trainings=findViewById(R.id.list_train);
            ArrayList<Training> list=new ArrayList<Training>();
            Bundle bundle=getIntent().getExtras();
            email=bundle.getString("email");
            Log.d("EMAIL",email);
            Intent intent=new Intent();
            client=new MqttAndroidClient(SelectActivity.this,"tcp://193.206.55.23:1883",String.valueOf(System.currentTimeMillis()));
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill("retilab/swim/LWT", "I'm gone :(SELECT".getBytes(), 0, false);
            client.connect(options).setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    tools.toast("Connected");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                }
            });
            TrainingAdapter adapter=new TrainingAdapter(getApplicationContext(),R.layout.train_item,list);
            trainings.setAdapter(adapter);
            trainings.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    //visualizza informazioni training
                    Intent intent=new Intent(getApplicationContext(),TrainingActivity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("training",trainList.get(position));
                    startActivity(intent);
                }
            });

            button=findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final HashMap<String,Object> map=new HashMap<String, Object>();
                    map.put("email",email);
                    PubTask pub=new PubTask(client,Topic.NEW_TRAIN);
                    pub.execute(map);
                    SubTask sub=new SubTask(client,SelectActivity.this,Topic.NEW_TRAIN+"/"+email, new IMqttMessageListener() {
                        @Override
                        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                            try
                            {
                                final JSONArray array=new JSONArray(new String(mqttMessage.getPayload()));
                                trainList=new ArrayList<Training>();
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject json=array.getJSONObject(i);
                                    String coach=json.getString("email");
                                    Date date =new Date(json.getLong("date"));
                                    ArrayList<Exercise> exes=new ArrayList<Exercise>();
                                    for(int j=0;j<4;j++)
                                    {
                                        String item=json.getString("exe"+(j+1));
                                        String parts[]=item.split("-");
                                        Exercise exe=new Exercise(parts[0],Integer.parseInt(parts[1]));
                                        exes.add(exe);
                                    }
                                    Training training=new Training(coach,date,exes);
                                    trainList.add(training);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TrainingAdapter adapter=new TrainingAdapter(SelectActivity.this,R.layout.train_item,trainList);
                                        trainings.setAdapter(adapter);
                                    }
                                });
                                Log.d("SUB","FINISH");
                            }
                            catch (Exception e)
                            {
                                Log.e("ERROR",e.getMessage());
                            }
                        }
                    });
                    sub.execute();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
