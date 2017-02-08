$(document).ready(function() {
	showActive([ 'integral-mgr', 'exchange-list-nav' ]);
	
	$('#exchange_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/integral/exchange/datatable",
		"aoColumns" : [ {
			"mData" : "recordUserName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//test
				
				cutOutData_(nTd, sData,35, oData['userUuid']);
			}
		}, {
			"mData" : "recordDate",
			"orderable" : false
		}, {
			"mData" : "processDate",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				dealTime(nTd, sData,oData['status']);
			}
		}, {
			"mData" : "expend",
			"orderable" : false
		}, {
			"mData" : "consumeName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData,30);
			}
		}, {
			"mData" : "status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operation(nTd, oData)
			}
		} ]
	});
	
	$('#address_modal').on('hide.bs.modal', function() {
		$(this).data('uuid', '');
		$('#ex_name').val('');
		$('#ex_phone').val('');
		$('#ex_address').val('');
	});
	
	$('#address_but').click(function(event) {
		event.preventDefault();
		// 点击事件
		var uuid = $('#address_modal').data('uuid');
		// 点击的请求
		var name=$('#ex_name').val();
		var phone=$('#ex_phone').val();
		var address=$('#ex_address').val();
		if(name==''){
			alert('请填写收货人');
			return false;
		}
		if(phone==''){
			alert('请填写手机号');
			return false;
		}
		if(address==''){
			alert('请填写收货地址');
			return false;
		}
		$.post('/project/integral/exchange/update/address',
				{'uuid':uuid,
			'name':name,
			'phone':phone,
			'address':address},
				function(data){
			if(data.status=='success'){
				$('#address_modal').modal('hide');
			}else{
				alert(data.message);
			}
		});
	});
});

// js截取字符串

function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}

function cutOutData_(cell, val, num, userUuid) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	
	var item = $('<a href="#">' + val + '</a>')
	item.click(function(e){
		e.preventDefault();
		userInfo(userUuid)
	})
	c.append(item);
}





function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function dealTime(cell,time,status){
	var c = $(cell);
	c.empty();
	if(status==1){
		c.append(time);
	}
}

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 0) {
		value = '<span style="color: red;">未处理</span>'
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已处理</span>';
	}
	_p.append(value);
}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['recordUserName'],35);
	renderCell(tds[1], data['recordDate']);
	dealTime(tds[2], data['processDate'],data['status']);
	renderCell(tds[3], data['expend']);
	cutOutData(tds[4], data['consumeName'],30);
	statusCell(tds[5], data['status']);
	operation(tds[6], data);
}

function operation(cell, data) {
	var _p = $(cell);
	_p.empty();
	
	var integralType=data.integralType;
	if(integralType==3){
	//可以编辑
	var view_address=$('<a href="javascript:void(0);">收货地址</a>');
		view_address.click(function(e){
			e.preventDefault();
			$.getJSON('/project/integral/exchange/get/address',
					{'uuid':data['uuid']},
					function(data){
						if(data.status=='success'){
							showModal(data);
						}else{
							alert(data.message);
						}
					});
		});
		_p.append(view_address);
	}
	
	//处理
	if(data['status'] == 0){
		var pass = '';
		if($(cell).children('a').length>0){
			pass=$('<a style="margin-left:10px" href="javascript:void(0);">处理</a>');
		}else{
			pass=$('<a href="javascript:void(0);">处理</a>');
		}
		pass.click(function(e) {
			e.preventDefault();
			if(confirm("请确认已将兑换商品发放？")){
				$.post('/admin/integral/exchange/status', {
					uuid : data['uuid'],
					status : 1
				}).always(function(_data) {
					if (_data.status == 'success') {
						refreshItem(_p.parent().children(), _data.data)
					} else {
						if (!!_data.message) {
							alert(_data.message)
						} else {
							alert(_data.status)
						}
					}
				})
			}
		})
		_p.append(pass);
	}
}

function showModal(data){
	var address_modal=$('#address_modal');
	address_modal.data('uuid', data.uuid);
	$('#ex_name').val(data.name);
	$('#ex_phone').val(data.phone);
	$('#ex_address').val(data.address);
	address_modal.modal('show');
}
