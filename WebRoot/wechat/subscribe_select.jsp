<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>知了云盒公测预约</title>
    <link rel="stylesheet" media="all" href="/assets/css/app/default.css" />
    <link rel="stylesheet" media="all" href="/assets/css/app/subscribe.css" />
  </head>

  <body>
    <div class="subscribe_select">
      <h5 class="tt">请选择您的用户类型</h5>
      <div class="btnGroup">
        <a href="javascript:;" class="btn subscribeBtn">品牌用户</a>
        <!-- <a href="/f/wx/normal" class="btn normalBtn">普通用户</a> -->
        <a href="javascript:;" class="btn normalBtn">普通用户</a>
      </div>
    </div>
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
      $('.normalBtn').click(function(){
        alert('您好，公测申请目前只针对品牌用户，请继续关注"知了云盒"微信平台。');
      });

      $('.subscribeBtn').click(function(){
        var starttime = new Date('2015/09/22 11:30');
        var endtime = new Date('2015/09/25 11:30');
        var nowtime = new Date();
        var leftsecond = parseInt((starttime.getTime()-nowtime.getTime())/1000);
        var endsecond = parseInt((endtime.getTime()-nowtime.getTime())/1000);
        if (leftsecond <= 0){
          $(this).attr('href', '/f/wx/brand');
        }
        else if (endsecond <= 0) {
      	  alert('第一批公测申请已结束,请继续关注"知了云盒"微信平台，"知了云盒"即将正式发布,Coming Soon …');
        }
        else {
      	  alert('公测申请将于9月23日12点正式开始，请您耐心等待。');
        }

      });
    });
    </script>
  </body>
</html>
