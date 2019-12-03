package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.matrice.databinding.ActivityFightBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class FightActivity extends AppCompatActivity implements
		Response.Listener<JSONObject>, Response.ErrorListener
{
	private Smaug h = Smaug.getInstance();
	private ActivityFightBinding b;
	
	private Item item;
	private double near;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_fight);
		
		near = getIntent().getExtras().getDouble("Distance", Double.MAX_VALUE);
		item = (Item) h.get(getIntent().getExtras().getString("Id"));
		b.setItem(item);
		loadItemInfo();
	}
	
	public void loadItemInfo() {
		int btnText, infoText;
		switch (item.getType()+item.getSize())
		{
			case "CAS":
				btnText = R.string.eat_yes;
				infoText = R.string.eat_s;
				break;
			case "CAM":
				btnText = R.string.eat_yes;
				infoText = R.string.eat_m;
				break;
			case "CAL":
				btnText = R.string.eat_yes;
				infoText = R.string.eat_l;
				break;
			case "MOS":
				btnText = R.string.fight_yes;
				infoText = R.string.fight_s;
				break;
			case "MOM":
				btnText = R.string.fight_yes;
				infoText = R.string.fight_m;
				break;
			case "MOL":
				btnText = R.string.fight_yes;
				infoText = R.string.fight_l;
				break;
			default:
				btnText = 0;
				infoText = 0;
				Log.d("mdt","switch error managing");
				finish();
				break;
		}
		b.lytSideBFight.btnYesFight.setText(btnText);
		b.lytSideBFight.txtInfoFight.setText(infoText);
		Log.d("mdt", ""+near);
		if(near > Double.parseDouble(getString(R.string.max_distance)))
		{
			b.lytSideBFight.btnYesFight.setEnabled(false);
			b.lytSideBFight.btnYesFight.setTextColor(Color.GRAY);
			b.lytSideBFight.txtInfoFight.setText(R.string.not_near);
		}
	}
	
	public void onYesClick(View v) {
		try {
			JSONObject j = new JSONObject();
			j.put("target_id", item.getId());
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
