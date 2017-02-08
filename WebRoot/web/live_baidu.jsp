<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <meta http-equiv="X-UA-Compatible" content="IE=9">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>${vo_data.name}</title>
    <link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
    <link rel="stylesheet" media="all" href="/assets/css/web/web.css" />
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/assets/live/player/cyberplayer.js"></script>
</head>

<body>
    <nav class="navbar top-bar" id="top_bar">
      <div class="container">
        <ul class="nav navbar-nav nav-menu">
          <li class="logo">
            <a href="javascript:;">
              <img src="/assets/img/web/logo.png" class="icon" />
            </a>
          </li>
          <li id="nav_index">
            <a href="javascript:;">${vo_data.name}</a>
          </li>
        </ul>
      </div>
    </nav>

    <div class="container">
      <div class="video-normal-con baidu-live-container mt16">
        <div class="row">
          <div class="col-xs-12 col-sm-12 col-md-9 con-pr0 baidu-live-left">
            <div class="video-container">
              <div class="video" id="playercontainer"></div>
            </div>
          </div>
          <div class="col-xs-12 col-sm-12 col-md-3 con-pl0 baidu-live-right">
            <div class="video-info">
              <div class="intro">
                <div class="tt">直播简介</div>
                <div class="info">
                  <p>名称：${vo_data.name}</p>
                  <p>日期：${vo_data.sTime}</p>
                </div>
                <div class="info">简介：${vo_data.desc}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <footer class="footer">
      <div class="container">
        <div class="copyright">© 2016 <b>Lankr.</b> All Rights Reserved. <a href="http://www.miitbeian.gov.cn">沪ICP备14039441号-10</a></div>
      </div>
    </footer>
    <input type="hidden" id="baidu_ak" value="${vo_data.baiduAk}" />
	<input type="hidden" id="baidu_url" value="${vo_data.baiduUrl}" />
	
    <script type="text/javascript">
      var video_width = $('#playercontainer').outerWidth();
      var video_height = $('#playercontainer').outerHeight();
      var baidu_ak=$('#baidu_ak').val();
      var baidu_url=$('#baidu_url').val();
      var player = cyberplayer("playercontainer").setup({
          width: video_width,
          height: video_height,
          stretching: "uniform",
          //file: "http://ggdnpjjganyep8pag14.exp.bcelive.com/lss-gk3ay1un5380cxrm/live.m3u8",
          file: baidu_url,
          autostart: true,
          repeat: false,
          volume: 100,
          controls: true,
          ak: baidu_ak // 公有云平台注册即可获得accessKey
      });
    </script>
    <!-- 加入分享 -->
</body>
</html>
