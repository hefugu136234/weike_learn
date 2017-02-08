<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="renderer" content="webkit">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>知了微课 - 直播活动</title>
<link rel="icon" href="/assets/favicon.ico" type="image/x-ico">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" media="all" href="/assets/css/app/font.css" />
<link rel="stylesheet" media="all" href="/assets/css/web/web.css" />

<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="/assets/js/web/common.js"></script>
</head>

<body class="deep-color-bg">
	<div class="live-con-bg" style="background-image: url('${vo_data.bgUrl}');">
		<div class="live-slogen">
			<div class="container">
				<div class="live-main-title mt32"><img src="/assets/img/web/logo_line.png" class="logo-img" />${vo_data.name}</div>

				<div class="slogen-con mt32">
					<!-- <div class="tt">
						${requestScope.broadcast_data.name}<span class="tag">${requestScope.broadcast_data.integralVal}</span>
					</div> -->
					<div class="info">
						<div class="date">
							<div>结束时间：${vo_data.endDate}</div>
							<div class="countdown" data-times="${vo_data.countDownStart}">
								<span class="icon icon-countdown"></span><span class="num day-show">0</span>天<span class="num hour-show">0</span>小时<span class="num minute-show">0</span>分<span class="num second-show">0</span>秒
							</div>
						</div>
					</div>
				</div>

				<div class="live-status-group">
          <div class="status"><span class="icon icon-users"></span>已参加：<span class="current">${vo_data.bookNum}</span>/${vo_data.limitCountVal}</div>
          <div class="btns">
             <a id="plat_redirect" class="btn btn-lg btn-orange long" href="${vo_data.liveUrl}">观看直播</a>
          </div>
        </div>

			</div>
		</div>

		<div class="live-con">
			<div class="container">
				<div class="live-introduction">
					<h4 class="tt">简 介</h4>
					<div class="info">${broadcast_data.description}</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="uuid" value="${broadcast_data.uuid}"/>
	</div>
  <script type="text/javascript">
    var error = '${requestScope.error}';
    var url='${vo_data.liveUrl}';
   // alert('dd')
    $(function() {
		if (!!error) {
			alert(error);
		}else{
			// 直播倒计时
			  $('.countdown').each(function () {
			    var $that = $(this);
			    var intDiff = parseInt($that.data('times'));

			    countDownTimer($that, intDiff);
			  });
			
			var uuid=$('#uuid').val();
			$('#plat_redirect').click(function(){
				$.post('/api/tv/broadcast/redirect/thrid/record',{
					uuid:uuid,
					liveUrl:url
				},function(data){
					console.log(data)
				});
			});
		}
	});
 </script>
</body>
</html>
