<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 新页面 原 common_share.jsp-->
<!-- 分享签名的隐藏域 -->
<input type="hidden" id="overall_wx_timestamp"
	value="${wx_signature.timestamp}" />
<input type="hidden" id="overall_wx_nonceStr"
	value="${wx_signature.nonceStr}" />
<input type="hidden" id="overall_wx_signature"
	value="${wx_signature.signature}" />
<input type="hidden" id="overall_wx_appid" value="${wx_signature.appId}" />
<input type="hidden" id="overall_wx_shareType"
	value="${wx_signature.shareType}" />
<input type="hidden" id="overall_wx_url" value="${wx_signature.url}" />
<input type="hidden" id="overall_wx_title" value="${wx_signature.title}" />
<input type="hidden" id="overall_wx_picUrl"
	value="${wx_signature.picUrl}" />
<input type="hidden" id="overall_wx_oriUserUuid"
	value="${wx_signature.oriUserUuid}" />
<textarea class="hide" id="overall_wx_desc">${wx_signature.desc}</textarea>
<!-- 分享签名的隐藏域 -->
<!-- 自定义微信分享页面 -->
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	var overall_wx_timestamp = $("#overall_wx_timestamp").val();//时间戳
	var overall_wx_nonceStr = $("#overall_wx_nonceStr").val();//随机串
	var overall_wx_signature = $("#overall_wx_signature").val();//签名
	var overall_wx_appid = $("#overall_wx_appid").val();
	var overall_wx_shareType = $("#overall_wx_shareType").val();
	var overall_wx_url = $("#overall_wx_url").val();
	var overall_wx_title = $("#overall_wx_title").val();
	var overall_wx_picUrl = $("#overall_wx_picUrl").val();
	var overall_wx_oriUserUuid = $("#overall_wx_oriUserUuid").val();
	var overall_wx_desc = $("#overall_wx_desc").text();
	var overall_base_url = location.href.split('#')[0];
	wx.config({
		debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		appId : overall_wx_appid,
		timestamp : overall_wx_timestamp,
		nonceStr : overall_wx_nonceStr,
		signature : overall_wx_signature,
		jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage',
				'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone',
				'chooseImage', 'previewImage', 'uploadImage', 'downloadImage',
				'openLocation', 'getLocation','hideOptionMenu','showOptionMenu',
				'hideMenuItems','showMenuItems','closeWindow','scanQRCode','chooseWXPay','openProductSpecificView']
	});

	 wx.ready(function() {
		// 分享到朋友圈
		wx.onMenuShareTimeline({
			title : overall_wx_title, // 分享标题
			link : overall_wx_url, // 分享链接
			imgUrl : overall_wx_picUrl, // 分享图标
			desc : overall_wx_desc,
			success : function() {
				// 用户确认分享后执行的回调函数
				shareSuccessTotal('分享到朋友圈');
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});

		// 分享给朋友
		wx.onMenuShareAppMessage({
			title : overall_wx_title, // 分享标题
			desc : overall_wx_desc, // 分享描述
			link : overall_wx_url, // 分享链接
			imgUrl : overall_wx_picUrl, // 分享图标
			type : 'link', // 分享类型,music、video或link，不填默认为link
			dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
			success : function() {
				// 用户确认分享后执行的回调函数
				shareSuccessTotal('分享给朋友');
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});

		wx.onMenuShareWeibo({
			title : overall_wx_title, // 分享标题
			desc : overall_wx_desc, // 分享描述
			link : overall_wx_url, // 分享链接
			imgUrl : overall_wx_picUrl, // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
				shareSuccessTotal('分享到腾讯微博');
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});

		wx.onMenuShareQZone({
			title : overall_wx_title, // 分享标题
			desc : overall_wx_desc, // 分享描述
			link : overall_wx_url, // 分享链接
			imgUrl : overall_wx_picUrl, // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
				shareSuccessTotal('分享到QQ空间');
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});

		wx.onMenuShareQQ({
			title : overall_wx_title, // 分享标题
			desc : overall_wx_desc, // 分享描述
			link : overall_wx_url, // 分享链接
			imgUrl : overall_wx_picUrl, // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
				shareSuccessTotal('分享到QQ');
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
		});

	});

	wx.error(function(error) {
		//alert(JSON.stringify(error));
		console.log(JSON.stringify(error));
	}); 

	/**
	 ** 以下添加分享的方法
	 **/
	//分享成功后，记录分享成功的总方法
	 function shareSuccessTotal(type) {
		overall_wx_shareType = parseInt(overall_wx_shareType);
		if (overall_wx_shareType == 1) {
			//页面分享
			shareUrl(overall_base_url);
		} else if (overall_wx_shareType == 2) {
			//资源分享
			shareRes($('#resource_uuid').val(), overall_wx_oriUserUuid,type);
		} else if (overall_wx_shareType == 3) {
			//活动分享
			shareUrl(overall_base_url);
		} else if (overall_wx_shareType == 4) {
			//直播分享
			shareCast('');//缺uuid
		} else if (overall_wx_shareType == 5) {
			//活动专家分享--页面分享
			shareUrl(overall_base_url);
		}
	}

	//记录普通分享
	function shareUrl(url) {
		$.post('/api/webchat/common/share/page', {
			'url' : url
		}, function(data) {
			console.log(data);
		});
	}

	// 记录后台分享的资源
	function shareRes(uuid, oriUserUuid,type) {
		$.post('/api/webchat/share/res', {
			'uuid' : uuid,
			'type':type,
			'oriUserUuid' : oriUserUuid
		}, function(data) {
			console.log(data);
		});
	}
	
	//记录后台的直播分享
	function shareCast(uuid){
		$.post('/api/webchat/cast/share/record', {
			'uuid' : uuid
		}, function(data) {
			console.log(data);
		});
	} 
</script>
