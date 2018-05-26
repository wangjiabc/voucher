package com.voucher.manage.daoImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.voucher.manage.dao.HiddenDAO;
import com.voucher.manage.daoModel.RoomInfo;
import com.voucher.manage.daoModel.Assets.Hidden;
import com.voucher.manage.daoModel.Assets.Hidden_Assets;
import com.voucher.manage.daoModel.Assets.Hidden_Check;
import com.voucher.manage.daoModel.Assets.Hidden_Check_Date;
import com.voucher.manage.daoModel.Assets.Hidden_Data;
import com.voucher.manage.daoModel.Assets.Hidden_Level;
import com.voucher.manage.daoModel.Assets.Hidden_Neaten;
import com.voucher.manage.daoModel.Assets.Hidden_Neaten_Date;
import com.voucher.manage.daoModel.Assets.Hidden_Type;
import com.voucher.manage.daoModel.Assets.Hidden_User;
import com.voucher.manage.daoModel.Assets.Position;
import com.voucher.manage.daoModel.Assets.WeiXin_User;
import com.voucher.manage.daoModelJoin.Assets.Hidden_Check_Join;
import com.voucher.manage.daoModelJoin.Assets.Hidden_Join;
import com.voucher.manage.daoModelJoin.Assets.Hidden_Neaten_Join;
import com.voucher.manage.daoSQL.DeleteExe;
import com.voucher.manage.daoSQL.InsertExe;
import com.voucher.manage.daoSQL.SelectExe;
import com.voucher.manage.daoSQL.SelectJoinExe;
import com.voucher.manage.daoSQL.UpdateExe;
import com.voucher.manage.file.AbstractFileUpload;
import com.voucher.manage.singleton.Singleton;
import com.voucher.manage.tools.FileConvect;
import com.voucher.manage.tools.MyTestUtil;
import com.voucher.manage.tools.TransMapToString;

public class HiddenDAOImpl extends JdbcDaoSupport implements HiddenDAO{

	@Override
	public Integer InsertIntoHiddenData(String GUID,String NAME,String TYPE,String uri) {
		// TODO Auto-generated method stub
		Hidden_Data hidden_Data=new Hidden_Data();
		Date date=new Date();
		
		hidden_Data.setGUID(GUID);
		hidden_Data.setNAME(NAME);
		hidden_Data.setTYPE(TYPE);
		hidden_Data.setURI(uri);
        hidden_Data.setDate(date);
		
		return InsertExe.get(this.getJdbcTemplate(), hidden_Data);
	}
	
	
	@Override
	public Integer InsertIntoHiddenCheckData(String Check_id, String NAME, String TYPE, String uri) {
		// TODO Auto-generated method stub
		Hidden_Check_Date hidden_Check_Date=new Hidden_Check_Date();
		Date date=new Date();
		
		hidden_Check_Date.setCheck_id(Check_id);
		hidden_Check_Date.setNAME(NAME);
		hidden_Check_Date.setTYPE(TYPE);
		hidden_Check_Date.setURI(uri);
		hidden_Check_Date.setDate(date);
		
		return InsertExe.get(this.getJdbcTemplate(), hidden_Check_Date);
	}


	@Override
	public Integer InsertIntoHiddenNeatenData(String Neaten_id, String NAME, String TYPE, String uri) {
		// TODO Auto-generated method stub
		Hidden_Neaten_Date hidden_Neaten_Date=new Hidden_Neaten_Date();
		Date date=new Date();
		
		hidden_Neaten_Date.setNeaten_id(Neaten_id);
		hidden_Neaten_Date.setNAME(NAME);
		hidden_Neaten_Date.setTYPE(TYPE);
		hidden_Neaten_Date.setURI(uri);
		hidden_Neaten_Date.setDate(date);
		
		return InsertExe.get(this.getJdbcTemplate(), hidden_Neaten_Date);
	}
	
