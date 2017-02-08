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
	showActive([ 'qrcode_mgr_nav', 'assets-mgr' ]);

	// 加载table数据
	$('#qrcode_list_user_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/qrcode/user/list/data/"+$('#uuid').val(),
		"aoColumns" : [ {
			"mData" : "username",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 30);
			}
		}, {
			"mData" : "phone",
			"orderable" : false
		},{
			"mData" : "realName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 30);
			}
		},{
			"mData" : "wxnickname",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 30);
			}
		},{
			"mData" : "scanCount",
			"orderable" : false
		}, {
			"mData" : "viewCount",
			"orderable" : false
		}
//		, {
//			"mData" : "uuid",
//			"orderable" : false,
//			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
//				loadAddLink(nTd, sData, oData['isStatus']);
//			}
//		}
		]
	});

});

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 2) {
		value = '<span style="color: #FF9933;">未通过</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已通过</span>';
	} else if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>'
	}
	_p.append(value);
}

function loadAddLink(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	if(isStatus == 0){
		//通过，拒绝
		item = $('<a href="javascript:void(0);">通过</a>');
		item.click(function(event) {
			event.preventDefault();
			$.post('/project/broadcast/user/update/status', {
				uuid : uuid,
				status:1
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
		var refuse = $('<a style="margin-left:10px" href="javascript:void(0);">拒绝</a>');
		refuse.click(function(event) {
			event.preventDefault();
			$('#refuse_case').val('');
			$('#resufe_confirm_btn').unbind('click');
			$('#resufe_confirm_btn').click(function(e){
				e.preventDefault();
				refuse_case=$('#refuse_case').val();
				if(refuse_case==''){
					alert('请填写拒绝的理由');
					return false;
				}
				$.post('/project/broadcast/user/update/status', {
					uuid : uuid,
					status:2,
					mark:refuse_case
				}, function(data) {
					if (data.status == 'success') {
						$('#dataModal_refuse').modal('hide');
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
			$('#dataModal_refuse').modal('show');
		});
		parent.append(refuse);
	}else{
		//撤销
		var cancel = $('<a style="margin-left:10px" href="javascript:void(0);">撤销</a>');
		cancel.click(function(event) {
			event.preventDefault();
			$.post('/project/broadcast/user/update/status', {
				uuid : uuid,
				status:0
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
		parent.append(cancel);
	}

}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['username'], 30);
	renderCell(tds[1], data['bookDate']);
	renderCell(tds[2], data['modifyDate']);
	renderCell(tds[3], data['phone']);
	cutOutData(tds[4], data['realName'], 30);
	cutOutData(tds[5], data['wxnickname'], 30);
	renderCell(tds[6], data['userType']);
	statusCell(tds[7], data['isStatus']);
	loadAddLink(tds[8], data['uuid'], data['isStatus']);
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

