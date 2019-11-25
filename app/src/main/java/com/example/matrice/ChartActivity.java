package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.matrice.databinding.ActivityChartBinding;
import com.example.matrice.databinding.ElementChartBinding;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity
{
	
	private Smaug h = Smaug.getInstance();
	private ActivityChartBinding b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_chart);
		
		// RIEMPIRE L'ARRAY
		List<ChartElement> a = new ArrayList<>();
		a.add(new ChartElement());
		a.add(new ChartElement());
		a.add(new ChartElement());
		
		b.rcyListChart.setAdapter(new ChartAdapter(a));
	}
	
	class ChartAdapter extends RecyclerView.Adapter<ChartVH>
	{
		private List<ChartElement> rivalList;
		
		private ChartAdapter(List<ChartElement> rivalList){
			this.rivalList = rivalList;
		}
		
		@NonNull
		@Override
		public ChartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
		{
			return new ChartVH(
					ElementChartBinding.inflate(
							LayoutInflater.from(parent.getContext()),
							parent,
							false
					)
			);
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
		ElementChartBinding b;
		
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