$.fn.dataTableExt.oApi.fnReloadAjax = function(oSettings, sNewSource,
		fnCallback, bStandingRedraw) {
	if (sNewSource != undefined && sNewSource != null) {
		oSettings.sAjaxSource = sNewSource;
	}
	// Server-side processing should just call fnDraw
	if (oSettings.oFeatures.bServerSide) {
		this.fnDraw();
		return;
	}
	this.oApi._fnProcessingDisplay(oSettings, true);
	var that = this;
	var iStart = oSettings._iDisplayStart;
	var aData = [];
	this.oApi._fnServerParams(oSettings, aData);
	oSettings.fnServerData.call(oSettings.oInstance, oSettings.sAjaxSource,
			aData, function(json) {
				/* Clear the old information from the table */
				that.oApi._fnClearTable(oSettings);
				/* Got the data - add it to the table */
				var aData = (oSettings.sAjaxDataProp !== "") ? that.oApi
						._fnGetObjectDataFn(oSettings.sAjaxDataProp)(json)
						: json;
				for (var i = 0; i < aData.length; i++) {
					that.oApi._fnAddData(oSettings, aData[i]);
				}
				oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
				that.fnDraw();
				if (bStandingRedraw === true) {
					oSettings._iDisplayStart = iStart;
					that.oApi._fnCalculateEnd(oSettings);
					that.fnDraw(false);
				}
				that.oApi._fnProcessingDisplay(oSettings, false);
				/* Callback user function - for event handlers etc */
				if (typeof fnCallback == 'function' && fnCallback !== null) {
					fnCallback(oSettings);
				}
			}, oSettings);
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

var dataTable;
var courseUuid;

$(document).ready(
		function() {
		
			
			activeStub('collect_mgr_nav');
			
			courseUuid = $('#courseUuid').val();
			var table;
			
			var name = new Column('name', function(nTd, sData, oData){
				nTd.empty();
				nTd.append(sData);
			});
			var createTime = new Column('createTime', function(nTd, sData,
					oData) {
				nTd.empty();
				var date = new Date(sData);
				nTd.append(date.customFormat('#YYYY#-#MM#-#DD# #hhh#:#mm#'))
			})
			
			var passScore = new Column('passScore', function(nTd, sData, oData){
				nTd.empty();
				nTd.append(sData);
			});
			
			var mark = new Column('mark', function(nTd, sData, oData){
				nTd.empty();
				nTd.append(sData);
			})
			var _status = new Column('_status', function(nTd, sData, oData) {
				nTd.empty();
				if (sData == 0) {
					nTd.append('<span style="color:red">未审核</span>')
				} else if (sData == 1) {
					nTd.append('<span style="color:green">已上线</span>')
				} else {
					nTd.append('<span style="color:yellow">已下线</span>')
				}
			})
			
			var operator = new Column('uuid', function(nTd, sData, oData, iRow,
					iCol, rows) {
				oprationCell(nTd, sData, oData, rows);
			})
			
			dataTable = datatable($('#collect_chapters_list_table'),
					'/project/collect/course/chapters/list/data/' + courseUuid, [ name, createTime, passScore,
							mark, _status, operator ]);
			


		$('#questionnaire_modal').on('hide.bs.modal', function() {
			console.log('hide');
			$(this).data('uuid', '');
			$('.pre-view').attr('src','');
			$('#collect_name').html('');
			$('#questionnaire_name').val('');
			$('#collect_num').val('');
			$('#collect_time').val('');
			$('#mark').val('');
			$('#select_div').empty();
		});
		
		$('#collect_questionnaire').click(function(event) {
			event.preventDefault();
			// 点击事件
			var uuid = $('#questionnaire_modal').data('uuid');
			// 点击的请求
			var questionnaire_select=$('#questionnaire_select').val();
			if(questionnaire_select==''||questionnaire_select==0){
				alert('请选择试题');
				return false;
			}
			var name = $('#questionnaire_name').val();
			if (!name) {
				alert('请填写试卷名称');
				return false;
			}
			var cover = $('#cover').val();
			if(!cover){
				alert('请上传封面图');
				return false;
			}
	
			var mark = $('#mark').val();
			if(!mark){
				alert('请填写试卷简介');
				return false;
			}
			var collectNum = $('#collect_num').val();
			if(!collectNum||isNaN(collectNum)){
				alert('请填写试题数据，整数');
				return false;
			}
			collectNum=parseInt(collectNum);
			var collectTime = $('#collect_time').val();
			if(!collectTime||isNaN(collectTime)){
				alert('请填写答题时间，整数');
				return false;
			}
			collectTime=parseInt(collectTime);
			
			$.post('/project/collect/course/chapters/update/questionnaire',
					{'uuid':uuid,
				    'questUuid':questionnaire_select,
				    'picUrl':cover,
				    'name':name,
				    'mark':mark,
				    'collectNum':collectNum,
				    'collectTime':collectTime
				    },function(data){
						//console.log(data);
				if(data.status=='success'){
					$('#questionnaire_modal').modal('hide');
				}else{
					alert(data.message);
				}
			});
		});
		
		// 封面上传(cover)
		var $e = $('#cover-view');
		uploaderInit(new Part($e, 1, function(part) {
		}, function(src) {
			$('#cover').val(src);
		}).init());
});

function initModalData(data) {
	$('#collect_name').val(data.name);
	//$('#record_url').val(data.resourceUrl);
	var questionnaire_select = $('#questionnaire_select');
	buildSelect(data.optionAdditions);
	var select = data.selectItem;
	var picUrl = data.picUrl;
	var name = data.questionnaireName;
	var mark = data.questionnaireMark;
	var collectTime=data.collectTime;
	var collectNum=data.collectNum;
	if (!!picUrl) {
		$('.pre-view').attr('src',picUrl);
		$('#cover').val(picUrl)
	}
	if (!!name) {
		$('#questionnaire_name').val(name);
	}
	if (!!mark) {
		$('#mark').val(mark);
	}
	if(collectNum>0){
		$('#collect_num').val(collectNum);
	}
	if(collectTime>0){
		$('#collect_time').val(collectTime);
	}
	
	if (!!select) {
		questionnaire_select.val(select);
	} else {
		questionnaire_select.get(0).selectedIndex = 0;
	}
	questionnaire_select.ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/project/questionnaire/search'
	}, {
		loadingImg : '/assets/img/loading.gif'
	});
	$('#questionnaire_select_chosen').css('width', '100%');
}

