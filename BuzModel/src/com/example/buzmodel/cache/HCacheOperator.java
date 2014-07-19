package com.example.buzmodel.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.buzmodel.model.TBuz;

import android.content.Context;

/**
 * 
 * @author junjiang2
 * ����������
 */
public class HCacheOperator {
	
	private Context context;
	private DBObserverManager observerManager;
	public static enum EUpdateType {
		INSERT, DELETE
	}
	
	private static HCacheOperator instance;
	private HCacheOperator(Context context) {
		this.context = context;
		observerManager = new DBObserverManager();
	};
	
	public static HCacheOperator getInstance(Context context) {
		if (null == instance) {
			instance = new HCacheOperator(context);
		}
		return instance;
	}

	/**
	 * �����»�ȡ�����������
	 * @param records
	 * @return
	 */
	public int insert(List<TBuz> records) {
		return 0;
	}
	
	/**
	 * ����ʱ��β�ѯ�����¼
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<TBuz> query(int id, long startTime, long endTime) {
		return null;
	}
	
	/**
	 * ����ʱ��β�ѯ�����¼
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<TBuz> query(int id, Date startDate, Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		return query(id, startTime, endTime);
	}
	
	/**
	 * ����ʱ���ɾ�������¼
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public int delete(int id, long startTime, long endTime) {
		return 0;
	}
	
	/**
	 * ����ʱ���ɾ�������¼
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int delete(int id, Date startDate, Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		return delete(id, startTime, endTime);
	}
	
	private void sendUpdateMsg(EUpdateType type, Object params) {
		if (null != observerManager) {
			observerManager.sendUpdateMsg(type, params);
		}
	}
	
}
