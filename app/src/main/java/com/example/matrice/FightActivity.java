package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.matrice.databinding.ActivityFightBinding;
import com.example.matrice.databinding.ActivitySplashBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class FightActivity extends AppCompatActivity implements
		Response.Listener<JSONObject>, Response.ErrorListener
{
	private Smaug h = Smaug.getInstance();
	private ActivityFightBinding b;
	
	private String itemId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_fight);
		
		itemId = getIntent().getExtras().getString("Id");
		b.setItem((Item) h.get(itemId));
		loadItemInfo();
	}
	
	public void loadItemInfo()
	{
		// todo LA DISTANZA è CORRETTA, caramella o mostro, percentuale perdita
	}
	
	public void onYesClick(View v) {
		try {
			JSONObject j = new JSONObject();
			j.put("target_id", itemId);
			Smaug.sendJSONRequest(this, this,this, R.string.fighteat_url, j);
		} catch (JSONException e) {
			Snackbar.make(b.lytBackFight, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onResponse(JSONObject response)
	{
		// Saving on Smaug
		h.put(ResultActivity.H_LOC, response);
		
		// New Activity
		Intent i = new Intent(this, ResultActivity.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		finish();
		this.startActivity(i);
	}
	
	public void onNoClick(View v) {
		finish();
	}
	
	@Override
	public void onErrorResponse(VolleyError error)
	{ Snackbar.make(b.lytBackFight, getText(R.string.no_internet), Snackbar.LENGTH_LONG).show(); }
}
