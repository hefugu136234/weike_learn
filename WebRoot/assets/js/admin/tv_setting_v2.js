var part;
$(document).ready(function() {
	activeStub('nav_tv_settings');

	$('#add-widget-btn').click(function() {
		addNewWidget('NORMAL')
	});

	$('#add-resource-btn').click(function() {
		addNewWidget('RESOURCE')
	});

	function addNewWidget(type) {
		var handler_layout_id = getHandlerLayoutId();
		var scriptId = makeScriptId();
		var widget = new_widget(makeScriptId())
		widget.x = 0;
		widget.y = 0;
		widget.handler = handler_layout_id;
		widget.type = type
		widget.saved = false
		if (type == 'RESOURCE') {
			widget.offset_x = 18;
			widget.offset_y = 5;
		} else {
			widget.offset_x = 5;
			widget.offset_y = 5;
		}
		widget.handler = getHandlerLayoutId()
		Layout.prototype.get(handler_layout_id).addWidget(widget);
	}

	// 初始化serch
	Ref.prototype.search.init('resource')
	Ref.prototype.search.init('activity')
	Ref.prototype.search.init('broadcast')
	var selector = $('#ref_type_selector');
	selector.change(function(e) {
		toggleRef(new Ref($(this).val()))
	})

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
	}).on('select_node.jstree', function(e, data) {
		if (data && data.selected && data.selected.length) {
			current_node = data.node;
		}
	});

	// 实例化上传组件
	part = new Part($('.uploader-component'), 0);
	uploaderInit(part.init());

	// 提交保存
	$('#save_widgets_btn').click(function(e) {
		var handler = getHandlerLayoutId();
		var tmp = [];

		// for(var i = 0 ; i < widgets_data.length; i++){
		// var e = widgets_data[i];
		// e.refreshPosition($('#' + e.scriptId))
		// var clone = jQuery.extend(true, {}, e);
		// console.log(clone)
		// if(handler == clone.handler && clone.savePrettier()){
		// tmp.push(clone);
		// }else{
		// alert('有未设置关联值的widget')
		// return;
		// }
		// }
		var commitable = true;
		$.each(widgets_data, function(i, e) {
			if (e.handler == handler) {
				// 重新刷新position
				e.refreshPosition($('#' + e.scriptId))
				var clone = jQuery.extend(true, {}, e);
				console.log(clone.ref == undefined)
				if (clone.savePrettier()) {
					tmp.push(clone);
				} else {
					commitable = false;
				}
			}
		})
		if (!commitable) {
			alert('有未设置正确值的widget');
			return;
		}
		if (tmp.length == 0 && !confirm('确定保存为空的布局？')) {
			return;
		}

		//test
		console.log(JSON.stringify(tmp));
		
		$.post('/project/tv/layout/save', {
			uuid : handler,
			dataJson : JSON.stringify(tmp)
		}).always(function(data) {
			if (data.status == 'success') {
				alert('保存成功')
				location.reload();
			} else {
				if (!isBlank(data.message)) {
					alert(data.message)
				} else {
					alert('保存失败')
				}
			}
		})
		// }
	})
	initEnv();

})

var count = 0;
function makeScriptId() {
	count += 1;
	return 'widget-' + count;
}

Ref.prototype.search = {
	init : function(type) {
		$('#selector_' + type.toLowerCase()).ajaxChosen(
				{
					dataType : 'json',
					type : 'GET',
					url : '/project/tv/widget/setting/reference?refType='
							+ type.toUpperCase(),

				}, {
					loadingImg : '/assets/img/loading.gif'
				})
		$('#selector_' + type + '_chosen').width(320)
	},
	show : function(type) {
		$('#selector_' + type.toLowerCase() + '_chosen').parent().show()
	},
	hide : function(type) {
		$('#selector_' + type.toLowerCase() + '_chosen').parent().hide();
	}
}

function showSettingModal(widget) {
	var setting_modal = $('#tv_widget_setting_modal');
	widget.prepareShow();
	setting_modal.modal('show');
	setting_modal.unbind('shown.bs.modal')
	setting_modal.bind('shown.bs.modal', function() {
		initConfirmBtn(widget);
	})
}

function initConfirmBtn(widget) {
	var btn = $('#confirm_btn');
	btn.unbind('click');
	btn.click(function() {
		widget.imageUrl = part.url;
		widget.mark = $('#mark').val()
		var ref = new Ref($('#ref_type_selector').val());
		if (ref.makeValue()) {
			widget.ref = ref;
			$('#tv_widget_setting_modal').modal('hide');
			// 如果未设置，则用之前的配置
		} else if (widget.ref != undefined && widget.ref.isOk()) {
			$('#tv_widget_setting_modal').modal('hide');
		} else {
			alert('请设置正确的关联值')
		}
		widget.pretty(); 
	})

}

