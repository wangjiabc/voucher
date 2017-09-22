package com.voucher.manage.daoImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.voucher.manage.dao.RoomInfoDao;
import com.voucher.manage.daoModel.RoomChangeHireLog;
import com.voucher.manage.daoModel.RoomChartLog;
import com.voucher.manage.daoModel.RoomInfo;
import com.voucher.manage.daoModel.RoomInfoRowMapper;
import com.voucher.manage.daoModelJoin.RoomChangeHireLog_RoomChartLog;
import com.voucher.manage.daoRowMapper.RowMappers;
import com.voucher.manage.daoRowMapper.RowMappersJoin;
import com.voucher.manage.daoSQL.SelectSQL;
import com.voucher.manage.daoSQL.SelectSQLJoin;
import com.voucher.manage.tools.MyTestUtil;

public class RoomInfoDaoImpl extends JdbcDaoSupport implements RoomInfoDao{

	
	@Override
	public List<RoomInfo> findAllRoomInfo(Integer limit, Integer offset, String sort,
			String order,String search) {
		// TODO Auto-generated method stub
		/*
		String sql="SELECT top "+ limit+" [GUID],[Num],[OriginalNum],[Address],[OriginalAddress],[Region],[Segment],[ManageRegion]"+
                    ",[RoomProperty],[Useful],[Floor],[State],[Structure],[BuildArea],[RoomType],[IsCity],[Manager],[ManagerPhone]"+
                    ",[IsStreet],[FitMent],[BeFrom],[InDate],[PropertyRightNo],[PropertyRightArea],[DesignUseful],[BuildYear],[PropertyRightUnit]"+
                    ",[RealPropertyRightUnit],[PropertyCardUnit],[GlebeCardUnit],[TransferUnit],[GlebeCardNo],[GlebeUseType],[GlebeEndDate]"+
                    ",[GlebeTypeUseful],[PlanUseful],[BefromFile],[NoPRNResion],[NoGCResion],[RealEstateNo],[PropertyMemo]"+
                    ",[OriginalAmount],[AllDepreciation],[DepreciationYear],[NetWorth],[EvaluationPrice],[EvaluationSinglePrice]"+
                    ",[EvaluationPlace],[BefromAmount],[MarketHire],[EvaluationUnit],[EvaluationNo],[IsPawn],[PawnUnit],[OriginalUnit]"+
                    ",[FinanceMemo],[Utility],[ChartGUID],[AddressCode],[OriginalAddressCode],[SecurityClassification],[DangerClassification]"+
                    ",[HiddenDanger],[ResponsiblePerson],[sMemo],[BelongUnit],[FileIndex],[SecurityRegion],[RoomCount],[LandArea],[UseYears]"+
                    "FROM [TTT].[dbo].[RoomInfo]"+
                    "where [GUID]"+
                    "not in("+
                    "select top "+offset+"[GUID] FROM [TTT].[dbo].[RoomInfo])";
		*/
		
		
		RoomInfo roomInfo=new RoomInfo();
		roomInfo.setLimit(limit);
		roomInfo.setOffset(offset);
		roomInfo.setNotIn("[GUID]");
		if(search!=null&&!search.trim().equals("")){
		  String[] where={"Address like ",search};
		  roomInfo.setWhereTerm("OR");
		  roomInfo.setWhere(where);
		}
		System.out.println("impl sort="+sort+ "   order="+order);

		if(sort!=null)
			roomInfo.setSort(sort);
		if(order!=null)
			roomInfo.setOrder(order);
		

        String sql="";
        Map<String,Object> map=new HashMap<>();
		try {
			map = SelectSQL.get(roomInfo);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql=(String) map.get("sql");
		List params=(List) map.get("params");
		
		//return this.getJdbcTemplate().query(sql, new RoomInfoRowMapper());
		List list= this.getJdbcTemplate().query(sql,params.toArray(),new RowMappers(RoomInfo.class));
		MyTestUtil.print(map);
		MyTestUtil.print(list);
		return list;
	}

	@Override
	public Integer getRoomInfoCount(String search) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		//String sql="SELECT count(*) FROM [TTT].[dbo].[RoomInfo]";
		
		RoomInfo roomInfo=new RoomInfo();
		if(search!=null&&!search.trim().equals("")){
			String[] where={"Address like ",search};
			  roomInfo.setWhereTerm("OR");
			  roomInfo.setWhere(where);
			}
		 String sql="";
	        Map<String,Object> map2=new HashMap<>();
		try {
			map2 = SelectSQL.getCount(roomInfo);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql=(String) map2.get("sql");
		System.out.println(sql);
		List params=(List) map2.get("params");
		map=this.getJdbcTemplate().queryForMap(sql,params.toArray());
	
		/*
		Set  set=map.entrySet();       	    
		Iterator iterator=set.iterator();       		    
		while (iterator.hasNext()){       		    
		    Map.Entry  mapentry = (Map.Entry) iterator.next();       		    
		    System.out.println(mapentry.getKey()+"/"+ mapentry.getValue());       		    
		}   
		*/
		
		return (Integer) map.get("");
	}

	@Override
	public Map<String, Object> findAllChangehire_CharLog(Integer limit, Integer offset, String sort, String order,
			String search) {
		// TODO Auto-generated method stub
		
		RoomChangeHireLog roomChangeHireLog=new RoomChangeHireLog();
		RoomChartLog roomChartLog=new RoomChartLog();
		
		roomChangeHireLog.setLimit(limit);
		roomChangeHireLog.setOffset(offset);
		roomChangeHireLog.setSort(sort);
		roomChangeHireLog.setOrder(order);
		roomChangeHireLog.setNotIn("[GUID]");
		roomChangeHireLog.setWhereTerm("OR");
		
		roomChartLog.setLimit(limit);
		roomChartLog.setOffset(offset);
		roomChartLog.setSort(sort);
		roomChartLog.setOrder(order);
		roomChartLog.setNotIn("[Charter]");
		roomChartLog.setWhereTerm("OR");
		
		System.out.println("srot="+sort);
		
		if(search!=null&&!search.trim().equals("")){
		 String[] where={"[RoomManage].[dbo].[RoomChangeHireLog].Charter LIKE",search,
				"[RoomManage].[dbo].[RoomChangeHireLog].Region LIKE",search};
		 roomChangeHireLog.setWhere(where);
		}
		
		
		
		Object[] objects={roomChangeHireLog,roomChartLog};
		Class<?>[] classes={roomChangeHireLog.getClass(),roomChartLog.getClass()};
		Map map2=null;
		try {
			map2=new SelectSQLJoin().get(objects,"[Charter]");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List params=(List) map2.get("params");
		String sql=(String) map2.get("sql");
		
		System.out.println("params="+params);
		
		List list=this.getJdbcTemplate().query(sql,params.toArray(), new RowMappersJoin(classes, RoomChangeHireLog_RoomChartLog.class));
		
		Map map=new HashMap<String, Object>();
		
		map.put("value", list);
		
		try {
			map2=new SelectSQLJoin().getCount(objects, "[Charter]")	;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		params=(List) map2.get("params");
		sql=(String) map2.get("sql");
		
		Map map3=this.getJdbcTemplate().queryForMap(sql,params.toArray());
		
		map.put("rows", map3.get(""));
		
		return map;
	}

}
