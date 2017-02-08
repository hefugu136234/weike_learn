<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	max-height: 240px;
	display: none
}
</style>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="row">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>TV页面定制 &gt; 首页</h5>
					<input id="add_widget_btn" disabled="disabled" type="button"
						class="btn btn-sm btn-primary dlBtn bfR" value="添加组件">
				</div>
				<div class="ibox-content">
					<div class="gridsterBox">
						<div class="gridster">
							<div id="fresh_loading_tips" style="display: none;">
								<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>
								Loading...
							</div>
							<ul>
							</ul>
						</div>
					</div>
					<input id="save_widgets_btn" type="button" class="btn btn-primary"
						value="提交">
				</div>
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
					<input
						type="text" class="form-control" placeholder="推荐文案" name="mark" id="mark" maxlength="60"
						value="" />
				</div>


				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-3 control-label">设置图片:</label>
					<div class="col-sm-9">
						<input id="uploadify" type="file">
						<img id="image-preview" alt="" class="img-thumbnail pre-view" src="" >
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

<script type="text/javascript" src="/assets/js/admin/tv_banner.js"></script>
<script type="text/javascript" src="/assets/js/admin/uploader_common.js"></script>
