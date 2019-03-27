package com.voucher.manage.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.voucher.manage.dao.AssetsDAO;
import com.voucher.manage.dao.HiddenDAO;
import com.voucher.manage.dao.MobileDAO;
import com.voucher.manage.dao.RoomInfoDao;
import com.voucher.manage.daoModelJoin.Assets.Hidden_Check_Join;
import com.voucher.manage.daoModelJoin.Assets.Hidden_Join;
import com.voucher.manage.daoModelJoin.Assets.Hidden_Neaten_Join;
import com.voucher.manage.service.UserService;
import com.voucher.manage.singleton.Singleton;
import com.voucher.manage.tools.MyTestUtil;
import com.voucher.manage.tools.TestDistance;
import com.voucher.sqlserver.context.Connect;

@Controller
@RequestMapping("/android")
public class AndroidController {

	ApplicationContext applicationContext=new Connect().get();
	
	HiddenDAO hiddenDAO=(HiddenDAO) applicationContext.getBean("hiddenDao");
	
	AssetsDAO assetsDAO=(AssetsDAO) applicationContext.getBean("assetsdao");
	
	MobileDAO mobileDao=(MobileDAO) applicationContext.getBean("mobileDao");
	
	RoomInfoDao roomInfoDao=(RoomInfoDao) applicationContext.getBean("roomInfodao");
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/getAll")
	public @ResponseBody Map<String, Object> RoomInfo(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			String search,Integer search2,Integer search3,String manageRegion,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String term="OR";
		
		if(sort!=null&&sort.equals("buildArea")){
			sort="BuildArea";
		}else if(sort!=null&&sort.equals("address")){
			sort="Address";
		}else{
			sort="Num";
		}
		
		if(order!=null&&order.equals("asc")){
			order="asc";
		}else if(order!=null&&order.equals("desc")){
			order="desc";
		}else{
			order="asc";
		}
		
		Map where=new HashMap<>();
		
		if(search!=null&&!search.trim().equals("")){
			search="%"+search+"%";  
			where.put("Address like ", search);
			where.put("Num like ", search);
		}		

		if(manageRegion!=null&&!manageRegion.equals("")){
			where.put(" [RoomInfo].ManageRegion = ", manageRegion);
		}
		
		//设置有搜索条件并且时间为空时的查询条件
		String searchTerm="";
		
		if(search2!=null){
		
			term="and";
			
			Calendar cal = Calendar.getInstance();  
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
			
			String startTime = null;
			
			startTime=sdf.format(cal.getTime());
			
			where=new HashMap<>();
			
			if(search!=null&&!search.trim().equals("")){
				where.put("Address like ", search);
				searchTerm="Address like '"+search+"'";
			}
		
			if(search2==0){
				
				where.put("convert(varchar(11),"+Singleton.ROOMDATABASE+
						".[dbo].[RoomInfo].hidden_check_date ,120 )<", startTime);
				
				//用于查询datatime等于空值
				Map roomInfo_Positions=assetsDAO.findAllRoomInfoCheckDateNULL(limit, offset, sort, order,term,searchTerm,where, 1);
				
				List roominfos=(List) roomInfo_Positions.get("rows");
				int total=(int) roomInfo_Positions.get("total");
				
				map.put("rows", roominfos);
				map.put("total", total);
				
				Map fileBytes=mobileDao.roomInfo_PositionImageQuery(request, roominfos);
				map.put("fileBytes", fileBytes);
				
				return map;
				
			}else if(search2==1){
				where.put(Singleton.ROOMDATABASE+".[dbo].[RoomInfo].hidden_check_date >", startTime);
			}
		}
		
		if(search3!=null){
			Calendar cal = Calendar.getInstance();  
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	        cal.add(Calendar.MONTH, -1);
	        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
			
			String startTime = null;
			
			startTime=sdf.format(cal.getTime());
			
			where=new HashMap<>();
			
			if(search!=null&&!search.trim().equals("")){
				where.put("Address like ", search);
				searchTerm="Address like '"+search+"'";
			}
	
			if(search3==0){
		
				where.put("convert(varchar(11),"+Singleton.ROOMDATABASE+
						".[dbo].[RoomInfo].asset_check_date ,120 )<", startTime);
				
				//用于查询datatime等于空值
				Map roomInfo_Positions=assetsDAO.findAllRoomInfoCheckDateNULL(limit, offset, sort, order,term,searchTerm, where, 2);
				
				List roominfos=(List) roomInfo_Positions.get("rows");
				int total=(int) roomInfo_Positions.get("total");
				
				map.put("rows", roominfos);
				map.put("total", total);
				
				Map fileBytes=mobileDao.roomInfo_PositionImageQuery(request, roominfos);
				map.put("fileBytes", fileBytes);
				
				return map;
				
			}else if(search3==1){
				where.put(Singleton.ROOMDATABASE+".[dbo].[RoomInfo].asset_check_date >", startTime);
			}
		}
		
		Map roomInfo_Positions=assetsDAO.findAllRoomInfo_Position(limit, offset, sort, order,term, where);
		
		List roominfos=(List) roomInfo_Positions.get("rows");
		int total=(int) roomInfo_Positions.get("total");
		
		map.put("rows", roominfos);
		map.put("total", total);
		
		Map fileBytes=mobileDao.roomInfo_PositionImageQuery(request, roominfos);
		map.put("fileBytes", fileBytes);
		
		MyTestUtil.print(fileBytes);
		
		return map;
	}
	
