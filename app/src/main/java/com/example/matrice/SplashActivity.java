package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.matrice.databinding.ActivitySplashBinding;
import com.google.android.material.snackbar.Snackbar;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class SplashActivity extends AppCompatActivity implements
		PermissionsListener,
		Response.ErrorListener
{
	private Smaug h = Smaug.getInstance();
	private ActivitySplashBinding b;
	private PermissionsManager bouncer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_splash);
		
		// Empty object to avoid glithcing layout
		h.put(getString(R.string.profile), new Player(this));
		
		//getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putString("session_id","wStTD5QkaO1ZrGAv").apply();  // Tua MADRE
	}

	public void onClick(View v) {
		if(PermissionsManager.areLocationPermissionsGranted(this)) checkSession();
		else
		{
			// Carica il permission manager
			bouncer = new PermissionsManager(this);
			bouncer.requestLocationPermissions(this);
		}
	}
	
	private void checkSession()
	{
		// First request: GET on session_id
		String sessionId = getSharedPreferences("settings", Context.MODE_PRIVATE).getString("session_id",null);
		if(sessionId == null) {
			JsonObjectRequest jReq = new JsonObjectRequest(
					Request.Method.GET,
					getText(R.string.register_url).toString(),
					null,
					new OnSessionId(),
					this);
			Volley.newRequestQueue(this).add(jReq);
		}
		else {
			loadGame();
		}
	}
	
	private void loadGame() {
		// Getting profile
		try {
			Smaug.sendJSONRequest(this, new OnProfile(),this, R.string.getprofile_url, new JSONObject());
		} catch (JSONException e) {
			Snackbar.make(b.lytBackSplash, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onPermissionResult(boolean granted)
	{
		if(granted) {
			checkSession();
		}
		else {
			Snackbar.make(b.lytBackSplash, getText(R.string.no_permission), Snackbar.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		bouncer.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	@Override public void onExplanationNeeded(List<String> permissionsToExplain) { /* nothing to do here*/ }
	
	public class OnSessionId implements Response.Listener <JSONObject> {
		@Override public void onResponse(JSONObject response)
		{
			try	{
				// Saving session_id
				String sid = response.getString("session_id");
				getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putString("session_id",sid).apply();
				loadGame();
			}
			catch (JSONException e) {
				Snackbar.make(b.lytBackSplash, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
			}
		}
	}
	
	public class OnProfile implements Response.Listener <JSONObject> {
		@Override public void onResponse(JSONObject response)
		{
			try {
				Player userProfile = new Player(SplashActivity.this).fromJSON(response, getApplicationContext());
				h.put(getString(R.string.profile), userProfile);
				
				// Everything is ok, next!
				SplashActivity.this.startActivity(new Intent(SplashActivity.this, MapActivity.class));
			}
			catch (JSONException e) {
				Snackbar.make(b.lytBackSplash, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onErrorResponse(VolleyError error)
	{
		Snackbar.make(b.lytBackSplash, getText(R.string.no_internet), Snackbar.LENGTH_LONG).show();
	}
}