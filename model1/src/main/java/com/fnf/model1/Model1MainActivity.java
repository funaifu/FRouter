package com.fnf.model1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fnf.frouter_annotation.Router;
import com.fnf.router.FRouter;

@Router(path = "model1/main")
public class Model1MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model1_main);
        findViewById(R.id.model_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FRouter.startActivity(Model1MainActivity.this, "main", null);
            }
        });
    }
}
