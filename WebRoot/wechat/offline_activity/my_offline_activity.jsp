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
<meta content="black-translucent"
	name="apple-mobile-web-app-status-bar-style" />
<title>我报名的活动</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/offline_activity.css" rel="stylesheet">

<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
<script type="text/javascript"
	src="/assets/js/wechat/offline_activity/my_offline_activity.js"></script>
</head>

<body>
	<div class="page">
		<div class="tab-control">
			<ul class="tab-control-tag select-tag with-bg">
				<li class="btn active">待审核<sup class="num">${waitCheck}</sup></li>
				<li class="btn">报名成功<sup class="num">${checked}</sup></li>
			</ul>

			<div class="tab-content active">
				<div id="un_check_div">
					<div id="un_check_ul" class="list-with-img with-arr p10">
						<%-- <a href="/api/webchat/offline/activity/detail/${item.uuid}"
							class="box">
							<div class="img">
								<img src="">
							</div>
							<div class="info">
								<h5 class="tt limit-str-text" data-minstr="34">2016医疗市场年会</h5>
								<div class="desc">
									<p>
										人数：<span class="num">60</span>/100
									</p>
									<p>报名时间：2016/08/01~08/20</p>
								</div>
							</div>
							<div class="icon icon-arr-r"></div>
						</a> --%>
					</div>
				</div>
			</div>
			<div class="tab-content">
				<div id="checked_div">
					<div id="checked_ul" class="list-with-img with-arr p10"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
