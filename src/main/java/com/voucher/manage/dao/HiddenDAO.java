package com.voucher.manage.dao;

import java.util.Map;

import com.voucher.manage.daoModel.Assets.Hidden;

public interface HiddenDAO {

	public Map<String, Object> findAllHidden(Integer limit, Integer offset, String sort,
			String order,Map<String, String> search);
	
	public Integer insertIntoHidden(Hidden hidden);
	
	public Integer updateHidden(Hidden hidden);
}
