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

var qrListTable;

// Custom scripts
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
	showActive([ 'qrcode_mgr_nav', 'assets-mgr' ]);

	// 加载table数据
	qrListTable = $('#qrsence_list_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/project/qrcode/list/data",
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				cutOutData(nTd, sData, 45);
			}
		}, {
			"mData" : "createDate",
			"orderable" : false
		}, {
			"mData" : "qrProperty",
			"orderable" : false
		}, {
			"mData" : "qrType",
			"orderable" : false
		},{
			"mData" : "scanCount",
			"orderable" : false
		}, {
			"mData" : "uuid",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				loadAddLink(nTd, sData);
			}
		} ]
	});
	
	selectOperation();
});




function initModalData(data) {
	//console.log(data);
	$('#broadcast_name').html(data.name);
	//$('#record_url').val(data.resourceUrl);
	var video_select = $('#video_select');
	buildSelect(data.optionAdditions);
	var select = data.selectItem;
	if (!!select) {
		video_select.val(select);
	} else {
		video_select.get(0).selectedIndex = 0;
	}
	video_select.ajaxChosen({
		dataType : 'json',
		type : 'GET',
		url : '/project/broadcast/search/record'
	}, {
		loadingImg : '/assets/img/loading.gif'
	});

	$('#video_select_chosen').css('width', '100%');
}

function buildSelect(list) {
	var video_select = $('#video_select');
	video_select.empty();
	video_select.append('<option value="0">请输入"资源名称"或者"讲者"检索资源</option>');
	if (!!list && list.length > 0) {
		$.each(list, function(index, item) {
			var option = buildOption(item);
			video_select.append(option);
		});
	}
}

function buildOption(addition) {
	var option = '<option value="' + addition.id + '">' + addition.text
			+ '</option>';
	return option;
}

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	if (isStatus == 2) {
		value = '<span style="color: #FF9933;">已下线</span>';
	} else if (isStatus == 1) {
		value = '<span style="color: green;">已上线</span>';
	} else if (isStatus == 0) {
		value = '<span style="color: red;">未审核</span>'
	}
	_p.append(value);
}

function loadAddLink(cell, uuid) {
	var parent = $(cell);
	parent.empty();

	// 编辑
	var edit = $('<a href="javascript:void(0);">编辑</a>');
	edit.click(function(event) {
		event.preventDefault();
		location.href = "/project/qrcode/edit/page/" + uuid;
	});
	parent.append(edit);
	
	var qrcode = $('<a style="margin-left:10px" href="javascript:void(0);">二维码</a>');
	qrcode.click(function(event) {
		event.preventDefault();
		$.getJSON('/project/qrcode/get/qrcode/' + uuid, function(data) {
			if (data.status == 'success') {
				$('#qrcode_url').val(data.message);
				$('#qrcode_preview').attr('src', data.message);
				$('#dataModal_qrcode').modal('show');
			} else {
				alert(data.message);
			}
		});
	});
	parent.append(qrcode);
	
	var scan_code = $('<a style="margin-left:10px" href="javascript:void(0);">扫码详情</a>');
	scan_code.click(function(event) {
		event.preventDefault();
		location.href = "/project/qrcode/user/list/page/" + uuid;
	});
	parent.append(scan_code);
}

function refreshItem(tds, data) {
	cutOutData(tds[0], data['name'], 50);
	renderCell(tds[1], data['createDate']);
	limitNum(tds[2], data['limitNum']);
	castType(tds[3], data['castType']);
	cutOutData(tds[4], data['resourceName'], 50);
	cutOutData(tds[5], data['platFormName'], 50);
	statusCell(tds[6], data['isStatus']);
	loadAddLink(tds[7], data['uuid'], data['isStatus']);
}
function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

// js截取字符串

function cutOutData(cell, val, num) {
	var c = $(cell);
	c.empty();
	val = val.length > num ? val.substr(0, num) + '...' : val;
	c.append(val);
}

function selectOperation(){
	var limitType = $('#searchButton_limitType');
	var judyType = $('#searchButton_judyType');
	
	//二维码属性过滤（永久／临时）
	limitType.change(function(){
		loadData();
	});
	
	//二维码类型
	judyType.change(function(){
		loadData();
	});
}

function loadData(){
	var limitTypeValue = $('#searchButton_limitType option:selected').val();
	var judyTypeValue = $('#searchButton_judyType option:selected').val();
	qrListTable.fnReloadAjax("/project/qrcode/list/data?limitType=" + limitTypeValue + "&judyType=" + judyTypeValue );
}


