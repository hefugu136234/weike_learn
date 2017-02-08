<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/activity/oups_code_list.js"></script>
<style>
.small-chat-box {
	margin-top: 10px;
  background: #fff;
  border: 1px solid #e7eaec;
  border-radius: 4px;
}
.small-chat-box.ng-small-chat {
  display: block;
}
.body-small .small-chat-box {
  bottom: 70px;
  right: 20px;
}
.small-chat-box.active {
  display: block;
}
.small-chat-box .heading {
  background: #2f4050;
  padding: 8px 15px;
  font-weight: bold;
  color: #fff;
}
.small-chat-box .chat-date {
  opacity: 0.6;
  font-size: 10px;
  font-weight: normal;
}
.small-chat-box .content {
  padding: 15px 15px;
}
.small-chat-box .content .author-name {
  font-weight: bold;
  margin-bottom: 3px;
  font-size: 11px;
}
.small-chat-box .content > div {
  padding-bottom: 20px;
}
.small-chat-box .content .chat-message {
  padding: 5px 10px;
  border-radius: 6px;
  font-size: 11px;
  line-height: 14px;
  max-width: 80%;
  background: #f3f3f4;
  margin-bottom: 10px;
}
.small-chat-box .content .chat-message.active {
  background: #1ab394;
  color: #fff;
}
.small-chat-box .content .left {
  text-align: left;
  clear: both;
}
.small-chat-box .content .left .chat-message {
  float: left;
}
.small-chat-box .content .right {
  text-align: right;
  clear: both;
}
.small-chat-box .content .right .chat-message {
  float: right;
}
.small-chat-box .form-chat {
  padding: 10px 10px;
}
.msg-del{
	margin: 10px;
}
</style>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>作品编号管理</h2>
		<ol class="breadcrumb">
			<li class="active">作品编号列表</li>
		</ol>
	</div>
	<!-- <div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/pdf/add/page">新建PDF</a>
	</div> -->
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>作品编号列表</h5>
		</div>
		<div class="ibox-content">

			<div id="list_wrapper" class="dataTables_wrapper">
				<table id="oups_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">

					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">作品编号</th>
							<th rowspan="1" colspan="1" style="width: 10%;">作品名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>
							<th rowspan="1" colspan="1" style="width: 10%;">作品申请者</th>
							<th rowspan="1" colspan="1" style="width: 15%;">作品分类</th>
							<th rowspan="1" colspan="1" style="width: 15%;">活动名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th style="width: 20%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
	</div>
</div>

<!-- 作品信息 -->
<div class="modal fade" id="dataModal_oups_view" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">查看作品的信息：</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label">作品信息：</label>
						<div class="col-sm-8">
							<div id="opus_info_container" style="padding:10px; background-color: #e0e0e0"></div>	
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-3 control-label">状态控制：</label>
						<div class="col-sm-5">
							<!-- 	<div><input type="checkbox" value="3"><span>收到作品</span></div>
						<div><input type="checkbox" value="4"><span>初审中</span></div>
						<div><input type="checkbox" value="5"><span>作品转码</span></div>
						<div><input type="checkbox" value="6"><span>专业审核</span></div>
						<div><input type="radio" name="status_final" value="8">合格 <input type="radio" name="status_final" value="">不合格</div> -->

							<select class="form-control" id="status_controller">
								<option value="0">初始状态</option>
								<option value="3">收到作品</option>
								<option value="4">初审中</option>
								<option value="5">作品转码</option>
								<option value="6">专业审核</option>
								<option value="8">合格</option>
								<option value="10">不合格</option>
							</select>

						</div>
					</div>

					<!-- 互动环节 -->

					<div class="form-group">
						<label class="col-sm-3 control-label">作品互动：</label>
						<div
							class="col-sm-8">
							<div class="row">
	
								<div class="col-sm-10">
									<textarea rows="2" class="form-control" id="interaction_message"
									maxlength="60" name="description" placeholder=""></textarea>
								</div>
								<div class="col-sm-2">
									<button id="msg_send_btn" class="btn btn-primary" type="button">发表</button>
								</div>
								
								<div>
								</div>
								
							</div>
							
							<div class="small-chat-box">
								<div id="interaction_frame" class="content">
									<div class="left">
	                    <div class="author-name">
	                        Monica Jackson <small class="chat-date">
	                        10:02 am
	                    </small>
	                    </div>
	                    <div class="chat-message active">
	                        Lorem Ipsum is simply dummy text input.
	                    </div>
	                </div>
	                <div class="right">
	                    <div class="author-name">
	                        Mick Smith
	                        <small class="chat-date">
	                            11:24 am
	                        </small>
	                    </div>
	                    <div class="chat-message">
	                        Lorem Ipsum is simpl.
	                    </div>
	                </div>
									  
								</div>
							</div>
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
<!-- 作品信息 -->

<script type="text/javascript">
	
</script>
