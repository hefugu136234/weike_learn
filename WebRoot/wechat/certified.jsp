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
    <title>实名认证</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/certified.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/certified.js"></script>
  </head>

  <body>
    <div class="page">
      <div class="content">
        <!-- <header class="top-bar">
          <div class="crumb-nav">
            <a href="/api/webchat/my/center" class="logo icon-logo"></a>
            实名认证
          </div>
        </header> -->

	<c:if test="${vo_data.certificFlag eq 1}">
        <div class="certified-status success show">
          <div class="tt"><span class="icon"><img src="/assets/img/app/icon_success.png" alt="" width="100%"></span>恭喜您，认证成功</div>
          <div class="tag">认证信息为：</div>
          <div class="info">
            <p><strong>${vo_data.name}</strong></p>
            <p>${vo_data.certifiCredentials}</p>
          </div>
        </div>
       </c:if>

	<c:if test="${vo_data.certificFlag eq 2}">
        <div class="certified-status reject">
          <div class="tt"><span class="icon"><img src="/assets/img/app/icon_reject.png" alt="" width="100%"></span>认证被拒绝</div>
          <div class="info">
            <p>${vo_data.certificMark}</p>
          </div>
        </div>
       </c:if>

	<c:if test="${vo_data.certificFlag eq 0}">
        <div class="certified-status ing">
          <div class="tt"><span class="icon"><img src="/assets/img/app/icon_ing.png" alt="" width="100%"></span>正在审核…</div>
        </div>
        </c:if>

        <div class="icon-title mb-1 white bold clearfix">
          <h5 class="tt bfL">
            <span class="icon icon-upload-thin"></span>上传医师执照或医院工作证/胸牌
          </h5>
        </div>

        <div class="certified-photo">
          <div class="area">
          <c:if test="${vo_data.certificFlag gt 1}">
          <!-- 上传照片 -->
            <div class="take-photo" id="takeCertified">
              <p class="img">
                <span class="icon icon-cloud"></span>
              </p>
              <p class="tt">上传照片</p>
            </div>
            <!-- 上传照片 -->
            </c:if>
            <img src="${vo_data.certificUrl}" id="user_license" class="result-photo wechat-preview-img${vo_data.certificFlag gt 1?' hide':''}"/>
          </div>
        </div>

        <div class="icon-title mb-1 white bold mt10 clearfix">
          <h5 class="tt bfL">
            <span class="icon icon-info"></span>医师执照上传说明
          </h5>
        </div>

        <div class="certificate-intro clearfix">
          <div class="img"><img src="/assets/img/app/certified/example_img.jpg" alt="" width="100%"></div>
          <div class="info">
            <p>请上传本人医师资格证照片</p>
            <p>照片需要清晰，字迹清晰可辨</p>
            <p>需要至少包含姓名，医师资格，执业地点，执业类别，执业范围</p>
            <p>通过审核后，可获得<span class="num">400</span>积分</p>
            <p>实名用户，可以提交作品，赢取更多积分</p>
          </div>
        </div>

        <div class="icon-title mb-1 white bold mt10 clearfix">
          <h5 class="tt bfL">用户信息确认</h5>
          <div class="more bfR" id="open_certified_form">展开全部</div>
        </div>
        <div class="default-form hide" id="certified_form_list">
          <label class="form-group">
            <div class="form-label">用户姓名：</div>
            <div class="form-input">
              <input type="text" name="real_name" id="real_name" class="form-control" placeholder="请输入用户姓名"
               value="${vo_data.name}"/>
             </div>
          </label>
          <label id="city" class="form-group form-select">
            <div class="form-label">所在城市：</div>
            <div class="form-input">
              <div class="form-control select-text" data-uuid="${vo_data.cityUuid}">${vo_data.cityName}</div>
            </div>
          </label>
          <label id="hosipital" class="form-group form-select">
            <div class="form-label">所在医院：</div>
            <div class="form-input">
              <div class="form-control select-text" data-uuid="${vo_data.hospitalUuid}">${vo_data.hospitalName}</div>
            </div>
          </label>
          <label id="department" class="form-group form-select">
            <div class="form-label">所在科室：</div>
            <div class="form-input">
              <div class="form-control select-text" data-uuid="${vo_data.departmentUuid}">${vo_data.departmentName}</div>
            </div>
          </label>
          <label class="form-group">
            <div class="form-label">职　　称：</div>
            <div class="form-input">
              <input type="text" name="professor" id="professor" class="form-control" maxlength="80" value="${vo_data.professor}"/>
            </div>
          </label>
        </div>

	      <c:if test="${vo_data.certificFlag gt 1}">
          <div class="p10">
            <button type="button" id="submitBtn" class="button btn-full btn-cyan btn-radius">实名认证</button>
          </div>
        </c:if>
      </div>
    </div>

    <div class="page" id="certified_feedback">
      <div class="content feedback">
        <div class="p10">
          <div class="info success">
            <div class="icon"><img src="/assets/img/app/certified/icon_success.png" width="19%"></div>
            <div class="sub">
              <p>尊敬的“<span id="userName"></span>”</p>
              <p>您的认证申请正在审核中…</p>
            </div>
          </div>
          <a href="/api/webchat/my/center" class="button btn-full btn-cyan btn-radius">我知道了</a>
        </div>
      </div>
    </div>

    <!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
  </body>
</html>
