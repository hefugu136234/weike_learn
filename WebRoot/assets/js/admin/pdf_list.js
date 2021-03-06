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
	showActive([ 'pdf_mrg_nav', 'assets-mgr' ]);

	// 加载table数据
	$('#pdf_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/pdf/list/data",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData);
			}
		}, {
			"mData" : "spaker",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				buildSpeaker(nTd, sData);
			}
		},{
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "categoryName",
			"orderable" : false
		}, {
			"mData" : "coverTaskId",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//test
				console.log(sData);
				imageShow(nTd, sData, '封面');
			}
		}, {
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
				loadAddLink(nTd, sData, oData['isStatus'], oData['taskId']);
			}
		} ]
	});

});

//标题，RefUrl, 备注等信息完整显示
function showDetail(td, data){
	var td = $(td);
	td.empty();
	if(data.length > 25){
		var tmpData = data.substring(0, 25) + " ......";
		td.append(tmpData);
	}else{
		td.append(data);
	}
	td.on("click", function(){
		$('#dataDetail_pdf').text(data);
		$('#dataModal_pdf').modal('show');
	})
}

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

function loadAddLink(cell, uuid, isStatus, taskId) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	if (isStatus == 2) {
		item = $('<a href="#">上线</a>');
	} else if (isStatus == 1) {
		item = $('<a href="#">下线</a>');
	} else if (isStatus == 0) {
		item = $('<a href="#">审核</a>');
	}
	$(item).bind('click', function() {
		$.post('/project/pdf/update/status', {
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
	// 查看pdf文件
	if (isStatus != 0) {
		var show = $('<a style="margin-left:10px" href="http://7xo6el.com2.z0.glb.qiniucdn.com/'
				+ taskId + '" target="_blank">预览</a>');
		// show.bind('click', showPdf(taskId));
		parent.append(show);
	}
}

// 编辑事件
function editor(uuid) {
	// 跳转修改页面
	return function() {
		location.href = "/project/pdf/update/page/" + uuid;
	}
}

// pdf预览
function showPdf(taskId) {
	// 跳转修改页面
	return function() {
		location.href = "http://7xo6el.com2.z0.glb.qiniucdn.com/" + taskId;
	}
}

function refreshItem(tds, data) {
	console.log(data)
	renderCell(tds[0], data['name']);
	buildSpeaker(tds[1],data['spaker']);
	renderCell(tds[2], data['createDate']);
	renderCell(tds[3], data['categoryName']);
	imageShow(tds[4], data['coverTaskId'], '封面');
	imageShow(tds[5], data['qrTaskId'], '二维码');
	statusCell(tds[6], data['isStatus']);
	loadAddLink(tds[7], data['uuid'], data['isStatus'], data['taskId']);
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
var patt = new RegExp("^(http).*");
function imageShow(cell, taskId, title) {
	/*var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var bigUrl, url
		if (patt.test(taskId)) {
			bigUrl = taskId;
			url = taskId;
		} else {
			bigUrl = "http://cloud.lankr.cn/api/image/" + taskId
					+ "?m/2/h/500/f/jpg";
			url = "http://cloud.lankr.cn/api/image/" + taskId
					+ "?m/2/h/80/f/jpg";
		}
		var img = '<a href=' + bigUrl + ' title="' + title
				+ '" data-gallery=""><img style="height:80px;" src='
				+ url + '/></a>';
		var img = '<a href=' + bigUrl + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src='
			+ url + '/></a>';
		parent.append(img);
	}*/
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var img = '<a href=' + taskId + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src="'
			+ taskId + '"/></a>';
		parent.append(img);
	}
}

//modified by mayuan -->添加讲者信息
function buildSpeaker(cell, val) {
	var c = $(cell);
	c.empty();
	if(!!val){
		c.append(val);
	}else{
		c.append('<span style="color: #FF9933;">未指定</span>');	
	}
}
