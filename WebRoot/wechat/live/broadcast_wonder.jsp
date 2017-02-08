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
<title>精彩直播</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/Swiper/3.1.7/css/swiper.min.css"
	rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<link href="/assets/css/app/broadcast.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript"
	src="http://cdn.bootcss.com/Swiper/3.1.7/js/swiper.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/broadcast/broadcast_wonder.js"></script>
</head>

<body>
	<div class="page">
		<div class="content pb50">
			<!-- banner -->
			<c:if test="${not empty vo_data.bannerList}">
				<div class="swiper-container default-swiper">
					<ul class="swiper-wrapper">
						<c:forEach var="item" items="${vo_data.bannerList}">
							<li class="swiper-slide"><a
								href="${not empty item.url?item.url:'javascript:void(0);'}">
									<img src="${item.cover}">
							</a></li>
						</c:forEach>
					</ul>
					<div class="swiper-pagination"></div>
				</div>
			</c:if>
			<!-- banner -->

			<div class="tab-control">
				<ul class="tab-control-tag select-tag with-bg">
					<li class="btn">即将开始<small class="num">${vo_data.bookLiveCount}</small></li>
					<li class="btn active">正在直播<small class="num">${vo_data.livingCount}</small></li>
					<li class="btn">精彩回顾<small class="num">${vo_data.livedCount}</small></li>
				</ul>

				<div class="tab-ctrl-content">
					<div class="tab-content">
						<!-- 即将开始 -->
						<c:choose>
							<c:when test="${vo_data.bookLiveCount gt 0}">
							<c:forEach var="block_item" items="${vo_data.books}">
								<div class="broadcast-panel">
									<div class="date-tag clearfix mb10">
										<div class="tag">
											${block_item.startTime}
											<c:if test="${block_item.todayFlag}">
											<span class="today">今天</span>
											</c:if>
										</div>
									</div>
									<div class="list-with-img with-arr broadcast-list">
									<c:forEach var="item" items="${block_item.itemList}">
										<a href="/api/webchat/broadcast/first/page/${item.uuid}" class="box">
											<div class="img">
												<img src="${item.cover}">
											</div>
											<div class="info">
												<h5 class="tt limit-str-text" data-minstr="34">${item.name}</h5>
												<p><span class="icon icon-users"></span>${item.bookNum}人<span
														class="deadline">即将开始</span></p>
											</div>
											<div class="icon icon-arr-r"></div>
										</a>
										</c:forEach>
									</div>
								</div>
								</c:forEach>
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

					<div class="tab-content active">
						<!-- 正在直播 -->
						<c:choose>
							<c:when test="${vo_data.livingCount gt 0}">
							<c:forEach var="block_item" items="${vo_data.livings}">
								<div class="broadcast-panel">
									<div class="date-tag clearfix mb10">
										<div class="tag">
											${block_item.startTime}
											<c:if test="${block_item.todayFlag}">
											<span class="today">今天</span>
											</c:if>
										</div>
									</div>
									<div class="list-with-img with-arr broadcast-list">
									<c:forEach var="item" items="${block_item.itemList}">
										<a href="/api/webchat/broadcast/first/page/${item.uuid}" class="box">
											<div class="img">
												<img src="${item.cover}">
											</div>
											<div class="info">
												<h5 class="tt limit-str-text" data-minstr="34">${item.name}</h5>
												<p>
													<span class="icon icon-users"></span>${item.bookNum}人<span
														class="deadline">剩余${item.remainDays}天结束</span>
												</p>
											</div>
											<div class="icon icon-arr-r"></div>
										</a>
										</c:forEach>
									</div>
								</div>
								</c:forEach>
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
						<!-- 精彩回顾 -->
						<c:choose>
							<c:when test="${vo_data.livedCount gt 0}">
								<div id="live_recorded_div" class="broadcast-panel">
									<div id="live_recorded_ul"
										class="list-with-img with-arr broadcast-list"></div>
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
				</div>
			</div>
		</div>

		<jsp:include page="/wechat/common/foot.jsp"></jsp:include>
	</div>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
