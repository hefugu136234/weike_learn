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
	var resourceUuid = $('#resUuid').val();
	
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
	var date = new Column('date', function(nTd, sData, oData){
		nTd.empty();
		$(nTd).append(new Date(sData).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'))
	}, '', false);
	var mark = new Column('user.mark');
	table = datatable($('#praise_user'), '/project/praiseUser/datatable/' + resourceUuid, [
	date, name, username, mark]);
});