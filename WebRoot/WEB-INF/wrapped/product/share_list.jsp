<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript" src="/assets/js/admin/product/share_list.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>分享有礼管理</h2>
		<ol class="breadcrumb">
			<li class="active">分享有礼列表</li>
		</ol>
	</div>
</div>

 
 
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>分享列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="share_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 30%;">分享课程</th>
							<th rowspan="1" colspan="1" style="width: 15%;">分享者</th>
							<th rowspan="1" colspan="1" style="width: 15%;">查看人</th>
							<th rowspan="1" colspan="1" style="width: 20%;">查看时间</th>
							<th rowspan="1" colspan="1" style="width: 20%;">点击次数</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

<!-- <script type="text/javascript">
	var status = '${requestScope.error_info}';
	if (!!status) {
		alert(status);
	}
</script> -->
