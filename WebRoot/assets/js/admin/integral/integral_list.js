$(document).ready(function() {
	showActive([ 'goods-list', 'integral-mgr' ]);

	$('#goods_list').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/integral/goods/list/datatable",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "cover",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//test
				console.log(oData);
				
				imageShow(nTd, sData, '商品图片');
			}
		}, {
			"mData" : "integral",
			"orderable" : false
		}, {
			"mData" : "type",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				dealType(nTd, sData);
			}
		}, {
			"mData" : "status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operation(nTd, sData, oData['status']);
			}
		} ]
	});
})

// 当前审核状态
function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已上线</span>';
	} else if (isStatus == 2) {
		value = '<span style="color: red;">已下线</span>'
	}
	_p.append(value);
}

// 审核操作
function operation(cell, uuid, status) {
	var _p = $(cell);
	_p.empty();
	var pass;
	if (status == 1) {
		pass = $('<a href="javascript:">下线</a>');
	} else if (status == 2) {
		pass = $('<a href="javascript:">上线</a>');
	} else {
		pass = $('<a href="javascript:">审核</a>');
	}
	pass.click(function(e) {
		e.preventDefault();
		$.post('/project/integral/goods/status/change', {
			uuid : uuid
		}, function(data) {
			if (data.status == 'success') {
				refreshItem(_p.parent().children(), data.integralConsume);
			} else {
				alert('状态更改失败');
			}
		});
	})
	_p.append(pass);
	
	// 编辑
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>');
	edit.click(function(event) {
		event.preventDefault();
		location.href = "/admin/consume/update/page/" + uuid;
	});
	_p.append(edit);
}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'], 50);
	renderCell(tds[1], data['createDate']);
	imageShow(tds[2], data['cover'], '商品图片');
	renderCell(tds[3], data['integral']);
	dealType(tds[4], data['type']);
	statusCell(tds[5], data['status']);
	operation(tds[6], data['uuid'], data['status']);
}

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

// 图片预览
function imageShow(cell, taskId, title) {
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		
		var img = '<a href=' + taskId + ' title="' + title
				+ '" data-gallery=""><img style="width:50px;" src="' + taskId
				+ '"/></a>';
		parent.append(img);
	}
}

// js截取字符串

function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}

function dealType(cell, val) {
	var c = $(cell);
	c.empty();
	if (val == 1) {
		c.append('虚拟商品');
	}else if(val==3){
		c.append('实体商品');
	}
}
