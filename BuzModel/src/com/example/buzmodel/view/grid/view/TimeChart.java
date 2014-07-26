package com.example.buzmodel.view.grid.view;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;

import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.buzmodel.ChartActivity;
import com.example.buzmodel.Utils;
import com.example.buzmodel.model.TBuz;
import com.example.buzmodel.view.grid.lib.AbsGridChart;
import com.example.buzmodel.view.grid.lib.GridChartException;
import com.example.buzmodel.view.grid.lib.GridChartException.ExceptionCode;

public class TimeChart extends AbsGridChart {

	private static final String TIME_FORMAT = "HH:mm:ss";
	
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
		updateTimerEmptyValue();
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
		
		for (int i = 0; i < dSize; i++) {
			TBuz nodei = data.get(i);
			xValues[i] = nodei.getDate();
			yValues[i] = nodei.getValue();
		}
		
		addXYPairs(mDataset.getSeriesAt(0), xValues, yValues);
		updateTimerEmptyValue();
	}
	
	/**
	 * 时间轴显示时间格式
	 */
	@Override
	public String getXLabel(double value) {
		long timeStamp = (long) value;
		DateFormat dFormat = new DateFormat();
		return (String) dFormat.format(TIME_FORMAT, timeStamp);
	}
	
	/**
	 * y轴显示推送值的格式
	 */
	@Override
	public String getYLabel(double value) {
		return super.getYLabel(value);
	}

	/**
	 * 添加自动生成"空"值的series，用于滚动时间线
	 * @param title			标题
	 * @param unitLength	单位长度
	 * @param emptyValue	初始空值
	 */
	public void addAutoGenSeries(XYMultipleSeriesDataset dataset, String title, 
			long unitLength, double emptyValue) {
		XYSeries series = new XYSeries(title);
		dataset.addSeries(series);
	}
	
	public void startTimer(Handler handler, GraphicalView view) {
		if (null == mTimer) {
			mTimer = new TimerSeriesGenerator(handler, this, view);
		}
		mTimer.start();
	}
	
	public void stopTimer() {
		if (null != mTimer) {
			mTimer.stop();
		}
	}
	
	public void updateTimerEmptyValue() {
		XYSeries series0 = mDataset.getSeriesAt(0);
		double newEmpValue = (series0.getMinY() + series0.getMaxY()) / 2.0;
		if (null != mTimer) {
			mTimer.emptyValue = newEmpValue;
		}
	}
	
	@Override
	protected XYMultipleSeriesDataset buildDataset(String title,
			double[] xValue, double[] yValue) {
		XYMultipleSeriesDataset dataset = super.buildDataset(title, xValue, yValue);
		addAutoGenSeries(dataset, "", 1000, 0);
		return dataset;
	}
	
	
	private TimerSeriesGenerator mTimer;
	public static class TimerSeriesGenerator implements Runnable {

		public long unitLength = 1000l;
		public double emptyValue = 0;
		public Handler handler;
		public GraphicalView view;
		public AbsGridChart chart;
		public boolean running = false;
		
		public void start() {
			running = true;
			new Thread(this).start();
		}
		
		public void stop() {
			running = false;
		}
		
		public TimerSeriesGenerator(Handler handler, AbsGridChart chart, GraphicalView view) {
			this.handler = handler;
			this.chart = chart;
			this.view= view;
		}
		
		@Override
		public void run() {
			// 时间数据 和 推送数据同时更新?
			final XYSeries series0 = chart.getDataset().getSeriesAt(0);
			final XYSeries series = chart.getDataset().getSeriesAt(1);
			// 是否推送数据已经设置 
			boolean isPushInit = (null != series0 && series0.getItemCount() > 0);
			
			if (null == series) {
				return;
			}
			if (running) {
				long timeStamp = System.currentTimeMillis();
				chart.addXYPairs(series, new double[]{timeStamp}, new double[]{emptyValue}, !isPushInit);
				// 重绘
				view.repaint();
				handler.postDelayed(this, unitLength);
			} 
			
		}
		
	}

}