	@Override
	public Map<String, Object> selectAllHiddenDate(String GUID) {
		
		String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+Singleton.filePath;
		
		List<String> names=new ArrayList<String>();
		List<String> types=new ArrayList<String>();
		List<byte[]> fileBytes=new ArrayList<byte[]>();
		
		// TODO Auto-generated method stub
		Hidden_Data hidden_Data=new Hidden_Data();
		hidden_Data.setLimit(1000);
		hidden_Data.setOffset(0);
		hidden_Data.setNotIn("GUID");
		String[] where={"GUID =",GUID};
		
		hidden_Data.setWhere(where);
		
		List<Hidden_Data> hidden_Datas=SelectExe.get(this.getJdbcTemplate(), hidden_Data);
		
		Iterator<Hidden_Data> iterator=hidden_Datas.iterator();
		
		while(iterator.hasNext()){
			
			Hidden_Data hidden_Data2=iterator.next();
			
			File file=new File(filePath+"\\"+hidden_Data2.getURI());
			
			byte[] fileByte=FileConvect.fileToByte(file);
			
			names.add(hidden_Data2.getNAME());
			types.add(hidden_Data2.getTYPE());
			fileBytes.add(fileByte);
			
		}
		
		Map<String, Object> map=new HashMap<>();
		
		map.put("names", names);
		map.put("types", types);
		map.put("fileBytes", fileBytes);
		
		return map;
	}
	
	
	@Override
	public Map<String, Object> selectAllHiddenCheckDate(String check_id) {
		// TODO Auto-generated method stub
        String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+Singleton.filePath;
		
		List<String> names=new ArrayList<String>();
		List<String> types=new ArrayList<String>();
		List<byte[]> fileBytes=new ArrayList<byte[]>();
		
		// TODO Auto-generated method stub
		Hidden_Check_Date hidden_Check_Date=new Hidden_Check_Date();
		hidden_Check_Date.setLimit(1000);
		hidden_Check_Date.setOffset(0);
		hidden_Check_Date.setNotIn("id");
		String[] where={"check_id =",check_id};
		
		hidden_Check_Date.setWhere(where);
		
		List<Hidden_Check_Date> hidden_Check_Dates=SelectExe.get(this.getJdbcTemplate(), hidden_Check_Date);
		
		Iterator<Hidden_Check_Date> iterator=hidden_Check_Dates.iterator();
		
		while(iterator.hasNext()){
			
			Hidden_Check_Date hidden_Check_Date2=iterator.next();
			
			File file=new File(filePath+"\\"+hidden_Check_Date2.getURI());
			
			byte[] fileByte=FileConvect.fileToByte(file);
			
			names.add(hidden_Check_Date2.getNAME());
			types.add(hidden_Check_Date2.getTYPE());
			fileBytes.add(fileByte);
			
		}
		
		Map<String, Object> map=new HashMap<>();
		
		map.put("names", names);
		map.put("types", types);
		map.put("fileBytes", fileBytes);
		
		return map;
	}


	@Override
	public Map<String, Object> selectAllHiddenNeatenDate(String neaten_id) {
		// TODO Auto-generated method stub
        String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+Singleton.filePath;
		
		List<String> names=new ArrayList<String>();
		List<String> types=new ArrayList<String>();
		List<byte[]> fileBytes=new ArrayList<byte[]>();
		
		// TODO Auto-generated method stub
		Hidden_Neaten_Date hidden_Neaten_Date=new Hidden_Neaten_Date();
		hidden_Neaten_Date.setLimit(1000);
		hidden_Neaten_Date.setOffset(0);
		hidden_Neaten_Date.setNotIn("id");
		String[] where={"neaten_id =",neaten_id};
		
		hidden_Neaten_Date.setWhere(where);
		
		List<Hidden_Neaten_Date> hidden_Neaten_Dates=SelectExe.get(this.getJdbcTemplate(), hidden_Neaten_Date);
		
		Iterator<Hidden_Neaten_Date> iterator=hidden_Neaten_Dates.iterator();
		
		while(iterator.hasNext()){
			
			Hidden_Neaten_Date hidden_Neaten_Date2=iterator.next();
			
			File file=new File(filePath+"\\"+hidden_Neaten_Date2.getURI());
			
			byte[] fileByte=FileConvect.fileToByte(file);
			
			names.add(hidden_Neaten_Date2.getNAME());
			types.add(hidden_Neaten_Date2.getTYPE());
			fileBytes.add(fileByte);
			
		}
		
		Map<String, Object> map=new HashMap<>();
		
		map.put("names", names);
		map.put("types", types);
		map.put("fileBytes", fileBytes);
		
		return map;
	}
	
