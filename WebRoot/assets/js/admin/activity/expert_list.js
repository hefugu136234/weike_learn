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

var oTable;
var activityUuid;
$(document).ready(function(){
	activityUuid = $('#activityUuid').val();
	oTable = $('#activity_expertList').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/activity/expertList/data/" + activityUuid,
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				fnCallback(json)
			});
		},
		"aoColumns" : [ {
			"mData" : "speaker.avatar",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				imageShow(nTd, sData, '头像');
			}
		}, {
			"mData" : "speaker.name",
			"defaultContent" : "",
			"orderable" : false
		}, {
			"mData" : "speaker.hospital.name",
			"defaultContent" : "",
			"orderable" : false
		}, {
			"mData" : "speaker.position",
			"defaultContent" : "",
			"orderable" : false
		}, {
			"mData" : "mark",
			"defaultContent" : "",
			"orderable" : false
		}, {
			"mData" : "createTime",
			"orderable" : false,
		 	"fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {		        
			  	$(nTd).empty();
			  	$(nTd).append(new Date(sData).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'))
		    }
		}, {
			"mData" : "modifyTime",
			"orderable" : false,
		  	"fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {		        
			  	$(nTd).empty();
			  	$(nTd).append(new Date(sData).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'))
	  		}
		}, {
			"mData" : "_status",
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

//头像
function imageShow(cell, taskId, title) {
	var img = '';
	var parent = $(cell);
	parent.empty();
	if (!!taskId) {
		img = '<a href=' + taskId + ' title="' + title
			+ '" data-gallery=""><img style="width:50px;" src="'
			+ taskId + '"/></a>';
	}
	parent.append(img);
}

function sexCell(cell, sex) {
	var c = $(cell);
	c.empty();
	c.append(sex == 0 ? "女" : "男")
}

//当前状态
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

//操作
function oprationCell(cell, td_data, uuid) {
	var parent = $(cell);
	parent.empty();
	var stateOperation = '';
	var state = td_data['_status'];
	var setToTop = '';
	
	if (state == '2'){
		//如果当前的状态是2（已下线），进行上线操作
		stateOperation = $('<a href="#">上线</a>');
	} else if (state == '1'){
		//如果当前的状态是1（已上线），进行下线操作
		stateOperation = $('<a href="#">下线</a>');
	} else if (state == '0'){
		//如果当前状态是0（未审核），进行审核操作
		stateOperation = $('<a href="#">审核</a>');
	} else{
		stateOperation = $('<font color="red">???</font>');
	}
	
	$(stateOperation).bind("click", function(e){
		e.preventDefault();
		$.post('/admin/activity/expert/updateState', {uuid: uuid, status: state}).always(function(data) {
			if (data.status == 'success') {
				refreshItem(parent.parent().children(), data['aaData'][0]);
			} else {
				if (!!data.message) {
					alert(data.message)
				} else {
					alert(data.status)
				}
			}
		})
	});
	parent.append(stateOperation).append(" | ");
	
	var edit = $('<a href="javascript:void(0);">编辑</a>')
	edit.click(function() {
		window.location.href='/admin/activity/expert/update/page/' + uuid
	})
	parent.append(edit).append(" | ");
	
	var del = $('<a href="javascript:void(0);">移除</a>')
	del.click(function() {
		if(confirm("是否确定从活动中移除该专家?")){
			$.post('/admin/activity/expert/del/', {uuid : uuid}, function(data){
				if('success'== data['status']){
					oTable.fnReloadAjax("/admin/activity/expertList/data/" + activityUuid);
				} else {
					alert(data['message']);
				}
			});
		}
	})
	parent.append(del).append(" | ");
	
	setToTop = $('<a href="javascript:void(0);">置顶</a>');
	setToTop.click(function() {
		$.post('/admin/activity/expert/recommend', {uuid : uuid}, function(data){
			if('success'== data['status']){
				//refreshItem(parent.parent().children(), data['data']);
				oTable.fnReloadAjax("/admin/activity/expertList/data/" + activityUuid);
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

function refreshItem(cell, td_data){
	cellReflsh(cell[6], new Date(td_data['modifyTime']).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'));
	statusCell(cell[7], td_data['_status']);
	oprationCell(cell[8], td_data, td_data['uuid']);
}

function cellReflsh(cell, value){
	var c = $(cell);
	c.empty();
	c.append(value);
}