function buildSelect(list) {
	var questionnaire_select = $('#questionnaire_select');
	if (!!list && list.length > 0) {
		$.each(list, function(index, item) {
			var option = buildOption(item);
			questionnaire_select.append(option);
		});
	}
}

function buildOption(addition) {
	var option = '<option value="' + addition.id + '">' + addition.text
			+ '</option>';
	return option;
}


		
function oprationCell(nTd, sData, oData, rows) {
	nTd.empty();

	// 更新状态
	var _status = oData['_status']
	var item = '';
	if (_status == 0) {
		item = $('<a class="btn btn-success btn-xs" href="javascript:void(0);">审核<a>')
	} else if (_status == 1) {
		item = $('<a class="btn btn-success btn-xs" href="javascript:void(0);">下线<a>')
	} else if (_status == 2) {
		item = $('<a class="btn btn-success btn-xs" href="javascript:void(0);">上线<a>')
	}
	item.click(function() {
		$.post('/project/collect/course/chapters/status', {
			uuid : oData['uuid']
		}, function() {
		}).done(function() {

		}).fail(function() {

		}).always(function(data) {
			if (data.status == 'success') {
				dataTable.refreshItem(data['aaData'][0]);
			} else {
				if (!!data['message']) {
					alert(data['message']);
				} else {
					alert(data.status)
				}
			}
		})
	})
	nTd.append(item);

	// 编辑
	var edit = $('<a style="margin-left:10px" class="btn btn-success btn-xs" href="javascript:void(0);">编辑</a>')
	edit.click(function() {
		window.location.href = "/project/collect/course/chapters/update/"
				+ oData['uuid'];
	})
	nTd.append(edit);

	// 删除
	var remove = $('<a style="margin-left:10px" class="btn btn-success btn-xs" href="javascript:void(0);">删除</a>')
	remove.click(function() {
		if (confirm("是否要删除该章节?")) {
			$.post('/project/collect/course/chapters/remove', {
				uuid : oData['uuid']
			}, function() {
			}).done(function() {

			}).fail(function() {

			}).always(function(data) {
				if (data.status == 'success') {
					dataTable.table.fnReloadAjax('/project/collect/course/chapters/list/data/' + courseUuid);
				} else {
					if (!!data['message']) {
						alert(data['message']);
					} else {
						alert(data.status)
					}
				}
			})
		}
	})
	nTd.append(remove);
	
	// 资源
	var resource = $('<a class="btn btn-success btn-xs" href="javascript:void(0);">资源</a>')
	resource.click(function() {
		window.location.href = "/project/collect/course/chapters/resource/list/page/"
				+ oData['uuid'];
	})
	nTd.append(resource);
	
	//问卷
	var questionnaire = $('<a style="margin-left:10px" class="btn btn-success btn-xs" href="javascript:void(0);">试题</a>');
	questionnaire.click(function(e){
		e.preventDefault();
		//添加select
		var select_val='<select class="form-control" id="questionnaire_select">'
		                +'<option value="">请输入"试卷名称"检索资源</option>'
		                +'</select>';
		$('#select_div').append(select_val);
		$('#questionnaire_modal').data('uuid', oData['uuid']);
		// 加载数据所做的操作
		$.getJSON('/project/collect/course/chapters/select/questionnaire', {
			'uuid' : oData['uuid']
		}, function(data) {
			if (data.status == 'success') {
				console.log(data);
				initModalData(data);
				$('#questionnaire_modal').modal('show');
			} else {
				alert(data.message);
			}
		});
		
	});
	
	nTd.append(questionnaire);
	// 置顶
	var top = $('<a style="margin-left:10px" class="btn btn-success btn-xs" href="javascript:void(0);">置顶</a>');
	top.click(function() {
		$.post('/project/collect/course/chapters/top', {
			uuid : oData['uuid']
		}, function() {
		}).done(function() {

		}).fail(function() {

		}).always(function(data) {
			if (data.status == 'success') {
				dataTable.table.fnReloadAjax('/project/collect/course/chapters/list/data/' + courseUuid);
			} else {
				if (!!data['message']) {
					alert(data['message']);
				} else {
					alert(data.status)
				}
			}
		})
	})
	
	nTd.append(' | ');
	nTd.append(top);
}