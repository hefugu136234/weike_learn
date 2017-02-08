$.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw ){
    if ( sNewSource != undefined && sNewSource != null ) {
        oSettings.sAjaxSource = sNewSource;
    }
    // Server-side processing should just call fnDraw
    if ( oSettings.oFeatures.bServerSide ) {
        this.fnDraw();
        return;
    }
    this.oApi._fnProcessingDisplay( oSettings, true );
    var that = this;
    var iStart = oSettings._iDisplayStart;
    var aData = [];
    this.oApi._fnServerParams( oSettings, aData );
    oSettings.fnServerData.call( oSettings.oInstance, oSettings.sAjaxSource, aData, function(json) {
        /* Clear the old information from the table */
        that.oApi._fnClearTable( oSettings );
        /* Got the data - add it to the table */
        var aData =  (oSettings.sAjaxDataProp !== "") ?
            that.oApi._fnGetObjectDataFn( oSettings.sAjaxDataProp )( json ) : json;
        for ( var i=0 ; i<aData.length ; i++ ){
            that.oApi._fnAddData( oSettings, aData[i] );
        }
        oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
        that.fnDraw();
        if ( bStandingRedraw === true ){
            oSettings._iDisplayStart = iStart;
            that.oApi._fnCalculateEnd( oSettings );
            that.fnDraw( false );
        }
        that.oApi._fnProcessingDisplay( oSettings, false );
        /* Callback user function - for event handlers etc */
        if ( typeof fnCallback == 'function' && fnCallback !== null )
        {
            fnCallback( oSettings );
        }
    }, oSettings );
};

//Custom scripts
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

var oTable_category;
var edit_row;
var current_id;

var relate_cell,relate_data;

var checkedUuids;
var _resUuid;

var permissions;

$(document).ready(function(){
	
	// 加载table数据
	oTable_category = $('#categoryDetail_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/resource/categoryDeail/" + current_id,
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				fnCallback(json)
			});
		},
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//cellCreate(nTd, sData)
				renderResourceCell(nTd, sData, oData['uuids']['resourceUuid']);
			}
		}, {
			"mData" : "dates",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cellCreate(nTd, sData)
			}
		}, {
			"mData" : "viewCount",
			"orderable" : false
		}, {
			"mData" : "speakerVo",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				speakerCell(nTd, oData.speakerVo);
			}
		}, {
			"mData" : "type",
			"orderable" : false
		},
