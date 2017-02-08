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
	showActive([ 'news_list_nav', 'assets-mgr' ]);

	// 加载table数据
	$('#news_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/news/list/data",
		"aoColumns" : [ {
			"mData" : "title",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 40);
			}
		}, {
			"mData" : "categoryName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 40);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "label",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 40);
			}
		},{
			"mData" : "qrTaskId",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				imageShow(nTd, sData, '二维码');
			}
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

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
//	if (isStatus == 0) {
//		value = '<span style="color: #FF9933;">已下线</span>';
//	} else if (isStatus == 1) {
//		value = '<span style="color: green;">已上线</span>';
//	}
	//20160122 XiaoMa
	if (isStatus == 2) {
		value = '<span style="color: #FF9933;">已下线</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已上线</span>';
	}else if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>'
	}
	_p.append(value);
}

function loadAddLink(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	//if (isStatus == 0) {
	if (isStatus == 2) {//20160122 XiaoMa
		item = $('<a href="javascript:void(0);">上线</a>');
	} else if (isStatus == 1) {
		item = $('<a href="javascript:void(0);">下线</a>');
	} else if (isStatus == 0) {
		item = $('<a href="javascript:void(0);">审核</a>');
	}
	$(item).bind('click', function() {
		$.post('/project/news/change/status', {
			uuid : uuid
		}, function(data) {
			// console.log(data);
			if (data.status) {
				alert(data.status);
			} else {
				refreshItem(parent.parent().children(), data);
			}
		});

	});
	parent.append(item);
	// 编辑
	var edit = $('<a style="margin-left:10px" href="#">编辑</a>');
	edit.bind('click', editor(uuid));
	parent.append(edit);
}

// 编辑事件
function editor(uuid) {
	// 跳转修改页面
	return function() {
		location.href = "/project/news/update/page/" + uuid;
	}
}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['title'], 40);
	cutOutData(tds[1], data['categoryName'], 40);
	renderCell(tds[2], data['createDate']);
	cutOutData(tds[3], data['label'], 40);
	imageShow(tds[4], data['qrTaskId'], '二维码');
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

/**
 * 添加图片显示
 */
function imageShow(cell, taskId, title) {
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var img = '<a href=' + taskId + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src="'
			+ taskId + '"/></a>';
		parent.append(img);
	}
	
	/*
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var bigUrl = "http://cloud.lankr.cn/api/image/" + taskId
				+ "?m/2/h/500/f/jpg";
		var url = "http://cloud.lankr.cn/api/image/" + taskId
				+ "?m/2/h/80/f/jpg";
		var img = '<a href=' + bigUrl + ' title="' + title
				+ '" data-gallery=""><img src=' + url + '/></a>';
		parent.append(img);
	}*/
}
