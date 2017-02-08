// Custom scripts
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
	showActive([ 'project_list_nav', 'project_mgr' ]);
	$('#project_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ],
				[ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/project/datatable",
		"aoColumns" : [{
			"mData" : "project_name",
			"orderable" : false
		}, {
			"mData" : "create_date",
			"orderable" : false
		}, {
			"mData" : "apply",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				var _p = $(nTd);
				_p.empty();
				var mgr = $('<a href="#">进入</a>')
				mgr.click(function() {
					$.post('/user/hold/project',{
						uuid : sData},function() {}
					).always(function(data) {
						if ('success' == data.status) {
							/*window.location.href = '/tv/home/settings?timestamp='
									+ new Date().getTime()*/
							window.location.href = '/project/tv/home/settings/v2?timestamp='
									+ new Date().getTime()
						} else {
							alert(data.status)
						}
					})
				})
				_p.append(mgr);
				
				var item = $('<a style="margin-left:10px" href="#">删除</a>');
				item.click(function() {
					alert('无法删除');
				})
				_p.append(item);
				
				var home = $('<a style="margin-left:10px" href="#">tv首页标签</a>');
				_p.append(home);
				home.click(function(e) {
					e.preventDefault();
					$('#tv_home_mgr').modal('show');
					$('#tv_home_mgr').unbind('shown.bs.modal')
					$('#tv_home_mgr').on('shown.bs.modal',function() {
						$('#project_label').text('"'+ oData['project_name']+ '" TV首页板块管理')
						loadProjectLabels(sData);
						initSaveBtn(sData);
					})
				})
				
			}
		} ]
	});
});

function initSaveBtn(uuid) {
	var save_btn = $('#save-btn');
	save_btn.unbind('click');
	$('#save-btn').click(function(e) {
		var name = $('#label_name').val();
		if (isBlank(name)) {
			alert('请输入值板块名')
		} else {
			$.post('/project/tv/home/label/save', {
				uuid : uuid,
				name : name
			}).always(function(data) {
				if (data.status == 'success') {
					$('#label_name').val('')
					loadProjectLabels(uuid)
				} else {
					if (isBlank(data.message)) {
						alert('保存失败')
					} else {
						alert(data.message)
					}
				}
			})

		}
	})
}

function loadProjectLabels(uuid) {
	var container = $('#data-container');
	container.empty();
	var tips = $('<span id="loading_tips">加载中...</span>');
	container.append(tips)
	tips.html('加载中...')
	tips.show();
	$.get('/project/tv/home/labels', {
		uuid : uuid
	}).always(function(data) {
		//test
		console.log(data);
		
		tips.hide();
		if (data.status == 'success') {
			if (data.data && data.data.length > 0) {
				//renderUi(container, data.data)
				renderUi(container, data.data, uuid);
			} else {
				tips.show();
				tips.html('没有数据');
			}
		} else {
			if (!!data.message) {
				alert(data.message)
			} else {
				alert('获取数据失败')
			}
		}
	})
}

//function renderUi(container, data) {
function renderUi(container, data, projectUuid) {
	var table = $('<table class="table table-striped"></table>')
	var head = '<thead><tr><td class="lbname">名称</td><td class="lbdate">创建时间</td><td class="lbstatus">状态</td><td class="lbop">操作</td></tr></thead>';
	container.append(table)
	table.append(head)
	var body = $('<tbody></tbody>')
	table.append(body)
	$.each(data, function(i, e) {
		var item = $('<tr></tr>')
		body.append(item);
		//renderItem(e, item);
		console.log(i + '=====' + e + '=====' + data.length);
		renderItem(data.length, i, e, item, projectUuid);
	})
}

//function renderItem(e, tr) {
function renderItem(len, i, e, tr, projectUuid) {
	var name = '<td>' + e.name + '</td>';
	var time = '<td>' + e.createDate + '</td>';
	var status = '<td>' + (e.status == 2 ? '不可用' : '可用') + '</td>';
	var op = $('<td></td>');
	var del = $('<a>删除  </a>');
	op.append(del);
	
	//20160510 -->xm
	var cell = $('<td></td>');
	var toTop = $('<a>置顶</a>');
	if(0 != i){
		cell.append(toTop);
	}
	
	tr.append(name).append(time).append(status).append(op).append(cell);
	
	//删除操作
	del.click(function(event) {
		event.preventDefault();
		if(!confirm("确定删除?")){
			return;
		}
		$.post('/project/tv/home/label/del_super', {
			uuid : e.uuid
		}).always(function(data) {
			if (data.status == 'success') {
				tr.remove();
			} else {
				if (isBlank(data.message)) {
					alert('删除失败')
				} else {
					alert(data.message)
				}
			}
		})
	})
	
	//置顶
	toTop.click(function(event){
		event.preventDefault();
		$.post('/project/tv/home/label/toTop', {
			uuid : e.uuid
		}).always(function(data) {
			if (data.status == 'success') {
				loadProjectLabels(projectUuid);
			} else {
				if (isBlank(data.message)) {
					alert('操作失败')
				} else {
					alert(data.message)
				}
			}
		})
	})
}
