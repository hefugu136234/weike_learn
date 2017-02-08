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

var shakeTable;

$(document).ready(function() {
	showActive([ 'activity-mgr', 'game-shake-nav' ]);
	shakeTable = $('#shake_record_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/game/exchange/record/datatable",
		"aoColumns" : [ {
			"mData" : "userName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				//test
				console.log(oData);
				
				renderUserCell(nTd, sData, oData['userUuid']);
			}
		}, {
			"mData" : "userNickName",
			"orderable" : false
		}, {
			"mData" : "createDate",
			"orderable" : false,
		}, {
			"mData" : "isWinner",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onIsWinnerCellCreate(nTd, oData);
			}
		}, {
			"mData" : "modifyDate",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false,
		}, {
			"mData" : "status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onStatusCellCreate(nTd, oData);
			}
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onUuidCellCreate(nTd, oData);
			}
		}]
	});
	
	selectOperation();
})

function renderUserCell(cell,username,uuid){
	var c = $(cell);
	c.empty();
	var item = $('<a href="#">' + username + '</a>')
	item.click(function(e){
		e.preventDefault();
		userInfo(uuid)
	})
	c.append(item);
}

function onIsWinnerCellCreate(nTd, oData){
	var c = $(nTd);
	c.empty();
	if('true' === oData['isWinner']){
		c.append('<font color="green">√</font>');
	}else if('false' === oData['isWinner']){
		c.append('<font color="red">X</font>')
	}
}

function onStatusCellCreate(cell, data) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	
	if('true' === data['isWinner']){
		if (data['status'] == 2) {
			value = '<span style="color: #FF9933;">已处理</span>';
		} else if (data['status'] == 1) {
			value = '<span style="color: green;">未处理</span>';
		} 
	}else{
		value = '/';
	}
	_p.append(value);
}

function onUuidCellCreate(cell, oData) {
	var _p = $(cell);
	_p.empty();
	var op;
	
	if('true' === oData['isWinner']){
		if (oData['status'] == 1) {
			op = $('<a href="#">处理</a>');
		} else if (oData['status'] == 2) {
			op = $('<a href="#">撤销</a>');
		}
		op.click(function(e) {
			if(confirm("是否确认执行该操作")){
				e.preventDefault();
				$.post('/game/exchange/record/status', {
					uuid : oData['uuid']
				}).always(function(data) {
					if (data['status'] == 'success') {
						refreshItem(_p.parent().children(), data.data)
					} else {
						if (!!data.message) {
							alert(data.message)
						} else {
							alert(data.status)
						}
					}
				})
			}
		})
	}else{
		op = $('<a href="#">无需操作</a>');
		op.unbind('click');
	}
	
	_p.append(op);
}

//js截取字符串
function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}

function refreshItem(tds, data) {
	//test
	console.log(data);
	
	renderCell(tds[4], data['modifyDate']);
	onStatusCellCreate(tds[6], data);
	onUuidCellCreate(tds[7], data);
}

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function selectOperation(){
	var selectorIsWinner = $('#searchButton_isWinner');
	var selectorIsHandle = $('#searchButton_isHandle');
	var handle = $('#handle');
	var winer = $('#winer');
	
	selectorIsWinner.change(function(){
		if('yes' === $('#searchButton_isWinner option:selected').val()){
			handle.removeClass('hide');
			loadData();
		}else{
			handle.removeClass('hide');
			handle.addClass('hide');
			loadData();
		}
	});
	
	selectorIsHandle.change(function(){
		loadData();
	});
}

function loadData(){
	var isWinner = $('#searchButton_isWinner option:selected').val();
	//test
	console.log(isWinner);
	
	var isHandle = $('#searchButton_isHandle option:selected').val();
	
	if($('#searchButton_isHandle').is(':hidden')){
		shakeTable.fnReloadAjax("/game/exchange/record/datatable?isWiner=" + isWinner );
	}else{
		shakeTable.fnReloadAjax("/game/exchange/record/datatable?isWiner=" + isWinner + "&isHandle=" + isHandle );
	}
}