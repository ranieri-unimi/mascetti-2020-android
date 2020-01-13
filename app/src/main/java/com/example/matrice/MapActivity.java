package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.example.matrice.databinding.ActivityMapBinding;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;


public class MapActivity extends AppCompatActivity implements
		OnMapReadyCallback,
		Response.Listener<JSONObject>,
		Style.OnStyleLoaded,
		LocationEngineCallback<LocationEngineResult>,
		Response.ErrorListener,
		MapView.OnStyleImageMissingListener,
		MapboxMap.OnMapClickListener
{
	
	private final Smaug h = Smaug.getInstance();
	private ActivityMapBinding b;
	public static final String H_LOC = "ARE_ITEMS_OBSOLETE";
	
	private MapboxMap mapObj;
	private MapView mapLyt;
	
	private LatLng lastCood = new LatLng(45.465, 9.190);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Binding e layout
		super.onCreate(savedInstanceState);
		Mapbox.getInstance(this, getString(R.string.mapbox_token));
		b = DataBindingUtil.setContentView(this, R.layout.activity_map);
		
		// Binding provvisorio ProgressBar
		b.setUser((Player)h.get(getString(R.string.profile)));
		
		// Map views
		mapLyt = b.map;
		mapLyt.onCreate(savedInstanceState);
		
		// Posizione attiva?
		try {
			LocationManager locMan = (LocationManager) this.getSystemService(LOCATION_SERVICE);
			if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				// Activate location
				initLocEng();
				// Say: No item!
				h.put(H_LOC, true);
				// Task new map
				mapLyt.getMapAsync(this);
				return;
			}
		}
		catch (NullPointerException e) { Log.e("!!","Empty LocaManager in Map On Create"); }
		
		// Colonne d'Ercole - NO LOCATION
		Toast.makeText(this, getText(R.string.no_location), Toast.LENGTH_LONG).show();// New Activity
		Intent i = new Intent(this, SplashActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		finish();
		this.startActivity(i);
	}
	
	private void initLocEng()
	{
		// Meccanica della posizione
		LocationEngineRequest req = new LocationEngineRequest.Builder(1005)
				.setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
				.setMaxWaitTime(6000)
				//.setFastestInterval(750)
				.build();
		LocationEngine locEng = LocationEngineProvider.getBestLocationEngine(this);
		locEng.requestLocationUpdates(req, this, getMainLooper());
		locEng.getLastLocation(this);
	}
	
	@Override
	public void onSuccess(LocationEngineResult result)
	{
		if(result.getLastLocation() == null)
			return;
		lastCood =  new LatLng(result.getLastLocation());
		
		if(mapObj == null)
			return;
		
		try { mapObj.getLocationComponent().forceLocationUpdate(result.getLastLocation()); }
		catch (Exception e) { Log.e("!!", "Location Component not ready yet"); }
	}
	
	@Override
	public void onFailure(@NonNull Exception exception) {
		Snackbar.make(b.lytBackMap, getText(R.string.no_location), Snackbar.LENGTH_SHORT).show();
	}
	
	@Override
	public void onMapReady(@NonNull MapboxMap mapboxMap)
	{
		// Map getted
		mapObj = mapboxMap;
		
		// Camera start
		CameraPosition position = new CameraPosition.Builder()
				.target(new LatLng(lastCood.getLatitude(), lastCood.getLongitude() ))
				.zoom(9)
				.build();
		mapObj.animateCamera(CameraUpdateFactory.newCameraPosition(position));
		
		loadStyle();
	}
	
	public void loadStyle(){
		
		// Revalidate Smaug Items
		h.put(H_LOC, false);
		
		// Getting styling element
		try {
			Smaug.sendJSONRequest(
					this,this,
					this,R.string.getmap_url,new JSONObject());
			mapLyt.addOnStyleImageMissingListener(this);
		}
		catch (JSONException e) {
			Snackbar.make(b.lytBackMap, getText(R.string.no_ok_data), Snackbar.LENGTH_SHORT).show();
		}
	}
	
	@Override public void onResponse(JSONObject response)
	{
		ArrayList<Feature> mapPins = new ArrayList<>();
		
		try {
			// Getting items
			JSONArray jList = response.getJSONArray("mapobjects");
			for(int i = 0; i< jList.length();i++)
			{
				// Item
				Item item = new Item().fromJSON(jList.getJSONObject(i));
				h.put(item.getId(),item);
				// Feature
				Feature feat = Feature.fromGeometry(Point.fromLngLat(item.getLng(),item.getLat()), null, item.getId());
				feat.addStringProperty("ID", item.getId());
				feat.addStringProperty("NAME", item.getName());
				mapPins.add(feat);
			}
			
			// Setting style
			mapObj.setStyle(  // TODO : null referece on map-object, bad concurrency
					new Style.Builder()
							.fromUri(Style.DARK)
							.withSource(new GeoJsonSource("ITEM_SOURCE", FeatureCollection.fromFeatures(mapPins))),
					this
			);
		}
		catch (JSONException e) {
			Snackbar.make(b.lytBackMap, getText(R.string.no_ok_data), Snackbar.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onStyleLoaded(@NonNull Style style)
	{
		componentLoad();
		mapObj.addOnMapClickListener(this);
		style.addLayer(
				new SymbolLayer("ITEM_LAYER", "ITEM_SOURCE")
						.withProperties(
								iconImage(get("ID")),
								iconIgnorePlacement(true),
								iconAllowOverlap(true),
								textField(get("NAME")),
								textColor("#D9B668"),
								textIgnorePlacement(true),
								textAllowOverlap(true),
								textOffset(new Float[] { 0f, 2f })
						)
		);
	}
	
	@Override
	public void onStyleImageMissing(@NonNull final String id)
	{
		// Image already loaded
		Drawable i = ((Item) h.get(id)).getImg();
		if(i != null) {
			mapObj.getStyle().addImageAsync(id, Smaug.fromDrawabletoBitmap(i));
			return;
		}
		
		// Default image
		mapObj.getStyle().addImageAsync(id, Smaug.fromDrawabletoBitmap(getDrawable(R.drawable.map_item)));
		
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("target_id", id);
			Smaug.sendJSONRequest(this,
					new Response.Listener<JSONObject>() {
						@Override public void onResponse(JSONObject response) {
							try {
								String img64 = response.getString("img");
								mapObj.getStyle().addImageAsync(id, Smaug.from64toBitmap(img64));
								((Item) h.get(id)).setImg(Smaug.from64toDraw(img64, "item"+id));
							}
							catch (JSONException e) { Log.e("!!","no da ok in Image Missing"); }
							catch (NullPointerException e) { Log.e("!!","null image in Image Missing"); }
						}
					},
					this,
					R.string.getimage_url,
					jsonObj
			);
		}
		catch (JSONException e) { Log.e("!!","no da ok in Image Missing"); }
	}
	
	@Override
	public boolean onMapClick(@NonNull LatLng point)
	{
		PointF pointf = mapObj.getProjection().toScreenLocation(point);
		RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
		List<Feature> featureList = mapObj.queryRenderedFeatures(rectF, "ITEM_LAYER");
		
		if (featureList.size() <1)
			return false;
		Intent intent = new Intent(this, FightActivity.class);
		for (Feature feature: featureList)
			intent.putExtra("Id", feature.id());
		double dst = point.distanceTo(lastCood);
		intent.putExtra("Distance", dst);
		this.startActivity(intent);
		return true;
	}
	
	private void componentLoad()
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
		//dot.setCameraMode(CameraMode.TRACKING_COMPASS);
		dot.setRenderMode(RenderMode.COMPASS);
	}
	
	@Override public void onResume() {
		super.onResume();
		mapLyt.onResume();
		
		if(mapObj != null && mapObj.getStyle() != null){
			loadStyle();
			loadHp();
		}
	}
	
	public void loadHp() {
		// Binding ProgressBar definitivo
		try {
			Smaug.sendJSONRequest(this, new Response.Listener<JSONObject>() {
						@Override public void onResponse(JSONObject response) {
							try {
								Player userProfile = new Player(MapActivity.this).fromJSON(response);
								h.put(getString(R.string.profile), userProfile);
								b.setUser(userProfile);
							}
							catch (JSONException e) {
								Snackbar.make(b.lytBackMap, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
							}
						}
					}
					,this, R.string.getprofile_url, new JSONObject());
		} catch (JSONException e) {
			Snackbar.make(b.lytBackMap, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	
	@Override public void onStart() { super.onStart(); mapLyt.onStart(); }
	@Override public void onPause() { super.onPause(); mapLyt.onPause(); }
	@Override public void onStop() { super.onStop(); mapLyt.onStop(); }
	@Override public void onLowMemory() { super.onLowMemory(); mapLyt.onLowMemory(); }
	@Override protected void onDestroy() { super.onDestroy(); mapLyt.onDestroy(); }
	@Override protected void onSaveInstanceState(@NonNull Bundle outState) { super.onSaveInstanceState(outState); mapLyt.onSaveInstanceState(outState); }
	@Override public void onErrorResponse(VolleyError error)  {
		Snackbar.make(b.lytBackMap, getText(R.string.no_internet), Snackbar.LENGTH_INDEFINITE).show(); }
	public void onFabClick(View v) { this.startActivity(new Intent(this, ProfileActivity.class)); }
}