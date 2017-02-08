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
	showActive([ 'threescreen_mgr_nav', 'assets-mgr' ]);

	// 加载table数据
	$('#three_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/threescreen/list/data",
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
			"mData" : "categoryName",
			"orderable" : false
		}, {
			"mData" : "cover",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//test
				console.log(sData);
				imageShow(nTd, sData, '封面');
			}
		}, {
			"mData" : "speaker",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				buildSpeaker(nTd, sData);
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
	$(item).click(function(event) {
		event.preventDefault();
		$.post('/project/threescreen/update/status', {
			uuid : uuid
		}, function(data) {
			if (data.status=='success') {
				refreshItem(parent.parent().children(), data);
			} else {
				if(!!data.message){
					alert(data.message);
				}else{
					alert('状态修改失败');
				}
			}
		});

	});
	parent.append(item);
	// 编辑
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>');
	edit.click(function(event){
		event.preventDefault();
		location.href = "/project/threescreen/update/page/" + uuid;
	});
	parent.append(edit);
	
	//三分屏对应关系
	if(isStatus != 0){
		var sanfen=$('<a style="margin-left:10px" href="javascript:void(0);">对应关系</a>');
		sanfen.click(function(event){
			event.preventDefault();
			location.href = "/project/threescreen/congruent/page/" + uuid;
		});
		parent.append(sanfen);
	}

}


function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'],50);
	renderCell(tds[1], data['createDate']);
	renderCell(tds[2], data['categoryName']);
	imageShow(tds[3], data['cover'], '封面');
	buildSpeaker(tds[4], data['speaker']);
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

function buildSpeaker(cell, val) {
	var c = $(cell);
	c.empty();
	if(!!val){
		c.append(val);
	}else{
		c.append('<span style="color: #FF9933;">未指定</span>');		//modified by mayuan
	}
}

/**
 * 添加图片显示
 */
var patt = new RegExp("^(http).*");
function imageShow(cell, taskId, title) {
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var img = '<a href=' + taskId + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src="'
			+ taskId + '"/></a>';
		parent.append(img);
	}
	
	/*var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		if (!patt.test(taskId)) {
			var bigUrl = "http://cloud.lankr.cn/api/image/" + taskId
			+ "?m/2/h/500/f/jpg";
	    var url = "http://cloud.lankr.cn/api/image/" + taskId
			+ "?m/2/h/200/f/jpg";
	    var img = '<a href=' + bigUrl + ' title="' + title
			+ '" data-gallery=""><img width=100 src=' + url + '/></a>';
	       parent.append(img);
		}else{
			var cover=taskId;
			var bigUrl = cover;
	        var url = cover;
	    var img = '<a href=' + bigUrl + ' title="' + title
			+ '" data-gallery=""><img width=100 src=' + url + '/></a>';
	       parent.append(img);
		}
	}*/
}