//		{
//			"mData" : "mark",
//			"orderable" : false,
//			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
//				cellCreate(nTd, sData)
//			}
//		}, 
		{
			"mData" : "state",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, oData)
			}
		}, {
			"mData" : "uuids",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				oprationCell(nTd, oData, sData);
			}
		}]
	});
	
	$('#searchButton_type').change(function(){
		loadData();
	});
	
	$('#searchButton_state').change(function(){
		loadData();
	});
 
	$('#resAdd_VIDEO').click(function(){
		location.href = '/asset/video/upload';
	})
	$('#resAdd_PDF').click(function(){
		location.href = '/project/pdf/add/page';
	})
	$('#resAdd_NEWS').click(function(){
		location.href = '/project/news/add/page';
	})
	$('#resAdd_THREESCREEN').click(function(){
		location.href = '/project/threescreen/page/add';
	})
	
	
	//保存所操作资源关联的标签
	$('#checkboxSubmitButton').click(function(event){
		var checkedArr = new Array; 
		event.preventDefault();
		$('#childTagsTable input:checked').each(function(i){ 
			checkedArr[i] = $(this).val(); 
        }); 
		checkedUuids = checkedArr.join(','); 
		if('' !== checkedUuids && checkedUuids.length != 0 && '' !== _resUuid && _resUuid.length != 0){
			$.post("/project/resource/labeling/resAddLabel/",
					{checkedTagsUuids: checkedUuids, resourceUuid: _resUuid}, function(data){
				$("#childTagsTable :checkbox").prop("checked", false); 
				if ('success' == data.status){
					$('#tagsModel').modal('show');
					checkedUuids = '';
				} else {
					if(!!data.message){
						alert(data.message)
					}else{
						alert('关联标签失败');
					}
				}
			})
		}
	})
	
	//保存资源访问设置
	$('#resAccessSave').click(function(event){
		var checkedArr = new Array; 
		event.preventDefault();
		$('#resourceAccessBody input:checked').each(function(i){ 
			checkedArr[i] = $(this).val(); 
        }); 
		permissions = checkedArr.join(';'); 
		if('' !== _resUuid && _resUuid.length != 0){
			console.log(permissions);
			$.post("/resource/access/permissions",
					{'permissions': permissions, 'resourceUuid': _resUuid}, function(data){
				if ('success' == data.status){
					$('#resourceAccess').modal('hide');
					permissions = '';
				} else {
					if(!!data.message){
						alert(data.message)
					}else{
						alert('操作失败，请重试');
					}
				}
			})
		}else{
			$('#resourceAccess').modal('hide');
		}
	})
	
	//全选
	$('#selectAll').on('click',function () { 
		$("#childTagsTable :checkbox").prop("checked", true);   
	});
	
	//取消全选
	$('#calcelAll').on('click',function () { 
		$("#childTagsTable :checkbox").prop("checked", false);   
	});
	
	//反选
	$('#reverse').click(function () {  
		$('#childTagsTable :checkbox').each(function (index, element) {   
	        if($(element).prop("checked")==true){ 
	        	$(this).prop("checked",false); 
	        }else{
	        	$(this).prop("checked",true); 
	        }
	    }); 
	});
	
	//初始化按钮事件
	$('#code_search').click(function(event){
		event.preventDefault();
		var oups_code=$('#oups_code').val();
		if(!!oups_code){
			$.getJSON('/project/resource/oups/search/code',{'code':oups_code},
					function(data){
				//console.log(data);
				if(data.status=='success'){
					showoupsdetail(data);
					$('#oups_total_div').show();
					$('#oups_error_div').hide();
				}else{
					$('#oups_total_div').hide();
					$('#oups_error_label').text(data.message);
					$('#oups_error_div').show();
					$('#oupd_uuid_hide').val('');
				}
			});
		}else{
			alert('请输入作品编号');
		}
	});
	
	$('#oups_button').click(function(event){
		event.preventDefault();
		var resUuid = $('#res_uuid_hide').val();
		var oupd_uuid_hide=$('#oupd_uuid_hide').val();
		if(!!oupd_uuid_hide){
			$.post('/project/resource/oups/relate/res/save',{'resUuid':resUuid,'oupsUUid':oupd_uuid_hide},
					function(data){
				if(data.status=='success'){
					alert('绑定成功');
					$('#oupsModal').modal('hide');
					//relateoups(relate_cell, relate_data,2);
					oprationCell(relate_cell, data.data, data.data.uuids);
				}else{
					alert(data.message);
				}
			});
		}else{
			alert('请先查询作品编号');
		}
	});
	
});

function relateoups(cell, td_data){
	var uuids=td_data['uuids'];
	var resourceUuid = uuids['resourceUuid'];
	var relatedOups=td_data['relatedOups'];
	var related_name = '';
	
	if(relatedOups){
		related_name='作品查看';
	}else{
		related_name='关联作品';
	}
	var relatedOups_edit=$('<a href="javascript:void(0);">'+related_name+'</a>');
	relatedOups_edit.click(function(event){
		event.preventDefault();
		relate_cell=cell;
		relate_data=td_data;
		oupsModalShow(relatedOups_edit,resourceUuid,td_data['name']);
	});
	cell.append("</br>").append(relatedOups_edit);
}

