package com.voucher.manage.singleton;

import com.voucher.manage.daoModel.Assets.Hidden_User;

public class Singleton {
	private static Singleton instance = new Singleton();
		
	public final static String ROOMDATABASE="[RoomManage]";
	
	//本地文件目录
	public final static String filePath="\\Desktop\\pasoft\\photo";
	
	//资产管理系统图片目录
	public static final String ROOMINFOIMGPATH	="D:\\SYSPICTURE\\";
	
	//资产管理系统图片目录2
	public static final String ROOMINFOIMGPATH2	="D:\\SYSPICTURE\\pasoft";
	
    private Singleton (){    	
    }  
    
    public static Singleton getInstance() {  
    	return instance;  
    }
	
  
}
