package com.example.mqttdroid.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mqttdroid.Topic;
import com.example.mqttdroid.tool.Coach;
import com.example.mqttdroid.R;
import com.example.mqttdroid.tool.PubTask;
import com.example.mqttdroid.tool.SubTask;
import com.example.mqttdroid.tool.Swimmer;
import com.example.mqttdroid.tool.Tools;
import com.example.mqttdroid.tool.User;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class MainActivity extends SimpleActivity
{
    private Button login,signup;
    private TextView mailBox, pwdBox;
    private MqttAndroidClient client;
    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mailBox=findViewById(R.id.email);
        pwdBox=findViewById(R.id.pass);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        tools=new Tools(MainActivity.this);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //requisito Login;
            }
        });
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try{
                    String email=mailBox.getText().toString();
                    String pwd=mailBox.getText().toString();
                    if(email.matches("[0-9a-z/./-_]{1,}@[a-z]{1,}/.[a-z]{2,3}"))
                        throw new IllegalArgumentException("email non valida");
                    Intent intent=new Intent(getApplicationContext(),SignupActivity.class);

                    intent.putExtra("email",email);
                    intent.putExtra("password",pwd);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    Log.e("Error",e.getMessage());
                }
            }
        });
        final String clientId = Long.toString(new Date().getTime()) + "-pub";
        try {
            client=new MqttAndroidClient(getApplication(),"tcp://193.206.55.23:1883",clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill("retilab/swim", "I'm gone :(".getBytes(), 0, false);
            IMqttToken token=client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
                    //Thread thread=new Thread(new SubDroid(getApplication(),"retilab/time",clientId,text));
                    //thread.start();
                    //exam.spring.mqttclientsample.SubTask task=new exam.spring.mqttclientsample.SubTask(client,MainActivity.this,text);
                    //task.execute();
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String email=mailBox.getText().toString();
               String pwd=pwdBox.getText().toString();
               if(!email.equals("") && !pwd.equals(""))
               {
                   Login loginTask=new Login(email,pwd);
                   loginTask.execute();
               }
               else Toast.makeText(MainActivity.this,"Compila tutti i campi",Toast.LENGTH_LONG).show();

            }
        });
    }

    private class Login extends AsyncTask<String,String,String>
    {
        private String email,pwd;

        public Login(String email,String pwd)
        {
            this.email=email;
            this.pwd=pwd;
        }

        @Override
        protected String doInBackground(String... strings)
        {
            //invia email e pwd a MQTT
           String result=null;
            PubTask pub=new PubTask(client, Topic.LOGIN);
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("email",email);
            map.put("password",pwd);
            pub.execute(map);
            //publishProgress(result);
            Log.d("TOPIC",Topic.LOGIN+"/"+email);
            SubTask sub=new SubTask(client, MainActivity.this, Topic.LOGIN + "/" + email, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String message=new String(mqttMessage.getPayload());
                    final JSONObject json=new JSONObject(message);
                   if(json.getBoolean("correct"))
                   {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               try {
                                   Intent intent=new Intent(MainActivity.this,SwimActivity.class);
                                   intent.putExtra("email",email);
                                   intent.putExtra("type",json.getString("type"));
                                   Log.d("STAT","UI");
                                   client.close();
                                   startActivity(intent);
                               } catch (JSONException e) {
                                   Log.e("ERROR",e.getMessage());
                                   e.printStackTrace(); }
                           }
                       });
                   }
                   else
                   {
                       runOnUiThread(
                               new Runnable() {
                                   @Override
                                   public void run() {
                                       tools.toast("Incorrect email or password");
                                   }
                               }
                       );
                   }
                }
            });
            sub.execute();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            switch(values[0])
            {
                case("SWIM"): createSwimmer(email); break;
                case("COACH"): createCoach(email); break;
                default: toast("Error: Wrong credentials");
            }
        }
    }

    private void createSwimmer(String email)
    {
        Swimmer swimmer=new Swimmer(email);
        show(swimmer);
    }

    private void createCoach(String email)
    {
        Coach coach=new Coach(email);
        show(coach);
    }

    private void show(User user)
    {
        Intent intent=new Intent(getApplicationContext(),SwimActivity.class);
        intent.putExtra("email",user.getId());
        String type=(user instanceof Swimmer) ? "SWIM" : "COACH";
        intent.putExtra("type",type);
        startActivity(intent);
    }

    private void toast(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
