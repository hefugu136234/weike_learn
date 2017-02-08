var vote_total_area, vote_contain, sim_item, sim_btn;
$(function(){
	var uuid = $('#res_uuid').val();
	var fileId = $('#fileId').val();
	vote_total_area = $('#vote_total_area');
	vote_contain = $('#vote_contain');
	sim_item = $('#sim_item');
	sim_btn = $('#sim_btn');
	
	// 获取投票信息
	getPullVoteData(uuid);
	
	// 绑定投票事件
	sim_btn.click(function(e) {
		e.preventDefault();
		postVoteData(uuid);
	});
	
});

//获取投票信息
function getPullVoteData(re_uuid) {
	$.getJSON('/f/web/res/get/vote/data', {
		'uuid' : re_uuid,
		'timestamp' : new Date().getTime()
	}, function(data) {
		if (isSuccess(data)) {
			buildVoteCtrl(data);
		} else {
			console.log(data);
		}
	});
}


//处理获取的投票信息
function buildVoteCtrl(data) {
	var hasVote = data.hasVote;// 是否有投票
	if (!hasVote) {
		// 无
		vote_total_area.hide();
		return;
	}
	var questions = data.questions;// 题目
	if (!isItemList(questions)) {
		// 无投票
		vote_total_area.hide();
		return;
	}
	var abelVote = data.abelVote;// 是否可以投票
	buildTotal(questions, abelVote);

}

//循环总处理题目
function buildTotal(questions, abelVote) {
	if (abelVote) {
		sim_item.show();// 可以投票
	} else {
		sim_item.hide();// 不能投票
	}
	vote_contain.empty();
	// 编辑标题
	var box_div = '';
	$.each(questions, function(index, item) {
		box_div = box_div + buildSubject(index, item, abelVote);
	});
	vote_contain.append(box_div);

	// 处理选中框的事件
	dealskip();

	vote_total_area.show();
}

function dealskip() {
	var $i_checks_input = $('.i-checks');
	$i_checks_input.each(function(index, item) {
		var $item = $(item);
		iChecksSet($item);
		var disabled_item = $item.prop('disabled');
		var item_type = $item.attr('type');
		if (!disabled_item && item_type == 'checkbox') {
			// 增加监听点击事件ifChanged ifChecked
			$item.on({
				'ifChecked' : function(event) {
					optionClick($item, event.type);
				},
				'ifUnchecked' : function(event) {
					optionClick($item, event.type);
				}
			});
		}
	});
}

//每一个input 点击触发事件
function optionClick(item, event_type) {
	// console.log(event_type);
	// var checked=item.prop('checked');
	// if(!checked){
	// return false;
	// }
	var boxs = item.parents('div.box');
	if (!isItemList(boxs)) {
		return false;
	}
	var box_item = boxs[0];
	var item_type = $(box_item).data('type');
	if (item_type == 0) {
		return false;
	}
	// console.log(item_type);
	var checked_length = $(box_item).find("input[type=checkbox]:checked").length;
	console.log(checked_length);
	if (checked_length == item_type) {
		$(box_item).find("input[type=checkbox]").each(function(index, mitem) {
			var $m = $(mitem);
			if (!$m.prop('checked')) {
				$m.iCheck('disable');
			}
		});
	} else if (checked_length < item_type) {
		$(box_item).find("input[type=checkbox]").each(function(index, mitem) {
			var $m = $(mitem);
			if (!$m.prop('checked')) {
				$m.iCheck('enable');
			}
		});
	}
}

//绘制单项题目，单个box
function buildSubject(index, item, abelVote) {
	var ques_uuid = item.uuid;
	var ques_title = item.title;
	var ques_type = item.type;
	var ques_voted = item.voted;// true 投过 false//没投
	var ques_answerList = item.answerList;
	var ques_eq = index + 1;
	var ques_type_name = '';
	var ques_input_type = '';

	if (ques_type == 1) {
		ques_type_name = '【单选】';
		ques_input_type = 'radio';
	} else if (ques_type== 2) {
		ques_type_name = '【可选2项】';
		ques_input_type = 'checkbox';
	} else if (ques_type == 3) {
		ques_type_name = '【可选3项】';
		ques_input_type = 'checkbox';
	} else {
		ques_type_name = '【不限制】';
		ques_input_type = 'checkbox';
	}

	// abelVote 能否投票 true可以 false不可以
	//ques_abel 当前问题可以可以投 true 可以 false 不能
	var ques_abel = abelVote;
	if (abelVote) {
		// 能投时
		if (ques_voted) {
			// 投过
			ques_abel = false;
		} else {
			// 没投过
			ques_abel = true
		}
	}
	var ques_title_div = '<div class="tt">' + ques_eq + '.<span class="type">'
			+ ques_type_name + '</span>' + ques_title + '</div>';
	var ques_option_div = buildTotalOption(ques_answerList, ques_abel,
			ques_input_type, ques_uuid);

	var box_div = '<div class="box" data-uuid="' + ques_uuid + '" data-type="'
			+ ques_type + '">' + ques_title_div + ques_option_div + '</div>';
	return box_div;
}

