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

var datatable_has_reg;
var datatable_no_reg;
$(document).ready(function() {
	showActive([ 'pro_user_list_nav', 'holder_project' ]);
	datatable_has_reg = $('#project_hasreg_user_list_table');
	datatable_no_reg = $('#project_noreg_user_list_table');
	
	// 已注册用户列表
	datatable_has_reg.dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/userReference/user/datatable",
		"aoColumns" : [ {
			"mData" : "username",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				renderUserCell(nTd, sData,oData['uuid']);
			}
		}, {
			"mData" : "realName",
			"orderable" : false
		},{
			"mData" : "wxNickName",
			"orderable" : false
		}, {
			"mData" : "userType",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				createCell(nTd,sData);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "phoneNumber",
			"orderable" : false
		}, {
			"mData" : "validDate",
			"orderable" : false
		}, {
			"mData" : "roleName",
			"orderable" : false
		}, {
			"mData" : "isActive",
			"orderable" : false
		},{
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				hasRegOperation(nTd,sData,oData);
			}
		} ]
	});
	
	// 未注册用户列表
	datatable_no_reg.dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/userReference/user/noreg/datatable",
		"aoColumns" : [ {
			"mData" : "wxNickName",
			"orderable" : false
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "photo",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				imageShow(nTd, sData, '头像');
			}
		}, {
			"mData" : "openId",
			"orderable" : false
		}, {
			"mData" : "unionId",
			"orderable" : false
		}, {
			"mData" : "isActive",
			"orderable" : false
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				noRegOperation(nTd,sData,oData);
			}
		} ]
	});
	
	/* $('a[href="#list_hasReg"]').on('shown.bs.tab', function (e){
		datatable_has_reg.fnReloadAjax("/project/userReference/user/datatable");
	});
	
	$('a[href="#list_noReg"]').on('shown.bs.tab', function (e){
		datatable_no_reg.fnReloadAjax("/project/users");
	}); */
})

var table=$('');
var modelBoby=$('#userCardTable');

function createCell(nTd, sData){
	var parent = $(nTd);
	parent.empty();
	if('0' == sData){
		parent.append('<font color="green">医生用户</font>');
	}else if('1' == sData){
		parent.append("企业用户");
	}else{
		parent.append('<font color="red">未知</font>');
	}
}

function renderUserCell(cell,username,uuid){
	var c = $(cell);
	c.empty();
	var item = $('<a href="#">' + username + '</a>')
	item.click(function(e){
		e.preventDefault();
		userInfo(uuid)
	})
	c.append(item);
}


function hasRegOperation(cell,uuid,oData){
	var parent = $(cell);
	var stats=$(parent.parent().children()[8]);
	parent.empty();
	stats.empty();
	var item = '';
	var statItem='';
	//modified by mayuan点击查看备注,和卡记录
	var mark = $('<a href="#">备注</a>');
	var cardLog = $('<a href="#" data-toggle="modal" data-target="#userCardListModal">卡记录</a>');
	//var userIntegralDetail = $('<a href="/admin/user/integralDetail" target="view_window">积分明细</a>');
	var userIntegralDetail = $('<a href="/admin/user/integralDetail/'+ uuid +'" >积分明细</a>');
	//target="_blank"
	
	var user_isActive=oData['isActive'];
	if (user_isActive == 1) {
		item = $('<a href="#">禁用</a>');
		statItem='<span style="color: green;">已启用</span>';
	} else{
		item = $('<a href="#">启用</a>');
		statItem = '<span style="color: #FF9933;">已禁用</span>'
	} 
	item.click(function() {
		$.post('/project/manage/isAction/user', {
			uuid : uuid
		}, function() {
		}).done(function() {

		}).fail(function() {

		}).always(function(data) {
			//				0---禁用   1---启用
			if (data.status==1||data.status==0) {
				hasRegRefreshItem(parent.parent().children(), data, uuid, oData);
			}else{
				alert("permission denied");
			}
		});
	});
	
	//modified by mayuan添加备注按钮点击事件
	mark.click(function(){
		var mark = oData['mark'];
		if(mark){
			alert(mark);
		}else{
			alert("暂无相关备注！");
		}
	});
	
	//modified by mayuan -->添加卡记录点击事件
	cardLog.click(function(){
		$.get('/userCard/list/page/data', {
			uuid : uuid
		}, function(data) {
			var result_data = data.aaData;
			console.log(result_data);
			buildData(result_data);
		})
	})
	modelBoby.empty();
	table.empty();
	
	$('#select_confirm_btn_search').click(function(){
		$('#userCardListModal').modal('hide');
	})
	
	stats.append(statItem);
	parent.append(item).append(' | ').append(mark).append(' | ').append(cardLog).append(' | ').append(userIntegralDetail).append(' | ');
	
	//编辑
	var edit = $('<a href="#" >编辑</a>')
	edit.click(function(){
		if('N' == $('#isSuperAdmin')){
			alert('您没有权限操作，请联系管理员');
		}else{
			window.location.href='/project/userReference/user/updatePage/' + uuid ;
		}
	})
	
	//target="_blank"
	parent.append(edit);
}

