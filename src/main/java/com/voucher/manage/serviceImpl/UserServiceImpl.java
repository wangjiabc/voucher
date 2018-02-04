﻿package com.voucher.manage.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voucher.manage.mapper.User_AssetMapper;
import com.voucher.manage.mapper.UsersMapper;
import com.voucher.manage.model.User_Asset;
import com.voucher.manage.model.Users;
import com.voucher.manage.service.UserService;
import com.voucher.weixin.base.SNSUserInfo;


@Service("userService")
public class UserServiceImpl implements UserService {
	private UsersMapper usersMapper;         //操作用户信息

	private User_AssetMapper user_AssetMapper;
	
	@Autowired
	public void setUsersMapper(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}

	@Autowired
	public void setUser_AssetMapper(User_AssetMapper user_AssetMapper) {
		this.user_AssetMapper = user_AssetMapper;
	}
	
	public List<Users> getAllFullUser(Integer campusId,Integer limit, Integer offset, String sort,
			String order,String search) {
		return usersMapper.getAllFullUser(campusId,limit,offset,sort,order,search);
	}
	
	public Integer getUserCount(String campusAdmin ,Integer campusId,String search) {
		return usersMapper.getUserCount(campusAdmin,campusId,search);
	}

	public Integer getUserFullCount(Integer campusId,String search) {
		return usersMapper.getUserFullCount(campusId,search);
	}



	@Override
	public Integer getOpenId(Integer campusId, String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getOpenId(campusId, openId);
	}

	@Override
	public Integer insertUser(SNSUserInfo snsUserInfo) {
		// TODO Auto-generated method stub
		return usersMapper.insertUserInfo(snsUserInfo);
	}

	@Override
	public Users getUserInfoById(Integer campusId, String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getUserByOpenId(campusId, openId);
	}

	@Override
	public Integer upUserByOpenId(SNSUserInfo snsUserInfo) {
		// TODO Auto-generated method stub
		return usersMapper.upUserByOpenId(snsUserInfo);
	}

	@Override
	public Integer upsubscribeByOpenId(Map<String, Object> paramterMap) {
		// TODO Auto-generated method stub
		return usersMapper.upsubscribeByOpenId(paramterMap);
	}

	@Override
	public int upAtionFormatter(Map<String, Object> paramterMap) {
		// TODO Auto-generated method stub
		return usersMapper.upAtionFormatter(paramterMap);
	}

	@Override
	public int selectRepeatUser(String name) {
		// TODO Auto-generated method stub
		return usersMapper.selectRepeatUser(name);
	}

	@Override
	public int selectRepeatUserByOpenId(String openId) {
		// TODO Auto-generated method stub
		return usersMapper.selectRepeatUserByOpenId(openId);
	}

	@Override
	public int insertUsersInfo(Users users) {
		// TODO Auto-generated method stub
		return usersMapper.insertUsersInfo(users);
	}

	@Override
	public int updateUsersInfo(Users users) {
		// TODO Auto-generated method stub
		return usersMapper.updateUsersInfo(users);
	}

	@Override
	public Users getUserByOnlyOpenId(String openId) {
		// TODO Auto-generated method stub
		return usersMapper.getUserByOnlyOpenId(openId);
	}

	@Override
	public User_Asset selectUser_AssetByOpenId(String openId) {
		// TODO Auto-generated method stub
		return user_AssetMapper.selectByPrimaryKey(openId);
	}
	
	@Override
	public int insertIntoUser_AssetByOpenId(User_Asset user_Asset) {
		// TODO Auto-generated method stub
		return user_AssetMapper.insert(user_Asset);
	}

	@Override
	public int updateUser_AssetByOpenId(User_Asset user_Asset) {
		// TODO Auto-generated method stub
		return user_AssetMapper.updateByPrimaryKeySelective(user_Asset);
	}

	@Override
	public int getCountUser_AssetByOpenId(String openId) {
		// TODO Auto-generated method stub
		return user_AssetMapper.getCountUser_AssetByOpenId(openId);
	}

	@Override
	public List<Users> getUserByPhone(Integer limit, Integer offset, String sort, String order) {
		// TODO Auto-generated method stub
		return usersMapper.getUserByPhone(limit, offset, sort, order);
	}

	@Override
	public List<Users> getUserByGuidance() {
		// TODO Auto-generated method stub
		return usersMapper.getUserByGuidance();
	}

}
