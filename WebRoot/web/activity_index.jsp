<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <meta http-equiv="X-UA-Compatible" content="IE=9">
    <meta name="renderer" content="webkit">
    <title>知了微课 - 活动</title>
    <link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
    <link rel="stylesheet" media="all" href="//cdn.bootcss.com/flickity/1.2.1/flickity.min.css">
    <link rel="stylesheet" media="all" href="//cdn.bootcss.com/Swiper/3.3.1/css/swiper.min.css">
    <link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="//cdn.bootcss.com/flickity/1.2.1/flickity.pkgd.min.js"></script>
    <script src="//cdn.bootcss.com/Swiper/3.3.1/js/swiper.min.js"></script>
    <script src="/assets/js/web/common.js"></script>
    <script src="/assets/js/web/activity.js"></script>

    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <jsp:include page="partials/top.jsp"></jsp:include>
    <jsp:include page="partials/top_banner.jsp"></jsp:include>

    <div class="activity-hot-list">
      <div class="container">
        <div class="title-tag mt16 clearfix">
          <h5 class="tt pull-left"><span class="icon icon-flag-solid"></span>热门活动</h5>
        </div>
        <ul class="list row">
        <c:forEach var="item" items="${requestScope.data_total.activityList}">
          <li class="col-xs-12 col-sm-12 col-md-4 item"><a href="/f/web/activity/detail/${item.uuid}"><img src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder_2x1.jpg'}" /></a></li>
          </c:forEach>
        </ul>
      </div>
    </div>

    <div class="activity-new-list">
      <div class="container">
        <div class="title-tag mt16 clearfix">
          <h5 class="tt pull-left"><span class="icon icon-tv-solid"></span>最新直播</h5>
        </div>

        <div class="activity-new-scroll mt16">
          <div class="list swiper-container" id="activity_new_list">
            <div class="swiper-wrapper">

             <c:forEach var="item" items="${requestScope.data_total.latestLiveList}">
              <div class="swiper-slide thumbnail resource-item">
                <a href="/f/web/live/detail/${item.uuid}" class="img">
                  <img src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder.jpg'}" />
                  <div class="img-tag br"><span class="icon icon-users"></span>${item.bookNum}</div>
                </a>
                <div class="caption desc">
                  <h3 class="tt"><a href="/f/web/live/detail/${item.uuid}" data-minstr="40" class="limit-str-text">${item.name}</a></h3>
                  <p class="time">${item.remainDesc}</p>
                </div>
              </div>
             </c:forEach>

            </div>
            <div class="swiper-pagination" id="activity_new_pagination"></div>
          </div>

          <div class="swiper-button-next" id="activity_new_next"></div>
          <div class="swiper-button-prev" id="activity_new_prev"></div>

        </div>

      </div>
    </div>

    <div class="container">
      <div class="title-tag mt16 clearfix">
        <h5 class="tt pull-left"><span class="icon icon-clock-solid"></span>精彩回顾</h5>
      </div>

      <div class="category-list mt16 col-2">
        <div id="wondeReviewList_div" class="row">

         <c:forEach var="item" items="${requestScope.data_total.wondeReviewList}">
          <div class="item">
            <div class="media">
              <div class="media-left img">
                <a href="/f/web/live/detail/${item.uuid}">
                  <img class="media-object" src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder.jpg'}" width="132" alt="">
                  <div class="img-tag br"><span class="icon icon-users"></span>${item.bookNum}</div>
                </a>
              </div>
              <div class="media-body info">
                <h4 class="media-heading tt"><a href="/f/web/live/detail/${item.uuid}" data-minstr="40" class="limit-str-text">${item.name}</a></h4>
                <p class="end-time">结束：${item.endDate}</p>
              </div>
            </div>
          </div>
          </c:forEach>

        </div>
      </div>

      <div class="foot-page">
      <input type="hidden" id="liveCount" value="${requestScope.data_total.liveCount}"/>
        <ul id="wondeReviewList_page" class="pagination">
        </ul>
      </div>
    </div>

    <jsp:include page="partials/footer.jsp"></jsp:include>
  </body>
</html>
