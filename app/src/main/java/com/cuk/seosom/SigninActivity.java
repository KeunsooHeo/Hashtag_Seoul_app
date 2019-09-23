package com.cuk.seosom;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText_id, editText_pw;
    Button button_ok;
    String id, pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editText_id = (EditText) findViewById(R.id.editText_create_id);
        editText_pw = (EditText) findViewById(R.id.editText_create_pw);

        button_ok = (Button) findViewById(R.id.button_create_ok);
        button_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        id = editText_id.getText().toString();
        pw = editText_pw.getText().toString();
        if(checkid(id)){
            Toast.makeText(this,"존재하는 아이디입니다.",Toast.LENGTH_LONG).show();
        }
        else{

        }
    }

    boolean checkid(String id){
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        String[] columnText;
        try{

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
