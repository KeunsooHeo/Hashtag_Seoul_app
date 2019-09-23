package com.cuk.seosom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginInActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_login, button_create;
    EditText editText_id, editText_pw;
    String id, pw;
    ArrayList<String> id_list, pw_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        id_list = new ArrayList<>();
        id_list.add("test");
        pw_list = new ArrayList<>();
        pw_list.add("1234");
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

        if(id_list.contains(id)){
            if(pw.equals(pw_list.get(id_list.indexOf(id)))){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "아이디 혹은 비밀번호가 틀렸습니다.",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "아이디가 존재하지 않습니다.",Toast.LENGTH_LONG).show();
        }
    }
}