function embed(cell,td_data){
	var uuids=td_data['uuids'];
	var resourceUuid = uuids['resourceUuid'];
	var em = $('<a href="javascript:void(0);">embed</a>');
	em.click(function(e){
		e.preventDefault();		
		$.get('/project/resource/'+resourceUuid+'/embed/iframe').always(function(data){
			if(data.status == 'success'){
				$('#embedModel').modal('show');
				$('#iframe-code-container').text(data.iframe)
			}else{
				alert(data.message)
			}
		})
	})
	cell.append(" | ").append(em);
}

//上下线及编辑操作
function oprationCell(cell, td_data, uuids) {
	
	var pdfUuid = uuids['pdfUuid'];
	var newsUuid = uuids['newsUuid'];
	var threescreenUuid = uuids['threescreenUuid'];
	var vodeoUuid = uuids['vodeoUuid'];
	var resourceUuid = uuids['resourceUuid'];
	
	var resourceState = td_data['state'];
	var resourceType = td_data['type'];
	
	var parent = $(cell);
	parent.empty();
	
	
	//资源上下线
	var item = $('');
	if (resourceState == 1) {
		//item = $('<a href="javascript:void(0);">下线</a>')
		item = $('<button class="ladda-button btn btn-warning" data-style="zoom-in">下线</button>');
	} else if (resourceState == 0) {
		//item = $('<a href="javascript:void(0);">审核</a>')
		item = $('<button class="ladda-button btn btn-danger" data-style="zoom-in">审核</button>');
	} else if (resourceState == 2) {
		//item = $('<a href="javascript:void(0);">上线</a>')
		item = $('<button class="ladda-button btn btn-primary" data-style="zoom-in">上线</button>');
	}
	var item_ladda=item.ladda();
	item_ladda.click(function() {
		//进行资源类型判断，根据具体的资源类型，执行相应的操作
		if("PDF" === resourceType && !!pdfUuid){
			item_ladda.ladda( 'start' );
			$.post('/project/pdf/update/status', {
				uuid : pdfUuid
			}, function(pdf_data) {
				item_ladda.ladda( 'stop' );
				if (pdf_data.status) {
					alert(pdf_data.status);
				} else {
					td_data['state'] = pdf_data['isStatus'];
					td_data['dates'] = pdf_data['modifyDate'];
					refreshItem(parent.parent().children(), td_data, uuids);
				}
			});
		}else if("NEWS" === resourceType && !!newsUuid){
			item_ladda.ladda( 'start' );
			$.post('/project/news/change/status', {
				uuid : newsUuid
			}, function(news_data) {
				item_ladda.ladda( 'stop' );
				if (news_data.status) {
					alert(news_data.status);
				} else {
					td_data['state'] = news_data['isStatus'];
					td_data['dates'] = news_data['modifyDate'];
					refreshItem(parent.parent().children(), td_data, uuids);
				}
			});
		}else if("VIDEO" === resourceType && !!vodeoUuid){
			item_ladda.ladda( 'start' );
			$.post('/asset/t/video/status',{
				uuid : vodeoUuid
			},function(video_data){
				item_ladda.ladda( 'stop' );
				if (video_data.status == 'success') {
					td_data['state'] = video_data['video_status'];
					td_data['dates'] = video_data['modifyDate'];
					refreshItem(parent.parent().children(), td_data, uuids);
				} else {
					alert(video_data.status)
				}
			});
	
		}else if("THREESCREEN" === resourceType && !!threescreenUuid){
			item_ladda.ladda( 'start' );
			$.post('/project/threescreen/update/status', {
				uuid : threescreenUuid
			}, function(three_data) {
				item_ladda.ladda( 'stop' );
				if (three_data.status=='success') {
					td_data['state'] = three_data['isStatus'];
					td_data['dates'] = three_data['modifyDate'];
					refreshItem(parent.parent().children(), td_data, uuids);
				} else {
					if(!!three_data.message){
						alert(three_data.message);
					}else{
						alert('状态修改失败');
					}
				}
			});
		}else{
			alert("操作有误");
		}
	});
	parent.append(item).append(' | ');
	
	//资源编辑
	//进行资源类型判断，根据具体的资源类型，执行相应的操作
	var edit = $('<a href="javascript:void(0);">编辑</a>')
	edit.click(function() {
		if("PDF" == resourceType && !!pdfUuid){
			pdfEditor(pdfUuid);
		}else if("NEWS" == resourceType && !!newsUuid){
			newsEditor(newsUuid);
		}else if("VIDEO" == resourceType && !!vodeoUuid){
			videoEditor(vodeoUuid);
		}else if("THREESCREEN" == resourceType && !!threescreenUuid){
			threescreenEditor(threescreenUuid);
		}else{
			alert("操作有误");
		}
	})
	parent.append(edit).append(' | ');
	
	//配置资源标签
	var tags = $('<a href="javascript:void(0);">标签</a>');
	tags.click(function(){
		var parentTags = $('#parentTags');
		var childTags = $('#childTags');
		_resUuid = resourceUuid;
		onModalShow(resourceUuid);
		$('#tagsModel').modal('show');
	})
	parent.append(tags);
	
	//资源关联作品
	relateoups(parent, td_data);
	embed(parent,td_data);
	
	var answer = $('<a href="javascript:void(0);">投票</a>');
	answer.bind('click', answerClick(resourceUuid));
	parent.append(' | ').append(answer);
	
	// 设置资源访问权限
	var resAccess = $('<a href="javascript:void(0);">访问控制</a>');
	resAccess.click(function(event){
		event.preventDefault();
		_resUuid = resourceUuid;
		
		var checkboxs = $("#resourceAccessBody :checkbox");
		checkboxs.prop("checked", false); 
		$.ajax({
			type : "GET",
			async : false,
			url : '/resource/access/permissions/' + resourceUuid,
			success : function(data) {
				console.log(data);
				if('success' == data['status'] && data['permissions'].length > 0){
					$(data['permissions']).each(function(index, element){
						$("#resourceAccessBody :checkbox[value='" + element + "']").prop("checked", true);
					})
				}
			}
		});
	
		$('#resourceAccess').modal('show');
	})
	parent.append('</br>').append(resAccess);
}

