package com.cuk.seosom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<LinearLayout> contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        contents.add((LinearLayout) findViewById(R.id.content_1));
        contents.add((LinearLayout) findViewById(R.id.content_2));
        contents.add((LinearLayout) findViewById(R.id.content_3));
        contents.add((LinearLayout) findViewById(R.id.content_4));
        contents.add((LinearLayout) findViewById(R.id.content_5));
        contents.add((LinearLayout) findViewById(R.id.content_6));
        contents.add((LinearLayout) findViewById(R.id.content_7));
        contents.add((LinearLayout) findViewById(R.id.content_8));
        contents.add((LinearLayout) findViewById(R.id.content_9));
        contents.add((LinearLayout) findViewById(R.id.content_10));
        contents.add((LinearLayout) findViewById(R.id.content_11));
        contents.add((LinearLayout) findViewById(R.id.content_12));
        contents.add((LinearLayout) findViewById(R.id.content_13));
        contents.add((LinearLayout) findViewById(R.id.content_14));
        contents.add((LinearLayout) findViewById(R.id.content_15));
        contents.add((LinearLayout) findViewById(R.id.content_16));
        for (LinearLayout content:contents){
            content.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int index = contents.indexOf((LinearLayout) v);
        Intent result = new Intent();
        result.putExtra("chosen",MainActivity.H_START+index);
        finish();
    }
}
