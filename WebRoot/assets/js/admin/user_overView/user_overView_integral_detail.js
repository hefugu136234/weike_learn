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

	var userUuid = $('#userUuid').val();

	$('#user_integral_detail_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/integralRecord/datatable/" + userUuid,
		"aoColumns" : [ {
			"mData" : "integralType",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operation(nTd, oData);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onMarkCellCreateOperation(nTd, oData);
			}
		}, {
			"mData" : "resourceName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onResourceNameCellCreateOperation(nTd, oData);
			}
		}, {
			"mData" : "formUser",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onFromUserCellCreateOperation(nTd, oData);
			}
		}, {
			"mData" : "value",
			"orderable" : false
		}, {
			"mData" : "consumeName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onConsumeNameCellCreateOperation(nTd, oData);
			}
		} ]
	});

	var regInput = /^-?[1-9]\d*$/;

	$('#changeIntegral').click(function() {
		$('#chageIntegralModal').modal('show');
		showValidCode();
	})

	var submitTag = false;
	$('#input_integral').blur(function() {
		var userInputIntegral = $('#input_integral').val();
		var errorInfoSpan = $('#error_info');
		if (0 == userInputIntegral || !regInput.test(userInputIntegral)) {
			errorInfoSpan.empty();
			errorInfoSpan.text('请输入正确的积分');
		} else {
			submitTag = true;
			errorInfoSpan.empty();
		}
	})

	$('#changIntegralCommit').click(function() {
		var userInputIntegral = $('#input_integral').val();
		var errorInfoSpan = $('#error_info');
		if (0 == userInputIntegral || !regInput.test(userInputIntegral)) {
			errorInfoSpan.empty();
			errorInfoSpan.text('请输入正确的积分');
			submitTag = false;
		}
		if (submitTag) {
			$.post("/admin/user/integral/grant", {
				uuid : $('#userUuid').val(),
				value : $('#input_integral').val(),
				type : $('#type').val(),
				mark: $('#mark').val(),
				code: $('#validateCode').val()
			}, function(data) {
				if ('success' == data.status) {
					location.reload();
				} else {
					alert(data.message);
					showValidCode();
				}
			})
		}
	})
	
	function showValidCode(){
		$('#valid_controller').show();
		var img = $('#vcode');
		freshCode(img);
		img.unbind('click')
		img.click(function(){
			freshCode(img);
		})
	}

function freshCode(img){
	img.attr('src','/user/2/validate/code?timestamp' + new Date().getTime())
}
})

function operation(cell, data) {
	var _p = $(cell);
	_p.empty();
	if (0 == data['action']) {
		_p.append('<font color="blue">' + data['mark'] + '</a>');
		return;
	}
	if (1 == data['integralType']) {
		// _p.append('<font color="blue">[消耗积分商品] :</font> ' + '<font
		// color="green">' + data['consumeName'] + '</font>');
		_p.append('<font color="green">虚拟物品</font> ');
	} else if (0 == data['integralType']) {
		var value = data['value']
		if(value >= 0)
		_p.append('<font color="blue">新增</a>');
		else{
			_p.append('<font color="red">扣除</a>');
		}
	} else if(2 == data['integralType']){
		_p.append('<font color="green">直播</font> ');
	} else if(3 == data['integralType']){
		_p.append('<font color="green">实体商品</font> ');
	} else {
		_p.append('<font color="red">TODO:</a>');
	}
}

function onMarkCellCreateOperation(nTd, oData) {
	var _p = $(nTd);
	_p.empty();
	if (0 == oData['action']) {
		_p.append('<font color="blue"> / </a>');
		return;
	}
	if (1 == oData['integralType']) {
		_p.append('兑换');
	} else {
		_p.append(oData['mark']);
	}
}

function onResourceNameCellCreateOperation(nTd, oData) {
	var _p = $(nTd);
	_p.empty();
	if ('' == oData['resourceName']) {
		_p.append('无');
	} else {
		if ('' == oData['resourceSpeaker']) {
			_p.attr("title",'名称：' + oData['resourceName']);
			_p.append('名称：' + oData['resourceName'])
		} else {
			_p.attr('名称：' + oData['resourceName'] + ' -- 讲者：'
					+ oData['resourceSpeaker']);
			_p.append('名称：' + oData['resourceName'] + ' -- 讲者：'
					+ oData['resourceSpeaker'])
		}
	}
}

function onFromUserCellCreateOperation(nTd, oData) {
	var _p = $(nTd);
	_p.empty();
	if ('' == oData['formUser']) {
		_p.append('无');
	} else {
		_p.append(oData['formUser'])
	}
}

function onConsumeNameCellCreateOperation(nTd, oData) {
	var _p = $(nTd);
	_p.empty();
	if ('' == oData['consumeName']) {
		_p.append('无');
	} else {
		_p.append('名称：' + oData['consumeName'])
	}
}
