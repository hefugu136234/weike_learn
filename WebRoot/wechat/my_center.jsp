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
<title>个人中心</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/users.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
	<div class="page">
		<div class="content pb50">
			<div class="user-status-panel">
				<!-- 加载公共的头部个人信息 -->
				<jsp:include page="/wechat/common/top_userInfo.jsp"></jsp:include>
				<!-- 加载公共的头部个人信息 -->

				<div class="user-status">
					<div class="box points">
						<p class="num">${vo_user_data.score}</p>
						<p class="tt">积分</p>
					</div>
					<a href="/api/webchat/change/page/info" class="box link-btn">
						<p class="icon icon-edit"></p>
						<p class="tt">个人信息</p>
					</a>
				</div>
			</div>

			<div class="grid-icon-group">
				<ul class="list clearfix">
					<li>
						<a href="/api/webchat/activity/real/name/page" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon2.png">
							</div>
							<h5 class="tt">实名认证</h5>
						</a>
					</li>
					<li>
						<a href="/api/webchat/active/page/code" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon1.png">
							</div>
							<h5 class="tt">VIP卡</h5>
						</a>
					</li>
					<li>
						<a href="/api/webchat/wx/my/jifen" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon3.png">
							</div>
							<h5 class="tt">我的积分</h5>
						</a>
					</li>
					<li>
						<a href="/api/webchat/wx/jifen/shop" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon4.png">
							</div>
							<h5 class="tt">积分商场</h5>
						</a>
					</li>
					<li>
						<a href="/api/webchat/activity/my/oups/page" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon5.png">
							</div>
							<h5 class="tt">我的作品</h5>
						</a>
					</li>
					<li>
						<a href="/api/webchat/my/collection" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon6.png">
							</div>
							<h5 class="tt">我的收藏</h5>
						</a>
					</li>
					<li>
						<a href="/api/webchat/wx/upload/work" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon7.png">
							</div>
							<h5 class="tt">征稿上传</h5>
						</a>
					</li>
					<li>
						<a href="/api/webchat/shareGift/list/page" class="btn">
							<div class="icon">
								<img src="/assets/img/app/users/tag_icon8.png">
							</div>
							<h5 class="tt">邀请好友</h5>
						</a>
					</li>
				</ul>
			</div>

      <div class="list-with-icon u-center-list with-arr mt10">
	      <a href="/api/webchat/my/collection" class="box">
	        <div class="icon-l icon-star"></div>
	        我的收藏
	        <div class="icon icon-arr-r"></div>
	      </a>
	      <c:if test="${not empty collect_data.items}">
		      <div class="list-with-img with-arr p10">
	  				<c:forEach var="item" items="${collect_data.items}">
	  					<c:if test="${item.type ne 'NEWS'}">
		    				<a href="/api/webchat/resource/first/view/${item.uuid}" class="box">
		    					<div class="img">
		    						<img src="${item.cover}">
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
										<div class="img-tag br view-num">
											<span class="icon icon-eye"></span>${item.viewCount}
										</div>
		    					</div>
		    					<div class="info">
		    						<h5 class="tt limit-str-text" data-minstr="34">${item.name}</h5>
		    						<div class="desc">
											<p><span>${item.catgoryName}</span></p>
											<p><span>${item.speakerName}</span> | <span>${item.hospitalName}</span></p>
										</div>
		    					</div>
		    					<div class="icon icon-arr-r"></div>
		    				</a>
		    			</c:if>
	  				</c:forEach>
	  			</div>
	  		</c:if>

	      <a href="/api/webchat/activity/my/oups/page" class="box">
	        <div class="icon-l icon-player"></div>
	        我的作品
	        <div class="icon icon-arr-r"></div>
	      </a>
	      <c:if test="${not empty oups_data.items}">
		      <div class="list-with-img with-arr p10">
	  				<c:forEach var="item" items="${oups_data.items}">
	  					<c:if test="${item.type ne 'NEWS'}">
		    				<a href="/api/webchat/wx/works/detail/${item.uuid}" class="box">
		    					<div class="img">
		    						<img src="${item.cover}">
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
										<div class="img-tag br view-num">
											<span class="icon icon-eye"></span>${item.viewCount}
										</div>
		    					</div>
		    					<div class="info">
		    						<h5 class="tt limit-str-text" data-minstr="34">${item.name}</h5>
		    						<div class="desc">
											<p><span>${item.catgoryName}</span></p>
											<p><span>${item.speakerName}</span> | <span>${item.hospitalName}</span></p>
										</div>
		    					</div>
		    					<div class="icon icon-arr-r"></div>
		    				</a>
		    			</c:if>
	  				</c:forEach>
	  			</div>
	  		</c:if>
	  		<a href="/api/webchat/offline/activity/off/my" class="box">
	        <div class="icon-l icon-flag-solid"></div>
	        我报名的活动
	        <div class="icon icon-arr-r"></div>
	      </a>
	      <!-- <a href="#" class="box">
	        <div class="icon-l icon-search"></div>
	        我的搜索
	        <div class="icon icon-arr-r"></div>
	      </a>
	      <a href="/api/webchat/sign/page" class="box">
	        <div class="icon-l icon-edit"></div>
	        签收
	        <div class="icon icon-arr-r"></div>
	      </a> -->
	      <a href="/api/webchat/wx/exchange/jifen/page" class="box">
	        <div class="icon-l icon-gift"></div>
	        我的兑换
	        <div class="icon icon-arr-r"></div>
	      </a>
	      <a href="/api/webchat/shop/address/list/page" class="box">
	        <div class="icon-l icon-gps"></div>
	        收货地址管理
	        <div class="icon icon-arr-r"></div>
	      </a>
	      <!-- <a href="javascript:adviseSumit();" class="box">
	        <div class="icon-l icon-edit"></div>
	        意见反馈
	        <div class="icon icon-arr-r"></div>
	      </a> -->
	      <a href="/api/webchat/about/our" class="box">
	        <div class="icon-l icon-logo"></div>
	        关于我们
	        <div class="icon icon-arr-r"></div>
	      </a>
	    </div>
		</div>
		<jsp:include page="/wechat/common/foot.jsp"></jsp:include>
	</div>
	<script type="text/javascript">
		$(function() {
			addActiveClass('grzx');
		});
		//意见反馈
		function adviseSumit() {
			var base_url = location.href.split('#')[0];
			$.getJSON('/api/webchat/advise/retroaction', {
				'wenjuan_name' : '意见反馈',
				'redirect_uri' : base_url,
				'timestamp' : new Date().getTime()
			}, function(data) {
				if (isSuccess(data)) {
					// 跳转登录
					location.href = data.message;
				} else {
					alert(data.message);
				}
			});
		}
	</script>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
