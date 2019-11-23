package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.matrice.databinding.ActivityMainBinding;
import com.example.matrice.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity
{
	
	private Smaug h = Smaug.getInstance();
	private ActivityMapBinding b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_map);
	}
}
