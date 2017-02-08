<%@page import="com.lankr.tv_cloud.utils.Tools"%>
<%@page import="com.lankr.tv_cloud.model.NewsInfo"%>
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
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
</head>
<%
	NewsInfo news = (NewsInfo) request.getAttribute("news");
%>
<body>
	<div class="page">
		<div class="content">
			<header class="top-bar">
        <div class="crumb-nav">
          资讯详情
        </div>
      </header>
			<%
				if (news != null) {
			%>
			<div class="article-detail">
        <div class="summary">
          <h5 class="tt"><%=news.getTitle()%></h5>
          <div class="date"><%=Tools.formatYMDHMSDate(news.getCreateDate())%></div>
        </div>

        <div class="details"><%=news.getContent()%></div>
        <div id="news_summary" class="hide"><%=news.getSummary()%></div>
      </div>
			<%
				} else {
			%>
			<span class="error">${not empty requestScope.error?requestScope.error:'读取错误'}</span>
			<%
				}
			%>
		</div>
	</div>
</body>
</html>
