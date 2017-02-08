<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
    <meta http-equiv="X-UA-Compatible" content="IE=9">
    <meta name="renderer" content="webkit">
    <title>${requestScope.activity_data.activityData.name}</title>
    <link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
    <link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="/assets/js/web/common.js"></script>
    <script src="/assets/js/web/activity_detail.js"></script>

    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <jsp:include page="partials/top.jsp"></jsp:include>

    <div class="activity-slogen">
      <div class="container">
        <ol class="breadcrumb crumb-nav">
          <li><a href="/f/web/index">首页</a></li>
          <li><a href="/f/web/activity/list/page">活动</a></li>
          <li class="active">${requestScope.activity_data.activityData.name}</li>
        </ol>

        <div class="media slogen-con">
          <div class="media-left img">
            <img class="media-object" src="${not empty requestScope.activity_data.activityData.kv ? requestScope.activity_data.activityData.kv : '/assets/img/web/placeholder.jpg'}" alt="activity slogen">
          </div>
          <div class="media-body info">
            <h4 class="media-heading tt">${requestScope.activity_data.activityData.name}</h4>
            <div class="intro">${requestScope.activity_data.activityData.description}</div>
          </div>
        </div>

        <ul class="nav nav-pills category-tabs col-3" role="tablist">
          <li role="presentation" class="active">
            <a href="#activity_list" aria-controls="activity_list" role="tab" data-toggle="tab">活动作品</a>
          </li>
          <li role="presentation">
            <a href="#activity_news" aria-controls="activity_news" role="tab" data-toggle="tab">活动报道</a>
          </li>
          <li role="presentation">
            <a href="#activity_rank" aria-controls="activity_rank" role="tab" data-toggle="tab">排行榜</a>
          </li>
        </ul>

        <!-- <div class="upload-btn">
          <div class="icon icon-cloud-up"></div>
          <h5 class="tt">作品提交</h5>
        </div> -->

      </div>
    </div>

    <div class="tab-content">
      <div role="tabpanel" class="tab-pane active" id="activity_list">

        <c:if test="${not empty requestScope.activity_data.recommenList}">
          <div class="activity-recommend">
            <div class="container">
              <div class="title-tag mt16 clearfix">
                <h5 class="tt pull-left"><span class="icon icon-fire"></span>热门推荐</h5>
              </div>

              <div class="row resource-list mt10">
                <c:forEach items="${requestScope.activity_data.recommenList}" var="item">
                  <div class="col-xs-12 col-sm-3 col-md-3 item">
                    <div class="thumbnail resource-item">
                      <a href="/f/web/resource/detail/${item.uuid}" class="img">
                        <img src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder.jpg'}" />
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
                        <div class="img-tag br"><span class="icon icon-eye"></span>${item.viewCount}</div>
                      </a>
                      <div class="caption desc">
                        <h3 class="tt"><a href="/f/web/resource/detail/${item.uuid}" data-minstr="40" class="limit-str-text">${item.name}</a></h3>
                        <p><span>${item.code}</span> | <span>${item.speakerName}</span></p>
                        <p><span data-minstr="20" class="limit-str-text">${item.catgoryName}</span> | <span data-minstr="20" class="limit-str-text">${item.hospitalName}</span></p>
                      </div>
                    </div>
                  </div>
                </c:forEach>

              </div>
            </div>
          </div>
        </c:if>

        <div class="container mt20">
          <c:choose>
            <c:when test="${not empty requestScope.activity_data.allOupsList}">
              <div class="title-tag mt16 clearfix">
                <h5 class="tt pull-left"><span class="icon icon-film"></span>所有作品</h5>
              </div>

              <div class="category-list mt16 col-2">
                <div class="row" id="allOupsList_con">
                  <c:forEach items="${requestScope.activity_data.allOupsList}" var="item">
                    <div class="item">
                      <div class="media">
                        <div class="media-left img">
                          <a href="/f/web/resource/detail/${item.uuid}">
                            <img class="media-object" src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder.jpg'}" width="132" alt="">
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
                            <div class="img-tag br"><span class="icon icon-eye"></span>${item.viewCount}</div>
                          </a>
                        </div>
                        <div class="media-body info">
                          <h4 class="media-heading tt"><a href="/f/web/resource/detail/${item.uuid}" data-minstr="40" class="limit-str-text">${item.name}</a></h4>
                          <p><span>${item.code}</span> | <span>${item.speakerName}</span></p>
                          <p><span data-minstr="20" class="limit-str-text">${item.catgoryName}</span> | <span data-minstr="20" class="limit-str-text">${item.hospitalName}</span></p>
                        </div>
                      </div>
                    </div>
                  </c:forEach>
                </div>
              </div>

              <div class="foot-page">
                <input type="hidden" id="allOupsCount" value="${requestScope.activity_data.allOupsCount}" />
                <ul class="pagination" id="allOupsList_page"></ul>
              </div>
            </c:when>

            <c:otherwise>
              <div class="content-empty">
                <div class="icon icon-empty"></div>
                <div class="tips">
                  <p>内容暂无</p>
                  <p>去其他栏目看看吧</p>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <div role="tabpanel" class="tab-pane" id="activity_news">
        <div class="container mt20">
          <c:choose>
            <c:when test="${not empty requestScope.activity_data.reportList}">
              <div class="title-tag clearfix">
                <h5 class="tt pull-left">活动报道</h5>
              </div>

              <div class="category-list mt16 col-2">
                <div class="row">
                  <c:forEach items="${requestScope.activity_data.reportList}" var="item">
                    <div class="item">
                      <div class="media">
                        <div class="media-left img">
                          <a href="/f/web/resource/detail/${item.uuid}">
                            <img class="media-object" src="${not empty item.cover ? item.cover : '/assets/img/web/placeholder.jpg'}" width="132" alt="">
                          </a>
                        </div>
                        <div class="media-body info">
                          <h4 class="media-heading tt"><a href="/f/web/resource/detail/${item.uuid}" data-minstr="40" class="limit-str-text">${item.name}</a></h4>
                          <p class="desc limit-str-text" data-minstr="70">${item.desc}</p>
                        </div>
                      </div>
                    </div>
                  </c:forEach>
                </div>
              </div>

              <!-- <div class="foot-page">
                <ul class="pagination"></ul>
              </div> -->
            </c:when>

            <c:otherwise>
              <div class="content-empty">
                <div class="icon icon-empty"></div>
                <div class="tips">
                  <p>内容暂无</p>
                  <p>去其他栏目看看吧</p>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <div role="tabpanel" class="tab-pane" id="activity_rank">
        <div class="container mt20">
          <c:choose>
            <c:when test="${not empty requestScope.activity_data.rankList}">
              <table class="table table-striped table-hover activity-rank-list">
                <thead>
                  <tr>
                    <th></th>
                    <th>排 名</th>
                    <th class="name">作品名称</th>
                    <th>作 者</th>
                    <th>编 号</th>
                    <th>分 类</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${requestScope.activity_data.rankList}" var="item" varStatus="index">
                    <tr <c:if test="${index.count eq 1}"> class="first"</c:if><c:if test="${index.count eq 2}"> class="second"</c:if><c:if test="${index.count eq 3}"> class="third"</c:if>>
                      <td><span class="icon icon-cup"></span></td>
                      <td class="num">${index.count}</td>
                      <td class="name"><a href="/f/web/resource/detail/${item.uuid}">${item.name}</a></td>
                      <td class="author"><img alt="" src="${not empty item.speakerPhoto ? item.speakerPhoto:'/assets/img/app/default_img.jpg'}" class="avatar">${item.speakerName}</td>
                      <td class="code">${item.code}</td>
                      <td class="catgory">${item.catgoryName}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:when>

            <c:otherwise>
              <div class="content-empty">
                <div class="icon icon-empty"></div>
                <div class="tips">
                  <p>内容暂无</p>
                  <p>去其他栏目看看吧</p>
                </div>
              </div>
            </c:otherwise>

          </c:choose>
        </div>
      </div>
    </div>

    <input type="hidden" id="activity_uuid" value="${requestScope.activity_data.activityData.uuid}" />

    <jsp:include page="partials/footer.jsp"></jsp:include>
  </body>
</html>
