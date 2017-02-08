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

$(document).ready(
		function() {
			var table;
			activeStub('collect_mgr_nav')
			var name = new Column('name', function(nTd, sData, oData) {
				nTd.empty();
				nTd.append(sData);
			});
			var createTime = new Column('createTime', function(nTd, sData,
					oData) {
				nTd.empty();
				var date = new Date(sData);
				nTd.append(date.customFormat('#YYYY#-#MM#-#DD# #hhh#:#mm#'))
			})
			var speaker = new Column('speaker.name',
					function(nTd, sData, oData) {
						// console.log(sData)
						nTd.empty();
						nTd.append(sData);
					})

			var type = new Column('type', function(nTd, sData, oData) {
				nTd.empty()
				if (sData == 'COURSE') {
					nTd.append('课程')
				} else if (sData == 'GENERAL') {
					nTd.append('合集')
				}
			})

			var mark = new Column('mark', function(nTd, sData, oData) {
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
				
				console.log(oData);
				oprationCell(nTd, sData, oData);
				//console.log(rows)
			})

			dataTable = datatable($('#collect_list_table'),
					'/project/collect/datatable', [ name, createTime, speaker,
							type, mark, _status, operator ])
		})

function oprationCell(nTd, sData, oData) {
	nTd.empty();

	// 课程/合集 更新状态
	var _status = oData['_status']
	var item = '';
	if (_status == 0) {
		item = $('<a href="javascript:void(0);">审核<a>')
	} else if (_status == 1) {
		item = $('<a href="javascript:void(0);">下线<a>')
	} else if (_status == 2) {
		item = $('<a href="javascript:void(0);">上线<a>')
	}
	item.click(function() {
		$.post('/project/collect/status', {
			uuid : oData['uuid']
		}, function() {
		}).done(function() {

		}).fail(function() {

		}).always(function(data) {
			if (data.status == 'success') {
				// console.log(data['aaData'][0]);
				// var dt = new DT(dataTable);
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
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>')
	edit.click(function() {
		if("GENERAL" == oData['type']){
			// 合集编辑
			window.location.href = "/project/collect/compilation/update/"
				+ oData['uuid'];
		} else if("COURSE" == oData['type']){
			// 课程编辑
			window.location.href = "/project/collect/course/update/"
					+ oData['uuid'];
		}
	})
	nTd.append(edit);

	// 课程/合集 删除
	var remove = $('<a style="margin-left:10px" href="javascript:void(0);">删除</a>')
	remove.click(function() {
		if (confirm("是否要删除该条数据?")) {
			$.post('/project/collect/remove', {
				uuid : oData['uuid']
			}, function() {
			}).done(function() {

			}).fail(function() {

			}).always(function(data) {
				if (data.status == 'success') {
					dataTable.table.fnReloadAjax('/project/collect/datatable');
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
	
	
	if("GENERAL" == oData['type']){
		// 合集添加资源
		var compilationRes = $('<a style="margin-left:10px" href="javascript:void(0);">资源管理</a>')
		compilationRes.click(function() {
			window.location.href = "/project/collect/compilation/resource/list/page/"
					+ oData['uuid'];
		})
		nTd.append(' | ').append(compilationRes);
	} else if("COURSE" == oData['type']){
		// 章节设置
		var chapters = $('<a style="margin-left:10px" href="javascript:void(0);">章节设置</a>')
		chapters.click(function() {
			window.location.href = "/project/collect/course/chapters/list/page/"
					+ oData['uuid'];
		})
		nTd.append(' | ').append(chapters);
	}
}