function selectorInit(ref, bundle) {
	var selector = $('#ref_type_selector');
	selector.val(ref.type)
	if (bundle) {
		selector.attr("disabled", "disabled");
	} else {
		selector.show();
		selector.removeAttr("disabled");
	}
	toggleRef(ref)
}

function toggleRef(ref) {
	var selector = $('#selector_container');
	if (isBlank(ref.type)) {
		selector.hide()
	} else {
		selector.show();
	}
	selector.find('.ref-controller').hide();
	if ('RESOURCE' == ref.type) {
		selector.find('.control-label').text('选择资源')
		Ref.prototype.search.show(ref.type)
	} else if ('ACTIVITY' == ref.type) {
		selector.find('.control-label').text('选择活动')
		Ref.prototype.search.show(ref.type)
	} else if ('CATEGORY' == ref.type) {
		selector.find('.control-label').text('选择学科')
		$('#tree_container').show()
	} else if ('BROADCAST' == ref.type) {
		selector.find('.control-label').text('选择直播')
		Ref.prototype.search.show(ref.type)
	}
	// Ref.prototype.current = ref;
}

function initEnv() {
	if (0 == Layout.prototype.pool.length) {
		alert('首页布局还未初始化，请联系管理员!');
		return;
	}
	$.each(Layout.prototype.pool, function(i, e) {
		e.init();
		e.load();
	})
}

function Layout(uuid, widgets, projectUuid) {
	console.log(widgets);

	this.projectUuid = projectUuid;
	this.gridster = null;
	this.uuid = uuid;
	this.widgets = widgets
	var getElement = function() {
		return $('#' + uuid)
	}
	this.load = function() {
		var $this = this;
		$.each(this.widgets, function(i, e) {
			// 重新定义scriptId
			e.scriptId = makeScriptId();
			var w = new Widget(e.scriptId, e.type, e.imageUrl, '',
					parseInt(e.x), parseInt(e.y), parseInt(e.offset_x),
					parseInt(e.offset_y), e.mark)
			w.saved = true;
			if (e.ref) {
				//w.ref = new Ref(e.ref.type, e.ref.uuid)
				w.ref = new Ref(e.ref.type, e.ref.uuid, e.ref.overView) //xm
				if (w.ref.isCategory()) {
					// 保存更改前的ref
					w.savedRef = w.ref
				}
				$this.addWidget(w)
			}
		})
	}
	this.addWidget = function(widget) {
		widget.handler = this.uuid;
		var lay = this;
		addOrUpdateWidget(widget);
		init_widget_ui(lay, widget)
		// this.onChanged()
	}
	this.delWidget = function(widget) {
		this.onChanged()
	}
	this.init = function() {
		var l = this;
		this.gridster = getElement().find('.gridster ul').gridster({
			widget_selector : "li",
			widget_base_dimensions : [ 10, 10 ],
			widget_margins : [ 10, 10 ],
			resize : {
				enabled : true,
				start : function(e, ui, $widget) {
				},
				resize : function(e, ui, $widget) {
					showSize($widget);
					l.onChanged();
				},

				stop : function(e, ui, $widget) {
					showSize($widget)
				}
			}
		}).data('gridster');
	}
	this.onChanged = function() {
	}
	Layout.prototype.pool.push(this)
}

function getHandlerLayoutId() {
	return $('.tab-pane').parent().find('.active').attr('id');
}

