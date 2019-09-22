package com.cuk.seosom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginInActivity extends AppCompatActivity implements View.OnClickListener {
    Button button_login, button_create;
    EditText editText_id, editText_pw;
    String id, pw;
    String[] id_list, pw_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        button_login = (Button) findViewById(R.id.button_login);
        button_create = (Button) findViewById(R.id.button_create);
        editText_id = (EditText) findViewById(R.id.editText_id);
        editText_pw = (EditText) findViewById(R.id.editText_pw);

        button_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == button_login){
            id = editText_id.getText().toString();
            pw = editText_pw.getText().toString();
        }
    }
}
