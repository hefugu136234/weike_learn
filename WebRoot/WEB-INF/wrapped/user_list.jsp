<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="/assets/js/plugins/dataTables/jquery.dataTables.js"></script>
<link rel="stylesheet" type="text/css"
	href="/assets/css/plugins/dataTables/dataTables.responsive.css">



<div id="list_wrapper" class="dataTables_wrapper">
	<table id="user_list_table" class="display dataTable dtr-inline"
					cellspacing="0" width="100%" role="grid" style="width: 100%;">
		<thead>
			<tr>
				<th rowspan="1" colspan="1" style="width: 114px;">用户名</th>
				<th rowspan="1" colspan="1" style="width: 130px;">昵称</th>
				<th rowspan="1" colspan="1" style="width: 120px;">创建时间</th>
				<th rowspan="1" colspan="1" style="width: 110px;">手机号码</th>
				<th rowspan="1" colspan="1" style="width: 140px;">备注</th>
				<th rowspan="1" colspan="1" style="width: 50px;">状态</th>
				<th style="width: 98px;">操作</th>
			</tr>
		</thead>

	</table>
</div>
<link rel="stylesheet" type="text/css" href="/assets/css/site.css">
<script type="text/javascript">
	$(document).ready(function() {
		showActive([ 'admin_user_mgr', 'user_list_nav' ]);
		$('#user_list_table').dataTable({
			"bProcessing" : true,
			"bServerSide" : true,
			"bStateSave" : false,
			"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
			"fnDrawCallback" : function(oSettings) {
			},
			"iDisplayLength" : 10,
			"iDisplayStart" : 0,
			"sAjaxSource" : "/admin/user/datatable",
			"aoColumns" : [ {
				"mData" : "username",
				"orderable" : false
			}, {
				"mData" : "nickname",
				"orderable" : false
			}, {
				"mData" : "createDate",
				"orderable" : false
			}, {
				"mData" : "phoneNumber",
				"orderable" : false
			}, {
				"mData" : "mark",
				"orderable" : false
			}, {
				"mData" : "isActive",
				"orderable" : false,
				"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
					statusCell(nTd, oData['isActive']);
				}
			}, {
				"mData" : "uuid",
				"orderable" : false,
				"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
					loadAddLink(nTd, sData, oData['isActive']);
				}
			} ]
		});

		function refreshItem(tds, data) {
			renderCell(tds[0], data['username']);
			renderCell(tds[1], data['nickname']);
			renderCell(tds[2], data['createDate']);
			renderCell(tds[3], data['phoneNumber']);
			renderCell(tds[4], data['mark']);
			statusCell(tds[5], data['isActive']);
			loadAddLink(tds[6], data['uuid'], data['isActive']);
		}

		function renderCell(cell, value) {
			var c = $(cell);
			c.empty();
			c.append(value);
		}
		function statusCell(cell, isActive) {
			var _p = $(cell);
			_p.empty();
			var value = '';
			if (isActive == 0) {
				value = '<span style="color: #FF9933;">已禁用</span>';
			} else if (isActive == 1) {
				value = '<span style="color: green;">已启用</span>';
			}
			_p.append(value);
		}

		function loadAddLink(cell, uuid, isActive) {
			var parent = $(cell);
			parent.empty();
			var item = '';
			if (isActive == 0) {
				item = $('<a href="#">启用</a>');
			} else if (isActive == 1) {
				item = $('<a href="#">禁用</a>');
			}
			$(item).bind('click', function() {
				$.post('/admin/change/user/isaction', {
					uuid : uuid
				}, function(data) {
					console.log(data);
					if (data.status) {
						alert(data.status);
					} else {
						refreshItem(parent.parent().children(), data);
					}
				});

			});
			parent.append(item);
		}

	});
</script>