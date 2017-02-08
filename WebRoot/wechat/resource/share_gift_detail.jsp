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
<title>${res_vo.news.title}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<!-- 新页面 原 share_detail.jsp-->
<body>
	<div class="page">
		<div class="content">
			<header class="top-bar">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          <a href="/api/webchat/shareGift/list/page">邀请好友</a>
          ${res_vo.news.title}
        </div>
      </header>

      <div class="article-detail">
        <div class="summary">
          <h5 class="tt">${res_vo.news.title}</h5>
          <div class="date">${res_vo.dateTime}</div>
        </div>

        <div class="details">${res_vo.news.content}</div>

        <div class="qr-code-box clearfix">
					<div class="img">
						<img src="/assets/img/qr.jpg" alt="" width="172">
					</div>
					<div class="info">
						<p class="txtC">
							<img src="/assets/img/app/logo_wf.png" alt="" width="54">
						</p>
						<p class="mt10">长按二维码，关注<strong>“知了云盒”</strong>微信账号或在微信公众账号中搜索<strong>“知了云盒”</strong></p>
					</div>
				</div>
      </div>

		</div>
	</div>
	<input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
