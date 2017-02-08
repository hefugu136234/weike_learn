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

$(document).ready(
		function() {
			showActive([ 'ad_list_nav', 'ad-mgr' ]);
			$('#advert_list_table').dataTable(
					{
						"bProcessing" : true,
						"bServerSide" : true,
						"bStateSave" : false,
						"aLengthMenu" : [ [ 10, 15, 20, 30 ],
								[ "10", "15", "20", "30" ] ],
						"fnDrawCallback" : function(oSettings) {
						},
						"iDisplayLength" : 10,
						"iDisplayStart" : 0,
						"sAjaxSource" : "/project/adver/list",
						"aoColumns" : [
								{
									"mData" : "name",
									"orderable" : false
								},
								{
									"mData" : "createDate",
									"orderable" : false
								},
								{
									"mData" : "content_type",
									"orderable" : false
								},
								{
									"mData" : "limit_time",
									"orderable" : false
								},
								{
									"mData" : "mark",
									"orderable" : false
								},
								{
									"mData" : "createName",
									"orderable" : false
								},
								{
									"mData" : "ad_status",
									"orderable" : false,
									"fnCreatedCell" : function(nTd, sData,
											oData, iRow, iCol) {
										statusCell(nTd, oData['ad_status']);
									}
								},
								{
									"mData" : "uuid",
									"orderable" : false,
									"fnCreatedCell" : function(nTd, sData,
											oData, iRow, iCol) {
										loadAddLink(nTd, sData,
												oData['ad_status']);
									}
								} ]
					});

			

			 $('#uploadify').uploadify({
			 'swf' : '/assets/js/uploadify/uploadify.swf',
			 'uploader' : 'http://cloud.lankr.cn/api/image/upload',
			 'formData' : {
			 'appKey' : 'ff7a9de914595ec790dbf5b32ab46e9a'
			 },
			 'fileObjName' : 'file',
			
			 'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png',
			 'method' : 'post',
			 'fileSizeLimit' : 10000,
			 'auto' : true,
			 'cancelImg' : '/assets/js/uploadify/uploadify-cancel.png',
			 'onUploadSuccess' : function(file, data, response) {
			 $('#image-preview').show();
			 var json_data = JSON.parse(data);
			 $('#image-preview').attr('src', json_data.url);
			 }
			 });

			

			$('#adverEditModal').on(
					'show.bs.modal',
					function(e) {
						var uuid = $('#adverEditModal').attr("data-id");
						$('#image-preview').attr('src', '')
						$('#image-preview').hide();
						$.get("/project/adver/" + uuid + "/detail", function() {
						}).always(
								function(data) {
									// console.log(data)
									if (data.status) {
										alert(data.status);
									} else {
										$('#ad_name').val(data.name);
										$('#ad_position').empty();
										$.each(data.postionList, function() {
											var item = $('<option id='
													+ this.id + ' value='
													+ this.id + '>' + this.name
													+ '</option>');
											$('#ad_position').append(item);
										});
										$('#' + data.positionId).attr(
												"selected", "selected");
										$('#ad_time').val(data.limit_time);
										$('#description').val(data.mark);
										if (data.res_url) {
											$('#image-preview').show();
											$('#image-preview').attr('src',
													data.res_url);
										}
									}
								})
						$('#confirm_btn').unbind('click');
						$('#confirm_btn').click(function() {
							if (vidion() == 1) {
								$.post('/porject/adver/update', {
									uuid : uuid,
									adname : $('#ad_name').val(),
									adpostion : $('#ad_position').val(),
									adtime : $('#ad_time').val(),
									adimg : $('#image-preview').attr('src'),
									admark : $('#description').val()
								}, function() {
								}).always(function(data) {
									if (data.status) {
										alert(data.status)
									} else {
										$('#adverEditModal').modal('hide');
										refreshItem(edit_row.children(), data);
									}
								});
							}
						});
					});

		});

function vidion() {
	var ad_name = $('#ad_name').val();
	var ad_time = $('#ad_time').val();
	var reg = /^[1-9]*[1-9][0-9]*$/;
	if (ad_name == '') {
		alert('Please enter a value.');
		return 0;
	}
	if (!reg.test(ad_time)) {
		alert('Please enter a integers.');
		return 0;
	}
	return 1;
}

var edit_row;
function statusCell(cell, adver_stats) {
	var _p = $(cell);
	_p.empty();
	if (adver_stats == 0) {
		value = '<span style="color: #FF9933;">已下线</span>'
	} else if (adver_stats == 1) {
		value = '<span style="color: green;">已上线</span>'
	}
	_p.append(value)
}

function loadAddLink(cell, uuid, adver_stats) {
	var parent = $(cell);
	parent.empty();
	var item = '';
	if (adver_stats == 0) {
		item = $('<a href="#">上线</a>')
	} else if (adver_stats == 1) {
		item = $('<a href="#">下线</a>')
	}
	item.click(function() {
		$.post('/project/change/adver/stats', {
			uuid : uuid
		}, function() {
		}).done(function() {

		}).fail(function() {

		}).always(function(data) {
			console.log(data);
			if (data.status) {
				alert(data.status);
			} else {
				refreshItem(parent.parent().children(), data);
			}
		});

	});
	parent.append(item);
	// 编辑
	var edit = $('<a style="margin-left:10px" href="#">编辑</a>')
	edit.click(function() {
		$('#adverEditModal').attr("data-id", uuid);
		edit_row = parent.parent();
		$('#adverEditModal').modal('show');
	})
	parent.append(edit);

}

function refreshItem(tds, data) {
	renderCell(tds[0], data['name']);
	renderCell(tds[1], data['createDate']);
	renderCell(tds[2], data['content_type']);
	renderCell(tds[3], data['limit_time']);
	renderCell(tds[4], data['mark']);
	renderCell(tds[5], data['createName']);
	statusCell(tds[6], data['ad_status']);
	loadAddLink(tds[7], data['uuid'], data['ad_status']);
}

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}