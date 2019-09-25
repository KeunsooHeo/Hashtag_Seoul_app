package com.cuk.seosom;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginInActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_login, button_create;
    EditText editText_id, editText_pw;
    String id, pw;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        button_login = (Button) findViewById(R.id.button_login);
        button_create = (Button) findViewById(R.id.button_create);
        editText_id = (EditText) findViewById(R.id.editText_id);
        editText_pw = (EditText) findViewById(R.id.editText_pw);

        editText_pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        actionSearch();
                        break;
                    default:
                        actionSearch();
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });
        button_login.setOnClickListener(this);
        button_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == button_login){
            actionSearch();
        }
        else if(v == button_create){
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        }
    }

    void actionSearch(){
        id = editText_id.getText().toString();
        pw = editText_pw.getText().toString();
        if(pw.length() != 6){
            Toast.makeText(this,"비밀번호 6자리를 입력해주세요.",Toast.LENGTH_LONG).show();
            return;
        }
        if(id.equals("") || pw.equals("")){
            Toast.makeText(this, "아이디 혹은 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor cursor = db.rawQuery("select id, pw from user",null);

        while(cursor.moveToNext()){
            if(cursor.getString(0).equals(id)){
                System.out.println("id : "+ cursor.getString(0));
                if(cursor.getString(1).equals(pw)){
                    Cursor cursor1 = db.rawQuery("select num from userlike where id=?",new String[]{id});
                    if(!cursor1.moveToNext()){
                        Cursor cursor2 = db.rawQuery("select hash from userinfo where id=?",new String[]{id});
                        BufferedReader br = null;
                        String line;
                        String cvsSplitBy = ",";
                        InputStream in = getResources().openRawResource(R.raw.data);
                        while(cursor2.moveToNext()){
                            try{
                                InputStreamReader is = new InputStreamReader(in, "UTF-8");
                                br = new BufferedReader(is);
                                while ((line = br.readLine()) != null) {
                                    String[] field = line.split(cvsSplitBy);
                                    if (field[Integer.parseInt(cursor1.getString(0))].equals("1")) {
                                        System.out.println(field[1]);
                                        db.execSQL("insert into userlike (id, num) values (?,?)",new String[]{id, field[0]});
                                    }
                                }
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    return;
                }
            }
            else{
                continue;
            }
        }
        Toast.makeText(this,"아이디 혹은 비밀번호를 다시 확인해주세요.",Toast.LENGTH_LONG).show();
    }
}
