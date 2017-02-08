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
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/disposable.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
	<script type="text/javascript" src="/assets/js/wechat/shake.js"></script>
	<script type="text/javascript" src="/assets/js/wechat/games/template/1_yaoyiyao.js"></script>
	
	
<title>${not empty requestScope.lottery ? requestScope.lottery.name : '摇一摇' }</title>	
</head>

<body>
	<div class="page shakeAward">
		<input type="hidden" name="uuid" id="uuid" value="${requestScope.lottery.uuid }" /> 
		<input type="hidden" name="rules" id="rules" value="${requestScope.lottery.rules }" /> 

		<div class="content index" id="envelopes_index">
			<div class="logo">
				<a href="/api/webchat/index"><img
					src="/assets/img/app/shakeAward/logo.png" width="100%"></a>
			</div>
			<div class="title active">
				<img src="/assets/img/app/shakeAward/title.png" width="86%">
			</div>
			<div class="tag">
				<img src="/assets/img/app/shakeAward/tag.png" width="96%">
			</div>
			<div class="gift_box">
				<img src="/assets/img/app/shakeAward/gift_box.png" width="60%">
			</div>
		</div>

		<div class="content result hide" id="envelopes_result">
			<div class="logo">
				<a href="/api/webchat/index"><img
					src="/assets/img/app/shakeAward/logo.png" width="100%"></a>
			</div>
			<div class="tips">
				<img src="/assets/img/app/shakeAward/tt_tag.png" width="91%">
				<p class="tt" id="envelopes_tips"></p>
			</div>

			<div class="gift_box hide" id="winning">
				<img src="/assets/img/app/shakeAward/winning.png" width="100%"><img
					src="/assets/img/app/shakeAward/winning_img.png" width="100%"
					class="img">
			</div>
			<div class="gift_box" id="winning_no">
				<img src="/assets/img/app/shakeAward/winning_no.png" width="100%"><img
					src="/assets/img/app/shakeAward/winning_img_no.png" width="100%"
					class="img">
			</div>

			<div class="float_intro" id="envelopes_intro">
				<h5 class="tt">您知道吗？</h5>
				<div id="float_intro_div" class="info"></div>
			</div>
		</div>

		<div class="btn_group clearfix">
			<div class="btn" id="btn_rule">活动规则</div>
			<div class="btn" id="btn_record">抽奖记录</div>
		</div>

		<div class="float_info" id="info_rule">
			<h5 class="tt">活动规则</h5>
			<div class="info" id="info">
				
			</div>
			<div class="close">╳</div>
		</div>
		<div class="float_info" id="info_record">
			<h5 class="tt">抽奖纪录</h5>
			<ul id="record_list_ul" class="record_list"></ul>
			<div class="close">╳</div>
		</div>

	</div>

	<audio id="bg_music" preload="metadata" controls autoplay="false"></audio>


</body>
</html>
