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
<meta name="full-screen" content="yes">
<meta name="x5-fullscreen" content="true">
<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
<title>兑换记录</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/users.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript" src="/assets/js/wechat/jifen/jifen_common.js"></script>
</head>

<body>
  <div class="page">
    <header class="top-bar transparent">
      <div class="crumb-nav">
        <a href="/api/webchat/my/center" class="logo icon-logo"></a>
        兑换记录
      </div>
    </header>

    <div id="jifen_wrapper">
      <ul id="scoreHisList_ul" class="score-records"></ul>
    </div>
  </div>

  <script type="text/javascript">
    var droploadList;
    $(document).ready(function() {
        //实例化下拉插件
        droploadList=dataDropload($('#jifen_wrapper'), $('#scoreHisList_ul'), '/api/webchat/wx/exchange/list/show', buildDataList, buildUl,'');

    });

    //必须实现方法
    function buildDataList(data) {
        var list = data.list;
        if ( !! list && list.length > 0) {
        	droploadList.listStartTime = list[list.length - 1].dateTime;
        }
        return list;
    }

    function buildUl(scoreHisList_ul, itemList) {
        $.each(itemList,
        function(index, item) {
            var actionName = item.actionName;
            if (item.score < 0) {
                actionName = '兑换“' + actionName + '”';
            }
            var dateTime = item.dateTime;
            var score = deadNum(item.score);
            var li_points;

            if (score > 0){
              li_points = '<div class="box points">积分：<span class="num add">'+score+'</span></div>';
            } else {
              li_points = '<div class="box points">积分：<span class="num">'+score+'</span></div>';
            }
            var typeClass=jifenTypeClass(item.action);
            var li = '<li>'
                   + '<div class="box icon"><span class="'+typeClass+'"></span></div>'
                   + '<div class="box tt">'+actionName+'</div>'
                   + li_points
                   + '<div class="box time">'+dateTime+'</div>'
                   + '</li>';

            scoreHisList_ul.append(li);
        });
    }

    function deadNum(num) {
        var val = '';
        if (num > 0) {
            val = '+' + num;
        } else if (num < 0) {
            val = num;
        } else {
            val = num;
        }
        return val;
    }
  </script>
 <!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
