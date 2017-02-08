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
	activeStub('activity_oups_code_list-nav');

	showActive([ 'activity_oups_code_list-nav', 'activity-mgr' ]);
	$('#oups_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/oups/list/data",
		"aoColumns" : [ {
			"mData" : "codeNum",
			"orderable" : false
		}, {
			"mData" : "name",
			"orderable" : false
		}, {
			"mData" : "createDate",
			"orderable" : false,
		}, {
			"mData" : "applyUserName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				// console.log(oData)
				renderUserCell(nTd, sData, oData['userUuid']);
			}
		}, {
			"mData" : "applyCateName",
			"orderable" : false,
		}, {
			"mData" : "activityName",
			"orderable" : false,
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
				opear(nTd, sData, oData['isStatus'])
			}
		} ]
	});

	$('#dataModal_oups_view').on('hide.bs.modal', function() {
		$('#oups_code').html('');
		$('#oups_name').html('');
		$('#oups_user_name').html('');
		$('#oups_cat_name').html('');
		$('#oups_res_name').html('');
		$('#oups_res_cat').html('');
		$('#oups_mark').text('');
	});

})

function renderUserCell(cell, username, uuid) {
	var c = $(cell);
	c.empty();
	var item = $('<a href="javascript:">' + username + '</a>')
	item.click(function(e) {
		e.preventDefault();
		userInfo(uuid)
	})
	c.append(item);
}

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '<span style="">' + status_zh(isStatus) + '</span>'
	_p.append(value);
}

// function refreshItem(tds, data) {
// renderCell(tds[0], data['name']);
// renderCell(tds[1], data['createDate']);
// renderCell(tds[2], data['username']);
// renderCell(tds[3], data['categoryName']);
// renderCell(tds[4], data['members']);
// renderCell(tds[5], data['mark']);
// statusCell(tds[6], data['_status']);
// func(tds[7], data['uuid'], data['_status']);
// }

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function status_zh(status) {
	var ret = '未关联'
	if (status == 1) {
		ret = '已关联'
	} else if (status == 3) {
		ret = '收到作品'
	} else if (status == 4) {
		ret = '初审中'
	} else if (status == 5) {
		ret = '作品转码'
	} else if (status == 6) {
		ret = '专业审核'
	} else if (status == 8) {
		ret = '合格'
	} else if (status == 10) {
		ret = '不合格'
	}
	return ret;
}

function opear(cell, uuid, isStatus) {
	var _p = $(cell);
	_p.empty();
	var op = $('<a href="#">更新状态</a>');
	op.click(function(e) {
		e.preventDefault();
		$.get('/admin/oups/' + uuid + '/fixed/prepare').always(function(data) {
			if (data.status == 'success') {
				fixStatus(data)
				initInteraction(data)
			} else {
				if (data.message) {
					alert(data.message)
				} else {
					alert('读取作品信息失败')
				}
			}

		})
	})
	_p.append(op)

	// var view=$('<a href="javascript:">查看</a>');
	// view.click(function(e){
	// e.preventDefault();
	// $.getJSON('/admin/oups/view/detail/'+uuid,function(data){
	// if(data.status=='success'){
	// initTextOfMadal(data);
	// $('#dataModal_oups_view').modal('show');
	// }else{
	// alert(data.message);
	// }
	// });
	// });
	// _p.append(view);
	//	
	// if(isStatus==1){
	// var url='/admin/oups//view/relate/page/'+uuid;
	// var link=$('<a style="margin-left:10px" href="'+url+'">解除关联</a>');
	// _p.append(link);
	// }
}

function fixStatus(data) {
	$('#dataModal_oups_view').modal('show');
	opusInfoRender(data)
	opusStatusControl(data.isStatus, data.bundled);
	$('#status_controller').unbind('change');
	$('#status_controller').change(function(e) {
		var _this = $(this)
		changeStatus(data.uuid, _this.val())
	});
}