function noRegOperation(cell,uuid,oData){
	var parent = $(cell);
	var stats=$(parent.parent().children()[5]);
	parent.empty();
	stats.empty();
	var item = '';
	var statItem='';
	
	var user_isActive=oData['isActive'];
	if (user_isActive == 1) {
		item = $('<a href="#">禁用</a>');
		statItem='<span style="color: green;">已启用</span>';
	} else{
		item = $('<a href="#">启用</a>');
		statItem = '<span style="color: #FF9933;">已禁用</span>'
	} 
	item.click(function() {
		$.post('/project/manage/isAction/wechatuser', {
			uuid : uuid
		}, function() {
		}).done(function() {
		}).fail(function() {
		}).always(function(data) {
			if (data.status==1||data.status==0) {
				noRegRefreshItem(parent.parent().children(), data, uuid, oData);
			}else{
				alert("permission denied");
			}
		});
	});
	
	stats.append(statItem);
	parent.append(item);
}

// 已注册用户列表刷新
function hasRegRefreshItem(tds, data, uuid, oData) {
	//0---禁用   1---启用
	var parent = $(tds[9]);
	parent.empty();
	var stats=$(tds[8]);
	stats.empty();
	var item = '';
	var statItem='';
	var dataId=data.status;
	
	renderUserCell(tds[0],oData['username'],uuid)
	
	var mark = $('<a href="#">备注</a>');
	var cardLog = $('<a href="#" data-toggle="modal" data-target="#userCardListModal">卡记录</a>');
	var userIntegralDetail = $('<a href="/admin/user/integralDetail/'+ uuid +'" target="_blank">积分明细</a>');
	
	if (dataId == 1) {
		item = $('<a href="#">启用</a>');
		statItem = '<span style="color: #FF9933;">已禁用</span>'
	} else{ 
		item = $('<a href="#">禁用</a>');
		statItem='<span style="color: green;">已启用</span>';
	} 
	item.click(function() {
		$.post('/project/manage/isAction/user', {
			uuid : uuid
		}, function() {
		}).done(function() {

		}).fail(function() {

		}).always(function(data) {
			console.log(data);
			//				0---启用   1---禁用
			if (data.status==1||data.status==0) {
				hasRegRefreshItem(parent.parent().children(), data, uuid, oData);
			}else{
				alert("permission denied");
			}
		});
	});
	
	//modified by mayuan -->添加备注按钮点击事件
	mark.click(function(){
		var mark = oData['mark'];
		if(mark){
			alert(mark);
		}else{
			alert("暂无相关备注！");
		}
	});
	
	//modified by mayuan -->添加卡记录点击事件
	cardLog.click(function(){
		$.get('/userCard/list/page/data', {
			uuid : uuid
		}, function(data) {
			var result_data = data.aaData;
			console.log(result_data);
			buildData(result_data);
		})
	})
	modelBoby.empty();
	table.empty();
	
	$('#select_confirm_btn_search').click(function(){
		$('#userCardListModal').modal('hide');
	})
	
	stats.append(statItem);
	parent.append(item).append(' | ').append(mark).append(' | ').append(cardLog).append(' | ').append(userIntegralDetail).append(' | ');
	
	//编辑
	var edit = $('<a href="#" >编辑</a>')
	edit.click(function(){
		console.log($('#isSuperAdmin'))
		if('N' == $('#isSuperAdmin')){
			alert('您没有权限操作，请联系管理员');
			return false;
		}else{
			window.location.href='/project/userReference/user/updatePage/' + uuid ;
		}
	})
	parent.append(edit);
}

// 未注册用户列表刷新
function noRegRefreshItem(tds, data, uuid, oData) {
	var parent = $(tds[6]);
	parent.empty();
	var stats=$(tds[5]);
	stats.empty();
	var item = '';
	var statItem='';
	var dataId=data.status;
	
	if (dataId == 1) {
		item = $('<a href="#">启用</a>');
		statItem = '<span style="color: #FF9933;">已禁用</span>'
	} else{ 
		item = $('<a href="#">禁用</a>');
		statItem='<span style="color: green;">已启用</span>';
	} 
	item.click(function() {
		$.post('/project/manage/isAction/wechatuser', {
			uuid : uuid
		}, function() {
		}).done(function() {
		}).fail(function() {
		}).always(function(data) {
			console.log(data);
			if (data.status==1||data.status==0) {
				noRegRefreshItem(parent.parent().children(), data, uuid, oData);
			}else{
				alert("permission denied");
			}
		});
	});
	
	stats.append(statItem);
	parent.append(item);
}

//modified by mayuan --> 绘制html显示流量卡记录
function buildData(result_data){
	table=$('<table border=1 width=100%>'+
				'<tr>'+
					'<th width=25%>卡号</th>'+
					'<th width=25%>厂商</th>'+
					'<th width=25%>时长(天)</th>'+
					'<th width=25%>激活时间</th>'+
				'</tr>'+
			'</table>');
	if(result_data.length==0){
		table.empty();
		table=$('<table width=100%><tr><td width=100%><font color="red" size=4>该用户暂无流量卡信息</fong></td></tr></table>');
	}else{
		$.each(result_data,function(index,element){
			var deadLine='';
			if(!!element.duration){
				//TODO 秒转换成对应的月数
				deadLine=element.duration/(3600*24)
				console.log(deadLine);
			}
			var tr=$('<tr><td>'+element.cardNum+'</td><td>'+element.manufacturerName+'</td><td>'+deadLine+'</td><td>'+element.activationDate+'</td></tr>');	
			table.append(tr);
		})
	}
	modelBoby.empty();
	modelBoby.append(table);
}

function imageShow(cell, taskId, title) {
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var img = '<a href=' + taskId + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src="'
			+ taskId + '"/></a>';
		parent.append(img);
	}
}