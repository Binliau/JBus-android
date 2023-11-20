package com.kevinraihanjbusrd.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class AboutMeActivity extends AppCompatActivity {
    private TextView usernameTextView = null;
    private TextView emailTextView = null;
    private TextView balanceTextView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        getSupportActionBar().hide();

        usernameTextView = (TextView)findViewById(R.id.profile_username);
        emailTextView = (TextView)findViewById(R.id.profile_email);
        balanceTextView = (TextView)findViewById(R.id.profile_balance);

        usernameTextView.setText("Kevin Raihan");
        emailTextView.setText("kevinraihan@gmail.com");
        balanceTextView.setText("Rp.69420");
    }
}