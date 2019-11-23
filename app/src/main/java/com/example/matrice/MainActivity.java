package com.example.matrice;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.matrice.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.core.location.*;
import com.mapbox.android.core.permissions.*;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.*;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
		OnMapReadyCallback,
		PermissionsListener,
		Style.OnStyleLoaded,
		LocationEngineCallback < LocationEngineResult > ,
		View.OnClickListener {
	
	private Smaug h = Smaug.getInstance();
	private ActivityMainBinding b;
	
	private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 2500L;
	private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
	
	private MapboxMap box;
	private MapView gpsView;
	private PermissionsManager boss;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Istruzioni Magiche
		super.onCreate(savedInstanceState);
		Mapbox.getInstance(this, getString(R.string.mapbox_token));
		b = DataBindingUtil.setContentView(this, R.layout.activity_main);
		
		gpsView = b.mapMain;
		gpsView.onCreate(savedInstanceState);
		boss = new PermissionsManager(this);
		
		// Task mappa
		gpsView.getMapAsync(this);
		
		// Task bottone
		getSupportActionBar().hide();
		b.btnMain.setText("Attiva GPS");
		b.btnMain.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == b.btnMain.getId()) {
			if (PermissionsManager.areLocationPermissionsGranted(this)) {
				gpsOk();
			}
			else {
				boss.requestLocationPermissions(this);
			}
		}
	}
	
	public void gpsOk() {
		Snackbar.make(b.lytMain, "Sto caricando la tua posizione...", Snackbar.LENGTH_SHORT).show();
		b.btnMain.setText("GPS attivato");
		b.btnMain.setEnabled(false);
	}
	
	@Override
	public void onMapReady(MapboxMap mapboxMap) {
		
		// Mappa creata
		this.box = mapboxMap;
		
		// Task stile
		mapboxMap.setStyle(Style.OUTDOORS, this);
	}
	
	@Override public void onStyleLoaded(@NonNull Style style) {
		// Stile Creato
		
		// Devo avviare l'engine della posizione?
		if (PermissionsManager.areLocationPermissionsGranted(this)) {
			enableLocationComponent(style);
			gpsOk();
		} else {
			// Task per DAMMI I PERMESSI FIGLIO DI TUA MADRE
			boss.requestLocationPermissions(this);
		}
	}
	
	public void enableLocationComponent(Style style) {
		// Styling del pallino
		LocationComponentActivationOptions dotOpt = LocationComponentActivationOptions.builder(this, style).useDefaultLocationEngine(false).build();
		
		// L'oggetto del pallino
		LocationComponent gpsDot = box.getLocationComponent();
		gpsDot.activateLocationComponent(dotOpt);
		gpsDot.setLocationComponentEnabled(true);
		gpsDot.setCameraMode(CameraMode.TRACKING_COMPASS);
		gpsDot.setRenderMode(RenderMode.COMPASS);
		
		// Tipo di aggiornamento
		LocationEngineRequest req = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS).setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY).setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
		
		// Robot del GPS
		LocationEngine gpsRobot = LocationEngineProvider.getBestLocationEngine(this);
		gpsRobot.requestLocationUpdates(req, this, getMainLooper());
		
		// Task nuova posizione
		gpsRobot.getLastLocation(this);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		boss.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	@Override
	public void onExplanationNeeded(List < String > permissionsToExplain) {
		Snackbar.make(b.lytMain, "Senza permessi questa app è inutile. CONCEDIMELI!", Snackbar.LENGTH_INDEFINITE).show();
	}
	
	@Override
	public void onPermissionResult(boolean granted) {
		if (granted) {
			gpsOk();
			// Se il consenso arriva prima dello stile
			if (box.getStyle() != null) {
				// Nel dubbio ricaricalo una seconda volta
				enableLocationComponent(box.getStyle());
			}
		}
		else {
			Snackbar.make(b.lytMain, "Non hai attivato il GPS, KTTV", Snackbar.LENGTH_INDEFINITE).show();
			//finish();
		}
	}
	
	@Override
	public void onSuccess(LocationEngineResult result) {
		if (box != null && result.getLastLocation() != null) {
			box.getLocationComponent().forceLocationUpdate(result.getLastLocation());
		}
	}
	
	@Override
	public void onFailure(@NonNull Exception exception) {
		Snackbar.make(b.lytMain, exception.getMessage(), Snackbar.LENGTH_SHORT).show();
	}
	
	@Override public void onStart() {
		super.onStart();
		gpsView.onStart();
	}@Override public void onResume() {
		super.onResume();
		gpsView.onResume();
	}@Override public void onPause() {
		super.onPause();
		gpsView.onPause();
	}@Override public void onStop() {
		super.onStop();
		gpsView.onStop();
	}@Override public void onLowMemory() {
		super.onLowMemory();
		gpsView.onLowMemory();
	}@Override protected void onDestroy() {
		super.onDestroy();
		gpsView.onDestroy();
	}@Override protected void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		gpsView.onSaveInstanceState(outState);
	}
}
