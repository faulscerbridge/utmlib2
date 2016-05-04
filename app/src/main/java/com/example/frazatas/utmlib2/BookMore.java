package com.example.frazatas.utmlib2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frazatas on 16/4/2016.
 */
public class BookMore extends Activity {


    private static final String TAG_NAME = "title";
    private static final String TAG_ADD = "address";
    private static final String TAG_CN = "author";
    private static final String TAG_DES = "callno";
    ProgressDialog pDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);
        Button hold = (Button) findViewById(R.id.holdBtn);

        Intent in = getIntent();

        // Get JSON values from previous intent

        String title = in.getStringExtra(TAG_NAME);
        String address = in.getStringExtra(TAG_ADD);
        String author = in.getStringExtra(TAG_CN);
        String callno = in.getStringExtra(TAG_DES);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.booktitle);
        TextView lblEmail = (TextView) findViewById(R.id.detailContent);
        TextView lblName2 = (TextView) findViewById(R.id.authorContent);
        TextView lblEmail2 = (TextView) findViewById(R.id.callnoContent);


        lblName.setText(title);
        lblEmail.setText(address);
        lblName2.setText(author);
        lblEmail2.setText(callno);

        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserID = getIntent().getStringExtra("USER");
                String id = getIntent().getStringExtra("TAG_ID");
                //Toast.makeText(SearchSearch.this, Text, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "hum " + UserID, Toast.LENGTH_LONG).show();
                pDialog = new ProgressDialog(BookMore.this);
                pDialog.setMessage("Searching...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                getDataHold(UserID,id);//,Text);
            }

        });

    }
    public void getDataHold(final String userid, final String ID){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                // Depends on your web service


                InputStream inputStream = null;
                String result = null;


                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("userid", userid));
                    nameValuePairs.add(new BasicNameValuePair("ID", ID));

                    DefaultHttpClient httpclient = new DefaultHttpClient();//(new BasicHttpParams());
                    HttpPost httppost = new HttpPost("http://domain/bookhold.php");
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
                //Toast.makeText(SearchSearch.this, result, Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
