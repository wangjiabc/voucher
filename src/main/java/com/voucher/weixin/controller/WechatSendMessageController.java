package com.voucher.weixin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.voucher.manage.dao.AssetsDAO;
import com.voucher.manage.dao.HiddenDAO;
import com.voucher.manage.dao.RoomInfoDao;
import com.voucher.manage.daoModel.RoomInfo;
import com.voucher.manage.daoModel.Assets.Hidden;
import com.voucher.manage.daoModel.TTT.ChartInfo;
import com.voucher.manage.mapper.MessageListMapper;
import com.voucher.manage.mapper.UsersMapper;
import com.voucher.manage.mapper.WeiXinMapper;
import com.voucher.manage.model.MessageList;
import com.voucher.manage.model.Users;
import com.voucher.manage.model.WeiXin;
import com.voucher.manage.service.UserService;
import com.voucher.manage.service.WeiXinService;
import com.voucher.manage.serviceImpl.UserServiceImpl;
import com.voucher.manage.singleton.Singleton;
import com.voucher.sqlserver.context.Connect;
import com.voucher.weixin.MessageTemplate.ChatTemplateProcessor;
import com.voucher.weixin.MessageTemplate.TemplateData;
import com.voucher.weixin.MessageTemplate.WxTemplate;

@Controller
@RequestMapping("/mobile/WechatSendMessage")
public class WechatSendMessageController {
	Integer campusId=1;
	
	private WeiXinService weixinService;

	private UserService userService;
	
