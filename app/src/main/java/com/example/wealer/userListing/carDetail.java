package com.example.wealer.userListing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.wealer.R;

public class carDetail extends AppCompatActivity {
    RequestQueue queue;
    EditText edtMake;
    EditText edtModel;
    EditText edtDesc;
    EditText edtLink;
    EditText edtLoc;
    EditText edtPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardetail_activity);
        queue = Volley.newRequestQueue(this);
        edtMake = findViewById(R.id.edtMake);
        edtModel = findViewById(R.id.edtModel);
        edtDesc = findViewById(R.id.edtDesc);
        edtLink = findViewById(R.id.edtLink);
        edtLoc = findViewById(R.id.edtLocation);
        edtPrice = findViewById(R.id.edtPrice);


    }
}