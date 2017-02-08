<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />

<link href="/assets/css/plugins/dataTables/dataTables.responsive.css"
	rel="stylesheet">
<link href="/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="/assets/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="/assets/css/animate.css" rel="stylesheet">
<link href="/assets/css/style.css" rel="stylesheet">
<link href="/assets/css/default.css" rel="stylesheet">
<link href="/assets/css/site.css" rel="stylesheet">
<link rel="stylesheet"
	href="//cdn.bootcss.com/iCheck/1.0.2/skins/all.css">
<link rel="stylesheet" media="all" href="/assets/css/web/vote.css" />

<script src="/assets/js/jquery.js"></script>
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/bootstrap.min.js"></script>
<script src="/assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="/assets/js/main.js"></script>
<script src="/assets/js/plugins/pace/pace.min.js"></script>
<script src="/assets/js/admin/common_datatable.js"></script>
<script src="/assets/js/admin/resource_overView/user_vote.js"></script>
<script src="/assets/js/common.js"></script>
</head>

<body class="gray-bg">
	<input type="hidden" id="optionUuid"
		value="${requestScope.option.uuid }" />
	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="ibox float-e-margins">
			<div class="ibox-title">
				<h5>资源投票参与人列表</h5>
				<!-- <div class="col-lg-2">
					<a class="btn btn-sm btn-primary bfR mt20" href="history.back()">返回</a>
				</div> -->
			</div>
			<div class="ibox-content">
				<div id="list_wrapper" class="dataTables_wrapper">
					<table id="resource_voteUser" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
						<thead>
							<tr>
								<th rowspan="1" colspan="1" style="width: 25%;">投票日期</th>
								<th rowspan="1" colspan="1" style="width: 25%;">用户昵称</th>
								<th rowspan="1" colspan="1" style="width: 25%;">用户名</th>
								<th rowspan="1" colspan="1" style="width: 25%;">备注</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="user_info_global" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
		data-id="">
		<div class="modal-dialog" style="width: 75%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="user_label">用户详情</h4>
				</div>
				<div class="modal-body" style="min-height: 600px;">
					<iframe height=560 width=100% id="iframe" src="" frameborder=0
						allowfullscreen></iframe>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<!-- 	<button id="confirm_btn" type="button" class="btn btn-primary">确定</button> -->
				</div>
			</div>
		</div>
	</div>
</body>