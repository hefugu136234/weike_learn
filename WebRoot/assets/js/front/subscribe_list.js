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
	$("#subscribe_bar").addClass("active");
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
		"sAjaxSource" : "/f/subscribe/list/data",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false
		}, {
			"mData" : "effectDate",
			"orderable" : false
		}, {
			"mData" : "mobile",
			"orderable" : false
		}, {
			"mData" : "subscribeType",
			"orderable" : false
		}, {
			"mData" : "isStatus",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				loadAddLink(nTd, sData, oData['isStatus']);
			}
		} ]
	});

});

function statusCell(cell, isActive) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isActive == 0) {
		value = '<span style="color: #FF9933;">未通过</span>';
	} else if (isActive == 1) {
		value = '<span style="color: green;">已通过</span>';
	}
	_p.append(value);
}

function loadAddLink(cell, uuid, isActive) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	if (isActive == 0) {
		item = $('<a href="#" style="color: #428bca;">通过</a>');
	} else if (isActive == 1) {
		item = $('<a href="#" style="color: #428bca;">不通过</a>');
	}
	$(item).bind('click', function() {
		$.post('/f/subscribe/status/change', {
			uuid : uuid
		}, function(data) {
			//console.log(data);
			if (data.status) {
				alert(data.status);
			} else {
				console.log(parent)
				refreshItem(parent.parent().children(), data);
			}
		});

	});
	parent.append(item);
}

function refreshItem(tds, data) {
	renderCell(tds[0], data['name']);
	renderCell(tds[1], data['effectDate']);
	renderCell(tds[2], data['mobile']);
	renderCell(tds[3], data['subscribeType']);
	statusCell(tds[4], data['isStatus']);
	loadAddLink(tds[5], data['uuid'], data['isStatus']);
}
function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}
