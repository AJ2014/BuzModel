package com.example.buzmodel.cache;

import java.io.File;

import com.example.buzmodel.R;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

/**
 * 
 * @author junjiang2
 * 数据库操作类
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
	 * 初始化:
	 * 1、打开或创建数据库
	 * 2、若表不存在则创建表
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
			// TODO 给控制层发异常消息
		}
	}
	
	private void closeCursor(Cursor c) {
		if (null != c && Build.VERSION.SDK_INT < 14) {
			c.close();
		}
	}
}
