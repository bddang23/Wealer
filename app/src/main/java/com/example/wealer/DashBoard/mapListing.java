package com.example.wealer.DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wealer.LoginSignUp.loginscreen;
import com.example.wealer.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class mapListing extends AppCompatActivity implements OnMapReadyCallback {

    RequestQueue queue;
    GoogleMap map;
    Button btnViewImage;
    Button btnEmail;
    TextView txtModel;
    TextView txtMake;
    TextView txtPrice;
    TextView txtMile;
    TextView txtDescription;
    String carID;
    String imageURL;
    String owner;
    String ownerAddress;
    String ownerCoordinate;
    String make;
    String model;
    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_listing_activity);
        queue = Volley.newRequestQueue(this);
        btnViewImage = findViewById(R.id.btnViewImage);
        btnEmail = findViewById(R.id.btnEmail);
        txtModel = findViewById(R.id.txtModel);
        txtMake = findViewById(R.id.txtMake);
        txtPrice = findViewById(R.id.txtPrice);
        txtMile = findViewById(R.id.txtMile);
        txtDescription = findViewById(R.id.txtDescription);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        //get car id from intent
        Intent intent =  getIntent();
        carID = intent.getStringExtra("carID");
        ownerCoordinate = intent.getStringExtra("cord");
        ownerAddress = intent.getStringExtra("add");
        //get userID from sharedPref
        SharedPreferences sharedPref = getSharedPreferences("com.example.wealer", Context.MODE_PRIVATE);
        String userID = sharedPref.getString("activeUserID", null);
        //if the user is not logged in then send them back to the login screen
        //otherwise call fetchData() function
        if (userID == null){
            Intent login = new Intent(this, loginscreen.class);
            startActivity(login);
        }else{
            fetchData();
        }
        //opens the default browser with the link to the images of the car
        btnViewImage.setOnClickListener(view -> {
            if (!imageURL.contains("https://") ||!imageURL.contains("http://"))
                imageURL = "https://"+imageURL;

            Uri webpage = Uri.parse(imageURL);
            Intent intent2 = new Intent(Intent.ACTION_VIEW);
            intent2.setData(webpage);
            Log.d("MyApp", intent2.toString());
            if (intent2.resolveActivity(getPackageManager()) != null) {
                startActivity(intent2);
            }else{
                //Page not found
                Toast.makeText(this, "Error Retrieving Image!", Toast.LENGTH_SHORT).show();
            }


        });
        //opens the default email app and starts an email to the user of a listed car
        //set the subject to "Wealer Listing"
        btnEmail.setOnClickListener(view -> {
             //get request to get the email address based on userID
             String url = "https://project3-ceparker.onrender.com/users/";
             JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + owner,null,
                     response -> {
                             try{
                                  JSONObject jsonPerson = response.getJSONObject("message");
                                  String email = jsonPerson.getString("email");
                                  Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email,null));
//                                  i.setData(Uri.parse("mailto:"));
//                                  i.putExtra(Intent.EXTRA_EMAIL, email);
                                  i.putExtra(Intent.EXTRA_SUBJECT, "Wealer Listing");
                                  if(i.resolveActivity(getPackageManager())!=null){
                                      startActivity(i);
                                  }
                             }catch (JSONException e){
                                 e.printStackTrace();
                             }
                     },error -> {
                 Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
             });
             queue.add(request);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //get the map
        map = googleMap;





    }


    //method for get request
    public void fetchData(){
        //api string
        String url = "https://project3-ceparker.onrender.com/car/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url + carID,null,
              response->{
                     try {
                         //populate textfields
                             JSONObject jsonCar = response.getJSONObject("message");
                             //ownerAddress = jsonCar.getString("ownerAddress");
                             owner = jsonCar.getString("owner");
                             imageURL = jsonCar.getString("imageURL");
                             make = jsonCar.getString("make");
                             model = jsonCar.getString("model");
                             price = jsonCar.getString("price");
                             txtModel.setText(txtModel.getText() + " " + jsonCar.getString("model"));
                             txtMake.setText(txtMake.getText() + " " + jsonCar.getString("make"));
                             txtPrice.setText(txtPrice.getText() + " " + jsonCar.getString("price"));
                             txtMile.setText(txtMile.getText() + " " + jsonCar.getString("miles"));
                             txtDescription.setText(jsonCar.getString("description"));

                         //get the cordinate for the marker
                         String[] cords= ownerCoordinate.split(",");
                         LatLng car = new LatLng(Double.parseDouble(cords[0]), Double.parseDouble(cords[1]));
                         //add marker to map
                         map.addMarker(new MarkerOptions()
                                 .title(make + " " + model)
                                 .snippet("Price: " + price+ " - Location: " + ownerAddress )
                                 .position(car));
                         //move map to the cords
                         map.moveCamera(CameraUpdateFactory.newLatLngZoom(car,13));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
              },error->{
          Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
      });
        queue.add(request);
    }
}