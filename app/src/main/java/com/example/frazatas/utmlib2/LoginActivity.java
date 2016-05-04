package com.example.frazatas.utmlib2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by frazatas on 19/4/2016.
 */
public class LoginActivity extends Activity {
    ProgressDialog pDialog;
    EditText name, password;
    String Name, Password;
    Context ctx=this;
    String NAME=null, PASSWORD=null, MATRICSNO=null, USERID=null, FULLNAME=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //session = new SessionManager(getApplicationContext());

        name = (EditText) findViewById(R.id.enterUsername);
        password = (EditText) findViewById(R.id.enterPassword);

        //EditText  = (EditText) findViewById(R.id.search);

    }


    public void LoginButton(View v){
        Name = name.getText().toString();
        Password = password.getText().toString();


        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Attempting for login...");
        pDialog.setIndeterminate(false); pDialog.setCancelable(true);
        pDialog.show();


        BackGround b = new BackGround();
        b.execute(Name, Password);

    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data="";
            int tmpu;

            try {
                URL url = new URL("http://domain/log.php");
                String urlParams = "name="+name+"&password="+password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmpu=is.read())!=-1){
                    data+= (char)tmpu;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err=null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                NAME = user_data.getString("name");
                PASSWORD = user_data.getString("password");
                MATRICSNO = user_data.getString("matricsno");
                FULLNAME = user_data.getString("fullname");
                USERID = user_data.getString("userid");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }
            if(NAME == null ){
                pDialog.dismiss();


                AlertDialog alertDialog = new AlertDialog.Builder(
                        LoginActivity.this).create();
                alertDialog.setTitle("Login failed");

                // Setting Dialog Message
                alertDialog.setMessage("Invalid username and/or password");

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
            else if(NAME !=null){
                Intent i = new Intent(ctx, MainActivity.class);
                i.putExtra("name", NAME);
                i.putExtra("password", PASSWORD);
                i.putExtra("matricsno",MATRICSNO);
                i.putExtra("fullname",FULLNAME);
                i.putExtra("userid", USERID);
                i.putExtra("err", err);
                startActivity(i);}





        }
    }
}