function roleClick(resUuid){
	$('#resourceAccess').modal('show');
}


function oupsModalShow(cell,resUuid,name){
	$.getJSON('/project/resource/related/oups/'+resUuid,function(data){
		if(data.status=='success'){
			$('#res_name').val(name);
			$('#res_uuid_hide').val(resUuid);
			opusControl(data,cell)
			$('#oupsModal').modal('show');
		}else{
			alert(data.message);
		}
	});
}

function initBundleClick(cell){
	var btn = $('#unbundle_oups_button');
	console.log(btn)
	btn.unbind('click');	
	btn.click(function(){
		var opus_uuid = $('#oupd_uuid_hide').val();
		if(!isBlank(opus_uuid) && confirm('确定解除绑定？')){			
			$.post('/admin/oups/remove/binding',{uuid:opus_uuid}).always(function(data){
				if(data.status == 'success'){
					opusControl({})
				}else{
					if(isBlank(data.message)){
						alert('解除綁定失败')
					}else{
						alert(data.message)
					}
				}
			})
		}
	})
}

function opusControl(data,cell){
	if(!!data.uuid){
		$('#oups_code_div').hide();
		$('#oups_total_div').show();
		$('#oups_error_div').hide();
		$('#oups_button').hide();
		showoupsdetail(data,cell);
		
	}else{
		$('#oups_code_div').show();
		$('#oups_total_div').hide();
		$('#oups_error_div').hide();
		$('#oups_button').show();
	}
	
}

