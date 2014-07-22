package com.example.buzmodel.view.grid.lib;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;

public abstract class AbsGridChart implements IGridChart {

	protected int pColor = Color.YELLOW, aColor = Color.WHITE, lColor = Color.WHITE;
	protected PointStyle pStyle = PointStyle.CIRCLE;
	protected XYMultipleSeriesRenderer renderer;
	protected XYMultipleSeriesDataset dataset;
	
	protected static int MARGIN_RANGE_X = 10;
	protected static int MARGIN_RANGE_Y = 10;
	
	protected double[] xValues;
	protected double[] yValues;
	
	private double minXValue = Double.MAX_VALUE;
	private double maxXValue = Double.MIN_VALUE;
	private double minYValue = Double.MAX_VALUE;
	private double maxYValue = Double.MIN_VALUE;
	
	public AbsGridChart() {
		renderer = buildRenderer(new int[]{pColor}, new PointStyle[]{pStyle});
		((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setFillPoints(true);
		
		initRenderer();
	}

	
	private void initRenderer() {
		renderer.setChartTitle(getName());
	    renderer.setXTitle(getXName());
	    renderer.setYTitle(getYName());
	    renderer.setAxesColor(aColor);
	    renderer.setLabelsColor(lColor);
	    renderer.setXLabels(12);
	    renderer.setYLabels(10);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
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
		  dataset = new XYMultipleSeriesDataset();
		  addXYSeries(dataset, title, xValue, yValue);
		  return dataset;
	  }
	  
	  protected void addXYSeries(XYMultipleSeriesDataset dataset, String title, double[] xValue, double[] yValue) {
		  XYSeries series = new XYSeries(title);
		  addXYPairs(series, xValue, yValue);
		  dataset.addSeries(series);
	  }
	  
	  protected void addXYPairs(XYSeries series, double[] xValue, double[] yValue) {
		  final int size = xValue.length;
		  boolean isLimitUpdated = false;
		  for (int i = 0; i < size; i++) {
			  series.add(xValue[i], yValue[i]);
			  if (xValue[i] > maxXValue) {
				  maxXValue = xValue[i];
				  isLimitUpdated = true;
			  }
			  if (xValue[i] < minXValue) {
				  minXValue = xValue[i];
				  isLimitUpdated = true;
			  }
			  if (yValue[i] > maxYValue) {
				  maxYValue = yValue[i];
				  isLimitUpdated = true;
			  }
			  if (yValue[i] < minYValue) {
				  minYValue = yValue[i];
				  isLimitUpdated = true;
			  }
		  }
		  if (isLimitUpdated) {
			  // limit update change the pan and zoom limit
			  updateLimites(series);
		  }
	  }

	protected void updateLimites(XYSeries series) {
		final int itemCount = series.getItemCount();
		MARGIN_RANGE_X = (int) ((maxXValue - minXValue) / itemCount);
		MARGIN_RANGE_Y = (int) ((maxYValue - minYValue) / itemCount);
		
		MARGIN_RANGE_X = MARGIN_RANGE_X < 1 ? 1 : MARGIN_RANGE_X;
		MARGIN_RANGE_Y = MARGIN_RANGE_Y < 1 ? 1 : MARGIN_RANGE_Y;
		
		renderer.setXAxisMin(minXValue);
		renderer.setXAxisMax(maxXValue);
		renderer.setYAxisMin(minYValue);
		renderer.setYAxisMax(maxYValue);
		
		double lx = minXValue - MARGIN_RANGE_X;
		double mx = maxXValue + MARGIN_RANGE_X;
		double ly = minYValue - MARGIN_RANGE_Y;
		double my = maxYValue + MARGIN_RANGE_Y;
		
		renderer.setPanLimits(new double[] {lx, mx, ly, my});
		renderer.setZoomLimits(new double[] {lx, mx, ly, my});
	}
}
