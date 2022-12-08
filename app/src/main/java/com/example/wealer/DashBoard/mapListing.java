package com.example.wealer.DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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

        Intent intent =  getIntent();
        carID = intent.getStringExtra("carID");

        SharedPreferences sharedPref = getSharedPreferences("userID", Context.MODE_PRIVATE);
        String userID = sharedPref.getString("userID", null);
        if (userID == null){
            Intent login = new Intent(this, loginscreen.class);
            startActivity(login);
        }else{
            fetchData();
        }
        btnViewImage.setOnClickListener(view -> {
            if(imageURL != null){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(imageURL)));
            }
        });
        btnEmail.setOnClickListener(view -> {
             String url = "https://project3-ceparker.onrender.com/users/";
             JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + owner,null,
                     response -> {
                             try{
                                  JSONObject jsonPerson = response.getJSONObject("message");
                                  String email = jsonPerson.getString("email");
                                  Intent i = new Intent(Intent.ACTION_SENDTO);
                                  i.setData(Uri.parse("mailto:"));
                                  i.putExtra(Intent.EXTRA_EMAIL, email);
                                  i.putExtra(Intent.EXTRA_SUBJECT, "Wealer Listing");
                                  if(i.resolveActivity(getPackageManager())!=null){
                                      startActivity(i);
                                  }
                             }catch (JSONException e){
                                 e.printStackTrace();
                             }
                     },error -> {

             });
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        String[] cords = ownerAddress.split(",");
        LatLng car = new LatLng(Double.parseDouble(cords[0]), Double.parseDouble(cords[1]));
        map.addMarker(new MarkerOptions()
                .title(make + " " + model)
                .snippet(price)
                .position(car));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(car,13));
    }

    public void fetchData(){
      String url = "https://project3-ceparker.onrender.com/car/";
      JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url + carID,null,
              response->{
                     try {
                             JSONObject jsonCar = response.getJSONObject("message");
                             ownerAddress = jsonCar.getString("ownerAddress");
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

                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
              },error->{

      });
        queue.add(request);
    }
}