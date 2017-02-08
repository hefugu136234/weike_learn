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

var table_web;

$(document).ready(function() {
	showActive([ 'assets-mgr', 'banner_mgr_nav' ]);
	
	loadDate_web();
	
	$('#cloud').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
		
		table_web.fnReloadAjax("/project/banner/showBannerList?type=4");
	})
});

function loadDate_web(){
	// 加载table数据
	table_web = $('#bannerTable_web').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/banner/showBannerList?type=4",
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				if (json.status) {
					if (json.status != 'success')
						alert(json.status);
				}
				fnCallback(json);
			});
		},
		"aoColumns" : [ {
			"mData" : "imageUrls",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showImages_web(nTd, sData, '预览');
			}
		}, {
			"mData" : "title",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail_web(nTd, sData);
			}
		},{
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "positionStr",
			"orderable" : false
		}, {
			"mData" : "refUrl",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail_web(nTd, sData);
			}
		}, {
			"mData" : "type",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showType_web(nTd, sData);
			}
		}, {
			"mData" : "state",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showStatus_web(nTd, sData);
			}
		}, {
			"mData" : "validDate",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				validDateDetail_web(nTd, sData);
			}
		} ,{
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail_web(nTd, sData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operation_web(nTd, sData, oData, oData['state']);
			}
		}]
	});
}

//查看图片
var patt = new RegExp("^(http).*");
function showImages_web(cell, taskId, title) {
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		var img = '<a href=' + taskId + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src="'
			+ taskId + '"/></a>';
		parent.append(img);
	}
}

//Banner 平台类型展示
function showType_web(nTd, type){
	var _p = $(nTd);
	_p.empty();
	var value = '';
	if (type == '1') {
		value = '<span style="color: green;">盒子平台</span>';
	} else if (type == '2') {
		value = '<span style="color: green;">App</span>';
	} else if (type == '3') {
		value = '<span style="color: green;">微信平台</span>';
	} else if (type == '0'){
		value = '<span style="color: green;">通用</span>';
	}else if (type == '4'){
		value = '<span style="color: green;">WEB平台</span>';
	} else {
		value = '<span style="color: red;">未知</span>'
	}
	_p.append(value);
}

//Banner 当前状态显示
function showStatus_web(nTd, state){
	var _p = $(nTd);
	_p.empty();
	var value = '';
	if (state == '2') {
		value = '<span style="color: #FF9933;">已下线</span>';
	} else if (state == '1') {
		value = '<span style="color: green;">已上线</span>';
	} else {
		value = '<span style="color: red;">状态异常</span>';
	}
	_p.append(value);
}

//标题，RefUrl, 备注等信息完整显示
function showDetail_web(td, data){
	var td = $(td);
	td.empty();
	if(data.length > 10){
		var tmpData = data.substring(0, 10) + "......";
		td.append(tmpData);
	}else{
		td.append(data);
	}
	td.on("click", function(){
		$('#dataDetail_web').text(data);
		$('#dataModal_web').modal('show');
	})
}

//有效期详情显示
function validDateDetail_web(nTd, sData){
	var td = $(nTd);
	td.empty();
	var value = sData;
	if(0 == sData){
		value = '<span style="color: red;">未指定</span>';
	}
	td.append(value);
}

//上下线，编辑, 软删除等操作
function operation_web(td, uuid, data, state){
	var parent = $(td);
	parent.empty();
	var stateOperation = '';
	if (state == '2'){
		//如果当前的状态是2（已下线），则可以进行上线操作
		stateOperation = $('<a href="#">上线</a>');
	} else if (state == '1'){
		//如果当前的状态是1（已上线），则可以进行下线操作
		stateOperation = $('<a href="#">下线</a>');
	} else if (state == '0'){
		//如果当前状态是0（未审核），则可以进行审核操作
		stateOperation = $('<a href="#">审核</a>');
	} else{
		stateOperation = $('<font color="red">???</font>');
	}
	//图片上下线操作
	$(stateOperation).bind("click", function(){
		if(confirm("你确定要执行该操作吗")){
			$.post('/project/banner/updateState', {uuid : uuid}, function(data){
				//test
				console.log(data);
				if(data['status']){
					alert(data['status']);
				} else {
					//操作成功后，更新单元格中的状态显示
					//refreshItem_all(parent.parent().children(), data);
					//refreshItem_app(parent.parent().children(), data);
					//refreshItem_cloud(parent.parent().children(), data);
					//refreshItem_wechat(parent.parent().children(), data);
					refreshItem_web(parent.parent().children(), data);
					//window.location.href = '/project/banner/mgr';
				}
			});	
		}
	});
	parent.append(stateOperation).append(" | ");
	
	//编辑操作
	var edit = $('<a href="#">编辑</a>');
	edit.click(function(){
		location.href = "/project/banner/updatePage/" + uuid;
	})
	parent.append(edit).append(" | ");
	
	//banner软删除操作
	var del = $('<a href="#">删除</a>');
	$(del).bind("click", function(){
		if(confirm("你确定要删除该条记录吗")){
			$.post('/project/banner/del', {uuid : uuid}, function(data){
				if('success'== data['status']){
					//location.reload(true);
					//window.location.href = '/project/banner/mgr';
					
					//table_all.fnReloadAjax("/project/banner/showBannerList");
					//table_app.fnReloadAjax("/project/banner/showBannerList?type=2");
					//table_cloud.fnReloadAjax("/project/banner/showBannerList?type=1");
					//table_wechat.fnReloadAjax("/project/banner/showBannerList?type=3");
					table_web.fnReloadAjax("/project/banner/showBannerList?type=4");
				} else {
					alert(data['message']);
				}
			});
		}
	});
	parent.append(del);
	
}

//点击上下线操作后，显示图片当前的状态
function refreshItem_web(row, data) {
	showStatus_web(row[6], data['state']);
	operation_web(row[9], data['uuid'], data, data['state']);
}
