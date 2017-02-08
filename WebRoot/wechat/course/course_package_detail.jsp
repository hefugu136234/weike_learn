<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

<link rel="stylesheet" href="/assets/css/app/font.css">
<link rel="stylesheet" href="/assets/css/app/default.css">
<link rel="stylesheet" href="/assets/css/app/package.css">

<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript" src="/assets/js/wechat/course/course_package_detail.js"></script>
</head>

<body>
	<div class="page">
		<div class="content">
			<div class="package-user-status">
				<a href="/api/webchat/course/package/list/page" class="icons icon-logo page-absolute-logo"></a>

				<c:if test="${vo_data.studyStatus eq 0}">
				<div id="course_study" data-uuid="${vo_data.uuid}" class="button btn-radius btn-orange package-go-btn">我要学习</div>
				<div id="user-info" class="user-info hide">
						<!-- <div class="box">
							<h5 class="name"></h5>
						</div> -->

						<div class="status-box clearfix">
							<div class="box">
								<p class="info">
									<span class="num"></span>%
								</p>
								<p>进度</p>
							</div>
						</div>
					</div>
				</c:if>

				<c:if test="${vo_data.studyStatus gt 0}">
					<!-- <div class="user-photo">
						<div class="photo">
							<img src="" alt="">
						</div>
					</div> -->

					<div class="user-info">
						<!-- <div class="box">
							<h5 class="name"></h5>
						</div> -->

						<div class="status-box clearfix">
							<div class="box">
								<p class="info">
									<span class="num">${vo_data.learnSchedule}</span>%
								</p>
								<p>进度</p>
							</div>
						</div>
					</div>
				</c:if>

				<div class="course-package-info">
					<h5 class="title">${vo_data.name}</h5>
					<p><a href="#" class="more">查看课程简介</a></p>
					<p>
						<c:if test="${not empty vo_data.items}"><span>共计${fn:length(vo_data.items)}章</span></c:if>
						<span>在学习：${vo_data.learnCount}人</span>
					</p>
				</div>
			</div>

			<!-- <div class="package-show-status">
				<div class="title">
					<h5 class="tt">${vo_data.name}
						<c:if test="${not empty vo_data.items}">(${fn:length(vo_data.items)}章)</c:if>
					</h5>
					<div class="package-list-status">
						<span class="box"><i class="icon icon-edit"></i>${vo_data.learnCount}</span>
						<span class="box txtC"><i class="icon icon-like"></i>${vo_data.praiseCount}</span>
						<span class="box txtR"><i class="icon icon-message"></i>${vo_data.commentCount}</span>
					</div>
				</div>
				<div class="more">查看课程简介</div>
			</div> -->

			<c:if test="${not empty vo_data.items}">
				<c:forEach var="item" items="${vo_data.items}" varStatus="index">
					<div class="list-with-icon with-arr package-list-control">
						<a href="javascript:;" class="box package-list-tab ${index.index eq 0 ? 'open' : ''}">
							${item.name}
							<div class="icon icon-arr-r"></div>
						</a>

						<c:if test="${not empty item.items}">
							<div data-logined="${item.logined}" data-disableClick="${item.disableClick}"  class="list-with-img compact package-show-list with-arr ${index.index eq 0 ? 'open' : ''}">
								<c:forEach var="res_item" items="${item.items}">
									<div data-uuid="${res_item.uuid}" data-type="resource"
									class="box done">
										<div class="img">
											<img src="${res_item.cover}">
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
										</div>
										<div class="info">
											<h5 class="tt limit-str-text" data-minstr="34">${res_item.name}</h5>
											<div class="desc">
												<p>
													<span>时长${res_item.showMessage}</span> | <span>点赞${res_item.praiseCount}次</span>
												</p>
											</div>
										</div>
										<c:if test="${res_item.pass}">
										<div class="icon icon-round-check-fill"></div>
										</c:if>
										<div class="icon icon-arr-r"></div>
									</div>
								</c:forEach>
								<c:if test="${not empty item.examineItem}">
								<div data-type="examine" data-uuid="${item.examineItem.uuid}" class="box done">
									<div class="img">
										<img src="${item.examineItem.cover}">
										<div class="img-tag tr exam-type">考试</div>
									</div>
									<div class="info middle">
										<h5 class="tt limit-str-text" data-minstr="34">${item.examineItem.name}</h5>
									</div>
									<!-- <div class="icon icon-round-check-fill"></div> -->
									<c:if test="${item.examineItem.examineStatus==0}">
									<div class="exam-status">未参加</div>
									</c:if>
									<c:if test="${item.examineItem.examineStatus==1}">
									<div class="exam-status">已通过</div>
									</c:if>
									<c:if test="${item.examineItem.examineStatus==2}">
									<div class="exam-status">未通过</div>
									</c:if>
									<div class="icon icon-arr-r"></div>
								</div>
								</c:if>
							</div>
						</c:if>
					</div>
				</c:forEach>
			</c:if>

		</div>
	</div>
	<input type="hidden" id="course_uuid" value="${vo_data.uuid}"/>
	<input type="hidden" id="course_studyStatus" value="${vo_data.studyStatus}"/>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
