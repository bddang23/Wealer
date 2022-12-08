package com.example.wealer.DashBoard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.wealer.LoginSignUp.loginscreen;
import com.example.wealer.LoginSignUp.signupscreen;
import com.example.wealer.R;
import com.example.wealer.userListing.manageListing;

public class DashBoard extends AppCompatActivity {

    EditText edtSearchMake,edtSearchModel, edtSearchMaxMile, edtSearchMinMile, edtSearchMaxPrice, edtSearchMinPrice;
    Button btnSearch, btnManageListing,btnLogOut;
    Intent intent;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        definedElements();
        btnSearch.setOnClickListener(v->{

           // Start the sign up activity
            intent = new Intent(this, dashboardResult.class);
            validateText(intent);
            startActivity(intent);
        });

        btnManageListing.setOnClickListener(v->{
            // Start the sign up activity
            intent = new Intent(this, manageListing.class);
            startActivity(intent);
        });

        btnLogOut.setOnClickListener(v->{
            // Start the sign up activity
            intent = new Intent(this, loginscreen.class);
            // Get the shared preferences and add the corresponding username
            sharedPreferences = getSharedPreferences("com.example.wealer", MODE_PRIVATE);
            sharedPreferences.edit().remove("activeUserID");
            startActivity(intent);
        });

    }

    private void validateText(Intent intent) {
        if(!edtSearchMake.getText().toString().isEmpty())
            intent.putExtra("make",edtSearchMake.getText().toString().trim());

        if(!edtSearchModel.getText().toString().isEmpty())
            intent.putExtra("model",edtSearchModel.getText().toString().trim());

        if(!edtSearchMinMile.getText().toString().isEmpty())
            intent.putExtra("minMile",edtSearchMinMile.getText().toString().trim());

        if(!edtSearchMaxMile.getText().toString().isEmpty())
            intent.putExtra("maxMile",edtSearchMaxMile.getText().toString().trim());

        if(!edtSearchMinPrice.getText().toString().isEmpty())
            intent.putExtra("minPrice",edtSearchMinPrice.getText().toString().trim());

        if(!edtSearchMaxPrice.getText().toString().isEmpty())
            intent.putExtra("maxPrice",edtSearchMaxPrice.getText().toString().trim());


    }

    private void definedElements() {
        edtSearchMake =findViewById(R.id.edtSearchMake);
        edtSearchModel =findViewById(R.id.edtSearchModel);
        edtSearchMaxMile =findViewById(R.id.edtSearchMaxMiles);
        edtSearchMinMile =findViewById(R.id.edtSearchMinMiles);
        edtSearchMaxPrice =findViewById(R.id.edtSearchMaxPrices);
        edtSearchMinPrice =findViewById(R.id.edtSearchMinPrices);

        btnSearch =findViewById(R.id.btnSearch);
        btnManageListing =findViewById(R.id.btnManageListing);
        btnLogOut =findViewById(R.id.btnLogOut);
    }
}