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
var wxSubject_list_table,subject_modal,confirm_subject,subject_tree,wx_subject_name,linkType,cover_div;
var current_node_uuid='';
var select_trace_text='';
$(function() {
	showActive([ 'wx_subject', 'holder_project' ]);
	
	subject_modal=$('#subject_modal');
	confirm_subject=$('#confirm_subject');
	subject_tree=$('#subject_tree');
	wx_subject_name=$('#wx_subject_name');
	linkType=$('#linkType');
	cover_div=$('#cover_div');
	// 加载table数据
	wxSubject_list_table=$('#wxSubject_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/wx/subject/list/data",
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
			"mData" : "categroyName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 50);
			}
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
				loadAddLink(nTd, sData, oData['isStatus'],oData['propertype']);
			}
		} ]
	});
	
	//加载树
	subject_tree.jstree({
	    'core': {
	        'data': {
	            'url': '/asset/category/node',
	            'data': function(node) {
	                return {
	                    'uuid': node.id,
	                    'timestamp': new Date().getTime()
	                }
	            }
	        },
	        'check_callback': true,
	        'themes': {
	            'responsive': false
	        }
	    },
	    'plugins': ['state']
	}).on('changed.jstree',
	function(e, data) {
	    if (data && data.selected && data.selected.length) {
	        var current_node = data.node;
	        if (parent != '#') {
	            //选的不是根节点
	            current_node_uuid = current_node.id;
	            select_trace_text = current_node.text;
	        } else {
	            select_trace_text = '';
	            current_node_uuid = '';
	        }
	        wx_subject_name.val(select_trace_text);
	    }
	});
	
	subject_modal.on('hide.bs.modal', function() {
		// 清除所有之前的选择
		clearElall();
	});
	
	var cover_get=$('#cover_get');
	//使用原分类
	cover_get.click(function(e){
		e.preventDefault();
		if(!current_node_uuid){
			alert('请选中一个学科');
			return false;
		}
		cover_get.prop('disabled',true);
		$.getJSON('/project/wx/subject/orgin/cover',{
			'uuid':current_node_uuid
		},function(data){
			cover_get.prop('disabled',false);
			if(data.status=='success'){
				var cover=data.message;
				if(!!cover){
					$('#cover_hidden').val(cover);
					$('#cover_preview').attr('src',cover);
					$('#cover_preview').show();
				}
			}else{
				alert(data.message);
			}
		});
	});
	
	//增加学科
	$('#addSubject').click(function(e){
		e.preventDefault();
		// 清除所有之前的选择
		clearElall();
		appendDivItem();
		confirm_subject.unbind('click');
		confirm_subject.click(function(event){
			event.preventDefault();
			if(!current_node_uuid){
				alert('请选中一个学科');
				return false;
			}
			var name=wx_subject_name.val(); 
			if(name==''){
				alert('请输入一级学科名称');
				return false;
			}
			var cover=$('#cover_hidden').val();
			if(!cover){
				alert('请上传学科封面');
				return false;
			}
			var type=linkType.val();
			$.post('/project/wx/subject/first/add/data',
					{
				'categoryUuid':current_node_uuid,
				'rootType':1,
				'name':name,
				'cover':cover,
				'type':type
					},
					function(data){
						if(data.status=='success'){
							subject_modal.modal('hide');
							wxSubject_list_table.fnDraw(false);
						}else{
							alert(data.message);
						}
					});
		});
		initUploadImage();
		subject_modal.modal('show');
	});

});

//添加上传图片
function appendDivItem(){
	var item='<input id="cover" type="file" class="form-control"  value="学科封面" />'
		+'<img id="cover_preview" alt="" src="" style="display: none;" class="pre-view" />'
		 +'<input type="hidden" name="cover" id="cover_hidden"/>';
	cover_div.append(item);
}

//初始化上传图片
function initUploadImage(){
	uploaderInit(new Part($('#cover'), 1, function(part) {
		var cover_hidden=$('#cover_hidden').val();
		if(!!cover_hidden){
		   part.renderPreview(cover_hidden);
		}
	}, function(src){
		$('#cover_hidden').val(src);
	}).init());
}


function clearElall(){
	subject_tree.jstree().deselect_all();
	wx_subject_name.val('');
	linkType.val(1);
	cover_div.empty();
	current_node_uuid='';
	select_trace_text='';
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

function loadAddLink(cell, uuid, isStatus,propertype) {
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
				refreshItem(parent.parent().children(), data);
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
				current_node_uuid=data.categroyUuid;
				select_trace_text=data.name;
				subject_tree.jstree('select_node', current_node_uuid);
				wx_subject_name.val(data.name);
				appendDivItem();
				$('#cover_hidden').val(data.cover);
				linkType.val(data.propertype);
				confirm_subject.unbind('click');
				confirm_subject.click(function(event){
					event.preventDefault();
					if(!current_node_uuid){
						alert('请选中一个学科');
						return false;
					}
					var name=wx_subject_name.val(); 
					if(name==''){
						alert('请输入一级学科名称');
						return false;
					}
					var cover=$('#cover_hidden').val();
					if(!cover){
						alert('请上传学科封面');
						return false;
					}
					var type=linkType.val();
					$.post('/project/wx/subject/update/data',
							{
						'uuid':data.uuid,
						'reflectUuid':current_node_uuid,
						'name':name,
						'cover':cover,
						'type':type
							},
							function(subData){
								if(subData.status=='success'){
									subject_modal.modal('hide');
									refreshItem(parent.parent().children(), subData);
								}else{
									alert(subData.message);
								}
							});
				});
				initUploadImage();
				subject_modal.modal('show');
			} else {
				alert(data.message);
			}
		})
	})
	parent.append(edit);
	
	//子页面
	if(propertype==1){
		var link_sub=$('<a style="margin-left:10px" href="javascript:void(0);">子页面定制</a>');
		link_sub.click(function(event){
			event.preventDefault();
			location.href='/project/wx/subject/sub/list/page/'+uuid;
		});
		parent.append(link_sub);
	}
	
	//删除
	var detele=$('<a style="margin-left:10px" href="javascript:void(0);">删除</a>');
	detele.click(function(event){
		event.preventDefault();
		if(confirm('是否删除该学科 ，其子目录也将不再显示？')){
			$.post('/project/wx/subject/delete/data',{'uuid':uuid},function(data){
				if(data.status=='success'){
					wxSubject_list_table.fnDraw(false);
				}else{
					alert(data.message);
				}
			});
		}
	});
	parent.append(detele);
	
	var setToTop = $('<a style="margin-left:10px" href="javascript:void(0);">置顶</a>');
	setToTop.click(function() {
		$.post('/project/wx/subject/recommend', {'uuid' : uuid}, function(data){
			if('success'== data['status']){
				wxSubject_list_table.fnReloadAjax("/project/wx/subject/list/data");
			} else {
				if (!!data['message']) {
					alert(data['message']);
				} else {
					alert(data.status)
				}
			}
		});
	})
	parent.append(setToTop);
}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'],50);
	renderCell(tds[1], data['createDate']);
	cutOutData(tds[2], data['categroyName'],50);
	imageShow(tds[3], data['cover']);
	statusCell(tds[4], data['isStatus']);
	loadAddLink(tds[5], data['uuid'], data['isStatus'], data['propertype']);
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

function getNodeById(id) {
	var all = subject_tree.jstree();
	return all._model.data[id];
}