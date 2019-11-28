package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
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

import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity implements Response.ErrorListener, View.OnKeyListener
{
	public Smaug h = Smaug.getInstance();
	public ActivityProfileBinding b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_profile);
		
		// Last saved Profile
		b.setUser((Player)h.get(getString(R.string.profile)));
		
		b.edtNameProfile.setOnKeyListener(this);
		
		// Loading info player
		initLoadProfile();
	}
	
	public void onChartClick(View v) { this.startActivity(new Intent(this, ChartActivity.class)); }
	
	@Override
	public void onErrorResponse(VolleyError error)
	{
		// Super Network Error
		Snackbar.make(b.lytBackProfile, getText(R.string.no_internet), Snackbar.LENGTH_LONG).show();
		// Maybe reupload from Smaug
	}
	
	public void initLoadProfile()
	{
		// Forge body request
		String sessionId = getSharedPreferences("settings", Context.MODE_PRIVATE).getString("session_id",null);
		JSONObject postObj = new JSONObject();
		try {
			postObj.put("session_id", sessionId);
			JsonObjectRequest jReq = new JsonObjectRequest(
					Request.Method.POST,
					getString(R.string.getprofile_url),
					postObj,
					new LoadProfile(),
					this
			);
			
			// Put request
			RequestQueue netQueue = Volley.newRequestQueue(this);
			netQueue.add(jReq);
		} catch (JSONException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	public class LoadProfile implements Response.Listener <JSONObject>
	{
		@Override
		public void onResponse(JSONObject res)
		{
			try
			{
				Player userProfile = Smaug.fromJSONtoPlayer(res, getApplicationContext());
				
				// Udate it
				h.put(getString(R.string.profile), userProfile);
				b.setUser(userProfile);
			}
			catch (JSONException e)
			{
				Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
			}
		}
	}
	
	public void onImageClick(View v)
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "Select your profile picture..."), 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		try
		{
			InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
			
			// File size check
			if(inputStream.available() > 100*1024)
				throw new OutOfMemoryError();
			
			
			// Decoding
			Bitmap tmp = BitmapFactory.decodeStream(inputStream);
			String imgBase = Smaug.fromImageto64(tmp);
			
			// Body modelling
			String sessionId = getSharedPreferences("settings", Context.MODE_PRIVATE).getString("session_id",null);
			JSONObject postObj = new JSONObject();
			postObj.put("session_id", sessionId);
			postObj.put("img", imgBase);
			
			// Forge request
			JsonObjectRequest jReq = new JsonObjectRequest(
					Request.Method.POST,
					getString(R.string.setprofile_url),
					postObj,
					new UploadImage(),
					this
			);
			RequestQueue netQueue = Volley.newRequestQueue(this);
			netQueue.add(jReq);
		}
		catch (IOException | NullPointerException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_content), Snackbar.LENGTH_LONG).show(); }
		catch (JSONException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show(); }
		catch (OutOfMemoryError e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.too_big), Snackbar.LENGTH_LONG).show(); }
	}
	
	public class UploadImage implements Response.Listener <JSONObject>
	{
		@Override
		public void onResponse(JSONObject response)
		{
			Snackbar.make(b.lytBackProfile, getText(R.string.image_changed), Snackbar.LENGTH_LONG).show();
			initLoadProfile();
		}
	}
	
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent keyevent)
	{
		// If the keyevent is a key-down event on the "enter" button
		if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
		{
			initUploadUsername();
			return true;
		}
		return false;
	}
	
	public void initUploadUsername()
	{
		// Forge body request
		String sessionId = getSharedPreferences("settings", Context.MODE_PRIVATE).getString("session_id",null);
		JSONObject postObj = new JSONObject();
		try {
			// Params
			postObj.put("session_id", sessionId);
			postObj.put("username", b.edtNameProfile.getText());
			
			JsonObjectRequest jReq = new JsonObjectRequest(
					Request.Method.POST,
					getString(R.string.setprofile_url),
					postObj,
					new UploadUsername(),
					this
			);
			
			// Put request
			RequestQueue netQueue = Volley.newRequestQueue(this);
			netQueue.add(jReq);
		} catch (JSONException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	public class UploadUsername implements Response.Listener <JSONObject>
	{
		@Override
		public void onResponse(JSONObject response)
		{
			Snackbar.make(b.lytBackProfile, getText(R.string.username_changed), Snackbar.LENGTH_LONG).show();
			//initLoadProfile();
		}
	}
}
