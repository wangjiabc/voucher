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
import com.voucher.manage.daoModel.Assets.Hidden;
import com.voucher.manage.daoModel.Assets.Hidden_Data;
import com.voucher.manage.daoModel.Assets.Hidden_Level;
import com.voucher.manage.daoModel.Assets.Hidden_Type;
import com.voucher.manage.daoModelJoin.Assets.Hidden_Jion;
import com.voucher.manage.daoSQL.DeleteExe;
import com.voucher.manage.daoSQL.InsertExe;
import com.voucher.manage.daoSQL.SelectExe;
import com.voucher.manage.daoSQL.SelectJoinExe;
import com.voucher.manage.daoSQL.UpdateExe;
import com.voucher.manage.file.AbstractFileUpload;
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
	public Map<String, Object> selectAllHiddenDate(String GUID) {
		
		String pathRoot = System.getProperty("user.home");
		
		String filePath=pathRoot+AbstractFileUpload.filePath;
		
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
	public Map<String, Object> selectAllHidden(Integer limit, Integer offset, String sort, String order, Map<String, String> search) {
		// TODO Auto-generated method stub
        Hidden hidden=new Hidden();
		
		hidden.setLimit(limit);
		hidden.setOffset(offset);
		hidden.setSort(sort);
		hidden.setOrder(order);
		hidden.setNotIn("id");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
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
		
		return DeleteExe.get(this.getJdbcTemplate(), hidden);
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
		return InsertExe.get(this.getJdbcTemplate(), hidden_level);
	}

	@Override
	public Integer deleteHiddenLevel(Hidden_Level hidden_level) {
		// TODO Auto-generated method stub
		return DeleteExe.get(this.getJdbcTemplate(), hidden_level);
	}

	@Override
	public Map<String, Object> selectAllHidden_Jion(Integer limit, Integer offset, String sort, String order,
			Map<String, String> search) {
		// TODO Auto-generated method stub
        Hidden hidden=new Hidden();
		
		hidden.setLimit(limit);
		hidden.setOffset(offset);
		hidden.setSort(sort);
		hidden.setOrder(order);
		hidden.setNotIn("GUID");
		
		Hidden_Level hidden_Level=new Hidden_Level();
		
		hidden_Level.setLimit(limit);
		hidden_Level.setOffset(offset);
		hidden_Level.setSort(sort);
		hidden_Level.setOrder(order);
		hidden_Level.setNotIn("GUID");
		
		Hidden_Type hidden_Type=new Hidden_Type();
		
		hidden_Type.setLimit(limit);
		hidden_Type.setOffset(offset);
		hidden_Type.setSort(sort);
		hidden_Type.setOrder(order);
		hidden_Type.setNotIn("GUID");
		
		if(!search.isEmpty()){
		    String[] where=TransMapToString.get(search);
		    hidden.setWhere(where);
		    hidden_Level.setWhere(where);
		    hidden_Type.setWhere(where);
		}
		
		Object[] objects={hidden,hidden_Level,hidden_Type};
		
		Map map=new HashMap<String, Object>();
		
		Hidden_Jion hidden_Jion=new Hidden_Jion();
		
		String[] join={"hidden_level","type"};
		
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
		return DeleteExe.get(this.getJdbcTemplate(), hidden_Type);
	}


}
