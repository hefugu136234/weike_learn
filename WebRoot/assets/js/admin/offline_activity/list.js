$(function() {
	showActive([ 'offline_activity_list', 'activity-mgr' ]);

	// 加载table数据
	var $activity_list_table = $('#activity_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/offline/list/data",
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
			"mData" : "enrollType",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				buildenrollType(nTd, sData);
			}
		}, {
			"mData" : "limitNum",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				limitNum(nTd, sData);
			}
		}, {
			"mData" : "address",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
			}
		}, {
			"mData" : "price",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				buildPrice(nTd, sData);
			}
		}, {
			"mData" : "initiatorName",
			"orderable" : false
		}, {
			"mData" : "_status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				loadAddLink(nTd, sData, oData['_status']);
			}
		} ]
	});

	$bind_user_modal = $('#bind_user_modal');
	$activity_name = $('#activity_name');
	$unbind_user = $('#unbind_user');
	$bind_confirm = $('#bind_confirm');
	$select_div = $('#select_div');

	$bind_user_modal.on('hide.bs.modal', function() {
		$(this).data('uuid','');
		$activity_name.html('');
		$select_div.empty();
	});
	
	$unbind_user.click(function(e){
		var uuid=$bind_user_modal.data('uuid');
		$.post('/admin/offline/bind/initiator',{
			uuid:uuid,
			userUuid:'none'
		},function(data){
			if(data.status=='success'){
				$activity_list_table.dataTable().fnDraw(false);
				$bind_user_modal.modal('hide');
			}else{
				alert(data.message);
			}
		});
	});
	
	$bind_confirm.click(function(e){
		var uuid=$bind_user_modal.data('uuid');
		var user_select=$('#user_select').val();
		if(!user_select){
			alert('请选中要绑定的发起人');
			return false;
		}
		$.post('/admin/offline/bind/initiator',{
			uuid:uuid,
			userUuid:user_select
		},function(data){
			if(data.status=='success'){
				$activity_list_table.dataTable().fnDraw(false);
				$bind_user_modal.modal('hide');
			}else{
				alert(data.message);
			}
		});
	});

});

var $bind_user_modal, $activity_name, $unbind_user, $bind_confirm, $select_div;

function buildOption(addition) {
	var option = '<option value="' + addition.id + '" selected="selected">'
			+ addition.text + '</option>';
	return option;
}

function buildSelect(list) {
	var $user_select = $('#user_select');
	if (!!list && list.length > 0) {
		$.each(list, function(index, item) {
			var option = buildOption(item);
			$user_select.append(option);
		});
	}
}

function showModalIng(data) {
	$activity_name.html(data.q);
	buildSelect(data.items);
	var $user_select = $('#user_select');
	$user_select.ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/admin/user/search'
	}, {
		loadingImg : '/assets/img/loading.gif'
	});
	$('#user_select_chosen').css('width', '100%');
	$bind_user_modal.modal('show');
}

function showModalBefore(uuid) {
	// 添加select
	var select_val = '<select class="form-control" id="user_select">'
			+ '<option value="">请输入"用户姓名"或"手机"检索用户</option>' + '</select>';
	$select_div.append(select_val);
	$.getJSON('/admin/offline/bind/user/info', {
		uuid : uuid
	}, function(data) {
		if (data.status == 'success') {
			$bind_user_modal.data('uuid', uuid);
			showModalIng(data);
		} else {
			alert(data.message);
		}
	});
}

function loadAddLink(cell, uuid, status) {
	var $cell = $(cell);
	$cell.empty();
	var verify_val;
	if (status == 2) {
		verify_val = '上线';
	} else if (status == 1) {
		verify_val = '下线';
	} else if (status == 0) {
		verify_val = '审核';
	}
	verify_val = '<a href="javascript:void(0);">' + verify_val + '</a>';

	var $verify = $(verify_val);
	$verify.click(function(e) {
		e.preventDefault();
		$.post('/admin/offline/update/status', {
			uuid : uuid
		}, function(data) {
			if (data.status == 'success') {
				refreshItem($cell.parent().children(), data);
			} else {
				if (!!data.message) {
					alert(data.message);
				} else {
					alert('状态修改失败');
				}
			}
		});

	});
	$cell.append($verify);

	// 编辑
	var $edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>');
	$edit.click(function(event) {
		event.preventDefault();
		location.href = "/admin/offline/update/page/" + uuid;
	});
	$cell.append($edit);

	// 报名人员
	var $book_user = $('<a style="margin-left:10px" href="javascript:void(0);">报名人员</a>');
	$book_user.click(function(event) {
		event.preventDefault();
		location.href = "/admin/offline/book/user/list/page/" + uuid;
	});
	$cell.append($book_user);

	$cell.append('</br>');

	// 邀请码
	var $invite_code = $('<a href="javascript:void(0);">邀请码</a>');
	$invite_code.click(function(event) {
		event.preventDefault();
		location.href = "/admin/offline/type/code/list/page/" + uuid
				+ "?type=1";
	});
	$cell.append($invite_code);

	// 兑换码
	var $exchang_code = $('<a style="margin-left:10px" href="javascript:void(0);">兑换码</a>');
	$exchang_code.click(function(event) {
		event.preventDefault();
		location.href = "/admin/offline/type/code/list/page/" + uuid
				+ "?type=2";
	});
	$cell.append($exchang_code);

	// 发起人
	var $bind_user = $('<a style="margin-left:10px" href="javascript:void(0);">发起人</a>');
	$bind_user.click(function(event) {
		event.preventDefault();
		showModalBefore(uuid);
	});
	$cell.append($bind_user);

}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'], 50);
	renderCell(tds[1], data['createDate']);
	buildenrollType(tds[2], data['enrollType']);
	limitNum(tds[3], data['limitNum']);
	cutOutData(tds[4], data['address']);
	buildPrice(tds[5], data['price']);
	renderCell(tds[6], data['initiatorName']);
	statusCell(tds[7], data['_status']);
	loadAddLink(tds[8], data['uuid'], data['_status']);
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

function buildenrollType(cell, enrollType) {
	var c = $(cell);
	c.empty();
	if (enrollType == 0) {
		c.append('开放');
	} else {
		c.append('审核');
	}
}

function buildPrice(cell, price) {
	var c = $(cell);
	c.empty();
	var val = '免费';
	if (!!price) {
		var json = JSON.parse(price);
		if (!!json.price && json.price > 0) {
			val = '价格：' + json.price + '￥';
		}
		if (!!json.integral && json.integral > 0) {
			if (val != '免费') {
				val = val + '/积分：' + json.integral;
			} else {
				val = '积分：' + json.integral;
			}
		}
	}
	c.append(val);
}

// js截取字符串

function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
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