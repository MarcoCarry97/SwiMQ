package com.example.mqttdroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mqttdroid.R;
import com.example.mqttdroid.Topic;
import com.example.mqttdroid.tool.PubTask;
import com.example.mqttdroid.tool.SubTask;
import com.example.mqttdroid.tool.Tools;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity
{
    private MqttAndroidClient client;

    private EditText email, checkMail, pwd, checkPwd;
    private Button signup;
    private Tools tools;
    private RadioGroup radios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tools=new Tools(SignupActivity.this);
        final String clientId = Long.toString(new Date().getTime()) + "-pub";
        email=findViewById(R.id.email);
        checkMail=findViewById(R.id.email2);
        pwd=findViewById(R.id.pass);
        checkPwd=findViewById(R.id.pass2);
        radios=findViewById(R.id.group);
        try {
            client=new MqttAndroidClient(getApplication(),"tcp://193.206.55.23:1883",clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill("retilab/swim", "I'm gone :(".getBytes(), 1, false);
            IMqttToken token=client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Toast.makeText(SignupActivity.this,"Connected",Toast.LENGTH_LONG).show();
                    //Thread thread=new Thread(new SubDroid(getApplication(),"retilab/time",clientId,text));
                    //thread.start();
                    //exam.spring.mqttclientsample.SubTask task=new exam.spring.mqttclientsample.SubTask(client,MainActivity.this,text);
                    //task.execute();
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                }
            });
            signup=findViewById(R.id.signup);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String mailText=email.getText().toString();
                    String mailCheck=checkMail.getText().toString();
                    String pwdText=pwd.getText().toString();
                    String pwdCheck=checkPwd.getText().toString();
                    String resRadio= radios.getCheckedRadioButtonId()==R.id.swimmer ? "SWIMMER" : "COACH";
                    if(mailText.equals(mailCheck) && pwdCheck.equals(pwdText) && !mailText.equals("") && !pwdText.equals(""))
                    {
                        PubTask task=new PubTask(client, Topic.SIGNUP);
                        HashMap<String,Object> map=new HashMap<String,Object>();
                        map.put("email",mailText);
                        map.put("password",pwdCheck );
                        map.put("type",resRadio);
                        task.execute(map);
                        Log.d("TOPIC:",Topic.SIGNUP+"/"+mailText);
                        SubTask sub=new SubTask(client, SignupActivity.this, Topic.SIGNUP+"/"+mailText, new IMqttMessageListener() {
                            @Override
                            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                                Log.d("STAT","SONO DENTRO");
                                String content=new String(mqttMessage.getPayload());
                                JSONObject json=new JSONObject(content);
                                Log.d("JSON","OK");
                                boolean find=json.getBoolean("find");
                                Log.d("FIND",String.valueOf(find));
                                //boolean correct=json.getBoolean("correct");
                                //String toast=null;
                                if(find) {
                                    Log.d("STAT","TOAST");
                                    final String toast=mailText+" already used";
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tools.toast(toast);
                                        }
                                    });
                                    //tools.toast(toast);
                                }
                                else
                                {
                                    final String toast="User created";
                                    Log.d("OUT","OUT");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("IN","IN");
                                            tools.toast(toast);
                                            Log.d("OKOKOKOKOK","OKOKOKOKOKOK");
                                            finish();
                                        }
                                    });
                                    //tools.toast(toast);
                                   // finish();
                                }
                                Log.d("STAT","FINISHED");
                            }
                        });
                        sub.execute();

                    }
                    else Toast.makeText(SignupActivity.this,"emails or passwords are not equals!",Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(SignupActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.unregisterResources();
        client.close();
    }

    protected void onResume() {
        super.onResume();
        try {
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        //client.close();
    }

    protected void onPause() {
        super.onPause();
        //client.unregisterResources();
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
