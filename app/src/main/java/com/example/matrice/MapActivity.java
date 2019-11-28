package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.matrice.databinding.ActivityMapBinding;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


public class MapActivity extends AppCompatActivity implements
		OnMapReadyCallback,
		Style.OnStyleLoaded,
		LocationEngineCallback<LocationEngineResult>
{
	
	private Smaug h = Smaug.getInstance();
	private ActivityMapBinding b;
	
	private MapboxMap mapObj;
	private MapView mapLyt;
	private Location lastCood;
	private boolean locInit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Binding e layout
		super.onCreate(savedInstanceState);
		Mapbox.getInstance(this, getString(R.string.mapbox_token));
		b = DataBindingUtil.setContentView(this, R.layout.activity_map);
		
		// View della mappa
		mapLyt = b.map;
		mapLyt.onCreate(savedInstanceState);
		mapLyt.getMapAsync(this);
		
		// Posizione attiva
		try
		{
			LocationManager locMan = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
			if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				inintLocEng();
				return;
			}
		}
		catch (NullPointerException e) { }
		
		Toast.makeText(this, getText(R.string.no_location), Toast.LENGTH_LONG).show();
		this.startActivity(new Intent(this, SplashActivity.class));
	}
	
	@Override
	public void onMapReady(@NonNull MapboxMap mapboxMap)
	{
		mapObj = mapboxMap;
		// Puoi personalizzare lo stile con le features
		mapObj.setStyle(Style.DARK, this);
	}
	
	@Override
	public void onStyleLoaded(@NonNull Style style)
	{
		// Altre cose da fare con lo stile già caricato
	}
	
	@Override
	public void onSuccess(LocationEngineResult result)
	{
		// Gate gps
		if(result.getLastLocation() == null)
			return;
		// gps ok
		lastCood = result.getLastLocation();
		
		// Gate mappa
		if(mapObj == null)
			return;
		
		// Gate stile
		Style s = mapObj.getStyle();
		if(s == null)
			return;
		
		// Gate LocationComponent
		if(locInit)
		{
			mapObj.getLocationComponent().forceLocationUpdate(lastCood);
		}
		else
		{
			firstLoad();
			locInit = true;
		}
	}
	
	@Override
	public void onFailure(@NonNull Exception exception)
	{
		Snackbar.make(b.lytBackMap, getText(R.string.no_location), Snackbar.LENGTH_SHORT).show();
	}
	
	private void firstLoad()
	{
		// Styling del pallino
		LocationComponentActivationOptions dotOpt = LocationComponentActivationOptions
				.builder(this, mapObj.getStyle())
				.useDefaultLocationEngine(false)
				//.styleRes(R.style.AppTheme)
				.build();
		
		// L'oggetto del pallino
		LocationComponent dot = mapObj.getLocationComponent();
		dot.activateLocationComponent(dotOpt);
		dot.setLocationComponentEnabled(true);
		dot.setCameraMode(CameraMode.TRACKING_COMPASS);
		dot.setRenderMode(RenderMode.COMPASS);
		dot.zoomWhileTracking(13,2400);
		dot.tiltWhileTracking(60, 100);

            		/*
		// Posizione di inizio della camera
		CameraPosition position = new CameraPosition.Builder()
				.target(new LatLng(lastCood.getLatitude(), lastCood.getLongitude())) // Sets the new camera position
				.zoom(13) // Sets the zoom
				.bearing(180) // Rotate the camera
				.tilt(60) // Set the camera tilt
				.build(); // Creates a CameraPosition from the builder
		mapObj.animateCamera(CameraUpdateFactory.newCameraPosition(position), 700);
		 */
	}
	
	private void inintLocEng()
	{
		// Meccanica della posizione
		LocationEngineRequest req = new LocationEngineRequest.Builder(750)
				.setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
				.setMaxWaitTime(6000)
				.setFastestInterval(750)
				.build();
		LocationEngine locEng = LocationEngineProvider.getBestLocationEngine(this);
		locEng.requestLocationUpdates(req, this, getMainLooper());
		locEng.getLastLocation(this);
	}
	
	public void onFabClick(View v)
	{
		this.startActivity(new Intent(this, ProfileActivity.class));
	}
	
	@Override public void onStart() { super.onStart(); mapLyt.onStart(); }
	@Override public void onResume() { super.onResume(); mapLyt.onResume(); }
	@Override public void onPause() { super.onPause(); mapLyt.onPause(); }
	@Override public void onStop() { super.onStop(); mapLyt.onStop(); }
	@Override public void onLowMemory() { super.onLowMemory(); mapLyt.onLowMemory(); }
	@Override protected void onDestroy() { super.onDestroy(); mapLyt.onDestroy(); }
	@Override protected void onSaveInstanceState(@NonNull Bundle outState) { super.onSaveInstanceState(outState); mapLyt.onSaveInstanceState(outState); }
}


