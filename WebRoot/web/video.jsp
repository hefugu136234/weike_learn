<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="renderer" content="webkit">
<title>${requestScope.res_data.name}</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="//cdn.bootcss.com/iCheck/1.0.2/skins/all.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/iCheck/1.0.2/icheck.min.js"></script>
<script src="//qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
<script src="/assets/js/web/common.js"></script>
<script src="/assets/js/web/video.js"></script>
<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="partials/top.jsp"></jsp:include>
	<div class="container">
		<ol id="view_line" class="breadcrumb crumb-nav">
			<li><a href="/f/web/index">首页</a></li>
			<c:forEach var="item" items="${requestScope.res_data.menuList}">
				<li><a href="/f/web/category/list/${item.uuid}">${item.name}</a></li>
			</c:forEach>
			<li class="active">${requestScope.res_data.name}</li>
		</ol>

		<div class="resource-title clearfix">
			<div class="tt pull-left">${requestScope.res_data.name}</div>
			<div class="operation pull-right">
				<div class="item star-btn" data-uuid="${requestScope.res_data.uuid}">
					<span
						class="icon ${requestScope.res_data.collectStatus ? 'icon-star':'icon-star-line'}"></span>
					<span class="tt">${requestScope.res_data.collectStatus ? "已收藏":"收藏"}</span>
					<span class="num">${requestScope.res_data.collectCount}</span>
				</div>
				<div class="item like-btn" data-uuid="${requestScope.res_data.uuid}">
					<span class="icon icon-like"></span> <span class="tt">${requestScope.res_data.praiseStatus ? "已赞":"赞"}</span>
					<span class="num">${requestScope.res_data.praiseCount}</span>
				</div>
				<div class="item share-btn">
					<span class="icon icon-share"></span> <span class="tt">分享</span>
					<!-- <span class="num">999</span> -->
				</div>
			</div>
		</div>

		<div class="video-normal-con mt16">
			<div class="row">
				<div class="col-xs-12 col-sm-9 col-md-9 con-pr0">
					<div class="video-container">
						<div class="video" id="video_container"></div>
					</div>
				</div>
				<div class="col-xs-12 col-sm-3 col-md-3 con-pl0">
					<div class="video-info">
						<div class="tag">
							当前播放数：<span id="video_count" class="num">${requestScope.res_data.viewCount}</span>
						</div>
						<div class="intro">
							<div class="tt">视频简介</div>
							<div class="info">
								<p>讲者：${requestScope.res_data.speakerName}</p>
								<p>医院：${requestScope.res_data.hospitalName}</p>
								<p>时长：${requestScope.res_data.videoVo.videoTime}</p>
								<p>名称：${requestScope.res_data.name}</p>
								<p>日期：${requestScope.res_data.dateTime}</p>
							</div>
							<div class="info">简介：${requestScope.res_data.desc}</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 投票区域 -->
		<jsp:include page="/web/partials/resource_vote.jsp"></jsp:include>
		<!-- 投票区域 -->

		<!-- 评论区域 -->
		<jsp:include page="partials/comment.jsp"></jsp:include>
		<!-- 评论区域 -->
	</div>

	<jsp:include page="partials/footer.jsp"></jsp:include>

	<!-- 分享 -->
	<div class="modal fade no-radius share-process-modal"
		id="share_process">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">分享到微信</h4>
				</div>
				<div class="modal-body">
					<div class="con clearfix">
						<div class="code">
							<img src="${requestScope.res_data.qr}" />
						</div>
						<div class="info">用微信“扫一扫”左侧的二维码, 即可把视频分享给您的微信好友或朋友圈。</div>
					</div>
					<div class="process">
						<img src="/assets/img/web/share_process.jpg" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<input type="hidden" id="fileId" value="${requestScope.res_data.videoVo.fileId}" />
	<input type="hidden" id="res_uuid" value="${requestScope.res_data.uuid}" />
	<input type="hidden" id="page_reamin_uuid" value="${page_reamin_uuid}" />
	</body>
</html>
