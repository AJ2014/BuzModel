package com.example.buzmodel.view.grid.lib;

import java.util.List;

import com.example.buzmodel.model.TBuz;

import android.content.Context;
import android.content.Intent;

public interface IGridChart {
	
	  /** A constant for the name field in a list activity. */
	  String NAME = "name";
	  /** A constant for the description field in a list activity. */
	  String DESC = "desc";

	  /**
	   * Returns the chart name.
	   * 
	   * @return the chart name
	   */
	  String getName();

	  /**
	   * Returns the chart description.
	   * 
	   * @return the chart description
	   */
	  String getDesc();
	  
	  String getXName();
	  
	  String getYName();
	  
	  /**
	   * Initial chart with data.
	   * 
	   * @param context the context
	   * @param data the init data set
	   * 
	   * @return the built intent
	   */
	  Intent initDataSet(Context context, List<TBuz> data);
	  Intent initDataSet(Context context, TBuz[] data);

	  /**
	   * Add chart data
	   * 
	   * @param data the data to be added
	   */
	  void addDataSet(List<TBuz> data) throws GridChartException;
	  void addDataSet(TBuz[] data) throws GridChartException;
	  
	  void clearDataset();
	  
}
