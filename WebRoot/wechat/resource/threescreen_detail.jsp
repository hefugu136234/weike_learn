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
<script src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript" src="/assets/js/wechat/resource/threescreen_detail.js"></script>
</head>

<!-- 新页面 原 three_screen.jsp-->
<body>
	<div class="page">
		<header class="top-bar video-top-bar">
      <div class="crumb-nav">
        <a href="/api/webchat/index" class="logo icon-logo"></a>
        ${res_vo.name}
      </div>
    </header>

		<div class="video-split-con clearfix">
			<div class="video-container">
				<div id="video_container"></div>
			</div>

			<div class="video-ppt-container">
				<ul id="pdf_list_ul" class="ppt-list">
					<c:forEach var="item" items="${res_vo.threeScreen.pdfList}" varStatus="index">
						<li class="item${index.index==0?' active':''}">
							<img src="http://7xo6el.com2.z0.glb.qiniucdn.com/${res_vo.threeScreen.pdfTaskId}?odconv/jpg/page/${item}/density/150/quality/80/resize/800" height="280" alt="">
						</li>
					</c:forEach>
				</ul>
			</div>

			<div class="ppt-select-list" id="ppt_select_list">
        <ul id="chapters_list_ul" class="list">
					<c:forEach var="item" items="${res_vo.threeScreen.pdfList}" varStatus="index">
						<li class="item${index.index==0?' active':''}">第 ${index.count} 页</li>
					</c:forEach>
				</ul>
      </div>
		</div>

		<div class="video-tips" id="video_tips">
			<div class="box">
				<h5 class="tt" id="video_tips_tt"></h5>
				<div class="info" id="video_tips_info"></div>
			</div>
		</div>

		<div class="video-operation-tag clearfix">
			<div id="page_select_btn" class="page-select icon-list bfL"></div>
			<div class="video-tips-btn bfL" id="video_tips_btn">
				<span class="icon icon-info"></span>
			</div>
			<div class="video-btns">
				<div id="play_pre_button" class="btns btn-prev icon-backward disabled"></div>
        <div id="play_total_button" class="btns btn-play icon-play2"></div>
        <div id="play_next_button" class="btns btn-next icon-forward"></div>
			</div>
			<div class="video-status bfR">
				<span id="current_page_span">1</span>/<span id="v_all_p">${res_vo.threeScreen.pdfNum}页</span>
			</div>
		</div>
	</div>

	<input type="hidden" id="three_fileid" value="${res_vo.threeScreen.fileId}"/>
  <input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />
  <textarea id="three_division" class="hide">${res_vo.threeScreen.division}</textarea>
  <input type="hidden" id="page_reamin_uuid" value="${page_reamin_uuid}"/>
<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
