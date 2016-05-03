package com.example.frazatas.utmlib2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by frazatas on 10/4/2016.
 */
public class RenewNews  extends ActionBarActivity {

    ListAdapter adapter ;
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_TITLE = "title";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE ="tarikh";
    JSONArray peoples = null;

    private Button renew;

    ArrayList<HashMap<String, String>> renewList;
    ListView list;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        renew = (Button) findViewById(R.id.renewbut);

        list = (ListView) findViewById(R.id.listRenew);
        renewList = new ArrayList<HashMap<String,String>>();
        String user = getIntent().getStringExtra("userid");
        getData(user);
        /*checkBox = (CheckBox)findViewById(R.id.selectall);
        checkBox.setChecked(false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                Toast.makeText(RenewNews.this,
                "Select All", Toast.LENGTH_SHORT).show();
            }
        });*/
        renew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(RenewNews.this,
                 //       "Done", Toast.LENGTH_SHORT).show();
                String user = getIntent().getStringExtra("userid");
                sendData(user);

            }

        });

    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                // String name = c.getString(TAG_NAME);
                String tarikh = c.getString(TAG_DATE);
                HashMap<String,String> renew = new HashMap<String,String>();
                renew.put(TAG_TITLE, title);
                // persons.put(TAG_NAME,name);
                renew.put(TAG_DATE, tarikh);
                renewList.clear();
                renewList.add(renew);
            }
            ListAdapter adapter = new SimpleAdapter(
                    RenewNews.this, renewList, R.layout.renew_info,
                    new String[]{TAG_TITLE,TAG_DATE},
                    new int[]{R.id.checkBoxrenew, R.id.renewDate}

            );

            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(final String check2){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... params) {

                // Depends on your web service


                InputStream inputStream = null;
                String result = null;


                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("check2", check2));
                    DefaultHttpClient httpclient = new DefaultHttpClient();//(new BasicHttpParams());
                    HttpPost httppost = new HttpPost("http://utmlibraryapp.site88.net/stat.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //httppost.setHeader("Content-type", "application/json");
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    public void sendData(final String check1){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                // Depends on your web service


                InputStream inputStream = null;
                String result = null;


                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("check1", check1));
                    DefaultHttpClient httpclient = new DefaultHttpClient();//(new BasicHttpParams());
                    HttpPost httppost = new HttpPost("http://utmlibraryapp.site88.net/renew.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    //httppost.setHeader("Content-type", "application/json");
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish) {
                    }
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result){

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
        String user = getIntent().getStringExtra("userid");
        getData(user);
    }


}





