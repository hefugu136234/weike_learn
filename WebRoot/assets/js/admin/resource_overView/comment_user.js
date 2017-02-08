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

var table;
var resourceUuid;
$(document).ready( function() {
	resourceUuid = $('#resUuid').val();
	
	var date = new Column('createTime', function(nTd, sData, oData){
		nTd.empty();
		$(nTd).append(new Date(sData).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'))
	}, '', false);
	var name = new Column('user.name');
	/*var name = new Column('user.name',function(nTd, sData, oData) {
		nTd.empty();
		var item = $('<a href="javascript:void(0)" >' + sData + '</a>');
		item.click(function(){
			userInfo(oData['user']['uuid']);
		})
		nTd.append(item);
	});*/
	var username = new Column('user.userName');
	var target = new Column('target');
	var content = new Column('body');
	var commentUuid = new Column('uuid', function(nTd, sData, oData){
		oprationCell(nTd, sData, oData);
	})
	
	dataTable = datatable($('#resource_comment'), '/project/commentUser/datatable/' + resourceUuid, [
	date, name, username, target, content, commentUuid]);
});

function oprationCell(nTd, sData, oData){
	var status = oData['_status']
	var parent = $(nTd);
	parent.empty();
	var item = '';
	
	// 审核
//	if (status == 0) {
//		item = $('<a href="javascript:void(0);">审核</a>');
//	} else {
//		item = $('<a href="javascript:void(0);">取消审核</a>');
//	}
//	item.click(function() {
//		$.post('/asset/t/video/status', {
//			uuid : uuid
//		}, function() {
//		}).done(function() {
//
//		}).fail(function() {
//
//		}).always(function(data) {
//			if (data.status == 'success') {
//				table.refreshItem(data);
//			} else {
//				alert(data.status)
//			}
//		})
//	});
//	parent.append(item);

	// 删除
	var remove = $('<a style="margin-left:10px" href="javascript:void(0);">删除</a>')
	remove.click(function() {
		if(confirm("该评论下的所有子评论也会被删除，是否确认?")){
			$.post('/project/commentUser/remove', {
				commentUuid : oData['uuid']
			}, function() {
			}).done(function() {
			}).fail(function() {
			}).always(function(data) {
				if(!! data['message']){
					dataTable.table.fnReloadAjax('/project/commentUser/datatable/' + resourceUuid);
					alert(data['message']);
				}else{
					alert(data['status'])
				}
			})
		}
	})
	parent.append(remove);
}