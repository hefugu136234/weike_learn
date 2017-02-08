<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<script src="/assets/js/common.js"></script>
<script src="/assets/js/admin/resource_overView/collection_user.js"></script>
<script src="/assets/js/admin/resource_overView/praise_user.js"></script>
<script src="/assets/js/admin/resource_overView/comment_user.js"></script>
</head>

<body class="gray-bg">
	<!-- 资源uuid -->
	<input type="hidden" id="resUuid"
		value="${requestScope.resource.uuids.resourceUuid }">
	<div class="row">
		<div class="col-lg-9" style="width: 100%">
			<div class="wrapper wrapper-content">
				<!-- 基本信息 start -->
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>资源基础信息</h5>
					</div>
					<div class="ibox-content">
						<div class="row">
							<div class="col-lg-12">
								<!-- <div class="m-b-md">
									<a href="#" class="btn btn-white btn-xs pull-right">编辑</a>
								</div> -->
								<c:if test="${not empty requestScope.resource.state}">
									<dl class="dl-horizontal">
										<dt>状态:</dt>
										<dd>
											<c:choose>
												<c:when test="${requestScope.resource.state eq 0}">
													<span class="label label-danger">未审核</span>
												</c:when>
												<c:when test="${requestScope.resource.state eq 1}">
													<span class="label label-primary">已上线</span>
												</c:when>
												<c:when test="${requestScope.resource.state eq 2}">
													<span class="label label-warning">已下线</span>
												</c:when>
												<c:otherwise>
													<span class="label label-danger">???</span>
												</c:otherwise>
											</c:choose>
										</dd>
									</dl>
								</c:if>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-5">
								<dl class="dl-horizontal">
									<dt>名称:</dt>
									<dd>${ not empty requestScope.resource.name ? requestScope.resource.name : '暂无' }</dd>
									<dt>讲者:</dt>
									<dd>${ not empty requestScope.resource.speakerVo.name ? requestScope.resource.speakerVo.name : '暂无' }</dd>
									<dt>创建日期:</dt>
									<dd>${ not empty requestScope.resource.createDate ? requestScope.resource.createDate : '暂无' }</dd>
									<dt>类型:</dt>
									<dd>${ not empty requestScope.resource.type ? requestScope.resource.type : '暂无' }</dd>
								</dl>
							</div>
							<div class="col-lg-7" id="cluster_info">
								<dl class="dl-horizontal">
									<dt>最后编辑时间:</dt>
									<dd>${ not empty requestScope.resource.dates ? requestScope.resource.dates : '暂无' }</dd>
									<dt>浏览次数:</dt>
									<dd>${ not empty requestScope.resource.viewCount ? requestScope.resource.viewCount : '暂无' }</dd>
								</dl>
							</div>
						</div>
					</div>
				</div>

				<!-- 投票信息 start -->
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>投票信息</h5>
					</div>
					<div class="ibox-content">
						<div class="panel no-radius vote-panel">
							<div class="panel-body">
								<div id="vote_contain">
									<c:choose>
										<c:when test="${not empty requestScope.votes}">
											<c:forEach items="${requestScope.votes}" var="vote_subject">
												<div class="box">
													<div class="tt">
														<c:choose>
															<c:when test="${vote_subject.type eq 1}">
																<span class="type">【单选题】</span>
															</c:when>
															<c:when test="${vote_subject.type eq 2}">
																<span class="type">【多选题】</span>
															</c:when>
														</c:choose>
														<span>${vote_subject.title }</span>
													</div>
													<c:choose>
														<c:when test="${not empty vote_subject.options}">
															<div class="list-group answer-list result">
																<ol>
																	<c:forEach items="${vote_subject.options}"
																		var="vote_option">
																		<li><label class="list-group-item item">
																				<span>${vote_option.option }</span> <span
																				class="badge"> <i class="num"><a
																						href="/project/voteUser/page/${vote_option.uuid }">(${vote_option.count }票)</a></i>
																			</span>
																		</label></li>
																		<div class="progress">
																			<fmt:parseNumber
																				value="${vote_subject.optionsCountSum}"
																				type="number" var="optionsCountSum" />
																			<fmt:parseNumber value="${vote_option.count}"
																				type="number" var="count" />
																			<fmt:formatNumber
																				value="${(count/optionsCountSum)*100}"
																				pattern="#.##" var="show" maxFractionDigits="2" />
																			<div
																				class="progress-bar progress-bar-success progress-bar-striped"
																				role="progressbar"
																				aria-valuenow="${vote_option.count }"
																				aria-valuemin="0"
																				aria-valuemax="${vote_subject.optionsCountSum }"
																				style="width: ${show}%">
																				<span>${show}%</span>
																			</div>
																		</div>
																	</c:forEach>
																</ol>
															</div>
														</c:when>
														<c:otherwise>
															<strong>暂无选项</strong>
														</c:otherwise>
													</c:choose>
												</div>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<strong>暂无投票信息</strong>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- 页脚标签页 start-->
				<div class="row m-t-sm">
					<div class="col-lg-12">
						<div class="panel blank-panel">
							<div class="panel-heading">
								<div class="panel-options">
									<ul class="nav nav-tabs">
										<li class="active"><a href="#tab-collection"
											data-toggle="tab">收藏记录</a></li>
										<li class=""><a href="#tab-praise" data-toggle="tab">点赞记录</a></li>
										<li class=""><a href="#tab-comment" data-toggle="tab">评论记录</a></li>
									</ul>
								</div>
							</div>
							<div class="panel-body">
								<div class="tab-content">
									<!-- 收藏 -->
									<div class="tab-pane active" id="tab-collection">
										<div class="wrapper wrapper-content animated fadeInRight">
											<div class="ibox float-e-margins">
												<div class="ibox-content">
													<div id="list_wrapper" class="dataTables_wrapper">
														<table id="collection_user"
															class="display dataTable dtr-inline" cellspacing="0"
															width="100%" role="grid" style="width: 100%;">
															<thead>
																<tr>
																	<th rowspan="1" colspan="1" style="width: 25%;">收藏日期</th>
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
									</div>
									<!-- 点赞 -->
									<div class="tab-pane" id="tab-praise">
										<div class="wrapper wrapper-content animated fadeInRight">
											<div class="ibox float-e-margins">
												<div class="ibox-content">
													<div id="list_wrapper" class="dataTables_wrapper">
														<table id="praise_user"
															class="display dataTable dtr-inline" cellspacing="0"
															width="100%" role="grid" style="width: 100%;">
															<thead>
																<tr>
																	<th rowspan="1" colspan="1" style="width: 25%;">点赞日期</th>
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
									</div>
									<!-- 评论 -->
									<div class="tab-pane" id="tab-comment">
										<div class="wrapper wrapper-content animated fadeInRight">
											<div class="ibox float-e-margins">
												<div class="ibox-content">
													<div id="list_wrapper" class="dataTables_wrapper">
														<table id="resource_comment"
															class="display dataTable dtr-inline" cellspacing="0"
															width="100%" role="grid" style="width: 100%;">
															<thead>
																<tr>
																	<th rowspan="1" colspan="1" style="width: 15%;">日期</th>
																	<th rowspan="1" colspan="1" style="width: 15%;">用户昵称</th>
																	<th rowspan="1" colspan="1" style="width: 15%;">用户名</th>
																	<th rowspan="1" colspan="1" style="width: 15%;">目标</th>
																	<th rowspan="1" colspan="1" style="width: 25%;">内容</th>
																	<th rowspan="1" colspan="1" style="width: 15%;">操作</th>
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
					</div>
				</div>
				<!-- 页脚标签页 end-->
			</div>
		</div>
	</div>
</body>