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

var table_hospital;

$(document).ready(function() {
	showActive([ 'hospital_mgr_nav', 'assets-mgr' ]);
	
	table_hospital = $('#hospital_list').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/hospital/mgr/datatable",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false
		}, {
			"mData" : "grade",
			"orderable" : false
		}, {
			"mData" : "address",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				initAddress(nTd, oData);
			}
		}, {
			"mData" : "mobile",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onMobileCellCreate(nTd, oData);
			}
		}, {
			"mData" : "address",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onAddressCellCreate(nTd, oData);
			}
		}, {
			"mData" : "modifyDate",
			"orderable" : false
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operation(nTd, oData);
			}
		} ]
	});
	
	
	
})

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function operation(cell, data) {
	var _p = $(cell);
	_p.empty();
	var hospitalUuid = data['uuid'];
	// 编辑
	var edit = $('<a href="javascript:void(0);">编辑</a>');
	edit.click(function(event){
		event.preventDefault();
		location.href = "/hospital/mgr/update/page/" + hospitalUuid;
	});
	_p.append(edit).append(" | ");
	
	// 删除
	var remove = $('<a href="javascript:void(0);">删除</a>');
	remove.click(function(event){
		event.preventDefault();
		if(confirm("你确定要删除该医院吗")){
			$.post('/hospital/mgr/remove/' + hospitalUuid, function() {
			}).always(function(data) {
				if ('success' == data['status']) {
					//window.location.href = '/project/hospital/mgr';
					table_hospital.fnReloadAjax('/hospital/mgr/datatable');
				} else {
					alert(data['message']);
				}
			});
		}
	});
	_p.append(remove);
}

function initAddress(nTd, oData){
	var _p = $(nTd);
	_p.empty();
	_p.append(oData['provinceName']).append(',').append(oData['cityName']);
}

function onMobileCellCreate(nTd, oData){
	var _p = $(nTd);
	_p.empty();
	if(!!oData['mobile']){
		_p.append(oData['mobile']);
	}else{
		_p.append('<font color="red">待完善</font>');
	}
}

function onAddressCellCreate(nTd, oData){
	var _p = $(nTd);
	_p.empty();
	if(!!oData['address']){
		_p.append(oData['address']);
	}else{
		_p.append('<font color="red">待完善</font>');
	}
}
