var gridster;
$(function() {
	var count = 0;
	// 第一次进来加载首页
	$.get('/tv/home/structure?timestamp=' + new Date().getTime(), function() {
		$('#fresh_loading_tips').show();
	}).done(function(data) {
		$.each(data, function(index, element) {
			if (element.scriptId) {
				count = index + 1;
				addOrUpdateWidget(element)
				init_widget_ui(element)
			}
		})

	}).fail(function() {

	}).always(function(data) {
		if (data.status) {
			alert(data.status);
		}
		$('#fresh_loading_tips').hide();
		$('#add_widget_btn').prop("disabled", false);
	})
	gridster = $(".gridster ul").gridster({
		widget_selector : "li",
		widget_base_dimensions : [ 20, 20 ],
		widget_margins : [ 2, 2 ],
		resize : {
			enabled : true,
			start : function(e, ui, $widget) {
				// console.log($widget.attr("data-sizex"));
			},
			resize : function(e, ui, $widget) {
				// console.log('RESIZE offset: ' + ui.pointer.diff_top
				// + ' ' + ui.pointer.diff_left);
			},

			stop : function(e, ui, $widget) {
				// console.log('STOP position: ' + ui.position.top + ' '
				// + ui.position.left);
			}
		}
	}).data('gridster');
	$('#add_widget_btn').click(function() {
		count++;
		var scriptId = 'widget-' + count
		var widget = new_widget(scriptId)
		widget.x = 0;
		widget.y = 0;
		widget.offset_x = 5;
		widget.offset_y = 5;
		addOrUpdateWidget(new_widget(scriptId))
		init_widget_ui(widget)
	});

	function init_widget_ui(widget) {
		var w = $('<li class="widget-item" id="'
				+ widget.scriptId
				+ '"><a href="#" data-toggle="modal" data-target="#settingModal" data-id="'
				+ widget.scriptId
				+ '" style="display:none" class="gs_setting">设置子组件</a> <span style="display:none;" class="gs_remove_handle">x</span></li>');
		gridster.add_widget(w, widget.offset_x, widget.offset_y, widget.x,
				widget.y);
		var close = w.find('.gs_remove_handle');
		var setting = w.find('.gs_setting')
		w.hover(function() {
			$(close).show();
			$(setting).show();
			w.css("border", "1px solid green");
		}, function() {
			$(close).hide();
			$(setting).hide()
			w.css("border", "none");
		});
		w.find('.gs_remove_handle').click(function() {
			gridster.remove_widget(w);
			delWidget(widget.scriptId)
		})
		if (widget.imageUrl || widget.categoryId) {
			prefect_widget_ui(widget)
		}
	}

	var effected_widget = '';
	$('#settingModal').on(
			'show.bs.modal',
			function(e) {
				effected_widget = $(e.relatedTarget).data('id');
				var widget_obj = widgets_data[effected_widget]
				$('#image-preview').attr('src', '');
				$('#image-preview').hide();
				if (widget_obj) {
					var url = widget_obj.imageUrl;
					if (url != "" && typeof url != 'undefined') {
						$('#image-preview').show();
						$('#image-preview').attr('src', url);
					}
					// 清除所有之前的选择
					$('#tree').jstree().deselect_all()
					// 获取当前的选择
					$('#tree').jstree(true).select_node(
							getNodeById(widget_obj.categoryId))
				}
			})
	$('#settingModal').on('hide.bs.modal', function(e) {

	})

	$('#setting_confirm_btn').click(function() {
		$('#settingModal').modal('hide');
		var image_url = $('#image-preview').attr('src')
		var widget = new_widget(effected_widget);
		widget.imageUrl = image_url;
		widget.categoryId = $('#tree').jstree('get_selected')[0];
		addOrUpdateWidget(widget)
		prefect_widget_ui(widget);
	})
	function prefect_widget_ui(widget) {
		var widget_ui = $('#' + widget.scriptId);
		widget_ui.css('background-image', 'url(' + widget.imageUrl + ')');
		widget_ui.css('background-repeat', 'no-repeat');
		widget_ui.css('background-size', 'contain');
	}

	$('#save_widgets_btn').click(
			function() {
				var data = [];
				var all_widgets = gridster.$widgets
				$.each(all_widgets, function(index, element) {
					var ele = $(element);
					var scriptId = ele.attr("id");
					console.log(ele)
					girdWidget(scriptId, ele.data('col'), ele.data('row'), ele
							.data('sizex'), ele.data('sizey'));
					console.log(new_widget(scriptId))
				});
				var keys = Object.keys(widgets_data);
				var ok = true;
				$.each(keys, function(i, e) {
					data.push(widgets_data[e]);
					var widget = widgets_data[e];
					console.log('===' + widget.categoryId)
					if (typeof widget.categoryId === 'undefined'
							|| widget.categoryId.length < 10) {
						$('#' + e).css("border", "1px solid red");
						ok = false;
					}
				});
				if (ok) {
					$.post("/tv/home/save", {
						data_json : JSON.stringify(data)
					}).done(function(data) {
						alert(data.status);
					}).fail(function() {
						alert("fail");
					}).always(function() {
					});
				}
			});
	showActive([ 'nav_banner_setting','holder_project' ]);
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

		'plugins' : [ 'state' ]
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
	$('#upload').click(function() {
		$('#uploadify').uploadifyUpload();
		return false;
	});
	
//	$('#preview').click(function(){
//		console.log($(".gridster"))
//		$(".gridster").hide()
//	})
	
	
});
var widgets_data = {};
function Widget(scriptId, imageUrl, categoryId, x, y, offset_x, offset_y) {
	this.scriptId = scriptId;
	this.imageUrl = imageUrl;
	this.categoryId = categoryId;
	this.x = x;
	this.y = y;
	this.offset_x = offset_x;
	this.offset_y = offset_y;
}

function addOrUpdateWidget(widget) {
	widgets_data[widget.scriptId] = widget;
}

function new_widget(scriptId) {
	var w = widgets_data[scriptId];
	if (w) {
		return w;
	}
	w = new Widget(scriptId);
	addOrUpdateWidget(w);
	return w;
}

function delWidget(scriptId) {
	delete widgets_data[scriptId];
}

function girdWidget(scriptId, x, y, offset_x, offset_y) {
	var widget = widgets_data[scriptId];
	if (widget) {
		widget.x = x;
		widget.y = y;
		widget.offset_x = offset_x;
		widget.offset_y = offset_y;
		return true;
	}
	return false;
}

function getNodeById(id) {
	var all = $('#tree').jstree();
	return all._model.data[id];
}