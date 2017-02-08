<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta name="wxc-sitekey" content="a8643">
<meta name="wxc-baseurl" content="http://v.wedocrm.cn/ap/">
<meta name="wxc-usertoken" content="010-01540bb1bca64d1e">
<meta name="wxc-userappid" content="wx46e60f0e8ed323a0">
<meta name="wxc-userapptype" content="1">
<meta name="wxc-componentappid" content="wx95c7ff09332c8789">
<meta name="wxc-ext-generateVer" content="xPortal_2.1">
<meta name="wxc-ext-generateTime" content="20160422162220">
<title>test</title>
<meta name="description" content="">
<link
	href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css"
	type="text/css" rel="stylesheet">
<link
	href="http://cdn.bootcss.com/font-awesome/4.5.0/css/font-awesome.min.css"
	type="text/css" rel="stylesheet">
<link href="http://img1.qidapp.cn/wxcrm/wdui/wdui.css?v=0.4"
	type="text/css" rel="stylesheet">

<style type="text/css">
body, .bd, .wdui {
	background-color: #00dbff
}

body {
	padding: 0;
	background: #00dbff url(http://img1.qidapp.cn/image/s.gif) no-repeat
		center 0;
	background-size: 100% auto;
}

#scratch {
	margin: 0;
	text-align: center;
	background:
		url(http://img1.qidapp.cn/wxcrm/site/activity/scratch/main-top.png)
		no-repeat center 0;
	background-size: 360px;
}

#scratch .top-area {
	min-height: 380px;
	position: relative;
}

.top-area .scr-block {
	position: absolute;
	width: 216px;
	height: 60px;
	line-height: 60px;
	font-size: 24px;
	color: #666;
	font-weight: 400;
	background-color: #ccc;
	top: 143px;
	left: 50%;
	margin-left: -108px;
}

.top-area .scr-block canvas {
	left: 0;
	top: 0;
	margin: 0;
}

.top-area .scr-block span {
	color: #fff;
}

.info-box {
	background-color: #fff;
	box-shadow: 0 0 6px rgba(0, 0, 0, 0.5);
	margin: 14px;
	margin-bottom: 18px;
	border-radius: 5px;
}

.info-box .info-box-inner {
	border: 1px dashed #ccc;
	border-radius: 5px;
}

.info-box-inner h4 {
	background:
		url(http://img1.qidapp.cn/wxcrm/site/activity/roundabout/bg-title.png)
		no-repeat 0 0;
	margin: 0;
	height: 22px;
	line-height: 22px;
	font-size: 16px;
	padding-left: 9px;
	color: #fff;
	border-top-left-radius: 4px;
}

.info-box-inner>div {
	padding: 12px 15px;
	color: #3d90f0;
	line-height: 1.4;
	font-size: 14px;
}
</style>
</head>
<body class="wdui">
	<div id="sharePicUrl">
		<img src="http://img1.qidapp.cn/wxcrm/img/picurl-scratch.png">
	</div>
	<div class="bd">
		<div id="scratch">
			<div class="top-area">
				<div class="scr-block">
					<span></span>
				</div>
			</div>
		</div>
		<div class="info-box">
			<div class="info-box-inner">
				<h4>奖项设置</h4>
				<div>
					<ul style="padding-left: 16px; margin-bottom: 0;"
						class="lucky -detail">
						<li data-level="1"><span data-level="1">一等奖</span>：1。共<span
							class="total">1</span>份</li>
						<li data-level="2"><span data-level="2">二等奖</span>：2。共<span
							class="total">2</span>份</li>
						<li data-level="3"><span data-level="3">三等奖</span>：3。共<span
							class="total">3</span>份</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="info-box">
			<div class="info-box-inner">
				<h4>活动说明</h4>
				<div>test</div>
			</div>
		</div>
	</div>
	<style type="text/css">
.tipsbox {
	text-align: center;
	position: absolute;
	left: 0;
	right: 0;
	top: 0;
	z-index: 1999;
}

.tipsbox h1 {
	font-size: 26px;
	color: #f9b100;
	margin-top: 200px;
	margin-bottom: 6px;
	font-weight: bold;
	z-index: 1999;
}

.tipsbox h2 {
	color: #fff;
	font-size: 18px;
	padding: 0 8%;
	line-height: 1.5;
	z-index: 1999;
	margin-top: 0
}

.tipsbox p {
	color: #fff;
	font-size: 13px;
	margin: 9px 8%;
}

.tipsbox.luck {
	background: url(http://img1.qidapp.cn/wxcrm/site/activity/cupbg.png)
		no-repeat center -180px;
	background-size: 280px auto;
}

.btn-wrap {
	margin: 0;
	margin-top: 30px;
	text-align: center;
}

.btn-wrap .btn.wdui-btn {
	width: 84%;
	border: 0;
	font-size: 15px;
}

.btn-wrap.col2 .btn.wdui-btn {
	width: 41%;
	margin: 0 1%;
}

.btn-wrap.col2 .btn.wdui-btn:first-chile {
	margin-left: 0;
}

.btn-wrap.col2 .btn.wdui-btn:last-chile {
	margin-right: 0;
}

.input-wrap {
	margin-bottom: -15px;
}

.input-wrap input {
	width: 84%;
	border: 0 none;
	border-radius: 0;
	padding: 4px 6px;
	border-bottom: 1px solid #fff;
	margin-bottom: 6px;
	background-color: transparent;
	color: #fff;
	font-size: 15px;
	text-align: center;
}

#lucky_tip_box, #nolucky_tip_box {
	display: none
}

#nolucky_tip_box h1 {
	font-size: 21px;
	margin-top: 30%;
	margin-bottom: 18px;
}

#nolucky_tip_box h2 {
	font-size: 15px;
	margin: 0;
}

.copyright {
	background-color: rgba(0, 0, 0, 0.2);
	color: #fff !important;
	padding: 7px;
}

.wdui-share-tips {
	z-index: 1999
}
</style>
	<div id="lucky_tip_box">
		<div class="modal-backdrop"></div>
		<div class="tipsbox luck">
			<h1>中奖啦</h1>
			<h2></h2>
			<div class="input-wrap">
				<input type="text" id="name" placeholder="请输入姓名"> <input
					type="tel" id="phone" placeholder="请输入手机号码" maxlength="11">
				<p></p>
			</div>
			<div class="btn-wrap col2">
				<a class="btn btn-default wdui-btn">提交兑奖信息</a> <a
					class="btn btn-success wdui-btn">告诉小伙伴</a>
			</div>
		</div>
	</div>
	<div id="nolucky_tip_box">
		<div class="modal-backdrop"></div>
		<div class="tipsbox">
			<h1>很遗憾你没有中奖</h1>
			<h2></h2>
			<div class="btn-wrap col2">
				<a class="btn btn-default wdui-btn">再来一次</a> <a
					class="btn btn-success wdui-btn">告诉小伙伴</a>
			</div>
		</div>
	</div>
	<div class="copyright">&copy;xx技术支持</div>
</body>
</html>
