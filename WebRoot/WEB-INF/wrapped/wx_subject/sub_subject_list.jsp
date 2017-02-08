<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="/assets/js/admin/wx_subject/sub_subject_list.js"></script>
<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>
				<font color="red">"${requestScope.sub_name}"</font>的子学科列表
			</h5>
		</div>
		<div class="ibox-content">
			<div class="form-horizontal">

				<div class="form-group">
					<label class="col-sm-2 control-label">新增子学科：</label>
					<div class="col-sm-3">
						<input type="button" class="btn btn-primary" id="addCategory"
							value="新增" />
					</div>
				</div>
				<div class="hr-line-dashed"></div>

				<div class="form-group">
					<label class="col-sm-2 control-label">子学科列表列表：</label>
					<div class="col-sm-9">
						<table id="wxSubject_sub_list_table"
							class="display nowrap dataTable dtr-inline" style="width: 100%"
							role="grid">

							<thead>
								<tr>
									<th rowspan="1" colspan="1" style="width: 15%;">名称</th>
									<th rowspan="1" colspan="1" style="width: 15%;">创建时间</th>
									<th rowspan="1" colspan="1" style="width: 15%;">关联分类</th>
									<th rowspan="1" colspan="1" style="width: 20%;">封面</th>
									<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
									<th style="width: 25%;">操作</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<!-- 增加学科 -->
<div class="modal fade" id="subject_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">微信学科设置</h4>
			</div>
			<div class="modal-body">

				<div class="form-horizontal">

					<div class="form-group">
						<label class="control-label col-md-3">学科：</label>
						<div class="col-md-8">
							<div id="subject_tree"></div>
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-md-3">微信学科名称：</label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="wx_subject_name"
								maxlength="50" />
						</div>
					</div>


					<div class="form-group">
						<label class="control-label col-md-3">学科链接类型：</label>
						<div class="col-md-5">
							<select id="linkType" name="linkType" class="form-control">
								<option value="1">子页面链接</option>
								<option value="2">资源页链接</option>
							</select>
						</div>
					</div>

					<span style="color: red">注：子页面链接可以定制下级页面，资源链接直接打开资源列表。</span>

					<div class="form-group">
						<label class="col-sm-3 control-label">学科封面：</label>
						<div id="cover_div" class="col-sm-5"></div>
						<input id="cover_get" type="button" class="btn btn-primary mr20" value="应用分类封面" />
					</div>

				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="confirm_subject" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 增加学科 -->

<!-- 学科层级变更-->
<div class="modal fade" id="floor_modal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">学科层级变更</h4>
			</div>
			<div class="modal-body">

				<div class="form-horizontal">

					<div class="form-group">
						<label class="control-label col-md-3">学科父目录：</label>
						<div class="col-md-5">
							<select id="subject_parent" name="subject_parent" class="form-control">
							</select>
						</div>
					</div>

					

				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="floor_confirm" type="button" class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>
<!-- 学科层级变更 -->