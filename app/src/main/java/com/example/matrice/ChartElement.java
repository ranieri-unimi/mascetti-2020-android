package com.example.matrice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;

public class ChartElement
{
	private String username;
	private String xp;
	private String hp;
	private Drawable img;
	
	public Drawable getImg() { return img; }
	public void setImg(Drawable img) { if (img!=null) this.img = img; }
	public String getUsername() { return username; }
	public void setUsername(String username) { if (username!="null") this.username=username; }
	public String getXp() { return xp; }
	public void setXp(String xp) { this.xp = xp; }
	public String getHp() { return hp; }
	public void setHp(String hp) { this.hp = hp; }
	
	public ChartElement(Context c)
	{
		username = "(no name)";
		xp = "0";
		hp = "100";
		img = c.getDrawable(R.drawable.chart_rival);
	}

	@BindingAdapter({"srcCompat"})
	public static void setImageView(ImageView view, Drawable draw) {
		view.setImageDrawable(draw);
	}
}
