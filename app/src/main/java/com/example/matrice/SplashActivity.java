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
		Response.Listener <JSONObject>,
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
	}

	public void onClick(View v) {
		if(PermissionsManager.areLocationPermissionsGranted(this))
		{
			checkSession();
		}
		else
		{
			// Carica il permission manager
			bouncer = new PermissionsManager(this);
			bouncer.requestLocationPermissions(this);
		}
	}
	
	private void checkSession(){
		String sessionId = getSharedPreferences("settings", Context.MODE_PRIVATE).getString("session_id",null);
		if(sessionId == null) {
			JsonObjectRequest jReq = new JsonObjectRequest(
					Request.Method.GET,
					getText(R.string.register_url).toString(),
					null,
					this,
					this);
			Volley.newRequestQueue(this).add(jReq);
		}
		else {
			loadGame();
		}
	}
	
	private void loadGame() {
		this.startActivity(new Intent(this, MapActivity.class));
	}
	
	@Override
	public void onPermissionResult(boolean granted)
	{
		if(granted) {
			checkSession();
		}
		else {
			Snackbar.make(b.lytBackSplash, getText(R.string.no_location), Snackbar.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		bouncer.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	@Override public void onExplanationNeeded(List<String> permissionsToExplain) {}
	
	@Override
	public void onResponse(JSONObject response)
	{
		try	{
			String sid = response.getString("session_id");
			getSharedPreferences("shrd-prf", Context.MODE_PRIVATE).edit().putString("session_id",sid).apply();
		}
		catch (JSONException e) {
			Snackbar.make(b.lytBackSplash, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
		finally	{
			loadGame();
		}
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		Snackbar.make(b.lytBackSplash, getText(R.string.no_internet), Snackbar.LENGTH_LONG).show();
	}
	
}
