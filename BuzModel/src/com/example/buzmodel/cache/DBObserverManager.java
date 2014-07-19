package com.example.buzmodel.cache;

import java.util.ArrayList;
import java.util.List;

import com.example.buzmodel.cache.HCacheOperator.EUpdateType;
import com.example.buzmodel.model.TBuz;

/**
 * 
 * @author junjiang2
 * 数据库更新监听类
 */
public class DBObserverManager {

	private List<IDBUpdateCallback> observers;
	
	public void registerObserver(IDBUpdateCallback observer) {
		if (null == observers || observers.contains(observer)) {
			observers = new ArrayList<IDBUpdateCallback>();
		}
		observers.add(observer);
	}
	
	public void unRegisterObserver(IDBUpdateCallback observer) {
		if (null == observers || observers.isEmpty()) {
			return;
		}
		observers.remove(observer);
	}
	
	public void sendUpdateMsg(EUpdateType type, Object params) {
		if (null != observers && !observers.isEmpty()) {
			if (EUpdateType.INSERT == type) {
				for (IDBUpdateCallback observer : observers) {
					observer.onInsert((List<TBuz>) params);
				}
			}
		}
	}
	
}
