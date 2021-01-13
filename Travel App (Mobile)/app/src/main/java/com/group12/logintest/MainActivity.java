package com.group12.logintest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

//First activity that is loaded when the app starts.
public class MainActivity extends AppCompatActivity {

    EditText username, password;
    TextView info2;
    Button btnLogin, btnRegister;
    int attempt = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        info2 = (TextView)findViewById(R.id.info2);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m_username = username.getText().toString();
                final String m_password = password.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(intent);
                            } else {
                                alert();
                                info2.setText("Don't have an account? Sign-Up below");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(m_username, m_password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void alert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setMessage("Username or Password is incorrect");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
