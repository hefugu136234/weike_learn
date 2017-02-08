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

var table;

$(document).ready(
		function() {
			showActive([ 'video_mrg_nav', 'assets-mgr' ]);
			// oTable = $('#video_list_table').dataTable({
			// "bProcessing" : true,
			// "bServerSide" : true,
			// "bStateSave" : false,
			// "aLengthMenu" : [ [ 10, 15, 20, 30 ], [ "10", "15", "20", "30" ]
			// ],
			// "fnDrawCallback" : function(oSettings) {
			//
			// },
			// "iDisplayLength" : 10,
			// "iDisplayStart" : 0,
			// "aaSorting" : [ [ 2, 'desc' ] ],
			// "sAjaxSource" : "/asset/videos/datatable",
			// "fnServerData" : function(sSource, aoData, fnCallback, oSettings)
			// {
			// $.getJSON(sSource, aoData, function(json) {
			// // console.log(json)
			// if (json.status) {
			// if (json.status != 'success')
			// alert(json.status);
			// }
			// fnCallback(json)
			// });
			// },
			// "aoColumns" : [ {
			// "mData" : "title",
			// "orderable" : false
			// }, {
			// "mData" : "categoryName",
			// "orderable" : false,
			// "fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
			// transformType(nTd, oData);
			// }
			// }, {
			// "mData" : "modifyDate",
			// "orderable" : true
			// }, {
			// "mData" : "duration",
			// "orderable" : false
			// }, {
			// "mData" : "speaker",
			// "orderable" : false,
			// "fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
			// speakerCell(nTd, oData.speaker);
			// }
			//
			// }, {
			// "mData" : "createUser",
			// "orderable" : false
			// }, {
			// "mData" : "price",
			// "orderable" : true,
			// "fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
			// priceCell(nTd, oData);
			// }
			// }, {
			// "mData" : "description",
			// "orderable" : false
			// }, {
			// "mData" : "video_status",
			// "orderable" : true,
			// "fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
			// statusCell(nTd, sData);
			// }
			// }, {
			// "mData" : "uuid",
			// "orderable" : false,
			// "fnCreatedCell" : function(nTd, sData, oData, iRow, iCol) {
			// //test
			// console.log(sData);
			//				
			// oprationCell(nTd, oData, sData)
			// }
			// } ]
			// });
			//	
			var title = new Column('title')
			var categoryName = new Column('categoryName', function(nTd, sData,
					oData) {
				transformType(nTd, oData);
			})
			var modifyDate = new Column('modifyDate', function() {
			}, '', true)
			var duration = new Column('duration')
			var speaker = new Column('speaker', function(nTd, sData, oData) {
				nTd.empty();
				if (sData.name) {
					nTd.append('<span title="' + sData.hospitalName
							+ "&nbsp;&nbsp;" + sData.professor + '">'
							+ sData.name + '</span>')
				} else {
					nTd.append('<span style="color: #FF9933;">未指定</span>')
				}
			})
			var createUser = new Column('createUser')
			var price = new Column('price', function(nTd, sData, oData) {
				priceCell(nTd, oData);
			})
			var description = new Column('description')
			var video_status = new Column('video_status', function(nTd, sData,
					oData) {
				statusCell(nTd, sData);
			})
			var uuid = new Column('uuid', function(nTd, sData, oData) {
				oprationCell(nTd, oData, sData)
			})

			table = datatable($('#video_list_table'),
					'/asset/videos/datatable', [ title, categoryName,
							modifyDate, duration, speaker, createUser, price,
							description, video_status, uuid ])

			// 上传封面（七牛）20160321
			var $e = $('#uploadify')
			uploaderInit(new Part($e, 1, function(part) {
			}, uploadFinished).init())
			function uploadFinished(src) {
				$('#cover_hidden').val(src)
				$('#uploadTag').val('uploadSuccess');
			}

			/*
			 * $('#uploadify').uploadify({ 'swf' :
			 * '/assets/js/uploadify/uploadify.swf', 'uploader' :
			 * 'http://cloud.lankr.cn/api/image/upload', 'formData' : { 'appKey' :
			 * 'ff7a9de914595ec790dbf5b32ab46e9a' }, 'fileObjName' : 'file',
			 * 'fileTypeExts' : '*.bmp;*.jpg;*.jpeg;*.png', 'method' : 'post',
			 * 'fileSizeLimit' : 10000, 'auto' : true, 'cancelImg' :
			 * '/assets/js/uploadify/uploadify-cancel.png', 'onUploadSuccess' :
			 * function(file, data, response) { $('#image-preview').show(); var
			 * json_data = JSON.parse(data); $('#image-preview').attr('src',
			 * json_data.url); } });
			 */

			$('#videoEditModal').on('show.bs.modal', function(e) {
				$('#speaker_selector_chosen').width(320)
				speaker_selector.val(edit_speaker.uuid)
				speaker_selector.trigger("chosen:updated");
				var uuid = $('#videoEditModal').attr("data-id");
				videoUuid = uuid;
				$('#image-preview').attr('src', '')
				$('#image-preview').hide();
				$.get("/asset/video/" + uuid + "/detail", function() {
				}).always(function(data) {
					if (data.status == 'success') {
						$('#video_name').val(data.title)
						$('#description').val(data.description)
						editPriceInit(data.need_price, data.price)
						if (data.cover) {
							$('#image-preview').show();
							$('#image-preview').attr('src', data.cover);
						}
					} else {
						alert(data.status)
					}
				});
				$('#confirm_btn').unbind('click');
				$('#confirm_btn').click(function() {
					$.post('/asset/video/' + videoUuid + '/update', {
						cover : $('#image-preview').attr('src'),
						title : $('#video_name').val(),
						need_price : $('#price_control').is(':checked'),
						price : $('#price').val(),
						mark : $('#description').val(),
						speaker_uuid : $('#speaker_selector').val()
					}, function() {
					}).always(function(data) {
						// test
						console.log(data);

						if (data.status != 'success') {
							alert(data.message);
						} else {
							$('#videoEditModal').modal('hide');
							table.refreshItem(data);
						}
					})
				})
			});

			function editPriceInit(need_price, price) {
				var pc = $('#price_control');
				pc.unbind('click')
				pc.click(function() {
					editPriceInit($(this).is(':checked'), price)
				})
				pc.prop('checked', need_price);
				if (need_price) {
					$('#price').show();
					$('#price').val(price);
				} else {
					$('#price').hide();
				}
			}

			$('#tree').jstree({
				'core' : {
					'multiple' : false,
					'data' : {
						'url' : '/asset/category/node',
						'data' : function(node) {
							return {
								'uuid' : node.id,
								'timestamp' : new Date().getTime()
							};
						}
					},
					'check_callback' : true,
					'themes' : {
						'responsive' : false
					}
				},

				'plugins' : [ 'state' ],
				'callback' : {
					'onselect' : function(node) {

					}
				}
			}).bind(
					"after_open.jstree",
					function(e, data) {
						console.log('dadada');
						var categoryUuid = $('#categorySelectorModal').attr(
								'data-num');
						var targetNode = $('#tree').jstree('get_node',
								categoryUuid);
						if (targetNode) {
							if (!targetNode.state.selected) {
								$('#tree').jstree('select_node', categoryUuid);
							}
						}
					});

			$('#categorySelectorModal').on('show.bs.modal', function(e) {
				var videoUuid = $('#categorySelectorModal').attr('data-id');
				$('#select_confirm_btn').unbind('click');
				$('#select_confirm_btn').click(function() {
					// 更改种类选择
					var selectUuid = $('#tree').jstree('get_selected')[0];
					console.log(selectUuid);
					$.post('/asset/video/' + videoUuid + '/category/update', {
						'categoryUuid' : selectUuid
					}, function() {
					}).always(function(data) {
						if (data.status != 'success') {
							alert(data.status)
						} else {
							$('#categorySelectorModal').modal('hide');
							table.refreshItem(data);
						}
					});
				});
			});

			// $("#tree").bind("after_open.jstree", function(e, data) {
			// console.log(data);
			// var categoryUuid=$('#categorySelectorModal').attr('data-num');
			// var targetNode=$('#tree').jstree('get_node',categoryUuid);
			// console.log(targetNode);
			// if(targetNode){
			// ]if(!targetNode.state.selected){
			// $('#tree').jstree('select_node',categoryUuid);
			// }
			// }
			// });
			speaker_selector = $('#speaker_selector');
			showSpeakerSelector();
		});
