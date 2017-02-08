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
var compilationUuid;

$(document).ready(
		function() {
			activeStub('collect_mgr_nav');

			compilationUuid = $('#compilationUuid').val();

			var name = new Column('name', function(nTd, sData, oData) {
				nTd.empty();
				nTd.append(sData);
			});
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
				oprationCell(nTd, sData, oData, rows);
			})

			dataTable = datatable($('#collect_compilation__res_list_table'),
					'/project/collect/compilation/resource/list/data/'
							+ compilationUuid, [ name, mark, _status, operator ])
		})

function oprationCell(nTd, sData, oData, rows) {
	nTd.empty();

	// 更新状态
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
		$.post('/project/collect/compilation/resource/status', {
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
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>')
	edit.click(function() {
		window.location.href = "/project/collect/compilation/resource/update/page/"
				+ oData['uuid'];
	})
	nTd.append(edit);

	// 删除
	var remove = $('<a style="margin-left:10px" href="javascript:void(0);">删除</a>')
	remove.click(function() {
		if (confirm("是否要删除该资源?")) {
			$.post('/project/collect/compilation/resource/remove', {
						uuid : oData['uuid']
				}, function() {
				})
				.done(function() {

				})
				.fail(function() {

				})
				.always(function(data) {
					if (data.status == 'success') {
						dataTable.table
								.fnReloadAjax('/project/collect/compilation/resource/list/data/'
										+ compilationUuid);
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
	
	// 置顶
	var top = $('<a style="margin-left:10px" href="javascript:void(0);">置顶</a>');
	top.click(function() {
		$.post('/project/collect/compilation/resource/top', {
			uuid : oData['uuid']
		}, function() {
		}).done(function() {

		}).fail(function() {

		}).always(function(data) {
			if (data.status == 'success') {
				dataTable.table
				.fnReloadAjax('/project/collect/compilation/resource/list/data/'
						+ compilationUuid);
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