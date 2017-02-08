<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		// /wechat/common_share.jsp
		$.get('/api/webchat/wx/share/config',{
			url:location.href.split('#')[0],
			timestamp : new Date().getTime()
		}, function() {
		}).always(function(data) {
			
			if (data.status == 'success') {
				initWx(data)
			}
		})
		console.log(location.href.split('#')[0])
	})

	var title = '知了云盒 诚邀您参加产品公测'
	var desc = '公测申请已经开始，名额有限快点来申请吧。'
	var link = 'http://www.z-box.com.cn/wechat'
	var imgUrl = 'http://www.z-box.com.cn/assets/img/webchat/yuyue.png'

	function initWx(data) {
		wx.config({
			debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId : data.appId,
			timestamp : data.timestamp,
			nonceStr : data.nonceStr,
			signature : data.signature,
			jsApiList : [ 'onMenuShareTimeline', 'onMenuShareAppMessage',
					'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone' ]
		});

		wx.ready(function() {
			//分享到朋友圈
			wx.onMenuShareTimeline({
				title : title, // 分享标题
				link : link, // 分享链接
				imgUrl : imgUrl, // 分享图标
				desc : desc,
				success : function() {
					// 用户确认分享后执行的回调函数
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});

			//分享给朋友
			wx.onMenuShareAppMessage({
				title : title, // 分享标题
				desc : desc, // 分享描述
				link : link, // 分享链接
				imgUrl : imgUrl, // 分享图标
				type : 'link', // 分享类型,music、video或link，不填默认为link
				dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
				success : function() {
					// 用户确认分享后执行的回调函数
					
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});

			wx.onMenuShareWeibo({
				title : title, // 分享标题
				desc : desc, // 分享描述
				link : link, // 分享链接
				imgUrl : imgUrl, // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});

			wx.onMenuShareQZone({
				title : title, // 分享标题
				desc : desc, // 分享描述
				link : link, // 分享链接
				imgUrl : imgUrl, // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});

			wx.onMenuShareQQ({
				title : title, // 分享标题
				desc : desc, // 分享描述
				link : link, // 分享链接
				imgUrl : imgUrl, // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});

		});

		wx.error(function(res) {
		});
	}
</script>