var speaker_selector
function showSpeakerSelector() {
	// speaker
	$.get('/user/speakers/json', function() {
	}).always(
			function(data) {
				if (data.status == 'success') {
					$.each(data.data, function(i, e) {
						speaker_selector.append('<option value="'
								+ e.uuid
								+ '">'
								+ e.name
								+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
								+ (typeof (e.hospitalName) == 'undefined' ? '无'
										: e.hospitalName) + '</option>')

					})
				}
				speaker_selector.chosen({
					placeholder_text_single : "请选择该资源的讲者",
					no_results_text : "没有匹配到结果"
				});
			})
}

// function refreshItem(tds, data) {
// renderCell(tds[0], data['title'])
// transformType(tds[1], data);
// // renderCell(tds[2], data['createDate'])
// renderCell(tds[2], data['modifyDate'])
// renderCell(tds[3], data['duration'])
// speakerCell(tds[4], data['speaker'])
// renderCell(tds[5], data['createUser'])
// priceCell(tds[6], data);
// renderCell(tds[7], data['description'])
// statusCell(tds[8], data['video_status'])
// oprationCell(tds[9], data, data['uuid'])
// }

function renderCell(cell, value) {
	var c = $(cell);
	c.empty();
	c.append(value);
}

function speakerCell(cell, speaker) {
	var _p = $(cell);
	_p.empty();
	if (speaker.name) {
		_p.append('<span title="' + speaker.hospitalName + "&nbsp;&nbsp;"
				+ speaker.professor + '">' + speaker.name + '</span>')
	} else {
		_p.append('<span style="color: #FF9933;">未指定</span>')
	}
}

