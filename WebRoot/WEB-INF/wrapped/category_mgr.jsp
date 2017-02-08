<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<link rel="stylesheet"
	href="/assets/css/plugins/ladda/ladda-themeless.min.css">
<link rel="stylesheet" type="text/css"
	href="/assets/js/uploadify/uploadify.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/jstree/tree_style.min.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/chosen/chosen.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/bootstrapTags/bootstrap-tags.css" />

<script src="/assets/js/plugins/ladda/spin.min.js"></script>
<script src="/assets/js/plugins/ladda/ladda.min.js"></script>
<script src="/assets/js/plugins/ladda/ladda.jquery.min.js"></script>

<script src="/assets/js/plugins/jstree/jstree.min.js"></script>
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/plugins/chosen/chosen.jquery.js"></script>
<script src="/assets/js/plugins/bootstrapTags/bootstrap-tags.js"></script>
<script src="/assets/js/uploadify/jquery.uploadify.min.js"></script>
<script src="/assets/js/admin/category_mgr_detail.js?ver=1.0"></script>
<script src="/assets/js/admin/category_mgr.js?ver=1.0"></script>
<script src="/assets/js/admin/uploader_common.js"></script>


<style>
<!--
#container {
	min-width: 320px;
	margin: 0px auto 0 auto;
	border-radius: 0px;
	padding: 0px;
	overflow: hisdden;
}

#tree {
	float: left;
	min-width: 200px;
	border-right: 1px solid silver;
	overflow: auto;
	padding: 0px 0;
	min-height: 600px;
}

#data {
	margin-left: 320px;
}

#tree_contoller {
	height: 40px;
	margin-left: 210px;
	font-size: 14px;
}

#tree_selected_sign {
	min-width: 240px;
	display: inline-block;
}

#tree_controller_foo {
	display: inline-block;
	margin-left: 80px;
}

#data textarea {
	margin: 0;
	padding: 0;
	height: 100%;
	width: 100%;
	border: 0;
	background: white;
	display: block;
	line-height: 18px;
}

#data, #code {
	font: normal normal normal 12px/18px 'Consolas', monospace !important;
}

#parentTagsTable tbody tr:hover td {
	background: #E2E4E5;
	cursor: pointer;
}

#childTagsTable tbody tr:hover td {
	background: #E2E4E5;
	cursor: pointer;
}

#parentTagsTable td, th {
	text-align: center;
}

#childTagsTable td, th {
	text-align: center;
}

table {
	table-layout: fixed;
}

td {
	white-space: nowrap;
	overflow: hidden;
	word-break: keep-all;
	text-overflow: ellipsis
}
-->
</style>


<div id="container">
	<div id="tree" class="position" style="position: absolute;"></div>
	<div id="tree_contoller">
		<div id="tree_selected_sign"></div>
		<div id="tree_controller_foo">
			<input id="foo_add" class="btn btn-primary" type="button"
				data-toggle="modal" data-target="#categoryModal" value="新增子分类">
			<input id="foo_edit" class="btn btn-primary" data-toggle="modal"
				data-target="#categoryModal" type="button" value="分类编辑"> <input
				id="cover_edit" data-toggle="modal" data-target="#coverModal"
				class="btn btn-primary" type="button" value="封面编辑" /> <input
				id="foo_del" class="btn btn-primary" type="button" value="删除">
		</div>

		<div class="bfR">
			<input id="res_add" class="btn btn-primary" type="button"
				data-toggle="modal" data-target="#resAddModal" value="新增资源">
		</div>

		<div>
			<div class="content code" style="display: none;">
				<textarea id="code" readonly="readonly"></textarea>
			</div>
			<div class="content folder" style="display: none;"></div>
			<div class="content image" style="display: none; position: relative;">
				<img src="" alt=""
					style="display: block; position: absolute; left: 50%; top: 50%; padding: 0; max-height: 90%; max-width: 90%;" />
			</div>
			<div class="content default" style="text-align: left;">
				<!-- Select
				a node from the tree. -->
				&nbsp;
			</div>
		</div>

		<div>
			<div class="content code" style="display: none;">
				<textarea id="code" readonly="readonly"></textarea>
			</div>
			<div id="category_wrapper">
				<div id="filterSearch">
					资源类型：<select name="resourceType" id="searchButton_type">
						<option value="">所有</option>
						<option value="VIDEO">VIDEO</option>
						<option value="PDF">PDF</option>
						<option value="NEWS">NEWS</option>
						<option value="THREESCREEN">THREESCREEN</option>
					</select>&emsp;&emsp; 资源状态: <select name="resourceState"
						id="searchButton_state">
						<option value="">所有</option>
						<option value="0">未审核</option>
						<option value="1">已上线</option>
						<option value="2">已下线</option>
					</select>&emsp;&emsp;
				</div>
				<br />
				<table id="categoryDetail_table"
					class="display nowrap dataTable dtr-inline" style="width: 100%"
					role="grid">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 20%;">资源名称</th>
							<th rowspan="1" colspan="1" style="width: 15%;">修改日期</th>
							<th rowspan="1" colspan="1" style="width: 10%;">次数</th>
							<th rowspan="1" colspan="1" style="width: 10%;">讲者</th>
							<th rowspan="1" colspan="1" style="width: 10%;">类型</th>
							<!-- <th rowspan="1" colspan="1" style="width: 20%;">备注</th> -->
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th rowspan="1" colspan="1" style="width: 25%;">操作</th>
							<!-- <th rowspan="1" colspan="1" style="width: 5%;">关联作品</th> -->
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="categoryModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">分类管理</h4>
			</div>
			<div class="modal-body">
				<input id="category_name" class="form-control" placeholder="输入分类名"
					required="required">
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="save_btn" type="button" class="btn btn-primary">保存</button>
			</div>
		</div>
	</div>
