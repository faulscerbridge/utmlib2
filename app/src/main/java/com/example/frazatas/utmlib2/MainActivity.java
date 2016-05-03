package com.example.frazatas.utmlib2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
public ImageButton Book,News,Info,Search,File;
    String USERID=null;
    Context ctx =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListenerOnBook();
        ListenerOnNews();
        ListenerOnInfo();
        ListenerOnSearch();
        ListenerOnFile();

        Intent intent = getIntent();
        USERID = getIntent().getStringExtra("userid");


    }

    public void ListenerOnBook(){
        Book = (ImageButton) findViewById (R.id.BookButton);

        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "Borrow A Book", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, MainBarcode.class);
                i.putExtra("userid", USERID);

                startActivity(i);

            }

        });
    }
    public void ListenerOnNews(){
        News = (ImageButton) findViewById (R.id.NewsButton);

        News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "Renew Borrowing", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, RenewNews.class);
                i.putExtra("userid", USERID);

                startActivity(i);

            }

        });
    }

    public void ListenerOnInfo(){
        Info = (ImageButton) findViewById (R.id.InfoButton);

        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "User Information", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, UserInfo.class);
                i.putExtra("userid", USERID);

                startActivity(i);

            }

        });
    }

    public void ListenerOnSearch(){
        Search = (ImageButton) findViewById (R.id.SearchButton);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "Search A Book", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, SearchSearch.class);
                i.putExtra("userid", USERID);

                startActivity(i);

            }

        });
    }

    public void ListenerOnFile(){
       File = (ImageButton) findViewById (R.id.FileButton);

        File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "Books History", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, HoldResult.class);
                i.putExtra("userid", USERID);

                startActivity(i);}



        });
    }


}
