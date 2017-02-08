<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="renderer" content="webkit">
<title>知了微课 课程包列表</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all"
	href="//cdn.bootcss.com/flickity/1.2.1/flickity.min.css">
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/flickity/1.2.1/flickity.pkgd.min.js"></script>
<script src="/assets/js/web/common.js"></script>
<script src="/assets/js/web/package/list.js"></script>

<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="/web/partials/top.jsp"></jsp:include>
	<jsp:include page="/web/partials/top_banner.jsp"></jsp:include>

	<div class="package-list">
		<div class="container">
			<c:choose>
				<c:when test="${not empty vo_data.items}">
					<ul class="resource-list row" id="package_ul">
						<c:forEach var="item" items="${vo_data.items}">
							<li class="col-xs-12 col-sm-6 col-md-3 item">
								<div class="thumbnail resource-item">
									<a href="/f/web/course/package/detail/${item.uuid}" class="img">
										<img src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder.jpg'}"alt="" />
										<c:if test="${item.studyStatus gt 0}">
											<c:if test="${item.learnSchedule lt 100}">
												<div class="package-process">
													<div class="process-circle">
														<p class="process">
															<span class="num">${item.learnSchedule}</span>%
														</p>
														<p>进度</p>
													</div>
												</div>
											</c:if>
											<c:if test="${item.learnSchedule eq 100}">
												<div class="package-done">
													<div class="info">
														<div class="icon icon-checked"></div>
														<p>已完成</p>
													</div>
												</div>
											</c:if>
										</c:if>
									</a>
									<div class="caption desc">
										<h3 class="tt">
											<a href="/f/web/course/package/detail/${item.uuid}" data-minstr="40" class="limit-str-text">${item.name}</a>
										</h3>
										<div class="package-list-status clearfix">
											<span class="box"><i class="icon icon-edit"></i>${item.learnCount}</span> <span
												class="box text-center"><i class="icon icon-like"></i>${item.praiseCount}</span>
											<span class="box text-right"><i
												class="icon icon-message"></i>${item.commentCount}</span>
										</div>
									</div>
								</div>
							</li>
						</c:forEach>
					</ul>
					<div class="foot-page">
						<input type="hidden" id="all_package_count"
							value="${vo_data.itemTotalSize}" />
						<ul class="pagination" id="all_package_list"></ul>
					</div>
				</c:when>
				<c:otherwise>
					<div class="content-empty">
						<div class="icon icon-empty"></div>
						<div class="tips">
							<p>内容暂无</p>
							<p>去其他栏目看看吧</p>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<jsp:include page="/web/partials/footer.jsp"></jsp:include>
</body>
</html>
