package com.example.wealer.DashBoard;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.wealer.R;

public class dashboardResult extends AppCompatActivity {
    Intent intent;
    String make, model, minMile,maxMile,minPrice,maxPrice;
    ImageButton btnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_result_activity);
        intent = getIntent();
        getExtraValue();

        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(v->{
           Intent intent = new Intent(this, DashBoard.class);
           startActivity(intent);
        });


    }

    private void getExtraValue() {
        if (intent.getStringExtra("make")!=null)
            make = intent.getStringExtra("make");
        if (intent.getStringExtra("model")!=null)
            model = intent.getStringExtra("model");
        if (intent.getStringExtra("minMile")!=null)
            minMile = intent.getStringExtra("minMile");
        if (intent.getStringExtra("maxMile")!=null)
            maxMile = intent.getStringExtra("maxMile");
        if (intent.getStringExtra("minPrice")!=null)
            minPrice = intent.getStringExtra("minPrice");
        if (intent.getStringExtra("maxPrice")!=null)
            maxPrice = intent.getStringExtra("maxPrice");
    }
}