<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>${res_vo.name}</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/resource.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
    <script type="text/javascript" src="/assets/player/UtoVRPlayerGuide.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/resource/vr_video_detail.js"></script>
  </head>

  <body>
    <div class="vr-video-container" id="panoDesk"></div>
    <input type="hidden" id="page_reamin_uuid" value="${page_reamin_uuid}" />
    <input type="hidden" id="resource_uuid" value="${res_vo.uuid}" />
    <input type="hidden" id="resource_vrurl" value="${res_vo.video.vrUrl}" />
    <%@ include file="/wechat/common/base_share.jsp"%>
  </body>
</html>
