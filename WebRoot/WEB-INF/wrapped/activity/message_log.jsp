<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/css/plugins/datetimepicker/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="/assets/css/site.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/moment.min.js"></script>
<script src="/assets/js/monment_zh-cn.js"></script>	
<script src="/assets/js/plugins/datetimepicker/bootstrap-datetimepicker.min.js"
		charset="UTF-8"></script>
<script src="/assets/js/admin/activity/message_list.js"></script>
<script src="/assets/js/admin/activity/main.js?ver=1.0"></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/list">返回活动列表</a></li>
			<li class="active">消息推送列表</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/activity/list">返回活动列表</a>
	</div>
</div>

<input type="hidden" id="activityUuid" value="${requestScope.activity.uuid}"/>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5><font color="green">${requestScope.activity.name}</font>&nbsp;&nbsp;的消息推送列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<div id="filterSearch" class="hide" >
					<div id="user_send" style="display:inline" class="hide" >
						<div class="form-group">
							<label class="col-sm-1">操作人：</label>
							<div class="col-sm-2">
								<select name="gameName" id="searchButton_gameName" class="form-control">
									<option value="">所有</option>
									<%-- <c:if test="${not empty requestScope.lotterys}">
										<c:forEach var="item" items="${requestScope.lotterys}">
											<option value="${item.id}">${item.name}</option>
										</c:forEach>
									</c:if> --%>
								 </select>
							</div>
						</div>
					</div>	
					<div id="user_receive" style="display:inline" class="hide" >
						<div class="form-group">
							<label class="col-sm-1">接收者：</label>
							<div class="col-md-2">
								<input type="text" class="form-control">
							</div>
						</div>
					</div>
					<div style="display:inline">
						<div class="form-group" id="timeRange">
                            <label class="col-sm-1">时间段：</label>
                            <div class="col-md-5">
                            	<div class="input-daterange input-group" id="datepicker">
	                                <input type="text" class="input-sm form-control" name="range_start" id="range_start" readonly="readonly"/>
	                                <span class="input-group-addon">TO</span>
	                                <input type="text" class="input-sm form-control" name="range_end" id="range_end" readonly="readonly"/>
	                            </div>
                            </div>
                        </div>
					</div>
				</div>
				<table id="activity_messageList" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">操作人</th>
							<th rowspan="1" colspan="1" style="width: 10%;">发送时间</th>
							<th rowspan="1" colspan="1" style="width: 60%;">消息概述</th>
							<th rowspan="1" colspan="1" style="width: 10%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>