package com.example.matrice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Smaug extends HashMap<String, Object>
{
	private static final Smaug ourInstance = new Smaug();
	
	public static Smaug getInstance()
	{
		return ourInstance;
	}
	
	public static Player fromJSONtoPlayer(JSONObject playerObject, Context context) throws JSONException
	{
		// Set object
		Player player = new Player(context);
		player.setUsername(playerObject.getString("username"));
		player.setXp(playerObject.getString("xp"));
		player.setHp(playerObject.getString("lp"));
		
		// Getting IMG response
		String img = playerObject.getString("img");
		Drawable imgDrw =  Smaug.from64toDraw(img, player.getUsername());
		player.setImg(imgDrw);
		
		return player;
	}
	
	public static Drawable from64toDraw(String base64Image, String runtimeName)
	{
		ByteArrayInputStream imgByte = new ByteArrayInputStream(Base64.decode(base64Image.getBytes(), Base64.DEFAULT));
		return Drawable.createFromStream(imgByte, runtimeName);
	}
	
	public static String fromImageto64(Bitmap image)
	{
		ByteArrayOutputStream imgByte = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, imgByte);
		return Base64.encodeToString(imgByte.toByteArray(), Base64.DEFAULT);
	}
	
}
