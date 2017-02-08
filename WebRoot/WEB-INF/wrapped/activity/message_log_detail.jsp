<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<link rel="stylesheet" href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" href="/assets/css/plugins/datapicker/datepicker3.css" >
<link rel="stylesheet" href="/assets/css/plugins/rangeSlider/ion.rangeSlider.css" >
<link rel="stylesheet" href="/assets/css/plugins/rangeSlider/ion.rangeSlider.skinFlat.css" >
<link rel="stylesheet" href="/assets/css/site.css">

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>
<script src="/assets/js/plugins/rangeSlider/ion.rangeSlider.min.js"></script>
<script src="/assets/js/admin/activity/message_list_detail.js"></script>
<script src="/assets/js/admin/activity/main.js?ver=1.0"></script>
	
<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>活动管理</h2>
		<ol class="breadcrumb">
			<li><a href="/admin/activity/message/page/${requestScope.activity.uuid}">返回记录列表</a></li>
			<li class="active">消息推送详情</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/admin/activity/message/page/${requestScope.activity.uuid}">返回记录列表</a>
	</div>
</div>

<input type="hidden" id="notificationUuid" value="${requestScope.notification.uuid}"/>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>记录详情</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<div id="filterSearch" class="hide" >
					<div id="user_send" style="display:inline">
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
					<div id="user_receive" style="display:inline">
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
                            <div class="col-md-3">
                            	<div class="input-daterange input-group" id="datepicker">
	                                <input type="text" class="input-sm form-control" name="range_start" value=""/>
	                                <span class="input-group-addon">to</span>
	                                <input type="text" class="input-sm form-control" name="range_end" value="" />
	                            </div>
                            </div>
                        </div>
					</div>
				</div>
				<table id="activity_messageList" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">目标用户</th>
							<th rowspan="1" colspan="1" style="width: 10%;">发送时间</th>
							<th rowspan="1" colspan="1" style="width: 60%;">消息概述</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>