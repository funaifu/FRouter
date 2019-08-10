package com.fnf.router;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fnf.frouter_annotation.Router;

@Router(path = "main/main2")
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
