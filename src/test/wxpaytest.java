import java.util.HashMap;
import java.util.Map;

import com.voucher.weixin.util.HttpUtils;
import com.voucher.weixin.wxpay.sdk.WXPayConfig;
import com.voucher.weixin.wxpay.sdk.WXPayUtil;

public class wxpaytest {

	public static void main(String[] args) throws Exception {
		
		Map map=new HashMap();
		
		map.put("appid", "wx341107fe47464801");
		map.put("mch_id", "1277689001");
		map.put("nonce_str", "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
		map.put("body", "腾讯充值中心");
		map.put("out_trade_no", "20150806125346");
		map.put("total_fee", "1");
		map.put("spbill_create_ip", "123.12.12.123");
		map.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");
		map.put("trade_type", "JSAPI");
		
		String sign=WXPayUtil.generateSignature(map, "jiuchengxinbaozhangshangluzhou11");
		
		System.out.println(sign);
		
		map.put("sign", sign);
		
		String xml=WXPayUtil.mapToXml(map);
		
		System.out.println(xml);
		
		HttpUtils httpUtils=new HttpUtils();
		/*
		xml="<?xml version='1.0' encoding='UTF-8'?><xml>"+
"<nonce_str><![CDATA[5K8264ILTKCH16CQ2502SI8ZNMTM67VS]]></nonce_str>"+
"<out_trade_no>20150806125346</out_trade_no>"+
"<appid><![CDATA[wx341107fe47464801]]></appid>"+
"<total_fee>1</total_fee>"+
"<sign><![CDATA[472577E5CF7FBCBB77819D17A2E2073F]]></sign>"+
"<trade_type><![CDATA[JSAPI]]></trade_type>"+
"<mch_id>1277689001</mch_id>"+
"<body><![CDATA[腾讯充值中心]]></body>"+
"<notify_url><![CDATA[http://www.weixin.qq.com/wxpay/pay.php]]></notify_url>"+
"<spbill_create_ip>123.12.12.123</spbill_create_ip>"+
"</xml>";*/

		System.out.println("xml===="+xml);
		
		String aa=httpUtils.doPost("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
		
		System.out.println(aa);
	}
	
}
