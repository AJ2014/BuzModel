package com.example.buzmodel.view.grid.lib;

import java.util.List;

import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;

public abstract class AbsGridChart extends LineChart implements IGridChart {

	protected int pColor = Color.YELLOW, aColor = Color.WHITE, lColor = Color.WHITE;
	protected PointStyle pStyle = PointStyle.CIRCLE;
	
	protected static int MARGIN_RANGE_X = 10;
	protected static int MARGIN_RANGE_Y = 10;
	
	protected static double mMinClearRange = 1000;
	protected static double mMaxClearRange = 5000;
	
	protected double minXValue = Double.MAX_VALUE;
	protected double maxXValue = Double.MIN_VALUE;
	protected double minYValue = Double.MAX_VALUE;
	protected double maxYValue = Double.MIN_VALUE;
	
	public AbsGridChart() {
		mRenderer = buildRenderer(new int[]{pColor}, new PointStyle[]{pStyle});
		((XYSeriesRenderer) mRenderer.getSeriesRendererAt(0)).setFillPoints(true);
		
		initRenderer();
	}

	
	private void initRenderer() {
		mRenderer.setChartTitle(getName());
		mRenderer.setXTitle(getXName());
		mRenderer.setYTitle(getYName());
		mRenderer.setAxesColor(aColor);
		mRenderer.setLabelsColor(lColor);
		mRenderer.setXLabels(12);
		mRenderer.setYLabels(10);
		mRenderer.setXLabelsAlign(Align.RIGHT);
		mRenderer.setYLabelsAlign(Align.RIGHT);
	}


	/**
	   * Builds an XY multiple series renderer.
	   * 
	   * @param colors the series rendering colors
	   * @param styles the series point styles
	   * @return the XY multiple series renderers
	   */
	  protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    setRenderer(renderer, colors, styles);
	    return renderer;
	  }

	  protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setMargins(new int[] { 20, 30, 15, 20 });
	    int length = colors.length;
	    for (int i = 0; i < length; i++) {
	      XYSeriesRenderer r = new XYSeriesRenderer();
	      r.setColor(colors[i]);
	      r.setPointStyle(styles[i]);
	      renderer.addSeriesRenderer(r);
	    }
	  }
	  
	  protected XYMultipleSeriesDataset buildDataset(String title, double[] xValue, double[] yValue) {
		  mDataset = new XYMultipleSeriesDataset();
		  addXYSeries(mDataset, title, xValue, yValue);
		  return mDataset;
	  }
	  
	  protected void addXYSeries(XYMultipleSeriesDataset dataset, String title, double[] xValue, double[] yValue) {
		  XYSeries series = new XYSeries(title);
		  addXYPairs(series, xValue, yValue);
		  dataset.addSeries(series);
	  }
	  
	  protected void addXYPairs(XYSeries series, double[] xValue, double[] yValue) {
		  if (null == xValue || xValue.length == 0
				  || null == yValue || yValue.length == 0) {
			  return;
		  }
		  final int size = xValue.length;
//		  boolean isLimitUpdated = false;
		  for (int i = 0; i < size; i++) {
			  series.add(xValue[i], yValue[i]);
//			  if (xValue[i] > maxXValue) {
//				  maxXValue = xValue[i];
//				  isLimitUpdated = true;
//			  }
//			  if (xValue[i] < minXValue) {
//				  minXValue = xValue[i];
//				  isLimitUpdated = true;
//			  }
//			  if (yValue[i] > maxYValue) {
//				  maxYValue = yValue[i];
//				  isLimitUpdated = true;
//			  }
//			  if (yValue[i] < minYValue) {
//				  minYValue = yValue[i];
//				  isLimitUpdated = true;
//			  }
		  }
//		  if (isLimitUpdated) {
			  // limit update change the pan and zoom limit
			  updateLimites(series);
//		  }
	  }

	protected void updateLimites(XYSeries series) {
		final int itemCount = series.getItemCount();
		MARGIN_RANGE_X = (int) ((maxXValue - minXValue) / itemCount);
		MARGIN_RANGE_Y = (int) ((maxYValue - minYValue) / itemCount);
		
		MARGIN_RANGE_X = MARGIN_RANGE_X < 1 ? 1 : MARGIN_RANGE_X;
		MARGIN_RANGE_Y = MARGIN_RANGE_Y < 1 ? 1 : MARGIN_RANGE_Y;
		
		
		final int count = series.getItemCount();
		if (count > 5) {
			series.remove(series.getIndexForKey(series.getMinX()));
		}
		mRenderer.setXAxisMin(series.getMinX());
		mRenderer.setXAxisMax(series.getMaxX());
		mRenderer.setYAxisMin(series.getMinY());
		mRenderer.setYAxisMax(series.getMaxY());
		
		double lx = series.getMinX() - MARGIN_RANGE_X;
		double mx = series.getMaxX() + MARGIN_RANGE_X;
		double ly = series.getMinY() - MARGIN_RANGE_Y;
		double my = series.getMaxY() + MARGIN_RANGE_Y;
		
		mRenderer.setPanLimits(new double[] {lx, mx, ly, my});
		mRenderer.setZoomLimits(new double[] {lx, mx, ly, my});
	}
	
	@Override
	public void clearDataset() {
		if (null == mDataset) {
			return;
		}
		if (null == mDataset.getSeriesAt(0)) {
			return;
		}
		mDataset.getSeriesAt(0).clear();
	}
	
}
