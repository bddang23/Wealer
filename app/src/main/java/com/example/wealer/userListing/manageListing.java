package com.example.wealer.userListing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wealer.Model.Car;
import com.example.wealer.databinding.ActivityManageListingBinding;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;


import com.example.wealer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

public class manageListing extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityManageListingBinding binding;
    ArrayList<Car> cars = new ArrayList<>();
    SharedPreferences sharedPreferences;
    public RequestQueue queue;
    String userName;
    ListView lstListings;
    carListingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        binding = ActivityManageListingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lstListings = findViewById(R.id.lstListings);
         adapter = new carListingAdapter(cars);
        lstListings.setAdapter(adapter);



        sharedPreferences = getSharedPreferences("com.example.wealer", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("activeUserID", null);
//        if (userName == null){
//            Intent login = new Intent(this, loginscreen.class);
//            startActivity(login);
//        }
        userName = "638d5e4e7e60fec557c9b45a";
        queue = Volley.newRequestQueue(this);


        fetchUserCarData(userName);













        binding.fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), carDetail.class);
                startActivity(intent);


            }
        });
    }



    public void fetchUserCarData(String userID) {
        String apiLink = "https://project3-ceparker.onrender.com/users/"+userID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiLink, null,
                response->{
                    try {
                        JSONObject results = response.getJSONObject("message");
                        JSONArray carsData = results.getJSONArray("cars");


                        for(int i = 0; i<carsData.length(); i++){
                            JSONObject carInfo = carsData.getJSONObject(i);
                            cars.add( new Car(carInfo.getString("make"),
                                    carInfo.getString("model"),
                                    carInfo.getInt("miles"),
                                    carInfo.getString("description"),
                                    carInfo.getString("imageURL"),
                                    (float) carInfo.getDouble("price"),
                                    carInfo.getString("ownerAddress"),
                                    carInfo.getString("_id")
                                    ));
                        }

                        adapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("MYAPP", "An error occurred in test: " + e.toString());
                    }

                },
                error-> {
                    Log.d("MYAPP", "An error occurred: " + error);
                });

        queue.add(request);

    }


     class carListingAdapter extends BaseAdapter {
         ArrayList<Car> cars;

         public carListingAdapter(ArrayList<Car> cars) {
             this.cars = cars;
         }


        @Override
        public int getCount() {
            return cars.size();
        }

        @Override
        public Object getItem(int i) {
            return cars.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null)
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.car_listing_layout,viewGroup,false);
            Car car = (Car)getItem(i);
            TextView txtPriceListing = view.findViewById(R.id.txtPriceListing);
            TextView txtModelListing = view.findViewById(R.id.txtModelListing);
            TextView txtMakeListing = view.findViewById(R.id.txtMakeListing);
            Button btnEdit = view.findViewById(R.id.btnEditListing);
            Button btnDelete = view.findViewById(R.id.btnView);




            btnEdit.setOnClickListener(view1 -> {



                Intent intent= new Intent(getApplicationContext(), carDetail.class);
                intent.putExtra("carID",car.getID());
                startActivity(intent);



            });
            btnDelete.setOnClickListener(view1 -> {
              String apiLink =  "https://project3-ceparker.onrender.com/cars/"+car.getID()+"?auth="+userName;



                JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, apiLink, null ,
                        response->{
                            try {

                                String results = response.getString("message");
                                Log.d("MYAPP", results);
                                cars.remove(car);


                                adapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("MYAPP", "An error occurred in test: " + e.toString());
                            }

                        },
                        error-> {
                            Log.d("MYAPP", "An error occurred: " + error);
                        });

                queue.add(request);
           });

            NumberFormat formatter = NumberFormat.getCurrencyInstance();



            txtPriceListing.setText(formatter.format(car.getPrice()));
            txtModelListing.setText(car.getModel());
            txtMakeListing.setText(car.getMake());



            return view;

        }
    }

}