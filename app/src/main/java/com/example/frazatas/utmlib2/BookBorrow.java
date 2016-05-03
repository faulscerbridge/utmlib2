package com.example.frazatas.utmlib2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by frazatas on 10/4/2016.
 */
public class BookBorrow extends AppCompatActivity{

Context context;
    EditText edittext;
  //  Button buttong;
    private String url1 = "http://www.google.com";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        //ListenerOnBook();
        context = this;

       // buttong =(Button) findViewById(R.id.testhihi);
    }



}
