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
	href="//cdn.bootcss.com/flickity/1.2.1/flickity.min.css">
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/flickity/1.2.1/flickity.pkgd.min.js"></script>
<script src="/assets/js/web/common.js"></script>
<script src="/assets/js/common.js"></script>
<script src="/assets/js/web/index.js"></script>

<!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="partials/top.jsp"></jsp:include>
	<jsp:include page="partials/top_banner.jsp"></jsp:include>

	<div class="index-activity">
		<div class="container">
			<ul class="list row">
				<c:forEach items="${requestScope.activity_list}" var="item">
					<li class="col-xs-12 col-sm-12 col-md-4 item"><a href="/f/web/activity/detail/${item.uuid}"><img src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder_2x1.jpg'}" /></a></li>
				</c:forEach>
			</ul>
		</div>
	</div>

	<div class="index-plate" id="index_plate">
		<div class="container">
      <c:forEach items="${requestScope.subject_list}" var="item">
        <div class="title-tag mt24 clearfix">
          <h5 class="tt pull-left">
            <span class="icon" data-name="${item.name}"></span>${item.name}
          </h5>
          <a href="/f/web/category/list/${item.uuid}" class="right-arr-item pull-right more">查看更多（${item.resCount}资源）</a>
        </div>

        <ul class="resource-list row">
          <c:forEach items="${item.itemList}" var="child_item">
            <li class="col-xs-12 col-sm-6 col-md-3 item">
              <div class="thumbnail resource-item">
                <a href="/f/web/resource/detail/${child_item.uuid}" class="img">
                  <img src="${not empty child_item.cover ? child_item.cover : '/assets/img/web/placeholder.jpg'}" alt="" />
                  <c:if test="${child_item.type eq 'VIDEO'}">
                    <div class="img-tag tr video-type">视频</div>
                  </c:if>
                  <c:if test="${child_item.type eq 'PDF'}">
                    <div class="img-tag tr pdf-type">PDF</div>
                  </c:if>
                  <c:if test="${child_item.type eq 'THREESCREEN'}">
                    <div class="img-tag tr ppt-type">课件</div>
                  </c:if>
                  <c:if test="${child_item.type eq 'VR'}">
                    <div class="img-tag tr vr-type">VR</div>
                  </c:if>
                  <c:if test="${child_item.bloody}">
                    <div class="bloody-icon">
                      <img alt="" src="/assets/img/app/icon_bloody.png">
                    </div>
                  </c:if>
                  <div class="img-tag br">
                    <span class="icon icon-eye"></span>${child_item.viewCount}
                  </div>
                </a>
                <div class="caption desc">
                  <h3 class="tt">
                    <a href="/f/web/resource/detail/${child_item.uuid}" data-minstr="40" class="limit-str-text">${child_item.name}</a>
                  </h3>
                  <p>
                    <span>${child_item.code}</span> | <span>${child_item.speakerName}</span>
                  </p>
                  <p>
                    <span data-minstr="20" class="limit-str-text">${child_item.catgoryName}</span> | <span data-minstr="20" class="limit-str-text">${child_item.hospitalName}</span>
                  </p>
                </div>
              </div>
            </li>
          </c:forEach>
        </ul>
      </c:forEach>
		</div>
	</div>

	<jsp:include page="partials/footer.jsp"></jsp:include>
</body>
</html>
