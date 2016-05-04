package com.example.frazatas.utmlib2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchSearch extends ActionBarActivity  {
    ListAdapter adapter ;
    ProgressDialog pDialog;
        String myJSON, Search;
        private static final String TAG_RESULTS="result";
        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "name";
        private static final String TAG_ADD ="address";
    private static final String TAG_CN ="callno";
    private static final String TAG_DES ="des";
        JSONArray peoples = null;

        private Button find;
        private EditText searchable;

    String TITLE=null, CALLNO=null, USER=null, USERID=null;
        ArrayList<HashMap<String, String>> personList;
        ListView list;
    Spinner spinnerwithTextView;
    String[] Subject = new String[]{
            "All Fields",
            "Title",
            "Author",
            "Publisher"
    };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);

            searchable = (EditText) findViewById(R.id.searchbar);
            find = (Button) findViewById(R.id.gobut);



            spinnerwithTextView = (Spinner)findViewById(R.id.spinner1);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    this,R.layout.textview_layout,Subject );
            spinnerArrayAdapter.setDropDownViewResource(R.layout.textview_layout);
            spinnerwithTextView.setAdapter(spinnerArrayAdapter);

            list = (ListView) findViewById(R.id.booklist);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // getting values from selected ListItem
                    String getid = ((TextView) view.findViewById(R.id.bookid))
                            .getText().toString();
                    String idn = ((TextView) view.findViewById(R.id.firstLine))
                            .getText().toString();
                    String address = ((TextView) view.findViewById(R.id.secondLine))
                            .getText().toString();
                    String tiga = ((TextView) view.findViewById(R.id.thirdLine))
                            .getText().toString();
                    String empat = ((TextView) view.findViewById(R.id.lastLine))
                            .getText().toString();
                    String user = getIntent().getStringExtra("userid");
                    //Toast.makeText(getApplicationContext(), "hum " + user, Toast.LENGTH_LONG).show();
                    // Starting single contact activity
                    Intent in = new Intent(getApplicationContext(),
                            BookMore.class);
                    in.putExtra("TAG_ID",getid);
                    in.putExtra(TAG_NAME,idn);
                    in.putExtra(TAG_ADD, address);
                    in.putExtra(TAG_CN,tiga);
                    in.putExtra(TAG_DES, empat);
                    in.putExtra("USER",user);
                    startActivity(in);

                }
            });
            personList = new ArrayList<HashMap<String,String>>();
            //getData();

            searchable.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            });
            find.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Search = searchable.getText().toString();
                    String Text = spinnerwithTextView.getSelectedItem().toString();
                    String UserID = getIntent().getStringExtra("userid");
                    //Toast.makeText(SearchSearch.this, Text, Toast.LENGTH_SHORT).show();
                    list.setAdapter(null);
                    //Toast.makeText(getApplicationContext(), "hum " + UserID, Toast.LENGTH_LONG).show();
                    pDialog = new ProgressDialog(SearchSearch.this);
                    pDialog.setMessage("Searching...");
                    pDialog.setIndeterminate(false); pDialog.setCancelable(true);
                    pDialog.show();
                    getDataSearch(Search, Text, UserID);//,Text);
                }

            });

        }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            personList.clear();
            for(int i=0;i<10;i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);
                String callno = c.getString(TAG_CN);
                String des = c.getString(TAG_DES);
                HashMap<String,String> persons = new HashMap<String,String>();
                persons.put(TAG_ID,id);
                persons.put(TAG_NAME,name);
                persons.put(TAG_ADD, address);
                persons.put(TAG_CN, callno);
                persons.put(TAG_DES, des);
                personList.add(persons);

            }
            ListAdapter adapter = new SimpleAdapter(
                    SearchSearch.this, personList, R.layout.book_list,
                    new String[]{TAG_ID,TAG_NAME,TAG_ADD,TAG_CN,TAG_DES},
                    new int[]{R.id.bookid,R.id.firstLine, R.id.secondLine, R.id.thirdLine, R.id.lastLine}

            );
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void getDataSearch(final String valueIWantToSend1, final String valueIWantToSend2, final String valueIWantToSend3){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                // Depends on your web service


                InputStream inputStream = null;
                String result = null;


                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("valueIWantToSend1", valueIWantToSend1));
                    nameValuePairs.add(new BasicNameValuePair("valueIWantToSend2", valueIWantToSend2));
                    nameValuePairs.add(new BasicNameValuePair("valueIWantToSend3", valueIWantToSend3));
                    DefaultHttpClient httpclient = new DefaultHttpClient();//(new BasicHttpParams());
                    HttpPost httppost = new HttpPost("http://domain/new.php");
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
                myJSON=result;
                showList();
                pDialog.dismiss();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}

