package com.voucher.manage.daoSQL;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.voucher.manage.daoSQL.annotations.DBTable;
import com.voucher.manage.daoSQL.annotations.QualifiLimit;
import com.voucher.manage.daoSQL.annotations.QualifiNotIn;
import com.voucher.manage.daoSQL.annotations.QualifiOffset;
import com.voucher.manage.daoSQL.annotations.QualifiOrder;
import com.voucher.manage.daoSQL.annotations.QualifiSort;
import com.voucher.manage.daoSQL.annotations.SQLDateTime;
import com.voucher.manage.daoSQL.annotations.SQLFloat;
import com.voucher.manage.daoSQL.annotations.SQLInteger;
import com.voucher.manage.daoSQL.annotations.SQLString;
import com.voucher.manage.daoSQL.annotations.QualifiWhere;



public class SelectSQL {

	
	
	public static String get(Class className) throws ClassNotFoundException{
   	    String name = className.getName();                                    //从控制台输入一个类名，我们输入User即可
        Class<?> cl = Class.forName(name);                         //加载类，如果该类不在默认路径底下，会报 java.lang.ClassNotFoundException
        DBTable dbTable = cl.getAnnotation(DBTable.class);         //从User类中获取DBTable注解
        Integer limit=0;
		Integer offset=10; 
		String notIn="";
    	String sort="";
		String order="";
        String tableName="";
        
        try{
         tableName = (dbTable.name().length()<1)?cl.getName():dbTable.name();//获取表的名字，如果没有在DBTable中定义，则获取类名作为Table的名字
        }catch (Exception e) {
			// TODO: handle exception
         tableName =name;
		 }
         List<String> columnDefs = new ArrayList<String>();
         String[] columnWhere=null;
         boolean term=false;
        for(Field field : cl.getDeclaredFields())                  //获取声明的属性
        {
            String columnName = null;           
            Annotation[] anns = field.getDeclaredAnnotations();//获取注解，一个属性可以有多个注解，所以是数组类型
            if(anns.length < 1)
            {
                continue;
            }else
            if(anns[0] instanceof SQLInteger)                //判断注解类型
            {
                SQLInteger sInt = (SQLInteger)anns[0];
                columnName = (sInt.name().length()<1)?field.getName():sInt.name();//获取列名称与获取表名一样
                columnDefs.add(columnName);//使用一个方法，自己写的getConstraints(Constraints constraints)获取列定义
            }else
            if(anns[0] instanceof SQLString)
            {
                SQLString sStr = (SQLString)anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName);
            }else
            if(anns[0] instanceof SQLFloat)
            {
                SQLFloat sStr = (SQLFloat) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName);
            }else
            if(anns[0] instanceof SQLDateTime)
            {
                SQLDateTime sStr = (SQLDateTime) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                columnDefs.add(columnName);
            }else
            if(anns[0] instanceof QualifiWhere)
            {
            	term=true;
                QualifiWhere sStr = (QualifiWhere) anns[0];
                columnWhere = sStr.where();
            }else
            if(anns[0] instanceof QualifiLimit)
            {
            	 QualifiLimit sStr = (QualifiLimit) anns[0];
                 columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                 limit=Integer.parseInt(columnName);
             }else	
            if(anns[0] instanceof QualifiOffset)
            {
            	QualifiOffset sStr = (QualifiOffset) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                offset=Integer.parseInt(columnName);
             }else
             if(anns[0] instanceof QualifiNotIn)
             {
            	QualifiNotIn sStr = (QualifiNotIn) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                notIn=columnName;
             }else	
            if(anns[0] instanceof QualifiSort)
            {
            	QualifiSort sStr = (QualifiSort) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                sort=columnName;
             }else
            if(anns[0] instanceof QualifiOrder)
            {
            	QualifiOrder sStr = (QualifiOrder) anns[0];
                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
                order=columnName;
             }
        }
        
        StringBuilder selectCommand = new StringBuilder("SELECT top "+limit);
                
        for(String columnDef :columnDefs){
            selectCommand.append("\n    "+columnDef+",");
        }
        
        String select=selectCommand.substring(0,selectCommand.length()-1)+"\n FROM \n   "+tableName;
        
        select=select+
        		 "\n  where "+notIn+
                 " not in("+
                 " select top "+offset+" "+notIn+" FROM [TTT].[dbo].[RoomInfo])";
        
        if(term){
          StringBuilder whereCommand = new StringBuilder();
          int i=1;
          for(String whereterm:columnWhere){
        	  if(i%2==0){
        		//  System.out.println("偶数");
        		  whereCommand.append(whereterm+"\n  AND ");
        	   }else{
        		//  System.out.println("奇数");
        		  whereCommand.append("\n   "+whereterm);
        	   }
           i++;
          }
          select=select+"\n  AND "+whereCommand.substring(0,whereCommand.length()-7);
        }

        
        return select;
   }
	
	public static String getCount(Class className) throws ClassNotFoundException{
	   	 String name = className.getName();                                    //从控制台输入一个类名，我们输入User即可
	        Class<?> cl = Class.forName(name);                         //加载类，如果该类不在默认路径底下，会报 java.lang.ClassNotFoundException
	        DBTable dbTable = cl.getAnnotation(DBTable.class);         //从User类中获取DBTable注解
	        String tableName="";
	        try{
	         tableName = (dbTable.name().length()<1)?cl.getName():dbTable.name();//获取表的名字，如果没有在DBTable中定义，则获取类名作为Table的名字
	        }catch (Exception e) {
				// TODO: handle exception
	         tableName =name;
			 }
	         List<String> columnDefs = new ArrayList<String>();
	         String[] columnWhere=null;
	         boolean term=false;
	        for(Field field : cl.getDeclaredFields())                  //获取声明的属性
	        {
	            String columnName = null;           
	            Annotation[] anns = field.getDeclaredAnnotations();//获取注解，一个属性可以有多个注解，所以是数组类型
	            if(anns.length < 1)
	            {
	                continue;
	            }else
	            if(anns[0] instanceof SQLInteger)                //判断注解类型
	            {
	                SQLInteger sInt = (SQLInteger)anns[0];
	                columnName = (sInt.name().length()<1)?field.getName():sInt.name();//获取列名称与获取表名一样
	                columnDefs.add(columnName);//使用一个方法，自己写的getConstraints(Constraints constraints)获取列定义
	            }else
	            if(anns[0] instanceof SQLString)
	            {
	                SQLString sStr = (SQLString)anns[0];
	                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
	                columnDefs.add(columnName);
	            }else
	            if(anns[0] instanceof SQLFloat)
	            {
	                SQLFloat sStr = (SQLFloat) anns[0];
	                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
	                columnDefs.add(columnName);
	            }else
	            if(anns[0] instanceof SQLDateTime)
	            {
	                SQLDateTime sStr = (SQLDateTime) anns[0];
	                columnName = (sStr.name().length()<1)?field.getName().toUpperCase():sStr.name();
	                columnDefs.add(columnName);
	            }else
	            if(anns[0] instanceof QualifiWhere)
	            {
	            	term=true;
	                QualifiWhere sStr = (QualifiWhere) anns[0];
	                columnWhere = sStr.where();
	             }
	        }
	        
	        StringBuilder selectCommand = new StringBuilder("SELECT ");
	                
	        for(String columnDef :columnDefs){
	            selectCommand.append("\n    "+columnDef+",");
	        }
	        
	        String select=selectCommand.substring(0,selectCommand.length()-1)+"\n FROM \n   "+tableName;
	        
	        if(term){
	          StringBuilder whereCommand = new StringBuilder();
	          int i=1;
	          for(String whereterm:columnWhere){
	        	  if(i%2==0){
	        		//  System.out.println("偶数");
	        		  whereCommand.append(whereterm+"\n  AND ");
	        	   }else{
	        		//  System.out.println("奇数");
	        		  whereCommand.append("\n   "+whereterm+"=");
	        	   }
	           i++;
	          }
	          select=select+"\n  WHERE "+whereCommand.substring(0,whereCommand.length()-7);
	        }
	              
	        return select;
	   }
}
