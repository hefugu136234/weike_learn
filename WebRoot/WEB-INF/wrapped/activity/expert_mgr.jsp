<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">

<script src="/assets/js/admin/activity/main.js?ver=1.0"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
<script src="/assets/js/admin/activity/expert_mgr.js"></script>
	
	
<style>
.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}
table td{
vertical-align:top;
width: 300px;
}
</style>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回活动列表</a></li>
			<li class="active">活动专家配置</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20" href="/admin/activity/expert/page/${requestScope.activity.uuid}">返回专家列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<%-- <h5>给&nbsp;&nbsp;<font color="green">${requestScope.activity.name}</font>&nbsp;&nbsp;活动添加专家</h5> --%>
			<h5>专家预览</h5>
		</div>
		<div class="ibox-content">
			<form id="activity_expertMgr_form" method="post" action=""
				class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" name="activityUuid" id="activityUuid" value="${requestScope.activity.uuid}" />
				<input type="hidden" name="webchatBgImage" id="webchatBgImgTaskId" value="${requestScope.webChatMedia.url }">
				<input type="hidden" name="activityExpertUuid" id="activityExpertUuid" value="${requestScope.activityExpert.uuid }">
				
				<div class="form-group">
					<label class="col-sm-3 control-label">选择专家：</label>
					<div class="col-sm-4">
						<select class="form-control" id="speaker_selector" name="speakerUuid" >
							<option value="0">请输入专家姓名</option>
							<c:if test="${not empty requestScope.chosenItem}">
								<option selected="selected" value="${requestScope.chosenItem.id}">${requestScope.chosenItem.text}</option>
							</c:if>
						</select>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">微信背景：</label>
					<div class="col-sm-4">
						<input id="speakerWechatBgImg" type="file"> 
						<span id="bgImgUploadStatus" style="display: none; color: red; margin-left: 10px;"></span>
						<img class="pre-view" alt="" src="${requestScope.webChatMedia.url }"/>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
			
				<div class="form-group">
					<label class="col-sm-3 control-label">备注：</label>
					<div class="col-md-4">
						<textarea rows="6" class="form-control" id="mark" maxlength="1000"
								name="mark" placeholder="">${requestScope.activityExpert.mark }</textarea>
					</div>
				</div>
				<div class="hr-line-dashed"></div>
				
				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="button" id="expertMgr_btn_submit">提交</button>
						<a href="/admin/activity/list" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		var error = '${requestScope.error}';
		if (!!error) {
			alert(error);
		}
		
		var item_text = '${requestScope.chosenItem.text}';
		var selector = $('#speaker_selector');
		var chosen = $('#speaker_selector_chosen');
		if(!!item_text){
			$('span', chosen).text(item_text);
		}
	})
</script>
