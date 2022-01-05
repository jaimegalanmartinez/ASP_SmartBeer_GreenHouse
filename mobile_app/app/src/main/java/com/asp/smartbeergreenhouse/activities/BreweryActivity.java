package com.asp.smartbeergreenhouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.asp.smartbeergreenhouse.databinding.ActivityBreweryBinding;

public class BreweryActivity extends AppCompatActivity {
    private ActivityBreweryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBreweryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}