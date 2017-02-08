<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="renderer" content="webkit">
<title>${vo_data.name}</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="/assets/js/web/common.js"></script>
<script src="/assets/js/web/offline_activity/detail.js"></script>

<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="/web/partials/top.jsp"></jsp:include>
	<div class="activity-slogen offline">
		<div class="container">
			<ol class="breadcrumb crumb-nav">
				<li><a href="/f/web/index">首页</a></li>
				<li><a href="/f/web/activity/list/page">活动</a></li>
				<li class="active">${vo_data.name}</li>
			</ol>

			<div class="media slogen-con">
				<div class="media-left img">
					<img class="media-object" src="${not empty vo_data.webcover?vo_data.webcover:'/assets/img/web/placeholder.jpg'}"
						alt="activity slogen">
				</div>
				<div class="media-body info">
					<h4 class="media-heading tt">${vo_data.name}</h4>
					<div class="offline-activity-desc">
						<div class="status clearfix">
							<div class="box">
								费用：<span class="num price">${vo_data.priceShow}</span>
							</div>
							<div class="box">
								人数：<span class="num">${vo_data.bookNum}</span>/${vo_data.personNum}
							</div>
							<div class="box">地点：${vo_data.address}</div>
						</div>
						<div class="date">报名时间：${vo_data.bookTimeShow}</div>
					</div>
					<div class="offline-activity-btns clearfix">
					<c:if test="${vo_data.initiatorFlag}">
						<button type="button" id="book_detail"
							class="btn btn-lg btn-bd-orange">报名详情</button>
						<button type="button" data-toggle="modal"
							data-target="#share_process" class="btn btn-lg btn-bd-green">邀请好友</button>
					</c:if>
					<c:if test="${!vo_data.initiatorFlag && !vo_data.bookFlag}">
						<a href="javascript:;" id="go_book" class="btn btn-lg btn-orange btn-long">立即报名</a>
					</c:if>
						<div class="share-area pull-right">
							<span class="icon icon-is-share"></span> 分享： <span
								class="share-icon wechat" data-toggle="modal"
								data-target="#share_process"></span>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>

	<div class="offline-activity-intro">
		<div class="container">
			<div class="title-tag mt24 clearfix">
				<h5 class="tt pull-left">
					<span class="icon icon-detail"></span>活动简介
				</h5>
			</div>
			<div class="intro">${vo_data.description}</div>
		</div>
	</div>

	<div class="container">
		<!-- 评论区域 -->
		<jsp:include page="/web/comment/comment_common.jsp"></jsp:include>
		<!-- 评论区域 -->
	</div>

	<jsp:include page="/web/partials/footer.jsp"></jsp:include>

	<!-- 报名详情 -->
	<div class="modal fade no-radius" id="offline_apply_detail">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">报名详情</h4>
				</div>
				<div class="modal-body">
					<div class="offline-apply-info">
						已报名人数：<span id="booked_person_num" class="num">${vo_data.bookNum}</span>/${vo_data.personNum}
					</div>
					<table id="book_detail_table" class="table table-striped table-hover offline-apply-table">
						<thead>
							<tr>
								<th>姓名</th>
								<th>报名时间</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody>
							<!-- <tr>
								<td>陈潇</td>
								<td>2016/08/09 16:00</td>
								<td class="status">报名成功</td>
							</tr> -->
						</tbody>
					</table>
					<div class="panel-footer foot-page">
						<ul id="table_pagination_ul" class="pagination">
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

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
							<img src="" />
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
	<input type="hidden" id="comment_body_uuid" value="${vo_data.uuid}" />
	<input type="hidden" id="comment_body_type" value="5" />
</body>
</html>
