<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 新页面 原 无-->
<ul class="operation-tag clearfix">
	<li>
		<div id="my_collection" class="btn like" data-uuid="${res_vo.uuid}">
			<span class="icon ${res_vo.collectStatus?'icon-star':'icon-star-line'}"></span>
			<span class="info">${res_vo.collectStatus?'已收藏':'收藏'}</span>
			<div class="num">${res_vo.collectCount}</div>
		</div>
	</li>
	<li>
		<div id="my_zan" class="btn zan" data-uuid="${res_vo.uuid}">
			<span class="icon icon-like"></span>
			<span class="info">${res_vo.praiseStatus?'已赞':'赞'}</span>
			<div class="num">${res_vo.praiseCount}</div>
		</div>
	</li>
	<li>
		<div id="my_share" class="btn share" data-uuid="${res_vo.uuid}">
			<span class="icon icon-share"></span>
			<span class="info">分享</span>
		</div>
	</li>
</ul>

<div id="share_from_wechat" class="share-wechat-tips">
	<p>请从微信中进行分享！</p>
	<div class="small mt32">
		<p>分享后，文章每次被其他用户浏览，</p>
		<p>您都将获得4个积分。</p>
	</div>
</div>

<%-- <jsp:include page="/wechat/common/login_part.jsp"></jsp:include> --%>
