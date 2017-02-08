<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" href="/assets/css/plugins/jstree/tree_style.min.css">
	
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/admin/video_update.js"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>视频管理</h2>
		<ol class="breadcrumb">
			<li><a href="/asset/video/mgr">视频列表</a></li>
			<li class="active">更新视频</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="javascript:;" onClick="javascript :history.back(-1);">返回</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>更新视频信息</h5>
		</div>
		<div class="ibox-content">
			<form id="videoUpdata_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" id="videoInfo" value="${requestSscope.video_info }" />
				<input type="hidden" name="uuid" value="${requestScope.video_info.uuid}" />
				<input type="hidden" id="_cover" value="${requestScope.video_info.cover}" />

				<div class="form-group">
					<label class="col-sm-3 control-label">视频名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="title" id="video_title"
							required="required" maxlength="80"
							value="${requestScope.video_info.title}" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">选择分类：</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary" id="video_category"
							value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal" name="videoCategory" /> 
						<span id="category_trace"></span> 
						<input type="hidden" id="categoryUuid" name="categoryUuid" 
							   required="required" value="${requestScope.video_info.categoryId}"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">讲者：</label>
					<div class="col-sm-4">
						<select class="form-control" id="spaker_selector"
							name="speaker_uuid">
							<option value="0">请选择</option>
							<c:if test="${not empty requestScope.spaker_list}">
							 <c:forEach var="item" items="${requestScope.spaker_list}">
								<option value="${item.id}">${item.text}</option>
							</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">是否收费：</label>
					<div class="col-sm-6">
						<div class="checkbox">
							<label><input id="need_price" name="need_price" type="checkbox"></label> 
							<input id="price" class="form-control" type="text" style="display: none;" 
								   placeholder="输入价格" name="price"> 
							<span id="chinese_tips" style="color: green;"></span>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label" for="email">视频封面：</label>
					<div class="col-sm-6">
						<input id="uploadify" type="file" class="form-control"/>
						<img style="display: none; max-height: 240px;" id="image_preview" alt="" class="img-thumbnail pre-view" src="" >
						<input type="hidden" id="cover_hidden" />
						<input type="hidden" id="uploadTag" value="" />
					</div>
				</div>
					
				<div class="form-group">
					<label class="col-sm-3 control-label">视频简介：</label>
					<div class="col-sm-6">
						<textarea id="description" name="mark" cols="60" rows="4" maxlength="499"
							placeholder="请输入视频简介(500字以内)">${requestScope.video_info.description}</textarea>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/asset/video/mgr" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- CategorySelectorModal -->
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
				<button id="confirm_btn" type="button" class="btn btn-primary"
					data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- CategorySelectorModal -->

<script type="text/javascript">
$(function() {
	//回显讲者信息
	var speakerUUid='${requestScope.speaker.uuid}';
	if(!!speakerUUid){
		$("#spaker_selector").val(speakerUUid)
		$("#spaker_selector").trigger("chosen:updated");
	}
	
	//价格回显
	var needPrice='${requestScope.video_info.need_price}';
	//test
	console.log(needPrice);
	
	var price='${requestScope.video_info.price}';
	var priceInput = $('#price');
	var chinese_tips = $('#chinese_tips');
	if('true' === needPrice){
		$('#need_price').prop("checked",true);
		priceInput.show();
		priceInput.val(price);
		//priceInput.unbind('input');
		chinese_tips.show();
		chinese_tips.text(chinaCost(priceInput.val()));
		$('#price').click(function() {
			priceInput.bind('input', function() {
				chinese_tips.text(chinaCost($(this).val()))
			})
		})
	}else{
		$('#need_price').prop("checked",false);
		priceInput.hide();
		chinese_tips.hide();
	}
	
	//提交数据
	$('#videoUpdata_form').submit(function(event) {
		event.preventDefault();
		var $form = $(this);
		var videoUuid = "${requestScope.video_info.uuid}";
		if ($form.valid()) {
			$.post('/asset/video/'+ videoUuid + '/update', 
							//$form.serialize(),  
							{cover : $('#image_preview').attr('src'),
							title : $('#video_title').val(),
							need_price : $('#need_price').is(':checked'),
							price : $('#price').val(),
							mark : $('#description').val(),
							categoryUuid : $('#categoryUuid').val(),
							speaker_uuid : $("#spaker_selector").val()}, function() {}).always(
				function(data) {
					console.log(data);
					if (data.status == 'success') {
						alert("更新视频信息成功");
						window.location.href = '/asset/video/mgr';
					} else {
						alert(data.status);
					}
				}
			);
		}
	});
});
</script>
