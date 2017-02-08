<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title>${vo_data.name}</title>
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<!-- <link rel="stylesheet" media="all" href="/assets/css/web/web.css" /> -->

<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/resource.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/live/player/cyberplayer.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
  <div class="page">
    <header class="top-bar">
      <div class="crumb-nav">
        <a href="/api/webchat/index" class="logo icon-logo"></a>
        ${vo_data.name}
      </div>
    </header>

    <div id="id_video_container"></div>

    <div class="resource-intro">
      <div class="icon-title mb-1 white bold clearfix">
        <h5 class="tt bfL">
          <span class="icon icon-detail"></span>直播简介
        </h5>
      </div>

      <div class="intro">
        <p><span class="tt">名称：</span>${vo_data.name}</p>
        <p><span class="tt">日期：</span>${vo_data.sTime}</p>
        <p><span class="tt">简介：</span>${vo_data.desc}</p>
      </div>
    </div>
  </div>

  <input type="hidden" id="baidu_ak" value="${vo_data.baiduAk}" />
  <input type="hidden" id="baidu_url" value="${vo_data.baiduUrl}" />

  <script type="text/javascript">
    var video_width = $('#id_video_container').outerWidth();
    var video_height = $('#id_video_container').outerHeight();
    var baidu_ak=$('#baidu_ak').val();
    var baidu_url=$('#baidu_url').val();
    var player = cyberplayer("id_video_container").setup({
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
  <%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
