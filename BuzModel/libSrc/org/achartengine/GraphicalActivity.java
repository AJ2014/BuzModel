/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine;

import org.achartengine.chart.AbstractChart;

import com.example.buzmodel.view.MyCommonDialogMaker;
import com.example.buzmodel.view.MyCommonDialogMaker.DialogCallBack;
import com.example.buzmodel.view.MyCommonPopWindow;
import com.example.buzmodel.view.MyCommonPopWindow.CommonBottomUpPopWondowCallBack;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

/**
 * An activity that encapsulates a graphical view of the chart.
 * TODO 搜索控件需要另外加到主视图上,如何考虑历史数据加载和新数据更新(滑动预加载?)
 */
public class GraphicalActivity extends Activity {
  /** The encapsulated graphical view. */
  private GraphicalView mView;
  /** The chart to be drawn. */
  private AbstractChart mChart;
  
  private Dialog mFuncMenu, mDatePicker;
  
  private static final String[] FUNC_TITLE = {"查询","取消"};
  private static final String[] PICKER_TITLE = {"确认","取消"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle extras = getIntent().getExtras();
    mChart = (AbstractChart) extras.getSerializable(ChartFactory.CHART);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			if (null == mFuncMenu) {
				mFuncMenu = MyCommonPopWindow.showCommonBottomUpPopWondow(GraphicalActivity.this, 
						FUNC_TITLE, mCallback);
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
			switch (position) {
			case 0:
				if (null == mDatePicker) {
					mDatePicker = MyCommonDialogMaker.showDatePickerDialog(
							GraphicalActivity.this, "请选择时间段", "2014-07-01", PICKER_TITLE, new DialogCallBack() {
								@Override
								public void onCancelDialog(Dialog dialog, Object tag) {
									
								}
								@Override
								public void onButtonClicked(Dialog dialog, int position, Object tag) {
									
								}
							}, true);
				} else if (!mDatePicker.isShowing()) {
					mDatePicker.show();
				}
				break;
			case 1:
			default:
				break;
			}
		}
	};

}