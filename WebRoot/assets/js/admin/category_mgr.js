$(document).ready(function() {
    showActive(['assets-mgr', 'category_mgr_nav']);

    var tree_selected_sign = $('#tree_selected_sign');
    var save_btn = $('#save_btn');
    var current_text;
    var foo_add = $('#foo_add');
    var foo_edit = $('#foo_edit');
    var foo_del = $('#foo_del');
    var cover_edit = $('#cover_edit');
    var cateExpands_data = [];
    var cateExpands_data_oldTmp = [];
    var cateExpands_data_final = [];
    var cateExpands_data_newTmp_typeId = [];
    var plantCount;

    foo_del.bind('click',
    function() {
        if ( !! current_text) {
            var result = confirm('是否将 "' + current_text + '" 删除！');
        } else {
            alert("请先选择一个分类!!");
        }
        if (result) {
            $.post('/delete/category/node', {
                'uuid': current_id
            },
            function(data) {
                if (data.status == 'success') {
                    $('#tree').jstree('delete_node', current_id);
                    alert('删除成功');
                } else {
                    if ( !! data.message) {
                        alert(data.message);
                    } else {
                        alert('数据删除出错');
                    }
                }
            });
        }
    });

    $('#tree').jstree({
        'core': {
            'data': {
                url: '/asset/category/node',
                data: function(node) {
                    return {
                        'uuid': node.id,
                        'timestamp': new Date().getTime()
                    }
                }
            },
            check_callback: true,

            themes: {
                'responsive': false
            },
            callback: {
                onopen_all: function() {
                    log("Open ALL");
                }
            }

        },
        'plugins': ['state']
    }).on('changed.jstree',
    function(e, data) {
        save_btn.unbind('click');
        if (data && data.selected && data.selected.length) {
            tree_selected_sign.empty();
            var current_node = data.node;
            $(data.node.parents).each(function(i, e) {
                var node = getNodeById(e);
                if (e != '#') {
                    tree_selected_sign.prepend(node.text + " > ");
                }
            });
            tree_selected_sign.append($('<b>' + current_node.text + '</b>'));
            current_id = current_node.id;
            current_text = current_node.text;
            parent_id = current_node.parent;
            var oplet = current_node.original;
            if (oplet.addable) {
                foo_add.fadeIn();
            } else {
                foo_add.fadeOut();
            }
            if (oplet.editable) {
                foo_edit.fadeIn();
                cover_edit.fadeIn();
                foo_del.fadeIn();
            } else {
                foo_edit.fadeOut();
                cover_edit.fadeOut();
                foo_del.fadeOut();
            }
            // if (oplet.deletable) {
            // foo_del.fadeIn();
            // } else {
            // foo_del.fadeOut();
            // }
            /*
			var tmp = getNodeById(current_id)
			if("#" == tmp.parent){
				showCategoryDetail("all");
			}else{
				showCategoryDetail(current_id);
			}*/
            showCategoryDetail(current_id);
        } else {
            $('#data .content').hide();
            //$('#data .default').html('Select a file from the tree.').show();
        }
    });

    //TODO: Clean All Node 
    $('#tree').jstree().deselect_all();

    //给每次点击发起查询相应分类请求
    function showCategoryDetail(categoryUuid) {
        var resourceType = $('#searchButton_type option:selected').val();
        var resourceState = $('#searchButton_state option:selected').val();
        //oTable.fnReloadAjax("/project/resource/categoryDeail/"+categoryUuid);
        oTable_category.fnReloadAjax("/project/resource/categoryDeail/" + categoryUuid + "?type=" + resourceType + "&state=" + resourceState);
    }

    $('#categoryModal').on('show.bs.modal', function(e) {
        $('#save_btn').unbind('click');
        var text = $('#category_name');
        if (e.relatedTarget.id == 'foo_add') {
            text.val('');
            $('#save_btn').click(function() {
                $.post('/asset/category/node/save', {
                    parent_uuid: current_id,
                    categoryName: text.val()
                }).always(function(e) {
                    alert(e.status);
                    if (e.status == 'success') modal_always();
                })
            })
        } else if (e.relatedTarget.id == 'foo_edit') {
            text.val(getNodeById(current_id).text);
            $('#save_btn').click(function() {
                $.post('/asset/category/node/update', {
                    uuid: current_id,
                    categoryName: text.val()
                }).always(function(e) {
                    alert(e.status);
                    if (e.status == 'success') modal_always();
                })
            })
        }
    });

    function modal_always() {
        $('#categoryModal').modal('hide');
        $('#tree').jstree('refresh');
    }

    // 上传图片的初始化
    uploaderInit(new Part($('#cover_uploadify'), 1,
    function(part) {
        var cover_hidden = $('#coverTaskId').val();
        if ( !! cover_hidden) {
            part.renderPreview(cover_hidden);
        }
    },
    function(src) {
        $('#coverTaskId').val(src);
    }).init());

    /*$('#cover_uploadify').uploadify({
		'swf' : '/assets/js/uploadify/uploadify.swf',
		'uploader' : 'http://cloud.lankr.cn/api/image/upload',
		'formData' : {
			'appKey' : 'ff7a9de914595ec790dbf5b32ab46e9a'
		},
		'fileObjName' : 'file',

		'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
		'method' : 'post',
		'fileSizeLimit' : 10000,
		'buttonText' : '选择封面',
		'auto' : true,
		'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
		'onUploadSuccess' : function(file, data, response) {
			var json_data = JSON.parse(data);
			showImage(json_data.taskId);
		},
	});*/

    $('#coverModal').on('show.bs.modal', function() {
    	$('#plam_select').val('1');
    	var plantCount_tmp = 0;
        $('#cover_category_name').text(current_text);
        
        plam_select = $(this).find("#plam_select option");
        plam_select.each(function(index, item) {
            $(item).click(function() {
                var typeId = $(this).val();
                radioClick(cateExpands_data_final, typeId);
            });
            plantCount_tmp++;
        });
        plantCount = plantCount_tmp;
        
        loadData();
    });
    
    function loadData(){
    	$('#plam_select').val('1');
    	//加载封面图片数据,默认显示微信平台图片
        $.getJSON('/project/category/relate/cover_new/' + current_id, function(data) {
            if (data.status == 'success') {
            	//如果新表中存在所有数据，用新表数据渲染页面
            	if(data.cateExpands.length == plantCount){
            		cateExpands_data = [];
            		cateExpands_data = data.cateExpands;
                    radioClick(cateExpands_data, 1);
            	}else{
            		cateExpands_data = [];
            		cateExpands_data_newTmp_typeId = [];
            		cateExpands_data = data.cateExpands;
            		$.each(cateExpands_data, function(index, element) {
                		cateExpands_data_newTmp_typeId.push(element['typeId']);
                    });
            		$.getJSON('/project/category/relate/cover/' + current_id, function(data) {
                        if (data.status == 'success') {
                        	cateExpands_data_oldTmp = data.cateExpands;
                        	//合并两个数组的数据，用旧表的数据填充新表中没有的数据
                        	cateExpands_data_final = mergeDate(cateExpands_data_oldTmp);
                        	radioClick(cateExpands_data_final, 1);
                        } else {
                            if ( !! data.message) {
                                alert(data.message);
                            } else {
                                alert('数据加载出错');
                            }
                        }
                    });
            	}
            } else {
            	if ( !! data.message) {
                    alert(data.message);
                } else {
                    alert('数据加载出错');
                }
            }
        });
    }
    
    function mergeDate(cateExpands_data_oldTmp){
    	$.each(cateExpands_data_oldTmp, function(index, element) {
    		typeId_old = element['typeId'];
    		if(-1 == $.inArray(typeId_old, cateExpands_data_newTmp_typeId)){
    			cateExpands_data.push(element);
    		}
        });
    	
    	return cateExpands_data;
    }

    $('#coverModal').on('hide.bs.modal', function() {
        $('#cover_category_name').text('');
        hideImage();
    });

    $('#cover_save').click(function() {
    	var plam_selected = $('#plam_select').find("option:selected").val(); 
     	var type_selected = $('#type_select').find("option:selected").val();
     	var cssValue = $('#css').val();
     	
        if (plam_selected == '') {
            alert('请选中一个平台');
            return;
        }
    	if('pic' === type_selected){
    		var coverTaskId = $('#coverTaskId').val();
	        var cover_src = $('#cover_preview').attr('src');
	        if (cover_src == '' || coverTaskId == '') {
	            alert('请先上传封面图片');
	            return;
	        }
    	}else if('css' === type_selected){
    		if (cssValue == '') {
                alert('请提供填写样式');
                return;
            }
    	}
        
        // 提交数据
    	$.post('/project/category/cover/save', {
            typeId: plam_selected,
            categoryUuid: current_id,
            taskId: coverTaskId,
            cssValue: cssValue
        }, function(data) {
            if (data.status == 'success') {
            	alert('保存成功');
            	loadData();
            } else {
                if ( !! data.message) {
                    alert(data.message);
                } else {
                    alert('数据保存出错');
                }
            }
        });
    });
    
    $('#plam_select').change(function(){
    	var plam_selected = $('#plam_select').find("option:selected").val();
    	radioClick(cateExpands_data_final, plam_selected);
    })
    
    $('#type_select').change(function(){
    	var _obj;
    	var plam_selected = $('#plam_select').find("option:selected").val(); 
    	var type_selected = $('#type_select').find("option:selected").val();
    	$.each(cateExpands_data_final, function(i, m) {
            var m_typeId = m.typeId;
            if (m_typeId == plam_selected) {
                _obj = m;
                return true;
            }
        });
    	if('pic' === type_selected){
    		$('#pic').removeClass('hide');
    		$('#style').addClass('hide');
    		if(!!_obj && !!_obj['taskId']){
    			showImage(_obj['taskId']);
    		}else{
    			hideImage();
    		}
    	}else if('css' === type_selected){
    		$('#style').removeClass('hide');
    		$('#pic').addClass('hide');
    		if(!!_obj && !!_obj['text']){
    			showCssValue(_obj['text']);
    		}else{
    			showCssValue('');
			}
    	}
    })
});