	@Autowired
	public void setAccessTokenService(WeiXinService weiXinService) {
		this.weixinService=weiXinService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	ApplicationContext applicationContext=new Connect().get();
	
	HiddenDAO hiddenDAO=(HiddenDAO) applicationContext.getBean("hiddenDao");
	
	AssetsDAO assetsDAO=(AssetsDAO) applicationContext.getBean("assetsdao");
	
	RoomInfoDao roomInfoDao=(RoomInfoDao) applicationContext.getBean("roomInfodao");
	
	@RequestMapping("/send")
	public @ResponseBody String template(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String title,@RequestParam String reportUser,
			@RequestParam String reportContext,@RequestParam String place,@RequestParam String url) {
		String accessToken;
    	WeiXin weixin;
		
		weixin=weixinService.getCampusById(campusId);   	
		accessToken=weixin.getAccessToken();
    	
		List userlist=userService.getUserByGuidance();
		
    	ChatTemplateProcessor wechatTemplate=new ChatTemplateProcessor();
    	
    	Iterator<Users> iterator=userlist.iterator();
    	
    	String message="";
    	
    	while (iterator.hasNext()) {
		
    	Users users=iterator.next();	
    	
    	String openid=users.getOpenId();
    	
    	WxTemplate templateData=new WxTemplate();
    	System.out.println("url="+url);
    	templateData.setUrl(url);
    	templateData.setTouser(openid);
    	templateData.setTopcolor("#000000");
    	templateData.setTemplate_id("XjGvV8BcSYzVtcxOJmXD2VefRqor4QQXYfLabWckkiw");
    	Map<String,TemplateData> m = new HashMap<String,TemplateData>();
    	TemplateData first = new TemplateData();
    	first.setColor("#000000");
    	first.setValue(title);
    	m.put("first", first);
    	TemplateData keyword1 = new TemplateData();
    	keyword1.setColor("#328392");
    	keyword1.setValue(reportUser);
    	m.put("keyword1", keyword1);
    	TemplateData keyword2 = new TemplateData();
    	keyword2.setColor("#328392");
    	keyword2.setValue(reportContext);
    	m.put("keyword2", keyword2);
    	TemplateData keyword3 = new TemplateData();
    	keyword3.setColor("#328392");
    	keyword3.setValue(place);
    	m.put("keyword3", keyword3);
    	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	Date date=new Date();
		String time=sdf.format(date);
    	TemplateData keyword4 = new TemplateData();
    	keyword4.setColor("#328392");
    	keyword4.setValue(time);
    	m.put("keyword4", keyword4);
    	TemplateData remark = new TemplateData();
    	remark.setColor("#929232");
    	remark.setValue("点击查看事件位置");
    	m.put("remark", remark);
    	templateData.setData(m);
    	
    	String s=wechatTemplate.sendTemplateMessage(accessToken, templateData);
    	
    	message=message+"  "+users.getNickname()+"  "+s;
    	
    	MessageList messageList=new MessageList();
    	
    	messageList.setCampusId(campusId);
    	messageList.setOpenId(openid);
    	messageList.setContext(reportContext);
    	messageList.setType("紧急报告");
    	messageList.setSendTime(date);
    	if(s.equals("消息发送成功")){
    		messageList.setState(1);
    	}else{
    		messageList.setState(0);
    	}
    	
    	weixinService.insertMessageList(messageList);
    	
       }
    	
      return message;
    	
	}
	
	
	public void send(@RequestParam String guid,@RequestParam String hidden_GUID){

		Integer campusId=1;
		
		String accessToken;
    	WeiXin weixin;

		Map search=new HashMap<>();
		
		search.put(Singleton.ROOMDATABASE+".[dbo].[RoomInfo].GUID=", guid);
		
		List<RoomInfo> list=roomInfoDao.findAllRoomInfo(2, 0, null, null, search);
		
		RoomInfo roomInfo=list.get(0);
		
		String chartGUID=roomInfo.getChartGUID();
		
		Map search2=new HashMap<>();
		
		search2.put(Singleton.ROOMDATABASE+".[dbo].[ChartInfo].GUID=", chartGUID);
		
		List<ChartInfo> list2=(List<ChartInfo>) roomInfoDao.getChartInfoByGUID(2, 0, null, null, search2).get("rows");
		
		try{
		
		if(list2!=null){
			
			ChartInfo chartInfo=list2.get(0);
			
			String charter=chartInfo.getCharter().trim();
			String idNo=chartInfo.getIDNo().trim();
			
			System.out.println("charter="+charter+"     idno="+idNo);
			
			ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring-mybatis2.xml");
			
			DefaultSqlSessionFactory defaultSqlSessionFactory= (DefaultSqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
				
			SqlSession sqlSession=defaultSqlSessionFactory.openSession();
			
			UsersMapper usersMapper=sqlSession.getMapper(UsersMapper.class);
			
			Users users=usersMapper.getUserByAssetCharter(charter, idNo);
			
			if(users==null)
				return ;
			
			String openId=users.getOpenId();
			
			Map search3=new HashMap<>();
			
			search3.put("[Hidden].GUID=", hidden_GUID);
			
			List<Hidden> list3=(List<Hidden>) hiddenDAO.selectAllHidden(2, 0, null, null, search3).get("rows");
			
			Hidden hidden=list3.get(0);
			
			WeiXinMapper weiXinMapper=sqlSession.getMapper(WeiXinMapper.class);
			
			weixin=weiXinMapper.getCampus(campusId);   	
			accessToken=weixin.getAccessToken();
		
			WxTemplate templateData=new WxTemplate();
	    	templateData.setTouser(openId);
	    	templateData.setTopcolor("#000000");
	    	templateData.setTemplate_id("JqvvyZUkOSWC3FlaFQQNg22zxBqNfFAxwM1-xfg6vxo");
	    	Map<String,TemplateData> m = new HashMap<String,TemplateData>();
	    	TemplateData first = new TemplateData();
	    	first.setColor("#000000");
	    	first.setValue("尊敬的承租人"+charter+" , 您好 : ");
	    	m.put("first", first);
	        
	    	String user=hidden.getUserName();
	    	
	    	TemplateData keyword1 = new TemplateData();
	    	keyword1.setColor("#328392");
	    	keyword1.setValue(user);
	    	m.put("keyword1", keyword1);
	    	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	    	Date date=new Date();
			String time=sdf.format(date);
			TemplateData keyword2 = new TemplateData();
			keyword2.setValue(time);
			m.put("keyword2", keyword2);
			TemplateData keyword3 = new TemplateData();
			keyword3.setValue("限期检查");
			m.put("keyword3", keyword3);
	    	TemplateData keyword4 = new TemplateData();
	    	keyword4.setColor("#328392");
	    	keyword4.setValue("您承租的资产存在"+hidden.getName()+","+
	    			hidden.getDetail()+"的隐患,请注意安全防范!");
	    	m.put("keyword4", keyword4);
	    	templateData.setData(m);
	    	
	    	ChatTemplateProcessor wechatTemplate=new ChatTemplateProcessor();
	    	
	    	String s=wechatTemplate.sendTemplateMessage(accessToken, templateData);
	    	
	    	MessageList messageList=new MessageList();
	    	
	    	messageList.setCampusId(campusId);
	    	messageList.setOpenId(openId);
	    	messageList.setContext("您承租的资产存在"+hidden.getName()+","+
	    			hidden.getDetail()+"的隐患,请注意安全防范!");
	    	messageList.setType("安全通知");
	    	messageList.setSendTime(date);
	    	if(s.equals("消息发送成功")){
	    		messageList.setState(1);
	    	}else{
	    		messageList.setState(0);
	    	}
	    	
	    	MessageListMapper messageListMapper=sqlSession.getMapper(MessageListMapper.class);
	    	
	    	messageListMapper.insertMessageList(messageList);
		 }
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	public void sendMessage(@RequestParam Integer place,@RequestParam String Template_Id,
			@RequestParam String Send_Type,@RequestParam String url,
			@RequestParam String first_data,@RequestParam String keyword1_data,
			@RequestParam String keyword2_data,@RequestParam String keyword3_data,
			@RequestParam String keyword4_data,@RequestParam String remark_data){

		Integer campusId=1;
		
		String accessToken;
    	WeiXin weixin;

	try{
		
		ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring-mybatis2.xml");		
		DefaultSqlSessionFactory defaultSqlSessionFactory= (DefaultSqlSessionFactory) applicationContext.getBean("sqlSessionFactory");				
		SqlSession sqlSession=defaultSqlSessionFactory.openSession();
			
		UsersMapper usersMapper=sqlSession.getMapper(UsersMapper.class);
			
		MessageListMapper messageListMapper=sqlSession.getMapper(MessageListMapper.class);
			
		List<Users> list=usersMapper.getUserByPlace(place);
			
	  if(list!=null){
		
		 Iterator<Users> iterator=list.iterator();
			
	    while (iterator.hasNext()) {
			
		    Users users=iterator.next();

			if(users==null)
				return ;
			
			String openId=users.getOpenId();
						
			WeiXinMapper weiXinMapper=sqlSession.getMapper(WeiXinMapper.class);
			
			weixin=weiXinMapper.getCampus(campusId);   	
			accessToken=weixin.getAccessToken();
		
			WxTemplate templateData=new WxTemplate();
			templateData.setUrl(url);
	    	templateData.setTouser(openId);
	    	templateData.setTopcolor("#000000");
	    	templateData.setTemplate_id(Template_Id);
	    	Map<String,TemplateData> m = new HashMap<String,TemplateData>();
	    	TemplateData first = new TemplateData();
	    	first.setColor("#000000");
	    	first.setValue(first_data);
	    	m.put("first", first);
	        
	    	TemplateData keyword1 = new TemplateData();
	    	keyword1.setColor("#328392");
	    	keyword1.setValue(keyword1_data);
	    	m.put("keyword1", keyword1);
			TemplateData keyword2 = new TemplateData();
			keyword2.setColor("#328392");
			keyword2.setValue(keyword2_data);
			m.put("keyword2", keyword2);
			TemplateData keyword3 = new TemplateData();
			keyword3.setColor("#328392");
			keyword3.setValue(keyword3_data);
			m.put("keyword3", keyword3);
			
			if(keyword4_data!=null&&!keyword4_data.equals("")){
				TemplateData keyword4 = new TemplateData();
				keyword4.setColor("#328392");
				keyword4.setValue(keyword4_data);
				m.put("keyword4", keyword4);
			}
			
			if(remark_data!=null&&remark_data.equals("")){
				TemplateData remark = new TemplateData();
				remark.setColor("#929232");
				remark.setValue(remark_data);
				m.put("remark", remark);
			}
	    	templateData.setData(m);
	    	
	    	ChatTemplateProcessor wechatTemplate=new ChatTemplateProcessor();
	    	
	    	String s=wechatTemplate.sendTemplateMessage(accessToken, templateData);
	    	
	    	MessageList messageList=new MessageList();
	    	
	    	messageList.setCampusId(campusId);
	    	messageList.setOpenId(openId);
	    	messageList.setContext(keyword1_data+","+keyword2_data+
	    			","+keyword3_data+","+keyword4_data);
	    	messageList.setType(Send_Type);
	    	messageList.setSendTime(new Date());
	    	if(s.equals("消息发送成功")){
	    		messageList.setState(1);
	    	}else{
	    		messageList.setState(0);
	    	}
	    	
	    	messageListMapper.insertMessageList(messageList);
		  }
		}
	   }catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
