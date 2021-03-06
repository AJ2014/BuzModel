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

	protected int pColor = Color.YELLOW, aColor = Color.WHITE,
			lColor = Color.WHITE;
	protected PointStyle pStyle = PointStyle.CIRCLE;

	// x轴上可多移动的距离
	protected static int MARGIN_RANGE_X = 10;
	// y轴上可多移动的距离
	protected static int MARGIN_RANGE_Y = 10;

	// 可视窗口距离 以毫秒为单位
	private static final int RENDER_RANGE_COUNT = 10 * 1000;
	// series总共缓存的点个数
	private static final int MAX_VISIBLE_COUNT = 30;

	public AbsGridChart() {
		mRenderer = buildRenderer(new int[] { pColor, Color.TRANSPARENT },
				new PointStyle[] { pStyle, PointStyle.X });
		((XYSeriesRenderer) mRenderer.getSeriesRendererAt(0))
				.setFillPoints(true);

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
	 * @param colors
	 *            the series rendering colors
	 * @param styles
	 *            the series point styles
	 * @return the XY multiple series renderers
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
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

	protected XYMultipleSeriesDataset buildDataset(String title,
			double[] xValue, double[] yValue) {
		mDataset = new XYMultipleSeriesDataset();
		addXYSeries(mDataset, title, xValue, yValue);
		return mDataset;
	}

	protected void addXYSeries(XYMultipleSeriesDataset dataset, String title,
			double[] xValue, double[] yValue) {
		XYSeries series = new XYSeries(title);
		addXYPairs(series, xValue, yValue, true);
		dataset.addSeries(series);
	}
	
	public void addXYPairs(XYSeries series, double[] xValue, double[] yValue) {
		if (null == xValue || xValue.length == 0 || null == yValue
				|| yValue.length == 0) {
			return;
		}
		final int size = xValue.length;
		for (int i = 0; i < size; i++) {
			series.add(xValue[i], yValue[i]);
		}
		updateLimites(series);
	}
	
	/**
	 * 添加新值到指定series里
	 * @param series
	 * @param xValue
	 * @param yValue
	 * @param updatelimites 是否需要更新renderer, true renderer完整更新{range,pan,zoom}, 
	 * 		  false renderer 只更新x轴对应的limites
	 */
	public void addXYPairs(XYSeries series, double[] xValue, double[] yValue, boolean updatelimites) {
		if (null == xValue || xValue.length == 0 || null == yValue
				|| yValue.length == 0) {
			return;
		}
		final int size = xValue.length;
		for (int i = 0; i < size; i++) {
			series.add(xValue[i], yValue[i]);
		}
		if (updatelimites) {
			updateLimites(series);
		} else {
			updateXLimites(series);
		}
	}

	private void updateXLimites(XYSeries series) {
		final int itemCount = series.getItemCount();
		if (itemCount == 0) {
			return;
		}
		if (itemCount > MAX_VISIBLE_COUNT) {
			// remove the minit x item;
			series.removeFirst();
		}
		double maxX = series.getMaxX();
		double minX = maxX - RENDER_RANGE_COUNT;
		mRenderer.setXAxisMax(maxX);
		mRenderer.setXAxisMin(minX);
		
		MARGIN_RANGE_X = (int) ((series.getMaxX() - series.getMinX()) / itemCount);
		MARGIN_RANGE_X = MARGIN_RANGE_X < 1 ? 1 : MARGIN_RANGE_X;
		
		double lx = series.getMinX() - MARGIN_RANGE_X;
		double mx = series.getMaxX() + MARGIN_RANGE_X;
		
		double[] panLimits = mRenderer.getPanLimits();
		double[] zoomLimits = mRenderer.getZoomLimits();
		panLimits[0] = lx; panLimits[1] = mx;
		zoomLimits[0]= lx; zoomLimits[1]= mx;
		mRenderer.setPanLimits(panLimits);
		mRenderer.setZoomLimits(zoomLimits);
	}

	protected void updateLimites(XYSeries series) {
		final int itemCount = series.getItemCount();
		if (itemCount == 0) {
			return;
		}
		if (itemCount > MAX_VISIBLE_COUNT) {
			// remove the minit x item;
			series.removeFirst();
		}

		double maxX = series.getMaxX();
		double minX = maxX - RENDER_RANGE_COUNT; // required x-range
		double maxY = series.getMinY();
		double minY = series.getMaxY();

		mRenderer.setRange(new double[] { minX, maxX, minY, maxY });

		MARGIN_RANGE_X = (int) ((series.getMaxX() - series.getMinX()) / itemCount);
		MARGIN_RANGE_Y = (int) ((series.getMaxY() - series.getMinY()) / itemCount);

		MARGIN_RANGE_X = MARGIN_RANGE_X < 1 ? 1 : MARGIN_RANGE_X;
		MARGIN_RANGE_Y = MARGIN_RANGE_Y < 1 ? 1 : MARGIN_RANGE_Y;

		double lx = series.getMinX() - MARGIN_RANGE_X;
		double mx = series.getMaxX() + MARGIN_RANGE_X;
		double ly = series.getMinY() - MARGIN_RANGE_Y;
		double my = series.getMaxY() + MARGIN_RANGE_Y;

		mRenderer.setPanLimits(new double[] { lx, mx, ly, my });
		mRenderer.setZoomLimits(new double[] { lx, mx, ly, my });
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