function radioClick(cateExpands_data, typeId) {
    var obj;
    var type_select_button = $('#type_select');
    
    $.each(cateExpands_data, function(i, m) {
        var m_typeId = m.typeId;
        if (m_typeId == typeId) {
            obj = m;
            return true;
        }
    });
	if(!!obj && !!obj['taskId']){
		type_select_button.val('pic');
		$('#pic').removeClass('hide');
		$('#style').addClass('hide');
		showImage(obj['taskId']);
	}else if(!!obj && !!obj['text']){
		type_select_button.val('css');
		$('#style').removeClass('hide');
		$('#pic').addClass('hide');
		showCssValue(obj['text']);
	}else{
		type_select_button.val('pic');
		$('#pic').removeClass('hide');
		$('#style').addClass('hide');
		hideImage();
	}
}

// 显示图片
function showImage(taskId) {
    $('#cover_preview').show();
    $('#cover_preview').attr('src', coverShow(taskId));
    $('#coverTaskId').val(taskId);
}

var patt = new RegExp("^(http).*");
function coverShow(cover) {
    var img_src = cover;
    if (!patt.test(cover)) {
        img_src = "http://cloud.lankr.cn/api/image/" + cover + "?m/2/h/180/f/jpg";
    }
    return img_src;
}

// 隐藏图片
function hideImage() {
    $('#cover_preview').hide();
    $('#cover_preview').attr('src', "");
    $('#coverTaskId').val('');
}

function getNodeById(id) {
    var all = $('#tree').jstree();
    return all._model.data[id];
}

function showCssValue(text){
	var cssArea = $('#css');
	cssArea.val(text);
}