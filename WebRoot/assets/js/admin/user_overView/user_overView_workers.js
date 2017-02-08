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

$(document).ready(function() {

	var userUuid = $('#userUuid').val();

	$('#user_works').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/userWorkers/datatable/" + userUuid,
		"aoColumns" : [ {
			"mData" : "workName",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cellCreate(nTd, sData)
			}
		}, {
			"mData" : "category",
			"orderable" : false
		}, {
			"mData" : "date",
			"orderable" : false
		}, {
			"mData" : "clickCount",
			"orderable" : false
		}, {
			"mData" : "status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				onStatusCellCreate(nTd, oData);
			}
		} ]
	});
})

function cellCreate(cell, value) {
	var c = $(cell);
	c.empty();
	c.attr("title",value);
	c.append(value);
}

function onStatusCellCreate(nTd, oData) {
	var _p = $(nTd);
	_p.empty();
	if (0 == oData['status']) {
		_p.append('未审核');
	} else if (1 == oData['status']) {
		_p.append('已上线');
	} else if (2 == oData['status']) {
		_p.append('已下线');
	}
}
