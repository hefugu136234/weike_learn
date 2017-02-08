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
	showActive([ 'active_mgr_nav', 'holder_project' ]);

	// 加载table数据
	$('#active_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/group/active/list/data",
		"aoColumns" : [ {
			"mData" : "cardNum",
			"orderable" : false
		}, {
			"mData" : "createDate",
			"orderable" : false
		},{
			"mData" : "manufacturer",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 45);
			}
		}, {
			"mData" : "group",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 35);
			}
		},{
			"mData" : "deadline",
			"orderable" : false
		},
		{
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				opCell_(nTd, sData,oData['isStatus'],oData['userUuid']);
			}
		},{
			"mData" : "modifyDate",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				opCell(nTd, sData,oData['isStatus']);
			}
		},{
			"mData" : "isStatus",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}
		, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				loadAddLink(nTd, sData, oData['isStatus']);
			}
		} 
		]
	});

});

function opCell_(cell,val,isStatus,userUuid){
	var _p = $(cell);
	_p.empty();
	if (isStatus == 0) {
		
	} else if (isStatus == 1) {
		var item = $('<a href="#">' + val + '</a>')
		item.click(function(e){
			e.preventDefault();
			userInfo(userUuid)
		})
		_p.append(item);
	} 
}

function opCell(cell,val,isStatus){
	var _p = $(cell);
	_p.empty();
	if (isStatus == 0) {
		
	} else if (isStatus == 1) {
		_p.append(val);
	} 
}

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 0) {
		value = '<span style="color: #FF9933;">未激活</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已激活</span>';
	} 
	_p.append(value);
}

function loadAddLink(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = $('<a href="javascript:void(0);">查看</a>');
	$(item).click(function(e) {
		e.preventDefault();
		$.get('/project/group/code/view', {
			uuid : uuid
		}, function(data) {
			if (data.status=='success') {
				alert(data.message);
			} else {
				alert(data.message);
			}
		});

	});
	parent.append(item);
	
	//禁用
	if (isStatus == 0) {
		var del = $('<a style="margin-left:10px" href="javascript:void(0);">禁用</a>');
		del.click(function(event){
			event.preventDefault();
			 if(confirm("禁用后，将不在可用，确认禁用吗？")){
			$.post('/project/group/code/disable', {
				uuid : uuid
			}, function(data) {
				if (data.status=='success') {
					parent.parent().remove();
				} else {
					alert(data.message);
				}
			});
			 }
		});
		parent.append(del);
	} 
}


function refreshItem(tds, data) {
	renderCell(tds[0], data['name']);
	renderCell(tds[1], data['createDate']);
	renderCell(tds[2], data['serialNum']);
	cutOutData(tds[3], data['manufacturer'],60);
	statusCell(tds[4], data['isStatus']);
	loadAddLink(tds[5], data['uuid'], data['isStatus']);
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
		var bigUrl = "http://cloud.lankr.cn/api/image/" + taskId
				+ "?m/2/h/500/f/jpg";
		var url = "http://cloud.lankr.cn/api/image/" + taskId
				+ "?m/2/h/80/f/jpg";
		var img = '<a href=' + bigUrl + ' title="' + title
				+ '" data-gallery=""><img src=' + url + '/></a>';
		parent.append(img);
	}
}
