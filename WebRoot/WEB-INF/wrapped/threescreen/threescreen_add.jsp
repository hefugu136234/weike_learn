<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css" >
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" href="/assets/css/plugins/jstree/tree_style.min.css">
	
<script src="/assets/js/qq/uploaderh5.js?ver=2.0" charset="utf-8"></script>
<script src="/assets/js/jquery.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="/assets/js/qq/qquploadVod.js?ver=2.0"></script>
<script src="/assets/js/admin/threescreen/threescreen_common.js?ver=1.0"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
	
<style>
.out {
	width: 100px;
	height: 30px;
	text-align: center;
	margin: 0px auto;
	margin-top: 5px;
}

.result {
	width: auto;
	height: 60px;
	border: solid 1px green;
}

.count {
	width: 300px;
	height: 50px;
	border: solid 1px green;
	font-size: 13px;
}

.delete {
	cursor: pointer;
	padding: 0px 5px;
	text-decoration: underline;
	color: red;
}

#secret .head {
	padding: 5px;
	margin-bottom: 20px;
}

#secret .input_area {
	margin-bottom: 10px;
	text-align: left;
}

#secret .input_area span {
	display: inline-block;
	min-width: 83px;
}

#secret .head a {
	font-weight: bold;
}

#secret input[type=text] {
	width: 300px;
	height: 28px;
}

#secret button {
	margin: 10px;
	padding: 5px;
	width: 98px;
}
</style>
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>三分屏管理</h2>
		<ol class="breadcrumb">
			<li><a href="/project/threescreen/list/page">返回列表</a></li>
			<li class="active">新建三分屏</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/threescreen/list/page">返回列表</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>新建三分屏</h5>
		</div>
		<div class="ibox-content">
			<form id="threescreen_form" class="form-horizontal formBox valForm">
				<input type="hidden" name="token" value="${requestScope.token}" />
				<input type="hidden" id="signature" name="signature"
					value="${requestScope.signature}" />

				<div class="form-group">
					<label class="col-sm-3 control-label">三分屏名称：</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" name="name" id="name"
							required="required" maxlength="29" placeholder="请填写三分屏名称(30字以内)"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">视频选择：</label>
					<div class="col-sm-6">
						<button class="btn btn-primary" id="pickfiles" type="button">添加文件</button>
						<div id="file_select_status" style="display: none;">
							<div class="result" id="result"></div>
							<button id="start_upload" class="btn btn-primary"
								style="margin-top: 5px; display: none;" type="button">开始上传</button>
							<button id="re_upload" class="btn btn-primary"
								style="display: none;" type="button">重新上传</button>
							<div class="out" id="error"
								style="color: red; width: 400px; text-align: left;"></div>
						</div>
						<input type="text" class="form-control" id="fileId" name="fileId" required="required" />
					</div>
				</div>


				<div class="form-group">
					<label class="col-sm-3 control-label">选择PDF：</label>
					<div class="col-sm-6">
						<input type="file" id="pdf_upload" name="pdf_upload" value="选择PDF" />
						<span id="pdf_status"
							style="display: none; color: red; margin-left: 10px;"></span> <input
							type="hidden" name="pdfTaskId" id="pdfTaskId" required="required" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">选择分类：</label>
					<div class="col-sm-6">
						<input type="button" class="btn btn-primary" id="pdf_category"
							value="选择分类" data-toggle="modal"
							data-target="#categorySelectorModal" name="pdf_category" /> <span
							id="category_trace"></span> <input type="hidden"
							id="categoryUuid" name="categoryUuid" required="required" />
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">封面：</label>
					<div class="col-sm-6">
						<input id="cover_uploadify" type="file" /> <img
							id="cover_preview" alt="" src="" style="display: none;" class="pre-view"
							width="480" height="320" /> <input type="hidden" id="cover"
							name="cover" />
						<hr>
					</div>
				</div>

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


				<div class="form-group">
					<label class="col-sm-3 control-label">三分屏简介：</label>
					<div class="col-sm-6">
						<textarea name="mark" cols="60" rows="4" maxlength="280"
							class="form-control" placeholder="三分屏简介(300字以内)"></textarea>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-6 col-sm-offset-3">
						<button class="btn btn-primary mr20" type="submit">保存</button>
						<a href="/project/threescreen/list/page" class="btn btn-warning">取消</a>
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
	function submitFrom(from) {
		$.post('/project/threescreen/save/data', $(from).serialize(), function(
				data) {
			if (data.status == 'success') {
				alert('保存成功，请设置该资源的访问权限');
				window.location.href = '/project/threescreen/list/page';
			} else {
				alert(data.message);
			}
		});
	}
</script>
