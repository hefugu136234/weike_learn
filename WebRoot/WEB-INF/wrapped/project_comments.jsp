<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet"
	href="/assets/css/plugins/datapicker/datepicker3.css">
<link rel="stylesheet"
	href="/assets/css/plugins/rangeSlider/ion.rangeSlider.css">
<link rel="stylesheet"
	href="/assets/css/plugins/rangeSlider/ion.rangeSlider.skinFlat.css">
<link rel="stylesheet" type="text/css" href="/assets/css/top.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/web/comments.css" />

<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>
<script src="/assets/js/plugins/rangeSlider/ion.rangeSlider.min.js"></script>
<script type="text/javascript" src="/assets/js/admin/top.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/project_comments.js"></script>


<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>资源评论</h2>
		<!-- <ol class="breadcrumb">
			<li class="active">评论列表</li>
		</ol> -->
		<div id="filterSearch">
			<div id="user_send" style="display: inline">
				<div class="form-group">
					<label class="col-sm-1">目标：</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="target"
							placeholder="资源或用户名">
					</div>
				</div>
			</div>
			<div id="user_receive" style="display: inline">
				<div class="form-group">
					<label class="col-sm-1">内容：</label>
					<div class="col-md-2">
						<input type="text" class="form-control" id="content"
							placeholder="评论的内容">
					</div>
				</div>
			</div>
			<div style="display: inline">
				<div class="form-group" id="timeRange">
					<label class="col-sm-1">时间：</label>
					<div class="col-md-4">
						<div class="input-daterange input-group" id="datepicker">
							<input type="text" class="input-sm form-control"
								name="range_start" id="range_start" value="" /> <span
								class="input-group-addon">to</span> <input type="text"
								class="input-sm form-control" name="range_end" id="range_end"
								value="" />
						</div>
					</div>
				</div>
			</div>
			<div style="display: inline">
				<div class="bfR">
					<input id="clean" class="btn btn-primary" type="button" value="清空">
				</div>
			</div>
		</div>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="row">
		<div class="ibox float-e-margins">
			<div class="ibox-title">
				<h5>资源评论</h5>
				<div class="ibox-tools">
					<span class="label label-default pull-right" id="comment_num">?
						Messages</span>
				</div>
			</div>
			<div class="ibox-content">
				<div class="feed-activity-list" id="comment_list">
					<span>加载中......</span>

					<!-- <div class="feed-element">
						<a href="profile.html" class="pull-left"> <img alt="image"
							class="img-circle" src="img/a5.jpg">
						</a>
						<div class="media-body ">
							<small class="pull-right">2h ago</small> <strong>Kim
								Smith</strong> posted message on <strong>Monica Smith</strong> site. <br>
							<small class="text-muted">Yesterday 5:20 pm - 12.06.2014</small>
							<div class="well">Lorem Ipsum is simply dummy text of the
								printing and typesetting industry. Lorem Ipsum has been the
								industry's standard dummy text ever since the 1500s. Over the
								years, sometimes by accident, sometimes on purpose (injected
								humour and the like).</div>
							<div class="pull-right">
								<a class="label label-warning"> 审核 </a>
							</div>
						</div>
					</div> -->
				</div>
				<div class="row">
					<!-- <div class="col-lg-4">
						<button class="btn btn-primary m-t">
							<i class="fa fa-arrow-down"></i> 查看更多...
						</button>
					</div> -->
					<div class="text-center">
						<ul id="pagination_ul" class="pagination"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<a href="#" onclick="gotoTop();return false;" class="totop"></a>


<div class="modal fade" id="commonsDetailModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">评论详情</h4>
			</div>
			<div class="modal-body">
				<ul class="dialog-comments-list" id="dialog_comments_list">
				</ul>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>