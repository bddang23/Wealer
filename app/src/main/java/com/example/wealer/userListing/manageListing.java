package com.example.wealer.userListing;

import android.os.Bundle;

import com.example.wealer.databinding.ActivityManageListingBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.wealer.R;

public class manageListing extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityManageListingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManageListingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.fabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}