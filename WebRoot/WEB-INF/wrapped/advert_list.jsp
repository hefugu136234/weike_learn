<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="/assets/js/formValidation.min.js"></script>
<script type="text/javascript" src="/assets/js/formvalidation.js"></script>
<script type="text/javascript" src="/assets/js/admin/adver_list.js"></script>
<script type="text/javascript" src="/assets/js/uploadify/jquery.uploadify.min.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-lg-10">
    <h2>
      广告管理
    </h2>
    <ol class="breadcrumb">
      <li>
        <a href="/project/adver/list/page">广告管理</a>
      </li>
      <li class="active">
        广告列表
      </li>
    </ol>
  </div>
  <div class="col-lg-2">
    <a class="btn btn-sm btn-primary bfR mt20" href="/project/adver/add/page">创建广告</a>
  </div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>广告列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="advert_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th>广告名</th>
							<th>创建时间</th>
							<th>广告类型</th>
							<th>时长（秒）</th>
							<th>描述</th>
							<th>创建者</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>

				</table>
			</div>

		</div>
	</div>
</div>


<!-- Modal -->
<div class="modal fade" id="adverEditModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="videoEditModalLabel">更新广告</h4>
			</div>
			<div class="modal-body">

				<div class="form-horizontal" name="commentform">

					<div class="form-group">
						<label class="control-label col-md-4" for="ad_name">广告名称</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="ad_name"
								name="adName" placeholder="" required="required" maxlength=50/>
						</div>
					</div>

					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="control-label col-md-4" for="ad_position">广告位置</label>
						<div class="col-md-6">
								<select class="form-control" id="ad_position" name="adposition">
				                </select>
						</div>
					</div>

					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="control-label col-md-4" for="ad_time">广告时长（秒）</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="ad_time"
								name="adtime" placeholder="" required="required" maxlength=5/>
						</div>
					</div>

					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="control-label col-md-4" for="email">广告图片</label>
						<div class="col-md-6">
							<input id="uploadify" type="file"> <img
								style="display: none; max-height: 240px;" id="image-preview"
								alt="" class="img-thumbnail" src="">
						</div>
					</div>

					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="control-label col-md-4" for="description">广告简介</label>
						<div class="col-md-6">
							<textarea rows="6" class="form-control" id="description"
								name="description" placeholder=""></textarea>
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
