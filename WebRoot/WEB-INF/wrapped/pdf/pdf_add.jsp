<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script type="text/javascript"
	src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/pdf_common.js?ver=1.0"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
	
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>PDF管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/pdf/list/page">返回列表</a></li>
			<li class="active">新建PDF</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/pdf/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建PDF</h5>
		</div>
		<div class="ibox-content">
			<form id="pdf_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" id="signature" name="signature"
					value="${requestScope.signature}" />


				<div class="form-group">
					<label class="col-sm-3 control-label">选择PDF：</label>
					<div class="col-sm-6">
						<input type="file" id="pdf_upload" name="pdf_upload" value="选择PDF" />
						<span id="pdf_status"
							style="display: none; color: red; margin-left: 10px;"></span> <input
							type="hidden" name="pdfsize" id="pdfsize" /> <input
							type="hidden" name="pdfnum" id="pdfnum" /> <input type="hidden"
							name="taskId" id="taskId" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">PDF名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name" id="pdf_name"
							required="required" maxlength="80" placeholder="请填写PDF名称(30字以内)"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">选择分类：</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary" id="pdf_category"
							value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal" name="pdf_category" /> <span
							id="category_trace"></span> <input type="hidden"
							id="categoryUuid" name="categoryUuid" />
					</div>
				</div>
				
				<!-- modified by mayuan start ==>添加讲者 -->
				<div class="form-group">
					<label class="col-sm-3 control-label">讲者：</label>
					<div class="col-sm-4">
						<select class="form-control" id="spaker_selector"
							name="spaker_selector">
							<option value="0">请选择</option>
							<c:if test="${not empty requestScope.spaker_list}">
							 <c:forEach var="item" items="${requestScope.spaker_list}">
								<option value="${item.id}">${item.text}</option>
							</c:forEach>
							</c:if>
						</select>
					</div>
				</div>
				<!-- modified by mayuan end -->

				<div class="form-group">
					<label class="col-sm-3 control-label">封面图：</label>
					<div class="col-sm-6">
						<input id="cover_uploadify" type="file"> <img
							id="cover_preview" alt="" src="" style="display: none;height : 180px;" class="pre-view" > <input
							type="hidden" name="coverTaskId" id="coverTaskId">
						<hr>
					</div>
				</div>

				<!-- 
				<div class="form-group">
					<label class="col-sm-3 control-label">二维码：</label>
					<div class="col-sm-6">
						<input id="qrcode_uploadify" type="file" /> <img
							id="qrcode_preview" alt="" src="" style="display: none;" /> <input
							type="hidden" id="qrTaskId" name="qrTaskId" />
						<hr>
					</div>
				</div>  -->

				<div class="form-group">
					<label class="col-sm-3 control-label">显示方式：</label>
					<div class="col-sm-6">
						<select class="form-control col-sm-2" name="show_type">
							<option value="0">全屏</option>
							<option value="1">自适应</option>
						</select>
						<hr>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">PDF简介：</label>
					<div class="col-sm-6">
						<textarea name="mark" cols="60" rows="4" maxlength="300"
							placeholder="PDF简介(300字以内)"></textarea>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/pdf/list/page" class="btn btn-warning">取消</a>
					</div>
				</div>
			</form>
		</div>
	</div>
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
				<button id="confirm_btn" type="button" class="btn btn-primary"
					data-dismiss="modal">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- Modal -->
<script type="text/javascript">
	$('#pdf_form').submit(function(event) {
		event.preventDefault();
		var $form = $(this);
		if ($form.valid()) {
			$.post('/project/pdf/save', $form.serialize(), function() {
			}).always(function(data) {
				if (data.status == 'success') {
					alert("pdf信息保存成功，请设置该资源的访问权限");
					window.location.href = '/project/pdf/list/page';
				} else {
					alert(data.message);
				}
			});
		}
	});
</script>
