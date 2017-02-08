$(document).ready(function() {
	$('#activity_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/activity/list/datatable",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 25);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "username",
			"orderable" : false,
		}, {
			"mData" : "categoryName",
			"orderable" : false
		}, {
			"mData" : "members",
			"orderable" : false,
		}, {
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 25);
			}
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
				// loadAddLink(nTd, sData, oData['isStatus'], oData['taskId']);
				func(nTd, sData, oData['_status'])
			}
		} ]
	});
	
	/*//全选
	$('#selectAll').on('click',function () { 
		$('ul').find('input:checkbox').prop("checked", true);   
	});
	
	//取消全选
	$('#calcelAll').on('click',function () { 
		$('ul').find('input:checkbox').prop("checked", false);   
	});
	
	//反选
	$('#reverse').click(function () {  
		$('ul').find('input:checkbox').each(function (index, element) {   
	        if($(element).prop("checked")==true){ 
	        	$(this).prop("checked",false); 
	        }else{
	        	$(this).prop("checked",true); 
	        }
	    }); 
	});*/
})

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

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'],25);
	renderCell(tds[1], data['createDate']);
	renderCell(tds[2], data['username']);
	renderCell(tds[3], data['categoryName']);
	renderCell(tds[4], data['members']);
	cutOutData(tds[5], data['mark'],25);
	statusCell(tds[6], data['_status']);
	func(tds[7], data['uuid'], data['_status']);
}

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function func(cell, uuid, _status) {
	var _p = $(cell);
	_p.empty();
	var op;
	if (_status == 0) {
		op = $('<a href="#">审核</a>');
	} else if (_status == 1) {
		op = $('<a href="#">下线</a>');
	} else if (_status == 2) {
		op = $('<a href="#">上线</a>');
	}
	_p.append(op)
	op.click(function(e) {
		e.preventDefault();
		$.post('/admin/activity/' + uuid + '/status', {
			status : _status
		}).always(function(data) {
			if (data.status == 'success') {
				refreshItem(_p.parent().children(), data.data)
			} else {
				if (!!data.message) {
					alert(data.message)
				} else {
					alert(data.status)
				}
			}
		})
	})
	// 设置
	var settings = '&nbsp;&nbsp;<a href="/admin/activity/' + uuid
			+ '/config">配置</a>';
	_p.append(settings)
	
	var update = '&nbsp;&nbsp;<a href="/admin/activity/' + uuid
			+ '/edit" >编辑</a>';
	if(_status == 2){
		_p.append(update);
	}
	var res_mgr = '&nbsp;&nbsp;<a href="/admin/activity/' + uuid 
			+ '/reslist">资源配备</a>';
	_p.append(res_mgr);
	var user_mgr = '&nbsp;&nbsp;<a href="/admin/activity/' + uuid 
			+ '/userlist">人员管理</a>&nbsp;&nbsp;';
	_p.append(user_mgr);
	
	var qrcode_url = $('<a href="javascript:void(0);">二维码</a>');
	qrcode_url.click(function(event){
		event.preventDefault();
		$.getJSON('/project/qrcode/type/res',
				{
			'uuid':uuid,
			'type':1
				},
				function(data){
			if(data.status=='success'){
				$('#qrcode_url').val(data.message);
				$('#qrcode_preview').attr('src',data.message);
				$('#dataModal_qrcode').modal('show');
			}else{
				alert(data.message);
			}
		});
		
	});
   _p.append(qrcode_url).append('</br>');
   
   if (_status != 0) {
		// 查看知了活动链接
		var banner = $('<a style="margin-left:10px" href="javascript:void(0);">banner链接</a>');
		banner.click(function(event) {
			event.preventDefault();
			$('#wx_content').val(
					'/api/webchat/activity/total/page/' + uuid);
			$('#web_content').val(
					'/f/web/activity/detail/' + uuid);
			$('#dataModal_link').modal('show');
		});
		_p.append(banner);
	}
   
   //活动学科
   var activity_sub=$('<a href="javascript:void(0);">活动学科</a>');
   activity_sub.click(function(e){
	   e.preventDefault();
	   location.href='/admin/activity/subject/page/'+uuid;
   });
   _p.append('&nbsp;&nbsp;');
   _p.append(activity_sub);
   
   //专家
   var activity_expert=$('<a href="javascript:void(0);">专家</a>');
   activity_expert.click(function(e){
     e.preventDefault();
     location.href='/admin/activity/expert/page/'+uuid;
   });
   _p.append('&nbsp;&nbsp;');
   _p.append(activity_expert);
   
   //推送
   var activity_message=$('<a href="javascript:void(0);">推送</a>');
   activity_message.click(function(e){
	    e.preventDefault();
	    onModalShow(uuid);
	    $('#dataModal_message').modal('show');
	    
	    //提交发送消息请求
		$('#message_confirm_btn').unbind('click');
		$('#message_confirm_btn').click(function(e){
			e.preventDefault();
			var checkedArr = new Array; 
			var checks = $('ul').find('input:checked');
			checks.each(function(i){ 
				checkedArr[i] = $(this).val(); 
	        }); 
			checkedUuids = checkedArr.join(','); 
			console.log(checkedUuids);
			
			if(!$('#subject').val() || !$('#content').val()){
				alert('请输入消息详情');
				return ;
			}
			/*if(!checkedUuids || checkedUuids.length <= 0){
				alert('请选择要发送模版消息的用户');
				return ;
			}*/
			
			if(!confirm("是否确定为用户推送消息?")){
				alert('您已取消操作');
				return ;
			}else{
				$.post("/admin/activity/messageForChange", {
					checkedUuids: checkedUuids, 
					activityUuid: uuid,
					subject: $('#subject').val(),
					content: $('#content').val()}, function(data){
					if ('success' == data.status){
						alert('消息已发送');
						$('#dataModal_message').modal('hide');
					} else {
						if(!!data.message){
							alert(data.message)
						}else{
							alert('操作失败');
						}
					}
				})
			}
		});
   });
   _p.append('&nbsp;&nbsp;');
   _p.append(activity_message);
   
   //消息推送记录
   var message_log=$('<a href="javascript:void(0);">推送记录</a>');
   message_log.click(function(e){
     e.preventDefault();
     location.href='/admin/activity/message/page/'+uuid;
   });
   _p.append('&nbsp;&nbsp;');
   _p.append(message_log);
}

