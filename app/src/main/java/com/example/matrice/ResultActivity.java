package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ResultActivity extends AppCompatActivity
{
	public static final String H_LOC = "JSON_SAVED_RESPONSE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
	}
	
	public void onClick(View v)
	{
		finish();
	}
}