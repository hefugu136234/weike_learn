<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/disposable.css" rel="stylesheet">
    <title>知了闹元宵</title>
  </head>

  <body>
    <div class="page envelopes">
      <div class="content index" id="envelopes_index">
        <div class="logo"><img src="/assets/img/app/envelopes/logo.png" width="100%"></div>
        <div class="title"><img src="/assets/img/app/envelopes/title.png" width="74%"></div>
      </div>

      <div class="content result" id="envelopes_result">
        <div class="logo"><img src="/assets/img/app/envelopes/logo.png" width="100%"></div>
        <div class="title"><img src="/assets/img/app/envelopes/title.png" width="74%"></div>
        <div id="result_money" class="result_tag"></div>
      </div>

      <div class="start_img" id="start_envelopes"><img src="/assets/img/app/envelopes/start_img.png" width="51%"></div>

      <div class="tips" id="envelopes_tips">今天还有<span id="last_chance">${requestScope.num}</span>次机会</div>

      <div class="float_intro" id="envelopes_intro">
        <h5 class="tt">权威麻醉专家，坐镇知了微课！</h5>
        <div class="info">
          <p></p>
        </div>
      </div>

      <div class="btn_group clearfix">
        <div class="btn" id="btn_rule"><img src="/assets/img/app/envelopes/btn_rule.png" width="100%"></div>
        <div class="btn" id="btn_record"><img src="/assets/img/app/envelopes/btn_record.png" width="100%"></div>
      </div>

      <div class="float_info" id="info_rule">
        <h5 class="tt">活动规则</h5>
        <div class="info">
          <p>活动时间：</p>
          <p>元宵节期间（2月22日-2月26日）</p>
          <p>参与方式：</p>
          <p>每日进入知了云盒，参与抽奖，即有机会获得10-50元话费</p>
          <p>每日3次抽奖机会</p>
        </div>
        <div class="close">╳</div>
      </div>
      <div class="float_info" id="info_record">
        <h5 class="tt">抽奖纪录</h5>
        <ul id="record_list_ul" class="record_list"></ul>
        <div class="close">╳</div>
      </div>
    </div>

    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript">
      $(document).ready(function(){
        $('#start_envelopes').click(function(){
          //请求数据
          $.getJSON('/api/webchat/yuan/xiao/chou/jiang?timestamp='+new Date().getTime(),function(data){
          	var code=data.code;
          	if(code==0){
          		var money=parseFloat(data.money).toFixed(1);
          		var result;
          		if(money>0){
          			//抽中
          			 result='恭喜您，获得<span id="hua_fei" class="num">'+money+'</span>元话费';
          		}else{
          			//未抽中
          			 result='很遗憾，您未抽中';
          		}
          		$('#result_money').html(result);
          		  $('#envelopes_index').hide();
                $('#envelopes_result').show();
                $('#result_money').fadeOut();
                $('#result_money').fadeIn();

                if (data.times == 0) {
                  $('#start_envelopes').hide();
                  $('#envelopes_tips').hide();
                  var intro_arr = [
                    '>课题：靶控输注丙泊酚静脉麻醉的快速指南',
                    '>课题：Marsh VS Schnider',
                    '>更多精彩内容即将上线，敬请期待。'
                  ];
                  var randomNum = Math.round(Math.random() * 3)%3;

                  $('#envelopes_intro > .info').find('p').html(intro_arr[randomNum]);
                  $('#envelopes_intro').show();
                }
            	}else if(code==1){
            		alert('您今天已经没有抽奖机会');
            	}else if(code==2){
            		alert('活动还未开始');
            	}else if(code==3){
            		alert('活动已结束');
            	}
            	$('#last_chance').text(data.times);
          });
        });

        $('#envelopes_intro').click(function(){
          window.location.href='/api/webchat/index';
        });

        $('#btn_rule').click(function(){
          $('.float_info').fadeOut();
          $('#info_rule').fadeIn();
        });

        $('#btn_record').click(function(){
        var record_list_ul=$('#record_list_ul');
        $.getJSON('/api/webchat/yuan/xiao/record?timestamp='+new Date().getTime(),function(data){
        	console.log(data)
        	if(data.status=='success'){
        		record_list_ul.empty();
        		var record_list=data.list;
        		$.each(record_list,function(index,item){
        			var money=parseFloat(item.money).toFixed(1);
        			var date=timeStamp2String(item.date);
        			var li='<li><span class="date">'+date+'</span>话费'+money+'元</li>';
        			record_list_ul.append(li);
        		});
        	}
        	$('.float_info').fadeOut();
            $('#info_record').fadeIn();
        });

        });

        $('.float_info').find('.close').click(function(){
          $(this).parents('.float_info').fadeOut();
        });
      });

      function timeStamp2String (time){
          var datetime = new Date();
           datetime.setTime(time);
           var year = datetime.getFullYear();
           var month = datetime.getMonth() + 1;
           var date = datetime.getDate();
           var hour = datetime.getHours();
           var minute = datetime.getMinutes();
           var second = datetime.getSeconds();
           return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
  }
    </script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript">
      $(function() {
        var base_url=location.href.split('#')[0];
    		$.getJSON('/api/webchat/wx/share/config', {
    			url : base_url,
    			timestamp : new Date().getTime()
    		}, function(data_sin) {
    			if (data_sin.status == 'success') {
    				var title='知了闹元宵';
    				var desc="知了闹元宵，送话费，跟家人多联系。";
    				var cover='http://www.z-box.com.cn/assets/img/app/envelopes/start_img.png';
    				initWx(data_sin,title,desc,data_sin.url,cover);
    			}
    		});
      });

    //微信的分享的公共方法
      function initWx(data,title,desc,link,imgUrl) {
      	wx.config({
      		debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
      		appId : data.appId,
      		timestamp : data.timestamp,
      		nonceStr : data.nonceStr,
      		signature : data.signature,
      		jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage',
      				'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone','chooseImage', 'previewImage', 'uploadImage', 'downloadImage' ]
      	});

      	wx.ready(function() {
      		// 分享到朋友圈
      		wx.onMenuShareTimeline({
      			title : title, // 分享标题
      			link : link, // 分享链接
      			imgUrl : imgUrl, // 分享图标
      			desc : desc,
      			success : function() {
      				// 用户确认分享后执行的回调函数
      				shareRes();
      			},
      			cancel : function() {
      				// 用户取消分享后执行的回调函数
      			}
      		});

      		// 分享给朋友
      		wx.onMenuShareAppMessage({
      			title : title, // 分享标题
      			desc : desc, // 分享描述
      			link : link, // 分享链接
      			imgUrl : imgUrl, // 分享图标
      			type : 'link', // 分享类型,music、video或link，不填默认为link
      			dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
      			success : function() {
      				// 用户确认分享后执行的回调函数
      				shareRes();
      			},
      			cancel : function() {
      				// 用户取消分享后执行的回调函数
      			}
      		});

      		wx.onMenuShareWeibo({
      			title : title, // 分享标题
      			desc : desc, // 分享描述
      			link : link, // 分享链接
      			imgUrl : imgUrl, // 分享图标
      			success : function() {
      				// 用户确认分享后执行的回调函数
      				shareRes();
      			},
      			cancel : function() {
      				// 用户取消分享后执行的回调函数
      			}
      		});

      		wx.onMenuShareQZone({
      			title : title, // 分享标题
      			desc : desc, // 分享描述
      			link : link, // 分享链接
      			imgUrl : imgUrl, // 分享图标
      			success : function() {
      				// 用户确认分享后执行的回调函数
      				shareRes();
      			},
      			cancel : function() {
      				// 用户取消分享后执行的回调函数
      			}
      		});

      		wx.onMenuShareQQ({
      			title : title, // 分享标题
      			desc : desc, // 分享描述
      			link : link, // 分享链接
      			imgUrl : imgUrl, // 分享图标
      			success : function() {
      				// 用户确认分享后执行的回调函数
      				shareRes();
      			},
      			cancel : function() {
      				// 用户取消分享后执行的回调函数
      			}
      		});

      	});

      	wx.error(function(res) {
      	});
      }

      //记录后台分享的资源
      function shareRes(){
    	  var base_url=location.href.split('#')[0];
      	$.post('/api/webchat/common/share/page',
      			{'url':base_url},
      			function(data){
      				console.log(data);
      			});
      }

    </script>
  </body>
</html>
