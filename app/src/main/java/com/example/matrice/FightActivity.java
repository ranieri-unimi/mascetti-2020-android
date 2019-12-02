package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.matrice.databinding.ActivityFightBinding;
import com.example.matrice.databinding.ActivitySplashBinding;

public class FightActivity extends AppCompatActivity
{
	private Smaug h = Smaug.getInstance();
	private ActivityFightBinding b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_fight);
		
		String itemId = getIntent().getExtras().getString("Id");
		b.setItem((Item) h.get(itemId));
	}
	
}
