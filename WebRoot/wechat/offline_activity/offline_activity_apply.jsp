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
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title>${vo_data.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/offline_activity.css" rel="stylesheet">

<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript" src="/assets/js/wechat/offline_activity/offline_activity_apply.js"></script>
</head>

<body>
	<div class="page blue-bg">
		<div class="offline-activity-tag clearfix">
			<h5 class="tt bfL">活动简介</h5>
			<div class="offline-apply bfR">
				人数：<span class="num">${vo_data.bookNum}</span>/${vo_data.personNum}
			</div>
		</div>
		<div id="offline_apply_div">
			<ul class="offline-apply-list" id="offline_apply_ul">
				<!-- <li>
					<div class="box name">陈潇</div>
					<div class="box date">2016/08/09 16:00</div>
					<div class="box status">报名成功</div>
				</li> -->
			</ul>
		</div>
		<input id="body_modal_uuid" type="hidden" value="${vo_data.uuid}" />
	</div>
</body>
</html>
