package com.voucher.weixin.wxpay.sdk;

public class WXConstant {
	
	public static final String body = "租金";
	
	public static final String notify_url = "http://lzxlzc.com/voucher/mobile/weinXinPay/callback.do";
	public static final String spbill_create_ip = "223.86.150.188";
	public static final String trade_type = "JSAPI";
	
	//统一下单地址
	public static final String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	//下单成功返回xml
	public static final String SUCCESS = "<xml>\n<return_code><![CDATA[SUCCESS]]></return_code>\n<return_msg><![CDATA[OK]]></return_msg>\n</xml>";
	
	//下单失败返回xml
	public static final String FAIL = "<xml>\n<return_code><![CDATA[FAIL]]></return_code>\n<return_msg><![CDATA[ERROR]]></return_msg>\n</xml>";
  
  
}
