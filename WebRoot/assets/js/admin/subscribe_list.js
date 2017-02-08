// Custom scripts
jQuery.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings) {
	return {
		"iStart" : oSettings._iDisplayStart,
		"iEnd" : oSettings.fnDisplayEnd(),
		"iLength" : oSettings._iDisplayLength,
		"iTotal" : oSettings.fnRecordsTotal(),
		"iFilteredTotal" : oSettings.fnRecordsDisplay(),
		"iPage" : oSettings._iDisplayLength === -1 ? 0 : Math
				.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
		"iTotalPages" : oSettings._iDisplayLength === -1 ? 0 : Math
				.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
	};
};
$(document).ready(function() {
	showActive([ 'subscribe_mgr_nav', 'holder_project' ]);

	// 加载table数据
	$('#subscribe_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/subscribe/list/data",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false
		}, {
			"mData" : "mobile",
			"orderable" : false
		}, {
			"mData" : "company",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
			}
		}, {
			"mData" : "group",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
			}
		}, {
			"mData" : "mail",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
			}
		}, {
			"mData" : "phone",
			"orderable" : false
		} ]
	});

});

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 1) {
		value = '<span style="color: green;">已审核</span>';
	} else if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>'
	}
	_p.append(value);
}

function loadAddLink(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	if (isStatus == 0) {
		item = $('<a href="#">审核</a>');
		$(item).bind('click', function() {
			$.post('/project/apply/status/update', {
				uuid : uuid
			}, function(data) {
				if (data.status) {
					alert(data.status);
				} else {
					refreshItem(parent.parent().children(), data);
					alert("邀请码生成");
				}
			});

		});
		parent.append(item);
	} else {
		item = $('<a href="#">查看</a>');
		$(item).bind('click', function() {
			$.get('/project/apply/invite/code/' + uuid, function(data) {
				if (data.status) {
					alert(data.status);
				} else {
					alert(data.invitcode);
				}
			});

		});
		parent.append(item);
	}
}

function refreshItem(tds, data) {
	renderCell(tds[0], data['name']);
	renderCell(tds[1], data['createDate']);
	renderCell(tds[2], data['mobile']);
	renderCell(tds[3], data['hospital']);
	renderCell(tds[4], data['departments']);
	statusCell(tds[5], data['isStatus']);
	loadAddLink(tds[6], data['uuid'], data['isStatus']);
}
function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

// js截取字符串

function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}
