package com.example.buzmodel.cache;

import java.util.List;

import com.example.buzmodel.model.TBuz;

/**
 * 
 * @author junjiang2
 * 数据库更新回调
 */
public interface IDBUpdateCallback {

	public void onInsert(List<TBuz> records);
	
}
