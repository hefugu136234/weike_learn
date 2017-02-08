<%@page import="com.lankr.tv_cloud.utils.Tools"%>
<%@page import="com.lankr.tv_cloud.model.TvLayout"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css"
	href="/assets/css/jquery.gridster.css">
<link rel="stylesheet" type="text/css" href="/assets/css/widget_ui.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<script type="text/javascript"
	src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/jstree/jstree.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<script src="/assets/js/jquery.gridster.js" type="text/javascript"
	charset="utf-8"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
<style type="text/css">
.gs_remove_handle {
	float: right;
	margin-right: 2px;
}

.gs_setting {
	margin-top: 20px;
	color: black;
}

.child-setting {
	color: black;
	margin-left: 20px;
	display: none;
}

#image-preview {
	max-height: 300px;
	display: none
}

.pre-view {
	border: 1px gray solid;
	max-width: 280px;
	max-height: 280px;
}

</style>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="row">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>TV页面定制 &gt; <a href="/tv/layout/${requestScope.category.uuid }/list"> ${requestScope.category.name } </a></h5>
					<input id="add_widget_btn" disabled="disabled" type="button"
						class="btn btn-sm btn-primary dlBtn bfR" value="添加组件">
				</div>
				<div class="ibox-content">
					<div class="gridsterBox">
						<div class="gridster">
							<div id="fresh_loading_tips" style="display: none;">
								<span
									class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>
								Loading...
							</div>
							<ul>
							</ul>
						</div>
					</div>

					<form class="form-horizontal formBox valForm">
						<input type="hidden" value="${requestScope.layout.uuid }"
							id="layout_uuid">
						<div class="form-group">
							<label class="col-sm-3 control-label">版块名称：</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="name"
									id="layout_name" required="required" maxlength="80"
									value="${fn:escapeXml(requestScope.layout.name) }" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">简介：</label>
							<div class="col-sm-6">
								<textarea id="layout_mark" name="mark" cols="60" rows="6"
									maxlength="150" class="form-control" placeholder="布局简介"
									required="required">${fn:escapeXml(requestScope.layout.mark) }</textarea>
							</div>
						</div>


						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-3">
								<button id="save_widgets_btn" class="btn btn-primary mr20"
									type="button">保存</button>
								<a href="/tv/layout/${requestScope.category.uuid }/list"
									class="btn btn-warning">取消</a>
							</div>
						</div>
				</div>

				</form>

			</div>
		</div>
	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="settingModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="false">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">设置组件</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">关联分类:</label>
					<div style="margin-left: 130px;" id="tree"></div>
				</div>

				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">设置图片:</label>
					<div class="col-sm-9">
						<!-- <input id="uploadify" type="file"> -->
						<input class="uploader-component" type="file">
						<img id="image-preview" alt="" class="img-thumbnail pre-view " src="">
					</div>
					<!-- <div id="image_show">
						<img id="image-preview" alt="" class="img-thumbnail" src="">
					</div> -->
				</div>
				<br>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="setting_confirm_btn" type="button"
					class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/assets/js/admin/tv_sub_banner.js?ver=1.1"></script>
<script type="text/javascript">
	var category = new Category('${requestScope.category.uuid }',
			'${requestScope.category.name }');

	function Category(uuid, name) {
		this.uuid = uuid;
		this.name = name;
	}
	var widgets = {};
	<%
	TvLayout layout = (TvLayout)request.getAttribute("layout");
		if(layout != null){
			%>
			widgets = <%= layout.getWidgets()%>
		<%}
	%>
</script>