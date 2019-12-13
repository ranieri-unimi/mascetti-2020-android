package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.matrice.databinding.ActivityResultBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity
{
	public Smaug h = Smaug.getInstance();
	public ActivityResultBinding b;
	public static final String H_LOC = "JSON_SAVED_RESPONSE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_result);
		
		try {
			JSONObject j = (JSONObject) h.get(H_LOC);
			Player p = (Player) h.get(getString(R.string.profile));
			if(j.getBoolean("died")) {
				b.lytBackResult.setBackgroundColor(Color.RED);
				b.txtTitleResult.setTextColor(Color.BLACK);
				b.txtTitleResult.setText(R.string.died);
			}
			int hp = Integer.parseInt(j.getString("lp")) - Integer.parseInt(p.getHp());
			b.txtHpResult.setText(beauty(hp));
			int xp = Integer.parseInt(j.getString("xp")) - Integer.parseInt(p.getXp());
			b.txtXpResult.setText(beauty(xp));
		}
		catch (JSONException e){
			Snackbar.make(b.lytBackResult, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
			finish();
		}
	}
	
	public String beauty(int value) {
		String label = "";
		if(value > 0) { label = "+ "; }
		if (value < 0) { label = "- "; }
		return label+Math.abs(value);
	}
	
	public void onClick(View v) {
		// New Activity
		Intent i = new Intent(this, MapActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		finish();
		this.startActivity(i);
	}
}