package com.voucher.weixin.util;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import common.HttpClientManager;

public class HttpUtils {
	
	 private HttpClient httpClient;

	    public HttpUtils(){
	    	
		if (httpClient == null) {
			BasicHttpClientConnectionManager connManager;

			connManager = new BasicHttpClientConnectionManager(
					RegistryBuilder.<ConnectionSocketFactory>create()
							.register("http", PlainConnectionSocketFactory.getSocketFactory())
							.register("https", SSLConnectionSocketFactory.getSocketFactory()).build(),
					null, null, null);
			httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
			}
	    }
	
	    /**
	     * get 请求
	     *
	     * @param url
	     * @return
	     */
	    public String doGet(String url,String params){
	        String result="";
	        HttpEntity entity = null;
	        HttpResponse response=null;
	        HttpGet method=new HttpGet(url);
	        try{
	            if(params != null && !params.isEmpty()){
	                String str = params;
	                method.setURI(new URI(method.getURI().toString() + "?" + str));
	            }
	            response=httpClient.execute(method);
	            if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
	                entity=response.getEntity();
	                result=EntityUtils.toString(entity);
	            }
	        }catch (Exception ex){
	            throw new RuntimeException(ex);
	        }
	        return result;
	    }

	    /**
	     * post 请求
	     *
	     * @param url
	     * @return
	     */
	    public String doPost(String url,String params){
	        String result="";
	        HttpEntity entity = null;
	        HttpResponse response=null;
	        HttpPost httpPost=new HttpPost(url);
	        httpPost.addHeader("Content-Type", "text/xml");
	        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 ");
	        try{
	            if(params != null && !params.isEmpty()){
	            	StringEntity stringEntity = new StringEntity(params, "UTF-8");
	            	httpPost.setEntity(stringEntity);
	            }
	            
	            response=httpClient.execute(httpPost);
	            if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
	                entity=response.getEntity();
	                result=EntityUtils.toString(entity,"UTF-8");
	            }
	        }catch (Exception ex){
	            throw new RuntimeException(ex);
	        }
	        return result;
	    }
	    
	/**
     * @Description: http post 请求 json 数据
     * @param @param urls
     * @param @param params
     * @param @return
     * @param @throws ClientProtocolException
     * @param @throws IOException
     * @author dapengniao
     * @date 2016 年 3 月 10 日 下午 3:58:15
     */
    public static String sendPostBuffer(String urls, String params)
            throws ClientProtocolException, IOException {
        HttpPost request = new HttpPost(urls);

        StringEntity se = new StringEntity(params, HTTP.UTF_8);
        request.setEntity(se);
        // 发送请求
        @SuppressWarnings("resource")
        HttpResponse httpResponse = new DefaultHttpClient().execute(request);
        // 得到应答的字符串，这也是一个 JSON 格式保存的数据
        String retSrc = EntityUtils.toString(httpResponse.getEntity());
        request.releaseConnection();
        return retSrc;

    }
}