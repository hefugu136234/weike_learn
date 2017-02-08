<%@page import="com.lankr.tv_cloud.model.Resource.Type"%>
<%@page import="com.lankr.tv_cloud.utils.OptionalUtils"%>
<%@page import="com.lankr.tv_cloud.model.Resource.Type"%>
<%@page import="com.lankr.tv_cloud.model.Resource"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	Resource res = (Resource) request.getAttribute("resource");
%>
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
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title class="page_title"><%=OptionalUtils.traceValue(res, "name")%></title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/resource.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script
	src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
</head>

<body>
	<div id="mainDiv" class="page">
		<div class="content videoBg">
			<div class="resource-preview" id="cantainer">
        <img src="" alt="" id="three_cover" style="height: 475px;">
        <div class="cover"></div>
        <a href="javascript:void(0);" class="play-btn icon-play" id="play"></a>
      </div>

			<!-- <div class="pdf_con">
				<img id="three_cover" style="height: 475px;" src="" alt="">
				<div class="cover" style="height: 480px">
					<a id="play" target="_blank" href="javascript:void(0);"
						class="playBtn icon-play"></a>
				</div>
			</div> -->
		</div>
	</div>
	<div id="id_video_container"></div>
	<script type="text/javascript">
	$(document).ready(function() {
	<%if (res == null || !res.apiUseable()) {%>
		alert('无效的资源访问');
	<%} else {
				if (res.getType() == Type.VIDEO) {%>
			renderVideo('<%=OptionalUtils.traceValue(res, "video.fileId")%>');
		<%} else if (res.getType() == Type.THREESCREEN) {%>
		renderRes('<%=res.getCoverTaskId()%>', '<%=res.getUuid()%>')
		<%} else if (res.getType() == Type.PDF) {%>
		renderRes('<%=res.getCoverTaskId()%>', '<%=res.getUuid()%>')
	<%}

			}%>
		function renderVideo(fileId) {
								$('#mainDiv').hide();
								var option = {
									"auto_play" : "0",
									"file_id" : fileId,
									"app_id" : "1251442335",
									"width" : 640,
									"height" : 480
								}; /* 调用播放器进行播放 */
								new qcVideo.Player("id_video_container",
										option, function(e) {
										});
							}

							function renderRes(cover, resUuid) {
								$('#three_cover').attr('src', cover)
								$('#id_video_container').hide();
								$('#play').attr(
										'href',
										'/project/resource/shared/' + resUuid
												+ '/play')
							}

			/* 				function renderPdf(cover, resUuid) {
								$('#three_cover').attr('src', cover)
								$('#id_video_container').hide();
								$('#play').attr(
										'href',
										'/api/webchat/pdf/click/open/'
												+ resUuid)
							} */

						})
	</script>
</body>
</html>
