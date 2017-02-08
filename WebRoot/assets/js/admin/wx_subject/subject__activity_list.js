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
var wxSubject_activity_list_table,subject_modal_activity,confirm_activity,
div_select_activity_total,div_select_activity,wx_subject_name_activity,cover_div_activity,parent_uuid;
$(function() {
	subject_modal_activity=$('#subject_modal_activity');
	confirm_activity=$('#confirm_activity');
	wx_subject_name_activity=$('#wx_subject_name_activity');
	cover_div_activity=$('#cover_div_activity');
	div_select_activity_total=$('#div_select_activity_total');
	div_select_activity=$('#div_select_activity');
	parent_uuid=$('#sub_uuid').val();
	// 加载table数据
	wxSubject_activity_list_table=$('#wxSubject_activity_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/wx/subject/sub/list/data/"+parent_uuid+"?rootType=2",
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
			"mData" : "cover",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				imageShow(nTd, sData);
			}
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
				loadAddLinkAct(nTd, sData, oData['isStatus']);
			}
		} ]
	});
	
	
	subject_modal_activity.on('hide.bs.modal', function() {
		// 清除所有之前的选择
		clearElallAct();
	});
	
	//增加学科
	$('#addActivity').click(function(e){
		e.preventDefault();
		// 清除所有之前的选择
		clearElallAct();
		appendDivItemAct();
		//div_select_activity_total.show();
		//下拉选择
		buildSearchSelect();
		
		confirm_activity.unbind('click');
		confirm_activity.click(function(event){
			event.preventDefault();
			var obj = $('#select_obj_item').val();
			if(!obj){
				alert('请选中一个活动');
				return false;
			}
			var name=wx_subject_name_activity.val(); 
			if(name==''){
				alert('请输入名称');
				return false;
			}
			var cover=$('#cover_hidden_act').val();
			if(!cover){
				alert('请上传封面');
				return false;
			}
			$.post('/project/wx/subject/common/add/data',
					{
				'name':name,
				'cover':cover,
				'type':2,
				'rootType':2,
				'reflectUuid':obj,
				'parentUuid':parent_uuid
					},
					function(data){
						if(data.status=='success'){
							subject_modal_activity.modal('hide');
							wxSubject_activity_list_table.fnDraw(false);
						}else{
							alert(data.message);
						}
					});
		});
		initUploadImageAct();
		subject_modal_activity.modal('show');
	});

});

//添加上传图片
function appendDivItemAct(){
	var item='<input id="cover_act" type="file" class="form-control" value="banner封面" />'
		      +'<img id="cover_preview_act" alt="" src="" style="display: none;height : 180px;" class="pre-view" >'
		      +'<input type="hidden" name="cover" id="cover_hidden_act"/>';
	cover_div_activity.append(item);
}

function buildSearchSelect() {
	var select_val = '<select class="form-control" id="select_obj_item"></select>';
	div_select_activity.append(select_val);

	// 获取服务器对应的10条活动数据
	$.getJSON('/project/wx/subject/get/activity',{'q':''}, function(data) {
		if (data.status == 'success') {
			initModalData(data);
		} else {
			alert(data.message);
		}
	});

}

function initModalData(data) {
	buildSelect(data.items);
	var select_obj_item = $('#select_obj_item');
	select_obj_item.get(0).selectedIndex = 0;
	// 绑定change事件
	select_obj_item.change(function() {
		var uuid = $(this).val();
		if (!!uuid) {
			// 获取服务器活动对应的cover
			$.getJSON('/project/wx/subject/get/activity/cover',{'uuid':uuid},  function(data) {
				if (data.status == 'success') {
					wx_subject_name_activity.val(data.activityName);
					$('#cover_hidden_act').val(data.cover);
					$('#cover_preview_act').attr('src',data.cover);
					$('#cover_preview_act').show();
				} else {
					alert(data.message);
				}
			});
		}
	});
	select_obj_item.ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/project/wx/subject/get/activity'
	}, {
		loadingImg : '/assets/img/loading.gif'
	});

	$('#select_obj_item_chosen').css('width', '100%');
}

