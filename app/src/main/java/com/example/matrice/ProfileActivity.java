package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.matrice.databinding.ActivityProfileBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements Response.Listener <JSONObject>, Response.ErrorListener
{
	public Smaug h = Smaug.getInstance();
	public ActivityProfileBinding b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_profile);
		
		// caricare le info del giocatore
		
		String sessionId = getSharedPreferences("settings", Context.MODE_PRIVATE).getString("session_id",null);
		JSONObject postObj = new JSONObject();
		try {
			postObj.put("session_id", sessionId);
			JsonObjectRequest jReq = new JsonObjectRequest(
					Request.Method.POST,
					getString(R.string.getprofile_url),
					postObj,
					this,
					this
			);
			RequestQueue netQueue = Volley.newRequestQueue(this);
			netQueue.add(jReq);
		} catch (JSONException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	// modificare l'username
	
	// modificare l'immagine
	
	public void onChartClick(View v)
	{
		this.startActivity(new Intent(this, ChartActivity.class));
	}
	
	@Override
	public void onErrorResponse(VolleyError error)
	{
	
	}
	
	@Override
	public void onResponse(JSONObject response)
	{
	
	}
}
