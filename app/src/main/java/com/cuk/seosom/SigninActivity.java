package com.cuk.seosom;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    Button button_ok, button_cancel;
    String id, pw;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        db = openOrCreateDatabase("user", MODE_PRIVATE, null);
        editText_id = (EditText) findViewById(R.id.editText_create_id);
        editText_pw = (EditText) findViewById(R.id.editText_create_pw);

        button_ok = (Button) findViewById(R.id.button_create_ok);
        button_ok.setOnClickListener(this);

        button_cancel = (Button) findViewById(R.id.button_create_cancel);
        button_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == button_ok){
            id = editText_id.getText().toString();
            pw = editText_pw.getText().toString();
            if(checkid(id)){
                Toast.makeText(this,"존재하는 아이디입니다.",Toast.LENGTH_LONG).show();
            }
            else{
                db.execSQL("insert into user (id, pw) values (?,?)", new String[]{id, pw});
                // -- 다음 설정창으로
            }
        } else if(v == button_cancel){
            finish();
        }
    }

    boolean checkid(String id){
        Cursor cursor= db.rawQuery("select id, pw from user", null);
        while (cursor.moveToNext()){
            if(id.equals(cursor.getString(0))) return true;
        }
        return false;
    }
}
