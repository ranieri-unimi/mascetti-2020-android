package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.matrice.databinding.ActivityMainBinding;
import com.example.matrice.databinding.ActivitySplashBinding;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import java.security.Permission;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements PermissionsListener
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
			loadGame();
		}
		else
		{
			// Carica il permission manager
			bouncer = new PermissionsManager(this);
			bouncer.requestLocationPermissions(this);
		}
	}
	
	private void loadGame(){
		
		// Controlla se è una nuova partita
		//  sse Carica un session_id
		
		// Avvia la intent
	}
	
	
	@Override
	public void onPermissionResult(boolean granted)
	{
		if(granted) {
			loadGame();
		}
		else {
			Snackbar.make(b.lytBackSplash, getText(R.string.no_location), Snackbar.LENGTH_INDEFINITE);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		bouncer.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	@Override public void onExplanationNeeded(List<String> permissionsToExplain) {}
}