//显示作品详情
function showoupsdetail(data,cell){
	$('#oups_name_label').text(data.name);
	$('#oups_code_label').text(data.codeNum);
	$('#oups_relate_label').text(data.activityName);
	$('#oups_user_label').text(data.applyUserName);
	$('#oups_category_label').text(data.categoryName);
	var send='';
	if(data.sendType==1){
		send='快递交付';
	}else if(data.sendType==2){
		send='网盘交付';
	}
	$('#oups_send_label').text(send);
	$('#oups_desc_label').text(data.desc);
	$('#oupd_uuid_hide').val(data.uuid);
	$('#unbundle_oups_button').hide();
	$('#bundled_tips').hide();
	if(data.bundled){
		if(data.resUuid == $('#res_uuid_hide').val()){
			$('#unbundle_oups_button').show()
			initBundleClick(cell);
		}else{
			$('#bundled_tips').show();
		}
	}
	
}

//资源当前状态显示
function statusCell(cell, resource) {
	var _p = $(cell);
	var value = $('');
	//test
	//console.log(resource);
	var state = resource['state'];
	//console.log(state);
	_p.empty();
	
	if(state == 0) {
		value = '<span style="color: red;">未审核</span>';
	}else if (state == 1) {
		value = '<span style="color: green;">已上线</span>';
	} else if (state == 2) {
		value = '<span style="color: #FF9933;">已下线</span>';
	}
	_p.append(value);
}

//讲者显示
function speakerCell(cell, speaker) {
	var _p = $(cell);
	_p.empty();
	if (speaker.name) {
		_p.append('<span title="' + speaker.hospitalName + "&nbsp;&nbsp;"
				+ speaker.professor + '">' + speaker.name + '</span>')
	} else {
		_p.append('<span style="color: #FF9933;">未指定</span>')
	}
}

//文本处理
function showDetail(nTd, sData){
	var markTd = $(nTd);
	markTd.empty();
//	if(sData.length > 10){
//		var markTmp = sData.substring(0,10) + "	......";
//		markTd.append(markTmp);
//	}else{
		markTd.append(sData);
//	}
//	markTd.on("click", function(){
//		$('#markDetail').text(sData);
//		$('#markModal').modal('show');
//	})
}

function loadData(){
	var resourceType=$('#searchButton_type option:selected').val();
	var resourceState=$('#searchButton_state option:selected').val();
	oTable_category.fnReloadAjax("/project/resource/categoryDeail/"+current_id+"?"+
			"type=" + resourceType + "&" +
			"state=" + resourceState
	);
}

function reloadItem(loadUI,uuid){
	
}

//点击“编辑，上线，下线”等按钮后，更新当前的页面
function refreshItem(cell, td_data, uuids) {
	renderResourceCell(cell[0], td_data['name'], uuids['resourceUuid']);
	cellCreate(cell[1], td_data['dates']);
	cellCreate(cell[2], td_data['viewCount']);
	speakerCell(cell[3], td_data['speakerVo']);
	cellCreate(cell[4], td_data['type']);
	//cellCreate(cell[5], td_data['mark']);
	statusCell(cell[5], td_data);
	oprationCell(cell[6], td_data, uuids);
}

//pdf编辑页面跳转操作
function pdfEditor(pdfUuid){
	location.href = "/project/pdf/update/page/" + pdfUuid;
}
//news编辑页面跳转操作
function newsEditor(newsUuid){
	location.href = "/project/news/update/page/" + newsUuid;
}
//video编辑页面跳转操作
function videoEditor(vodeoUuid){
	location.href = "/asset/videos/jumpUpdataPage/" + vodeoUuid;
}
//threescreen编辑页面跳转操作
function threescreenEditor(threescreenUuid){
	location.href = "/project/threescreen/update/page/" + threescreenUuid;
}

function onModalShow(resourceUuid){
	$('#tagsModel').on('show.bs.modal', function(e) {
		$("#childTagsTable :checkbox").prop("checked", false); 
		
		//显示资源已存在的标签
		showExistTags(resourceUuid);
		
		//加载父标签
		loadParentData();
	});
}

