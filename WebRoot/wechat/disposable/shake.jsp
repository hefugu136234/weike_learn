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
    <title>摇一摇 神奇的知了云盒带回家</title>
  </head>

  <body>
    <div class="page shakeAward">
      <div class="content index" id="envelopes_index">
        <div class="logo"><a href="/api/webchat/index"><img src="/assets/img/app/shakeAward/logo.png" width="100%"></a></div>
        <div class="title active"><img src="/assets/img/app/shakeAward/title.png" width="86%"></div>
        <div class="tag"><img src="/assets/img/app/shakeAward/tag.png" width="96%"></div>
        <div class="gift_box"><img src="/assets/img/app/shakeAward/gift_box.png" width="60%"></div>
      </div>

      <div class="content result hide" id="envelopes_result">
        <div class="logo"><a href="/api/webchat/index"><img src="/assets/img/app/shakeAward/logo.png" width="100%"></a></div>
        <div class="tips"><img src="/assets/img/app/shakeAward/tt_tag.png" width="91%"><p class="tt" id="envelopes_tips"></p></div>

        <div class="gift_box hide" id="winning"><img src="/assets/img/app/shakeAward/winning.png" width="100%"><img src="/assets/img/app/shakeAward/winning_img.png" width="100%" class="img"></div>
        <div class="gift_box" id="winning_no"><img src="/assets/img/app/shakeAward/winning_no.png" width="100%"><img src="/assets/img/app/shakeAward/winning_img_no.png" width="100%" class="img"></div>

        <div class="float_intro" id="envelopes_intro">
        	<h5 class="tt">您知道吗？</h5>
        	<div id="float_intro_div" class="info"></div>
        </div>
      </div>

      <div class="btn_group clearfix">
        <div class="btn" id="btn_rule">活动规则</div>
        <div class="btn" id="btn_record">抽奖记录</div>
      </div>

      <div class="float_info" id="info_rule">
        <h5 class="tt">活动规则</h5>
        <div class="info">
          <p>活动时间：</p>
          <p>4.21 00:00-4.23 23:59</p>
          <p>参与方式：</p>
          <p>摇一摇，就有机会获把知了云盒带回家！</p>
          <p>每人共有三次机会。</p>
        </div>
        <div class="close">╳</div>
      </div>
      <div class="float_info" id="info_record">
        <h5 class="tt">抽奖纪录</h5>
        <ul id="record_list_ul" class="record_list"></ul>
        <div class="close">╳</div>
      </div>

    </div>

    <audio id="bg_music" preload="metadata" controls autoplay="false"></audio>

    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/shake.js"></script>
    <script type="text/javascript">
      $(document).ready(function(){
        var myShakeEvent = new Shake({
          threshold: 10, // optional shake strength threshold
          timeout: 1000 // optional, determines the frequency of event generation
        });

        var bgMedia = $('#bg_music');
        var bgMusic = bgMedia.attr('src');
        var firstTime = true;

        myShakeEvent.start();

        window.addEventListener('shake', shakeEventDidOccur, false);

        function shakeEventDidOccur(){
          if (!firstTime){
            $('#envelopes_index').fadeIn();
            $('#envelopes_result').fadeOut();
          }

        	//摇完之后出现的效果
          if(!bgMusic){
            bgMedia.attr('src', '/assets/wechat.mp3');
          }

          setTimeout(function(){
        	  bgMedia.play();
          }, 1000);

          //请求数据
          $.getJSON('/api/webchat/game/yao/action?timestamp='+new Date().getTime(),function(data){
        	  console.log(data);
          	var envelopes_tips=$('#envelopes_tips');
          	var envelopes_intro=$('#envelopes_intro');
          	var code=data.code;
          	var winning=$('#winning');
      		  var winning_no=$('#winning_no');
          	if(code==0){
          		var money=data.money;
          		var result;
          		if(money>0){
          			 result='恭喜您，获得知了云盒一台，还有'+data.times+'次机会！';
          			 winning.show();
          			 winning_no.hide();
          		}else{
          			 result='很遗憾，未抽中，还有'+data.times+'次机会！';
          			 winning.hide();
         			 winning_no.show();
          		}
          		envelopes_tips.html(result);
            	}else if(code==1){
            		envelopes_tips.html('您已经没有抽奖机会了');
            		 winning.hide();
         			 winning_no.show();
            	}else if(code==2){
            		envelopes_tips.html('活动还未开始');
            		 winning.hide();
         			 winning_no.show();
            	}else if(code==3){
            		envelopes_tips.html('活动已经结束');
            		 winning.hide();
         			 winning_no.show();
            	}
          	
          	//随机显示内容
          	var float_intro_div=$('#float_intro_div');
          	var content=['知了盒子带回家，让它成为您的客厅教授','知了云盒已经有100多个外科视频啦','2016播客秀已经整合升级到知了云盒啦！','知了云盒是一个可以随时随地进行收看的医学可视化学习分享平台','您可以通过手机微信公众号或通过知了盒子在电视上收看知了云盒学术内容哦！'];
          	var randomNum = Math.round(Math.random() * 5)%5
          	float_intro_div.html(content[randomNum]);
          	envelopes_intro.show();

              $('#envelopes_index').find('.title').removeClass('active');
              $('#envelopes_index').find('.gift_box').addClass('active');

              setTimeout(function(){
                $('#envelopes_index').fadeOut();
                $('#envelopes_result').fadeIn();
              }, 1000);

              firstTime = false;
          });

        }

        $('#btn_rule').click(function(){
           $('.float_info').fadeOut();
          $('#info_rule').fadeIn();
        });

        $('#btn_record').click(function(){
          var record_list_ul = $('#record_list_ul');
          $.getJSON(
            '/api/webchat/game/yao/record?timestamp='+new Date().getTime(),
            function(data){
              if(data.status=='success'){
                record_list_ul.empty();
                var record_list=data.list;
                $.each(record_list,function(index,item){
                  var money=item.money;
                  if(money>0){
                	  money='抽中“云盒”';
                  }else{
                	  money="未中奖";
                  }
                  var date=timeStamp2String(item.date);
                  var li='<li><span class="date">'+date+'</span>'+money+'</li>';
                  record_list_ul.append(li);
                });
              }
              $('.float_info').fadeOut();
              $('#info_record').fadeIn();
            }
          );
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
        return repairZero(year) + "-" + repairZero(month) + "-" + repairZero(date)+" "+repairZero(hour)+":"+repairZero(minute)+":"+repairZero(second);
      }

      function repairZero(num){
    		if(num<10){
    			return num="0"+num;
    		}
    		return num;
    	}
    </script>

    <script type="text/javascript">
      $(function() {
        var base_url=location.href.split('#')[0];
        $.getJSON('/api/webchat/wx/share/config', {
          url : base_url,
          timestamp : new Date().getTime()
        }, function(data_sin) {
          if (data_sin.status == 'success') {
            var title='摇一摇';
            var desc="摇一摇，有机会获得知了云盒";
            var cover='http://www.z-box.com.cn/assets/img/webchat/game/yao_share.jpg';
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
