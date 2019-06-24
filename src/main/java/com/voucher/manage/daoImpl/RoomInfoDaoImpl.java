package com.voucher.manage.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.voucher.manage.dao.RoomInfoDao;
import com.voucher.manage.daoImpl.AssetsDAOImpl.hidden_AssetsRowMapper;
import com.voucher.manage.daoModel.Floor;
import com.voucher.manage.daoModel.RoomChangeHireLog;
import com.voucher.manage.daoModel.RoomChartLog;
import com.voucher.manage.daoModel.RoomInfo;
import com.voucher.manage.daoModel.RoomInfoRowMapper;
import com.voucher.manage.daoModel.Assets.Hidden_Assets;
import com.voucher.manage.daoModel.Assets.Position;
import com.voucher.manage.daoModel.TTT.ChartInfo;
import com.voucher.manage.daoModel.TTT.FileSelfBelong;
import com.voucher.manage.daoModel.TTT.HireList;
import com.voucher.manage.daoModelJoin.RoomChangeHireLog_RoomChartLog;
import com.voucher.manage.daoModelJoin.RoomInfo_Position;
import com.voucher.manage.daoRowMapper.RowMappers;
import com.voucher.manage.daoRowMapper.RowMappersJoin;
import com.voucher.manage.daoSQL.DeleteExe;
import com.voucher.manage.daoSQL.InsertExe;
import com.voucher.manage.daoSQL.SelectExe;
import com.voucher.manage.daoSQL.SelectJoinExe;
import com.voucher.manage.daoSQL.SelectSQL;
import com.voucher.manage.daoSQL.SelectSQLJoin;
import com.voucher.manage.daoSQL.SelectSqlJoinExe;
import com.voucher.manage.daoSQL.UpdateExe;
import com.voucher.manage.singleton.Singleton;
import com.voucher.manage.tools.MyTestUtil;
import com.voucher.manage.tools.TransMapToString;

import voucher.UpdateSql;

import com.voucher.manage.singleton.Singleton;

public class RoomInfoDaoImpl extends JdbcDaoSupport implements RoomInfoDao{
	
	@Override
	public List<RoomInfo> findAllRoomInfo(Integer limit, Integer offset, String sort,
			String order,Map search) {
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
                    "FROM ROOMDATABASE"+".[dbo].[RoomInfo]"+
                    "where [GUID]"+
                    "not in("+
                    "select top "+offset+"[GUID] FROM ROOMDATABASE"+".[dbo].[RoomInfo])";
		*/
		
		
		RoomInfo roomInfo=new RoomInfo();
		roomInfo.setLimit(limit);
		roomInfo.setOffset(offset);
		roomInfo.setNotIn("[GUID]");
		/*
		if(search!=null&&!search.trim().equals("")){
		  String[] where={"Address like ",search};
		  roomInfo.setWhereTerm("OR");
		  roomInfo.setWhere(where);
		}
		*/
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    roomInfo.setWhere(where);
		}
		
		System.out.println("impl sort="+sort+ "   order="+order);

		if(sort!=null)
			roomInfo.setSort(sort);
		if(order!=null)
			roomInfo.setOrder(order);
		
       /*
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
		*/
		
		List list=SelectExe.get(this.getJdbcTemplate(), roomInfo);
	
		MyTestUtil.print(list);
	