function statusCell(cell, video_status) {
	var _p = $(cell);
	_p.empty();
	value = '<span style="color: red;">未审核</span>'
	if (video_status == 1) {
		value = '<span style="color: green;">已上线</span>'
	} else if (video_status == 2) {
		value = '<span style="color: #FF9933;">已下线</span>'
	}
	_p.append(value)
}

function priceCell(cell, data) {
	var _p = $(cell);
	_p.empty();
	if (data['need_price']) {
		value = '<span>' + data['price'] + '</span>'
	} else {
		value = '<span style="color: green">免费</span>'
	}
	_p.append(value)
}

// var cateItem = '';
function transformType(cell, data) {
	var item = $(cell);
	item.empty();
	var dataValue = data['categoryName'];
	var category = $('<a href="#">' + dataValue + '</a>');
	category.click(function(e) {
		e.preventDefault();
		$('#categorySelectorModal').attr("data-id", data['uuid']);
		$('#categorySelectorModal').attr("data-num", data['categoryId']);
		edit_row = item.parent();
		// 清除所有之前的选择
		$('#tree').jstree().deselect_all();
		$('#tree').jstree().close_all();
		// 找到#的根节点，根据根节点找到顶级节点
		var maxroot = $('#tree').jstree('get_node', '#');
		var root = maxroot.children[0];
		var rootchildren = $('#tree').jstree('get_node', root);
		// console.log(rootchildren);
		if (!rootchildren.state.loaded) {
			$("#tree").jstree('load_node', rootchildren, function(data) {
				checkopen(data);
			});
		} else {
			checkopen(rootchildren);
		}
	});
	item.append(category);
}

function checkopen(data) {
	if (!data.state.opened) {
		$("#tree").jstree('open_node', data, function(dataShow) {
			selectNode();
		});
	} else {
		selectNode();
	}
}

function selectNode() {
	var categoryUuid = $('#categorySelectorModal').attr('data-num');
	var targetNode = $('#tree').jstree('get_node', categoryUuid);
	console.log(targetNode);
	if (!targetNode) {
		$.get('/search/parents/' + categoryUuid + '/root', {}, function(data) {
			if (data.status) {
				alert(data.status);
			} else if (data.list) {
				var list = (data.list).split(':');
				console.log(list);
				for (var i = 0; i < list.length; i++) {
					console.log(list[i]);
					var targetRoot = $('#tree').jstree('get_node', list[i]);
					console.log(targetRoot);
					if (!targetRoot.state.opened) {
						$("#tree").jstree('open_node', targetRoot);
					}
				}
				$('#tree').jstree('select_node', categoryUuid);
			}
			$('#categorySelectorModal').modal('show');
		});
	} else {
		$('#tree').jstree('select_node', categoryUuid);
		$('#categorySelectorModal').modal('show');
	}
}

