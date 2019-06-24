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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.jce.provider.JCEMac.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.voucher.manage.dao.FinanceDAO;
import com.voucher.manage.model.Users;
import com.voucher.manage.model.WeiXin;
import com.voucher.manage.service.UserService;
import com.voucher.manage.service.WeiXinService;
import com.voucher.manage.singleton.Singleton;
import com.voucher.manage.tools.Md5;
import com.voucher.manage.tools.MyTestUtil;
import com.voucher.sqlserver.context.Connect;
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
	
	ApplicationContext applicationContext=new Connect().get();
	
	FinanceDAO financeDAO=(FinanceDAO) applicationContext.getBean("financeDao");
	
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

		String homeUrl=request.getHeader("Host")+request.getContextPath();

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

		String out_trade_no = Md5.GetMD5Code(UUID.randomUUID().toString());

		int total_fee = hire;

		Map<String, String> map = new HashMap<String, String>();

		map.put("appid", appId);
		map.put("body", WXConstant.body);
		map.put("mch_id", mch_id);
		map.put("nonce_str", nonce_str);
		map.put("openid", openId);
		map.put("out_trade_no", out_trade_no);
		map.put("spbill_create_ip", spbill_create_ip);
		map.put("total_fee", String.valueOf(total_fee));
		map.put("trade_type", WXConstant.trade_type);
		map.put("notify_url", notify_url);
		
		
		String sign = WXPayUtil.generateSignature(map, weixin.getApi());

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

		if (return_code.equals("SUCCESS")) {

			System.out.println("return_code++++" + return_code);

			String result_code = returnMap.get("result_code");

			if (result_code.equals("SUCCESS")) {

				System.out.println("result_code==========" + result_code);

				Map<String, String> payMap = new HashMap<String, String>();
				
				payMap.put("appId", appId);

				payMap.put("timeStamp", String.valueOf(WXPayUtil.getCurrentTimestampMs()));

				payMap.put("nonceStr", WXPayUtil.generateNonceStr());
				
				payMap.put("signType", "MD5");

				prepay_id = returnMap.get("prepay_id");

				payMap.put("package", "prepay_id=" + prepay_id);

				String paySign = WXPayUtil.generateSignature(payMap, weixin.getApi());
				
				payMap.put("paySign", paySign);

				MyTestUtil.print(payMap);
				
				String xmlpay=WXPayUtil.mapToXml(payMap);
				
				System.out.println("xmlpay===="+xmlpay);
				
				result.add("SUCCESS");
				
				result.add(payMap);

				LinkedHashMap<String,Map<String, Object>> registerMap=Singleton.getInstance().getRegisterMapLong();
				
				Map tradeMap=new HashMap<>();
				
				tradeMap.put("guids", guids);
				
				tradeMap.put("payMap", payMap);
				
				registerMap.put(out_trade_no, tradeMap);
				
				return result;
				
			} else {

				Map<String, String> payMap = new HashMap<String, String>();
				
				String err_code = returnMap.get("err_code");

				String err_code_des = returnMap.get("err_code_des");

				payMap.put("err_code", err_code);

				payMap.put("err_code_des", err_code_des);

				result.add("ERR");
				
				result.add(payMap);

				return result;
				
			}

		} else if (return_code.equals("FAIL")) {

			Map<String, String> payMap = new HashMap<String, String>();
			
			String return_msg = returnMap.get("return_msg");

			payMap.put("return_msg", return_msg);

			result.add("FAIL");
			
			result.add(payMap);

			return result;

		}
		
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

		map = WXPayUtil.xmlToMap(xmlString);

		result_code = map.get("result_code");

		if (result_code.equals("SUCCESS")) {

			LinkedHashMap<String,Map<String, Object>> registerMap=Singleton.getInstance().getRegisterMapLong();
			
			String out_trade_no=map.get("out_trade_no");
			
			Map tradeMap=registerMap.get(out_trade_no);
			
			if(tradeMap.get("guids")!=null&&!tradeMap.get("guids").equals("")){
				
				String guids=(String) tradeMap.get("guids");
				
				String[] filesString = guids.split(",");
				
				ArrayList<String> list=new ArrayList<>();
				for (String fileString : filesString) {

					list.add(URLDecoder.decode(fileString,"utf-8"));
					
				}
				
				int i=financeDAO.updateHireSetHireListWinXinPay(list);
				
				if(i>0){
					return WXConstant.SUCCESS;
				}else{
					return WXConstant.FAIL;
				}
				
			}else{
				
				return WXConstant.FAIL;
				
			}

		}

		return WXConstant.FAIL;

	}

}
