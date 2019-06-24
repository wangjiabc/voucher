package com.voucher.manage.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.MapMaker;
import com.voucher.manage.dao.FinanceDAO;
import com.voucher.manage.daoImpl.AssetsDAOImpl.allHire;
import com.voucher.manage.daoModel.TTT.ChartInfo;
import com.voucher.manage.daoModel.TTT.ChartInfo2;
import com.voucher.manage.daoModel.TTT.HireList;
import com.voucher.manage.daoModel.TTT.HirePay;
import com.voucher.manage.daoModel.TTT.User_AccessTime;
import com.voucher.manage.daoModelJoin.Finance.HireList_ChartInfo_Join;
import com.voucher.manage.daoRowMapper.RowMappers;
import com.voucher.manage.daoSQL.InsertExe;
import com.voucher.manage.daoSQL.SelectExe;
import com.voucher.manage.daoSQL.SelectJoinExe2;
import com.voucher.manage.daoSQL.SelectSQL;
import com.voucher.manage.daoSQL.UpdateExe;
import com.voucher.manage.model.Users;
import com.voucher.manage.singleton.Singleton;
import com.voucher.manage.tools.MyTestUtil;
import com.voucher.manage.tools.TransMapToString;

import voucher.Mybatis;

public class FinanceDAOImpl extends JdbcDaoSupport implements FinanceDAO{

	@Override
	public Map findMatureHire(Integer days,Integer limit, Integer offset, String sort, String order, Map<String, String> search) {
		// TODO Auto-generated method stub
		
		String startTime = null;
		
		String endTime = null;
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();  
		
		startTime=sdf.format(cal.getTime());
		
        cal.add(Calendar.DAY_OF_MONTH, days);
        
		endTime=sdf.format(cal.getTime());
        
        
        System.out.println("startTime="+startTime+"   endTime="+endTime);
        
        String[] where={"convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )>",startTime,
				"convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )<",endTime,
				"State=","未交"};
        
		if(search!=null&&!search.isEmpty()){
        	
			search.put("convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )>", startTime);
			search.put("convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )<", endTime);
			search.put("State=","未交");
			
        	StringBuilder sb = new StringBuilder();
        	
        	where=TransMapToString.get(search);
        	
		}
		
		HireList hireList=new HireList();
		hireList.setLimit(limit);
		hireList.setOffset(offset);
		hireList.setNotIn("GUID");
		hireList.setWhere(where);
		
		ChartInfo chartInfo=new ChartInfo();
		chartInfo.setLimit(limit);
		chartInfo.setOffset(offset);
		chartInfo.setNotIn("GUID");
		chartInfo.setWhere(where);
		
		if(sort!=null&&!sort.equals("")){
			hireList.setSort(sort);
			hireList.setOrder(order);
		}
		
		if(order!=null&&order.equals("")){
			chartInfo.setSort(sort);
			chartInfo.setOrder(order);
		}
				
		HireList_ChartInfo_Join hireList_ChartInfo_Join=new HireList_ChartInfo_Join();
		
		Object[] objects={chartInfo,hireList};
		
		String[][] join={{"GUID","ChartGUID"}};
		
		List list=SelectJoinExe2.get(this.getJdbcTemplate(), objects, hireList_ChartInfo_Join, join);
		
		int total=(int) SelectJoinExe2.getCount(this.getJdbcTemplate(), objects, join).get("");
		
		String sql2="SELECT sum([HireList].[Hire]) as allHire,count(*) as count from HireList left join ChartInfo "
				+ "on [HireList].ChartGUID=[ChartInfo].GUID where "
				+ "convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )>'"+startTime+"' and "
				+ "convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )<'"+endTime+"' and "
				+ "State='未交'";
		
		List list2=this.getJdbcTemplate().query(sql2,new allHire());
		
		Map HireMap=(Map) list2.get(0);
		
		Double allHire=(Double) HireMap.get("allHire")/10000;	
		int  count=(int) HireMap.get("count");
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("######0.00");   			
				
		Map map=new HashMap<>();
		
		map.put("rows", list);
		
		map.put("total", total);
		
		map.put("count", count);
		
		map.put("allHire", df.format(allHire)+"万元");
		
		map.put("matureTime", startTime+" 至  "+endTime);
		
		return map;
	}

	
	class allHire implements RowMapper<Map> {

		@Override
		public Map mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Double allHire=rs.getDouble("allHire");
			
			Integer count=rs.getInt("count");
			
			Map map=new HashMap<>();
			
