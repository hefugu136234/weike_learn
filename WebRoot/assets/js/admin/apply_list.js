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
	showActive([ 'apply_mgr_nav', 'holder_project' ]);

	// 加载table数据
	$('#apply_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/apply/list/data",
		"aoColumns" : [ {
			"mData" : "applyName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				renderUserCell(nTd, sData,oData['userUuid']);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "mobile",
			"orderable" : false
		}, {
			"mData" : "hospital",
			"orderable" : false
		}, {
			"mData" : "departments",
			"orderable" : false
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
	

});

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

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已审核</span>';
	} else if (isStatus == 2) {
		value = '<span style="color: blue;">已发货</span>'
	}
	_p.append(value);
}

function loadAddLink(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = $('<a href="javascript:">更改状态</a>');
	item.click(function(event){
		event.preventDefault();
		$('div.form-group').find(':radio').each(function(index,item){
			var itemval=$(item).val();
			itemval=parseInt(itemval);
			if(isStatus==itemval){
				$(item).prop('checked','checked');
			}else{
				$(item).prop('checked',false);
			}
		});
		$('#vip_button').unbind('click');
		$('#vip_button').click(function(){
			var recordStatus=$('input[type="radio"][name="status"]:checked').val();
			recordStatus=parseInt(recordStatus);
			if(!recordStatus){
				alert('请选中更改状态');
				return false;
			}
			$.post('/project/apply/status/update', {
				'uuid' : uuid,
				'recordStatus':recordStatus
			}, function(data) {
				if (data.status == 'success') {
					refreshItem(parent.parent().children(), data);
					$('#dataModal_vip').modal('hide');
				} else {
					alert(data.message);
				}
			});
		});
		$('#dataModal_vip').modal('show');
	});
	parent.append(item);

	
//	else{
//		item=$('<a href="#">查看</a>');
//		$(item).bind('click', function() {
//			$.get('/project/apply/invite/code/'+uuid, function(data) {
//				if (data.status) {
//					alert(data.status);
//				} else {
//					alert(data.invitcode);
//				}
//			});
//
//		});
//		parent.append(item);
//	}
}



function refreshItem(tds, data) {
	renderUserCell(tds[0], data['applyName'],data['userUuid']);
	renderCell(tds[1], data['createDate']);
	renderCell(tds[2], data['mobile']);
	renderCell(tds[3], data['hospital']);
	renderCell(tds[4], data['departments']);
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
