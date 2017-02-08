<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/qrcode/qrcode_list.js"></script>
	
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>二维码管理</h2>
		<ol class="breadcrumb">
			<li class="active">二维码列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/qrcode/add/page">新建二维码</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>二维码列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<div id="filterSearch">
					<div id="limitType" style="display:inline">
						二维码属性：	<select name="limitType" id="searchButton_limitType" >
										<option value="">所有</option>
										<option value="2">临时二维码</option>
										<option value="1">永久二维码</option>
										<option value="3">自制的永久二维码</option>
									 </select>&emsp;&emsp;
					</div>
					<div id="judyType" style="display:inline">
						二维码类型:  <select name="judyType" id="searchButton_judyType" >
										<option value="">所有</option>
										<option value="1">活动</option>
										<option value="3">直播</option>
										<option value="4">URL</option>
										<option value="6">资源</option>
										<option value="7">游戏</option>
									 </select>&emsp;&emsp;
					</div>
				</div><br/>
				<table id="qrsence_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 25%;">二维码名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 20%;">二维码属性</th>
							<th rowspan="1" colspan="1" style="width: 10%;">二维码类型</th>
							<th rowspan="1" colspan="1" style="width: 15%;">扫码次数</th>
							<th style="width: 20%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
			
		</div>
	</div>
</div>
<!-- 二维码 -->
<div class="modal fade" id="dataModal_qrcode" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
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
							<input type="text" class="form-control" id="qrcode_url"/>
						</div>
				</div>
				<label class="control-label col-md-2"></label><span style="color: red">将链接复制到浏览器，可以下载</span>
				
				<div class="form-group">
						<label class="control-label col-md-2">二维码：</label>
						<div class="col-md-6">
						    <img style="max-height: 240px;" id="qrcode_preview"
								alt="" class="img-thumbnail" src="">
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
<script type="text/javascript">
	var error = '${requestScope.error}';
	if (!!error) {
		alert(error);
	}
</script>
