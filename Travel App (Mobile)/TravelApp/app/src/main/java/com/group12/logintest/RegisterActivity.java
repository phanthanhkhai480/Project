package com.group12.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, password, rePassword;
    Button btnRegister, registerBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        rePassword = (EditText)findViewById(R.id.rePassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        registerBack = (Button)findViewById(R.id.registerBack);

        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m_username = userName.getText().toString();
                final String m_password = password.getText().toString();
                final String m_rePassword = rePassword.getText().toString();

                if(m_rePassword.equals(m_password)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success){
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    success();
                                    startActivity(intent);

                                } else { alert(); }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(m_username, m_password, m_rePassword, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                } else { alert(); }
            }
        });
    }

    private void alert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        dialogBuilder.setMessage("Password Does Not Match");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void success() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        dialogBuilder.setMessage("Account has been created");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}