	@RequestMapping("/getAll2")
	public @ResponseBody Map<String, Object> RoomInfo2(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			String search,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		
		String term="OR";
		
		if(sort!=null&&sort.equals("buildArea")){
			sort="BuildArea";
		}
		
		if(sort!=null&&sort.equals("address")){
			sort="Address";
		}
		if(order!=null&&order.equals("asc")){
			order="asc";
		}
		
		if(order!=null&&order.equals("desc")){
			order="desc";
		}
		
		Map where=new HashMap<>();
		
		where.put("[RoomInfo].State !=", "已划拨");
		where.put("[RoomInfo].State= ", "空置");
		
		if(search!=null&&!search.trim().equals("")){
			search="%"+search+"%";  
			where.put("Address like ", search);
		}		

		//Map roomInfo_Positions=assetsDAO.findAllRoomInfo_Position(limit, offset, sort, order,term, where);
		
		//List roominfos=(List) roomInfo_Positions.get("rows");
		
		List roominfos=roomInfoDao.findAllRoomInfo_Position(limit, offset, sort, order, where);
		
		map.put("rows", roominfos);
			
		Map fileBytes=mobileDao.roomInfo_PositionImageQuery(request, roominfos);
		map.put("fileBytes", fileBytes);
		
		MyTestUtil.print(fileBytes);
		
		return map;
	}
	
	@RequestMapping("/selectAllHidden")
	public @ResponseBody Map selectAllHidden(@RequestParam Integer limit, @RequestParam Integer offset, 
			String sort, String order,
			 String search,HttpServletRequest request) {
		Map searchMap=new HashMap<>();
		
		if(search!=null&&!search.equals("")){
			searchMap.put("Hidden.name like", "%"+search+"%");
		}
		
		Map map=hiddenDAO.selectAllHidden_Jion(limit, offset, sort, order, searchMap);
		
		List list=(List) map.get("rows");
		
		Map fileBytes=mobileDao.hiddenImageQuery(request,list);
		
		Map result=new HashMap<>();
		
		result.put("hidden", list);
		result.put("fileBytes", fileBytes);
		
		return result;
	}
	
	@RequestMapping("/selectHiddenByGuid")
	public @ResponseBody Map selectHiddenByGuid(@RequestParam String guid,HttpServletRequest request){
        Map searchMap=new HashMap<>();
		System.out.println("guid="+guid);
	    searchMap.put("Hidden.GUID = ", guid);
		
		Map map=hiddenDAO.selectAllHidden_Jion(10, 0, null, null, searchMap);
		
		List list=(List) map.get("rows");
		
		Map result=new HashMap<>();
		
		Hidden_Join hidden_Join=(Hidden_Join) list.get(0);
		
		List fileBytes=mobileDao.allHiddenImageByGUID(request, hidden_Join);
		
		result.put("hidden", hidden_Join);
		result.put("fileBytes", fileBytes);
		MyTestUtil.print(fileBytes);
		
		return result;
	}
	
