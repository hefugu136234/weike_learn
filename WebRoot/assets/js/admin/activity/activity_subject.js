var subject_modal,confirm_subject,subject_tree;
var current_node_uuid='';
var select_trace_text='';
function initDataSubject(){
	var uuid=$('#uuid').val();
	subject_modal=$('#subject_modal');
	confirm_subject=$('#confirm_subject');
	subject_tree=$('#subject_tree');
	
	$('#addSubject').click(function(e){
		e.preventDefault();
		// 清除所有之前的选择
		clearElall();
		confirm_subject.unbind('click');
		confirm_subject.click(function(event){
			event.preventDefault();
			if(!current_node_uuid){
				alert('请选中一个学科');
				return false;
			}
			var activity_sub_name=$('#activity_sub_name').val(); 
			if(activity_sub_name==''){
				alert('请输入活动学科名称');
				return false;
			}
			$.post('/admin/activity/subject/add/data',
					{
				'activityUuid':uuid,
				'cateUuid':current_node_uuid,
				'cateNickName':activity_sub_name
					},
					function(data){
						if(data.status=='success'){
							subject_modal.modal('hide');
							$('#subject_list_table').dataTable().fnDraw(false);
						}else{
							alert(data.message);
						}
					});
		});
		subject_modal.modal('show');
	});
	
	// 加载table数据
	$('#subject_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/activity/subject/list/"+uuid,
		"aoColumns" : [ {
			"mData" : "categoryName",
			"orderable" : false
		}, {
			"mData" : "name",
			"orderable" : false
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				loadAddLink(nTd, sData,uuid);
			}
		} ]
	});
	
	subject_modal.on('hide.bs.modal', function() {
		// 清除所有之前的选择
		clearElall();
	});
	
//	subject_modal.on('show.bs.modal', function() {
//		// 清除所有之前的选择
//		//subject_tree.jstree().open_all();
//		if(!!current_node_uuid){
//			subject_tree.jstree('select_node', current_node_uuid);
//		}
//	});
	
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
	        $('#activity_sub_name').val(select_trace_text);
	    }
	});
	
	
	
}

function clearElall(){
	subject_tree.jstree().deselect_all();
	$('#activity_sub_name').val('');
	current_node_uuid='';
	select_trace_text='';
}

function getNodeById(id) {
	var all = subject_tree.jstree();
	return all._model.data[id];
}

function loadAddLink(cell, uuid,activity_uuid) {
	var parent = $(cell);
	parent.empty();
	var item = item = $('<a href="javascript:void(0);">编辑</a>');
	item.click(function(event) {
		event.preventDefault();
		$.getJSON('/admin/activity/subject/update/page', {
			'uuid' : uuid,
		}, function(data) {
			if (data.status == 'success') {
				current_node_uuid=data.categoryUuid;
				select_trace_text=data.name;
				subject_tree.jstree('select_node', current_node_uuid);
				$('#activity_sub_name').val(data.name);
				subject_modal.data('uuid',data.uuid);
				confirm_subject.unbind('click');
				confirm_subject.click(function(event){
					event.preventDefault();
					if(!current_node_uuid){
						alert('请选中一个学科');
						return false;
					}
					var activity_sub_name=$('#activity_sub_name').val(); 
					if(activity_sub_name==''){
						alert('请输入活动学科名称');
						return false;
					}
					$.post('/admin/activity/subject/update',
							{
						'uuid':subject_modal.data('uuid'),
						'cateUuid':current_node_uuid,
						'cateNickName':activity_sub_name
							},
							function(subdata){
								if(subdata.status=='success'){
									subject_modal.modal('hide');
									refreshItem(parent.parent().children(), subdata,activity_uuid);
								}else{
									alert(subdata.message);
								}
							});
				});
				subject_modal.modal('show');
			} else {
				alert(data.message);
			}
		});

	});
	parent.append(item);

	// 删除
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">删除</a>');
	edit.click(function(event) {
		event.preventDefault();
		if(confirm('确定要删除吗？')){
			$.post('/admin/activity/subject/detele',
					{
				'uuid':uuid
					},
					function(data){
						if(data.status=='success'){
							$('#subject_list_table').dataTable().fnDraw(false);
							//parent.parent().remove();
						}else{
							alert(data.message);
						}
					});
		}
	});
	parent.append(edit);
}

function refreshItem(tds, data) {
	renderCell(tds[0], data['categoryName']);
	renderCell(tds[1], data['name']);
	loadAddLink(tds[2], data['uuid'], activity_uuid);
}
function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}