function init_widget_ui(layout, widget) {
	if (widget.length > 0) {
		console.log(widget['ref']['uuid']);
	}

	addOrUpdateWidget(widget);

	var w = $('<li class="widget-item" id="' + widget.scriptId + '">'
				+ '<a href="#" data-toggle="modal" data-target="#settingModal" data-id="' + widget.scriptId
																		+ '" style="display:none" class="gs_setting">设  置</a>'
					+ '<span style="display:none;" class="gs_remove_handle">×</span>'
					+ '<span class="widget-size"></span>' 
					+ '<div class="" id="resourceDetail"></div>'
			+ '</li>');

	layout.gridster.add_widget(w, widget.offset_x, widget.offset_y, widget.x,
			widget.y);
	var child;
	if (widget.saved && widget.ref && widget.ref.type == 'CATEGORY') {
		$('#tv_home_mgr').attr("data-id", widget.ref.uuid);

		tmp = widget.ref.uuid;

		// 旧子页面定制入口
		/*
		 * child = $('<a target="_blank" class="child-setting"
		 * href="/tv/layout/' + widget.ref.uuid + '/list">子页订制</a>');
		 */

		// 新子页面定制入口
		child = $('<a class="child-setting" href="#">子页订制</a>');
		child.click(function(e) {
			e.preventDefault();
			if (widget.doseRefChanged()) {
				if (!widget.ref.isCategory()) {
					alert('只有关联分类才能设置子布局')
					return;
				}
				alert('绑定分类已更改,需保存之后才能设置该组件的子布局')
				return;
			}
			$('#tv_home_mgr').modal('show');
			$('#tv_home_mgr').unbind('shown.bs.modal')
			$('#tv_home_mgr').on('shown.bs.modal', function() {
				loadWidgetLabels(widget['ref']['uuid'], layout['projectUuid']);
				initSaveBtn(widget['ref']['uuid'], layout['projectUuid']);
			})
		})

		w.append(child)
	}
	var close = w.find('.gs_remove_handle');
	var setting = w.find('.gs_setting');
	var px_tips = w.find('.widget-size');
	var res_detail = w.find('.col-md-7');
	w.hover(function() {
		$(res_detail).show();
		$(close).show();
		$(setting).show();
		if (child) {
			child.show();
		}
		px_tips.show();
		
		// w.css("border", "1px solid green");

	}, function() {
		$(close).hide();
		$(setting).hide()
		if (child) {
			child.hide();
		}
		px_tips.hide();
		// w.css("border", "none");
	});
	setting.click(function() {
		showSettingModal(widget);
	})
	w.find('.gs_remove_handle').click(function() {
		layout.gridster.remove_widget(w);
		delWidget(widget.scriptId)
		layout.delWidget();
	})
	// if (widget.imageUrl || widget.categoryId) {
	// prefect_widget_ui(widget)
	// }
	showSize(w);
	if (widget.type == 'RESOURCE') {
		resizeDisable(w);
	}
	widget.pretty();
}

var search = function(ref) {

}

function Ref(type, uuid, overView) {
	this.type = type;
	this.uuid = uuid;
	this.overView = overView;
	this.makeValue = function() {
		if (this.isResource()) {
			this.uuid = $('#selector_resource').val();
			var tmp = new OverView();
			tmp.init(this.uuid);
			this.overView = tmp;
		} else if (this.isActivity()) {
			this.uuid = $('#selector_activity').val()
		} else if (this.isCategory()) {
			this.uuid = $('#tree').jstree('get_selected')[0];
		} else if (this.isBroadcast()) {
			this.uuid = $('#selector_broadcast').val()
		}
		return this.isOk();
	}
	this.isOk = function() {
		return !isBlank(this.uuid) && this.uuid.length > 20;
	}
	this.isCategory = function() {
		return this.type == 'CATEGORY'
	}

	this.isActivity = function() {
		return this.type == 'ACTIVITY'
	}

	this.isResource = function() {
		return this.type == 'RESOURCE'
	}

	this.isBroadcast = function() {
		return this.type == 'BROADCAST'
	}

	this.equals = function(target) {
		if (target == undefined) {
			return false;
		}
		return this.type == target.type && this.uuid == target.uuid
	}

}

function referenceInit(ref) {
	if (ref == undefined)
		return;
	if (ref.type == 'RESOURCE' || ref.type == 'ACTIVITY'
			|| ref.type == 'BROADCAST') {
		$.get('/project/tv/widget/reference/detail', {
			refType : ref.type,
			uuid : ref.uuid
		}).always(
				function(data) {
					if (data.status == 'success') {
						var selector = $('#selector_' + ref.type.toLowerCase())
						var chosen = $('#selector_' + ref.type.toLowerCase()
								+ '_chosen')
						var item = data.data
						if (item) {
							selector.val(data.data.id)
							$('span', chosen).text(data.data.text)
						} else {
							alert('请求关联数据失败,请重新关联')
						}
					} else {
						alert('请求关联数据失败,请重新关联')
					}
				})
	} else if (ref.type == 'CATEGORY') {
		var tree = $('#tree');
		tree.jstree().deselect_all();
		// 获取当前的选择
		tree.jstree(true).select_node(getNodeById(ref.uuid))
	}

}

// 禁用resize
function resizeDisable(widget) {
	var component = widget.find('.gs-resize-handle');
	if (component != 'undefine') {
		component.hide();
	}
}

function showSize($widget) {
	var widget_size = $widget.find('.widget-size');
	widget_size.text($widget.width() * 1.5 + 'px × ' + $widget.height() * 1.5
			+ 'px')
	// var widget = widgets_data[$widget.attr("id")]
	// console.log(widget)
	// if (widget) {
	// widget.refreshPosition($widget);
	// }
}

