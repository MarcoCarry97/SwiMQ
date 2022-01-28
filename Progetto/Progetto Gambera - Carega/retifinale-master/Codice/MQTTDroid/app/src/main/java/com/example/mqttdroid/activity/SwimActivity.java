package com.example.mqttdroid.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mqttdroid.tool.Coach;
import com.example.mqttdroid.R;
import com.example.mqttdroid.tool.Swimmer;
import com.example.mqttdroid.tool.User;

public class SwimActivity extends AppCompatActivity
{
    private User user;
    private Button search, show, post, create;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swim);
        //connect = findViewById(R.id.put_fit);
        search = findViewById(R.id.show_train);
        show = findViewById(R.id.show_old_train);
        post = findViewById(R.id.post_train);
        create = findViewById(R.id.create_train);

        /*connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //associa fitbit
                goTo(SetActivity.class);
            }
        });*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selezione allenamento
                goTo(SelectActivity.class);
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostra storico
                goTo(OldActivity.class);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //analisi post-allenamento
                goTo(PostActivity.class);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //crea allenamento
                goTo(CreateActivity.class);
            }
        });

        Bundle bundle = getIntent().getExtras();

        String email, type;

        email = bundle.getString("email");
        type = bundle.getString("type");

        if(type.equals("SWIMMER"))
        {
            user = new Swimmer(email);
            // connect.setVisibility(View.VISIBLE);
            post.setVisibility(View.VISIBLE);
            show.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
        }

        else {
            user = new Coach(email);
            create.setVisibility(View.VISIBLE);
        }

    }

    private void goTo(Class activity)
    {
        Intent intent=new Intent(getApplicationContext(),activity);
        intent.putExtra("email",user.getId());
        intent.putExtra("type",(user instanceof Swimmer) ? "SWIMMER" : "COACH");
        Log.d("EMAIL SWIM",user.getId());
        startActivity(intent);
    }
}
