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
		// todo LA DISTANZA è CORRETTA
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
		try {
			String dspMess;
			if(response.getBoolean("died")) {
				b.lytBackFight.setBackgroundColor(Color.RED);
				dspMess = getString(R.string.died);
			}
			else {
				dspMess= getString(R.string.alive);
				// Hai vito, modifica altre cose ma boh
			}
			// Cose da fare in ogin caso
			b.lytSideBFight.txtInfoFight.setText(dspMess);
			b.lytSideBFight.btnYesFight.setEnabled(false);
			b.lytSideBFight.btnYesFight.setTextColor(Color.GRAY);
		}
		catch (JSONException e) {
			Snackbar.make(b.lytBackFight, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	public void onNoClick(View v) {
		this.startActivity(new Intent(this, MapActivity.class));
	}
	@Override
	public void onErrorResponse(VolleyError error)
	{ Snackbar.make(b.lytBackFight, getText(R.string.no_internet), Snackbar.LENGTH_LONG).show(); }
}
