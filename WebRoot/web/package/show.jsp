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
<title>${vo_data.name}</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="/assets/js/web/common.js"></script>
<script src="/assets/js/web/package/show.js"></script>

<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="/web/partials/top.jsp"></jsp:include>

	<div class="package-detail-banner">
		<div class="container">
			<ol class="breadcrumb crumb-nav">
				<li><a href="/f/web/index">首页</a></li>
				<li><a href="/f/web/course/package/list/page">课程</a></li>
				<li class="active">${vo_data.name}</li>
			</ol>

			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-8">
					<div class="package-intro">
						<h5 class="title">${vo_data.name}</h5>
						<div class="intro">${vo_data.desc}</div>
						<div class="status">
							<span>共计<c:if test="${not empty vo_data.items}">${fn:length(vo_data.items)}</c:if>章</span>
							<span>在学习：${vo_data.learnCount}人</span>
						</div>
					</div>
				</div>

					<div class="col-xs-12 col-sm-12 col-md-4">
				<c:if test="${vo_data.studyStatus gt 0}">
						<div class="user-package-status clearfix">
							<!-- <div class="user-info pull-left">
								<div class="photo">
									<img src="" alt="用户头像" />
								</div>
								<div class="name"></div>
							</div> -->
							<div class="package-info pull-right">
								<div class="item">
									<p class="info">
										<span class="num">${vo_data.learnSchedule}</span>%
									</p>
									<p>进度</p>
								</div>
							</div>
						</div>

				</c:if>
						<c:if test="${vo_data.studyStatus eq 0}">
						 <button id="course_study" type="button" class="btn btn-lg btn-orange begin-package-btn">开始学习</button>
						 <div id="user_package_status" class="user-package-status clearfix hide">
							<div class="package-info pull-right">
								<div class="item">
									<p class="info">
										<span class="num"></span>%
									</p>
									<p>进度</p>
								</div>
							</div>
						</div>
						</c:if>
					</div>

				<!-- <div class="package-show-status clearfix">
					<span class="box"><i class="icon icon-edit"></i>${vo_data.learnCount}</span>
					<span class="box text-center"><i class="icon icon-like"></i>${vo_data.praiseCount}</span>
					<span class="box text-right"><i class="icon icon-message"></i>${vo_data.commentCount}</span>
				</div> -->
			</div>


		</div>
	</div>

	<c:if test="${not empty vo_data.items}">
		<c:forEach var="item" items="${vo_data.items}">
			<div class="container">
				<div class="title-tag mt16 clearfix">
					<h5 class="tt pull-left">${item.name}</h5>
				</div>
				<c:if test="${not empty item.items}">
					<div class="category-list col-2" data-logined="${item.logined}" data-disableClick="${item.disableClick}">
						<div class="row">
							<c:forEach var="res_item" items="${item.items}">
								<div class="item data" data-uuid="${res_item.uuid}" data-type="resource">
									<div class="media with-icon">
										<div class="media-left img">
											 <a href="/f/web/course/learn/${res_item.uuid}" href="javascript:void(0);">
												<img class="media-object" src="${not empty res_item.cover ? res_item.cover : '/assets/img/web/placeholder.jpg'}" alt="">
												<c:if test="${res_item.type eq 'VIDEO'}">
													<div class="img-tag tr video-type">视频</div>
												</c:if>
												<c:if test="${res_item.type eq 'PDF'}">
													<div class="img-tag tr pdf-type">PDF</div>
												</c:if>
												<c:if test="${res_item.type eq 'THREESCREEN'}">
													<div class="img-tag tr ppt-type">课件</div>
												</c:if>
												<c:if test="${res_item.type eq 'VR'}">
			                    <div class="img-tag tr vr-type">VR</div>
			                  </c:if>
			                  <c:if test="${res_item.bloody}">
			                    <div class="bloody-icon">
			                      <img alt="" src="/assets/img/app/icon_bloody.png">
			                    </div>
			                  </c:if>
											</a>
										</div>
										<div class="media-body info row-2">
											<h4 class="media-heading tt">
												<a href="/api/webchat/course/learn/${res_item.uuid}" data-minstr="90" class="limit-str-text">${res_item.name}</a>
											</h4>
											<p>
												<span>时长${res_item.showMessage}</span> | <span>点赞${res_item.praiseCount}次</span>
											</p>
										</div>
										<c:if test="${res_item.pass}">
										<div class="status-icon">
											<span class="icon-round-check-fill"></span>
										</div>
										</c:if>
									</div>
								</div>
							</c:forEach>
							<c:if test="${not empty item.examineItem}">
							<div class="item data" data-type="examine" data-uuid="${item.examineItem.uuid}">
								<div class="media with-icon">
									<div class="media-left img">
										<a href="javascript:void(0);">
											<img class="media-object" src="${item.examineItem.cover}" alt="">
											<div class="img-tag tr exam-type">考试</div>
										</a>
									</div>
									<div class="media-body info middle">
										<h4 class="media-heading tt">
											<a href="javascript:void(0);" data-minstr="90" class="limit-str-text">${item.examineItem.name}</a>
										</h4>
									</div>
									<c:if test="${item.examineItem.examineStatus eq 0}">
									<div class="status-exam">未参加</div>
									</c:if>
									<c:if test="${item.examineItem.examineStatus eq 1}">
									<div class="status-icon">
										<span class="icon-round-check-fill"></span>
									</div>
									</c:if>
									<c:if test="${item.examineItem.examineStatus eq 2}">
									<div class="status-exam">未通过</div>
									</c:if>
								</div>
							</div>
							</c:if>
						</div>
					</div>
				</c:if>
			</div>
		</c:forEach>
	</c:if>

	<div class="modal fade bs-example-modal-lg" id="package_cover" tabindex="-1" role="dialog" aria-labelledby="package_coverLabel">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="package_coverLabel"></h4>
	      </div>
	      <div class="modal-body">
	      	<div id="package_coverDes" class="package-cover-intro"></div>
	      	<div class="package-cover-status clearfix">
	      		<div class="item">
	      			<span class="icon icon-clock"></span>
	      			考试时间：
	      			<span id="package_coverTime" class="num"></span>
	      		</div>
	      		<div class="item">
	      			<span class="icon icon-rank"></span>
	      			考题数量：
	      			<span id="package_coverNum" class="num"></span>
	      		</div>
	      		<div class="item">
	      			<span class="icon icon-pass"></span>
	      			合格分数：
	      			<span id="package_coverScore" class="num"></span>
	      		</div>
	      	</div>
	      </div>
	      <div class="modal-footer no-border text-center">
	        <a href="javascript:" id="examine_start" class="btn btn-lg btn-long btn-orange">开始考试</a>
	      </div>
	    </div>
	  </div>
	</div>

	<input type="hidden" id="course_uuid" value="${vo_data.uuid}"/>
	<input type="hidden" id="course_studyStatus" value="${vo_data.studyStatus}"/>
	<jsp:include page="/web/partials/footer.jsp"></jsp:include>
</body>
</html>
