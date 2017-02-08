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
<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
<title>核对兑换信息</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/address.css" rel="stylesheet">
<link href="/assets/css/app/shop.css" rel="stylesheet">
</head>

<body>
  <div class="page">
    <div class="content">
      <header class="top-bar">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          <a href="/api/webchat/wx/jifen/shop">积分商场</a>
          核对兑换信息
        </div>
      </header>

      <div class="icon-title bold white mt10 mb-1 clearfix">
        <h5 class="tt bfL">兑换商品信息</h5>
      </div>

      <input type="hidden" id="consume_uuid" value="${requestScope.comfirm_data.consumeUuid}">

      <div class="exchange-confirm clearfix">
        <div class="img"><img src="${requestScope.comfirm_data.consumeCover}"></div>
        <div class="info">
          <h5 class="tt">${requestScope.comfirm_data.consumeName}</h5>
          <p>所需积分：<span id="num_span" class="points">${requestScope.comfirm_data.consumeIntegral}</span></p>
        </div>
      </div>

      <c:if test="${requestScope.comfirm_data.needAddress}">
        <input type="hidden" id="address_uuid" value="${requestScope.comfirm_data.addressUuid}">

        <div class="icon-title bold white mt10 mb-1 clearfix">
          <h5 class="tt bfL">收件人信息</h5>
        </div>
        <ul class="address-list" id="address_list">
          <li class='current'>
            <div class="box">
              <div class="info clearfix">
                <div class="name">${requestScope.comfirm_data.name}<span class="tag">默 认</span></div>
                <div class="tel">${requestScope.comfirm_data.phone}</div>
              </div>
              <div class="detail">${requestScope.comfirm_data.address}</div>
            </div>
          </li>
        </ul>
      </c:if>

    </div>

    <div class="foot-statistics-bar">
      <div class="btns">
        <a href="javascript:;" class="btn btn-cyan" id="dealclick">确认兑换</a>
      </div>
    </div>

    <input id="shop_uuid" type="hidden" value="${requestScope.uuid}"/>
  </div>
  <script type="text/javascript" src="/assets/js/jquery.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      $('#dealclick').click(function(){
        var consume_uuid = $('#consume_uuid').val();
        var address_uuid = $('#address_uuid').val();

        $.post(
          '/api/webchat/jifen/exchange/goods',
          {
            'uuid': consume_uuid,
            'addressUuid': address_uuid
          },
          function(data){
            if(data.status=='success'){
              var value='您的商品兑换成功，消耗了'+$('#num_span').html()+'积分';
              alert(value);
              location.href='/api/webchat/wx/jifen/shop';
            }else{
              alert(data.message);
            }
          }
        );
      });
    });
  </script>
</body>
</html>
