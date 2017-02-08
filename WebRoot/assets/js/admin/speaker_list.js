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
	showActive([ 'assets-mgr', 'speaker_mgr_nav' ]);

	// 加载table数据
	oTable = $('#speakers_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/speakers/datatable",
		"aoColumns" : [ {
			"mData" : "avatar",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				console.log(sData);
				imageShow(nTd, sData, '头像');
			}
		},{
			"mData" : "name",
			"orderable" : false
		}, {
			"mData" : "sex",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				sexCell(nTd, sData);
			}
		}, {
			"mData" : "date",
			"orderable" : false
		}, {
			"mData" : "userName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				userNameOperation(nTd, sData, oData['userUuid']);
			}
		} , {
			"mData" : "hospitalName",
			"orderable" : false
		}, {
			"mData" : "department",
			"orderable" : false
		}, {
			"mData" : "professor",
			"orderable" : false
		},{
			"mData" : "isStatus",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operator(nTd, sData, oData['isStatus'], oData);
			}
		} ]
	});
	
	onUserModelShow();
	
	userSelectorInit();
	
	showSelectedUserDetail();
});

var speakerUuid;
var userUuid;

function userNameOperation(nTd, sData, uuid){
	//test
	console.log(sData);
	
	var _p = $(nTd);
	_p.empty();
	if(sData.length == 0){
		_p.append('<font color="red">暂无</font>');
	}else{
		var item = $('<a href="#">' + sData + '</a>')
		item.click(function(e){
			e.preventDefault();
			userInfo(uuid)
		})
		_p.append(item);
	}
}

function userNickNameOperation(nTd, sData){
	//test
	console.log(sData);
	
	var _p = $(nTd);
	_p.empty();
	if(sData.length == 0){
		_p.append('<font color="red">暂无</font>');
	}else{
		_p.append(sData);
	}
}


function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	 if (isStatus == 1) {
		value = '<span style="color: green;">已启用</span>';
	} else if (isStatus == 0) {
		value = '<span style="color: red;">已禁用</span>'
	}
	_p.append(value);
}

function operator(cell, uuid, isStatus, oData) {
	var tmp = $(cell);
	
	tmp.empty();
	var editor = $('<a href="javascript:void(0);">编辑</a>');
	editor.click(function(e) {
		e.preventDefault();
		location.href = "/project/speaker/" + uuid + "/edit";
	});
	tmp.append(editor).append(' | ');
	
	var change_status = '';
	if (isStatus == 1) {
		//change_status = $('<a style="margin-left: 10px;" href="javascript:void(0);">禁用</a>');
		change_status = $('<a href="javascript:void(0);">禁用</a>');
	} else {
		//change_status = $('<a style="margin-left: 10px;" href="javascript:void(0);">启用</a>');
		change_status = $('<a href="javascript:void(0);">启用</a>');
	}
	change_status.click(function(e) {
		e.preventDefault();
		$.post('/project/speaker/status/change',{'uuid' : uuid},
				function(data){
			//test
			console.log(data);
			
			if(data.status=='success'){
				refreshItem(tmp.parent().children(), data);
				//oTable.fnReloadAjax("/project/speakers/datatable");
			}else{
				alert(data.message);
			}
		});
	});
	tmp.append(change_status).append(' | ');
	
	if(!!oData && oData.userId == '0'){
		var associationUser = $('<a href="javascript:void(0);"><font color="green">关联用户</font></a>');
		associationUser.click(function(){
			$('#userModel').modal('show');
			userAssociationSubmit(tmp,uuid)
		})
		tmp.append(associationUser);
	} else {
		var associationUserExist = $('<a href="javascript:void(0);"><font color="blue">解除关联</font></a>');
		associationUserExist.click(function(){
			if(confirm("你确定要取消关联吗")){
				$.post("/project/speaker/userClean", {speakerUuid: uuid}, function(data){
					//test
					console.log(data);
					if('success' == data.status){
						alert("解除关联成功");
						//oTable.fnReloadAjax("/project/speakers/datatable");
						refreshItem(tmp.parent().children(), data);
					}else{
						alert(data.status.message)
					}
				})
			}
		})
		tmp.append(associationUserExist);
	}
	
}

function sexCell(cell, sex) {
	var c = $(cell);
	c.empty();
	c.append(sex == 0 ? "女" : "男")
}

function refreshItem(tds, data) {
	imageShow(tds[0], data['avatar'], '头像');
	renderCell(tds[1], data['name']);
	sexCell(tds[2], data['sex']);
	renderCell(tds[3], data['date']);
	userNameOperation(tds[4], data['userName'], data['userUuid']);
	//userNickNameOperation(tds[4], data['userNickName']);
	renderCell(tds[5], data['hospitalName']);
	renderCell(tds[6], data['department']);
	renderCell(tds[7], data['professor']);
	statusCell(tds[8], data['isStatus']);
	operator(tds[9], data['uuid'], data['isStatus'], data);
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

function onUserModelShow(){
	var body = $('#userInfoTable').find('tbody');
	$('#userModel').on('show.bs.modal', function() {
		$('#user_selector').find("option[text='null']").attr("selected",true);
		$('#user_selector_chosen').width(320);
		$('#user_detail_div').hide();
	});
}

function userSelectorInit(){
	$('#user_selector').ajaxChosen({
	    dataType: 'json',
	    type: 'GET',
	    url:'/admin/user/search'
	},{
	    loadingImg: '/assets/img/loading.gif'
	})
}

function showUserDetail(data){
	$('#user_nickName').text(data.nickname);
	$('#user_name').text(data.username);
	$('#user_phoneNum').text(data.phoneNumber);
	$('#user_desciption').text(data.mark);
}

function showSelectedUserDetail(){
	$('#user_selector').change(function(){
		userUuid = $('#user_selector').val();
		userInfo(userUuid);
		
		/*$('#user_detail_div').show();
		userUuid = $('#user_selector').val();
		$.get("/admin/user/detail", {userUuid: userUuid}, function(data){
			if('success' == data.status){
				showUserDetail(data.data);
			}
		})*/
	})
}

//关联请求
function userAssociationSubmit(cell,speakerUuid){
	var submit = $('#user_association_submit'); 
	submit.unbind('click');
	submit.click(function(){
		$.post('/admin/speaker/associationUser/',{
			checkedUserUuid : userUuid,
			speakerUuid : speakerUuid
			}, function(data){
			if(data.status=='success'){
				alert("关联成功");
				$('#userModel').modal('hide');
				//oTable.fnReloadAjax("/project/speakers/datatable");
				
				//TODO:
				refreshItem(cell.parent().children(), data);
			}else{
				alert(data.message);
				$('#userModel').modal('hide');
			}
		});
	})
}

var patt = new RegExp("^(http).*");
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

