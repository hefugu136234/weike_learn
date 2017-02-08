<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>出错啦！</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
  </head>

  <!-- 新页面 原 无-->
  <body>
  	<div class="page">
  		<div class="content error-page">
  			<div class="p10">
          <div class="icon">
            <img src="/assets/img/web/mascot.png" width="30%">
          </div>
          <p>${error}</p>
          <a href="/api/webchat/index" class="button btn-full btn-cyan btn-radius">我知道了</a>
        </div>
  		</div>
  	</div>
  </body>
</html>
