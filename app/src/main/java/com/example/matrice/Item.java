package com.example.matrice;

import org.json.JSONException;
import org.json.JSONObject;

public class Item
{
	// Binding
	private String _name;
	private String _size;
	private String _type;
	
	// Map & JSON
	private String _id;
	private float _lat;
	private float _lng;
	
	public Item (JSONObject j) throws JSONException
	{
		_name = j.getString("name");
		_size = j.getString("size");
		_type = j.getString("type");
		_lng = Float.parseFloat(j.getString("lon"));
		_lat = Float.parseFloat(j.getString("lat"));
		_id = j.getString("id");
	}
}
