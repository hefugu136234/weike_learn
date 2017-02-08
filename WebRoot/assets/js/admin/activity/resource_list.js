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

var oTable;
var activityUuid;
$(document).ready(function(){
	activityUuid = $('#activityUuid').val();
	// 加载table数据
	oTable = $('#activity_reslist').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/activity/reslist/data/" + activityUuid,
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				fnCallback(json)
			});
		},
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//test
				console.log(oData);
				
				//showDetail(nTd, sData)
				renderResourceCell(nTd, sData, oData['resourceUuid']);
			}
		}, {
			"mData" : "speaker",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				speakerCell(nTd, sData)
			}
		}, {
			"mData" : "type",
			"orderable" : false
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "modifyDate",
			"orderable" : false
		}, {
			"mData" : "viewCount",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData)
			}
		}, {
			"mData" : "status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData)
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				oprationCell(nTd, oData, sData)
			}
		} ]
	});
});

//资源操作
function oprationCell(cell, td_data, uuid) {
	var parent = $(cell);
	parent.empty();
	var stateOperation = '';
	var state = td_data['status'];
	var recommendDate = td_data['recommendDate'];
	var setToTop = '';
	
	if (state == '2'){
		//如果当前的状态是2（已下线），则可以进行上线操作
		stateOperation = $('<a href="#">上线</a>');
	} else if (state == '1'){
		//如果当前的状态是1（已上线），则可以进行下线操作
		stateOperation = $('<a href="#">下线</a>');
	} else if (state == '0'){
		//如果当前状态是0（未审核），则可以进行审核操作
		stateOperation = $('<a href="#">审核</a>');
	} else{
		stateOperation = $('<font color="red">???</font>');
	}
	
	$(stateOperation).bind("click", function(e){
		e.preventDefault();
		if(confirm("你确定要执行该操作吗")){
			$.post('/admin/activity/res/updateState', {uuid: uuid, status: state}).always(function(data) {
				if (data.status == 'success') {
					//test
					console.log(data);
					refreshItem(parent.parent().children(), data['data']);
				} else {
					if (!!data.message) {
						alert(data.message)
					} else {
						alert(data.status)
					}
				}
			})
		}
	});
	parent.append(stateOperation).append(" | ");
	
	var del = $('<a href="javascript:void(0);">移除</a>')
	del.click(function() {
		if(confirm("你确定要删除该资源吗?")){
			$.post('/admin/activity/resmgr/del/', {uuid : uuid}, function(data){
				//test
				console.log(data);
				if('success'== data['status']){
					oTable.fnReloadAjax("/admin/activity/reslist/data/" + activityUuid);
				} else {
					alert(data['message']);
				}
			});
		}
	})
	parent.append(del).append(" | ");
	
	if('' == recommendDate){
		setToTop = $('<a href="javascript:void(0);">置顶</a>');
	}else{
		setToTop = $('<a href="javascript:void(0);">取消置顶</a>');
	}
	
	setToTop.click(function() {
		$.post('/admin/activity/resource/recommend', {uuid : uuid}, function(data){
			if('success'== data['status']){
				refreshItem(parent.parent().children(), data['data']);
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

//资源当前状态显示
function statusCell(cell, status) {
	var _p = $(cell);
	_p.empty();
	var value = $('');
	if(status == 0) {
		value = '<span style="color: red;">未审核</span>';
	}else if (status == 1) {
		value = '<span style="color: green;">已上线</span>';
	} else if (status == 2) {
		value = '<span style="color: #FF9933;">已下线</span>';
	}
	_p.append(value);
}

//讲者显示
function speakerCell(cell, speaker) {
	var _p = $(cell);
	_p.empty();
	if (speaker) {
		_p.append(speaker);
	} else {
		_p.append('<span style="color: #FF9933;">未指定</span>')
	}
}

//文本处理
function showDetail(nTd, sData){
	var markTd = $(nTd);
	markTd.empty();
	if(sData.length > 10){
		var markTmp = sData.substring(0,10) + "	......";
		markTd.append(markTmp);
	}else{
		markTd.append(sData);
	}
	markTd.on("click", function(){
		$('#markDetail').text(sData);
		$('#markModal').modal('show');
	})
}

function refreshItem(cell, td_data){
	statusCell(cell[7], td_data['status']);
	oprationCell(cell[8], td_data, td_data['uuid']);
	cellReflsh(cell[4], td_data['modifyDate']);
}

function cellReflsh(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function renderResourceCell(cell, resourceName, uuid){
	var c = $(cell);
	c.empty();
	var item = $('<a href="#" >' + resourceName + '</a>')
	item.click(function(){
		resourceInfo(uuid)
	})
	c.append(item);
}


