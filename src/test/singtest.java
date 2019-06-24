import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.voucher.manage.tools.Md5;
import com.voucher.manage.tools.TransMapToString;
import com.voucher.weixin.wxpay.sdk.WXConstant;
import com.voucher.weixin.wxpay.sdk.WXPay;
import com.voucher.weixin.wxpay.sdk.WXPayConstants.SignType;
import com.voucher.weixin.wxpay.sdk.WXPayUtil;

public class singtest {

	public static void main(String[] args) throws Exception {
	    
		Map<String, String> map = new HashMap();
		  
		  map.put("appid","wxd930ea5d5a258f4f");
		  map.put("device_info","1000");
		  map.put("body","test");
		  map.put("mch_id","10000100");
		  map.put("nonce_str","ibuaiVcKdpRxkhJA");
//		  map.put("openid",openId);
//		  map.put("out_trade_no",out_trade_no);
//		  map.put("spbill_create_ip",spbill_create_ip);
//		  map.put("total_fee",total_fee);
//		  map.put("trade_type",WXConstant.trade_type);
//		  map.put("notify_url",notify_url);
		  
		  String key="192006250b4c09247ec02edce69f6a2d";
		  
		  String A = WXPayUtil.generateSignature(map, key);
		  
		  System.out.println("wxpay+++"+A);
		  
		  String signnew = "";
		  
		  Collection<String> keyset= map.keySet(); 
		  
		  List list=new ArrayList<String>(keyset);

		  System.out.println("list===="+list);
		  
		  Collections.sort(list);
		  //这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
		  for(int i=0;i<list.size();i++){
			  
			  
			  signnew = signnew + list.get(i)+"="+map.get(list.get(i))+"&";
		 	
		  }
		  
		  signnew=signnew.substring(0, signnew.length()-1)+"&key=192006250b4c09247ec02edce69f6a2d";
		  
		  
		  
		  System.out.println(signnew);
		  
		  signnew = WXPayUtil.MD5(signnew).toUpperCase();
		  
//		  signnew = Md5.GetMD5Code(signnew).toUpperCase();
		  
		  System.out.println("signnew======"+signnew);
		  
		  String stringSignTemp="";
		  for (Map.Entry<String, String> entry : map.entrySet()) {
			  stringSignTemp=stringSignTemp+entry.getKey()+"="+entry.getValue()+"&";
			  
			  System.out.println(stringSignTemp);
		  }
		  
		  String stringA="appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA&key=192006250b4c09247ec02edce69f6a2d";
		  
		  //stringSignTemp=stringSignTemp.substring(0, stringSignTemp.length()-1)+"&key=192006250b4c09247ec02edce69f6a2d"; 
		  
//		  stringSignTemp=stringA+"&key=192006250b4c09247ec02edce69f6a2d";
		  String sign= WXPayUtil.MD5(stringA).toUpperCase();
//		  String sign=Md5.GetMD5Code(stringSignTemp).toUpperCase();
		  //String sign = WXPayUtil.generateSignature(map, "192006250b4c09247ec02edce69f6a2d");
		
	    System.out.println(sign);
  	}
}
