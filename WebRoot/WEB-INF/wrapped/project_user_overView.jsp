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

<script src="/assets/js/jquery.js"></script>
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/bootstrap.min.js"></script>
<script src="/assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="/assets/js/main.js"></script>
<script src="/assets/js/plugins/pace/pace.min.js"></script>
<script src="/assets/js/admin/common_datatable.js"></script>
<script src="/assets/js/common.js"></script>
<script src="/assets/js/admin/user_overView/project_user_overView.js"></script>
<script
	src="/assets/js/admin/user_overView/user_overView_integral_detail.js"></script>
<script src="/assets/js/admin/user_overView/user_overView_workers.js"></script>
<script src="/assets/js/admin/user_overView/user_overView_collection.js"></script>
<script src="/assets/js/admin/user_overView/user_overView_praise.js"></script>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<input type="hidden" id="isHasAvatar"
			value="${requestScope.user_info.avatar }">

		<div class="row">
			<div class="col-sm-6">
				<div class="ibox float-e-margins">
					<div class="ibox-content">
						<div class="clearfix">
							<div class="pull-left" id="head_image">
								<img alt="image" id="headImage" class="img-circle" src=""
									width="100" height="100">
							</div>
							<div class="media-body" style="padding-left: 24px;">
								<h4 class="clearfix">
									<span class="pull-left">${not empty requestScope.certification_info.name ? requestScope.certification_info.name : not empty requestScope.user_info.username ? requestScope.user_info.username : '' }</span>
									<span class="pull-right">${not empty requestScope.user_info.phone ? requestScope.user_info.phone : '暂无'}</span>
								</h4>
								<p class="row">
									<span class="col-sm-6">${ not empty requestScope.userExpand_info.hospital.name ? requestScope.userExpand_info.hospital.name : '暂无' }</span>
									<span class="col-sm-3">${ not empty requestScope.userExpand_info.departments.name ? requestScope.userExpand_info.departments.name : '暂无' }</span>
									<span class="col-sm-3">${ not empty requestScope.userExpand_info.professor ? requestScope.userExpand_info.professor : '暂无' }</span>
								</p>
								<p class="row">
									<span class="col-sm-4">${not empty requestScope.userExpand_info.city.name ? requestScope.userExpand_info.city.name : '暂无'}</span>
									<span class="col-sm-4">${requestScope.userExpand_info.sex eq "1" ? '男' : '女'}</span>
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-sm-3">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>当前积分</h5>
					</div>
					<div class="ibox-content" style="height: 88px;">
						<h1 class="no-margins">${not empty requestScope.userIntegral_info.current ? requestScope.userIntegral_info.current : '暂无记录'}</h1>
					</div>
				</div>
			</div>
			<div class="col-sm-3">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>最高积分</h5>
					</div>
					<div class="ibox-content" style="height: 88px;">
						<h1 class="no-margins">${not empty requestScope.userIntegral_info.history ? requestScope.userIntegral_info.history : '暂无记录'}</h1>
					</div>
				</div>
			</div>
		</div>

		<!-- 实名信息 start -->
		<div class="ibox float-e-margins">
			<input type="hidden" id="isCertification"
				value="${requestScope.certification_info.uuid }">

			<div class="ibox-title">
				<h5>实名信息</h5>
			</div>
			<div class="ibox-content">
				<div class="clearfix">
					<div class="pull-left">
						<img alt="image"
							src="${not empty requestScope.certification_info.imageUrl ? requestScope.certification_info.imageUrl : ''}"
							height="250">
					</div>
					<div class="media-body" style="padding-left: 24px;">
						<div id="certification_info">${not empty requestScope.certification_info.credentials ? requestScope.certification_info.credentials : ''}
						</div>
						<div id="certification_noInfo">
							<strong>暂无</strong>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 实名信息 end -->

		<!-- 物流地址 start -->
		<div class="ibox float-e-margins">
			<input type="hidden" id="isHasAddress"
				value="${requestScope.address_info.uuid }">

			<div class="ibox-title">
				<h5>物流地址</h5>
			</div>
			<div class="ibox-content">
				<div id="address">
					<p>${not empty requestScope.address_info.city.province.name ? requestScope.address_info.city.province.name : ''}</p>
					<p>${not empty requestScope.address_info.city.name ? requestScope.address_info.city.name : ''}</p>
					<p>${not empty requestScope.address_info.district.name ? requestScope.address_info.district.name : ''}</p>
					<p>${not empty requestScope.address_info.address ? requestScope.address_info.address : ''}</p>
					<p>${not empty requestScope.certification_info.name ? requestScope.certification_info.name : ''}&nbsp;&nbsp;
						${not empty requestScope.address_info.phone ? requestScope.address_info.phone : ''}</p>
				</div>
				<div id="noAddress">
					<strong>暂无</strong>
				</div>
			</div>
		</div>

		<!-- 页脚标签页 start-->
		<div class="panel blank-panel">
			<div class="panel-heading">
				<div class="panel-options">
					<ul class="nav nav-tabs">
						<li class="active"><a href="#tab-works" data-toggle="tab">作品</a></li>
						<li class=""><a href="#tab-integralDetail" data-toggle="tab">积分</a></li>
						<li class=""><a href="#tab-collection" data-toggle="tab">收藏</a></li>
						<li class=""><a href="#tab-praise" data-toggle="tab">点赞</a></li>
					</ul>
				</div>
			</div>

			<div class="panel-body">
				<div class="tab-content">

					<!-- 用户作品 -->
					<div class="tab-pane active" id="tab-works">
						<div class="wrapper wrapper-content animated fadeInRight">
							<div class="ibox float-e-margins">
								<div class="ibox-content">
									<div id="list_wrapper" class="dataTables_wrapper">
										<table id="user_works" class="display dataTable dtr-inline"
											cellspacing="0" width="100%" role="grid" style="width: 100%;">
											<thead>
												<tr>
													<th rowspan="1" colspan="1" style="width: 25%;">作品名称</th>
													<th rowspan="1" colspan="1" style="width: 20%;">分类</th>
													<th rowspan="1" colspan="1" style="width: 30%;">日期</th>
													<th rowspan="1" colspan="1" style="width: 10%;">点击数</th>
													<th rowspan="1" colspan="1" style="width: 20%;">状态</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- 积分详情 -->
					<div class="tab-pane" id="tab-integralDetail">
						<input type="hidden" id="userUuid"
							value="${requestScope.user_info.uuid }">
						<div class="wrapper wrapper-content animated fadeInRight">
							<div class="ibox float-e-margins">
								<div class="ibox-content">
									<div id="list_wrapper" class="dataTables_wrapper">
										<table id="user_integral_detail_table"
											class="display dataTable dtr-inline" cellspacing="0"
											width="100%" role="grid" style="width: 100%;">
											<thead>
												<tr>
													<th rowspan="1" colspan="1" style="width: 15%;">积分类型</th>
													<th rowspan="1" colspan="1" style="width: 10%;">日期</th>
													<th rowspan="1" colspan="1" style="width: 15%;">行为</th>
													<th rowspan="1" colspan="1" style="width: 15%;">资源</th>
													<th rowspan="1" colspan="1" style="width: 15%;">积分来源</th>
													<th rowspan="1" colspan="1" style="width: 15%;">分值</th>
													<th rowspan="1" colspan="1" style="width: 15%;">物品</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- 收藏 -->
					<div class="tab-pane" id="tab-collection">
						<div class="wrapper wrapper-content animated fadeInRight">
							<div class="ibox float-e-margins">
								<div class="ibox-content">
									<div id="list_wrapper" class="dataTables_wrapper">
										<table id="user_collection"
											class="display dataTable dtr-inline" cellspacing="0"
											width="100%" role="grid" style="width: 100%;">
											<thead>
												<tr>
													<th rowspan="1" colspan="1" style="width: 20%;">资源名称</th>
													<th rowspan="1" colspan="1" style="width: 20%;">资源类型</th>
													<th rowspan="1" colspan="1" style="width: 20%;">资源讲者</th>
													<th rowspan="1" colspan="1" style="width: 20%;">收藏时间</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- 点赞 -->
					<div class="tab-pane" id="tab-praise">
						<div class="wrapper wrapper-content animated fadeInRight">
							<div class="ibox float-e-margins">
								<div class="ibox-content">
									<div id="list_wrapper" class="dataTables_wrapper">
										<table id="user_praise" class="display dataTable dtr-inline"
											cellspacing="0" width="100%" role="grid" style="width: 100%;">
											<thead>
												<tr>
													<th rowspan="1" colspan="1" style="width: 20%;">资源名称</th>
													<th rowspan="1" colspan="1" style="width: 20%;">资源类型</th>
													<th rowspan="1" colspan="1" style="width: 20%;">资源讲者</th>
													<th rowspan="1" colspan="1" style="width: 20%;">点赞时间</th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
		<!-- 页脚标签页 end-->
	</div>
</body>