//总的处理题目中的选项
function buildTotalOption(answerList, ques_abel, ques_input_type, ques_uuid) {
	// ques_abel true 可以投 false 不能投(可能投过了)
	if (!isItemList(answerList)) {
		return '';
	}
	var option_label = '';
	$.each(answerList, function(index, item) {
		option_label = option_label
				+ buildSingleOption(index, item, ques_abel, ques_input_type,
						ques_uuid);
	});
	var option_div = '<div class="list-group answer-list result">'
			+ option_label + '</div>';
	return option_div;
}

//绘制单个选项
function buildSingleOption(index, item, ques_abel, ques_input_type, ques_uuid) {
	var ans_uuid = item.uuid;
	var ans_option = item.option;
	var ans_voted = item.voted;// true 投过 false 没投
	var ans_votedCount = item.votedCount;
	var ans_precent = item.precent;
	var option_label_class = 'list-group-item item';
	var option_input = '<input type="' + ques_input_type + '" name="'
			+ ques_uuid + '" value="' + ans_uuid + '"'
			+ ' class="i-checks" data-skin="square" data-color="blue"';
	var option_span = '';
	if (!ques_abel) {
		// 不能投时，才判断 投没投过 加disabled
		option_input = option_input + ' disabled="disabled"';
		if (ans_voted) {
			option_input = option_input + ' checked="checked"';
			option_label_class = option_label_class + ' checked';
		}
		option_span = '<span class="badge">' + ans_precent + '<i class="num">('
				+ ans_votedCount + '票)</i></span>';
	}
	// else{
	// option_input=option_input+' onclick="optionClick(this);"';
	// }
	option_input = option_input + '/>';
	var option_label = '<label class="' + option_label_class + '">'
			+ option_input + ans_option + option_span + '</label>'
	return option_label;
}

/**
* 提交投票
*/
function postVoteData(re_uuid) {
	var boxs = vote_contain.find('div.box');
	if (!isItemList(boxs)) {
		alert('无投票选项');
		return false;
	}
	var data_flag = true;
	var curr_index = 0;
	var posts = [];
	boxs.each(function(P_index, p_item) {
		curr_index = P_index;
		var $p_item = $(p_item);
		var p_uuid = $p_item.data('uuid');
		var p_type = $p_item.data('type');
		var select_result = buildAnswerOfItem($p_item);
		//console.log(select_result);
		if (p_type != 0 && select_result.length > p_type) {
			// 数据出错
			data_flag = false;
			return false;
		}
		if (isItemList(select_result)) {
			var obj = buildVote(p_uuid, select_result);
			posts.push(obj);
		}

	});
	if (!data_flag) {
		curr_index = curr_index + 1;
		alert('第' + curr_index + '题选中的投票项超过可选项总数');
		return false;
	}
	if (!isItemList(posts)) {
		alert('请选中一题投票');
		return false;
	}

	var post_obj = new Object();
	post_obj.posts = posts;
	var vote_json = JSON.stringify(post_obj);
	
	sim_btn.prop('disabled',true);
	$.post('/f/web/res/vote/post/data', {
		'uuid' : re_uuid,
		'vote_json' : vote_json
	}, function(data) {
		sim_btn.prop('disabled',false);
		if (isSuccess(data)) {
			buildVoteCtrl(data);
		} else {
			alert(data.message);
		}
	});
}

//循环每一小题的答案
function buildAnswerOfItem($p_item) {
	var select_result = [];
	var select_op = $p_item.find('input:checked');
	if (!isItemList(select_op)) {
		return select_result;
	}
	select_op.each(function(index, item) {
		var $item = $(item);
		var uuid = $item.val();
		if (!$item.prop('disabled')) {
			//去掉之前已选中的
			select_result.push(uuid);
		}
	});
	return select_result;
}

function buildVote(s_uuid, selected_options) {
	var obj = new Object();
	obj.s_uuid = s_uuid;
	obj.selected_options = selected_options;
	return obj;
}