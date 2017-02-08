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
    <title>编辑收货地址</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/address.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/address/address_common.js"></script>
  </head>

  <body>
    <div class="page">
      <div class="content">
        <header class="top-bar">
          <div class="crumb-nav">
            <a href="/api/webchat/my/center" class="logo icon-logo"></a>
            <a href="/api/webchat/shop/address/list/page">收货地址管理</a>
            编辑收货地址
          </div>
        </header>

        <div id="addressForm">
          <input type="hidden" id="item_uuid" value="${requestScope.address_data.uuid}">

          <div class="default-form mt10">
            <label class="form-group">
              <div class="form-label">收件人姓名：</div>
              <div class="form-input">
                <input type="text" name="name" id="name" class="form-control" placeholder="收件人姓名不得为空" value="${requestScope.address_data.name}">
              </div>
            </label>

            <label class="form-group">
              <div class="form-label">手机号码：</div>
              <div class="form-input">
                <input type="tel" name="phone" id="phone" class="form-control" placeholder="请填写您的手机号码" value="${requestScope.address_data.phone}">
              </div>
            </label>
          </div>

          <div class="default-form mt10">
            <label id="city" class="form-group form-select">
              <div class="form-label">所在城市：</div>
              <div class="form-input">
                <div class="form-control select-text" data-uuid="${address_data.selectCityUuid}">${address_data.selectCityName}</div>
              </div>
            </label>
            <label id="district" class="form-group form-select">
              <div class="form-label">所在区域：</div>
              <div class="form-input">
                <div class="form-control select-text" data-uuid="${address_data.selectDistrictUuid}">${address_data.selectDistrictName}</div>
              </div>
            </label>
            <label class="form-group">
              <div class="form-label">详细地址：</div>
              <div class="form-input">
                <input type="text" name="address" id="address" class="form-control" maxlength="150" placeholder="请填写您的详细地址" value="${requestScope.address_data.address}">
              </div>
            </label>
          </div>

          <c:if test="${requestScope.address_data.defaultAdress eq 0}">
            <div class="default-form mt10">
              <label class="form-group set-address-default" id="set_default">
                <div class="form-label">设为默认地址</div>
                <div class="icon icon-checked"></div>
              </label>
            </div>
          </c:if>

          <div class="p10">
            <button type="button" id="update_address_submit" class="button btn-full btn-cyan btn-radius">保 存</button>
          </div>

        </div>
      </div>
    </div>
  </body>
</html>
