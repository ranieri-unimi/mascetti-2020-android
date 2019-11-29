package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.matrice.databinding.ActivityChartBinding;
import com.example.matrice.databinding.ElementChartBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity implements Response.Listener <JSONObject>, Response.ErrorListener
{
	public Smaug h = Smaug.getInstance();
	public ActivityChartBinding b;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_chart);
		try {
			Smaug.sendJSONRequest(this,this,this, R.string.ranking_url, new JSONObject());
		} catch (JSONException e) {
			Snackbar.make(b.lytBackChart, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onResponse (JSONObject response)
	{
		ArrayList<Player> a = new ArrayList<>();
		
		try
		{
			JSONArray rk = response.getJSONArray("ranking");
			for(int i = 0; i< rk.length();i++)
			{
				JSONObject aRival = rk.getJSONObject(i);
				Player pushRival = new Player(this);
				
				
				pushRival.setUsername(aRival.getString("username"));
				pushRival.setXp(aRival.getString("xp"));
				pushRival.setHp(aRival.getString("lp"));
				pushRival.setImg(Smaug.from64toDraw(aRival.getString("img"), "rival"+i));
				//byte[] bs = Base64.decode(aRival.getString("img").getBytes(), Base64.DEFAULT);
				//pushRival.setImg(Drawable.createFromStream(new ByteArrayInputStream(bs),"rival"+i));
				
				a.add(pushRival);
			}
			
			b.rcyListChart.setAdapter(new ChartAdapter(a));
			b.rcyListChart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		}
		catch (JSONException e) {
			Snackbar.make(b.lytBackChart, getText(R.string.no_ok_data), Snackbar.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onErrorResponse (VolleyError error)
	{
		Snackbar.make(b.lytBackChart, getText(R.string.no_internet), Snackbar.LENGTH_LONG).show();
	}
	
	class ChartAdapter extends RecyclerView.Adapter<ChartVH>
	{
		private ArrayList<Player> rivalList;
		
		private ChartAdapter(ArrayList<Player> rivalList){
			this.rivalList = rivalList;
		}
		
		@NonNull
		@Override
		public ChartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
		{
			LayoutInflater lytInf = LayoutInflater.from(parent.getContext());
			ElementChartBinding e = ElementChartBinding.inflate(lytInf, parent, false);
			return new ChartVH(e);
		}
		
		@Override
		public void onBindViewHolder(@NonNull ChartVH holder, int position)
		{
			holder.bind(rivalList.get(position));
		}
		
		@Override
		public int getItemCount()
		{
			try { return rivalList.size(); }
			catch (NullPointerException e) { return 0; }
		}
	}
	
	class ChartVH extends RecyclerView.ViewHolder
	{
		private final ElementChartBinding b;
		
		private ChartVH(ElementChartBinding bind)
		{
			super(bind.getRoot());
			b = bind;
		}
		
		private void bind(Player r) {
			b.setRival(r);
			b.executePendingBindings();
		}
	}
	
}