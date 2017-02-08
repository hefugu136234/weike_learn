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
$(document).ready( function() {
	var uuid = $('#optionUuid').val();
	
	/*var name = new Column('user.name',function(nTd, sData, oData) {
		nTd.empty();
		var item = $('<a href="javascript:void(0)" >' + sData + '</a>');
		item.click(function(){
			userInfo(oData['user']['uuid']);
		})
		nTd.append(item);
	});*/
	var date = new Column('date', function(nTd, sData, oData){
		nTd.empty();
		$(nTd).append(new Date(sData).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'))
	}, '', false);
	var name = new Column('user.name');
	var username = new Column('user.userName');
	var mark = new Column('user.mark');
	
	table = datatable($('#resource_voteUser'), '/project/voteUser/datatable/' + uuid, [
	date, name, username, mark]);
});

function userInfo(uuid) {
	$('#iframe').attr("src", '/project/userOverView/' + uuid);
	$('#user_info_global').modal('show');
}