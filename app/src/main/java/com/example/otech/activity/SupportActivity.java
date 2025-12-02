package com.example.otech.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otech.R;
import com.google.android.material.appbar.MaterialToolbar;

public class SupportActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}