	@Override
	public Map<String, Object> selectAllHidden(Integer limit, Integer offset, String sort, String order, Map<String, String> search) {
		// TODO Auto-generated method stub
        Hidden hidden=new Hidden();
		
		hidden.setLimit(limit);
		hidden.setOffset(offset);
		hidden.setSort(sort);
		hidden.setOrder(order);
		hidden.setNotIn("id");
		
		if(!search.isEmpty()){
			search.put("[Hidden].exist =", "1");
		    String[] where=TransMapToString.get(search);
		    hidden.setWhere(where);
		}else{
			String[] where={"[Hidden].exist =", "1"};
			hidden.setWhere(where);
		}
		
		Map map=new HashMap<String, Object>();
		
		List list=SelectExe.get(this.getJdbcTemplate(), hidden);
		MyTestUtil.print(list);
		map.put("rows", list);
		
		Map countMap=SelectExe.getCount(this.getJdbcTemplate(), hidden);
		
		map.put("total", countMap.get(""));
		
		return map;
	}

	@Override
	public Integer insertIntoHidden(Hidden hidden) {
		// TODO Auto-generated method stub
		if(hidden!=null){
		  return InsertExe.get(this.getJdbcTemplate(), hidden);	     
		}else{
			return 0;
		}
	}

	@Override
	public Integer updateHidden(Hidden hidden) {
		// TODO Auto-generated method stub
	    
		return UpdateExe.get(this.getJdbcTemplate(), hidden);
	}

	@Override
	public Integer deleteHidden(Hidden hidden) {
		// TODO Auto-generated method stub
		
		//return DeleteExe.get(this.getJdbcTemplate(), hidden);
		Hidden hidden2=new Hidden();
		hidden2.setExist(0);
		hidden2.setWhere(hidden.getWhere());
		int i=UpdateExe.get(this.getJdbcTemplate(), hidden2);
		if(i==1){
			String[] where2={"[Hidden_Data].GUID=",hidden.getGUID()};
			Hidden_Data hidden_Data=new Hidden_Data();
			hidden_Data.setWhere(hidden.getWhere());
			DeleteExe.get(this.getJdbcTemplate(), hidden_Data);
		}
		return i;
	}

	@Override
	public List<Hidden_Level> setctAllHiddenLevel() {
		// TODO Auto-generated method stub
		Hidden_Level hidden_level=new Hidden_Level();
		hidden_level.setOffset(0);
		hidden_level.setLimit(1000);
		hidden_level.setNotIn("id");
		return SelectExe.get(this.getJdbcTemplate(), hidden_level);
	}

