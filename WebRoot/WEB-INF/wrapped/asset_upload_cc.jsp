<%@page import="com.lankr.tv_cloud.utils.Tools"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js">	
</script>
<script type="text/javascript" src="/assets/js/formValidation.min.js">
</script>
<script type="text/javascript" src="/assets/js/formvalidation.js"></script>
<script src="/assets/js/admin/assets_mgr_cc.js" type="text/javascript"
	charset="utf-8"></script>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">

<style>
<!--
-->
</style>
<%
	Object obj = request.getAttribute("status");
	String status = (obj == null ? "" : (String) obj);
%>
<div class="ibox-content" style="display: block;">
	<form id="new_project_form" method="post" action="/asset/video/save"
		class="form-horizontal">
		<input type="hidden" name="token"
			value="<%=request.getAttribute("token")%>">
		<div class="form-group">
			<label class="col-sm-2 control-label">文件选择</label>
			<div class="col-sm-5 controls">
				<span
					style="position: absolute; width: 100px; height: 40px; background-color: #18a689; border-radius: 4px; z-index: 2; text-align: center; line-height: 40px;">点击选择文件</span>
				<object style="position: absolute; z-index: 3;"
					classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
					codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0"
					width="100" height="40" id="videoUpload">
					<param name="allowFullScreen" value="true">
					<param name="allowScriptAccess" value="always">
					<param name="wmode" value="transparent">
					<embed src="/assets/js/uploader.swf" width="100" height="40"
						id="myUploader" name="cc_batch_upload" allowfullscreen="true"
						allowscriptaccess="always" wmode="transparent"
						pluginspage="http://www.macromedia.com/go/getflashplayer"
						type="application/x-shockwave-flash">
				</object>
				<div id="video_upload_progress" class="progress"
					style="margin-left: 125px; display: none;">
					<div class="progress-bar progress-bar-striped active"
						role="progressbar" aria-valuenow="0" aria-valuemin="0"
						aria-valuemax="100" style="width: 0%;"></div>
				</div>
				<span id="upload_status" style="margin-left: 125px; display: none;"></span>
				<input type="hidden" value="" name="ccVideoId" id="ccVideoId">
			</div>
		</div>
		<div class="hr-line-dashed"></div>
		<div class="form-group">
			<label class="col-sm-2 control-label">视频名称</label>
			<div class="col-sm-5">
				<input type="text" class="form-control" name="title" id="videoName"
					required="required">
			</div>
		</div>
		<div class="hr-line-dashed"></div>
		<div class="form-group">
			<label class="col-sm-2 control-label">视频分类</label>
			<div class="col-sm-8">
				<input type="button" class="btn btn-primary"
					id="video_category_button" value="选择分类" data-toggle="modal"
					data-target="#categorySelectorModal"> <span
					id="category_trace"></span>
			</div>
			<input type="hidden" id="categoryUuid" name="categoryUuid">
		</div>
		<div class="hr-line-dashed"></div>
		<div class="form-group">
			<label class="col-sm-2 control-label">是否收费</label>
			<div class="col-sm-5">
				<div class="checkbox">
					<label> <input id="price_control" name="need_price"
						type="checkbox"></label> <input id="price" class="form-control"
						type="text" style="display: none;" placeholder="输入价格" name="price">
					<span id="chinese_tips" style="color: green;"> </span>
				</div>

			</div>
		</div>

		<div class="hr-line-dashed"></div>
		<div class="form-group">
			<label class="col-sm-2 control-label">视频简介</label>
			<div class="col-sm-5">
				<textarea class="form-control" rows="3" placeholder="请输入视频简介"
					name="description"></textarea>
			</div>
		</div>
		<div class="hr-line-dashed"></div>
		<div class="form-group">
			<div class="col-sm-4 col-sm-offset-2">
				<button class="btn btn-white" type="button">取消</button>
				<button class="btn btn-primary" type="submit">保存</button>
			</div>
		</div>
	</form>
</div>

<!-- Modal -->
<div class="modal fade" id="categorySelectorModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">选择分类</h4>
			</div>
			<div class="modal-body">
				<div id="tree"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<script>
$('#new_project_form').formValidation({
    framework: 'bootstrap',
    fields: {      
    }
});

	var status ='<%=status%>';
	if (status != '') {
		alert(status);
		status = ''
	}

	$('#price_control').click(function() {
		priceInputShow();
	})

	var priceInput = $('#price');
	var chinese_tips = $('#chinese_tips');
	function priceInputShow() {
		if ($('#price_control').is(':checked')) {
			priceInput.show();
			priceInput.unbind('input');
			chinese_tips.show();
			priceInput.bind('input', function() {
				chinese_tips.text(chinaCost($(this).val()))
			})
		} else {
			priceInput.hide();
			chinese_tips.hide();
		}

	}
</script>