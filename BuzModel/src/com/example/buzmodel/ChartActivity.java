package com.example.buzmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import com.example.buzmodel.model.TBuz;
import com.example.buzmodel.view.MyCommonDialogMaker;
import com.example.buzmodel.view.MyCommonPopWindow;
import com.example.buzmodel.view.MyCommonDialogMaker.DialogCallBack;
import com.example.buzmodel.view.MyCommonPopWindow.CommonBottomUpPopWondowCallBack;
import com.example.buzmodel.view.grid.lib.AbsGridChart;
import com.example.buzmodel.view.grid.lib.GridChartException;
import com.example.buzmodel.view.grid.lib.IGridChart;
import com.example.buzmodel.view.grid.view.TimeChart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class ChartActivity extends Activity {
	  /** The encapsulated graphical view. */
	  private GraphicalView mView;
	  /** The chart to be drawn. */
	  private AbsGridChart mChart;
	  
	  private Dialog mFuncMenu, mDatePicker;
	  
	  public static final String TAG_START_MODE = "start_mode"; 
	  // 模式 - 动态趋势图
	  public static final int MODE_DYNAMIC = 0X001;
	  // 模式 - 静态趋势图
	  public static final int MODE_STATIC  = 0X002;
	  public int mStartMode = MODE_DYNAMIC;
	  
	  private static final String[] FUNC_TITLE_DYNAMIC = {"插入新数据", "查询","停止插入新数据"};//, "开启时间线"};
	  private static final String[] FUNC_TITLE_STATIC = {"保存到图片","取消"};
	  private static final String[] PICKER_TITLE = {"确认","取消"};

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    mStartMode = getIntent().getIntExtra(TAG_START_MODE, MODE_DYNAMIC);
	    Bundle extras = getIntent().getExtras();
	    mChart = (AbsGridChart) extras.getSerializable(ChartFactory.CHART);
	    mView = new GraphicalView(this, mChart);
	    String title = extras.getString(ChartFactory.TITLE);
	    if (title == null) {
	      requestWindowFeature(Window.FEATURE_NO_TITLE);
	    } else if (title.length() > 0) {
	      setTitle(title);
	    }
	    setContentView(mView);
	  }
	  
	  @Override
	protected void onResume() {
		super.onResume();
		((TimeChart)mChart).startTimer(mHandler, mView);
	}
	  
	  
	  @Override
	protected void onDestroy() {
		super.onDestroy();
		((TimeChart)mChart).stopTimer();
	}
	  
	  @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				if (null == mFuncMenu) {
					String[] funcTitles = null;
					switch (mStartMode) {
					case MODE_DYNAMIC:
						funcTitles = FUNC_TITLE_DYNAMIC;
						break;
					case MODE_STATIC:
						funcTitles = FUNC_TITLE_STATIC;
						break;
					default:
						break;
					}
					if (null != funcTitles && funcTitles.length != 0) {
						mFuncMenu = MyCommonPopWindow.showCommonBottomUpPopWondow(ChartActivity.this, 
								funcTitles, mCallback);
					}
				} else if (mFuncMenu.isShowing()) {
					mFuncMenu.dismiss();
				} else {
					mFuncMenu.show();
				}
				return true;

			default:
				break;
			}
			return super.onKeyDown(keyCode, event);
		}
	  
	  	private CommonBottomUpPopWondowCallBack mCallback = new CommonBottomUpPopWondowCallBack() {
			@Override
			public void onClickItem(View clickedView, int position) {
				if (null != mFuncMenu && mFuncMenu.isShowing()) {
					mFuncMenu.dismiss();
				}
				switch (mStartMode) {
				case MODE_DYNAMIC: {
					dealWithDynamicMenu(position);
				}
				break;
				case MODE_STATIC: {
					dealWithStaticMenu(position);
				}
				break;
				default:
					break;
				}
			}
			
		};
		
		private void dealWithStaticMenu(int position) {
			switch (position) {
			case 0:
				// TODO 保存bitmap到本地图片
				String uri = Utils.saveBitmap2DCIMFolder(ChartActivity.this.getApplicationContext(), 
						mView.toBitmap(), String.valueOf(System.currentTimeMillis()), "description for chart.");
				Toast.makeText(ChartActivity.this, "图片被保存到:\n" + uri, Toast.LENGTH_SHORT).show();
				break;
			case 1:
				break;
			default:
				break;
			}
		}
		
		private void dealWithDynamicMenu(int position) {
			switch (position) {
			case 0:
				if (!pushing) {
					pushing = true;
					mHandler.postDelayed(mTestPusher, 1000l);
				}
				break;
			case 1:
				if (null == mDatePicker) {
					mDatePicker = MyCommonDialogMaker.showDatePickerDialog(
							ChartActivity.this, "请选择时间段", "2014-07-01", PICKER_TITLE, new DialogCallBack() {
								@Override
								public void onCancelDialog(Dialog dialog, Object tag) {
									
								}
								@Override
								public void onButtonClicked(Dialog dialog, int position, Object tag) {
									testQuery();
								}
								
							}, true);
				} else if (!mDatePicker.isShowing()) {
					mDatePicker.show();
				}
				break;
			case 2:
				pushing = false;
				break;
			case 3:
				((TimeChart)mChart).startTimer(mHandler, mView);
				break;
			default:
				break;
			}
		}

		/**
		 * 模拟测试查询数据
		 */
		private void testQuery() {
			IGridChart chart = new TimeChart();
			Intent intent = chart.initDataSet(ChartActivity.this, Utils.getRandomDataList(10, MODE_STATIC, "Test_1"));
            intent.putExtra(ChartActivity.TAG_START_MODE, ChartActivity.MODE_STATIC);
            ChartActivity.this.startActivity(intent);
		}
		
		/**
		 * 模拟测试插入数据
		 */
		private void testInsertNewNode() {
			List<TBuz> nodes = Utils.getRandomDataList(1, MODE_DYNAMIC, "Test_0");
			try {
				mChart.addDataSet(nodes);
			} catch (GridChartException e) {
				e.printStackTrace();
				Toast.makeText(ChartActivity.this, "非法数据", Toast.LENGTH_SHORT).show();
				return;
			}
			// 重绘
			mView.repaint();
		}
		private boolean pushing = false;
		private Handler mHandler = new Handler();
		private Runnable mTestPusher = new Runnable() {
			@Override
			public void run() {
				testInsertNewNode();
				if (pushing) {
					mHandler.postDelayed(this, 1000l);
				}
			}
		};
}
