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
	oTable = $('#activity_messageList').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/activity/messageList/data/" + activityUuid,
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				fnCallback(json)
			});
		},
		"aoColumns" : [ {
			"mData" : "send.name",
			"defaultContent" : "",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetailSend(nTd, oData);
			}
		}, {
			"mData" : "sendDate",
			"defaultContent" : "",
			"orderable" : false,
		  	"fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {		        
			  	$(nTd).empty();
			  	$(nTd).append(new Date(sData).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'))
	  		}
		}, {
			"mData" : "body",
			"defaultContent" : "",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cellCreate(nTd, sData, 60)
			}
		}, {
			"mData" : "uuid",
			"defaultContent" : "",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData);
			}
		}]
	});
	
	// 时间区间选择初始化
	/*$('#timeRange .input-daterange').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });*/
	
	rangeOperation();
	
	$('#range_start').datetimepicker({
		format : 'YYYY-MM-DD HH:mm',
		ignoreReadonly: true
	});
	$('#range_end').datetimepicker({
		format : 'YYYY-MM-DD HH:mm',
		useCurrent : false,
		ignoreReadonly: true
	});
	$("#range_start").on("dp.change", function(e) {
		$('#range_end').data("DateTimePicker").minDate(e.date);
	});
	$("#range_end").on("dp.change", function(e) {
		$('#range_start').data("DateTimePicker").maxDate(e.date);
	});
});

function cellCreate(cell, value, num) {
	var c = $(cell);
	c.empty();
	c.attr("title",value);
	value = value.length > num ? value.substr(0, num) + '...' : value;
	c.append(value);
}

function showDetailSend(cell, data){
	var td = $(cell);
	td.empty();
	if(!data['send'] || !data['send']['name'] || !data['send']['uuid']){
		td.append('<font color="red">???</font>');
	}else{
		var item = $('<a href="#">' + data['send']['name'] + '</a>')
		item.click(function(e){
			e.preventDefault();
			userInfo(data['send']['uuid'])
		})
		td.append(item);
	}
}

function showDetailReceive(cell, data){
	var td = $(cell);
	td.empty();
	if(!data['receive'] || !data['receive']['name'] || !data['receive']['uuid']){
		td.append('<font color="red">???</font>');
	}else{
		var item = $('<a href="#">' + data['receive']['name'] + '</a>')
		item.click(function(e){
			e.preventDefault();
			userInfo(data['receive']['uuid'])
		})
		td.append(item);
	}
}

function showDetail(cell, data){
	var td = $(cell);
	td.empty();
	var item = $('<a href="/admin/activity/messageDetail/page/' + data + '/' + activityUuid + '">查看详情</a>');
	td.append(item);
}

function rangeOperation(){
	var range_start = $('#range_start');
	var range_end = $('#range_end');
	range_start.mouseout(function(){
		//loadData();
		console.log('start range running...');
	});
	range_end.mouseout(function(){
		//loadData();
		console.log('end range running...');
	});
}

function loadData(){
	var range_start_value = $('#range_start').val();
	var range_end_value = $('#range_end').val();
	oTable.fnReloadAjax( "/admin/activity/messageList/data/" + activityUuid + "?start=" + range_start_value + "&end=" + range_end_value );
}