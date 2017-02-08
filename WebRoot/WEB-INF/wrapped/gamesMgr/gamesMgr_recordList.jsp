<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	

<link href="/assets/css/site.css" rel="stylesheet" >
<link href="/assets/css/plugins/blueimp/css/blueimp-gallery.min.css" rel="stylesheet">
<link href="/assets/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/admin/gamesMgr/gamesMgr_recordList.js"></script>

<div class="row wrapper border-bottom white-bg page-heading">
	<div class="col-lg-10">
		<h2>游戏管理</h2>
		<ol class="breadcrumb">
			<li class="active">抽奖记录</li>
		</ol>
	</div>
	<div class="col-lg-2">
		<a class="btn btn-sm btn-primary bfR mt20"
			href="/project/games/add/page">新建游戏</a>
	</div>
</div>

<div class="wrapper wrapper-content animated fadeInRight">
	<div class="ibox float-e-margins">
		<div class="ibox-title">
			<h5>抽奖列表</h5>
		</div>
		<div class="ibox-content">
			<div id="list_wrapper" class="dataTables_wrapper">
				<div id="filterSearch">
					<div style="display:inline">
						游戏名称：<select name="gameName" id="searchButton_gameName" >
									<option value="">所有</option>
									<c:if test="${not empty requestScope.lotterys}">
										<c:forEach var="item" items="${requestScope.lotterys}">
											<option value="${item.id}">${item.name}</option>
										</c:forEach>
									</c:if>
								 </select>&emsp;&emsp;
					</div>	
					<div id="winer" style="display:inline">
						是否中奖：<select name="resourceType" id="searchButton_isWinner" >
									<option value="">所有</option>
									<option value="YES">中奖用户</option>
									<option value="NO">未中奖用户</option>
								 </select>&emsp;&emsp;
					</div>
					<div id="handle" class="hide" style="display:inline">
						处理状态: <select name="resourceState" id="searchButton_isHandle" >
									<option value="">所有</option>
									<option value="0">未处理</option>
									<option value="1">已处理</option>
								 </select>&emsp;&emsp;
					</div>
					<div> 
						<button class="btn btn-sm btn-primary bfR mt-25 mr300" id="exchangeTag">兑奖入口</button>
					</div>
				</div><br/>
				<table id="games_record_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="1" colspan="1" style="width: 10%;">游戏名称</th>
							<th rowspan="1" colspan="1" style="width: 10%;">参与用户</th>
							<th rowspan="1" colspan="1" style="width: 10%;">参与日期</th>
							<th rowspan="1" colspan="1" style="width: 10%;">是否中奖</th>
							<th rowspan="1" colspan="1" style="width: 10%;">奖品</th>
							<th rowspan="1" colspan="1" style="width: 10%;">兑换码</th>
							<th rowspan="1" colspan="1" style="width: 10%;">操作日期</th>
							<th rowspan="1" colspan="1" style="width: 10%;">备注</th>
							<th rowspan="1" colspan="1" style="width: 10%;">兑换状态</th>
							<th rowspan="1" colspan="1" style="width: 10%;">操作</th>
						</tr>
					</thead>
				</table>
			</div>
			
		</div>
	</div>
</div>

<div class="modal fade" id="exchangeModel" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="urlModalLabel">游戏兑奖</h4>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label">请输入兑换码：</label>
						<div class="col-md-8">
							<input type="text" class="form-control" id="exchangeCode" maxlength="200" />
						</div>
					</div>
				</div>	
			</div>
			<div class="modal-footer">
				<button id="doExchnage" class="btn btn-primary" data-clipboard-target="fe_text">查询</button>
			</div>
		</div>
	</div>
</div>
