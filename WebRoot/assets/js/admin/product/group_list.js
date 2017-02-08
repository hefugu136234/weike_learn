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

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 0) {
		value = '<span style="color: #FF9933;">已下线</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已上线</span>';
	} 
	_p.append(value);
}

function loadAddLink(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = '';
 if (isStatus == 1) {
		item = $('<a href="javascript:void(0);">下线</a>');
	} else if (isStatus == 0) {
		item = $('<a href="javascript:void(0);">上线</a>')
	}
	$(item).click(function(e) {
		e.preventDefault();
		$.post('/project/group/product/update/status', {
			uuid : uuid
		}, function(data) {
			if (data.status=='success') {
				refreshItem(parent.parent().children(), data);
			} else {
				alert(data.message);
			}
		});

	});
	parent.append(item);
	// 编辑
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>');
	edit.click(function(event){
		event.preventDefault();
		location.href = "/project/group/product/update/page/" + uuid+"?pageUuid="+$('#pageUuid').val();
	});
	parent.append(edit);
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
