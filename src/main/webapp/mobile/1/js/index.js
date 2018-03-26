var campusId=1; //公众号id

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
   }


var code=getQueryString("code");
var state=getQueryString("state");

 $.get("/voucher/oauth/test.do", { 
	 campusId:campusId
   }, function(data) {
	//   alert("data="+data);

  if(data=="false"){
	//   alert("false");
     if(code!=null){   	  
          $.get("/voucher/oauth/getUserInfo.do", {
             code:code,
             state:state,
              campusId:campusId
           }, function(text) {

        	  var obj = $.parseJSON(text);
           
        	 $("#brand").html(obj.campusName);
        	 $(".headimgUrl").attr("src",obj.headimgUrl);
        	 $(".nickName").html(obj.nickName);
        	  
      	    
        	 
        	url=document.location.toString();
        	
        	$.ajax({
			     url : "../../wechat/sign.do",
			     data : {
			    	 campusId:campusId,
			    	 url:url
			     },
			     async: false,
			     type : "GET",
			     success : function(data) {
			    	 var ticket=JSON.parse(data);

			    	 /*
		      	      * 此处需要两次执行相同的函数，否则回调后不能执行以下函数
		      	      */ 
			    	 
			    	 wx.config({
			    		  debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				      		appId: ticket.appId,
				      		timestamp: ticket.timestamp,
				      		nonceStr: ticket.nonceStr,
				      		signature: ticket.signature,
				      		jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
				                            'onMenuShareAppMessage', 'onMenuShareQQ',
				                            'onMenuShareWeibo', 'hideMenuItems',
				                            'showMenuItems', 'hideAllNonBaseMenuItem',
				                            'showAllNonBaseMenuItem', 'translateVoice',
				                            'startRecord', 'stopRecord', 'onRecordEnd',
				                            'playVoice', 'pauseVoice', 'stopVoice',
				                            'uploadVoice', 'downloadVoice', 'chooseImage',
				                            'previewImage', 'uploadImage', 'downloadImage',
				                            'getNetworkType', 'openLocation', 'getLocation',
				                            'hideOptionMenu', 'showOptionMenu', 'closeWindow',
				                            'scanQRCode', 'chooseWXPay',
				                            'openProductSpecificView', 'addCard', 'chooseCard',
				                            'openCard' ]
				      	});
 
			     }
        	});
           
        });
     }else{
  	   location.href=redirectUrl;
     }
    }else{
    	$.get("/voucher/oauth/getUserInfoByOpenId.do",{
 		   campusId:campusId
 	   },function(text) {
       	  var obj = $.parseJSON(text);

       	 $("#brand").html(obj.campusName);
       	 $(".headimgUrl").attr("src",obj.headimgUrl);
     	 $(".nickName").html(obj.nickName);
       	  

 			 url=document.location.toString();

 			 $.ajax({
 			     url : "../../wechat/sign.do",
 			     data : {
 			    	 campusId:campusId,
 			    	 url:url
 			     },
 			     async: false,
 			     type : "GET",
 			     success : function(data) {
 			    	 var ticket=JSON.parse(data);
 		       	     /*
 		     	      * 此处需要两次执行相同的函数，否则返回ture时不能执行以下函数
 		     	      */  
 		       	          
 			    wx.config({
 			   		debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
 			   		appId: ticket.appId,
 			   		timestamp: ticket.timestamp,
 			   		nonceStr: ticket.nonceStr,
 			   		signature: ticket.signature,
 			   		jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
 			                         'onMenuShareAppMessage', 'onMenuShareQQ',
 			                         'onMenuShareWeibo', 'hideMenuItems',
 			                         'showMenuItems', 'hideAllNonBaseMenuItem',
 			                         'showAllNonBaseMenuItem', 'translateVoice',
 			                         'startRecord', 'stopRecord', 'onRecordEnd',
 			                         'playVoice', 'pauseVoice', 'stopVoice',
 			                         'uploadVoice', 'downloadVoice', 'chooseImage',
 			                         'previewImage', 'uploadImage', 'downloadImage',
 			                         'getNetworkType', 'openLocation', 'getLocation',
 			                         'hideOptionMenu', 'showOptionMenu', 'closeWindow',
 			                         'scanQRCode', 'chooseWXPay',
 			                         'openProductSpecificView', 'addCard', 'chooseCard',
 			                         'openCard' ]
 			    	});
 			    	 
 			     }
 			 });
       });
    }
 });


 
 
 wx.ready(function () {

		document.querySelector('#hiddentrouble').onclick =function(){
			 // 2. 分享接口
         wx.getLocation({
            success : function(res) {
                 // alert(JSON.stringify(res));
                 var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                 // $("#latitude").val(latitude);
                 var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                 // $("#longitude").val(longitude);
                 var speed = res.speed; // 速度，以米/每秒计
                 // $("#speed").val(speed);
                 var accuracy = res.accuracy; // 位置精度
                 // $("#accuracy").val(accuracy);
                 
                 $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
                	 longitude:longitude,
                	 latitude:latitude               	 
                 },function(text){
                	 var obj = $.parseJSON(text);
                	 var result=obj.result;
                	 var lat=result[0].y;
                	 var lng=result[0].x;
                	 location.href="hidden/hiddenSearch.html?latitude="+lat+"&longitude="+lng;
                 });
              
            },
             cancel : function(res) {
                 alert('用户拒绝授权获取地理位置');
             }
         });
		 }
		
		document.querySelector('#addhiddenCheck').onclick =function(){
			wx.getLocation({
	               success : function(res) {
	                    // alert(JSON.stringify(res));
	                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	                    // $("#latitude").val(latitude);
	                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	                    // $("#longitude").val(longitude);
	                    var speed = res.speed; // 速度，以米/每秒计
	                    // $("#speed").val(speed);
	                    var accuracy = res.accuracy; // 位置精度
	                    // $("#accuracy").val(accuracy);
	                    
	                    $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
	                   	 longitude:longitude,
	                   	 latitude:latitude               	 
	                    },function(text){
	                   	 var obj = $.parseJSON(text);
	                   	 var result=obj.result;
	                   	 var lat=result[0].y;
	                   	 var lng=result[0].x;
	                   	 location.href="guidance/addCheckInfoList.html?latitude="+lat+"&longitude="+lng;
	                    });
	                    
	               },
	                cancel : function(res) {
	                    alert('用户拒绝授权获取地理位置');
	                }
	            });
		 }
		
		document.querySelector('#hiddenCheck').onclick =function(){
			wx.getLocation({
	               success : function(res) {
	                    // alert(JSON.stringify(res));
	                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	                    // $("#latitude").val(latitude);
	                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	                    // $("#longitude").val(longitude);
	                    var speed = res.speed; // 速度，以米/每秒计
	                    // $("#speed").val(speed);
	                    var accuracy = res.accuracy; // 位置精度
	                    // $("#accuracy").val(accuracy);
	                    
	                    $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
	                   	 longitude:longitude,
	                   	 latitude:latitude               	 
	                    },function(text){
	                   	 var obj = $.parseJSON(text);
	                   	 var result=obj.result;
	                   	 var lat=result[0].y;
	                   	 var lng=result[0].x;
	                   	 location.href="guidance/hiddenCheckList.html?latitude="+lat+"&longitude="+lng;
	                    });
	                    
	               },
	                cancel : function(res) {
	                    alert('用户拒绝授权获取地理位置');
	                }
	            });
		 }
		
		document.querySelector('#hiddenNeaten').onclick =function(){
			wx.getLocation({
	               success : function(res) {
	                    // alert(JSON.stringify(res));
	                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	                    // $("#latitude").val(latitude);
	                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	                    // $("#longitude").val(longitude);
	                    var speed = res.speed; // 速度，以米/每秒计
	                    // $("#speed").val(speed);
	                    var accuracy = res.accuracy; // 位置精度
	                    // $("#accuracy").val(accuracy);
	                    
	                    $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
	                   	 longitude:longitude,
	                   	 latitude:latitude               	 
	                    },function(text){
	                   	 var obj = $.parseJSON(text);
	                   	 var result=obj.result;
	                   	 var lat=result[0].y;
	                   	 var lng=result[0].x;
	                   	 location.href="guidance/hiddenNeatenList.html?latitude="+lat+"&longitude="+lng;
	                    });
	                    
	               },
	                cancel : function(res) {
	                    alert('用户拒绝授权获取地理位置');
	                }
	            });
		 }
		
		document.querySelector('#safeLike').onclick =function(){
			 location.href="safety/safeLike.html";
		 }
		
		document.querySelector('#report').onclick =function(){
			wx.getLocation({
	               success : function(res) {
	                    // alert(JSON.stringify(res));
	                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	                    // $("#latitude").val(latitude);
	                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	                    // $("#longitude").val(longitude);
	                    var speed = res.speed; // 速度，以米/每秒计
	                    // $("#speed").val(speed);
	                    var accuracy = res.accuracy; // 位置精度
	                    // $("#accuracy").val(accuracy);
	                    
	                    $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
	                   	 longitude:longitude,
	                   	 latitude:latitude               	 
	                    },function(text){
	                   	 var obj = $.parseJSON(text);
	                   	 var result=obj.result;
	                   	 var lat=result[0].y;
	                   	 var lng=result[0].x;
	                   	 location.href="safety/emergency.html?latitude="+lat+"&longitude="+lng;
	                    });
	                    
	               },
	                cancel : function(res) {
	                    alert('用户拒绝授权获取地理位置');
	                }
	            });
		 }
		 
		 
		document.querySelector('#userSetting').onclick =function(){
			 location.href="userSetting.html";
		 }
		
		
		document.querySelector('#allAsset').onclick =function(){
			wx.getLocation({
	               success : function(res) {
	                    // alert(JSON.stringify(res));
	                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	                    // $("#latitude").val(latitude);
	                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	                    // $("#longitude").val(longitude);
	                    var speed = res.speed; // 速度，以米/每秒计
	                    // $("#speed").val(speed);
	                    var accuracy = res.accuracy; // 位置精度
	                    // $("#accuracy").val(accuracy);
	                    
	                    $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
		                   	 longitude:longitude,
		                   	 latitude:latitude               	 
		                    },function(text){
		                   	 var obj = $.parseJSON(text);
		                   	 var result=obj.result;
		                   	 var lat=result[0].y;
		                   	 var lng=result[0].x;
		                   	 location.href="../assetAdmin/allAsset.html?latitude="+lat+"&longitude="+lng;
		                 });
	                    
	                 },
	                cancel : function(res) {
	                    alert('用户拒绝授权获取地理位置');
	                }
	            });
		 }
		
		 document.querySelector('#photo').onclick = function () {
 		  wx.getLocation({
	               success : function(res) {
	                    // alert(JSON.stringify(res));
	                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	                    // $("#latitude").val(latitude);
	                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	                    // $("#longitude").val(longitude);
	                    var speed = res.speed; // 速度，以米/每秒计
	                    // $("#speed").val(speed);
	                    var accuracy = res.accuracy; // 位置精度
	                    // $("#accuracy").val(accuracy);
	                    
	                    $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
		                   	 longitude:longitude,
		                   	 latitude:latitude               	 
		                    },function(text){
		                   	 var obj = $.parseJSON(text);
		                   	 var result=obj.result;
		                   	 var lat=result[0].y;
		                   	 var lng=result[0].x;
		                   	 location.href="safety/photo.html?latitude="+lat+"&longitude="+lng;
		                 });
	                    
	                 },
	                cancel : function(res) {
	                    alert('用户拒绝授权获取地理位置');
	                }
	            });
 	  }
		
	
		
  document.querySelector('#map').onclick = function () {
    // 2. 分享接口
    wx.getLocation({
         success : function(res) {
            // alert(JSON.stringify(res));
            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
            // $("#latitude").val(latitude);
            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
            // $("#longitude").val(longitude);
            var speed = res.speed; // 速度，以米/每秒计
            // $("#speed").val(speed);
            var accuracy = res.accuracy; // 位置精度
            // $("#accuracy").val(accuracy);
            
            $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
              	 longitude:longitude,
              	 latitude:latitude               	 
               },function(text){
              	 var obj = $.parseJSON(text);
              	 var result=obj.result;
              	 var lat=result[0].y;
              	 var lng=result[0].x;
              	 location.href="map.html?latitude="+lat+"&longitude="+lng;
            });
            
        },
        cancel : function(res) {
     	   console.log('用户拒绝授权获取地理位置');
        }
    });
  }
  
  document.querySelector('#assetMap').onclick = function () {
	    // 2. 分享接口
	    wx.getLocation({
	         success : function(res) {
	            // alert(JSON.stringify(res));
	            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	            // $("#latitude").val(latitude);
	            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	            // $("#longitude").val(longitude);
	            var speed = res.speed; // 速度，以米/每秒计
	            // $("#speed").val(speed);
	            var accuracy = res.accuracy; // 位置精度
	            // $("#accuracy").val(accuracy);
	            
	            $.get("/voucher/mobile/map/baiduSwitch.do",{ //微信地理位置坐标转换成百度地图坐标
	              	 longitude:longitude,
	              	 latitude:latitude               	 
	               },function(text){
	              	 var obj = $.parseJSON(text);
	              	 var result=obj.result;
	              	 var lat=result[0].y;
	              	 var lng=result[0].x;
	              	 location.href="../assetAdmin/assetMap.html?latitude="+lat+"&longitude="+lng;
	            });
	            
	        },
	        cancel : function(res) {
	     	   console.log('用户拒绝授权获取地理位置');
	        }
	    });
	  } 
	
  	document.querySelector('#hiddenStat').onclick = function () {
  			location.href="statistical/hiddenStat.html";
  	}
  	
  	document.querySelector('#asseStat').onclick = function () {
			location.href="statistical/asseStat.html";
	}
  
  	document.querySelector('#hiddenCheckMap').onclick = function () {
		location.href="guidance/hiddenCheckMap.html";
  	}
  	
	 // 2. 分享接口
	  // 2.1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
	    wx.onMenuShareAppMessage({
	      title: '互联网之子',
	      desc: '在长大的过程中，我才慢慢发现，我身边的所有事，别人跟我说的所有事，那些所谓本来如此，注定如此的事，它们其实没有非得如此，事情是可以改变的。更重要的是，有些事既然错了，那就该做出改变。',
	      link: 'http://movie.douban.com/subject/25785114/',
	      imgUrl: 'http://img3.douban.com/view/movie_poster_cover/spst/public/p2166127561.jpg',
	      trigger: function (res) {
	        alert('用户点击发送给朋友');
	      },
	      success: function (res) {
	        alert('已分享');
	      },
	      cancel: function (res) {
	        alert('已取消');
	      },
	      fail: function (res) {
	        alert(JSON.stringify(res));
	      }
	    });

	  
	  
	  
	 }); //wx.ready
