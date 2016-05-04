package com.example.frazatas.utmlib2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
 * Created by frazatas on 14/4/2016.
 */
public class UserInfo extends ActionBarActivity {

    ListAdapter adapter ;
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_TITLE = "title";
    private static final String TAG_REMARKS = "remarks";
    private static final String TAG_DATE ="tarikh";
    JSONArray historypeoples = null;

    private Button renew;

    ArrayList<HashMap<String, String>> historyList;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        list = (ListView) findViewById(R.id.listHistory);
        historyList = new ArrayList<HashMap<String,String>>();
        Intent intent = getIntent();
        String NAME = intent.getExtras().getString("name");
        String MATRICSNO = intent.getExtras().getString("matricsno");
        String USERID = intent.getExtras().getString("userid");
        String user = getIntent().getStringExtra("userid");
        getDataUser(user);

    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            historypeoples = jsonObj.getJSONArray(TAG_RESULTS);
            for(int i=0;i<historypeoples.length();i++){
                JSONObject c = historypeoples.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String remarks = c.getString(TAG_REMARKS);
                String tarikh = c.getString(TAG_DATE);
                HashMap<String,String> history = new HashMap<String,String>();
                history.put(TAG_TITLE, title);
                history.put(TAG_REMARKS,remarks);
                history.put(TAG_DATE, tarikh);
                //historyList.clear();
                historyList.add(history);
            }
            ListAdapter adapter = new SimpleAdapter(
                    UserInfo.this, historyList, R.layout.history_info,
                    new String[]{TAG_TITLE,TAG_DATE,TAG_REMARKS},
                    new int[]{R.id.tajuk, R.id.Date, R.id.remarks}

            );

            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDataUser(final String data1){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                // Depends on your web service


                InputStream inputStream = null;
                String result = null;


                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("data1", data1));
                    DefaultHttpClient httpclient = new DefaultHttpClient();//(new BasicHttpParams());
                    HttpPost httppost = new HttpPost("http://domain/status.php");
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

}






