package com.example.buzmodel.view.grid.view;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import com.example.buzmodel.ChartActivity;
import com.example.buzmodel.view.grid.lib.AbsGridChart;

import android.content.Context;
import android.content.Intent;

public class ChartFactory {

	/** The key for the chart data. */
	public static final String CHART = "chart";

	/** The key for the chart graphical activity title. */
	public static final String TITLE = "title";

	public static final Intent getGridChartIntent(Context context,
			AbsGridChart chart,
			String activityTitle) {
		checkParameters(chart.getDataset(), chart.getRenderer());
		Intent intent = new Intent(context, ChartActivity.class);
		intent.putExtra(CHART, chart);
		intent.putExtra(TITLE, activityTitle);
		return intent;
	}

	/**
	 * Checks the validity of the dataset and renderer parameters.
	 * 
	 * @param dataset
	 *            the multiple series dataset (cannot be null)
	 * @param renderer
	 *            the multiple series renderer (cannot be null)
	 * @throws IllegalArgumentException
	 *             if dataset is null or renderer is null or if the dataset and
	 *             the renderer don't include the same number of series
	 */
	private static void checkParameters(XYMultipleSeriesDataset dataset,
			XYMultipleSeriesRenderer renderer) {
		if (dataset == null
				|| renderer == null
				|| dataset.getSeriesCount() != renderer
						.getSeriesRendererCount()) {
			throw new IllegalArgumentException(
					"Dataset and renderer should be not null and should have the same number of series");
		}
	}

}
