package com.example.matrice;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

public class Player
{
	private String username;
	private String xp;
	private String hp;
	private Drawable img;
	private int hpValue;
	
	public int getHpValue() { return hpValue; }
	public Drawable getImg() { return img; }
	public String getUsername() { return username; }
	public String getXp() { return xp; }
	public String getHp() { return hp; }
	
	
	public Player fromJSON(JSONObject playerObject) throws JSONException {
		// Set object
		this.setUsername(playerObject.getString("username"));
		this.setXp(playerObject.getString("xp"));
		this.setHp(playerObject.getString("lp"));
		
		// Getting IMG response
		String img = playerObject.getString("img");
		Drawable imgDrw =  Smaug.from64toDraw(img, this.getUsername());
		this.setImg(imgDrw);
		
		return this;
	}
	
	public void setHpValue(int hpValue) {
		this.hpValue = hpValue;
		this.hp = ""+hpValue;
	}
	
	public void setImg(Drawable img) {
		if (img != null)
			this.img = img;
	}
	
	public void setUsername(String username) {
		if (!username.equals("null"))
			this.username=username;
	}
	
	public void setHp(String hp) {
		this.hp = hp;
		this.hpValue = Integer.parseInt(hp);
	}
	
	public void setXp(String xp) { this.xp = xp; }
	
	public Player(Context context) {
		username = "(no name)";
		xp = "0";
		hp = "100";
		hpValue = 100;
		img = context.getDrawable(R.drawable.chart_player);
	}
}
