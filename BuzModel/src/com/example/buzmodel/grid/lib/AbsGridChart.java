package com.example.buzmodel.grid.lib;

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
	
	protected double[] xValues;
	protected double[] yValues;
	
	public AbsGridChart() {
		renderer = buildRenderer(new int[]{pColor}, new PointStyle[]{pStyle});
		((XYSeriesRenderer) renderer.getSeriesRendererAt(0)).setFillPoints(true);
		
		initRenderer();
	}

	
	private void initRenderer() {
		renderer.setChartTitle(getName());
	    renderer.setXTitle(getXName());
	    renderer.setYTitle(getYName());
//	    renderer.setXAxisMin(xMin);
//	    renderer.setXAxisMax(xMax);
//	    renderer.setYAxisMin(yMin);
//	    renderer.setYAxisMax(yMax);
	    renderer.setAxesColor(aColor);
	    renderer.setLabelsColor(lColor);
	    renderer.setXLabels(12);
	    renderer.setYLabels(10);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
	    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
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
		  for (int i = 0; i < size; i++) {
			  series.add(xValue[i], yValue[i]);
		  }
	  }
}
