package com.voucher.manage.singleton;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Singleton {

	private static Singleton instance = new Singleton();
		
	public final static String ROOMDATABASE="[RoomManage]";
	
	//本地文件目录
	public final static String filePath="\\Desktop\\pasoft\\photo";
	
	//资产管理系统图片目录
	public static final String ROOMINFOIMGPATH	="D:\\SYSPICTURE\\";
	
	//资产管理系统图片目录2
	public static final String ROOMINFOIMGPATH2	="D:\\SYSPICTURE\\pasoft";
	
	//短信帐号
	public static final String UID="兴泸资产管理有限公司";
	
	//短信密钥
	public static final String KEY="d41d8cd98f00b204e980";
	
	private LinkedHashMap<String, Map<String, Date>> registerMap;
	
    private Singleton (){    	
    }  
    
    public static Singleton getInstance() {  
    	return instance;  
    }
	
	public LinkedHashMap<String, Map<String, Date>> getRegisterMap() {
		if (registerMap == null) {
			this.registerMap = new LinkedHashMap<String, Map<String, Date>>() {
				/**
				* 
				*/
				private static final long serialVersionUID = 1L;

				protected boolean removeEldestEntry(Map.Entry<String, Map<String, Date>> eldest) {
					long diff = 0;
					try {
						Date startDate = eldest.getValue().get("startTime");
						Date nowDate = new Date();
						diff = nowDate.getTime() - startDate.getTime();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return true;
					}

					/*
					 * System.out.println("endDate=   "+endDate.getTime());
					 * System.out.println("noeDate=   "+nowDate.getTime());
					 * System.out.println("diff="+diff/1000);
					 */
					return diff / 1000 > 60;
				}
			};
			return registerMap;
		} else {
			return registerMap;
		}
	}
  
}