function initInteraction(data) {
	var i = $('#msg_send_btn');
	i.unbind('click')
	i.click(function(e) {
		var msg = $('#interaction_message').val();
		if(isBlank(msg)){
			alert('请输入消息')
		}else{
			sendInteraction(msg, data.uuid)
		}
	})
	//加载互动数据
	loadInteraction(data.uuid)
}

function opusInfoRender(data) {
	var container = $('#opus_info_container');
	container.empty();
	container.append('<span><b>申请编号：</b>' + data.codeNum + '</span><br />')
	container.append('<span><b>参与活动：</b>' + data.activityName
			+ '</span><br /> <span><b>申请用户：</b>' + data.applyUserName
			+ '</span><br />')
	container.append('<span><b>申请时间：</b>' + data.createDate + '</span><br />')
	container.append('<span><b>申请学科：</b>' + data.applyCateName
			+ '</span><br /><span><b>申请名称：</b>' + data.name + '</span><br />')
	container.append('<span><b>关联资源：</b>' + data.resName
			+ '</span><br /><span><b>申请名称：</b>' + data.name + '</span><br />')
	container.append('<span><b>申请备注：</b>' + data.oupsMark + '</span>')
}

function opusStatusControl(status, bundled) {
	$('#status_controller').val(status + '')
}

function changeStatus(uuid, target_status) {
	$.post('/admin/oups/' + uuid + '/fixed/process', {
		target_status : parseInt(target_status)
	}).always(function(data) {
		if (data.status == 'success') {
			alert('更改状态成功')
		} else {
			if (!isBlank(data.message)) {
				alert(data.message)
			}
			if (data.ret_status) {
				opusStatusControl(data.ret_status, data.bundled)
			}
		}
	})
}

function loadInteraction(opus_uuid) {
	$.get('/admin/oups/'+opus_uuid+'/interact/msgs').always(function(data){
		if(data.status == 'success'){
			renderInteraction(data.data)
		}else{
			if(!isBlank(data.message)){
				alert(data.message)
			}else{
				alert('发表失败')
			}
		}
	})
}

function sendInteraction(message, opus_uuid) {
	if(!confirm('确定发表该消息吗？')){
		return;
	}
	$.post('/admin/oups/'+opus_uuid+'/interact/msg/create',{
		message:message
	}).always(function(data){
		if(data.status == 'success'){
			$('#interaction_message').val('')
			loadInteraction(opus_uuid)
		}else{
			if(!isBlank(data.message)){
				alert(data.message)
			}else{
				alert('发表失败')
			}
		}
	})
	
}

function renderInteraction(data) {
	var container = $('#interaction_frame');
	container.empty();
	$.each(data,function(i,e){
		container.append(messgeItem(e))
	})
}

function messgeItem(message){
	var html = '';
	if(!message.userInfo.isSystemUser){
		html = '<div class="left"><div class="author-name"> '+ message.userInfo.nickname +
		'<small class="chat-date">'+message.createDate+'</small></div><div class="chat-message">'+message.body+'</div></div>'
	}else{
		html = '<div class="right"><div class="author-name"> '+ message.userInfo.nickname +
		'<small class="chat-date">'+message.createDate+'</small></div><div class="chat-message active">'+message.body+'</div></div>'
	}
	html = $(html)
	var del = $('<a class="msg-del" href="#">删除</a>');
	html.append(del);
	del.hide();
	html.hover(function(){
		del.show()
	},function(){del.hide()})
	del.click(function(e){
		e.preventDefault();
		if(confirm('确定删除这条记录？')){
			$.post('/admin/oups/interact/msg/'+message.uuid+'/del').always(function(data){
				if(data.status == 'success'){
					html.remove();
				}else{
					if(isBlank(data.message)){
						alert('删除失败')
					}else{
						alert(data.message)
					}
				}
			})
		}
	})
	return html;
}


function initTextOfMadal(data) {
	$('#oups_code').html(data.codeNum);
	$('#oups_name').html(data.name);
	$('#oups_user_name').html(data.applyUserName);
	$('#oups_cat_name').html(data.applyCateName);
	$('#oups_res_name').html(data.resName);
	$('#oups_res_cat').html(data.cateName);
	$('#oups_mark').text(data.oupsMark);
}
