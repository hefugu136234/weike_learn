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
<title>${res_vo.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/resource.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript" src="/assets/js/wechat/resource/resource_preview.js"></script>
</head>

<!-- 新页面 原 /share_res/total_resource_preview.jsp-->
<body>
	<div class="page">
		<div class="content">
			<header class="top-bar with-status">
        <div class="crumb-nav">
          <a href="/api/webchat/index" class="logo icon-logo"></a>
          ${res_vo.name}
        </div>
        <div class="top-status play-times">
					<p>当前播放</p>
					<p class="num">${res_vo.viewCount}</p>
				</div>
      </header>

      <div class="resource-preview">
        <img src="${res_vo.cover}" alt="">
        <div class="cover"></div>
        <a href="javascript:void(0);" class="play-btn icon-play" id="three_play"></a>
      </div>

			<!-- 收藏点赞分享公共部分 -->
			<jsp:include page="/wechat/resource/resource_common_part.jsp"></jsp:include>

      <div class="resource-intro">
        <div class="icon-title mb-1 white bold clearfix">
          <h5 class="tt bfL">
            <span class="icon icon-detail"></span>简 介
          </h5>
        </div>

        <div class="intro">
          <div class="clearfix">
            <div class="col-2">
              <span class="tt">讲者：</span>${res_vo.speakerName}
            </div>
            <div class="col-2">
              <c:if test="${res_vo.type eq 'VIDEO'}">
                <span class="tt">时长：</span>${res_vo.video.videoTime}
              </c:if>
              <c:if test="${res_vo.type eq 'PDF'}">
                <span class="tt">页数：</span>${res_vo.pdf.pdfnum}
              </c:if>
              <c:if test="${res_vo.type eq 'THREESCREEN'}">
                <span class="tt">时长：</span>${res_vo.threeScreen.videoTime}
              </c:if>
            </div>
          </div>
          <p><span class="tt">名称：</span>${res_vo.name}</p>
          <p><span class="tt">日期：</span>${res_vo.dateTime}</p>
          <c:if test="${res_vo.type eq 'THREESCREEN'}">
            <p><span class="tt">页数：</span>${res_vo.threeScreen.pdfNum}</p>
          </c:if>
          <p><span class="tt">简介：</span>${res_vo.desc}</p>
          <p><span class="tt">医院：</span>${res_vo.hospitalName}</p>
        </div>
      </div>
		</div>
	</div>

	<!-- <div class="feed-back-modal" id="reg_alert">
  	<div class="cover"></div>
    <div class="alert">
      <h5 class="tt"></h5>
      <div class="con">您需要<strong class="blue">注册/登录</strong>“知了云盒”账号才能观看该视频哦</div>
      <div class="btns">
        <a href="javascript:" class="btn login">登录</a>
        <a href="javascript:" class="btn sub reg">立即注册</a>
      </div>
      <div class="close-btn cancel"><span class="icon icon-close"></span></div>
    </div>
  </div> -->

	<input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
