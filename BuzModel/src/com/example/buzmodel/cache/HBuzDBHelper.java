package com.example.buzmodel.cache;

import com.example.buzmodel.R;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

/**
 * 
 * @author junjiang2
 * ��ݿ������
 */
public class HBuzDBHelper {
	
	private static String DB_NAME;
	private static String TB_NAME;
	private Context					context;
	private SQLiteDatabase 			dataBase;
	private static HBuzDBHelper 	instance;
	
	private HBuzDBHelper(Context context) {
		this.context = context;
		DB_NAME = context.getString(R.string.db_name);
		TB_NAME = context.getString(R.string.tb_name);
		setup();
	}
	
	public static HBuzDBHelper getInstance(Context context) {
		if (null == instance) {
			instance = new HBuzDBHelper(context);
		}
		return instance;
	}
	
	/**
	 * ��ʼ��:
	 * 1���򿪻򴴽���ݿ�
	 * 2�����?�����򴴽���
	 */
	private void setup() {
		try {
			dataBase = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
			
			Cursor c = dataBase.rawQuery(context.getString(R.string.check_tb_exist), new String[]{TB_NAME});
			if (!c.moveToFirst() || c.getInt(0) <= 0) {
				dataBase.execSQL(context.getString(R.string.sql_create_tb_name));
			}
			closeCursor(c);
			
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO ����Ʋ㷢�쳣��Ϣ
		}
	}
	
	private void closeCursor(Cursor c) {
		if (null != c && Build.VERSION.SDK_INT < 14) {
			c.close();
		}
	}
}
