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
    <title>修改${vo_data.title}用户信息</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/change_info.js"></script>
  </head>

  <body>
    <div class="page">
      <div class="content pb70">
        <div class="default-form">
          <label class="form-group">
            <div class="form-label">用户姓名：</div>
            <div class="form-input">
              <input type="text" name="name" id="name" class="form-control" placeholder="用户姓名不得为空"
               value="${vo_data.name}"/>
             </div>
          </label>
        </div>

        <div class="default-form mt10">
          <input type="hidden" id="user_type" value="${vo_data.type}"/>
          <c:if test="${vo_data.type eq 0}">
            <!-- 医生 -->
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
            <!-- 医生 -->
           </c:if>

           <c:if test="${vo_data.type eq 1}">
            <!-- 企业 -->
            <label id="company" class="form-group form-select">
              <div class="form-label">企业名称：</div>
              <div class="form-input">
                <div class="form-control select-text" data-uuid="${vo_data.companyUuid}">${vo_data.companyName}</div>
              </div>
            </label>
            <!-- 企业 -->
          </c:if>

          <label class="form-group">
            <div class="form-label">职　　称：</div>
            <div class="form-input">
              <input type="text" name="professor" id="professor" class="form-control" maxlength="80" value="${vo_data.professor}"/>
            </div>
          </label>
        </div>

        <div class="list-with-icon white-item with-arr mt10">
          <a href="/api/webchat/change/page/psw" class="box">
            修改密码
            <div class="icon icon-arr-r"></div>
          </a>
          <a href="/api/webchat/forget/password" class="box">
            忘记密码
            <div class="icon icon-arr-r"></div>
          </a>
        </div>

        <div class="p10">
          <button type="button" id="submitBtn" class="button btn-full btn-cyan btn-radius">提 交</button>
        </div>

        <div class="foot-btns-area p10">
          <button type="button" id="out_login" class="button btn-full btn-cyan-line btn-radius"><span class="icon icon-off"></span>安全登出</button>
        </div>

      </div>
    </div>
  </body>
</html>