//js截取字符串
function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}

function onModalShow(activityUuid){
	$('#dataModal_message').on('show.bs.modal', function(e) {
		$('#subject').val('');
		$('#content').val('');
		
		//加载活动成员
		/*$.ajax({
	        type: "GET",
	        async: false,	//设置该ajax同步运行
	        url: '/admin/activity/userlist/forMessage/' + activityUuid,
	        success: function(data) {
	        	if (data.status == 'success') {
					console.log(data['data']);
					buildOption(data['data']);
				} else {
					if (!!data.message) {
						alert(data.message)
					} else {
						alert(data.status)
					}
				}
	        }
	    });*/
	});
}

function buildOption(data){
	console.log(data);
	
	var ul = $('.ibox-content').find('ul');
	ul.empty();
	if(data.length <= 0){
		ul.append(' <li>' + 
		                '<input type="checkbox" value="" name="" class="i-checks"/>' + 
		                '<span class="m-l-xs">该活动还没有人参加</span>' +
		            '</li>');
		$('#message_confirm_btn').attr("disabled","disabled");
		$('#selectAll').attr("disabled","disabled");
		$('#calcelAll').attr("disabled","disabled");
		$('#reverse').attr("disabled","disabled");
	}else{
		$.each(data, function(index, element){
			ul.append(' <li>' + 
			                '<input type="checkbox" value="' + element['uuid'] + '" name="" class="i-checks"/>' + 
			                '<span class="m-l-xs">' + element['name'] + '</span>' +
			                '<a class="btn btn-sm btn-primary bfR" href="javascript:void(0);" id="'
			                					+ element['uuid'] + '">点击查看用户详情</a>' +
			            '</li>');
			$(ul).find('li #' + element['uuid']).click(function(){
				userInfo(element['uuid']);
			})
		})
	}
}