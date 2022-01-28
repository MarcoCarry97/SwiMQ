package com.example.mqttdroid.tool;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mqttdroid.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.util.ArrayList;

public class TrainingAdapter extends ArrayAdapter<Training>
{
    private ArrayList<Training> trainings;
    private int resource;

    public TrainingAdapter( Context context, int resource,Iterable<Training> trainings)
    {
        super(context, resource);
        this.trainings= (ArrayList<Training>) trainings;
        this.resource=resource;
    }

    public void update(ArrayList<Training> list)
    {
        trainings.addAll(list);
    }

    @Override
    public int getCount() {
        return trainings.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView=View.inflate(getContext(),resource,null);
        TextView distance=convertView.findViewById(R.id.distance);
        TextView type=convertView.findViewById(R.id.distance);
        TextView coach=convertView.findViewById(R.id.distance);
        TextView date=convertView.findViewById(R.id.date);
        distance.setText(trainings.get(position).getTotalLength()+" m");
        distance.setText(trainings.get(position).getCoach().getId());
        distance.setText(trainings.get(position).getDate().toString());
        return convertView;
    }
}
