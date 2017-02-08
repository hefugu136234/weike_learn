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

var gamesRecordTable;

$(document).ready(function() {
	showActive([ 'games-mgr', 'game-record-nav']);
	
	gamesRecordTable = $('#games_record_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/games/record/data",
		"aoColumns" : [ {
			"mData" : "lotteryName",
			"orderable" : false
		}, {
			"mData" : "userName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				renderUserCell(nTd, sData, oData['userUuid']);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "isWinner",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onIsWinnerCellCreate(nTd, oData);
			}
		}, {
			"mData" : "awardName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onAwardNameCellCreate(nTd, oData);
			}
		}, {
			"mData" : "exchangeCode",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onExchangeCodeCreate(nTd, oData);
			}
		}, {
			"mData" : "modifyDate",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onModifyDateCreate(nTd, oData);
			}
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
	
	$('#exchangeTag').click(function(){
		$('#exchangeModel').modal('show');
	})
	
	//根据兑换码查询抽奖记录
	$('#doExchnage').click(function(){
		//获取输入框的兑换码
		var exchangeCode = $('#exchangeCode').val();
		//查询记录
		gamesRecordTable.fnReloadAjax("/project/games/record/data?exchangeCode=" + exchangeCode);
		$('#exchangeModel').modal('hide');
	})
})

function onModifyDateCreate(cell, oData){
	var c = $(cell);
	c.empty();
	if('YES'=== oData['isWinner']){
		if(1 == oData['status']){
			c.append(oData['modifyDate']);
		}else if(0 == oData['status']){
			c.append('<font color="red">暂未处理</font>');
		}
	}else{
		c.append('/');
	}
}

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
	if('YES'=== oData['isWinner']){
		c.append('<font color="green">√</font>');
	}else if('NO' === oData['isWinner']){
		c.append('<font color="red">X</font>')
	}
}

function onExchangeCodeCreate(nTd, oData){
	var c = $(nTd);
	c.empty();
	if('YES'=== oData['isWinner']){
		c.append(oData['exchangeCode']);
	}else if('NO' === oData['isWinner']){
		c.append('/');
	}
}

function onStatusCellCreate(cell, data) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if('YES' === data['isWinner']){
		if (data['status'] == 1) {
			value = '<span style="color: #FF9933;">已处理</span>';
		} else if (data['status'] == 0) {
			value = '<span style="color: green;">未处理</span>';
		} 
	}else{
		value = '/';
	}
	_p.append(value);
}

function onAwardNameCellCreate(cell, data) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if('YES' === data['isWinner']){
		value = '<span style="color: #FF9933;">' + data['awardName'] + '</span>';
	}else{
		value = '/';
	}
	_p.append(value);
}

function onUuidCellCreate(cell, oData) {
	var _p = $(cell);
	_p.empty();
	var op;
	if('YES' === oData['isWinner']){
		if (oData['status'] == 0) {
			op = $('<a href="#">处理</a>');
		} else if (oData['status'] == 1) {
			op = $('<a href="#">撤销</a>');
		}
		op.click(function(e) {
			if(confirm("是否确认执行该操作")){
				e.preventDefault();
				$.post('/project/games/record/status', {
					uuid : oData['uuid']
				}).always(function(data) {
					if (data['status'] == 'success') {
						//refreshItem(_p.parent().children(), data.data)
						loadData();
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
	
	onModifyDateCreate(tds[6], data);
	onStatusCellCreate(tds[8], data);
	onUuidCellCreate(tds[9], data);
}

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function selectOperation(){
	var selectorGameName = $('#searchButton_gameName');
	var selectorIsWinner = $('#searchButton_isWinner');
	var selectorIsHandle = $('#searchButton_isHandle');
	
	var handle = $('#handle');	//是否处理div
	var winer = $('#winer');	//是否中奖div
	
	//选择不同的游戏加载相应的数据
	selectorGameName.change(function(){
		loadData();
	});
	
	//是否中奖下拉框改变时加载数据
	selectorIsWinner.change(function(){
		if('YES' === $('#searchButton_isWinner option:selected').val()){
			handle.removeClass('hide');
			loadData();
		}else{
			handle.removeClass('hide');
			handle.addClass('hide');
			loadData();
		}
	});
	
	//是否处理下拉框改编时加载数据
	selectorIsHandle.change(function(){
		loadData();
	});
	
}

function loadData(){
	var gameId = $('#searchButton_gameName option:selected').val()		//获取游戏名称下拉框数据
	var isWinner = $('#searchButton_isWinner option:selected').val();	//获取是否中奖下拉框数据
	var isHandle = $('#searchButton_isHandle option:selected').val();	//获取是否处理下拉框数据
	
	if($('#searchButton_isHandle').is(':hidden')){
		gamesRecordTable.fnReloadAjax("/project/games/record/data?isWiner=" + isWinner + "&gameId=" + gameId );
	}else{
		gamesRecordTable.fnReloadAjax("/project/games/record/data?isWiner=" + isWinner + "&isHandle=" + isHandle + "&gameId=" + gameId );
	}
}