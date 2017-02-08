$(document).ready(function() {
	showActive([ 'video_mrg_nav', 'assets-mgr' ]);

	jQuery.validator.addMethod("val_empty", function(value, element) {
		return !!value;
	}, "请先赋一个值");
	var validator = $('#pdf_form').validate({
		ignore : ".ignore",
		errorElement : 'span',
		errorClass : 'help-block',
		focusInvalid : false,
		rules : {
			taskId : {
				val_empty : true
			},
			categoryUuid : {
				val_empty : true
			}
		},
		messages : {
			taskId : "请先上传PDF文件",
			name : "请输入PDF的名称.",
			categoryUuid : "请先选择一个分类",
		},

		highlight : function(element) {
			$(element).closest('.form-group').addClass('has-error');
		},

		success : function(label) {
			label.closest('.form-group').removeClass('has-error');
			label.remove();
		},

		errorPlacement : function(error, element) {
			element.parent('div').append(error);
		}
	});
	
	$('#new_ques').click(function(){
		buildQuestionConstrus();
	});

});

/*<div class="form-group">
<label class="col-sm-3 control-label">问题：</label>
<div class="col-sm-6">
	<table class="answer_table">
		<tr>
			<td><span class="radio"><label><input
						type="radio" name="select_answer1" value="0"
						style="margin-top: 10px;">单选</label> <label><input
						type="radio" name="select_answer1" value="0">多选</label></span></td>
			<td><input type="text" class="form-control"
				maxlength="280"></td>
			<td><a>新增选项</a></td>
			<td><a>删除</a></td>
		</tr>
		<tr>
			<td>选项：</td>
			<td><input type="text" class="form-control"
				maxlength="280"></td>
			<td><a>删除</a></td>
		</tr>
		<tr>
			<td>选项：</td>
			<td><input type="text" class="form-control"
				maxlength="280"></td>
			<td><a>删除</a></td>
		</tr>
		<tr>
			<td>选项：</td>
			<td><input type="text" class="form-control"
				maxlength="280"></td>
			<td><a>删除</a></td>
		</tr>
		<tr>
			<td>选项：</td>
			<td><input type="text" class="form-control"
				maxlength="280"></td>
			<td><a>删除</a></td>
		</tr>
	</table>
</div>
</div>*/

//穿建一个新的问题模式
function buildQuestionConstrus(){
	var contain=$('#qustion_contain');
	//创建的最外层的div
	var first_div=$('<div class="form-group"></div>');
	//左边的标签
	var first_lable=$('<label class="col-sm-3 control-label">问题：</label>');
	//第2层里面的div
	var sen_div=$('<div class="col-sm-6"></div>');
	//第一个div将2个结构添加进去
	first_div.append(first_lable);
	first_div.append(sen_div);
	//控制层div，添加整个结构
	contain.append(first_div);
}

//第2层里面的div,中添加table
function buildFrist(sen_div){
	var item= $('<table class="answer_table"></table>');
}

//创建问题的结构
function buildQuestion(table){
	
}

// 修改初始化的数据
function nodeChage(node) {
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
