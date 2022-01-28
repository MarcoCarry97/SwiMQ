package com.example.mqttdroid;

import java.util.ArrayList;

public class MessageQueue extends ArrayList
{
    private int limit;

    public MessageQueue(int limit)
    {
        this.limit=limit;
    }

    public boolean isFull()
    {
        return limit==size();
    }

    public synchronized void send(Object obj) throws InterruptedException
    {
        while(size()>=limit) wait();
        add(obj);
        notify();
    }

    public synchronized Object receive() throws InterruptedException
    {
        while(size()==0) wait();
        Object obj=remove(0);
        notify();
        return obj;
    }
}