			map.put("allHire", allHire);
			
			map.put("count", count);
			
			return map;
		}
		
	}

	
	@Override
	public Integer findMatureHireClew(String openId, Integer days) {
		// TODO Auto-generated method stub
		String startTime = null;
		
		String endTime = null;
		
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();  
		
		startTime=sdf.format(cal.getTime());
		
        cal.add(Calendar.DAY_OF_MONTH, days);
        
		endTime=sdf.format(cal.getTime());
		
		String sql="SELECT sum([HireList].[Hire]) as allHire,count(*) as count from HireList left join ChartInfo "
				+ "on [HireList].ChartGUID=[ChartInfo].GUID where "
				+ "convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )>'"+startTime+"' and "
				+ "convert(varchar(11),[HireList].HireDate ,120 )+'-'+ convert(varchar(2),[ChartInfo].[ChartEndDate] ,103 )<'"+endTime+"' and "
				+ "State='未交'";
		
		List list=this.getJdbcTemplate().query(sql,new allHire());
		
		Map HireMap=(Map) list.get(0);
		
		int  count=(int) HireMap.get("count");
		
		User_AccessTime user_AccessTime=new User_AccessTime();

		String[] where={"open_id=",openId};
		
		user_AccessTime.setLimit(1);
		user_AccessTime.setOffset(0);
		user_AccessTime.setNotIn("open_id");
		user_AccessTime.setWhere(where);
		
		String matureDate=null;
		
		try{
			List list2=SelectExe.get(this.getJdbcTemplate(), user_AccessTime);
			
			MyTestUtil.print(list2);
			
			user_AccessTime=(User_AccessTime) list2.get(0);
			
			MyTestUtil.print(user_AccessTime);
			
			Date matureChartInfo=user_AccessTime.getMatureChartInfo();
			matureDate=sdf.format(matureChartInfo);
			
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		System.out.println("matureDate="+matureDate+"  startTime="+startTime);
		System.out.println(count);
		if(matureDate!=null&&matureDate.equals(startTime)){
			return 0;
		}
		
		return count;
	}
	
	
	@Override
	public Map findGeneralChartInfo(Integer limit, Integer offset, String sort, String order, Integer isHistory,
			Map<String, String> search) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM");
		
        String matureTime = null;
		
        matureTime=sdf.format(cal.getTime());
		
        System.out.println("matureTime="+matureTime);
        
        String sql = null;
        
        String sql2 = null;
        
        if(!search.isEmpty()){
        	
        	StringBuilder sb = new StringBuilder();
        	
        	String[] where=TransMapToString.get(search);
        	
        	int i=0;
			
        	for(String str : where){
			    
			    if(i%2==0){
			    	sb.append(str);
			    }else{
			    	sb.append("'"+str+"'");
			    	sb.append(" or ");
			    }
			    i++;
			    
			   System.out.println("sb="+sb);
			}
        	
        	String s = sb.toString();
			
			String serach=s.substring(0,s.length()-4);
			
			System.out.println("serach="+serach);
        	
			sql=" select top "+limit+" * from "+
					"(select ROW_NUMBER() OVER (ORDER BY "+sort+" "+order+") AS rows ,* from "+
					"ChartInfo left join (	"+
					 "select ChartGUID from [HireList] "+
        			"where [HireList].ChartGUID not in(	"+        			
        			 "select ChartGUID from [HireList] "+
        			"where [HireList].State='未交'  and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+
        			"[HireList].ChartGUID )group by  chartGuid "+
        			") as t2 on ChartInfo.GUID=t2.ChartGUID	"+
        			"where ChartGUID is not null "+
        			")as w1 "+
        			"where rows>"+offset+" and "+
        			"IsHistory="+isHistory+" "+
        			"order by "+sort+" "+order;
		
        	sql2="select count(*) from "+
					"ChartInfo left join (	"+
					 "select ChartGUID from [HireList] "+
					 "where [HireList].ChartGUID not in(	"+        			
					 "select ChartGUID from [HireList] "+
					 "where [HireList].State='未交'  and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+
					 "[HireList].ChartGUID )group by  chartGuid "+
					 ") as t2 on ChartInfo.GUID=t2.ChartGUID	"+
					 "where ChartGUID is not null  and "+
					 "IsHistory="+isHistory;
			
        }else{
        
        	sql=" select top "+limit+" * from "+
					"(select ROW_NUMBER() OVER (ORDER BY "+sort+" "+order+") AS rows ,* from "+
					"ChartInfo left join (	"+
					 "select ChartGUID from [HireList] "+
        			"where [HireList].ChartGUID not in(	"+        			
        			 "select ChartGUID from [HireList] "+
        			"where [HireList].State='未交'  and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+
        			"[HireList].ChartGUID )group by  chartGuid "+
        			") as t2 on ChartInfo.GUID=t2.ChartGUID	"+
        			"where ChartGUID is not null "+
        			")as w1 "+
        			"where rows>"+offset+" and "+
        			"IsHistory="+isHistory+" "+
        			"order by "+sort+" "+order;
		
        	sql2="select count(*) from "+
					"ChartInfo left join (	"+
					 "select ChartGUID from [HireList] "+
					 "where [HireList].ChartGUID not in(	"+        			
					 "select ChartGUID from [HireList] "+
					 "where [HireList].State='未交'  and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+
					 "[HireList].ChartGUID )group by  chartGuid "+
					 ") as t2 on ChartInfo.GUID=t2.ChartGUID	"+
					 "where ChartGUID is not null and "+
					 "IsHistory="+isHistory;
        	
        }
        
        System.out.println("sql="+sql);
        
		ChartInfo chartInfo=new ChartInfo();
		
		List list=this.getJdbcTemplate().query(sql, new RowMappers(chartInfo.getClass()));
		
		int total=(int) this.getJdbcTemplate().queryForMap(sql2).get("");
		
		Map map=new HashMap<>();
		
		map.put("rows", list);
		
		map.put("total", total);
		
		return map;
	}
	

	@Override
	public Map findOverdueHire(Integer limit, Integer offset, String sort, String order, Map<String, String> search) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map findOverdueChartInfo(Integer limit, Integer offset, String sort, String order,
			Integer isHistory,Map<String, String> search) {
		// TODO Auto-generated method stub
		
		Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM");
		
        String matureTime = null;
		
        matureTime=sdf.format(cal.getTime());
		
        System.out.println("matureTime="+matureTime);
        
        String sql = null;
        
        String sql2 = null;
        
        if(!search.isEmpty()){
        	
        	StringBuilder sb = new StringBuilder();
        	
        	String[] where=TransMapToString.get(search);
        	
        	int i=0;
			
        	for(String str : where){
			    
			    if(i%2==0){
			    	sb.append(str);
			    }else{
			    	sb.append("'"+str+"'");
			    	sb.append(" or ");
			    }
			    i++;
			    
			   System.out.println("sb="+sb);
			}
        	
        	String s = sb.toString();
			
			String serach=s.substring(0,s.length()-4);
			
			System.out.println("serach="+serach);
        	
			sql="select top "+limit+" * from "+
					"(select ROW_NUMBER() OVER (ORDER BY "+sort+" "+order+") AS rows ,* from "+
					"ChartInfo left join ( "+
					"select ChartGUID,COUNT(*)as c from [HireList] "+
					"where [HireList].State='未交' and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+ 
					"[HireList].ChartGUID ) t2 on ChartInfo.GUID=t2.ChartGUID where "+
					"ChartGUID is not null "+
					") as w1 "+
					"where rows>"+offset+" and ("+serach+") and "+
					"IsHistory="+isHistory+" and "+
					"ChartGUID is not null order by "+sort+" "+order;
		
        	sql2="select COUNT(*) from "+
        			"ChartInfo left join ( "+
        			"select ChartGUID,COUNT(*)as c from [HireList] "+
        			"where [HireList].State='未交' and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+
        			"[HireList].ChartGUID ) t2 on ChartInfo.GUID=t2.ChartGUID where "+
        			"("+serach+") and "+
        			"IsHistory="+isHistory+" and "+
        			"ChartGUID is not null ";
			
        }else{
        
        	sql="select top "+limit+" * from "+
					"(select ROW_NUMBER() OVER (ORDER BY "+sort+" "+order+") AS rows ,* from "+
					"ChartInfo left join ( "+
					"select ChartGUID,COUNT(*)as c from [HireList] "+
					"where [HireList].State='未交' and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+ 
					"[HireList].ChartGUID ) t2 on ChartInfo.GUID=t2.ChartGUID where "+
					"ChartGUID is not null "+
					") as w1 "+
					"where rows>"+offset+" and "+
					"IsHistory="+isHistory+" and "+
					"ChartGUID is not null order by "+sort+" "+order;
		
        	sql2="select COUNT(*) from "+
        			"ChartInfo left join ( "+
        			"select ChartGUID,COUNT(*)as c from [HireList] "+
        			"where [HireList].State='未交' and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+
        			"[HireList].ChartGUID ) t2 on ChartInfo.GUID=t2.ChartGUID where "+
        			"IsHistory="+isHistory+" and "+
        			"ChartGUID is not null ";
        }
        
		ChartInfo2 chartInfo=new ChartInfo2();
		
		List list=this.getJdbcTemplate().query(sql, new RowMappers(chartInfo.getClass()));
		
		int total=(int) this.getJdbcTemplate().queryForMap(sql2).get("");
		
		Map map=new HashMap<>();
		
		map.put("rows", list);
		
		map.put("total", total);
		
		return map;
	}

	

	@Override
	public Integer findOverdueChartInfoClew(String openId) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM");
		
        String matureTime = null;
		
        matureTime=sdf.format(cal.getTime());
        
        User_AccessTime user_AccessTime=new User_AccessTime();

		String[] where={"open_id=",openId};
		
		user_AccessTime.setLimit(1);
		user_AccessTime.setOffset(0);
		user_AccessTime.setNotIn("open_id");
		user_AccessTime.setWhere(where);
		
		String matureDate=null;
		
		try{
			List list2=SelectExe.get(this.getJdbcTemplate(), user_AccessTime);
			user_AccessTime=(User_AccessTime) list2.get(0);
			Date matureChartInfo=user_AccessTime.getOverdueChartInfo();
			matureDate=sdf.format(matureChartInfo);
						
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
		String sql = null;
		
		if(matureDate==null||matureDate.equals("")){
			sql="select COUNT(*) from "+
    			"ChartInfo left join ( "+
    			"select ChartGUID,COUNT(*)as c from [HireList] "+
    			"where [HireList].State='未交' and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' group by "+
    			"[HireList].ChartGUID ) t2 on ChartInfo.GUID=t2.ChartGUID where "+
    			"IsHistory=0 and "+
    			"ChartGUID is not null ";
		}else{
			sql="select COUNT(*) from "+
	    			"ChartInfo left join ( "+
	    			"select ChartGUID,COUNT(*)as c from [HireList] "+
	    			"where [HireList].State='未交' and convert(varchar(11),[HireList].HireDate ,120 )<'"+matureTime+"' "+
	    			"and convert(varchar(11),[HireList].HireDate ,120 )>'"+matureDate+"' group by "+
	    			"[HireList].ChartGUID ) t2 on ChartInfo.GUID=t2.ChartGUID where "+
	    			"IsHistory=0 and "+
	    			"ChartGUID is not null ";
		}
		
        int count=(int) this.getJdbcTemplate().queryForMap(sql).get("");
        
		return count;
	}

	@Override
	public Map findAllChartInfo(Integer limit, Integer offset, String sort, String order, Map<String, String> search) {
		// TODO Auto-generated method stub
		
		ChartInfo chartInfo=new ChartInfo();
		
		chartInfo.setLimit(limit);
		chartInfo.setOffset(offset);
		chartInfo.setSort(sort);
		chartInfo.setOrder(order);
		chartInfo.setNotIn("GUID");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    chartInfo.setWhereTerm("OR");
		    chartInfo.setWhere(where);
		}
		
		List list=SelectExe.get(this.getJdbcTemplate(), chartInfo);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), chartInfo).get("");
		
		Map map=new HashMap<>();
		
		map.put("rows", list);
		
		map.put("total", total);
		
		return map;
	}


	@Override
	public Map<String, Object> getHireListByGUID(Integer limit, Integer offset, String sort, String order, Map search) {
		// TODO Auto-generated method stub
		HireList hireList=new HireList();
		
		hireList.setLimit(limit);
		hireList.setOffset(offset);
		hireList.setSort(sort);
		hireList.setOrder(order);
		hireList.setOffset(offset);
		hireList.setNotIn("GUID");
				
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    hireList.setWhere(where);
		}
		
		Map map=new HashMap<String, Object>();
		
		List list=SelectExe.get(this.getJdbcTemplate(), hireList);
		map.put("rows", list);
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), hireList).get("");
		map.put("total", total);
		
		return map;
	}


	@Override
	public Integer updateHireSetHireList(Users users,List files) {
		// TODO Auto-generated method stub
		
		Date date = new Date();
		
		String openId=users.getOpenId();
		String name=users.getName();
		
		float amount = 0;
		
		String printMemo = "";
		
		String chartGUID = null;
		
		String hireGUID = null;
		
		String payGUID=UUID.randomUUID().toString();
		
		int i=0;
		
		if(!files.isEmpty()){
            try {
            	int u=0;
            	Iterator<String> iterator=files.iterator();
            	
                while (iterator.hasNext()){
                        String guid=iterator.next();

                    	String[] where={"GUID=",guid};
                    	
                    	date=new Date();
                    	
                    	HireList hireList=new HireList();
                    	hireList.setState("已交");
                    	hireList.setOptDate(date);
                    	hireList.setOperator(name);
                    	hireList.setOpen_id(openId);
                    	hireList.setWhere(where);                    	
                    	hireList.setPayGUID(payGUID);
                    	
                    	u=UpdateExe.get(this.getJdbcTemplate(), hireList);
                    	
                    	if(u==0){
                    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    		return 0;
                    	}
                    	
                    	hireList.setLimit(1);
                    	hireList.setOffset(0);
                    	hireList.setNotIn("GUID");
                    	
                    	HireList hireList2=(HireList) SelectExe.get(this.getJdbcTemplate(), hireList).get(0);
                    	
                    	amount=amount+hireList2.getHire();  
                    	if(i==(files.size()-1)){
                    		printMemo=printMemo+hireList2.getHireDate(); 
                    	}else{
                    		printMemo=printMemo+hireList2.getHireDate()+","; 
                    	}
                    	chartGUID=hireList2.getChartGUID();                    	
                    	hireGUID=hireList2.getHireGUID();
                    	
                    	i++;
                }


                HirePay hirePay=new HirePay();
                
                hirePay.setGUID(payGUID);
                hirePay.setAmount(amount);
                hirePay.setOperator(name);
                hirePay.setOpen_id(openId);
                hirePay.setPrintMemo(printMemo);
                hirePay.setChartGUID(chartGUID);
                hirePay.setHireGUID(hireGUID);
                hirePay.setOptDate(date);
                hirePay.setPrintCount((float) 0);
                
                u=InsertExe.get(this.getJdbcTemplate(), hirePay);
                
                if(u==0){
            		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            		return 0;
            	}
                
                return u;
               
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 0;
            }
        }else {
        	
        	return 2;
        	
        }
		
	}


	@Override
	public Integer updateHireSetHireListWinXinPay(List files) {
		// TODO Auto-generated method stub
		Date date = new Date();
				
		float amount = 0;
		
		String printMemo = "";
		
		String chartGUID = null;
		
		String hireGUID = null;
		
		String payGUID=UUID.randomUUID().toString();
		
		int i=0;
		
		if(!files.isEmpty()){
            try {
            	int u=0;
            	Iterator<String> iterator=files.iterator();
            	
                while (iterator.hasNext()){
                        String guid=iterator.next();

                    	String[] where={"GUID=",guid};
                    	
                    	date=new Date();
                    	
                    	HireList hireList=new HireList();
                    	hireList.setState("已交");
                    	hireList.setOptDate(date);
                    	hireList.setOperator("微信支付");
                    	hireList.setWhere(where);                    	
                    	hireList.setPayGUID(payGUID);
                    	
                    	u=UpdateExe.get(this.getJdbcTemplate(), hireList);
                    	
                    	if(u==0){
                    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    		return 0;
                    	}
                    	
                    	hireList.setLimit(1);
                    	hireList.setOffset(0);
                    	hireList.setNotIn("GUID");
                    	
                    	HireList hireList2=(HireList) SelectExe.get(this.getJdbcTemplate(), hireList).get(0);
                    	
                    	amount=amount+hireList2.getHire();  
                    	if(i==(files.size()-1)){
                    		printMemo=printMemo+hireList2.getHireDate(); 
                    	}else{
                    		printMemo=printMemo+hireList2.getHireDate()+","; 
                    	}
                    	chartGUID=hireList2.getChartGUID();                    	
                    	hireGUID=hireList2.getHireGUID();
                    	
                    	i++;
                }


                HirePay hirePay=new HirePay();
                
                hirePay.setGUID(payGUID);
                hirePay.setAmount(amount);
                hirePay.setOperator("微信支付");
                hirePay.setPrintMemo(printMemo);
                hirePay.setChartGUID(chartGUID);
                hirePay.setHireGUID(hireGUID);
                hirePay.setOptDate(date);
                hirePay.setPrintCount((float) 0);
                
                u=InsertExe.get(this.getJdbcTemplate(), hirePay);
                
                if(u==0){
            		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            		return 0;
            	}
                
                return u;
               
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 0;
            }
        }else {
        	
        	return -2;
        	
        }
	}

			
	
}
