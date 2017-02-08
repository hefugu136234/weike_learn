<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/assets/css/plugins/chosen/chosen.css">
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/plugins/chosen/chosen.ajaxaddition.jquery.js"></script>
<script type="text/javascript"
	src="/assets/js/admin/wx_subject/subject__activity_list.js"></script>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>
				<font color="red">"${requestScope.sub_name}"</font>的活动banner列表
			</h5>
		</div>
		<div class="ibox-content">
			<div class="form-horizontal">

				<div class="form-group">
					<label class="col-sm-2 control-label">新增活动微信封面：</label>
					<div class="col-sm-3">
						<input type="button" class="btn btn-primary" id="addActivity"
							value="新增" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">动微信封面列表：</label>
					<div class="col-sm-9">
						<table id="wxSubject_activity_list_table"
							class="display nowrap dataTable dtr-inline" style="width: 100%"
							role="grid">

							<thead>
								<tr>
									<th rowspan="1" colspan="1" style="width: 25%;">名称</th>
									<th rowspan="1" colspan="1" style="width: 15%;">创建时间</th>
									<th rowspan="1" colspan="1" style="width: 20%;">封面</th>
									<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
									<th style="width: 30%;">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<!-- 增加活动 -->
<div class="modal fade" id="subject_modal_activity" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">微信学科活动封面设置</h4>
			</div>
			<div class="modal-body">

				<div class="form-horizontal">

					<div id="div_select_activity_total" class="form-group">
						<label class="control-label col-md-3">活动选择：</label>
						<div id="div_select_activity" class="col-md-6">
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-md-3">名称：</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="wx_subject_name_activity" maxlength="50"/>
						</div>
					</div>
                   
                   <div class="form-group">
					<label class="col-sm-3 control-label">banner封面：</label>
					<div id="cover_div_activity" class="col-sm-5">
					</div>
				</div>

				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="confirm_activity" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 增加学科 -->

