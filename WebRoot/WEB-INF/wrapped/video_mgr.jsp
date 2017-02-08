<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/assets/css/site.css">
<link rel="stylesheet"
	href="/assets/css/plugins/ladda/ladda-themeless.min.css">
<link rel="stylesheet" href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<link rel="stylesheet"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">

<script src="/assets/js/plugins/ladda/spin.min.js"></script>
<script src="/assets/js/plugins/ladda/ladda.min.js"></script>
<script src="/assets/js/plugins/ladda/ladda.jquery.min.js"></script>

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/plugins/dataTables/plug-ins/chinese-string.js"></script>
<script src="/assets/js/admin/video_list.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/admin/search_video_button.js"></script>
<script src="/assets/js/admin/uploader_common.js"></script>
<script src="/assets/js/admin/common_datatable.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-6">
		<h2>视频管理</h2>
		<ol class="breadcrumb">
			<li class="active">视频列表</li>
		</ol>
	</div>
	<div class="col-lg-6">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/asset/video/upload/1">新建七牛云视频</a> <a
			class="btn btn-sm btn-primary bfR mt20" style="margin-right: 30px;"
			href="/asset/video/upload/0">新建腾讯云视频</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">

		<!-- <div class="ibox-title">
			<h5>视频列表</h5> <span style="margin-left: 20px">分类筛选： </span><a href="#" >所有</a>
		</div> -->

		<div class="ibox-title">
			<h5>视频列表</h5>
			<input type="button" class="btn btn-primary btn-sm pull-right ttBtn"
				id="video_category_button_search" value="请选择类别" data-toggle="modal"
				data-target="#categorySelectorModal_search"> <span
				id="category_trace"></span>
		</div>

		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="video_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">视频名</th>
							<th rowspan="1" colspan="1" style="width: 10%;">分类</th>
							<th rowspan="1" colspan="1" style="width: 10%;">编辑时间</th>
							<th rowspan="1" colspan="1"
								style="width: 8%; text-align: center;">时长（秒）</th>
							<th rowspan="1" colspan="1" style="width: 10%;">讲者</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建人</th>
							<th rowspan="1" colspan="1" style="width: 8%;">价格(元)</th>
							<th rowspan="1" colspan="1" style="width: 12%;">描述</th>
							<th rowspan="1" colspan="1" style="width: 8%;">状态</th>
							<th style="width: 14%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>

	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="videoEditModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="videoEditModalLabel">更新视频</h4>
			</div>
			<div class="modal-body">

				<div class="form-horizontal" name="commentform">
					<div class="form-group">
						<label class="control-label col-md-4" for="video_name">视频名称</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="video_name"
								name="videoName" placeholder="" />
						</div>
					</div>

					<div class="hr-line-dashed"></div>
					<div class="form-group">
						<label class="control-label col-md-4" for="video_name">视频讲者</label>
						<div class="col-sm-7">
							<select class="form-control" id="speaker_selector"
								name="speaker_uuid">
								<option>请选择</option>
							</select>
						</div>
					</div>

					<div class="hr-line-dashed"></div>
					<div class="form-group">
						<label class="control-label col-md-4">是否收费</label>
						<div class="col-sm-6">
							<div class="checkbox">
								<label> <input id="price_control" name="need_price"
									type="checkbox"></label> <input id="price" class="form-control"
									type="text" style="display: none;" placeholder="输入价格"
									name="price"> <span id="chinese_tips"
									style="color: green;"> </span>
							</div>
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					<div class="form-group">
						<label class="control-label col-md-4" for="email">视频封面</label>
						<div class="col-md-6">
							<input id="uploadify" type="file"> <img
								style="display: none; max-height: 240px;" id="image-preview"
								alt="" class="img-thumbnail pre-view " src=""> <input
								type="hidden" name="cover" id="cover_hidden"> <input
								type="hidden" id="uploadTag" value="" />
						</div>
					</div>

					<div class="hr-line-dashed"></div>
					<div class="form-group">
						<label class="control-label col-md-4" for="description">视频简介</label>
						<div class="col-md-6">
							<textarea rows="6" class="form-control" id="description"
								maxlength="499" name="description" placeholder="请输入视频简介(500字以内)"></textarea>
						</div>
					</div>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="confirm_btn" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

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
				<button id="select_confirm_btn" type="button"
					class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="categorySelectorModal_search" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">请选择相应分类查询</h4>
				<font color="red">(提示:选择根目录查询全部)</font>
			</div>
			<div class="modal-body">
				<div id="tree_search"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="select_confirm_btn_search" type="button"
					class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
