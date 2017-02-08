//重新加载表数据插件
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

$(document).ready(function(){
	var categoryUuid='';
	var categoryName='';
	//生成选择树
	$('#tree_search').jstree({
		'core' : {
			'data' : {
				url : '/asset/category/node',
				data : function(node) {
					return {
						'uuid' : node.id,
						'timestamp' : new Date().getTime()
					}
				}
			},
			check_callback : true,
			themes : {
				'responsive' : false
			},
			callback : {
				onopen_all : function() {
					log("Open ALL");
				},
			}
		},
		'plugins' : [ 'state' ]
	}).bind("select_node.jstree", function(e,data){
		categoryUuid=data.node.id;
		categoryName=data.node.text;
		/*
		console.log(categoryUuid);
		console.log(data);
		*/
	})
	
	//给确定按钮绑定事件
	$('#select_confirm_btn_search').click(function(){
		var tmp = getNodeById(categoryUuid);
		if("#" == tmp.parent){
		//if("be528a45-4d61-11e5-b667-8c6a6fec53d9"===categoryUuid){
			oTable.fnReloadAjax("/asset/videos/datatable/");
		}else{
			oTable.fnReloadAjax("/asset/videos/datatable/"+categoryUuid);
		}
		$('#categorySelectorModal_search').modal('hide');
		$('#video_category_button_search').val(categoryName);
	})
});

function getNodeById(id) {
	var all = $('#tree').jstree();
	return all._model.data[id];
}