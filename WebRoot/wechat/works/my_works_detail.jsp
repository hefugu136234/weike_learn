<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<title class="page_title"></title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/resource.css" rel="stylesheet">
<link href="/assets/css/app/dropload.css" rel="stylesheet">
<link href="/assets/css/app/upload.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script
	src="http://qzonestyle.gtimg.cn/open/qcloud/video/h5/h5connect.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/works/my_works_detail.js"></script>
</head>

<body>
	<div id="mainDiv" class="page">
		<div class="content">
			<header class="top-bar with-status">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          <span id="oups_name"></span>
        </div>
        <div class="top-status play-times">
					<p>作品编号</p>
					<p class="num" id="p_oups_code"></p>
				</div>
      </header>

			<div id="id_video_container" class="upload-video">
				<div class="video-upload-ing">
					<div class="icon icon-upload-thin"></div>
					<div class="info">
						<p id="id_p1"></p>
						<p id="id_p2"></p>
					</div>
				</div>
			</div>

			<ul class="upload-process clearfix">
				<li>
					<div class="tt">初始状态</div>
					<div class="tag">
						<div class="arr"></div>
					</div>
				</li>
				<li>
					<div class="tt">收到作品</div>
					<div class="tag">
						<div class="arr"></div>
					</div>
				</li>
				<li>
					<div class="tt">初审中</div>
					<div class="tag">
						<div class="arr"></div>
					</div>
				</li>
				<li>
					<div class="tt">作品转码</div>
					<div class="tag">
						<div class="arr"></div>
					</div>
				</li>
				<li>
					<div class="tt">专业审核</div>
					<div class="tag">
						<div class="arr"></div>
					</div>
				</li>
				<li>
					<div class="tt">上线</div>
					<div class="tag">
						<div class="arr"></div>
					</div>
				</li>
			</ul>

			<div class="resource-intro mt10">
				<div class="icon-title mb-1 white bold clearfix">
					<h5 class="tt bfL">
						<span class="icon icon-detail"></span>简 介
					</h5>
				</div>

				<div class="intro">
					<div class="clearfix">
						<div class="col-2">
							<span class="tt">讲者：</span><span id="video_speaker"></span>
						</div>
						<div class="col-2">
							<span class="tt">时长：</span><span id="video_time"></span>
						</div>
					</div>
					<p><span class="tt">名称：</span><span id="video_name_s"></span></p>
					<p><span class="tt">日期：</span><span id="video_date"></span></p>
					<p><span class="tt">简介：</span><span id="video_intro"></span></p>
				</div>
			</div>

			<div class="comments-content mt10">
				<div class="icon-title mb-1 white bold clearfix">
					<h5 class="tt bfL">
						<span class="icon icon-message"></span>反 馈
					</h5>
					<button type="button" class="button btn-cyan pull-right" id="sub_comments">发 送</button>
				</div>
				<textarea name="comments" class="comments-content-text" id="video_comments_text" rows="3" placeholder="请输入反馈内容" maxlength="230"></textarea>

				<div id="oups_comments_div">
					<ul class="list" id="oups_comments_ul"></ul>
				</div>

			</div>

		</div>
	</div>
	<textarea class="hide" id="data_json">${requestScope.oups_detail}</textarea>
	<script type="text/javascript">
		$(function() {
			//处理页面数据
			var data_json = $('#data_json').text();
			data_json = JSON.parse(data_json);
			if (!!data_json) {
				dealDataInit(data_json);
			}
		});
	</script>
	<!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
