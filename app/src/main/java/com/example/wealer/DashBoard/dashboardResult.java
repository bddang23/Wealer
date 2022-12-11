package com.example.wealer.DashBoard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wealer.Model.Car;
import com.example.wealer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class dashboardResult extends AppCompatActivity {
    Intent intent;
    String make, model,maxMile,minPrice,maxPrice;
    ImageButton btnReturn;
    public RequestQueue queue;
    ListView lstCars;
    ArrayList<Car> cars = new ArrayList<>();
    carListingAdapter adapter;
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

        lstCars =findViewById(R.id.lstCars);
        adapter = new carListingAdapter(cars);
        lstCars.setAdapter(adapter);

        queue = Volley.newRequestQueue(this);
        getData();


    }

    private void getExtraValue() {
        if (intent.getStringExtra("make")!=null)
            make = intent.getStringExtra("make");
        if (intent.getStringExtra("model")!=null)
            model = intent.getStringExtra("model");

        if (intent.getStringExtra("maxMile")!=null)
            maxMile = intent.getStringExtra("maxMile");
        if (intent.getStringExtra("minPrice")!=null)
            minPrice = intent.getStringExtra("minPrice");
        if (intent.getStringExtra("maxPrice")!=null)
            maxPrice = intent.getStringExtra("maxPrice");
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
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.car_results_layout,viewGroup,false);
            Car car = (Car)getItem(i);
            TextView txtPriceListing = view.findViewById(R.id.txtPriceListing);
            TextView txtModelListing = view.findViewById(R.id.txtModelListing);
            TextView txtMakeListing = view.findViewById(R.id.txtMakeListing);
            TextView txtMilesResults = view.findViewById(R.id.txtMilesResults);
            Button btnView = view.findViewById(R.id.btnView);





            btnView.setOnClickListener(view1 -> {
                    Intent intent = new Intent(getApplicationContext(), mapListing.class);
                    intent.putExtra("carID",car.getID());
                    intent.putExtra("add",car.getAddress());
                    startActivity(intent);


            });

            NumberFormat formatter = NumberFormat.getCurrencyInstance();



            txtPriceListing.setText(formatter.format(car.getPrice()));
            txtModelListing.setText(car.getModel());
            txtMakeListing.setText(car.getMake());
            txtMilesResults.setText(car.getMiles()+"");



            return view;




        }







    }







    public void getData(){
        String apiLink =  "https://project3-ceparker.onrender.com/car/";
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("make", make);
        body.put("model", model);
        body.put("maxMiles", maxMile);
        body.put("minPrice", minPrice);
        body.put("maxPrice", maxPrice);




        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiLink, new JSONObject(body) ,
                response->{
                    try {
                        Log.d("MYAPP", response.toString());
                        JSONArray carsData = response.getJSONArray("cars");



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






}