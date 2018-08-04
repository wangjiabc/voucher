package com.voucher.weixin.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.voucher.manage.dao.AssetCheckDAO;
import com.voucher.manage.dao.AssetsDAO;
import com.voucher.manage.dao.FinanceDAO;
import com.voucher.manage.dao.HiddenDAO;
import com.voucher.manage.dao.MobileDAO;
import com.voucher.manage.dao.RoomInfoDao;
import com.voucher.manage.daoModel.RoomInfo;
import com.voucher.manage.daoModel.TTT.ChartInfo;
import com.voucher.manage.service.UserService;
import com.voucher.sqlserver.context.Connect;

@Controller
@RequestMapping("/mobile/finance")
public class AssetFinanceController {

	ApplicationContext applicationContext=new Connect().get();
	
	FinanceDAO financeDAO=(FinanceDAO) applicationContext.getBean("financeDao");
	
	HiddenDAO hiddenDAO=(HiddenDAO) applicationContext.getBean("hiddenDao");
	
	AssetsDAO assetsDAO=(AssetsDAO) applicationContext.getBean("assetsdao");
	
	AssetCheckDAO assetCheckDAO=(AssetCheckDAO) applicationContext.getBean("assetCheckdao");
	
	MobileDAO mobileDao=(MobileDAO) applicationContext.getBean("mobileDao");
	
	RoomInfoDao roomInfoDao=(RoomInfoDao) applicationContext.getBean("roomInfodao");
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/getAllChartInfo")
	public @ResponseBody Map<String, Object> allChartInfo(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			String search,HttpServletRequest request){
		
		if(sort!=null&&!sort.equals("")){

		}else{
			sort="ConcludeDate";
		}
		
		if(order!=null&&!order.equals("")){
		}else{
			order="asc";
		}
		
		Map searchMap=new HashMap<>();
		
		if(search!=null&&!search.equals("")){
			
			search="%"+search+"%";
			
			searchMap.put("RoomAddress like ", search);
		
			searchMap.put("Charter like ", search);
		
			searchMap.put("IDNo like ", search);
		
		}
		
		return financeDAO.findAllChartInfo(limit, offset, sort, order, searchMap);
		
	}
	
	@RequestMapping("/getGeneralChartInfo")
	public @ResponseBody Map<String, Object> generalChartInfo(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			@RequestParam Integer isHistory,String search,HttpServletRequest request) {
		
		if(sort!=null&&!sort.equals("")){

		}else{
			sort="ConcludeDate";
		}
		
		if(order!=null&&!order.equals("")){
		}else{
			order="asc";
		}
		
		Map searchMap=new HashMap<>();
		
		if(search!=null&&!search.equals("")){
			
			search="%"+search+"%";
			
			searchMap.put("RoomAddress like ", search);
		
			searchMap.put("Charter like ", search);
		
			searchMap.put("IDNo like ", search);
		
		}
		
		return financeDAO.findGeneralChartInfo( limit, offset, sort, order,isHistory,searchMap);
		
	}
	
	@RequestMapping("/getOverdueChartInfo")
	public @ResponseBody Map<String, Object> overdueChartInfo(@RequestParam Integer limit,@RequestParam Integer offset,String sort,String order,
			@RequestParam Integer isHistory,String search,HttpServletRequest request) {
		
		if(sort!=null&&!sort.equals("")){

		}else{
			sort="ConcludeDate";
		}
		
		if(order!=null&&!order.equals("")){
		}else{
			order="asc";
		}
		
		Map searchMap=new HashMap<>();
		
		if(search!=null&&!search.equals("")){
			
			search="%"+search+"%";
			
			searchMap.put("RoomAddress like ", search);
		
			searchMap.put("Charter like ", search);
		
			searchMap.put("IDNo like ", search);
		
		}
		
		return financeDAO.findOverdueChartInfo( limit, offset, sort, order,isHistory,searchMap);
		
	}
	
	@RequestMapping("/getHireListByGUID")
	public @ResponseBody Map getHireListByGUID(@RequestParam Integer limit,@RequestParam Integer offset,
			@RequestParam String chartGUID,String sort,String order,
			String search,HttpServletRequest request){
		if(sort!=null&&!sort.equals("")){
			if(!sort.equals("state")){
				sort=" state asc, "+sort;
			}
		}else{
			sort="state asc, HireDate";
		}
		
		if(order!=null&&!order.equals("")){
			
		}else{
			order="asc";
		}
		
		System.out.println("order="+order);
		
		Map searchMap=new HashMap<>();
    	
    	searchMap.put("[HireList].ChartGUID=", chartGUID);
    	   	    	
    	Map map=financeDAO.getHireListByGUID(limit, offset, sort, order, searchMap);
    	
    	return map;
    	
	}
	
	@RequestMapping("/getChartInfoByIdNo")
	public @ResponseBody ChartInfo getChartInfoByIdNo(@RequestParam String idNo){
		
		return assetsDAO.getChartInfoByIDNo(idNo);

	}
	
	@RequestMapping("/getRoomInfoByIdNo")
	public @ResponseBody RoomInfo getRoomInfoByIdNo(@RequestParam String idNo){
		
		ChartInfo chartInfo=assetsDAO.getChartInfoByIDNo(idNo);

		String chartGUID=chartInfo.getGUID();
		
		return roomInfoDao.findRoomInfoByChartGUID(chartGUID);
		
	}
	
	@RequestMapping(value="uploadFilesHireList")
	public @ResponseBody Integer uploadFilesHireList(@RequestParam("file") String file,
			HttpServletRequest request,HttpServletResponse response) throws Exception {  
		
		String[] filesString = file.split(",");

		// 娑擄拷濞嗏�冲灩闂勩倕顦挎稉顏堟祩妞嬶拷
		ArrayList<String> list=new ArrayList<>();
		for (String fileString : filesString) {

			list.add(URLDecoder.decode(fileString,"utf-8"));
			
		}
		
		return financeDAO.updateHireSetHireList(list);
 		
	}
	
}
