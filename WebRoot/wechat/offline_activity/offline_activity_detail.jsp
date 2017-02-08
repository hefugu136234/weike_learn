<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>${vo_data.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/offline_activity.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/dropload.min.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
	<div class="page pb50">
		<a href="/api/webchat/index" class="icon-logo page-absolute-logo"></a>
		<div class="offline-activity-top-btn">
			<a href="#resource_comments_text" class="btn icon-message"></a> <a
				href="javascript:;" class="btn icon-is-share share-from-wechat-btn"></a>
		</div>
		<div class="offline-activity-banner">
			<img src="${vo_data.wxcover}" alt="线下活动banner">
		</div>
		<div class="offline-activity-desc">
			<h5 class="title">${vo_data.name}</h5>
			<div class="price">费用：<span class="num">${vo_data.priceShow}</span></div>
			<div class="status">
				<div class="box">
					人数：<span class="num">${vo_data.bookNum}</span>/${vo_data.personNum}
				</div>
				<div class="box">地点：${vo_data.address}</div>
			</div>
			<div class="date">报名时间：${vo_data.bookTimeShow}</div>
		</div>
		<div class="offline-activity-tag clearfix">
			<h5 class="tt bfL">活动简介</h5>
		</div>
		<div class="offline-activity-intro">${vo_data.description}</div>
		<jsp:include page="/wechat/common/base_comment.jsp"></jsp:include>
	</div>
	<div class="offline-activity-btns clearfix">
		<c:if test="${vo_data.initiatorFlag}">
			<a id="book_detail" class="btn" href="javascript:;"> <span
				class="icon icon-detail"></span> 报名详情
			</a>
			<a id="apply_firend" class="btn share-from-wechat-btn"
				href="javascript:;"> <span class="icon icon-users"></span> 邀请好友
			</a>
		</c:if>
		<c:if test="${!vo_data.initiatorFlag && !vo_data.bookFlag}">
			<a id="go_book" class="button btn-orange apply-btn bfR"
				href="javascript:;">立即报名</a>
		</c:if>
	</div>
	<input type="hidden" id="comment_body_uuid" value="${vo_data.uuid}" />
	<input type="hidden" id="comment_body_type" value="5" />
	<script type="text/javascript">
		$(function() {
			var lineActivityUUid=$('#comment_body_uuid').val();
			var localhref=location.href.split('#')[0];
			$('#book_detail').click(function() {
				location.href='/api/webchat/offline/book/detail/'+lineActivityUUid;
			});
			$('#apply_firend').click(function() {

			});
			$('#go_book').click(function() {
				var $this=$(this);
				$.post('/api/webchat/offline/activity/book',{
					uuid:lineActivityUUid
				},function(data){
					if(isSuccess(data)){
					  $this.hide();
					  AlertClassTip('报名申请已提交');
					}else{
						if(data.code==511){
							loginAlert(localhref);
							return false;
					   }else{
						   AlertClassTip(data.message);
					   }
					}
				});
			});
		});
	</script>
</body>
</html>
