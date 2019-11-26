package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.matrice.databinding.ActivityChartBinding;
import com.example.matrice.databinding.ElementChartBinding;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity
{
	public Smaug h = Smaug.getInstance();
	public ActivityChartBinding b;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_chart);
		
		// RIEMPIRE L'ARRAY
		ArrayList<ChartElement> a = new ArrayList<>();
		a.add(new ChartElement(getApplicationContext()));
		a.add(new ChartElement(getApplicationContext()));
		a.add(new ChartElement(getApplicationContext()));
		
		b.rcyListChart.setAdapter(new ChartAdapter(a));
		b.rcyListChart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}
	
	class ChartAdapter extends RecyclerView.Adapter<ChartVH>
	{
		private ArrayList<ChartElement> rivalList;
		
		private ChartAdapter(ArrayList<ChartElement> rivalList){
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
		
		private void bind(ChartElement r) {
			b.setRival(r);
			b.executePendingBindings();
		}
	}
	
}