var edit_row;

function oprationCell(cell, data, uuid) {
	var video_status = data['video_status']
	var parent = $(cell);
	parent.empty();
	var item = '';
	// var video_status = oData['video_status']
	if (video_status == 1) {
		// item = $('<a href="javascript:void(0);">下线</a>');
		item = $('<button class="ladda-button btn btn-warning" data-style="zoom-in">下线</button>');
	} else if (video_status == 0) {
		// item = $('<a href="javascript:void(0);">审核</a>');
		item = $('<button class="ladda-button btn btn-danger" data-style="zoom-in">审核</button>');
	} else if (video_status == 2) {
		// item = $('<a href="javascript:void(0);">上线</a>');
		item = $('<button class="ladda-button btn btn-primary" data-style="zoom-in">上线</button>');
	}
	var item_ladda = item.ladda();
	item_ladda.click(function() {
		item_ladda.ladda('start');
		$.post('/asset/t/video/status', {
			uuid : uuid
		}, function(data) {
			item_ladda.ladda('stop');
			if (data.status == 'success') {
				table.refreshItem(data);
			} else {
				alert(data.status)
			}
		});
	});
	parent.append(item);

	// 编辑
	var edit = $('<a style="margin-left:10px" href="javascript:void(0);">编辑</a>')
	edit.click(function() {
		/*
		 * if(video_status == 1){ alert('请先将视频下线再编辑'); return ; }
		 */
		$('#videoEditModal').attr("data-id", uuid);
		edit_row = parent.parent();
		edit_speaker = data.speaker;
		$('#videoEditModal').modal('show');
	})
	parent.append(edit);

	/*
	 * var answer = $('<a style="margin-left:10px"
	 * href="javascript:void(0);">投票</a>'); answer.bind('click',
	 * answerClick(uuid)); parent.append(answer);/*
	 * 
	 * /** 2015-10-13 视频只有处于上线的状态才能编辑，最新最热
	 */
	// if (video_status == 1) {
	//
	// // 最热
	// var hottest = data['hottest'];
	// var hottest_tex = '';
	// if (hottest == 1) {
	// hottest_tex = '取消最热'
	// } else {
	// hottest_tex = '最热';
	// }
	// var hottest_item = $('<a style="margin-left:10px"
	// href="javascript:void(0);">'
	// + hottest_tex + '</a>');
	// hottest_item.click(function() {
	// $.post('/project/video/change/host/new', {
	// type : 1,
	// uuid : uuid
	// }, function(data) {
	// if (data.status == 'success') {
	// refreshItem(parent.parent().children(), data);
	// } else {
	// if (!!data.message) {
	// alert(data.message);
	// } else {
	// alert("状态更新失败");
	// }
	// }
	// });
	// });
	// parent.append(hottest_item);
	//
	// var newest = data['newest'];
	// var newest_tex = '';
	// if (newest == 1) {
	// newest_tex = '取消最新'
	// } else {
	// newest_tex = '最新';
	// }
	// var newest_item = $('<a style="margin-left:10px"
	// href="javascript:void(0)">'
	// + newest_tex + '</a>');
	// newest_item.click(function() {
	// $.post('/project/video/change/host/new', {
	// type : 2,
	// uuid : uuid
	// }, function(data) {
	// if (data.status == 'success') {
	// refreshItem(parent.parent().children(), data);
	// } else {
	// if (!!data.message) {
	// refreshItem(parent.parent().children(), data);
	// } else {
	// alert("状态更新失败");
	// }
	// }
	// });
	// });
	// }
	// parent.append(newest_item);
}

var edit_speaker;

/*
 * function answerClick(uuid) { // 跳转投票页面 return function() { // location.href =
 * "/project/resource/answer/page/" + uuid; location.href = "/project/resource/" +
 * uuid + "/vote/mgr"; } }
 */