function buildSelect(list) {
	var select_obj_item = $('#select_obj_item');
	select_obj_item.append('<option value="">请输入"活动名称"检索活动</option>');
	if (!!list && list.length > 0) {
		$.each(list, function(index, item) {
			var option = buildOption(item);
			select_obj_item.append(option);
		});
	}
}

function buildOption(addition) {
	var option = '<option value="' + addition.id + '">' + addition.text
			+ '</option>';
	return option;
}

//初始化上传图片
function initUploadImageAct(){
	uploaderInit(new Part($('#cover_act'), 1, function(part) {
		var cover_hidden=$('#cover_hidden_act').val();
		if(!!cover_hidden){
		   part.renderPreview(cover_hidden);
		}
	}, function(src){
		$('#cover_hidden_act').val(src);
	}).init())
}


function clearElallAct(){
	wx_subject_name_activity.val('');
	cover_div_activity.empty();
	div_select_activity.empty();
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

function loadAddLinkAct(cell, uuid, isStatus) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	if (isStatus == 2) {
		item = $('<a href="javascript:void(0);">上线</a>');
	} else if (isStatus == 1) {
		item = $('<a href="javascript:void(0);">下线</a>');
	} else if (isStatus == 0) {
		item = $('<a href="javascript:void(0);">审核</a>');
	}
	item.click(function(event){
		event.preventDefault();
		$.post('/project/wx/subject/status/update',{'uuid':uuid},function(data){
			if(data.status=='success'){
				refreshItemAct(parent.parent().children(), data);
			}else{
				alert(data.message);
			}
		});
	});
	parent.append(item);
	
	// 编辑
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>');
	edit.click(function(event){
		event.preventDefault();
		$.getJSON('/project/wx/subject/update/need/page/data', {
			'uuid' : uuid,
		}, function(data) {
			if (data.status == 'success') {
				wx_subject_name_activity.val(data.name);
				appendDivItemAct();
				var activity_label = '<label class="control-label">'+data.activityName+'</label>';
				div_select_activity.append(activity_label);
				$('#cover_hidden_act').val(data.cover);
				confirm_activity.unbind('click');
				confirm_activity.click(function(event){
					event.preventDefault();
					var name=wx_subject_name_activity.val(); 
					if(name==''){
						alert('请输入一级学科名称');
						return false;
					}
					var cover=$('#cover_hidden_act').val();
					if(!cover){
						alert('请上传学科封面');
						return false;
					}
					$.post('/project/wx/subject/update/data',
							{
						'uuid':data.uuid,
						'name':name,
						'cover':cover,
						'type':2
							},
							function(subData){
								if(subData.status=='success'){
									subject_modal_activity.modal('hide');
									refreshItemAct(parent.parent().children(), subData);
								}else{
									alert(subData.message);
								}
							});
				});
				initUploadImageAct();
				subject_modal_activity.modal('show');
			} else {
				alert(data.message);
			}
		})
	})
	parent.append(edit);
	
	
	//删除
	var detele=$('<a style="margin-left:10px" href="javascript:void(0);">删除</a>');
	detele.click(function(event){
		event.preventDefault();
		if(confirm('是否删除该活动banner？')){
			$.post('/project/wx/subject/delete/data',{'uuid':uuid},function(data){
				if(data.status=='success'){
					wxSubject_activity_list_table.fnDraw(false);
				}else{
					alert(data.message);
				}
			});
		}
	});
	parent.append(detele);
}

function refreshItemAct(tds, data) {
	cutOutData(tds[0], data['name'],50);
	renderCell(tds[1], data['createDate']);
	imageShow(tds[2], data['cover']);
	statusCell(tds[3], data['isStatus']);
	loadAddLinkAct(tds[4], data['uuid'], data['isStatus']);
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


/**
 * 添加图片显示
 */
function imageShow(cell, cover) {
	var parent = $(cell);
	var title='封面';
	parent.empty();
	if (!!cover) {
		var img = '<a href=' + cover + ' title="' + title
				+ '" data-gallery=""><img style="width:80px;" src="' + cover
				+ '"/></a>';
		parent.append(img);
	}
}
