package com.example.mqttdroid.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
import org.json.JSONException;
import org.json.JSONObject;

public class CreateActivity extends SimpleActivity {

    private MqttAndroidClient client;
    private Button dateButton;
    private TextView dateText;
    private long millis=0;
    private Spinner[] types;
    private EditText[] nums;
    private String[] values;
    private Button submit;
    private Tools tools;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        tools=new Tools(CreateActivity.this);
        try {
            client=new MqttAndroidClient(CreateActivity.this,"tcp://193.206.55.23:1883",String.valueOf(System.currentTimeMillis()));
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setWill("retilab/swim", "I'm gone :(".getBytes(), 0, false);
            Bundle bundle=getIntent().getExtras();
            email=bundle.getString("email");
            client.connect(options).setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    tools.toast("Connected");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                }
            });
            dateButton=findViewById(R.id.date);
            dateText=findViewById(R.id.tvDate);
            dateButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    final Calendar calendar=Calendar.getInstance();
                    final int currentYear=calendar.get(Calendar.YEAR);
                    final int currentMonth=calendar.get(Calendar.MONTH);
                    final int day=calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog=new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            try {
                                String curDate=day+"/"+(currentMonth+1)+"/"+currentYear;
                                String chosenDate=dayOfMonth+"/"+(month+1)+"/"+year;
                                long curMillis=new SimpleDateFormat("dd/MM/yyyy").parse(curDate).getTime();
                                long dateMillis=new SimpleDateFormat("dd/MM/yyyy").parse(chosenDate).getTime();
                                Date date=null;
                                String dateString=null;
                                if(dateMillis>=curMillis)
                                {
                                    date=new Date(dateMillis);
                                    dateString=chosenDate;
                                }
                                else
                                {
                                    date=new Date(curMillis);
                                    dateString=curDate;
                                }
                                dateText.setText(dateString);
                                millis=new SimpleDateFormat("dd/MM/yyyy").parse(dateString).getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    },currentYear,currentMonth,day);
                    dialog.show();
                }
            });
            types=new Spinner[4];
            types[0]=findViewById(R.id.esercizio1);
            types[1]=findViewById(R.id.esercizio2);
            types[2]=findViewById(R.id.esercizio3);
            types[3]=findViewById(R.id.esercizio4);
            nums=new EditText[4];
            nums[0]=findViewById(R.id.nVasche1);
            nums[1]=findViewById(R.id.nVasche2);
            nums[2]=findViewById(R.id.nVasche3);
            nums[3]=findViewById(R.id.nVasche4);
            values=new String[]{"","","",""};
            for(int i=0;i<4;i++)
            {
                final int finalI = i;
                SpinnerAdapter adapter=ArrayAdapter.createFromResource(CreateActivity.this,R.array.style,android.R.layout.simple_spinner_dropdown_item);
                types[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        values[finalI]=types[finalI].getAdapter().getItem(position).toString();
                        Log.d("VAL"+finalI,values[finalI]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                types[i].setAdapter(adapter);
            }
            submit=findViewById(R.id.sub);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(millis==0)
                            throw new IllegalArgumentException("Select a date");
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("date", millis);
                        map.put("email",email);
                        for (int i = 0; i < 4; i++)
                        {
                            if(!nums[i].getText().toString().matches("[0-9]{1}"))
                                throw new IllegalArgumentException("NaN");
                            map.put("exe" + (i + 1), values[i] + "-" + nums[i].getText().toString());
                        }
                        PubTask pub = new PubTask(client, Topic.CREATE);
                        pub.execute(map);
                        SubTask sub=new SubTask(client, CreateActivity.this, Topic.CREATE + "/" + email, new IMqttMessageListener() {
                            @Override
                            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                                final JSONObject json=new JSONObject(new String(mqttMessage.getPayload()));
                                Log.d("JSON",json.toString(3));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try
                                        {
                                            if(json.getBoolean("success"))
                                                tools.toast("Training created");
                                            else tools.toast("Training just present");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                        sub.execute();
                    }
                    catch (Exception e)
                    {
                        tools.toast(e.getMessage());
                        Log.e("ERROR",e.getMessage());
                    }
                }
            });
        }
        catch (MqttException e) {
            e.printStackTrace();
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
