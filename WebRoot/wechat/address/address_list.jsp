<%@page import="com.lankr.tv_cloud.web.api.webchat.vo.ApplyVo"%>
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
    <title>收货地址管理</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/dropload.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/address.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
  	<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
  	<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
  </head>

  <body>
    <div class="page">
      <div class="content">
        <!-- <header class="top-bar">
          <div class="crumb-nav">
            <a href="/api/webchat/my/center" class="logo icon-logo"></a>
            收货地址管理
          </div>
        </header> -->

        <div id="address_list_wrapper">
          <ul class="address-list" id="address_list"></ul>
        </div>

        <div class="p10">
          <a href="/api/webchat/shop/address/add/page" class="button btn-full btn-cyan btn-radius">新增收货地址</a>
        </div>
      </div>
    </div>

    <script type="text/javascript">
    var droploadList;
    $(document).ready(function() {
        //实例化下拉插件
        droploadList=dataDropload($('#address_list_wrapper'), $('#address_list'), '/api/webchat/shop/address/list/data', buildDataList, buildUl,'');
    });
    //必须实现方法
    function buildDataList(data) {
        var list = data.receiptAddressVos;
        if ( !! list && list.length > 0) {
        	droploadList.listStartTime = list[list.length - 1].dateTime;
        }
        return list;
    }

    //必须实现的方法
    function buildUl(ul_list, itemList) {
        $.each(itemList,
        function(index, item) {
            var name = item.name;
            var phone = item.phone;
            var address = item.listAddress;
            var defaults = item.defaultAdress;
            var default_class = '';
            var link = '/api/webchat/shop/address/update/page/' + item.uuid;

            if (defaults == 1) {
                default_class = 'current';
            }
            var li = '<li class=' + default_class + '><a href="'
            + link + '" class="box with-arr"><div class="info clearfix"><div class="name">'
            + name + '<span class="tag">默 认</span></div><div class="tel">'
            + phone + '</div></div><div class="detail">'
            + address + '</div></a></li>';
            ul_list.append(li);
        });
    }
    </script>
  </body>
</html>
