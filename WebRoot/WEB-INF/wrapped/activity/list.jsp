<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/activity/main.js?ver=1.0"></script>
<script type="text/javascript"
	src="/assets/js/admin/activity/list.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li class="active">活动列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/activity/new">新建活动</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>活动列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="display nowrap dataTable dtr-inline">
				<table id="activity_list_table" width="100%"
					class="display nowrap dataTable dtr-inline" style="width: 100%"
					role="grid">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">活动名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建者</th>
							<th rowspan="1" colspan="1" style="width: 10%;">学科分类</th>
							<th rowspan="1" colspan="1" style="width: 5%;">参与人数</th>
							<th rowspan="1" colspan="1" style="width: 15%;">简介</th>
							<th rowspan="1" colspan="1" style="width: 5%;">状态</th>
							<th style="width: 35%;">操作</th>
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
				<h4 class="modal-title" id="categoryModalLabel_pdf">直播的二维码：</h4>
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

<!-- 发送模版消息 -->
<div class="modal fade" id="dataModal_message" tabindex="-1" role="dialog"
						aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel_pdf">模版消息详情设置：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<h3><font color="green">请填写信息详情</font></h3>
					<div class="form-group">
						<label class="col-sm-3 control-label">主题：</label>
						<div class="col-sm-8 controls">
							<input type="text" class="form-control" id="subject" name="subject"
								placeholder="信息的标题，例如：您好，活动又有新内容啦！"
								autocomplete="off" required="required">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-3 control-label">内容：</label>
						<div class="col-sm-8">
							<textarea class="form-control" id="content" name="content" cols="60" rows="4" maxlength="200" 
											placeholder="变更的内容，例如：本活动新增多位知名专家参加，快来围观（限200字以内）"></textarea>
						</div>
					</div>
				</div>
				<div class="hide">
					<div class="row">
			            <div class="col-lg-12">
			           		<h3><font color="green">请选择活动成员</font></h3>	
			                <div class="ibox-content">
			                    <ul class="todo-list m-t" style="height:200px; overflow-y:auto;"></ul>
			                </div>
			            </div>
					</div>
					<div>
						<input type="button" value="选中全部" class="btn btn-sm btn-primary mt20" id="selectAll"> 
		            	<input type="button" value="取消全部" class="btn btn-sm btn-primary mt20" id="calcelAll"> 
		            	<input type="button" value="反选" class="btn btn-sm btn-primary mt20" id="reverse">
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button id="message_confirm_btn" type="button" class="btn btn-primary">发送</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 发送模版消息 -->

<!-- 直播的链接 -->
<div class="modal fade" id="dataModal_link" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">活动的banner的链接：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="control-label col-md-2">微信链接</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="wx_content" />
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-md-2">web链接</label>
						<div class="col-md-9">
							<input type="text" class="form-control" id="web_content" />
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

<script type="text/javascript">
	$(function() {
		showActive([ 'activity-list-nav', 'activity-mgr' ]);
		var error = '${requestScope.error}';
		if (!!error) {
			alert(error);
		}
	});
</script>