	@RequestMapping("/selectAllCheck")
	public @ResponseBody Map selectAllCheck(@RequestParam Integer limit, @RequestParam Integer offset, 
			String sort, String order,
			String search,String search2,String search3,HttpServletRequest request) {
		Map searchMap=new HashMap<>();
		
		Map map;
		
		/*
		if(!search.equals("")){
			searchMap.put("check_name like", "%"+search+"%");
		}
		*/
		
		if(search!=null&&!search.equals("")){
			
			int d=(int) TestDistance.get(search);
			
			System.out.println("d="+d);
			
			if(d>0){
				
				Calendar cal = Calendar.getInstance();  
				cal.set(cal.get(Calendar.YEAR), d-1, 0, 0, 0, 0);  
		        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
				
				String startTime = null;
				
				String endTime=null;
				
				startTime=sdf.format(cal.getTime());
				
				cal.set(cal.get(Calendar.YEAR), d, 0, 0, 0, 0);
		        
		        endTime=sdf.format(cal.getTime());
				
		        System.out.println("searchMap="+searchMap);
		        
				searchMap.put("convert(varchar(11),[Hidden_Check].date,120 ) >", startTime);
				searchMap.put("convert(varchar(11),[Hidden_Check].date,120 ) <", endTime);
				
				System.out.println("cal.getActualMinimum(Calendar.DAY_OF_MONTH="+startTime);
				
				System.out.println("searchMap="+searchMap);
				
				map=hiddenDAO.selectAllHiddenCheck(limit, offset, sort, order,null, searchMap);

				List list=(List) map.get("rows");
				
				int total=(int) map.get("total");
				
				Map fileBytes=mobileDao.checkImageQuery(request,list);
				
				Map result=new HashMap<>();
				
				result.put("hidden_Check", list);
				result.put("total", total);
				result.put("fileBytes", fileBytes);
				
				return result;
				
			}else{
				searchMap.put(Singleton.ROOMDATABASE+".[dbo].[RoomInfo].[Address] like", "%"+search+"%");
			}
	
		}
		
		if(search2!=null&&!search2.equals("")){
			
			Calendar cal = Calendar.getInstance();  
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
			
			String startTime = null;
			
			startTime=sdf.format(cal.getTime());
			
			searchMap.put("convert(varchar(11),[Hidden_Check].date,120 ) >", startTime);
			
			System.out.println("startTime="+startTime);
		}
		
		if(search3!=null&&!search3.equals("")){
			searchMap.put("[Hidden_Check].guid = ", search3);
		}
		
		map=hiddenDAO.selectAllHiddenCheck(limit, offset, sort, order,search, searchMap);
		
		List list=(List) map.get("rows");
		
		int total=(int) map.get("total");
		
		Map fileBytes=mobileDao.checkImageQuery(request,list);
		
		Map result=new HashMap<>();
		
		result.put("hidden_Check", list);
		result.put("total", total);
		result.put("fileBytes", fileBytes);
		
		return result;
	}
	
	@RequestMapping("/selectCheckByCheckId")
	public @ResponseBody Map selectCheckByCheckId(@RequestParam String check_id,HttpServletRequest request){
        Map searchMap=new HashMap<>();
		
	    searchMap.put("[Hidden_Check].check_id = ", check_id);
		
		Map map=hiddenDAO.selectAllHiddenCheck(1, 0, null, null,null, searchMap);
		
		List list=(List) map.get("rows");
		
		Map result=new HashMap<>();
		
		Hidden_Check_Join hidden_Check_Join=(Hidden_Check_Join) list.get(0);
		
		List fileBytes=mobileDao.allCheckImageByGUID(request, hidden_Check_Join);
		
		result.put("hidden_Check", hidden_Check_Join);
		result.put("fileBytes", fileBytes);
		MyTestUtil.print(fileBytes);
		
		return result;
	}
	
	
	@RequestMapping("/selectAllNeaten")
	public @ResponseBody Map selectAllNeaten(@RequestParam Integer limit, @RequestParam Integer offset, 
			String sort, String order,
			String search,HttpServletRequest request) {
		Map searchMap=new HashMap<>();
		
		if(!search.equals("")){
			searchMap.put("[Hidden_Neaten].neaten_name like", "%"+search+"%");
		}
		
		Map map=hiddenDAO.selectAllHiddenNeaten(limit, offset, sort, order, searchMap);
		
		List list=(List) map.get("rows");
		
		Map fileBytes=mobileDao.neatenImageQuery(request,list);
		
		Map result=new HashMap<>();
		
		result.put("hidden_Neaten", list);
		result.put("fileBytes", fileBytes);
		
		return result;
	}
	
	@RequestMapping("/selectNeatenByNeatenId")
	public @ResponseBody Map selectNeatenByNeatenId(@RequestParam String neaten_id,HttpServletRequest request){
        Map searchMap=new HashMap<>();
		
	    searchMap.put("[Hidden_Neaten].neaten_id = ", neaten_id);
		
		Map map=hiddenDAO.selectAllHiddenNeaten(10, 0, null, null, searchMap);
		
		List list=(List) map.get("rows");
		
		Map result=new HashMap<>();
		
		Hidden_Neaten_Join hidden_Neaten_Join=(Hidden_Neaten_Join) list.get(0);
		
		List fileBytes=mobileDao.allNeatenImageByGUID(request, hidden_Neaten_Join);
		
		result.put("hidden_Neaten", hidden_Neaten_Join);
		result.put("fileBytes", fileBytes);
		MyTestUtil.print(fileBytes);
		
		return result;
	}
	
	
	
	
}
