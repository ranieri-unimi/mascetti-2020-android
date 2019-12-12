package com.example.matrice;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class Item
{
	// Binding
	private String name;
	private String size;
	private String type;
	private Drawable img;
	
	// Map & JSON
	private String id;
	private float lat;
	private float lng;
	
	public Item fromJSON(JSONObject j) throws JSONException
	{
		this.setName(j.getString("name"));
		this.setSize(j.getString("size"));
		this.setType(j.getString("type"));
		this.setLng(Float.parseFloat(j.getString("lon")));
		this.setLat(Float.parseFloat(j.getString("lat")));
		this.setId(j.getString("id"));
		
		return this;
	}
	
	// Getter and setter
	public String getName() {return name;}
	public void setName(String name) {this.name=name;}
	public String getSize() {return size;}
	public void setSize(String size) {this.size=size;}
	public String getType() {return type;}
	public void setType(String type) {this.type=type;}
	public Drawable getImg() {return img;}
	public void setImg(Drawable img) {this.img=img;}
	public String getId() {return id;}
	public void setId(String id) {this.id=id;}
	public float getLat() {return lat;}
	public void setLat(float lat) {this.lat=lat;}
	public float getLng() {return lng;}
	public void setLng(float lng) {this.lng=lng;}
	
}
