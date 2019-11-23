package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import com.example.matrice.databinding.ActivityChartBinding;
import com.example.matrice.databinding.ActivityMainBinding;
import com.example.matrice.databinding.ActivityMapBinding;

public class ChartActivity extends AppCompatActivity
{
	
	private Smaug h = Smaug.getInstance();
	private ActivityChartBinding b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_chart);
	}
}
