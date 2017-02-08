var editor;
$(document).ready(
		function() {
			showActive([ 'games-list-nav', 'games-mgr' ]);

			// 初始化ueditor编辑器
			editor = UE.getEditor('gameRules');

			// 回显编辑器里的内容
			editor.addListener("ready", function() {
				editor.setContent($('#rules_hiden').text());
			});

			var templateId = $('#templateId').val();

			// 初始化时间插件
			$('#beginDate').datetimepicker({
				format : 'YYYY-MM-DD HH:mm',
				ignoreReadonly : true
			});
			$('#endDate').datetimepicker({
				format : 'YYYY-MM-DD HH:mm',
				useCurrent : false,
				ignoreReadonly : true
			});
			$("#beginDate").on("dp.change", function(e) {
				$('#endDate').data("DateTimePicker").minDate(e.date);
			});
			$("#endDate").on("dp.change", function(e) {
				$('#beginDate').data("DateTimePicker").maxDate(e.date);
			});

			// 模版类型回显
			if (!!templateId) {
				$('#gameTemplateId option[value=' + templateId + ']').attr(
						"selected", "selected");
			}

			// 表单验证
			var validator = $('#games_form').validate({
				ignore : ".ignore",
				errorElement : 'span',
				errorClass : 'help-block',
				focusInvalid : false,
				rules : {
					joinTimes : {
						digits : true
					}
				},
				messages : {
					name : "请输入合理的游戏名称",
					joinTimes : "请设置用户可参与该游戏的次数(整数)",
					beginDate : "请输入游戏开始时间",
					endDate : "请输入游戏结束时间",
					page : "请添加页面内容"
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
				},
				submitHandler : function(form) {
					submitFrom(form);
				}
			});
		});

// 奖品参数是否合法标记
var flag = false;

// 保存数据
function submitFrom(from) {
	var awards = [];
	var game_name = $('#gameName').val();
	var game_joinTimes = $('#gameJoinTimes').val();
	var game_beginDate = $('#beginDate').val();
	var game_endDate = $('#endDate').val();
	var game_mark = $('#gameMark').val();
	var game_uuid = $('#uuid').val();
	var game_id = $('#game_id').val();
	var game_isActive = $('#isActive').val();
	var game_status = $('#status').val();
	var game_templateId = $('#gameTemplateId').val();
	var game_rules = editor.getContent();
	// game_rules = game_rules.replace("\n", "<br/>" );
	// console.log(game_rules);

	$('#games_container').find('.list-group-item').each(function(i, e) {
		var $e = $(e);
		var award_name = $e.find('input[name=awardName]').val();
		var award_number = $e.find('input[name=awardNumber]').val();
		var award_maxWinTimes = $e.find('input[name=awardMaxWinTimes]').val();
		var award_conditional = $e.find('input[name=awardConditional]').val();
		var award_uuid = $e.data("id");
		awards.push({
			"name" : award_name,
			"number" : award_number,
			"maxWinTimes" : award_maxWinTimes,
			"conditional" : award_conditional,
			"uuid" : award_uuid
		})
	})

	var data = {
		"templateId" : game_templateId,
		"rules" : game_rules,
		"name" : game_name,
		"joinTimes" : game_joinTimes,
		"beginDate" : game_beginDate,
		"endDate" : game_endDate,
		"mark" : game_mark,
		"uuid" : game_uuid,
		"id" : game_id,
		"status" : game_status,
		"isActive" : game_isActive,
		"awards" : awards
	}

	var test = JSON.stringify(data);
	console.log(test);

	if (awards.length == 0) {
		alert('请添加游戏奖品!');
		return;
	}

	if (game_templateId == '0') {
		alert('请选择游戏模版!');
		return;
	}

	// 奖品输入信息校验
	for (var index = 0; index < awards.length; index++) {
		if (!awards[index]['name'].trim() || !awards[index]['number'].trim()
				|| !awards[index]['maxWinTimes'].trim()
				|| !awards[index]['conditional'].trim()) {
			flag = true;
		}
		if (!(/^\d+$/.test(awards[index]['number'].trim()))
				|| !(/^\d+$/.test(awards[index]['maxWinTimes'].trim()))
				|| !(/(^\d+$)|(^\d+\.\d+$)/.test(awards[index]['conditional']
						.trim()))) {
			flag = true;
		}
	}

	if (flag) {
		alert('请输入正确的奖品参数');
		flag = false;
		return;
	}

	$.post('/project/games/data/saveOrUpdate', {
		gameData : JSON.stringify(data),
		token : $('#submitToken').val()
	}, function() {
	}).always(function(data_reslut) {
		if (data_reslut.status == 'success') {
			// refresh_subject(item, data)
			alert('操作成功');
			window.location.href = '/project/games/list/page';
		} else {
			if (data_reslut.message) {
				alert(data_reslut.message);
			} else {
				alert(data_reslut.status);
			}
		}
	})
}