	@Override
	public Integer insertHiddenLevel(Hidden_Level hidden_level) {
		// TODO Auto-generated method stub
		String[] where={"[Hidden_Level].hidden_level=",String.valueOf(hidden_level.getHidden_level())};
		Hidden_Level hidden_Level2=new Hidden_Level();
		hidden_Level2.setWhere(where);
		
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden_Level2).get("");
		if(count==0){
			return InsertExe.get(this.getJdbcTemplate(), hidden_level);
		}else {
			return 2;
		}
	}

	@Override
	public Integer deleteHiddenLevel(Hidden_Level hidden_level) {
		// TODO Auto-generated method stub
		Hidden hidden=new Hidden();
		String[] where={"[Hidden].hidden_level=",String.valueOf(hidden_level.getHidden_level()),
				"[Hidden].exist=","1"};
		hidden.setWhere(where);
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden).get("");
		
		if(count==0){
			return DeleteExe.get(this.getJdbcTemplate(), hidden_level);
		}else{
			return 3;
		}
	}

	@Override
	public Integer updateHiddenLevel(Hidden_Level hidden_Level) {
		// TODO Auto-generated method stub
		Hidden hidden=new Hidden();
		String[] where={"[Hidden].hidden_level=",String.valueOf(hidden_Level.getHidden_level()),
				"[Hidden].exist=","1"};
		hidden.setWhere(where);
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden).get("");
		
		if(count==0){
			return UpdateExe.get(this.getJdbcTemplate(), hidden_Level);
		}else{
			return 3;
		}
	}


	@Override
	public Map<String, Object> selectAllHidden_Jion(Integer limit, Integer offset, String sort, String order,
			Map<String, String> search) {
		// TODO Auto-generated method stub
        Hidden hidden=new Hidden();
        search.put("[Hidden].exist =", "1");
        
        System.out.println("order="+order+"      sort="+sort);
        
		hidden.setLimit(limit);
		hidden.setOffset(offset);
		hidden.setSort(sort);
		hidden.setOrder(order);
		hidden.setNotIn("id");
		
		Hidden_Level hidden_Level=new Hidden_Level();
		
		hidden_Level.setLimit(limit);
		hidden_Level.setOffset(offset);
		hidden_Level.setNotIn("id");
		
		Hidden_Type hidden_Type=new Hidden_Type();
		
		hidden_Type.setLimit(limit);
		hidden_Type.setOffset(offset);
		hidden_Type.setNotIn("id");
		
		Hidden_User hidden_User=new Hidden_User();
		
		hidden_User.setLimit(limit);
		hidden_User.setOffset(offset);
		hidden_User.setNotIn("id");
		
		Position position=new Position();
		
		position.setLimit(limit);
		position.setOffset(offset);
		position.setNotIn("id");
		
		WeiXin_User weiXin_User=new WeiXin_User();
		
		weiXin_User.setLimit(limit);
		weiXin_User.setOffset(offset);
		weiXin_User.setNotIn("id");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    hidden.setWhere(where);
		    hidden_Level.setWhere(where);
		    hidden_Type.setWhere(where);
		    hidden_User.setWhere(where);
		    position.setWhere(where);
		    weiXin_User.setWhere(where);
		}
		
		Object[] objects={hidden,hidden_Level,hidden_Type,hidden_User,position,weiXin_User};
		
		Map map=new HashMap<String, Object>();
		
		Hidden_Join hidden_Jion=new Hidden_Join();
		
		String[] join={"hidden_level","type","principal","GUID","campusAdmin"};
		
		List list=SelectJoinExe.get(this.getJdbcTemplate(), objects,hidden_Jion,join);
		MyTestUtil.print(list);
		map.put("rows", list);
		
		Map countMap=SelectJoinExe.getCount(this.getJdbcTemplate(),objects,join);
		
		map.put("total", countMap.get(""));
		
		return map;
	}


	@Override
	public List<Hidden_Type> selectAllHiddenType() {
		// TODO Auto-generated method stub
		Hidden_Type hidden_Type=new Hidden_Type();
		hidden_Type.setOffset(0);
		hidden_Type.setLimit(1000);
		hidden_Type.setNotIn("id");
		return SelectExe.get(this.getJdbcTemplate(), hidden_Type);
	}


	@Override
	public Integer insertHiddenType(Hidden_Type hidden_Type) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), hidden_Type);
	}


	@Override
	public Integer deleteHiddenType(Hidden_Type hidden_Type) {
		// TODO Auto-generated method stub
		Hidden hidden=new Hidden();
		String[] where={"[Hidden].type=",String.valueOf(hidden_Type.getType()),
				"[Hidden].exist=","1"};
		hidden.setWhere(where);
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden).get("");
		
		if(count==0){
			return DeleteExe.get(this.getJdbcTemplate(), hidden_Type);
		}else{
			return 3;
		}
	}


	@Override
	public Integer updateHiddenType(Hidden_Type hidden_Type) {
		// TODO Auto-generated method stub
		Hidden hidden=new Hidden();
		String[] where={"[Hidden].type=",String.valueOf(hidden_Type.getType()),
				"[Hidden].exist=","1"};
		hidden.setWhere(where);
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden).get("");
		
		if(count==0){
			return UpdateExe.get(this.getJdbcTemplate(), hidden_Type);
		}else{
			return 3;
		}
	}


	@Override
	public Map<String, Object> selectHiddenUser(String campusAdmin) {
		// TODO Auto-generated method stub
		Hidden_User hidden_User=new Hidden_User();
		hidden_User.setOffset(0);
		hidden_User.setLimit(10);
		hidden_User.setNotIn("id");
		
		String[] where={"[Hidden_User].campusAdmin =",campusAdmin};
		hidden_User.setWhere(where);
		
		List list=SelectExe.get(this.getJdbcTemplate(), hidden_User);
		
		Map map=new HashMap<>();
		
		try{
		  Hidden_User hidden_User2=(Hidden_User) list.get(0);
		  map.put("state", 1);
		  map.put("row", hidden_User2);
		  return map;
		}catch (Exception e) {
			// TODO: handle exception
		  e.printStackTrace();	
		  map.put("state", 0);
		  return map;
		}
	}
	
	@Override
	public Map<String, Object> selectAllHiddenUser(Integer limit, Integer offset, String sort,
			String order,Map<String, String> search) {
		// TODO Auto-generated method stub
		Hidden_User hidden_User=new Hidden_User();
		hidden_User.setOffset(offset);
		hidden_User.setLimit(limit);
		hidden_User.setNotIn("id");
		
		try{
			if(!search.isEmpty()){
				search.put("[Hidden_User].purview !=", "0");
				String[] where=TransMapToString.get(search);
				hidden_User.setWhere(where);
			}else{
				String[] where={"[Hidden_User].purview !=", "0"};
				hidden_User.setWhere(where);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		Map<String, Object> map=new HashMap<>();
		
		List list=SelectExe.get(this.getJdbcTemplate(), hidden_User);
		
		MyTestUtil.print(list);
		
		map.put("rows", list);
		
		Map countMap=SelectExe.getCount(this.getJdbcTemplate(), hidden_User);
		
		map.put("total", countMap.get(""));
		
		return map;
		
	}


	@Override
	public Integer insertHiddenUser(Hidden_User hidden_User) {
		// TODO Auto-generated method stub
		
		Hidden_User hidden_User2=new Hidden_User();
		
		String[] where={"[Hidden_User].campusAdmin=",hidden_User.getCampusAdmin()};
		
		hidden_User2.setWhere(where);
		
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden_User2).get("");

		Hidden_User hidden_User3=new Hidden_User();
		
		String[] where2={"[Hidden_User].id=",String.valueOf(hidden_User.getId())};
		
		hidden_User3.setWhere(where2);
		
		int count2=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden_User3).get("");
		
		if(count!=0){
			return 2;
		}else if(count2!=0){
			return 3;
		}else{
		   return InsertExe.get(this.getJdbcTemplate(), hidden_User);
		}
	}


	@Override
	public Integer deleteHiddenUser(Hidden_User hidden_User) {
		// TODO Auto-generated method stub
		Hidden hidden=new Hidden();
		String[] where={"[Hidden].principal=",String.valueOf(hidden_User.getPrincipal()),
				"[Hidden].exist=","1"};
		hidden.setWhere(where);
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden).get("");
		
		if(count==0){
			return DeleteExe.get(this.getJdbcTemplate(), hidden_User);
		}else{
			return 3;
		}
	}


	@Override
	public Integer updateHiddenUser(Hidden_User hidden_User) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), hidden_User);
	}
	
	@Override
	public Integer updateUserPassword(Hidden_User hidden_User,String OldPw) {
		// TODO Auto-generated method stub
		hidden_User.setNotIn("id");
		hidden_User.setLimit(10);
		hidden_User.setOffset(0);
		
		String[] where={"[Hidden_User].campusAdmin=",hidden_User.getCampusAdmin()};
		hidden_User.setWhere(where);
		List list=SelectExe.get(this.getJdbcTemplate(), hidden_User);
		
		try{
			Hidden_User hidden_User2=(Hidden_User) list.get(0);
			if(!OldPw.equals(hidden_User2.getPassword())){
				System.out.println("npw="+hidden_User.getPassword());
				System.out.println("opw="+hidden_User2.getPassword());
				return 3;
			}else{
				return UpdateExe.get(this.getJdbcTemplate(), hidden_User);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 2;
		}
	}
	
	@Override
	public Map<String, Object> selectAllHiddenCheck(Integer limit, Integer offset, String sort,
			String order,String address,Map<String, String> search) {
		// TODO Auto-generated method stub
		
		Map map=new HashMap<String, Object>();
		List<Hidden_Check_Join> list;
		Map countMap;
		
		Hidden_Check hidden_Check=new Hidden_Check();
		search.put("[Hidden_Check].exist =", "1");
		
		hidden_Check.setOffset(offset);
		hidden_Check.setLimit(limit);
		hidden_Check.setSort(sort);
		hidden_Check.setOrder(order);
		hidden_Check.setNotIn("id");
		
		RoomInfo roomInfo=new RoomInfo();
		
		roomInfo.setOffset(offset);
		roomInfo.setLimit(limit);
		roomInfo.setSort(sort);
		roomInfo.setOrder(order);
		roomInfo.setNotIn("id");
		
		Position position=new Position();
		
		position.setLimit(limit);
		position.setOffset(offset);
		position.setSort(sort);
		position.setOrder(order);
		position.setNotIn("id");
		
		WeiXin_User weiXin_User=new WeiXin_User();
		
		weiXin_User.setLimit(limit);
		weiXin_User.setOffset(offset);
		weiXin_User.setSort(sort);
		weiXin_User.setOrder(order);
		weiXin_User.setNotIn("id");
		
		if(address!=null&&!address.equals("")){
			
			RoomInfo roomInfo2=new RoomInfo();
			
			String[] where2={Singleton.ROOMDATABASE+".[dbo].[RoomInfo].[Address] like ","%"+address+"%"};
			
			roomInfo2.setWhere(where2);
			roomInfo2.setLimit(2);
			roomInfo2.setOffset(0);			
			roomInfo2.setNotIn("GUID");
			
			List<RoomInfo> list3=SelectExe.get(this.getJdbcTemplate(), roomInfo2);
			
			try{
				
				RoomInfo roomInfo3=list3.get(0);
							
				//MyTestUtil.print(roomInfo3);
				
				if(roomInfo3!=null){

					search.put("[Hidden_Check].[GUID] = ", roomInfo3.getGUID());
				}
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
			
		}
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    hidden_Check.setWhere(where);
		    roomInfo.setWhere(where);
		    position.setWhere(where);
		    weiXin_User.setWhere(where);
		}

		if(offset>0){
			
			Object[] objects={hidden_Check,weiXin_User};
			
			String[] join={"campusAdmin"};
		
			Hidden_Check_Join hidden_Check_Join=new Hidden_Check_Join();
		
			list=SelectJoinExe.get(this.getJdbcTemplate(), objects, hidden_Check_Join, join);
		
			Iterator<Hidden_Check_Join> iterator=list.iterator();
			
			int i=0;
			
			while (iterator.hasNext()) {
				
				Hidden_Check_Join hidden_Check_Join2=iterator.next();
				
				String guid=hidden_Check_Join2.getGUID();
				
				RoomInfo roomInfo4=new RoomInfo();
				
				String[] where4={Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID = ",guid};
				
				roomInfo4.setWhere(where4);
				roomInfo4.setLimit(2);
				roomInfo4.setOffset(0);			
				roomInfo4.setNotIn("GUID");
				
				System.out.println("guid="+guid);
				
				List<RoomInfo> list2=SelectExe.get(this.getJdbcTemplate(), roomInfo4);
				
				try{
					
					RoomInfo roomInfo5=list2.get(0);
								
					//MyTestUtil.print(roomInfo5);
					
					hidden_Check_Join2.setAddress(roomInfo5.getAddress());
					hidden_Check_Join2.setManageRegion(roomInfo5.getManageRegion());
					hidden_Check_Join2.setState(roomInfo5.getState());
					
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				Position position2=new Position();
				
				String[] where5={"[Position].[check_id]=",hidden_Check_Join2.getCheck_id()};
				
				position2.setWhere(where5);
				position2.setLimit(2);
				position2.setOffset(0);
				position2.setNotIn("id");
				
				List<Position> list3=SelectExe.get(this.getJdbcTemplate(), position2);
				
				try{
					if(!list3.isEmpty()){
					
						Position position3=list3.get(0);
					
						hidden_Check_Join2.setLat(position3.getLat());
						hidden_Check_Join2.setLng(position3.getLng());
						hidden_Check_Join2.setCity(position3.getCity());
						hidden_Check_Join2.setDistrict(position3.getDistrict());
						hidden_Check_Join2.setStreet(position3.getStreet());
					
					}
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				list.set(i, hidden_Check_Join2);
				
				i++;
			}
			
			countMap=SelectJoinExe.getCount(this.getJdbcTemplate(), objects, join);
			
		}else{

			
			Object[] objects={hidden_Check,position,weiXin_User};
			
			String[] join={"check_id","campusAdmin"};
		
			Hidden_Check_Join hidden_Check_Join=new Hidden_Check_Join();
		
			list=SelectJoinExe.get(this.getJdbcTemplate(), objects, hidden_Check_Join, join);
		
			Iterator<Hidden_Check_Join> iterator=list.iterator();
			
			int i=0;
			
			while (iterator.hasNext()) {
				
				Hidden_Check_Join hidden_Check_Join2=iterator.next();
				
				String guid=hidden_Check_Join2.getGUID();
				
				RoomInfo roomInfo4=new RoomInfo();
				
				String[] where4={Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID = ",guid};
				
				roomInfo4.setWhere(where4);
				roomInfo4.setLimit(2);
				roomInfo4.setOffset(0);			
				roomInfo4.setNotIn("GUID");
				
				System.out.println("guid="+guid);
				
				List<RoomInfo> list2=SelectExe.get(this.getJdbcTemplate(), roomInfo4);
				
				try{
					
					RoomInfo roomInfo5=list2.get(0);
								
					//MyTestUtil.print(roomInfo5);
					
					hidden_Check_Join2.setAddress(roomInfo5.getAddress());
					hidden_Check_Join2.setManageRegion(roomInfo5.getManageRegion());
					hidden_Check_Join2.setState(roomInfo5.getState());
					
					
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
							
				
				list.set(i, hidden_Check_Join2);
				
				i++;
			}
			
			countMap=SelectJoinExe.getCount(this.getJdbcTemplate(), objects, join);
		}
		
		
		
		map.put("rows", list);
		System.out.println("checkjoinlist=");
		MyTestUtil.print(list);
		

		map.put("total", countMap.get(""));
		
		return map;
	}


	@Override
	public Integer insertHiddenCheck(Hidden_Check hidden_Check) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), hidden_Check);
	}


	@Override
	public Integer deleteHiddenCheck(Hidden_Check hidden_Check) {
		// TODO Auto-generated method stub
		Hidden_Check hidden_Check2=new Hidden_Check();
		hidden_Check2.setExist(0);
		hidden_Check2.setWhere(hidden_Check.getWhere());
		//return DeleteExe.get(this.getJdbcTemplate(), hidden_Check);
		
		int i=UpdateExe.get(this.getJdbcTemplate(), hidden_Check2);
		
		if(i==1){
			String[] where2={"check_id=",hidden_Check.getCheck_id()};
			Hidden_Check_Date hidden_Check_Date=new Hidden_Check_Date();
			hidden_Check_Date.setWhere(where2);
			DeleteExe.get(this.getJdbcTemplate(), hidden_Check_Date);
		}
		
		return i;
	}


	@Override
	public Map<String, Object> selectAllHiddenNeaten(Integer limit, Integer offset, String sort,
			String order,Map<String, String> search) {
		// TODO Auto-generated method stub
		Hidden_Neaten hidden_Neaten=new Hidden_Neaten();
		search.put("[Hidden_Neaten].exist =", "1");
		
		hidden_Neaten.setOffset(offset);
		hidden_Neaten.setLimit(limit);
		hidden_Neaten.setSort(sort);
		hidden_Neaten.setOrder(order);
		hidden_Neaten.setNotIn("id");
		
        Hidden hidden=new Hidden();
		
		hidden.setLimit(limit);
		hidden.setOffset(offset);
		hidden.setSort(sort);
		hidden.setOrder(order);
		hidden.setNotIn("id");
		
		Position position=new Position();
		
		position.setLimit(limit);
		position.setOffset(offset);
		position.setSort(sort);
		position.setOrder(order);
		position.setNotIn("id");
		
		WeiXin_User weiXin_User=new WeiXin_User();
		
		weiXin_User.setLimit(limit);
		weiXin_User.setOffset(offset);
		weiXin_User.setSort(sort);
		weiXin_User.setOrder(order);
		weiXin_User.setNotIn("id");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    hidden_Neaten.setWhere(where);
		    hidden.setWhere(where);
		    position.setWhere(where);
		    weiXin_User.setWhere(where);
		}
		
		Map map=new HashMap<String, Object>();
		
		Object[] objects={hidden_Neaten,hidden,position,weiXin_User};
		
		Hidden_Neaten_Join hidden_Neaten_Join=new Hidden_Neaten_Join();
		
		String[] join={"GUID","neaten_id","campusAdmin"};
		
		List<Hidden_Neaten> list=SelectJoinExe.get(this.getJdbcTemplate(), objects, hidden_Neaten_Join, join);
		
		map.put("rows", list);
		
		Map countMap=SelectJoinExe.getCount(this.getJdbcTemplate(), objects, join);
			
		map.put("total", countMap.get(""));
			
		return map;
	}


	@Override
	public Integer insertHiddenNeaten(Hidden_Neaten hidden_Neaten) {
		// TODO Auto-generated method stub
		return InsertExe.get(this.getJdbcTemplate(), hidden_Neaten);
	}


	@Override
	public Integer deleteHiddenNeaten(Hidden_Neaten hidden_Neaten) {
		// TODO Auto-generated method stub
		Hidden_Neaten hidden_Neaten2=new Hidden_Neaten();
		hidden_Neaten2.setExist(0);
		hidden_Neaten2.setWhere(hidden_Neaten.getWhere());
		//return DeleteExe.get(this.getJdbcTemplate(), hidden_Neaten);
		
		int i=UpdateExe.get(this.getJdbcTemplate(), hidden_Neaten2);
		
		if(i==1){
			String[] where2={"[Hidden_Neaten_Date].neaten_id=",hidden_Neaten.getNeaten_id()};
			Hidden_Neaten_Date hidden_Neaten_Date=new Hidden_Neaten_Date();
			hidden_Neaten_Date.setWhere(where2);
			DeleteExe.get(this.getJdbcTemplate(), hidden_Neaten_Date);		
		}
		
		return i;
	}


	@Override
	public Integer updateHiddenCheck(Hidden_Check hidden_Check) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), hidden_Check);
	}


	@Override
	public Integer updateHiddenNeaten(Hidden_Neaten hidden_Neaten) {
		// TODO Auto-generated method stub
		return UpdateExe.get(this.getJdbcTemplate(), hidden_Neaten);
	}


	@Override
	public List<Hidden_Join> selectHiddenOfMap(Map<String, String> search) {
		// TODO Auto-generated method stub
		 Hidden hidden=new Hidden();
		 search.put("[Hidden].exist =", "1");
		 
			hidden.setLimit(10);
			hidden.setOffset(0);
			hidden.setSort(null);
			hidden.setOrder(null);
			hidden.setNotIn("GUID");
			
			Position position=new Position();
			position.setLimit(10);
			position.setOffset(0);
			position.setSort(null);
			position.setOrder(null);
			position.setNotIn("GUID");
			
		 String[] where=TransMapToString.get(search);
		 hidden.setWhere(where);
		 position.setWhere(where);

			 
		Map map=new HashMap<String, Object>();
				
		Object[] objects={hidden,position};
				
		String[] join={"GUID","GUID"};
			
		Hidden_Join hidden_Join=new Hidden_Join();
		
		List hidden_joins=SelectJoinExe.get(this.getJdbcTemplate(), objects, hidden_Join, join);		
			 
		return hidden_joins;
	}


	@Override
	public Integer getAllAssetByHidden_GUID(String guid) {
		// TODO Auto-generated method stub
		
		Hidden_Assets hidden_Assets=new Hidden_Assets();
		
		String[] where={"hidden_GUID=",guid};
		
		hidden_Assets.setWhere(where);
		
		int count=(int) SelectExe.getCount(this.getJdbcTemplate(), hidden_Assets).get("");
		
		return count;
	}



}
