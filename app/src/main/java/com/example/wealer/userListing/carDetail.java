package com.example.wealer.userListing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wealer.LoginSignUp.loginscreen;
import com.example.wealer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class carDetail extends AppCompatActivity {
    RequestQueue queue;
    EditText edtMake;
    EditText edtModel;
    EditText edtDesc;
    EditText edtLink;
    EditText edtLoc;
    EditText edtPrice;
    EditText edtMiles;
    Button btnSave;
    String carID;
    String userID;
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
        edtMiles = findViewById(R.id.edtMiles);
        btnSave = findViewById(R.id.button5);
        SharedPreferences sharedPref = getSharedPreferences("userID", Context.MODE_PRIVATE);
        userID = sharedPref.getString("userID", null);
        //get carID from intent
        Intent i = getIntent();
        carID = i.getStringExtra("carID");
        //if the user is not logged in then kick them back to the login screen
        if (userID == null){
            Intent login = new Intent(this, loginscreen.class);
            startActivity(login);
        }
        //if there is no carID in the intent that means we are adding a new car
        //if there is a carID in the intent that means we are editing an existing car
        //so we need to populate texts with the information
        if(carID != null){
            populateTexts();
        }
        //onclick listener for the save button
        //if we have a carID then we call editCar()
        //if we dont have a carID then we call addCar()
        btnSave.setOnClickListener(view -> {
            if(carID == null){
                addCar();
            }else{
                editCar();
            }
        });

    }
    //method to add a car
    public void addCar(){
        //api string
        final String apiLink = "https://project3-ceparker.onrender.com/listcar/" + userID;
        //body of post request
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("make", edtMake.getText().toString());
        body.put("model", edtModel.getText().toString());
        body.put("miles", edtMiles.getText().toString());
        body.put("price", edtPrice.getText().toString());
        body.put("imageURL", edtLink.getText().toString());
        body.put("ownerAddress", edtLoc.getText().toString());
        body.put("auth",userID);
        //post request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiLink, new JSONObject(body),
                response -> {
                    Toast.makeText(this, "Your car has been listed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, manageListing.class));
                },
                error -> {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);
    }

    public void editCar(){
        //api string
        final String apiLink = "https://project3-ceparker.onrender.com/editcar/" + userID + "/" + carID;
        //body of post request
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("make", edtMake.getText().toString());
        body.put("model", edtModel.getText().toString());
        body.put("miles", edtMiles.getText().toString());
        body.put("price", edtPrice.getText().toString());
        body.put("imageURL", edtLink.getText().toString());
        body.put("ownerAddress", edtLoc.getText().toString());
        body.put("auth",userID);
        //post request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiLink, new JSONObject(body),
                response -> {
                    Toast.makeText(this, "Your car has been updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, manageListing.class));
                },
                error -> {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);
    }

    public void populateTexts(){
        //api string
        final String apiLink = "https://project3-ceparker.onrender.com/car/" + carID;
        //get request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiLink, null,
                response -> {
                    try {
                        //populate text boxes with return of call
                        JSONObject jsonCar = response.getJSONObject("message");
                        edtMake.setText(jsonCar.getString("make"));
                        edtModel.setText(jsonCar.getString("model"));
                        edtDesc.setText(jsonCar.getString("description"));
                        edtLink.setText(jsonCar.getString("imageURL"));
                        edtLoc.setText(jsonCar.getString("ownerAddress"));
                        edtPrice.setText(jsonCar.getString("price"));
                        edtMiles.setText(jsonCar.getString("miles"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                });
        queue.add(request);

    }
}