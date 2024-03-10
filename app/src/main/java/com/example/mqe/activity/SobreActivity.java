package com.example.mqe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mqe.R;

import java.util.Objects;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}