var widgets_data = {};
function Widget(scriptId, type, imageUrl, categoryId, x, y, offset_x, offset_y,
		mark, handler) {
	this.scriptId = scriptId;
	this.type = type;
	this.imageUrl = imageUrl;
	this.categoryId = categoryId;
	this.x = x;
	this.y = y;
	this.offset_x = offset_x;
	this.offset_y = offset_y;
	this.mark = mark
	this.handler = handler;
	this.refreshPosition = function(element) {
		this.x = parseInt(element.attr("data-col"))
		this.y = parseInt(element.attr("data-row"))
		this.offset_x = parseInt(element.attr("data-sizex"))
		this.offset_y = parseInt(element.attr("data-sizey"))
	}
	this.prepareShow = function() {
		var ref = $.extend({}, this.ref)
		if (ref.type == undefined) {
			if (this.type == 'RESOURCE') {
				ref = new Ref(this.type);
			} else {
				ref = new Ref('CATEGORY');
			}
		}
		selectorInit(ref, this.type == 'RESOURCE');
		part.renderPreview(this.imageUrl);
		$('#mark').val(isBlank(this.mark) ? "" : this.mark)
		referenceInit(this.ref);
	}
	this.pretty = function() {
		var widget_ui = $('#' + this.scriptId);
		if (pattern.test(this.imageUrl)) {
			widget_ui.css('background-image', 'url(' + this.imageUrl + ')');	//背景图
			widget_ui.css('background-repeat', 'no-repeat');	//背景图片是否拉伸
			widget_ui.css('background-size', 'contain');	//设置背景图片尺寸，完全适应
			
			widget_ui.find('#resourceDetail').empty();
			
			if(this.ref.type == 'RESOURCE'){
				widget_ui.find('#resourceDetail').append('<span></span><br>');
				widget_ui.find('#resourceDetail').append(
					'<div class="row">' + 
						//'<div class="col-md-5"><img src="' + this.imageUrl + '" width="100%" height="'+ widget_ui.outerHeight() +'" /></div>' + 
						'<div class="col-md-5"></div>' + 
						'<div class="col-md-7 text-left" id="detail" style="line-height:20px;padding:20px!important;"></div>' + 
					'</div>'	
				)
			}
		}
		
		if(!!this.ref){
			widget_ui.find('#detail').empty();
			var resOverView = this.ref.overView;
			if(!!resOverView && !!resOverView['name']){
				widget_ui.find('#detail').append('<p>名称：' + resOverView['name'] + '</p>');
			}
			if(!!resOverView && !!resOverView['speaker']){
				widget_ui.find('#detail').append('<p>讲者：' + resOverView['speaker'] + '</p>');
			}
			if(!!resOverView && !!resOverView['type']){
				widget_ui.find('#detail').append('<p>类型：' + resOverView['type'] + '</p>');
			}
		}
	}

	this.savePrettier = function() {
		var widget_ui = $('#' + this.scriptId);
		// 验证
		if (this.ref == undefined || !this.ref.isOk()) {
			widget_ui.css('border', '1px red solid');
			return false;
		} else {
			delete this.saved;
			delete this.savedRef;
			delete this.scriptId;
			widget_ui.css('border', '');
		}
		return true;
	}
	// this.savedCategory = '';
	// this.init = function(){
	// if (this.ref != undefined && this.ref.isOk()){
	// if(this.ref.isCategory()){
	// this.savedCategory = this.ref.uuid;
	// }
	// }
	// };
	this.saved = false;
	// 判断分类有没有变化
	this.doseRefChanged = function() {
		if (this.ref == undefined) {
			return this.savedRef != undefined
		}
		return !this.ref.equals(this.savedRef)
	}

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

/*--------2016-05-11 xm start--------*/
function loadWidgetLabels(categoryUuid, projectUuid) {
	var container = $('#data-container');
	container.empty();
	var tips = $('<span id="loading_tips">加载中...</span>');
	container.append(tips)
	tips.html('加载中...')
	tips.show();
	$.get('/project/tv/widget/labels', {
		categoryUuid : categoryUuid
	}).always(function(data) {
		tips.hide();
		if (data.status == 'success') {
			if (data.data && data.data.length > 0) {
				renderUi(container, data.data, projectUuid, categoryUuid)
			} else {
				tips.show();
				tips.html('没有数据');
			}
		} else {
			if (!!data.message) {
				alert(data.message)
			} else {
				alert('获取数据失败')
			}
		}
	})
}

var layoutLen = 0;
function renderUi(container, data, projectUuid, categoryUuid) {
	var table = $('<table class="table table-striped"></table>')
	var head = '<thead><tr><td class="lbname">名称</td><td class="lbdate">创建时间</td><td class="lbstatus">状态</td><td class="lbop">操作</td></tr></thead>';
	container.append(table)
	table.append(head)
	var body = $('<tbody></tbody>')
	table.append(body)
	$.each(data, function(i, e) {
		var item = $('<tr></tr>')
		body.append(item);
		renderItem(e, item, i, projectUuid, categoryUuid);
	})
	layoutLen = data.length;
	widgetSubNext(categoryUuid, projectUuid);
}

function renderItem(e, tr, index, projectUuid, categoryUuid) {
	var name = '<td>' + e.name + '</td>';
	var time = '<td>' + e.createDate + '</td>';
	var status = '<td>' + (e.status == 2 ? '不可用' : '可用') + '</td>';
	var op = $('<td></td>');
	var del = $('<a>删除  </a>');
	op.append(del);

	// 20160510 -->xm
	var cell = $('<td></td>');
	var toTop = $('<a>置顶</a>');
	if (0 != index) {
		cell.append(toTop);
	}

	tr.append(name).append(time).append(status).append(op).append(cell);

	// 删除操作
	del.click(function(event) {
		event.preventDefault();
		if (!confirm("确定删除?")) {
			return;
		}
		$.post('/project/tv/home/label/del_editor', {
			uuid : e.uuid
		}).always(function(data) {
			if (data.status == 'success') {
				tr.remove();
				loadWidgetLabels(categoryUuid, projectUuid);
				layoutLen--;
				var tvSetNext = $('#tvSet_next');
				if (0 >= layoutLen) {
					tvSetNext.unbind('click');
				}
			} else {
				if (isBlank(data.message)) {
					alert('删除失败')
				} else {
					alert(data.message)
				}
			}
		})
	})

	// 置顶
	toTop.click(function(event) {
		event.preventDefault();
		$.post('/project/tv/home/label/toTop', {
			uuid : e.uuid,
			projectUuid : projectUuid,
			categoryUuid : categoryUuid
		}).always(function(data) {
			if (data.status == 'success') {
				loadWidgetLabels(categoryUuid, projectUuid);
			} else {
				if (isBlank(data.message)) {
					alert('操作失败')
				} else {
					alert(data.message)
				}
			}
		})
	})
}

function initSaveBtn(categoryUuid, uuid) {
	var save_btn = $('#save-btn');
	save_btn.unbind('click');
	$('#save-btn').click(function(e) {
		var name = $('#label_name').val();
		if (isBlank(name)) {
			alert('请输入值板块名')
		} else {
			$.post('/project/tv/widget/label/save', {
				categoryUuid : categoryUuid,
				projectUuid : uuid,
				name : name
			}).always(function(data) {
				if (data.status == 'success') {
					$('#label_name').val('')
					loadWidgetLabels(categoryUuid, uuid);
				} else {
					if (isBlank(data.message)) {
						alert('保存失败')
					} else {
						alert(data.message)
					}
				}
			})

		}
	})
}

function widgetSubNext(categoryUuid, projectUuid) {
	var tvSetNext = $('#tvSet_next');
	tvSetNext.unbind('click');
	if (0 < layoutLen) {
		tvSetNext.click(function(e) {
			location.href = "/project/tv/home/settings/v2Sub?categoryUuid="
					+ categoryUuid + "&projectUuid=" + projectUuid;
		})
	} else {
		tvSetNext.unbind('click');
	}
}

/*--------2016-05-11 xm end--------*/

function OverView(name, category, speaker, description, type){
	this.name = name;
	this.category = category;
	this.speaker = speaker;
	this.description = description;
	this.type = type;
	this.init = function(resourceUuid){
		$this = this;
		$.ajax({
	        type: "GET",
	        async: false,	//设置该ajax同步运行
	        url: '/project/resource/' + resourceUuid ,
	        success: function(data) {
				if (data.status == 'success') {
					var result = data['data'];
					console.log(result);
					
					if(!!result['name']){
						$this.name = result['name'];
					}
					/*if(!!result['mark']){
						$this.description = result['mark'];
					}
					if(!!result['category'] && !!result['category']['name']){
						$this.category = result['category']['name'];
					}*/
					if(!!result['speaker'] && !!result['speaker']['name']){
						$this.speaker = result['speaker']['name'];
					}
					if(!!result['type']){
						$this.type = result['type'];
					}
				} else {
					alert('请求关联数据失败,请重新关联')
				}
	        }
	    });
	}
}
