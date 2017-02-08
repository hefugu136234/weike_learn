<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="renderer" content="webkit">
<title>知了微课 知我所学，了我所需</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all"
	href="//cdn.bootcss.com/Swiper/3.3.1/css/swiper.min.css">
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/Swiper/3.3.1/js/swiper.min.js"></script>
<script src="/assets/js/web/common.js"></script>
<script src="/assets/js/web/category.js"></script>

<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="partials/top.jsp"></jsp:include>

	<div class="container">
		<ul class="nav nav-pills category-tabs col-7" id="category_tabs">
			<c:forEach var="item"
				items="${requestScope.data_total.totalMenuList}">
				<li <c:if test="${item.active}"> class="active" </c:if>><a
					href="/f/web/category/list/${item.uuid}"><span class="icon"
						data-name="${item.name}"></span>${item.name}</a></li>
			</c:forEach>
		</ul>

		<div class="row">
			<div class="col-xs-12 col-sm-3 col-md-3 con-pr8">
				<c:if test="${not empty data_total.hasLevelList}">
					<c:forEach var="total_item" items="${data_total.hasLevelList}">
						<div class="panel no-radius category-side-list">
							<div class="panel-heading tt<c:if test="${total_item.active}"> active </c:if>">
								${total_item.levelName}<span class="num"></span>
							</div>
							<div class="panel-body">
								<div class="list-group list">
									<c:forEach var="item" items="${total_item.itemList}">
										<a href="/f/web/category/list/${item.uuid}"
											class="list-group-item<c:if test="${item.active}"> active </c:if>">${item.name}<span
											class="num">(${item.resCount}资源)</span></a>
									</c:forEach>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>
				<c:if test="${not empty data_total.subMenuList}">
					<div class="panel no-radius category-side-list">
						<!-- <div class="panel-heading tt">神经外科<span class="num"></span></div> -->
						<div class="panel-body">
							<div class="list-group list">
								<c:forEach var="item" items="${data_total.subMenuList}">
									<a href="/f/web/category/list/${item.uuid}"
										class="list-group-item<c:if test="${item.active}"> active </c:if>">${item.name}<span
										class="num">(${item.resCount}资源)</span></a>
								</c:forEach>
							</div>
						</div>
					</div>
				</c:if>
			</div>
			<div class="col-xs-12 col-sm-9 col-md-9 con-pl8">
				<c:if test="${not empty requestScope.data_total.activityList}">
					<div class="swiper-container category-banner default-banner mb10">
						<div class="swiper-wrapper">
							<c:forEach var="item"
								items="${requestScope.data_total.activityList}">
								<div class="swiper-slide">
									<a href="/f/web/activity/detail/${item.uuid}"> <img
										src="${not empty item.kv ? item.kv : '/assets/img/web/placeholder_2x1.jpg'}"
										width="100%" />
									</a>
								</div>
							</c:forEach>
						</div>
						<div class="swiper-pagination default-banner-ctrl"></div>
					</div>
				</c:if>

				<div class="panel no-radius category-panel">
					<div class="panel-heading title">${requestScope.data_total.selectName}</div>
					<div id="res_list_div" class="panel-body category-list">
						<c:choose>
							<c:when test="${not empty requestScope.data_total.subResList}">
								<c:forEach var="item"
									items="${requestScope.data_total.subResList}">
									<div class="item">
										<div class="media">
											<div class="media-left img">
												<a href="/f/web/resource/detail/${item.uuid}">
													<img class="media-object" src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder.jpg'}" alt="">
													<c:if test="${item.type eq 'VIDEO'}">
														<div class="img-tag tr video-type">视频</div>
													</c:if>
													<c:if test="${item.type eq 'PDF'}">
														<div class="img-tag tr pdf-type">PDF</div>
													</c:if>
													<c:if test="${item.type eq 'THREESCREEN'}">
														<div class="img-tag tr ppt-type">课件</div>
													</c:if>
													<c:if test="${item.type eq 'VR'}">
				                    <div class="img-tag tr vr-type">VR</div>
				                  </c:if>
				                  <c:if test="${item.bloody}">
				                    <div class="bloody-icon">
				                      <img alt="" src="/assets/img/app/icon_bloody.png">
				                    </div>
				                  </c:if>
													<div class="img-tag br"><span class="icon icon-eye"></span>${item.viewCount}</div>
												</a>
											</div>
											<div class="media-body info">
												<h4 class="media-heading tt">
													<a href="/f/web/resource/detail/${item.uuid}"
														data-minstr="90" class="limit-str-text">${item.name}</a>
												</h4>
												<p>
													<span>${item.code}</span> | <span>${item.speakerName}</span>
												</p>
												<p>
													<span>${item.catgoryName}</span> | <span>${item.hospitalName}</span>
												</p>
											</div>
										</div>
									</div>
								</c:forEach>
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
					<div class="panel-footer foot-page">
						<input type="hidden" id="rest_count"
							value="${requestScope.data_total.subResCount}" /> <input
							type="hidden" id="selectUuid"
							value="${requestScope.data_total.selectUuid}" />
						<ul id="pagination_ul" class="pagination">
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="partials/footer.jsp"></jsp:include>
</body>
</html>
