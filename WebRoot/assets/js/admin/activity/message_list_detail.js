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
var notificationUuid;

$(document).ready(function(){
	notificationUuid = $('#notificationUuid').val();
	oTable = $('#activity_messageList').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/activity/messageListDetail/data/" + notificationUuid,
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				fnCallback(json)
			});
		},
		"aoColumns" : [ {
			"mData" : "receive.name",
			"defaultContent" : "",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetailReceive(nTd, oData);
			}
		}, {
			"mData" : "createDate",
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
		}]
	});
	
	// 时间区间选择初始化
	$('#timeRange .input-daterange').datepicker({
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });
});

function cellCreate(cell, value, num) {
	var c = $(cell);
	c.empty();
	c.attr("title",value);
	value = value.length > num ? value.substr(0, num) + '...' : value;
	c.append(value);
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
