package com.example.buzmodel.grid.view;

import java.util.List;

import org.achartengine.ChartFactory;

import android.content.Context;
import android.content.Intent;

import com.example.buzmodel.grid.lib.AbsGridChart;
import com.example.buzmodel.model.TBuz;

public class TemperatureChart extends AbsGridChart {

	@Override
	public String getName() {
		return "TemperatureChart";
	}

	@Override
	public String getDesc() {
		return "TemperatureChart Desc";
	}
	
	@Override
	public String getXName() {
		return "Time";
	}

	@Override
	public String getYName() {
		return "Temperature";
	}

	@Override
	public Intent initDataSet(Context context, List<TBuz> data) {
		if (null == data || data.isEmpty()) {
			// TODO initial empty chart
			return null;
		}
		final int dSize = data.size();
		xValues = new double[dSize];
		yValues = new double[dSize];
		for (int i = 0; i < dSize; i++) {
			TBuz nodei = data.get(i);
			xValues[i] = nodei.getDate();
			yValues[i] = nodei.getValue();
		}
		dataset = buildDataset(getName(), xValues, yValues);
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
		        "Average temperature");
		return intent;
	}

	@Override
	public void addDataSet(List<TBuz> data) {
		if (null == data || data.isEmpty()) {
			// TODO initial empty chart
			return;
		}
		final int dSize = data.size();
		double[] xValues = new double[dSize];
		double[] yValues = new double[dSize];
		for (int i = 0; i < dSize; i++) {
			TBuz nodei = data.get(i);
			xValues[i] = nodei.getDate();
			yValues[i] = nodei.getValue();
		}
		addXYPairs(dataset.getSeriesAt(0), xValues, yValues);
	}

	@Override
	public Intent initDataSet(Context context, TBuz[] data) {
		if (null == data || data.length == 0) {
			// TODO initial empty chart
			return null;
		}
		final int dSize = data.length;
		xValues = new double[dSize];
		yValues = new double[dSize];
		for (int i = 0; i < dSize; i++) {
			TBuz nodei = data[i];
			xValues[i] = nodei.getDate();
			yValues[i] = nodei.getValue();
		}
		dataset = buildDataset(getName(), xValues, yValues);
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
		        "Average temperature");
		return intent;
	}

	@Override
	public void addDataSet(TBuz[] data) {
		if (null == data || data.length == 0) {
			// TODO initial empty chart
			return;
		}
		final int dSize = data.length;
		double[] xValues = new double[dSize];
		double[] yValues = new double[dSize];
		for (int i = 0; i < dSize; i++) {
			TBuz nodei = data[i];
			xValues[i] = nodei.getDate();
			yValues[i] = nodei.getValue();
		}
		addXYPairs(dataset.getSeriesAt(0), xValues, yValues);
	}

}
