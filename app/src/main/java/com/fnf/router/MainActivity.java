package com.fnf.router;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fnf.frouter_annotation.Router;

@Router(path = "main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.app_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FRouter.startActivity(MainActivity.this, "model1/main", null);
            }
        });
    }
}
