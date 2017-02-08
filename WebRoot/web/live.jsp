<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="renderer" content="webkit">
<title>${requestScope.broadcast_data.name}</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="/assets/js/web/common.js"></script>
<script src="/assets/js/web/live.js"></script>

<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body class="deep-color-bg">
	<jsp:include page="partials/top.jsp"></jsp:include>

  <div class="live-con-bg" style="background-image: url('${broadcast_data.bgUrl}');">
  	<div class="live-slogen">
  		<div class="container">
  			<ol class="breadcrumb crumb-nav">
  				<li><a href="/f/web/index">首页</a></li>
  				<li><a href="/f/web/activity/list/page">活动</a></li>
  				<li class="active">${broadcast_data.name}</li>
  			</ol>

        <div class="live-main-title mt10"><img src="/assets/img/web/logo_line.png" class="logo-img" />${broadcast_data.name}</div>

  			<div class="slogen-con mt20">
  				<!-- <div class="tt">${requestScope.broadcast_data.name}<span class="tag">${requestScope.broadcast_data.integralVal}</span></div> -->
  				<div class="info">
  					<div class="date">
  						<div>结束时间：${broadcast_data.endDate}</div>
  						<div class="countdown" data-times="${broadcast_data.countDownStart}">
  							<span class="icon icon-countdown"></span><span class="num day-show">0</span>天<span class="num hour-show">0</span>小时<span class="num minute-show">0</span>分<span class="num second-show">0</span>秒
  						</div>
  					</div>
  				</div>
  			</div>

        <div class="live-status-group">
          <div class="status"><span class="icon icon-users"></span>已参加：<span class="current">${broadcast_data.bookNum}</span>/${broadcast_data.limitCountVal}</div>
          <div class="btns">
            <button id="plat_redirect" type="button" class="btn btn-lg btn-orange long">${broadcast_data.buttonVal}</button>
          </div>
        </div>

  		</div>
  	</div>

  	<div class="live-con">
  		<div class="container">
  			<div class="live-introduction">
  				<h4 class="tt">简 介</h4>
  				<div class="info">${requestScope.broadcast_data.description}</div>
  			</div>
  		</div>
  	</div>
  	<input type="hidden" id="broadcast_uuid" value="${requestScope.broadcast_data.uuid}" />
  </div>

	<jsp:include page="partials/footer.jsp"></jsp:include>
</body>
</html>
