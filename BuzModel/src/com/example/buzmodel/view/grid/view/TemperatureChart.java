package com.example.buzmodel.view.grid.view;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;

import com.example.buzmodel.model.TBuz;
import com.example.buzmodel.view.grid.lib.AbsGridChart;
import com.example.buzmodel.view.grid.lib.GridChartException;
import com.example.buzmodel.view.grid.lib.GridChartException.ExceptionCode;

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
		double[] xValues = null;
		double[] yValues = null;
		if (null != data || !data.isEmpty()) {
			final int dSize = data.size();
			xValues = new double[dSize];
			yValues = new double[dSize];
			for (int i = 0; i < dSize; i++) {
				TBuz nodei = data.get(i);
				xValues[i] = nodei.getDate();
				yValues[i] = nodei.getValue();
			}
		}
		mDataset = buildDataset(getName(), xValues, yValues);
		setDatasetRenderer(mDataset, mRenderer);
		Intent intent = ChartFactory.getGridChartIntent(context, this, getName());
		return intent;
	}

	@Override
	public Intent initDataSet(Context context, TBuz[] data) {
		double[] xValues = null;
		double[] yValues = null;
		if (null != data || data.length != 0) {
			final int dSize = data.length;
			xValues = new double[dSize];
			yValues = new double[dSize];
			for (int i = 0; i < dSize; i++) {
				TBuz nodei = data[i];
				xValues[i] = nodei.getDate();
				yValues[i] = nodei.getValue();
			}
		}
		
		mDataset = buildDataset(getName(), xValues, yValues);
		setDatasetRenderer(mDataset, mRenderer);
		Intent intent = ChartFactory.getGridChartIntent(context, this, getName());
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
		addXYPairs(mDataset.getSeriesAt(0), xValues, yValues);
	}
	
	@Override
	public void addDataSet(List<TBuz> data) throws GridChartException {
		if (null == data || data.isEmpty()) {
			// TODO initial empty chart
			return;
		}
		final int dSize = data.size();
		double[] xValues = new double[dSize];
		double[] yValues = new double[dSize];
		
		double minXValue = Double.MAX_VALUE;
		double minYValue = Double.MAX_VALUE;
		double maxXValue = Double.MIN_VALUE;
		double maxYValue = Double.MIN_VALUE;
		
		for (int i = 0; i < dSize; i++) {
			TBuz nodei = data.get(i);
			xValues[i] = nodei.getDate();
			yValues[i] = nodei.getValue();
			if (xValues[i] > maxXValue) {
				maxXValue = xValues[i];
			}
			if (xValues[i] < minXValue) {
				minXValue = xValues[i];
			}
			if (yValues[i] > maxYValue) {
				maxYValue = yValues[i];
			}
			if (yValues[i] < minYValue) {
				minYValue = yValues[i];
			}
		}
		
		if (minXValue < this.maxXValue) {// 新数据x轴值比老数据还小
			throw new GridChartException(ExceptionCode.ERROR_INVALID_DATA_SET);
		}
		
		if (minXValue > this.maxXValue + mMaxClearRange 
				|| maxXValue < this.minXValue - mMaxClearRange
				|| minYValue > this.maxYValue + mMaxClearRange
				|| maxYValue < this.minYValue - mMaxClearRange) {
			clearDataset();
		}
		
		addXYPairs(mDataset.getSeriesAt(0), xValues, yValues);
	}
	
	@Override
	public String getXLabel(double value) {
		long timeStamp = (long) value;
		DateFormat dFormat = new DateFormat();
		return (String) dFormat.format("HH:mm:ss", timeStamp);
	}
	
	@Override
	public String getYLabel(double value) {
		return super.getYLabel(value);
	}

}
