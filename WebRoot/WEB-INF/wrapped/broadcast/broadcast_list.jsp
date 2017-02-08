<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/chosen/chosen.css">

<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/broadcast/broadcast_list.js?ver=1.0"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>直播管理</h2>
		<ol class="breadcrumb">
			<li class="active">直播列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/broadcast/add/page">新建直播</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>直播列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="broadcast_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">直播名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">报名上限</th>
							<th rowspan="1" colspan="1" style="width: 10%;">直播类型</th>
							<th rowspan="1" colspan="1" style="width: 10%;">录播</th>
							<th rowspan="1" colspan="1" style="width: 10%;">直播平台</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 30%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>
<!-- 直播的链接 -->
<div class="modal fade" id="dataModal_broadcast" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">直播的banner的链接：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-md-2">微信链接</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="wx_content_broadcast" />
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-md-2">web链接</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="web_content_broadcast" />
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<!-- 直播的链接 -->
<!-- 二维码 -->
<div class="modal fade" id="dataModal_qrcode" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">直播的二维码：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-md-2">链接：</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="qrcode_url" />
						</div>
					</div>
					<label class="control-label col-md-2"></label><span
						style="color: red">将链接复制到浏览器，可以下载</span>

					<div class="form-group">
						<label class="control-label col-md-2">二维码：</label>
						<div class="col-md-6">
							<img style="max-height: 240px;" id="qrcode_preview" alt=""
								class="img-thumbnail" src="">
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<!-- 二维码 -->

<!-- 录播地址 -->
<div class="modal fade" id="record_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">直播的录播地址配置：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-md-3">直播名称：</label>
						<div class="col-md-6">
							<span id="broadcast_name" style="color: green;"></span>
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-md-3">录播视频：</label>
						<div id="select_div" class="col-sm-9"></div>
					</div>

					<!-- <div class="form-group">
						<label class="control-label col-md-3">录播地址：</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="record_url"/>
						</div>
					</div>
					
					<div class="form-group">
					<label class="control-label col-md-3"></label>
					<div class="col-md-8">
					<span style="color: red">注：直播的录播，最好绑定系统内部视频。如果是第三方视频，请输入录播地址（二者选其一就可）。当二者都有时，以录播视频生效。</span>
					</div>
					</div> -->
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="broadcast_video" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 录播地址 -->
<script type="text/javascript">
	var error = '${requestScope.error}';
	if (!!error) {
		alert(error);
	}
</script>
