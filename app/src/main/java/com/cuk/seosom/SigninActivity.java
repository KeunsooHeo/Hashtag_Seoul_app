package com.cuk.seosom;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout frame1, frame2, frame3, frame4;
    LinearLayout ok1, ok2, ok3;
    LinearLayout s1_kid, s1_youth, s1_old;
    LinearLayout s2_multi, s2_dis;
    LinearLayout s3_edu, s3_fin, s3_cul, s3_thing, s3_cons, s3_medi, s3_house, s3_job, s3_faci;
    boolean[] isClicked;
    EditText editText_id, editText_pw;
    Button button_ok, button_cancel;
    String id, pw;
    SQLiteDatabase db;
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        db = openOrCreateDatabase("user", MODE_PRIVATE, null);
        editText_id = (EditText) findViewById(R.id.editText_create_id);
        editText_pw = (EditText) findViewById(R.id.editText_create_pw);

        button_ok = (Button) findViewById(R.id.button_create_ok);
        button_ok.setOnClickListener(this);

        button_cancel = (Button) findViewById(R.id.button_create_cancel);
        button_cancel.setOnClickListener(this);

        frame1 = (ConstraintLayout) findViewById(R.id.frame1);
        frame2 = (ConstraintLayout) findViewById(R.id.frame2);
        frame3 = (ConstraintLayout) findViewById(R.id.frame3);
        frame4 = (ConstraintLayout) findViewById(R.id.frame4);

        s1_kid = (LinearLayout) findViewById(R.id.signin1_1_kid);
        s1_youth = (LinearLayout) findViewById(R.id.signin1_2_youth);
        s1_old = (LinearLayout) findViewById(R.id.signin1_3_old);

        s1_kid.setOnClickListener(this);
        s1_youth.setOnClickListener(this);
        s1_old.setOnClickListener(this);

        s2_multi = (LinearLayout) findViewById(R.id.signin2_1_multi);
        s2_dis = (LinearLayout) findViewById(R.id.signin2_2_disable);

        s2_multi.setOnClickListener(this);
        s2_dis.setOnClickListener(this);

        s3_edu = (LinearLayout) findViewById(R.id.signin4_1_edu);
        s3_fin = (LinearLayout) findViewById(R.id.signin4_2_fin);
        s3_cul = (LinearLayout) findViewById(R.id.signin4_3_cul);
        s3_thing = (LinearLayout) findViewById(R.id.signin4_4_thing);
        s3_cons = (LinearLayout) findViewById(R.id.signin4_5_cons);
        s3_medi = (LinearLayout) findViewById(R.id.signin4_6_medi);
        s3_house = (LinearLayout) findViewById(R.id.signin4_7_house);
        s3_job = (LinearLayout) findViewById(R.id.signin4_8_job);
        s3_faci = (LinearLayout) findViewById(R.id.signin4_9_faci);

        s3_edu.setOnClickListener(this);
        s3_fin.setOnClickListener(this);
        s3_cul.setOnClickListener(this);
        s3_thing.setOnClickListener(this);
        s3_cons.setOnClickListener(this);
        s3_medi.setOnClickListener(this);
        s3_house.setOnClickListener(this);
        s3_job .setOnClickListener(this);
        s3_faci.setOnClickListener(this);

        ok1 = (LinearLayout) findViewById(R.id.signin1_4_no);
        ok2 = (LinearLayout) findViewById(R.id.signin2_ok);
        ok3 = (LinearLayout) findViewById(R.id.signin4_ok);

        ok1.setOnClickListener(this);
        ok2.setOnClickListener(this);
        ok3.setOnClickListener(this);

        isClicked = new boolean[MainActivity.H_END+1];
        Arrays.fill(isClicked, false);
    }



    boolean checkid(String id){
        Cursor cursor= db.rawQuery("select id, pw from user", null);
        while (cursor.moveToNext()){
            if(id.equals(cursor.getString(0))) return true;
        }
        return false;
    }

    void insertUserInfo(int col) {
        db.execSQL("insert into userinfo (id, hash) values (?,?)", new String[]{id, Integer.toString(col)});
    }

    void gotoFrame2(){
        frame1.setVisibility(View.GONE);
        frame2.setVisibility(View.VISIBLE);
        frame3.setVisibility(View.GONE);
        frame4.setVisibility(View.GONE);
    }
    void gotoFrame3(){
        frame1.setVisibility(View.GONE);
        frame2.setVisibility(View.GONE);
        frame3.setVisibility(View.VISIBLE);
        frame4.setVisibility(View.GONE);
    }
    void gotoFrame4(){
        frame1.setVisibility(View.GONE);
        frame2.setVisibility(View.GONE);
        frame3.setVisibility(View.GONE);
        frame4.setVisibility(View.VISIBLE);
    }
    void setFrame(){
        frame1.setVisibility(View.VISIBLE);
        frame2.setVisibility(View.GONE);
        frame3.setVisibility(View.GONE);
        frame4.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v == button_ok){
            hideKeyboard();
            id = editText_id.getText().toString();
            pw = editText_pw.getText().toString();
            if(checkid(id)){
                Toast.makeText(this,"존재하는 아이디입니다.",Toast.LENGTH_LONG).show();
            }
            else{
                gotoFrame2();
                // -- 다음 설정창으로
            }
        } else if(v == button_cancel){
            hideKeyboard();
            finish();
        }
        else if(v == ok1){
            insertUserInfo(MainActivity.CITIZEN);
            gotoFrame3();
        }
        else if(v == s1_kid){
            insertUserInfo(MainActivity.KIDS);
            gotoFrame3();
        }
        else if(v == s1_youth){
            insertUserInfo(MainActivity.YOUTH);
            gotoFrame3();
        }
        else if(v == s1_old){
            insertUserInfo(MainActivity.OLD);
            gotoFrame3();
        }
        else  if(v == ok2){
            gotoFrame4();
        }
        else if(v == s2_dis){
            insertUserInfo(MainActivity.DISABLE);
            gotoFrame4();
        }
        else if(v == s2_multi){
            insertUserInfo(MainActivity.MULTI);
            gotoFrame4();
        }
        else if(v == s3_edu){
            boolean flag = isClicked[MainActivity.H_EDU];
            isClicked[MainActivity.H_EDU] = !(isClicked[MainActivity.H_EDU]);
            if (!flag) {
                s3_edu.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_edu.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_cons){
            boolean flag = isClicked[MainActivity.H_CON];
            isClicked[MainActivity.H_CON] = !(isClicked[MainActivity.H_CON]);
            if (!flag) {
                s3_cons.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_cons.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_cul){
            boolean flag = isClicked[MainActivity.H_CUL];
            isClicked[MainActivity.H_CUL] = !(isClicked[MainActivity.H_CUL]);
            if (!flag) {
                s3_cul.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_cul.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_faci){
            boolean flag = isClicked[MainActivity.H_FAL];
            isClicked[MainActivity.H_FAL] = !(isClicked[MainActivity.H_FAL]);
            if (!flag) {
                s3_faci.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_faci.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_fin){
            boolean flag = isClicked[MainActivity.H_FIN];
            isClicked[MainActivity.H_FIN] = !(isClicked[MainActivity.H_FIN]);
            if (!flag) {
                s3_fin.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_fin.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_house){
            boolean flag = isClicked[MainActivity.H_HOU];
            isClicked[MainActivity.H_HOU] = !(isClicked[MainActivity.H_HOU]);
            if (!flag) {
                s3_house.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_house.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_job){
            boolean flag = isClicked[MainActivity.H_JOB];
            isClicked[MainActivity.H_JOB] = !(isClicked[MainActivity.H_JOB]);
            if (!flag) {
                s3_job.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_job.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_medi){
            boolean flag = isClicked[MainActivity.H_HEL];
            isClicked[MainActivity.H_HEL] = !(isClicked[MainActivity.H_HEL]);
            if (!flag) {
                s3_medi.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_medi.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == s3_thing){
            boolean flag = isClicked[MainActivity.H_TNG];
            isClicked[MainActivity.H_TNG] = !(isClicked[MainActivity.H_TNG]);
            if (!flag) {
                s3_thing.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonClicked));
            }
            else {
                s3_thing.setBackgroundColor(ContextCompat.getColor(this,R.color.buttonNotClicked));
            }
        }
        else if(v == ok3){
            finishSignIn();
        }
    }

    private void finishSignIn() {
        db.execSQL("insert into user (id, pw) values (?,?)", new String[]{id, pw});
        for (int i=MainActivity.H_START; i<=MainActivity.H_END; i++){
            if (isClicked[i]) insertUserInfo(i);
        }
        setFrame();
        Toast.makeText(getApplicationContext(), "사용자 생성이 완료되었습니다.",Toast.LENGTH_LONG).show();
        finish();
    }

    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(editText_id.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editText_pw.getWindowToken(), 0);
    }
}
