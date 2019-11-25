package com.example.matrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matrice.databinding.ActivityChartBinding;
import com.example.matrice.databinding.ActivityMainBinding;
import com.example.matrice.databinding.ActivityMapBinding;

public class ChartActivity extends AppCompatActivity
{
	
	private Smaug h = Smaug.getInstance();
	private ActivityChartBinding b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		b = DataBindingUtil.setContentView(this, R.layout.activity_chart);
		b.rcyListChart.setAdapter(new ChartAdapter());
	}
	
	class ChartAdapter extends RecyclerView.Adapter<ChartVH>
	{
		
		@NonNull
		@Override
		public ChartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
		{
			return new ChartVH(
					LayoutInflater
							.from(parent.getContext())
							.inflate(R.layout.element_chart, parent, false)
			);
		}
		
		@Override
		public void onBindViewHolder(@NonNull ChartVH holder, int position)
		{
		
		}
		
		@Override
		public int getItemCount()
		{
			return 0;
		}
	}
	
	class ChartVH extends RecyclerView.ViewHolder
	{
		
		public ChartVH(@NonNull View itemView)
		{
			super(itemView);
		}
	}
}