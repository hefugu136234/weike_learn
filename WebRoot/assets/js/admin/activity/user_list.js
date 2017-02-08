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
	oTable = $('#activity_userlist').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/activity/userlist/data/" + activityUuid,
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				fnCallback(json)
			});
		},
		"aoColumns" : [ {
			"mData" : "username",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				userInfoCell(nTd,oData);
			}
		}, {
			"mData" : "phone",
			"orderable" : false
		}, {
			"mData" : "email",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData)
			}
		}, {
			"mData" : "address",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData)
			}
		}, {
			"mData" : "company",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData)
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "modifyDate",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData)
			}
		}, {
			"mData" : "isActive",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				isActive(nTd, sData)
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

function cellReflsh(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function isActive(nTd, sData){
	var parent = $(nTd);
	parent.empty();
	var cell = '';
	if (sData == '0'){
		cell = $('<a href="#"><font color="red">已禁用</font></a>');
	} else if(sData == '1'){
		cell = $('<a href="#"><font color="green">已启用</font></a>');
	}else{}
	parent.append(cell);
}

//删除活动资源
function oprationCell(cell, td_data, uuid) {
	var parent = $(cell);
	parent.empty();
	var stateOperation = '';
	var _isActive = td_data['isActive'];
	
	if (_isActive == '0'){
		stateOperation = $('<a href="#"><font color="green">启用</font></a>');
	} else if(_isActive == '1'){
		stateOperation = $('<a href="#"><font color="red">禁用</font></a>');
	}else{}
	
	$(stateOperation).bind("click", function(e){
		e.preventDefault();
		if(confirm("你确定要执行该操作吗")){
			$.post('/admin/activity/resuser/updateIsActive', {uuid: uuid, isActive: _isActive}).always(function(data) {
				//test
				console.log(data);
				if (data.status == 'success') {
					//refreshItem(parent.parent().children(), data['data']);
					//test
					console.log(data['data']);
					
					oprationCell((parent.parent().children())[9], data['data'], data['data']['uuid']);
					isActive((parent.parent().children())[8], data['data']['isActive']);
					cellReflsh((parent.parent().children())[6], data['data']['modifyDate']);
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
	parent.append(stateOperation)
	
//	var del = $('<a href="javascript:void(0);">移除</a>')
//	del.click(function() {
//		if(confirm("你确定要删除该资源吗?")){
//			$.post('/admin/activity/resuser/del/', {uuid : uuid}, function(data){
//				if('success'== data['status']){
//					oTable.fnReloadAjax("/admin/activity/userlist/data/" + activityUuid);
//				} else {
//					alert(data['message']);
//				}
//			});
//		}
//	})
//	parent.append(del);
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

function userInfoCell(cell, data){
	var c = $(cell);
	c.empty();
	var item = $('<a href="#">' + data['username'] + '</a>')
	var userUuid = data['userUuid'];
	item.click(function(e){
		e.preventDefault();
		userInfo(userUuid);
	})
	c.append(item);
}


