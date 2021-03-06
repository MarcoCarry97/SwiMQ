package com.example.mqttdroid.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tools
{
    private Context context;

    public Tools(Context context)
    {
        this.context=context;
    }

    public void toast(int messageId)
    {
        toast(context.getString(messageId));
    }

    public void toast(String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        if(base64Str!=null)
        {
            byte[] bytes= Base64.decode(base64Str, Base64.DEFAULT);
            Bitmap image= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return image;
        }
        else return null;
    }

    public static String convert(Bitmap bitmap)
    {
       if(bitmap!=null)
       {
           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
           byte[] byteArray = byteArrayOutputStream .toByteArray();
           String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
           return encoded;
       }
       else return null;
    }

    public Bitmap download(String action)
    {
        boolean ok=false;
        try {
            URL url = new URL(action);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            Log.d("HTTP","CONNECT");
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            Log.d("HTTP","DECODE");
            ok=true;
            return bitmap;
        } catch (IOException e) {
            // Log exception
            Log.e("EXCEPTION",e.getMessage());
            return null;
        }
        finally {
            if(ok) Log.d("IMAGE","OK");
            else Log.e("IMAGE","ERROR");
        }
    }
}
