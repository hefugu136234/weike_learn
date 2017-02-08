<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <meta http-equiv="X-UA-Compatible" content="IE=9">
    <meta name="renderer" content="webkit">
    <title>${requestScope.res_data.newsVo.title}</title>
    <link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
    <link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="/assets/js/web/common.js"></script>

    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <jsp:include page="partials/top.jsp"></jsp:include>

    <div class="container">
      <ol class="breadcrumb crumb-nav">
        <li><a href="/f/web/index">首页</a></li>
        <li><a href="/f/web/activity/list/page">活动</a></li>
        <li class="active">${requestScope.res_data.newsVo.title}</li>
      </ol>

      <div class="panel no-radius news-detail-panel">
        <div class="panel-heading clearfix tt">
          <h3 class="panel-title pull-left">${requestScope.res_data.newsVo.title}</h3>
          <div class="date pull-right">${requestScope.res_data.newsVo.date}</div>
        </div>
        <div class="panel-body news-detail-content">${requestScope.res_data.newsVo.content}</div>
      </div>
    </div>

    <jsp:include page="partials/footer.jsp"></jsp:include>
  </body>
</html>
