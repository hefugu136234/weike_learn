$(function(){
	showActive([ 'questionnaire_list_nav', 'questionnaire_mgr' ]);

	// 加载table数据
	$('#questionnaire_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/questionnaire/list/data",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 60);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		},{
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
	if (isStatus == 2) {
		value = '<span style="color: #FF9933;">已下线</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已上线</span>';
	} else if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>'
	}
	_p.append(value);
}

function loadAddLink(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	if (isStatus == 2) {
		item = $('<a href="javascript:void(0);">上线</a>');
	} else if (isStatus == 1) {
		item = $('<a href="javascript:void(0);">下线</a>');
	} else if (isStatus == 0) {
		item = $('<a href="javascript:void(0);">审核</a>');
	}
	item.click(function(event) {
		event.preventDefault();
		$.post('/project/questionnaire/update/status', {
			uuid : uuid
		}, function(data) {
			if (data.status == 'success') {
				refreshItem(parent.parent().children(), data);
			} else {
				if (!!data.message) {
					alert(data.message);
				} else {
					alert('状态修改失败');
				}
			}
		});

	});
	parent.append(item);

	// 编辑
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>');
	edit.click(function(event) {
		event.preventDefault();
		location.href = "/project/questionnaire/update/page/" + uuid;
	});
	parent.append(edit);
	
	//查看答题链接
	if(isStatus!=0){
		var api_url='/project/questionnaire/answer/list/page/'+uuid;
		var answer_link = $('<a style="margin-left:10px" href="'+api_url+'">查看答题</a>');
		parent.append(answer_link);
	}

}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'], 60);
	renderCell(tds[1], data['createDate']);
	statusCell(tds[2], data['isStatus']);
	loadAddLink(tds[3], data['uuid'], data['isStatus']);
}
function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

//js截取字符串

function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}
