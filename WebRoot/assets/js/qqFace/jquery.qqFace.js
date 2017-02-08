function emotion(that, domobj,e) {
	var defaults = {
		id : 'facebox',
		path : '/assets/img/qqFace/',
		assign : 'content',
		tip : 'em_',
	};
	var num = new Array(75);
	if ($("#facebox").length>0) {
		return false;
	}
	// var option = $.extend(defaults, options);
	var assign = domobj;
	var id = defaults.id;
	var path = defaults.path;
	var tip = defaults.tip;
	if (assign.length <= 0) {
		alert('缺少表情赋值对象。');
		return false;
	}

	var insertAtCaret = function(textFeildValue) {
		var textObj = $(domobj).get(0);
		if (document.all && textObj.createTextRange && textObj.caretPos) {
			var caretPos = textObj.caretPos;
			caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == '' ? textFeildValue
					+ ''
					: textFeildValue;
		} else if (textObj.setSelectionRange) {
			var rangeStart = textObj.selectionStart;
			var rangeEnd = textObj.selectionEnd;
			var tempStr1 = textObj.value.substring(0, rangeStart);
			var tempStr2 = textObj.value.substring(rangeEnd);
			var sxh=tempStr1 + textFeildValue + tempStr2;
			textObj.value = tempStr1 + textFeildValue + tempStr2;
			textObj.focus();
			var len = textFeildValue.length;
			textObj.setSelectionRange(rangeStart + len, rangeStart + len);
			textObj.blur();
		} else {
			textObj.value += textFeildValue;
		}
	};

	//$this.click(function(e) {
		var strFace;
		var qq_div = $('<div id="'
				+ id
				+ '" class="qq-face"></div>');
		var qq_table = $('<table border="0" cellspacing="0" cellpadding="0"></table>');
		var tr_1 = $('<tr></tr>'), tr_2 = $('<tr></tr>'), tr_3 = $('<tr></tr>'), tr_4 = $('<tr></tr>'), tr_5 = $('<tr></tr>');
		$.each(num, function(index, item) {
			var index_num = index + 1;
			var labFace = '[' + tip + index_num + ']';
			var img_i = $('<td><img src="' + path + index_num
					+ '.gif"/></td>');
			img_i.click(function() {
				insertAtCaret(labFace);
			});
			if (index < 15)
				tr_1.append(img_i);
			else if (15 <= index && index < 30)
				tr_2.append(img_i);
			else if (30 <= index && index < 45)
				tr_3.append(img_i);
			else if (45 <= index && index < 60)
				tr_4.append(img_i);
			else
				tr_5.append(img_i);
		});
		qq_table.append(tr_1);
		qq_table.append(tr_2);
		qq_table.append(tr_3);
		qq_table.append(tr_4);
		qq_table.append(tr_5);
		qq_div.append(qq_table);
		$(that).parent().append(qq_div);
		var offset =$(that).position();
		var top = offset.top +$(that).outerHeight();
		$('#' + id).css('top', top + 6);
		$('#' + id).css('left', offset.left);
		$('#' + id).show();
		e.stopPropagation();
	//});

		$(document).click(function() {
			$('#' + id).hide();
			$('#' + id).remove();
		});
};