		return list;
	}

	@Override
	public Integer getRoomInfoCount(Map search) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		//String sql="SELECT count(*) FROM ROOMDATABASE"+".[dbo].[RoomInfo]";
		
		RoomInfo roomInfo=new RoomInfo();
		/*
		if(search!=null&&!search.trim().equals("")){
			String[] where={"Address like ",search};
			  roomInfo.setWhereTerm("OR");
			  roomInfo.setWhere(where);
			}
			*/
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
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
	public List<RoomInfo_Position> findAllRoomInfo_Position(Integer limit, Integer offset, String sort, String order,
			Map search) {
		// TODO Auto-generated method stub

		RoomInfo roomInfo=new RoomInfo();
		roomInfo.setLimit(limit);
		roomInfo.setOffset(offset);
		roomInfo.setNotIn("[GUID]");
		
		Position position=new Position();
		position.setLimit(limit);
		position.setOffset(offset);
		position.setNotIn("[GUID]");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    roomInfo.setWhere(where);
		    position.setWhere(where);
		}
		
		RoomInfo_Position roomInfo_Position=new RoomInfo_Position();
		
		Object[] objects={roomInfo,position};
		
		String[] join={"[GUID]"};
		
		List list=SelectJoinExe.get(this.getJdbcTemplate(), objects,roomInfo_Position,join);
		
		return list;
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
		/*
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
		*/
		
		Map map=new HashMap<String, Object>();
		
		RoomChangeHireLog_RoomChartLog roomChangeHireLog_RoomChartLog=new RoomChangeHireLog_RoomChartLog();
		
		String[] join={"[Charter]"};
		
		List list=SelectJoinExe.get(this.getJdbcTemplate(), objects, roomChangeHireLog_RoomChartLog, join);
		map.put("value", list);
		
		Map map3=SelectJoinExe.getCount(this.getJdbcTemplate(), objects, join);
		map.put("rows", map3.get(""));
		
		return map;
	}

	@Override
	public Map<String, Object> findAllFloor(Integer limit, Integer offset, String sort, String order, String search) {
		// TODO Auto-generated method stub
		Floor floor=new Floor();
		floor.setLimit(limit);
		floor.setOffset(offset);
		floor.setSort(sort);
		floor.setOrder(order);
		
		Map<String,Object> map=new HashMap<>();
	        Map<String,Object> map2=new HashMap<>();
			try {
				map2 = SelectSQL.get(floor);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			List params=(List) map2.get("params");
			String sql=(String) map2.get("sql");
			
			List list=this.getJdbcTemplate().query(sql,params.toArray(), new RowMappers(Floor.class));
			
			map.put("value", list);
			
			try {
				map2=new SelectSQL().getCount(floor);
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

	@Override
	public Integer updateRoomInfo(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		int i=UpdateExe.get(this.getJdbcTemplate(), roomInfo);
		return i;
	}

	@Override
	public Integer deleteRoomInfo(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		int i=DeleteExe.get(this.getJdbcTemplate(), roomInfo);
		return i;
	}


	@Override
	public Integer insertIntoFileSelfBelong(String RoomGUID, String UpFileFullName, String FileType,
			String FileBelong) {
		// TODO Auto-generated method stub
		FileSelfBelong fileSelfBelong=new FileSelfBelong();
		
		Date date=new Date();
		
		String GUID=UUID.randomUUID().toString();
		
		fileSelfBelong.setGUID(GUID);
		fileSelfBelong.setRoomGUID(RoomGUID);
		fileSelfBelong.setUpFileFullName(UpFileFullName);
		fileSelfBelong.setFileType(FileType);
		fileSelfBelong.setFileBelong("房屋图片");
		fileSelfBelong.setSequence(0);
		fileSelfBelong.setDate_time(date);
		
		String[] where={"[FileSelfBelong].RoomGUID=",RoomGUID,
				"[FileSelfBelong].FileBelong=","房屋图片"};
		
		fileSelfBelong.setWhere(where);
		
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), fileSelfBelong).get("");
		
		fileSelfBelong.setFileIndex(count+1);
		
		return InsertExe.get(this.getJdbcTemplate(), fileSelfBelong);
	}
	
	@Override
	public Map<String, Object> getChartInfoByGUID(Integer limit, Integer offset, String sort, String order,
			Map search) {
		// TODO Auto-generated method stub
		ChartInfo chartInfo=new ChartInfo();
		
		if(sort==null){
			sort="ConcludeDate";
		}
		
		if(order==null){
			order="desc";
		}
		
		chartInfo.setLimit(limit);
		chartInfo.setOffset(offset);
		chartInfo.setSort(sort);
		chartInfo.setOffset(offset);
		chartInfo.setNotIn("GUID");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    chartInfo.setWhere(where);
		}
		
		Map map=new HashMap<String, Object>();
		
		List list=SelectExe.get(this.getJdbcTemplate(), chartInfo);
		map.put("rows", list);
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), chartInfo).get("");
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
		hireList.setOffset(offset);
		hireList.setNotIn("GUID");
		
		if(sort==null){
			sort="State";
		}
		
		if(order==null){
			order="desc";
		}
		
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
	public Double getAllTotalHire() {
		// TODO Auto-generated method stub
		String sql="SELECT SUM(TotalHire) as Hire "+
					"FROM "+Singleton.ROOMDATABASE+".[dbo].[ChartInfo] ";
		
		Double totalHire=this.getJdbcTemplate().query(sql, new Hire()).get(0);
		
		return totalHire;
	}

	
	@Override
	public Double getAlreadyHire() {
		// TODO Auto-generated method stub
		String sql="SELECT SUM(Hire) as Hire "+
					"FROM "+Singleton.ROOMDATABASE+".[dbo].[HireList] where State='未交'";
	
		Double alreadyHire=this.getJdbcTemplate().query(sql, new Hire()).get(0);
	
		return alreadyHire;
	}

	@Override
	public Double getNotHire() {
		// TODO Auto-generated method stub
		String sql="SELECT SUM(Hire) as Hire "+
				"FROM "+Singleton.ROOMDATABASE+".[dbo].[HireList] where State='已交'";
	
		Double notHire=this.getJdbcTemplate().query(sql, new Hire()).get(0);
	
		return notHire;
	}

	
	class Hire implements RowMapper<Double> {

		@Override
		public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Double Hire=rs.getDouble("Hire");
			
			return Hire;
		}
		
	}

	class Year implements RowMapper<String> {

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			String year=rs.getString("year");
			
			return year;
		}
		
	}


	@Override
	public List findChartInfoByYear() {
		// TODO Auto-generated method stub

		String sql="SELECT convert(varchar(4),ConcludeDate,120) as year "+
				 	"FROM "+Singleton.ROOMDATABASE+".[dbo].[ChartInfo] where ConcludeDate is not null "+
				 	"group by convert(varchar(4),ConcludeDate,120) order by year desc";

		List list=this.getJdbcTemplate().query(sql, new Year());
		
		return list;
	}



	@Override
	public List findChartInfoByMonthOfYear(String year) {
		// TODO Auto-generated method stub
		String sql="SELECT convert(varchar(7),ConcludeDate,120) as year "+
					"FROM "+Singleton.ROOMDATABASE+".[dbo].[ChartInfo] where ConcludeDate is not null "+
					"AND convert(varchar(4),ConcludeDate,120) = "+year+" group by convert(varchar(7),ConcludeDate,120)";
		
		List list=this.getJdbcTemplate().query(sql, new Year());
		
		return list;
	}

	@Override
	public Double getTotalHireByMonth(String month) {
		// TODO Auto-generated method stub
		String sql="SELECT SUM(TotalHire) as Hire "+
					"FROM "+Singleton.ROOMDATABASE+".[dbo].[ChartInfo] where ConcludeDate is not null "+
					"AND convert(varchar(7),ConcludeDate,120) = '"+month+"'";
		
		Double hireByMonth=this.getJdbcTemplate().query(sql, new Hire()).get(0);
		
		return hireByMonth;
	}
	
	@Override
	public List findHireListByYear() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy");
		
		String currentYear=sdf.format(new Date());
		
		String sql="SELECT convert(varchar(4),HireDate,120) as year "+
				 	"FROM "+Singleton.ROOMDATABASE+".[dbo].[HireList] where HireDate is not null "+
				 	"AND convert(varchar(4),HireDate,120)<= "+currentYear+" "+
				 	"group by convert(varchar(4),HireDate,120) order by year desc";

		List list=this.getJdbcTemplate().query(sql, new Year());
		
		return list;
	}

	
	@Override
	public List findHireListByMonthOfYear(String year) {
		// TODO Auto-generated method stub
		String sql="SELECT convert(varchar(7),HireDate,120) as year "+
					"FROM "+Singleton.ROOMDATABASE+".[dbo].[HireList] where HireDate is not null "+ 
					"AND convert(varchar(4),HireDate,120) = "+year+" group by convert(varchar(7),HireDate,120)";
		
		List list=this.getJdbcTemplate().query(sql, new Year());
		
		return list;
	}
	
	@Override
	public Double getAlreadyHireByMonth(String month) {
		// TODO Auto-generated method stub
		String sql="SELECT SUM(Hire) as Hire "+
					"FROM "+Singleton.ROOMDATABASE+".[dbo].[HireList] where HireDate is not null "+
					"AND State='已交' "+
					"AND convert(varchar(7),HireDate,120) =  '"+month+"'";
		
		Double alreadyhireByMonth=this.getJdbcTemplate().query(sql, new Hire()).get(0);
		
		return alreadyhireByMonth;
	}

	@Override
	public Double getNotHireByMonth(String month) {
		// TODO Auto-generated method stub
		String sql="SELECT SUM(Hire) as Hire "+
				"FROM "+Singleton.ROOMDATABASE+".[dbo].[HireList] where HireDate is not null "+
				"AND State='未交' "+
				"AND convert(varchar(7),HireDate,120) =  '"+month+"'";
	
		Double nothireByMonth=this.getJdbcTemplate().query(sql, new Hire()).get(0);
	
		return nothireByMonth;
	}
	
	@Override
	public Map notPlaceRoomInfo(int limit,int offset,String search) {
		// TODO Auto-generated method stub
		
		String sql01="SELECT TOP "+limit+" "+		            
					Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Num,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].OriginalNum,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Address,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].OriginalAddress,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Region,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Segment,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].ManageRegion,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].RoomProperty,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Useful,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Floor,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].State,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Structure,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].BuildArea,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].RoomType,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].IsCity,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Manager,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].ManagerPhone,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].IsStreet,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].FitMent,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].BeFrom,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].InDate,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyRightNo,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyRightArea,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].DesignUseful,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].BuildYear,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyRightUnit,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].RealPropertyRightUnit,"+
				    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyCardUnit, "+	
				    "[Position].GUID,"+
					"[Position].province,"+
					"[Position].city,"+
					"[Position].district,"+
					"[Position].street,"+
					"[Position].street_number,"+
					"[Position].lng,"+
					"[Position].lat,"+
					"[Position].date "+
					"FROM "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo] left join  [Position] "+
					"on "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID = [Position].GUID "+
					"WHERE " ;
					
    String sql02= "[Position].lng is null AND [Position].lat is  null "+  
					"AND ([RoomInfo].State = '已出租' or [RoomInfo].State = '不可出租' or [RoomInfo].State = '空置' ) "+
					"AND "+
					Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID not in( select top "+offset+" "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID from "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo] left join  [Position] "+
					"on "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID = [Position].GUID "+
					"WHERE [Position].lng is null AND [Position].lat is null "+ 
					"AND ([RoomInfo].State = '已出租' or [RoomInfo].State = '不可出租' or [RoomInfo].State = '空置' ) )";
		
		
		String sql1="SELECT count(*) "+
				"FROM "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo] left join  [Position] "+
				"on "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID = [Position].GUID "+
				"WHERE [Position].lng is null AND [Position].lat is  null "+  
				"AND ([RoomInfo].State = '已出租' or [RoomInfo].State = '不可出租' or [RoomInfo].State = '空置' ) ";		
		
		String sql;
		
		if(search.equals("")){
			sql=sql01+sql02;
		}else{
			sql=sql01+sql02+"AND "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Address like '"+search+ "' ";
		    
			sql1=sql1+"AND "+Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Address like '"+search+"'";
		}
		
		System.out.println("sql="+sql);
		
		RoomInfo_Position roomInfo_Position=new RoomInfo_Position();
		
		Position position=new Position();		
		
		RoomInfo roomInfo=new RoomInfo();
		
		Object[] objects={roomInfo,position};
		
		Map map=new HashMap<>();
		
		try{
			List list=SelectSqlJoinExe.get(this.getJdbcTemplate(), sql, objects,roomInfo_Position);
			
			int total=(int) SelectSqlJoinExe.getCount(this.getJdbcTemplate(), sql1, objects).get("");
			
			map.put("rows", list);
			
			map.put("total", total);
		}catch (Exception e) {
			// TODO: handle exception
		}

		return map;
	}

	@Override
	public List getChartInfosByGUID(String GUID) {
		// TODO Auto-generated method stub
		
		ChartInfo chartInfo=new ChartInfo();
		
		chartInfo.setLimit(1);
		chartInfo.setOffset(0);
		
		String[] where={Singleton.ROOMDATABASE+".[dbo].[ChartInfo].GUID=",GUID};
		
		chartInfo.setWhere(where);
		
		return SelectExe.get(this.getJdbcTemplate(), chartInfo);
	}

	@Override
	public Map getAllRoomInfoPosition() {
		// TODO Auto-generated method stub
		Position position=new Position();
		
		String[] where={"[Position].is_roomInfo = ","1"};
		
		position.setLimit(1000000);
		position.setOffset(0);
		position.setWhere(where);
		
		Map map=new HashMap<>();
		
		List list=SelectExe.get(this.getJdbcTemplate(), position);
		
		int total=(int) SelectExe.getCount(this.getJdbcTemplate(), position).get("");
		
		String sql="SELECT sum([TotalHire]) as allHire "+
				"FROM "+Singleton.ROOMDATABASE+".[dbo].[ChartInfo] where IsHistory=0";
		
		List list2=this.getJdbcTemplate().query(sql,new allHire());
		
		Double allHire=(Double) list2.get(0)/10000;
		
		map.put("rows", list);
		
		map.put("total", total);
		
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("######0.00");   
				
		map.put("allHire", df.format(allHire)+"万元");
		
		return map;
	}
	
	
	class allHire implements RowMapper<Double> {

		@Override
		public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Double allHire=rs.getDouble("allHire");
			
			return allHire;
		}
		
	}


	@Override
	public Map getAllHiddenAsset() {
		// TODO Auto-generated method stub
		
		String sql="SELECT [Position].lng,[Position].lat from [Position]"
				+ "left join [Hidden_Assets] on [Position].GUID=[Hidden_Assets].asset_GUID "
				+" where  [Position].is_roomInfo=1 and [Hidden_Assets].asset_GUID is not null";
		
		String sql2="SELECT count(*) from [Position]"
				+ "left join [Hidden_Assets] on [Position].GUID=[Hidden_Assets].asset_GUID "
				+" where  [Position].is_roomInfo=1 and [Hidden_Assets].asset_GUID is not null";
		
		Map map=new HashMap<>();

		try{
			List list=this.getJdbcTemplate().query(sql,new position());
			int total=this.getJdbcTemplate().queryForInt(sql2);
			map.put("rows", list);
			map.put("total", total);
			//MyTestUtil.print(list);
		}catch (Exception e) {
		// TODO: handle exception
		}
		
		return map;
	}
	
	class position implements RowMapper<Position> {

		@Override
		public Position mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			
			Position position=new Position();
			
			position.setLng(rs.getDouble("lng"));
			position.setLat(rs.getDouble("lat"));
			
			return position;
		}
		
	}


	@Override
	public RoomInfo findRoomInfoByChartGUID(String chartGUID) {
		// TODO Auto-generated method stub
		
		RoomInfo roomInfo=new RoomInfo();
		roomInfo.setLimit(2);
		roomInfo.setOffset(0);
		roomInfo.setNotIn("GUID");
		
		String[] where={"[RoomInfo].ChartGUID=",chartGUID};
		
		System.out.println("where="+where);
		MyTestUtil.print(where);
		
		roomInfo.setWhere(where);		
		
		return (RoomInfo) SelectExe.get(this.getJdbcTemplate(), roomInfo).get(0);
	}

	@Override
	public Map<String, Object> findRoomInfoPositionByLatLng(Double lat, Double lng) {
		// TODO Auto-generated method stub
		String sql0="SELECT "+
				"[Position].province,"+
				"[Position].city,"+
				"[Position].district,"+
				"[Position].street,"+
				"[Position].street_number,"+
				"[Position].lng,"+
				"[Position].lat,"+
				"[Position].date,"+
				Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Num,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].OriginalNum,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Address,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].OriginalAddress,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Region,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Segment,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].ManageRegion,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].RoomProperty,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Useful,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Floor,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].State,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Structure,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].BuildArea,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].RoomType,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].IsCity,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].Manager,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].ManagerPhone,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].IsStreet,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].FitMent,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].BeFrom,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].InDate,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyRightNo,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyRightArea,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].DesignUseful,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].BuildYear,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyRightUnit,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].RealPropertyRightUnit,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].PropertyCardUnit ,"+	
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].DangerClassification ,"+
			    Singleton.ROOMDATABASE+".[dbo].[RoomInfo].ChartGUID ,"+
			    Singleton.ROOMDATABASE+".[dbo].[ChartInfo].Hire ,"+
			    Singleton.ROOMDATABASE+".[dbo].[ChartInfo].FareItem ,"+
			    Singleton.ROOMDATABASE+".[dbo].[ChartInfo].Charter ,"+
			    Singleton.ROOMDATABASE+".[dbo].[ChartInfo].Phone ";
			
		String sql1="from Position left join  (SELECT round(lat,4) as lat,"+
					"round(lng,4) as lng ,COUNT(*) as c "+      
					"FROM [Position] group by round(lat,4),round([lng],4)) t2 "+
					"on round(Position.lat,4)=t2.lat and round(Position.lng,4)=t2.lng "+
					"left join RoomInfo on Position.GUID=RoomInfo.GUID "+
					"left join ChartInfo on RoomInfo.ChartGUID=ChartInfo.GUID "+
					"where Position.GUID is not null and round(Position.lat,4)=round("+lat+",4) and "+
					"round(Position.lng,4)=round("+lng+",4)";
		
		String sql2="select count(*) from Position left join  (SELECT round(lat,4) as lat,"+
					"round(lng,4) as lng ,COUNT(*) as c "+      
					"FROM [Position] group by round(lat,4),round([lng],4)) t2 "+
					"on round(Position.lat,4)=t2.lat and round(Position.lng,4)=t2.lng "+
					"left join RoomInfo on Position.GUID=RoomInfo.GUID "+
					"left join ChartInfo on RoomInfo.ChartGUID=ChartInfo.GUID "+
					"where Position.GUID is not null and round(Position.lat,4)=round("+lat+",4) and "+
					"round(Position.lng,4)=round("+lng+",4)";
		
		String sql=sql0+sql1;
		
		RoomInfo_Position roomInfo_Position=new RoomInfo_Position();
		
		Position position=new Position();		
	
		RoomInfo roomInfo=new RoomInfo();
	
		ChartInfo chartInfo=new ChartInfo();
		
		Object[] objects={roomInfo,chartInfo,position};
	
		Map map=new HashMap<>();
	
		try{
			List list=SelectSqlJoinExe.get(this.getJdbcTemplate(), sql, objects,roomInfo_Position);
			int total=(int) SelectSqlJoinExe.getCount(this.getJdbcTemplate(), sql2, objects).get("");
			map.put("rows", list);
			map.put("total", total);
			//MyTestUtil.print(list);
		}catch (Exception e) {
		// TODO: handle exception
		}

		
		return map;
	}
	
}
