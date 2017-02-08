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

$(function(){
	showActive([ 'offline_activity_list', 'activity-mgr' ]);
	
	var uuid=$('#uuid').val();

	// 加载table数据
	$('#invite_code_table').dataTable({
		"bProcessing" : true,
		"bServerSide" : true,
		"bStateSave" : false,
		"aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ] ],
		"fnDrawCallback" : function(oSettings) {
		},
		"iDisplayLength" : 10,
		"iDisplayStart" : 0,
		"sAjaxSource" : "/admin/offline/type/code/list/data/"+uuid+"?type=1",
		"aoColumns" : [ {
			"mData" : "projectCode",
			"orderable" : false
		}, {
			"mData" : "createDate",
			"orderable" : false
		},{
			"mData" : "userNickName",
			"orderable" : false
		}, {
			"mData" : "activteTime",
			"orderable" : false
		}, {
			"mData" : "_status",
			"orderable" : false,
			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
				statusCell(nTd, sData);
			}
		}
//		,  {
//			"mData" : "uuid",
//			"orderable" : false,
//			"fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
//				loadAddLink(nTd, sData, oData['isStatus']);
//			}
//		} 
		]
	});
	
	//生成邀请码
	$('#new_invite').click(function(e){
		e.preventDefault();
		$('#code_modal').modal('show');
	});
	
	$('#code_confirm').click(function(e){
		e.preventDefault();
		var code=$('#code').val();
		if(!code){
			return false;
		}
		if(isNaN(code)){
			$('#code').val('');
			return false;
		}
		if(code>100){
			alert('最多批量生成100条记录');
		}
		var $this=$(this);
		$this.prop('disabled', true);
		$.post('/admin/offline/creat/type/code',{
			uuid:uuid,
			size:code,
			type:1
		},function(data){
			$this.prop('disabled', false);
			if(data.status=='success'){
				$('#invite_code_table').dataTable().fnDraw(false);
				$('#code_modal').modal('hide');
			}else{
				alert(data.message);
			}
		});
	});
});

function statusCell(cell, isStatus) {
	var _p = $(cell);
	_p.empty();
	var value = '';
	 if (isStatus == 1) {
		value = '<span style="color: green;">已激活</span>';
	} else if (isStatus == 0) {
		value = '<span style="color: red;">未激活</span>'
	}
	_p.append(value);
}