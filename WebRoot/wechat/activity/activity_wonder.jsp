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
<title>热门活动</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<link href="/assets/css/app/activity.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/activity/activity_wonder.js"></script>
</head>

<body>
	<div class="page">
		<div class="content pb50">
			<div class="tab-control">
				<ul class="tab-control-tag select-tag circle">
					<li class="btn active">线上活动</li>
					<li class="btn">线下活动</li>
				</ul>
				<div class="tab-content active">
					<c:choose>
						<c:when test="${vo_data.resCount gt 0}">
							<div id="activity_list_div">
								<div id="activity_list_ul" class="activity-list"></div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="empty-tips-area show">
								<p>
									<img src="/assets/img/app/icon_empty.png" width="18%">
								</p>
								<p>内容暂无</p>
								<p>去其他栏目看看吧</p>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
				<div class="tab-content">
					<div id="offline_activity_div">
						<div id="offline_activity_ul" class="activity-list"></div>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="/wechat/common/foot.jsp"></jsp:include>
	</div>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