</div>

<!-- Modal 2 -->
<div class="modal fade" id="coverModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 id="cover_category_name" class="modal-title"
					id="categoryModalLabel"></h4>
			</div>

			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label">封面场景：</label>
						<div class="col-sm-6">
							<select name="plam_select" id="plam_select" class="form-control"
								required="required">
								<option value="1" selected="selected">微信学科封面</option>
								<option value="13">微信"知了推荐"学科封面</option>
								<option value="3">TV学科封面</option>
								<option value="4">WEB学科样式</option>
							</select>
						</div>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<label class="col-sm-3 control-label">封面类型：</label>
						<div class="col-sm-6">
							<select name="type_select" id="type_select" class="form-control"
								required="required">
								<option value="pic" selected="selected">图片</option>
								<option value="css">CSS样式</option>
							</select>
						</div>
					</div>
					<div class="hr-line-dashed"></div>

					<div id="pic">
						<div class="form-group">
							<label class="col-sm-3 control-label">图片：</label>
							<div class="col-sm-6">
								<input id="cover_uploadify" type="file"> <img
									id="cover_preview" alt="" src="" style="display: none;"
									class="pre-view"> <input type="hidden" name="coverTaskId"
									id="coverTaskId" />
							</div>
						</div>
					</div>

					<div id="style" class="hide">
						<div class="form-group">
							<label class="col-sm-3 control-label">样式：</label>
							<div class="col-sm-6">
								<textarea rows="3" class="form-control" id="css"
									maxlength="1000" name="css" placeholder=""></textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label"></label>
							<div class="col-sm-6">
								<a class="btn btn-sm btn-primary bfR"
									href="http://v3.bootcss.com/components/">点击查看参考样式</a>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="cover_save" type="button" class="btn btn-primary">保存</button>
			</div>
		</div>
	</div>
</div>
<!-- Modal 2 -->


<!-- Modal 3 -->
<div class="modal fade" id="oupsModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">资源作品关联</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label">资源名称：</label>
						<div class="col-sm-6">
							<input type="text" id="res_name" class="form-control"
								readonly="readonly" />
						</div>
					</div>

					<div id="oups_code_div" class="form-group">
						<label class="col-sm-3 control-label">作品编号：</label>
						<div class="col-sm-6">
							<input type="text" id="oups_code" class="form-control" />
						</div>
						<input type="button" id="code_search" value="编号查询"
							class="btn btn-primary" />
					</div>

					<div id="oups_total_div" class="form-group">
						<!-- 作品相关信息 -->
						<div class="form-group">
							<label class="col-sm-3 control-label">作品名称：</label>
							<div class="col-sm-6">
								<label id="oups_name_label"></label>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">作品编号：</label>
							<div class="col-sm-6">
								<label id="oups_code_label"></label>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">关联活动：</label>
							<div class="col-sm-6">
								<label id="oups_relate_label"></label>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">申请者：</label>
							<div class="col-sm-6">
								<label id="oups_user_label"></label>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">作品分类：</label>
							<div class="col-sm-6">
								<label id="oups_category_label"></label>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">交付方式：</label>
							<div class="col-sm-6">
								<label id="oups_send_label"></label>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">作品简介：</label>
							<div class="col-sm-6">
								<label id="oups_desc_label"></label>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-6">
								<button id="unbundle_oups_button" style="margin-left: 100px"
									type="button" class="btn btn-primary">解除绑定</button>
								<span id="bundled_tips"
									style="margin-left: 100px; display: none; color: red;">已经绑定资源</span>
							</div>
						</div>

						<div id="oups_error_div" class="form-group">
							<label class="col-sm-3 control-label"></label>
							<div class="col-sm-6">
								<label id="oups_error_label" style="color: red"></label>
							</div>
						</div>

					</div>
					<input type="hidden" id="res_uuid_hide" /> <input type="hidden"
						id="oupd_uuid_hide" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button id="oups_button" type="button" class="btn btn-primary">确认</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Modal 3 -->

