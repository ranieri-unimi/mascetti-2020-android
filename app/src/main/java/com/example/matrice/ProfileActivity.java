package com.example.matrice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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
		
		b.lytBackA.edtNameProfile.setOnKeyListener(this);
		
		// Loading info player
		b.setUser((Player)h.get(getString(R.string.profile)));
	}
	
	public void onChartClick(View v) { this.startActivity(new Intent(this, ChartActivity.class)); }
	
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
		
		try {
			InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
			
			// File size check
			if(inputStream.available() > 100*1024)
				throw new OutOfMemoryError();
			
			// Decoding
			String imgBase = Smaug.fromImageto64(BitmapFactory.decodeStream(inputStream));
			
			// JSON
			JSONObject postObj = new JSONObject();
			postObj.put("img", imgBase);
			Smaug.sendJSONRequest(this,new OnImage(),this,R.string.setprofile_url,postObj);
			
		}
		catch (IOException | NullPointerException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_content), Snackbar.LENGTH_LONG).show(); }
		catch (JSONException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show(); }
		catch (OutOfMemoryError e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.too_big), Snackbar.LENGTH_LONG).show(); }
	}
	
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent keyevent)
	{
		// If the keyevent is a key-down event on the "enter" button
		if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
		{
			try {
				// Setting new username
				JSONObject postObj = new JSONObject();
				postObj.put("username", b.lytBackA.edtNameProfile.getText());
				Smaug.sendJSONRequest(this, new OnUsername(),this,R.string.setprofile_url,postObj);
				return true;
			}
			catch (JSONException e) {
				Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
			}
		}
		return false;
	}
	
	public class OnProfile implements Response.Listener <JSONObject> {
		@Override public void onResponse(JSONObject response)
		{
			try {
				Player userProfile = new Player(ProfileActivity.this).fromJSON(response);
				h.put(getString(R.string.profile), userProfile);
				
				// Reload Binder
				b.setUser(userProfile);
			}
			catch (JSONException e) {
				Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
			}
		}
	}
	
	public class OnImage implements Response.Listener <JSONObject> {
		@Override public void onResponse(JSONObject response)
		{
			Snackbar.make(b.lytBackProfile, getText(R.string.image_changed), Snackbar.LENGTH_LONG).show();
			reloadProfile();
		}
	}
	
	public class OnUsername implements Response.Listener <JSONObject> {
		@Override public void onResponse(JSONObject response)
		{
			Snackbar.make(b.lytBackProfile, getText(R.string.username_changed), Snackbar.LENGTH_LONG).show();
			reloadProfile();
		}
	}
	
	public void reloadProfile()
	{
		try {
			Smaug.sendJSONRequest(this, new OnProfile(),this, R.string.getprofile_url, new JSONObject());
		} catch (JSONException e) {
			Snackbar.make(b.lytBackProfile, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onErrorResponse(VolleyError error) {
		Snackbar.make(b.lytBackProfile, getText(R.string.no_internet), Snackbar.LENGTH_LONG).show();
		b.setUser((Player)h.get(getString(R.string.profile)));
	}
}
