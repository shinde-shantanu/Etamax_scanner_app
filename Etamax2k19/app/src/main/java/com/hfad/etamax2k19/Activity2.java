package com.hfad.etamax2k19;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.gms.common.api.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Activity2 extends AppCompatActivity {

    String id;

    /*private String getQuery(List<Pair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }120373
    830263*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String branch = intent.getStringExtra("branch");
        String rollNo = intent.getStringExtra("rollNo");
        String cost = intent.getStringExtra("cost");
        id = intent.getStringExtra("id");
        String team = intent.getStringExtra("team");
        int teamc = intent.getIntExtra("teamc",0);
        //{"name":null,"branch":null,"rollNo":null,"cost":0}

        TextView name_ = (TextView) findViewById(R.id.name);
        TextView branch_ = (TextView) findViewById(R.id.branch);
        TextView rollNo_ = (TextView) findViewById(R.id.rollNo);
        TextView cost_ = (TextView) findViewById(R.id.cost);
        TextView team_ = (TextView) findViewById(R.id.team);
        name_.setText("Name: "+name);
        branch_.setText("Branch: "+branch);
        rollNo_.setText("RollNo: "+rollNo);
        cost_.setText("Cost: "+cost);
        if (teamc>0){
            team_.setText("Unpaid team events: \n"+team);
        }

    }

    public void paid(View view){
        try {
            URL url = new URL("https://etamax.fcrit.ac.in/sp/api/admin/setPaid.php");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");


            JSONObject object = new JSONObject();
            object.put("pass", "et198079963");
            object.put("id", id);


            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(object.toString());
            writer.flush();
            writer.close();
            os.close();

            connection.connect();

            Toast.makeText(this, connection.getResponseMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Activity2.this, MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            //Log.wtf("flag2", "flag2");
            e.printStackTrace();
        }
    }

}
