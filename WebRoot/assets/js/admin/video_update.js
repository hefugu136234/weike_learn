$(document).ready(function() {
	showActive([ 'video_mrg_nav', 'assets-mgr' ]);
	
	//封面上传（七牛）20160321
	var $e = $('#uploadify')
	uploaderInit(new Part($e, 1, function(part) {
	}, uploadFinished).init())
	function uploadFinished(src) {
		$('#cover_hidden').val(src)
		$('#uploadTag').val('uploadSuccess');
	}

	//回显视频封面
	echoVideoCover();
	//$('#image-preview').show();
	//$('#image-preview').attr('src', $('#_cover').val());
	
	/*
	//视频封面上传操作
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
			$('#image_preview').show();
			var json_data = JSON.parse(data);
			$('#image_preview').attr('src', json_data.url);
		}
	});*/
	
	//价格
	var priceInput = $('#price');
	var chinese_tips = $('#chinese_tips');
	$('#need_price').click(function() {
		priceInputShow();
	})
	function priceInputShow() {
		if ($('#need_price').is(':checked')) {
			priceInput.show();
			priceInput.unbind('input');
			chinese_tips.show();
			priceInput.bind('input', function() {
				chinese_tips.text(chinaCost($(this).val()))
			})
		} else {
			priceInput.hide();
			chinese_tips.hide();
		}
	}

	//讲者下拉框异步加载
	$('#spaker_selector').ajaxChosen({
        dataType: 'json',
        type: 'GET',
        url:'/project/threescreen/search/speaker'
	},{
        loadingImg: '/assets/img/loading.gif'
	});
	
	//回显分类详情
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
	}).on({
		'after_open.jstree' : function(e, data) {
			// 清空所有的选中
			$('#tree').jstree().deselect_all();
			// 打开选中
			var categoryUuid = $('#categoryUuid').val();
			if (!!categoryUuid) {
				var targetNode = $('#tree').jstree('get_node', categoryUuid);
				if (targetNode) {
					//如果有数据时，初始化
					nodeChage(targetNode);
					if (!targetNode.state.selected) {
						$('#tree').jstree('select_node', categoryUuid);
					}
				}
			}
		}
	});
	// 对话框每次出现时，初始化其中的数据
	$('#categorySelectorModal').on('show.bs.modal', function() {
		$('#tree').bind('changed.jstree', function(e, data) {
			var node = data.node;
			if (node) {
				var parents = node.parents;
				if (parents) {
					var val_text = '';
					$(parents).each(function(index, item) {
						var parent_node = $('#tree').jstree('get_node', item);
						val_text = fromText(val_text, parent_node);
					});
					$('#category_trace').empty();
					$('#categoryUuid').val('');
					if (!!val_text) {
						selectCate(val_text, node);
						//单个元素的验证
						//validator.element($('#categoryUuid'));
					}
				}
			}
		});
	});

	$('#categorySelectorModal').on('hide.bs.modal', function() {
		$('#tree').unbind('changed.jstree');
	});
});

//回显视频封面
function echoVideoCover(){
	var coverTaskId = $('#_cover').val();
	//test
	console.log(coverTaskId);
	
	var patt = new RegExp("^(http).*");
	if (!!coverTaskId) {
		var cover_url;
		if (patt.test(coverTaskId)) {
			cover_url = coverTaskId;
		} else {
			/* cover_url = "http://cloud.lankr.cn/api/image/" + taskId
					+ "?m/2/h/180/f/jpg"; */
			//modified by mayuan
			cover_url = "http://cloud.lankr.cn/api/image/" + coverTaskId
			+ "?m/2/h/180/f/jpg";
		}
		$('#image_preview').show();
		$('#image_preview').attr('src', cover_url);
		$('#cover').val(coverTaskId);
	}
}

//修改初始化的数据
function nodeChage(node){
	if (node) {
		var parents = node.parents;
		if (parents) {
			var val_text = '';
			$(parents).each(function(index, item) {
				var parent_node = $('#tree').jstree('get_node', item);
				val_text = fromText(val_text, parent_node);
			});
			if (!!val_text) {
				selectCate(val_text, node);
			}
		}
	}
}
// 页面显示
function fromText(val_text, node) {
	var id = node.id;
	var parent = node.parent;
	var text = node.text;
	if (id != '#' && !!parent && !!text) {
		val_text = text + ">" + val_text;
	}
	return val_text;
}
// 页面赋值
function selectCate(val_text, node) {
	var uuid = node.id;
	val_text = val_text + '<b>' + node.text + '</b>';
	$('#category_trace').html(val_text);
	$('#categoryUuid').val(uuid);
}


//价格转换
function chinaCost(numberValue) {
	var numberValue = new String(Math.round(numberValue * 100)); // 数字金额
	var chineseValue = ""; // 转换后的汉字金额
	var String1 = "零壹贰叁肆伍陆柒捌玖"; // 汉字数字
	var String2 = "万仟佰拾亿仟佰拾万仟佰拾元角分"; // 对应单位
	var len = numberValue.length; // numberValue 的字符串长度
	var Ch1; // 数字的汉语读法
	var Ch2; // 数字位的汉字读法
	var nZero = 0; // 用来计算连续的零值的个数
	var String3; // 指定位置的数值
	if (len > 15) {
		alert("超出计算范围");
		return "";
	}
	if (numberValue == 0) {
		chineseValue = "零元整";
		return chineseValue;
	}

	String2 = String2.substr(String2.length - len, len); // 取出对应位数的STRING2的值
	for (var i = 0; i < len; i++) {
		String3 = parseInt(numberValue.substr(i, 1), 10); // 取出需转换的某一位的值
		if (i != (len - 3) && i != (len - 7) && i != (len - 11)
				&& i != (len - 15)) {
			if (String3 == 0) {
				Ch1 = "";
				Ch2 = "";
				nZero = nZero + 1;
			} else if (String3 != 0 && nZero != 0) {
				Ch1 = "零" + String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			} else {
				Ch1 = String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			}
		} else { // 该位是万亿，亿，万，元位等关键位
			if (String3 != 0 && nZero != 0) {
				Ch1 = "零" + String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			} else if (String3 != 0 && nZero == 0) {
				Ch1 = String1.substr(String3, 1);
				Ch2 = String2.substr(i, 1);
				nZero = 0;
			} else if (String3 == 0 && nZero >= 3) {
				Ch1 = "";
				Ch2 = "";
				nZero = nZero + 1;
			} else {
				Ch1 = "";
				Ch2 = String2.substr(i, 1);
				nZero = nZero + 1;
			}
			if (i == (len - 11) || i == (len - 3)) { // 如果该位是亿位或元位，则必须写上
				Ch2 = String2.substr(i, 1);
			}
		}
		chineseValue = chineseValue + Ch1 + Ch2;
	}

	if (String3 == 0) { // 最后一位（分）为0时，加上“整”
		chineseValue = chineseValue + "整";
	}
	return chineseValue;
}
