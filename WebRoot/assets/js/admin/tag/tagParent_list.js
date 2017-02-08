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

$(document).ready(function() {
	showActive([ 'assets-mgr', 'tag-list-nav' ]);
	
	// 加载table数据
	table_parent = $('#tagTable_parent').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [5, 10, 15, 20, 30 ], ["5", "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/tag/showParentTagList",
		"fnServerData" : function(sSource, aoData, fnCallback, oSettings) {
			$.getJSON(sSource, aoData, function(json) {
				if (json.status) {
					if (json.status != 'success')
						alert(json.status);
				}
				fnCallback(json);
			});
		},
		"aoColumns" : [ {
			"mData" : "name",
			"orderable" : false,
		}, {
			"mData" : "createDate",
			"orderable" : false,
		},{
			"mData" : "modifyDate",
			"orderable" : false
		}, {
			"mData" : "mark",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showDetail(nTd, sData);
			}
		}, {
			"mData" : "uuid_1",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				showChild(nTd, sData, oData);
			}
		}, {
			"mData" : "uuid_2",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				operation(nTd, sData, oData);
			}
		}]
	});
})

//备注信息完整显示
function showDetail(td, data){
	var td_tmp = $(td);
	td_tmp.empty();
	if(data.length > 10){
		var tmpData = data.substring(0, 10) + '......';
		td_tmp.append(tmpData);
	}else{
		td_tmp.append(data);
	}
	td_tmp.on("click", function(){
		$('#dataDetail').html(data);
		$('#dataModal').modal('show');
	})
}

//点击进入子标签列表
function showChild(nTd, sData, oData){
	var td = $(nTd);
	td.empty();
	//TODO	标签加样式
	td.append('<a><font color="green">点击查看子标签</font></a>');
	td.on("click", function(){
		window.location.href = '/tag/showChildTagPag/' + sData;
	})
}

//删除操作
function operation(td, uuid, data){
	var parent = $(td);
	parent.empty();
	
	//编辑操作
	var edit = $('<a href="#">编辑</a>');
	edit.click(function(){
		location.href = "/tag/updateParentTagPage/" + uuid;
	})
	parent.append(edit).append(" | ");
	
	//删除操作
	var del = $('<a href="#">删除</a>');
	$(del).bind("click", function(){
		if(confirm("你确定要删除该标签分类吗?")){
			$.post('/tag/deleteParentTag', {uuid : uuid}, function(data){
				if('success'== data['status']){
					//location.reload(true);
					table_parent.fnReloadAjax("/tag/showParentTagList");
				} else {
					alert(data['message']);
				}
			});
		}
	});
	parent.append(del);
}
