<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<script src="/assets/js/admin/banner/banner_showTableData_cloud.js"></script>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5><font color = "red">知了云盒</font></h5>
		</div>
		<div class="ibox-content">
			<div id="bannerList_cloud" class="dataTables_wrapper">
				<table id="bannerTable_cloud" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;" >
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">预览</th>
							<th rowspan="1" colspan="1" style="width: 10%;">标题</th>
							<th rowspan="1" colspan="1" style="width: 10%;">创建时间</th>	
							<th rowspan="1" colspan="1" style="width: 10%;">展示区域</th>
							<th rowspan="1" colspan="1" style="width: 10%;">REF地址</th>
							<th rowspan="1" colspan="1" style="width: 10%;">平台</th>
							<th rowspan="1" colspan="1" style="width: 10%;">状态</th>
							<th rowspan="1" colspan="1" style="width: 5%;">有效期(天)</th>
							<th rowspan="1" colspan="1" style="width: 15%;">备注</th>
							<th style="width: 10%;">操作</th><!-- 上下线，编辑等 -->
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

<!-- 信息完整显示模态框 -->
<div class="modal fade" id="dataModal_cloud" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="categoryModalLabel_cloud">该单元格完整信息如下：</h4>
			</div>
			<div class="modal-body">
				<div id="dataDetail_cloud" ></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
