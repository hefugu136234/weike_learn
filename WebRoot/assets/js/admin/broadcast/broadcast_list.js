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
	showActive([ 'live-list', 'live-mgr' ]);

	// 加载table数据
	$('#broadcast_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/broadcast/list/data",
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
			"mData" : "limitNum",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				limitNum(nTd, sData);
			}
		}, {
			"mData" : "castType",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				castType(nTd, sData);
			}
		}, {
			"mData" : "resourceName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
			}
		}, {
			"mData" : "platFormName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
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

	$('#record_modal').on('show.bs.modal', function() {
		//添加select
		var select_val='<select class="form-control" id="video_select">'
		                +'<option value="">请输入"资源名称"或者"讲者"检索资源</option>'
		                +'</select>';
		$('#select_div').append(select_val);
		var uuid = $(this).data('uuid');
		// 加载数据所做的操作
		$.getJSON('/project/broadcast/get/band/info', {
			'uuid' : uuid
		}, function(data) {
			if (data.status == 'success') {
				initModalData(data);
			} else {
				alert(data.message);
			}
		});
	});

	$('#record_modal').on('hide.bs.modal', function() {
		$(this).data('uuid', '');
		$('#broadcast_name').html('');
		$('#select_div').empty();
		$('#record_url').val('');
	});

	$('#broadcast_video').click(function(event) {
		event.preventDefault();
		// 点击事件
		var uuid = $('#record_modal').data('uuid');
		// 点击的请求
		var video_select=$('#video_select').val();
		var record_url='';//$('#record_url').val();
		if(video_select==''||video_select==0){
			alert('请配置录播的资源');
			return false;
		}
		$.post('/project/broadcast/bind/record',
				{'uuid':uuid,'resUuid':video_select,'resourceUrl':record_url},
				function(data){
			if(data.status=='success'){
				$('#record_modal').modal('hide');
				refreshItem(table_line.parent().children(), data);
			}else{
				alert(data.message);
			}
		});
	});


});

// $('select').ajaxChosen({
// dataType: 'json',
// type: 'POST',
// url:'/search',
// data: {'keyboard':'cat'}, //Or can be [{'name':'keyboard', 'value':'cat'}].
// chose your favorite, it handles both.
// success: function(data, textStatus, jqXHR){ doSomething(); }
// },{
// processItems: function(data){ return data.complex.results; },
// useAjax: function(e){ return someCheckboxIsChecked();
// },此处是否键入数值后，自己确定是否请求ajax，默认true；可以做搜索校验
// generateUrl: function(q){ return '/search_page/'+somethingDynamical(); },
// loadingImg: '../vendor/loading.gif',
// minLength: 2
// });
var table_line;

function initModalData(data) {
	//console.log(data);
	$('#broadcast_name').html(data.name);
	//$('#record_url').val(data.resourceUrl);
	var video_select = $('#video_select');
	buildSelect(data.optionAdditions);
	var select = data.selectItem;
	if (!!select) {
		video_select.val(select);
	} else {
		video_select.get(0).selectedIndex = 0;
	}
	video_select.ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/project/broadcast/search/record'
	}, {
		loadingImg : '/assets/img/loading.gif'
	});

	$('#video_select_chosen').css('width', '100%');
}

function buildSelect(list) {
	var video_select = $('#video_select');
	if (!!list && list.length > 0) {
		$.each(list, function(index, item) {
			var option = buildOption(item);
			video_select.append(option);
		});
	}
}

function buildOption(addition) {
	var option = '<option value="' + addition.id + '">' + addition.text
			+ '</option>';
	return option;
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
		$.post('/project/broadcast/update/status', {
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
		location.href = "/project/broadcast/update/page/" + uuid;
	});
	parent.append(edit);

	// 配置
	var config = $('<a style="margin-left:10px" href="javascript:void(0);">配置</a>');
	config.click(function(event) {
		event.preventDefault();
		location.href = "/project/broadcast/config/page/" + uuid;
	});
	parent.append(config);

	if (isStatus != 0) {
		// 查看知了直播链接
		var banner = $('<a style="margin-left:10px" href="javascript:void(0);">banner链接</a>');
		banner.click(function(event) {
			event.preventDefault();
			$('#wx_content_broadcast').val(
					'/api/webchat/broadcast/first/page/' + uuid);
			$('#web_content_broadcast').val(
					'/f/web/live/detail/' + uuid);
			$('#dataModal_broadcast').modal('show');
		});
		parent.append(banner);
	}

	// 二维码链接
	var qrcode = $('<a style="margin-left:10px" href="javascript:void(0);">二维码</a>');
	qrcode.click(function(event) {
		event.preventDefault();
		$.getJSON('/project/qrcode/type/res', {
			'uuid':uuid,
			'type':3 }, function(data) {
			if (data.status == 'success') {
				$('#qrcode_url').val(data.message);
				$('#qrcode_preview').attr('src', data.message);
				$('#dataModal_qrcode').modal('show');
			} else {
				alert(data.message);
			}
		});
	});
	parent.append(qrcode).append('</br>');

	if (isStatus != 0) {
		// 报名人员
		var book_person = $('<a style="margin-left:10px" href="javascript:void(0);">报名人员</a>');
		book_person.click(function(event) {
			event.preventDefault();
			location.href = "/project/broadcast/user/list/page/" + uuid;
		});
		parent.append(book_person);
		// 录播
		var video_recored = $('<a style="margin-left:10px" href="javascript:void(0);">录播</a>');
		video_recored.click(function(event) {
			event.preventDefault();
			table_line=parent;
			$('#record_modal').data('uuid', uuid);
			$('#record_modal').modal('show');
		});
		parent.append(video_recored);
	}

}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'], 50);
	renderCell(tds[1], data['createDate']);
	limitNum(tds[2], data['limitNum']);
	castType(tds[3], data['castType']);
	cutOutData(tds[4], data['resourceName'], 50);
	cutOutData(tds[5], data['platFormName'], 50);
	statusCell(tds[6], data['isStatus']);
	loadAddLink(tds[7], data['uuid'], data['isStatus']);
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

function limitNum(cell, val) {
	var c = $(cell);
	c.empty();
	if (val == 0) {
		c.append('无限制');
	} else {
		c.append(val);
	}

}

function castType(cell, val) {
	var c = $(cell);
	c.empty();
	if (val == 1) {
		c.append('开放');
	} else if (val == 2) {
		c.append('审核');
	} else if (val == 3) {
		c.append('pincode邀请');
	}
}
