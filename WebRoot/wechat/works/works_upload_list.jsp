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
<title>征稿上传</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<link href="/assets/css/app/activity.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/works/works_upload_list.js"></script>
</head>

<body>
	<div class="page">
		<div class="content">
			<!-- <header class="top-bar">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          征稿上传
        </div>
      </header> -->

			<div class="icon-title clearfix">
				<h5 class="tt bfL">
					<span class="icon icon-upload"></span>普通作品上传通道
				</h5>
			</div>
			<div class="activity-list line">
				<a href="/api/webchat/activity/common/opus/page" class="box">
					<div class="img">
						<img src="/assets/img/app/activity/up_normal.jpg">
					</div>
					<div class="info-tag">
						<h5 class="tt">普通作品上传入口</h5>
						<p>汇聚小亮点，融合大智慧。分享典型病例手术操作，更有机会获得领域顶尖专家解析。活动火热进行中，等你来加入！</p>
						<span class="icon icon-arr-r"></span>
					</div>
				</a>
			</div>

			<div class="icon-title mt8 clearfix">
				<h5 class="tt bfL">
					<span class="icon icon-speaker"></span>活动作品上传通道
				</h5>
			</div>
			<div id="activity_list_div">
				<div id="activity_list_ul" class="activity-list"></div>
			</div>
		</div>
	</div>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
