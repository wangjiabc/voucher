package com.voucher.manage.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.voucher.manage.daoModel.RoomInfo;
import com.voucher.manage.daoModelJoin.RoomInfo_Position;

public interface RoomInfoDao {

	public List<RoomInfo> findAllRoomInfo(Integer limit, Integer offset, String sort,
			String order,Map search);
	
	Integer getRoomInfoCount(Map search);
	
	public List<RoomInfo_Position> findAllRoomInfo_Position(Integer limit, Integer offset, String sort,
			String order,Map search);
	
	public Integer updateRoomInfo(RoomInfo roomInfo);
	
	public Integer deleteRoomInfo(RoomInfo roomInfo);
		
	public Map<String, Object> findAllChangehire_CharLog(Integer limit, Integer offset, String sort,
			String order,String search);
	
	public Map<String, Object> findAllFloor(Integer limit, Integer offset, String sort,
			String order,String search);
	
	public Map<String, Object> getChartInfoByGUID(Integer limit, Integer offset, String sort,
			String order,Map search);
	
	public Map<String, Object> getHireListByGUID(Integer limit, Integer offset, String sort,
			String order,Map search);
	
	public Integer insertIntoFileSelfBelong(String RoomGUID,String UpFileFullName,String FileType,
			String FileBelong);
	
	public Double getAllTotalHire();
	
	public Double getAlreadyHire();
	
	public Double getNotHire();
	
	public List findChartInfoByYear();
	
	public List findHireListByYear();
	
	public List findChartInfoByMonthOfYear(String year);
	
	public Double getTotalHireByMonth(String month);
	
	public List findHireListByMonthOfYear(String year);
	
	public Double getAlreadyHireByMonth(String month);
	
	public Double getNotHireByMonth(String month);
	
	public Map notPlaceRoomInfo(int limit,int offset,String search);
	
	public List getChartInfosByGUID(String GUID);
	
	public Map getAllRoomInfoPosition();
	
	public Map getAllHiddenAsset();
	
	public RoomInfo findRoomInfoByChartGUID(String chartGUID);
	
	public Map<String,Object> findRoomInfoPositionByLatLng(Double lat,Double lng);
}