function loadParentData(){
	$.get("/tag/showParentTagListWithoutPageOption", function(){}).done(
		function(data) {
			try {
				var loader = $('#parentTagsTable');
				var body = loader.find('tbody');
				body.empty();
				var _d = $(data['aaData']);
				if (_d.length == 0) {
					body.append($('<tr><td>没有标签，请添加</td></tr>'));
				} else {
					_d.each(function(i, e) {
						if (e.name) {
							var item = $('<tr><td>' + e.name
									+ '</td></tr>');
							body.append(item);
							item.click(function() {
								loadChildData(e);
							})
						}
					})
				}
			} catch (e) {}
		}).fail(function() {}
	).always(function() {});
}

function loadChildData(e){
	var parentUuid = e.uuid_1;
	$.get("/tag/showChildTagListWithoutPageOption/" + parentUuid, function(){}).done(
		function(data) {
			//test
			//console.log(data);
			try {
				var loader = $('#childTagsTable');
				var body = loader.find('tbody');
				body.empty();
				var _d = $(data['aaData']);
				if (_d.length == 0) {
					body.append($('<tr><td colspan="2"><font color="red" size=4>没有子标签! 请添加</font></td></tr>'));
				} else {
					_d.each(function(i, e) {
						if (e.name) {
							var item = $('<tr><td><input type="checkbox" value="' + e.uuid + '" >'
										+ '</td><td>' + e.name
										+ '</td></tr>');
							body.append(item);
						}
					})
				}
			} catch (e) {}
		}).fail(function() {}
	).always(function() {});
}

function showExistTags(resourceUuid){
	$.ajax({
        type: "GET",
        async: false,	
        url: '/project/resource/labeling/showExistTags/' + resourceUuid ,
        success: function(data) {
    		if(data.length !== 0){
    			$('#existTags').empty();
    			$.each(data, function(index, element){
    				var my_index = index+1;
    				var tags_name = element['name'];
    				var tags_uuid = element['uuid'];
    				$('#existTags').append('<div class="tag label btn-info lg" style="display:inline-block;margin:0 8px 8px 0;">'
    										+'<span>' + tags_name +'</span>&nbsp;&nbsp;&nbsp;'
    										+'<a id="'+ tags_uuid +'" >'
    										+'<i class="remove glyphicon glyphicon-remove-sign glyphicon-white" ></i></a>'
    										+'</div>').append('&nbsp;&nbsp;&nbsp;');
    				if(!!tags_uuid){
    					$('#' + tags_uuid).on('click',function(){
    						//var thisTagsUuid = $(this).attr('data-id');
    						var thisTagsUuid = tags_uuid;
    						if(confirm("确定移除该标签？")){
    							dleExistResTags(thisTagsUuid, resourceUuid);
    						}
    					})
    				}
    			});
    		}else{
    			$('#existTags').empty();
    			$('#existTags').append('<font color="red" size="3">"该资源还未关联标签，请在下方添加标签"</fong>');
    		}
        }
    });
}

//删除资源对应的标签
function dleExistResTags(tagsUuid, resourceUuid){
	$.post("/project/resource/labeling/del/", {tagsUuid: tagsUuid, resourceUuid: resourceUuid}, function(data) {
		if (data.status == 'success') {
			$('#tagsModel').modal('show');
		} else {
			if (!!data.message) {
				alert(data.message)
			} else {
				alert('删除失败，请刷新页面重新尝试');
			}
		}
	});
}

function cellCreate(cell, value) {
	var c = $(cell);
	c.empty();
	c.attr("title",value);
	c.append(value);
}

function answerClick(uuid) {
	return function() {
		location.href = "/project/resource/" + uuid + "/vote/mgr";
	}
}

//js截取字符串

function cutOutData(val, num) {
	if(!val){
		return "暂无";
	}
	val = val.length > num ? val.substr(0, num) + '...' : val;
	return val;
}

function renderResourceCell(cell, resourceName, uuid){
	var c = $(cell);
	c.empty();
	resourceName=cutOutData(resourceName,25);
	var item = $('<a href="javascript:void(0);" >' + resourceName + '</a>')
	item.click(function(){
		resourceInfo(uuid);
	});
	c.append(item);
}
