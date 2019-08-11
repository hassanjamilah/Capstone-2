package com.andalus.a_zmedicines.Utils;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {


    public String getHttpResponse(URL url) throws IOException {
        //oepening the conneciton
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //x is used to store all the data
        StringBuilder x = new StringBuilder();
        try {
            InputStream in = con.getInputStream();
            Scanner scanner = new Scanner(in);
            while (scanner.hasNext()) {
                x.append(" ").append(scanner.next());
            }
        } finally {
            //closing the connection to free resources
            con.disconnect();
        }

        return x.toString();
    }


    public String getHttpResponse1(URL url) {
        Uri uri = Uri.parse("https://azmedicine.000webhostapp.com/Drugs.json");
        try {
            url = new URL(uri.toString());
            Log.i("hassan", "The Url is : " + url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        StringBuilder builder = new StringBuilder();
        try {
            con = (HttpURLConnection) url.openConnection();

            InputStream in = con.getInputStream();
            Scanner scan = new Scanner(in);
            while (scan.hasNext()) {
                builder.append(" ").append(scan.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        Log.i("hassan", "The result is : " + builder.toString());
        return builder.toString();

    }


}
