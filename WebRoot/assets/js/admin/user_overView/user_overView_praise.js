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
	var userUuid = $('#userUuid').val();
	
	var name = new Column('resource.name');
	var type = new Column('resource.type');
	var speaker = new Column('resource.speaker.name');
	var date = new Column('date', function(nTd, sData, oData){
		nTd.empty();
		$(nTd).append(new Date(sData).customFormat('#YYYY#/#DD#/#MM# #hhh#:#mm#:#ss#'))
	}, '', false);

	table = datatable($('#user_praise'), '/project/userPraise/datatable/' + userUuid, [
	name, type, speaker, date]);
});