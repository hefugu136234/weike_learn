<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="template language" name="keywords" />
<meta content="author" name="author" />
<meta
  content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"
  name="viewport" />
<meta content="yes" name="mobile-web-app-capable" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="telephone=no" name="format-detection" />
<meta content="email=no" name="format-detection" />
<meta name="full-screen" content="yes">
<meta name="x5-fullscreen" content="true">
<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
<title>授权登录</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css">
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/users.css" rel="stylesheet">
<link href="/assets/css/app/reg.css" rel="stylesheet">
</head>

<body>
  <div class="page">
    <div class="content">
      <header class="top-bar">
        <div class="crumb-nav without-link">用户登录</div>
      </header>
      <div class="tv-login-user">
        <div class="photo"><img src="${showInfo.photo}" alt="用户头像"></div>
        <div class="name">${showInfo.showName}</div>
      </div>

      <div class="tv-login-info">
        <p>即将用此用户信息登录</p>
        <!-- <p><a href="/api/webchat/qr/auth/changeUser">切换用户</a></p> -->
      </div>

      <div class="p10">
        <input type="button" class="button btn-full btn-cyan btn-radius" id="comfirm_but" value="确认授权"/>
        <a href="javascript:;" id="cancel_btn" class="button btn-full btn-cyan-line btn-radius mt10">取消</a>
      </div>
    </div>
  </div>

  <div class="page" id="reg_banner_page">
    <div class="content">
      <div class="swiper-container reg-banner" id="reg_banner">
        <ul class="swiper-wrapper">
          <!-- <li class="swiper-slide bg1">
            <div class="tt">
              <h5>/ 方便快捷 /</h5>
              <p>移动医学教育平台</p>
            </div>
            <div class="bottom-area">
              <div class="info">
                <p><span class="num">7</span>大学科；</p>
                <p>超过<span class="num">500</span>个医学课件</p>
              </div>
            </div>
          </li> -->
          <li class="swiper-slide bg2">
            <div class="tt">
              <h5>/ 内容丰富 /</h5>
              <p>知了微课等你来加入</p>
            </div>
            <div class="bottom-area">
              <div class="btn" id="close_reg_banner">立即体验</div>
            </div>
          </li>
        </ul>
        <!-- <div class="swiper-pagination" id="reg_banner_pagination"></div> -->
      </div>
    </div>
  </div>

  <div class="feed-back-modal" id="vip_alert">
    <div class="cover"></div>
    <div class="alert">
      <h5 class="tt"></h5>
      <div class="con" id="alert_info"></div>
      <div class="btns">
        <div class="btn cancel">取消</div>
        <a id="go_vip" href="javascript:void(0);" class="btn sub">VIP激活</a>
      </div>
    </div>
  </div>

  <script type="text/javascript" src="/assets/js/jquery.js"></script>
  <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
  <script type="text/javascript" src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
  <script type="text/javascript">
  $(function(){
	  var userAgent = navigator.userAgent;
    var ua = userAgent.toLowerCase();
    var Wechat = ua.match(/MicroMessenger/i) == 'micromessenger';

    if (Wechat) {
      $('#cancel_btn').click(function(){
        wx.closeWindow();
      });
    } else {
      $('#cancel_btn').click(function(){
        history.go(-1);
        //window.open("about:blank","_self").close();
      });
    }

    $('#close_reg_banner').click(function(){
      $('#reg_banner_page').fadeOut();
    });

    // var reg_banner_swiper = new Swiper("#reg_banner", {
    //   pagination: "#reg_banner_pagination",
    //   paginationClickable: true,
    //   loop: true
    // });/api/webchat/active/page/code
    $('#go_vip').click(function(){
    	$.post('/api/webchat/tv/auth/go/vip',function(data){
    		if(data.status=='success'){
    			 location.href = "/api/webchat/active/page/code";
    		}else{
    			alert("用户不存在，请重新扫码");
    		}
    	});
    });

    // 弹框关闭
    $('.feed-back-modal').find('.btn.cancel, .btn.sub, .cover').click(function() {
      $(this).parents('.feed-back-modal').fadeOut().removeClass('active');
    });

    $('#comfirm_but').click(function(event){
		  event.preventDefault();
		  $this=$(this);
		  $.post('/api/webchat/tv/comfim/auth',{

			  },function(data){
				  if(data.status=='success'){
					  location.href = "/api/webchat/tv/auth/success";
				  }else{
					  if(data.code==-2010){
						  $('#alert_info').html("您还未激活vip，请前往激活");
			              $('#vip_alert').fadeIn().addClass('active');
			             return false;
					  }
					  if(!!data.message){
						  alert(data.message);
					  }else{
						  alert('数据出错');
					  }
				  }
			  });
	  });
  });
  </script>
</body>
</html>