<!-- 资源备注完整显示模态框 -->
<div class="modal fade" id="markModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">完整信息如下</h4>
			</div>
			<div class="modal-body">
				<div id="markDetail"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>

<!-- 资源添加选择模态框 -->
<div class="modal fade" id="resAddModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="chooseType">请选择资源类型</h4>
			</div>
			<div class="modal-body">
				<table>
					<tr>
						<td width="25%"><input id="resAdd_VIDEO"
							class="btn btn-primary" type="button" data-toggle="modal"
							data-target="#categoryModal" value="VIDEO"></td>
						<td width="25%"><input id="resAdd_PDF"
							class="btn btn-primary" type="button" data-toggle="modal"
							data-target="#categoryModal" value="PDF"></td>
						<td width="25%"><input id="resAdd_NEWS"
							class="btn btn-primary" type="button" data-toggle="modal"
							data-target="#categoryModal" value="NEWS"></td>
						<td width="25%"><input id="resAdd_THREESCREEN"
							class="btn btn-primary" type="button" data-toggle="modal"
							data-target="#categoryModal" value="THREESCREEN"></td>
					</tr>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>

<!-- 标签操作 -->
<%-- <input type="hidden" id="tagsResSaveToken" value="${requestScope.token}" /> --%>
<div class="modal fade" id="tagsModel" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="ModalLabel">资源标签操作</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<div id="existTags"></div>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group">
						<div id="saveTags">
							<div id="parentTags"
								style="width: 200px; float: left; height: 300px; overflow: scroll; border: 1px gray solid;">
								<table id="parentTagsTable" class="table">
									<thead>
										<tr>
											<th>标签类别</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<div id="childTags"
								style="width: 300px; margin-left: 250px; height: 300px; overflow: scroll; border: 1px gray solid;">
								<table id="childTagsTable" class="table">
									<thead>
										<tr>
											<th>选择</th>
											<th>标签名称</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="form-group">
						<input type="button" value="选中全部"
							class="btn btn-sm btn-primary mt20" id="selectAll"> <input
							type="button" value="取消全部" class="btn btn-sm btn-primary mt20"
							id="calcelAll"> <input type="button" value="反选"
							class="btn btn-sm btn-primary mt20" id="reverse">
						<button type="button" id="checkboxSubmitButton"
							class="btn btn-sm btn-primary bfR mt20">提交</button>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="embedModel" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" data-id="">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="ModalLabel">嵌入代码</h4>
			</div>
			<div class="modal-body">
				<span id="iframe-code-container"> </span>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>

<!-- Res Access -->
<div class="modal fade" id="resourceAccess" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel">资源访问限制</h4>
			</div>
			<div class="modal-body" id="resourceAccessBody">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th width="20%" class="text-center">权限/场景</th>
							<th class="text-center">未注册</th>
							<th class="text-center">已注册，未实名</th>
							<th class="text-center">实名</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="text-center">不可浏览</td>
							<td class="text-center"><input type="checkbox" value="#1#,#1#"></td>
							<td class="text-center"><input type="checkbox" value="#2#,#1#"></td>
							<td class="text-center"><input type="checkbox" value="#3#,#1#"></td>
						</tr>
						<tr>
							<td class="text-center">不可观看</td>
							<td class="text-center"><input type="checkbox" value="#1#,#2#"></td>
							<td class="text-center"><input type="checkbox" value="#2#,#2#"></td>
							<td class="text-center"><input type="checkbox" value="#3#,#2#"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button id="resAccessSave" type="button" class="btn btn-primary">保存</button>
			</div>
		</div>
	</div>
</div>

