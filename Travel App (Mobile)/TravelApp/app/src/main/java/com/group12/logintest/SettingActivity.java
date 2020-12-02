package com.group12.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    Button settingLogout, settingBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settingLogout = (Button) findViewById(R.id.settingLogout);
        settingBack = (Button) findViewById(R.id.settingBack);

        //going back button
        settingLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
            }
        });

        settingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
