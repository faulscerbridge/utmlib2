package com.example.frazatas.utmlib2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by frazatas on 10/4/2016.
 */
public class HistoryFile  extends AppCompatActivity {

    EditText searchable;
    Button find;
    String Searchable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        find = (Button) findViewById(R.id.gobut);
        //ListenerOnBook();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rest1();
            }

        });
    }

    public void Rest1() {
        class REST extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                HttpURLConnection urlConnection = null;
                String json = null;
                // The Username & Password
                final EditText searchable = (EditText) findViewById(R.id.searchbar);
                String Searchable = (String) searchable.getText().toString();

                // -----------------------

                try {
                    HttpResponse response;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("Searchable", Searchable);
                    json = jsonObject.toString();
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://domain/new.php");
                    httpPost.setEntity(new StringEntity(json, "UTF-8"));
                    httpPost.setHeader("Content-Type", "application/json");
                    httpPost.setHeader("Accept-Encoding", "application/json");
                    httpPost.setHeader("Accept-Language", "en-US");
                    response = httpClient.execute(httpPost);
                    String sresponse = response.getEntity().toString();
                    Log.w("QueingSystem", sresponse);
                    Log.w("QueingSystem", EntityUtils.toString(response.getEntity()));
                } catch (Exception e) {
                    Log.d("InputStream", e.getLocalizedMessage());

                } finally {
        /* nothing to do here */
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (result != null) {
                    // do something
                } else {
                    // error occured
                }
            }
        }
    }
}
