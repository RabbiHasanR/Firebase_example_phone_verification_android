package com.example.diu.firebaseapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.diu.firebaseapp.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
