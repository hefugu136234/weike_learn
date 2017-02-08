var total_vote_div,vote_content_div,vote_sumit_div,vote_sumit_but;
$(function(){
	var uuid=$('#resource_uuid').val();
	total_vote_div=$('#total_vote_div');
	vote_content_div=$('#vote_content_div');
	vote_sumit_div=$('#vote_sumit_div');
	vote_sumit_but=$('#vote_sumit_but');

	// 拉取投票信息
	getPullVoteData(uuid);

	// 绑定投票按钮
	vote_sumit_but.click(function(e){
		e.preventDefault();
		// 点击投票
		postVoteData(uuid);
	});
});

//获取投票信息
function getPullVoteData(uuid) {
	$.getJSON('/api/webchat/resoruce/get/vote/data', {
		'uuid':uuid,
		'timestamp' : new Date().getTime()
	}, function(data) {
		if (isSuccess(data)) {
			buildVoteCtrl(data);
		} else {
			console.log(data);
		}
	});
}

// 处理获取的投票信息
function buildVoteCtrl(data) {
	var hasVote = data.hasVote;// 是否有投票
	if (!hasVote) {
		// 无
		total_vote_div.hide();
		return;
	}
	var questions = data.questions;// 题目
	if (!isItemList(questions)) {
		// 无投票
		total_vote_div.hide();
		return;
	}
	var abelVote = data.abelVote;// 是否可以投票
	// 开始绘制投票
	buildTotal(questions, abelVote);
	total_vote_div.show();
}

// 循环总处理题目
function buildTotal(questions, abelVote) {
	if (abelVote) {
		vote_sumit_div.show();// 可以投票
	} else {
		vote_sumit_div.hide();// 不能投票
	}
	vote_content_div.empty();
	// 编辑标题
	var surveyList_div = '';
	$.each(questions, function(index, item) {
		surveyList_div = surveyList_div + buildSubject(index, item, abelVote);
	});
	vote_content_div.append(surveyList_div);
}

// 绘制每一个题目
function buildSubject(index,item,abelVote){
	var ques_uuid = item.uuid;
	var ques_title = item.title;
	var ques_type = item.type;
	var ques_voted = item.voted;// true 投过 false//没投
	var ques_answerList = item.answerList;
	var ques_eq = index + 1;
	var ques_type_name = '';
	var ques_input_class = '';

	if (ques_type == 1) {
		ques_type_name = '【单选】';
		ques_input_class = 'radio';
	} else if (ques_type == 2) {
		ques_type_name = '【可选2项】';
		ques_input_class = 'multiple';
	} else if (ques_type == 3) {
		ques_type_name = '【可选3项】';
		ques_input_class = 'multiple';
	} else {
		ques_type_name = '【不限制】';
		ques_input_class = 'multiple';
	}

	// abelVote 能不能够投，及有无投票的权限
	// ques_abel 当前问题可以可以投 true 可以 false 不能
	var ques_abel = abelVote;
	if(abelVote){
		// 当有权限
		if (ques_voted) {
			// 投过
			ques_abel = false;
		} else {
			// 没投过
			ques_abel = true
		}
	}
	// 投票包裹div
	var ques_box_div='<div class="survey-list" data-uuid="' + ques_uuid + '" data-type="'+ques_type+'">';
	// 投票的问题
	var ques_title_div='<h5 class="question-tt">'+ques_eq+'.<span class="' + ques_input_class + '">' + ques_type_name + '</span>'
	                   +ques_title+'</h5>';
	// 投票选项集合
	var ques_options=buildTotalOption(ques_answerList, ques_abel,
			ques_type, ques_uuid);
	ques_box_div=ques_box_div+ques_title_div+ques_options+'</div>';
	return ques_box_div;
}

// 绘制题目下选项的集合体
function buildTotalOption(ques_answerList,ques_abel,ques_type,ques_uuid){
	// ques_abel true 可以投 false 不能投(可能投过了)
	if (!isItemList(ques_answerList)) {
		return '';
	}
	var ques_input_type='';
	if(ques_type==1){
		ques_input_type='radio';
	}else{
		ques_input_type='checkbox';
	}
	var option_label = '';
	$.each(ques_answerList, function(index, item) {
		option_label = option_label
				+ buildSingleOption(index, item, ques_abel, ques_input_type,
						ques_uuid);
	});
	var option_div = '<ul class="list">'
		+ option_label + '</ul>';
	return option_div;
}


function buildSingleOption(index,item,ques_abel,ques_input_type,ques_uuid){
	var ans_uuid = item.uuid;
	var ans_option = item.option;
	var ans_voted = item.voted;// true 投过 false 没投
	var ans_votedCount = item.votedCount;
	var ans_precent = item.precent;
	var li='';

	if (ques_abel){
		// 可以投票
		li = '<li class="item"><label>'
				 + '<input type="'+ques_input_type+'" value="' + ans_uuid+'" name="'+ques_uuid+'"';
		if(ques_input_type=='checkbox'){
			li +=' onclick="checkState(this);"'
		}
		li +=  '/>'
			   + ans_option
				 + '</label></li>';
	} else {
		// 不能投票
		li = '<li'
		if(ans_voted){
			// 投过
			li = li+' class="active"';
		}
		li += '>';
		var li_label='<label class="label-checkbox item-content">';

		var inner_title='<div class="item-inner">'
						+'<div class="item-title">'
							+'<div class="answer">'+ans_option+'</div>'
							+'<div class="status" style="width:'+ans_precent+';"></div>'
							+'</div>'
							+'<div class="nums">'+ans_precent+'</div>'
							+'</div></label>';
		li=li+li_label+inner_title+'</li>';
	}

	return li;
}

function postVoteData(uuid){
	var surveyList = vote_content_div.find('.survey-list');
	if (!isItemList(surveyList)) {
		alert('无投票选项');
		return false;
	}
	var posts = [];
	var curr_index = 0;
	var data_flag = true;
	surveyList.each(function(index,item){
		curr_index = index;
		var $p_item = $(item);
		var p_uuid = $p_item.data('uuid');
		var p_type = $p_item.data('type');
		var select_result = buildAnswerOfItem($p_item);
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
	vote_sumit_but.prop('disabled',true);
	$.post('/api/webchat/resource/vote/post/data', {
		'uuid' : resource_uuid,
		'vote_json' : vote_json
	}, function(data) {
		vote_sumit_but.prop('disabled',false);
		if (isSuccess(data)) {
			alert('投票成功');
			buildVoteCtrl(data);
		} else {
			if(data.message=='not login'){
				var url='/api/webchat/resource/first/view/'+uuid;
				loginAlert(url);
			}else{
				alert(data.message);
			}
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
		select_result.push(uuid);
	});
	return select_result;
}

function buildVote(s_uuid, selected_options) {
	var obj = new Object();
	obj.s_uuid = s_uuid;
	obj.selected_options = selected_options;
	return obj;
}

function checkState(input) {
	var $input=$(input);
	//console.log($input.prop("checked"));
	if(!$input.prop("checked")){
		//取消选中，随意选
		return false;
	}
	var surveyLists=$input.parents('.survey-list');
	if(!isItemList(surveyLists)){
		return false;
	}
	var $surveyList=$(surveyLists[0]);
	var p_type = $surveyList.data('type');
	if(p_type==0){
		//无限制可以随便选
		return false;
	}
	var select_items=$surveyList.find('input:checked');
	if(!isItemList(select_items)){
		return false;
	}
	if(select_items.length>p_type){
		$input.prop("checked",false);
	}
	return true;
}
