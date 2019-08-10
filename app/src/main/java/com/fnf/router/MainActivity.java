package com.fnf.router;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fnf.frouter_annotation.Router;

@Router(path = "main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FRouter.startActivity(this, "main/main2", null);
    }
}
