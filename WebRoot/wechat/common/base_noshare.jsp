<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 引入签名 -->
<jsp:include page="/wechat/common/base_share.jsp"></jsp:include>
<script type="text/javascript">
	//使分享无效化，后台的页面view要加commonshare 
	wx.ready(function() {
		wx.hideMenuItems({
			// 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
			menuList : [ 'menuItem:share:appMessage', //分享给朋友
			'menuItem:share:timeline',//分享到朋友圈
			'menuItem:share:qq',//分享到qq
			'menuItem:share:weiboApp',//分享到Weibo
			"menuItem:share:facebook",//分享到FB
			'menuItem:share:QZone',//分享到 QQ 空间
			'menuItem:copyUrl',//复制链接
			'menuItem:originPage',//原网页
			'menuItem:readMode',//阅读模式
			'menuItem:openWithQQBrowser',//在QQ浏览器中打开
			'menuItem:openWithSafari',//在Safari中打开
			'menuItem:share:email',//邮件
			'menuItem:share:brand'//一些特殊公众号
			]
		});
	});
</script>