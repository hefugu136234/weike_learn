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

var listTable;

$(document).ready(function() {
	showActive([ 'games-list-nav', 'games-mgr' ]);

	listTable = $('#games_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/games/list/data",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "beginDate",
			"orderable" : false
		}, {
			"mData" : "endDate",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 20);
			}
		}, {
			"mData" : "currentStatus",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onCurrentStatusCellCreate(nTd, oData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onUuidCellCreate(nTd, oData);
			}
		}]
	});
	
	//点击按钮复制文字的剪切版插件
	var client = new ZeroClipboard(document.getElementById("getLotteryUrl"), {
		moviePath: "ZeroClipboard.swf"
	});
	client.on("aftercopy", function(event, args) {
		alert('已将链接复制到剪切板！');
		$('#lotteryUrlModal').modal('hide');
	});
});

function onCurrentStatusCellCreate(cell, oData) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (oData['currentStatus'] == 2) {
		value = '<span style="color: #FF9933;">已下线</span>';
	} else if (oData['currentStatus'] == 1) {
		value = '<span style="color: green;">已上线</span>';
	} else if (oData['currentStatus'] == 0) {
		value = '<span style="color: red;">未审核</span>'
	}
	_p.append(value);
}

function onUuidCellCreate(cell, oData) {
	var parent = $(cell);
	parent.empty();
	
	//游戏上下线
	var item = '';
	if (oData['currentStatus'] == 2) {
		item = $('<a href="javascript:void(0);">上线</a>');
	} else if (oData['currentStatus'] == 1) {
		item = $('<a href="javascript:void(0);">下线</a>');
	} else if (oData['currentStatus'] == 0) {
		item = $('<a href="javascript:void(0);">审核</a>');
	}
	item.click(function(event) {
		event.preventDefault();
		//if(confirm("是否确认执行该操作")){
			$.post('/project/games/update/status', {
				uuid : oData['uuid']
			}, function(data) {
				if (data['status'] == 'success') {
					refreshItem(parent.parent().children(), data['data']);
				} else {
					if (!!data.message) {
						alert(data.message);
					} else {
						alert('状态修改失败');
					}
				}
			});
		//}
	});
	parent.append(item).append(' | ');
	
	//展示页面配置	 老版页面设置
	/*var pageSet = $('<a href="javascript:void(0);">页面</a>');
	pageSet.click(function(event) {
		event.preventDefault();
		location.href = "/project/games/pageConfig/" + oData['uuid'];
	});
	parent.append(pageSet).append(' | ');*/

	// 编辑
	var edit = $('<a href="javascript:void(0);">编辑</a>');
	edit.click(function(event) {
		event.preventDefault();
		location.href = "/project/games/update/page/" + oData['uuid'];
	});
	parent.append(edit).append(' | ');
	
	// 生成链接
	var gamesUrl = $('<a href="javascript:void(0);">URL</a>');
	gamesUrl.click(function(event) {
		event.preventDefault();
		$.post('/project/games/viewUrl', {
			uuid : oData['uuid']
		}, function(data) {
			if (data['status'] == 'success') {
				var url = data['data']['url'];
				if(!! url){
					$('#fe_text').text(url);
					$('#lotteryUrlModal').modal('show');
				}
			} else {
				if (!!data.message) {
					alert(data.message);
				} else {
					alert('状态修改失败');
				}
			}
		});
	});
	parent.append(gamesUrl).append(' | ');
	
	//删除游戏
	var delButton = $('<a href="javascript:void(0);">删除</a>');
	delButton.click(function(event) {
		event.preventDefault();
		if(confirm("你确定要删除该条记录吗")){
			$.post('/project/games/remove', {
				uuid : oData['uuid']
			}, function(data) {
				if (data['status'] == 'success') {
					listTable.fnReloadAjax("/project/games/list/data");
				} else {
					if (!!data.message) {
						alert(data.message);
					} else {
						alert('状态修改失败');
					}
				}
			});
		}
	});
	parent.append(delButton);
}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'], 20);
	renderCell(tds[1], data['createDate']);
	renderCell(tds[2], data['beginDate']);
	renderCell(tds[3], data['endDate']);
	cutOutData(tds[4], data['mark'], 20);
	onCurrentStatusCellCreate(tds[5], data);
	onUuidCellCreate(tds[6], data);
}

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}
