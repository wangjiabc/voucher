package com.voucher.weixin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.voucher.manage.model.Users;
import com.voucher.manage.model.WeiXin;
import com.voucher.manage.service.UserService;
import com.voucher.manage.service.WeiXinService;
import com.voucher.manage.tools.MyTestUtil;
import com.voucher.weixin.base.AutoAccessToken;
import com.voucher.weixin.base.CommonUtil;
import com.voucher.weixin.util.HttpRequestUtil;
import com.voucher.weixin.util.HttpUtils;
import com.voucher.weixin.wxpay.sdk.WXConstant;
import com.voucher.weixin.wxpay.sdk.WXPayConstants.SignType;
import com.voucher.weixin.wxpay.sdk.WXPayUtil;

import voucher.wxpaytest;

@Controller
@RequestMapping("/mobile/weinXinPay")
public class WeinXinPayController {

	Logger logger = LoggerFactory.getLogger(WeinXinPayController.class);
	
	private UserService userService;

	private WeiXinService weixinService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setWeixinService(WeiXinService weixinService) {
		this.weixinService = weixinService;
	}

	@RequestMapping("/getHire")
	public @ResponseBody List getHire(String value, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

//		 String homeUrl=request.getHeader("Host")+request.getContextPath();

		String spbill_create_ip = WXConstant.spbill_create_ip;

		String notify_url = WXConstant.notify_url;

		JSONObject json = JSONObject.parseObject(value);

		System.out.println("value=" + value + "=====");

		Integer campusId = json.getInteger("campusId");
		String text = json.getString("text");
		String guids = json.getString("guid");
		int hire = (int) (json.getFloat("hire")* 100);

		String[] guidsString = guids.split(",");

		String openId = (String) request.getSession().getAttribute("openId");

		System.out.println("openId=" + openId);

		WeiXin weixin = weixinService.getCampusById(campusId);

		String appId = weixin.getAppId();

		String mch_id = weixin.getMchId();

		String api = weixin.getApi();

		String nonce_str = WXPayUtil.generateNonceStr();

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

		String out_trade_no = df.format(new Date());

		int total_fee = hire;

		Map<String, String> map = new HashMap<String, String>();

		map.put("appid", appId);
		map.put("body", WXConstant.body);
		map.put("mch_id", mch_id);
		map.put("sign_type", "HMAC-SHA256");
		map.put("nonce_str", nonce_str);
		map.put("openid", openId);
		map.put("out_trade_no", out_trade_no);
		map.put("spbill_create_ip", spbill_create_ip);
		map.put("total_fee", String.valueOf(total_fee));
		map.put("trade_type", WXConstant.trade_type);
		map.put("notify_url", notify_url);
		
		
		String sign = WXPayUtil.generateSignature(map, weixin.getApi(),SignType.HMACSHA256);

		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++sign:{}", sign);
		System.out.println(sign);

		map.put("sign", sign);


		String xml = WXPayUtil.mapToXml(map);

		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++xml:{}", xml);

		HttpUtils httpUtils=new HttpUtils();
		
		String xmlStr=httpUtils.doPost(WXConstant.unifiedorder_url, xml);
				 
		//System.out.println("xmlStr=====" + xmlStr);

		// 以下内容是返回前端页面的json数据

		String prepay_id = "";// 预支付id

		List result = new ArrayList();

		Map<String, String> returnMap = new HashMap<String, String>();

		returnMap = WXPayUtil.xmlToMap(xmlStr);
		
		logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++returnMap:{}", returnMap);

		String return_code = returnMap.get("return_code");

		Map<String, String> payMap = new HashMap<String, String>();

		if (return_code.equals("SUCCESS")) {

			System.out.println("return_code++++" + return_code);

			String result_code = returnMap.get("result_code");

			if (result_code.equals("SUCCESS")) {

				System.out.println("result_code==========" + result_code);

				payMap.put("appid", appId);

				payMap.put("timeStamp", String.valueOf(WXPayUtil.getCurrentTimestampMs()));

				payMap.put("nonceStr", WXPayUtil.generateNonceStr());
				
				payMap.put("sign_type", "HMAC-SHA256");

				prepay_id = returnMap.get("prepay_id");

				String trade_type = returnMap.get("trade_type");

				payMap.put("package", "prepay_id=" + prepay_id);

				String paySign = WXPayUtil.generateSignature(payMap, weixin.getApi(),SignType.HMACSHA256);
				
				payMap.put("paySign", paySign);

				result.add("SUCCESS");
			} else {

				String err_code = returnMap.get("err_code");

				String err_code_des = returnMap.get("err_code_des");

				payMap.put("err_code", err_code);

				payMap.put("err_code_des", err_code_des);

				result.add("ERR");
			}

		} else if (return_code.equals("FAIL")) {

			String return_msg = returnMap.get("return_msg");

			payMap.put("return_msg", return_msg);

			result.add("FAIL");

		}

		result.add(payMap);

		return result;
	}

	@RequestMapping("/callback")
	public String callback(HttpServletRequest request, HttpServletResponse response) throws Exception {

		BufferedReader reader = null;

		reader = request.getReader();

		String line = "";

		String xmlString = null;

		StringBuffer inputString = new StringBuffer();

		while ((line = reader.readLine()) != null) {
			inputString.append(line);
		}

		xmlString = inputString.toString();

		request.getReader().close();

		System.out.println("xmlString+++++++++++++++" + xmlString);

		Map<String, String> map = new HashMap<String, String>();

		String result_code = "";

		String return_code = "";

		map = WXPayUtil.xmlToMap(xmlString);

		result_code = map.get("result_code");

		return_code = map.get("return_code");

		if (result_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {

			return WXConstant.SUCCESS;

		}

		return WXConstant.FAIL;

	}

}
