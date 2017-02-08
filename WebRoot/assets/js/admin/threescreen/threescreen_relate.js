// var pageEditor = UE.getEditor('threescreenRelateDescription');
function buildDataUl(time, page, title, description) {
	var rowUuid = guid();
	var ui=$('<div class="row" >'+
				'<div class="col-sm-3">'+
					'<div class="input-group m-b"><span class="input-group-addon">时间点</span><input type="text" class="form-control" value="'+time+'" readonly="readonly"/></div>'+
				'</div>'+
				'<div class="col-sm-1 txtC">'+
					'<div class="fa fa-exchange"></div>'+
				'</div>'+
				'<div class="col-sm-3">'+
					'<div class="input-group m-b"><span class="input-group-addon">第</span><input type="text" class="form-control nopd txtC" value="'+page+'" readonly="readonly"/> <span class="input-group-addon">页</span></div>'+
				'</div>'+
			'</div>');

    var title = $('<div class="col-sm-3">' +
    				'<div class="input-group m-b"><span class="input-group-addon">标题</span><input type="text" id="threeScreenTitle" class="form-control" value="' + title + '" /></div>' +
    			'</div>');

    var description_hiden = $('<input type="hidden" id="description_' + rowUuid + '" class="form-control" value="' + description + '" />');

    var del_a = $('<a href="javascript:void(0);">删除</a>');
    var del_div = $('<div class="col-sm-1"></div>');
    var description = $('<a href="javascript:void(0);">描述</a>');
    var description_text = $('<div class="col-sm-1"></div>');
    description_text.append(description);
    del_div.append(del_a);
    ui.append(title);
    ui.append(description);
    ui.append(del_div);
    ui.append(description_hiden);

    del_a.on('click', function(event) {
        event.preventDefault();
        if (confirm("确定要删除！")) {
            $(ui).remove();
        }
    });

    description.on('click', function(event) {
        event.preventDefault();
        // pageEditor.setContent($(description.parent().find('input.form-control')[3]).val());
        $('#threescreenRelateDescription').val($(description.parent().find('input.form-control')[3]).val());
        $('#threescreenRelateModal').attr("data-id", rowUuid);
        $('#threescreenRelateModal').modal('show');
    });

    $('#confirm_btn').unbind('click');
    $('#confirm_btn').on('click',function() {
    	// $('#description_' + $('#threescreenRelateModal').attr("data-id")).val(pageEditor.getContent());
      $('#description_' + $('#threescreenRelateModal').attr("data-id")).val($('#threescreenRelateDescription').val());
    	$('#threescreenRelateModal').modal('hide');
    });

    return ui;
}

var objData = function(video, pdf, title, description) {
    var obj = new Object();
    obj.video = video;
    obj.pdf = pdf;
    obj.title = title;
    obj.description = description;
    return obj;
}

var palyer, hour_text, min_text, sends_text, page_text, sanfen_div_contain, videoTime, pdfNum;
function initData(division, fileId, mvideoTime, mpdfNum) {
    var option = {
        "auto_play": "0",
        "file_id": fileId,
        "app_id": "1251442335",
        "width": 570,
        "height": 400
    };
    /* 调用播放器进行播放 */
    palyer = new qcVideo.Player("id_video_container", option,
    function(event) {
        console.log(event);
        //暂停触发
        if (event == 'suspended') {
            setTimeOfText();
        } else if (event == 'playEnd') {
            //结束触发
            setTimeOfText();
        }
    });

    sanfen_div_contain = $('#sanfen_div_contain');
    hour_text = $('#hour_text');
    min_text = $('#min_text');
    sends_text = $('#sends_text');
    page_text = $('#page_text');
    videoTime = mvideoTime;
    pdfNum = mpdfNum;
    //控制text
    controlText();

    //绑定新增事件
    $('#sanfen_button').click(function(event) {
        event.preventDefault();
        addUi();
    });

    $('#sustain_button').click(function(event) {
        event.preventDefault();
        addsustainUi();
    });

    //如果有数据展示
    if ( !! division) {
        try {
            var data_division = JSON.parse(division);
            if ( !! data_division && data_division.length > 0) {
                //排序
                data_division.sort(function(a, b) {
                    var a_video = a.video;
                    var b_video = b.video;
                    if (a_video < b_video) {
                        return - 1;
                    } else if (a_video > b_video) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                sanfen_div_contain.empty();
                $.each(data_division, function(i, item) {
                    var video = item.video;
                    video = converSends(video);
                    var pdf = item.pdf;
                    var title = item.title;
                    if(undefined === title){
                    	title = '';
                    }
                    var description = item.description;
                    if(undefined === description){
                    	description = '';
                    }
                    sanfen_div_contain.append(buildDataUl(video, pdf, title, description));
                });
            }
        } catch(e) {
            console.log(e.message);
        }
    }

    //初始化插件
    pdfPugin();
}

function pdfPugin() {
    var connector = function(itemNavigation, carouselStage) {
        return carouselStage.jcarousel('items').eq(itemNavigation.index());
    };
    // Setup the carousels. Adjust the options for both carousels here.
    var carouselStage = $('.carousel-stage').jcarousel();
    var carouselNavigation = $('.carousel-navigation').jcarousel();

    // We loop through the items of the navigation carousel and set it up
    // as a control for an item from the stage carousel.
    carouselNavigation.jcarousel('items').each(function() {
        var item = $(this);

        // This is where we actually connect to items.
        var target = connector(item, carouselStage);

        item.on('jcarouselcontrol:active',
        function() {
            carouselNavigation.jcarousel('scrollIntoView', this);
            var cur_page_num = carouselStage.jcarousel('target').index() + 1;
            $('#cur_page').val(cur_page_num);
            $('#pdf_pages').find('.cur').html(cur_page_num);
            $('#pdf_pages').find('.all').html(pdfNum);
            item.addClass('active');
            setPageNum(cur_page_num);
        }).on('jcarouselcontrol:inactive',
        function() {
            item.removeClass('active');
        }).jcarouselControl({
            target: target,
            carousel: carouselStage
        });
    });

    // Setup controls for the stage carousel
    $('.prev-stage').on('jcarouselcontrol:inactive',
    function() {
        $(this).addClass('inactive');
    }).on('jcarouselcontrol:active',
    function() {
        $(this).removeClass('inactive');
    }).jcarouselControl({
        target: '-=1'
    });

    $('.next-stage').on('jcarouselcontrol:inactive',
    function() {
        $(this).addClass('inactive');
    }).on('jcarouselcontrol:active',
    function() {
        $(this).removeClass('inactive');
    }).jcarouselControl({
        target: '+=1'
    });

    // Setup controls for the navigation carousel
    $('.prev-navigation').on('jcarouselcontrol:inactive',
    function() {
        $(this).addClass('inactive');
    }).on('jcarouselcontrol:active',
    function() {
        $(this).removeClass('inactive');
    }).jcarouselControl({
        target: '-=1'
    });

    $('.next-navigation').on('jcarouselcontrol:inactive',
    function() {
        $(this).addClass('inactive');
    }).on('jcarouselcontrol:active',
    function() {
        $(this).removeClass('inactive');
    }).jcarouselControl({
        target: '+=1'
    });

}

//初始化控制文本的最小数值，根据视频时间判断
function controlText() {
    if (videoTime < 60) {
        hour_text.val('00');
        hour_text.attr('readonly', 'readonly');
        min_text.val('00');
        min_text.attr('readonly', 'readonly');
    } else if (videoTime < 3600) {
        hour_text.val('00');
        hour_text.attr('readonly', 'readonly');
    }
}

//新增对应关系
function addUi() {
    if (beforeAddData()) {
        //添加一条新数据
        sanfen_div_contain.prepend(buildDataUl(timeStr(), page_text.val(), '', ''));
        //添加完了，之后，要清空数据
        if (hour_text.attr('readonly') == undefined) {
            hour_text.val('');
        }
        if (min_text.attr('readonly') == undefined) {
            min_text.val('');
        }
        sends_text.val('');
        page_text.val('');
    }
}

function addsustainUi() {
    var sustain_time_input = $('#sustain_time_input');
    var sustain_page_input = $('#sustain_page_input');
    var sustain_time_text = sustain_time_input.val();
    sustain_time_text = parseInt(sustain_time_text);
    var sustain_page_text = sustain_page_input.val();
    sustain_page_text = parseInt(sustain_page_text);
    var flag = isNaN(sustain_time_text);
    if (flag || sustain_time_text < 0) {
        alert('持续时间填写大于0正整数');
        return false;
    }

    flag = isNaN(sustain_page_text);
    if (flag || sustain_page_text < 0) {
        alert('页数填写整数');
        return false;
    }

    if (sustain_page_text > pdfNum) {
        alert("页数超过视频总页数");
        return false;
    }

    var time = findBeforeTime(sustain_page_text, sustain_time_text);
    if (time > videoTime) {
        alert("总时长超过视频时长");
        return false;
    }

    if (judyAddTrue(time, sustain_page_text)) {
        //添加一条新数据
        sanfen_div_contain.prepend(buildDataUl(converSends(time), sustain_page_text, '', ''));
        sustain_time_input.val('');
        sustain_page_input.val('');
    }

}

function findBeforeTime(pdf, time) {
    var objList = judyAddBefor();
    var value;
    if (objList.length == 0) {
        value = time;
    } else {
        var temp = 0;
        $.each(objList,
        function(i, t) {
            if (t.pdf > pdf) {
                return false;
            }
            temp = t.video;
        });
        value = time + temp;
    }
    return value;
}

//新增关系之前，校验数据
function beforeAddData() {
    var hour_text_val = hour_text.val();
    hour_text_val = parseInt(hour_text_val);
    var min_text_val = min_text.val();
    min_text_val = parseInt(min_text_val);
    var sends_text_val = sends_text.val();
    sends_text_val = parseInt(sends_text_val);
    var page_text_val = page_text.val();
    page_text_val = parseInt(page_text_val);
    var flag = isNaN(hour_text_val);
    if (flag || hour_text_val < 0) {
        alert("小时请填写正整数");
        return false;
    }
    flag = isNaN(min_text_val);
    if (flag || min_text_val < 0) {
        alert("分钟请填写正整数");
        return false;
    }
    flag = isNaN(sends_text_val);
    if (flag || sends_text_val < 0) {
        alert("秒请填写正整数");
        return false;
    }
    flag = isNaN(page_text_val);
    if (flag || page_text_val < 0) {
        alert("页数请填写正整数");
        return false;
    }
    var time = judyTime(hour_text_val, min_text_val, sends_text_val);
    if (time > videoTime) {
        alert("总时长超过视频时长");
        return false;
    }
    if (page_text_val > pdfNum) {
        alert("页数超过视频总页数");
        return false;
    }

    //检查页数是否存在
    //时间是否在区间内
    return judyAddTrue(time, page_text_val);
}

//添加之前计算时长
function judyTime(hour, min, sends) {
    return hour * 3600 + min * 60 + sends;
}

//添加到下一行返回的时间字符串
function timeStr() {
    var hour_text_val = hour_text.val();
    hour_text_val = parseInt(hour_text_val);
    var min_text_val = min_text.val();
    min_text_val = parseInt(min_text_val);
    var sends_text_val = sends_text.val();
    sends_text_val = parseInt(sends_text_val);
    if (hour_text_val < 10) {
        hour_text_val = '0' + hour_text_val;
    }
    if (min_text_val < 10) {
        min_text_val = '0' + min_text_val;
    }
    if (sends_text_val < 10) {
        sends_text_val = '0' + sends_text_val;
    }
    return hour_text_val + ':' + min_text_val + ':' + sends_text_val;
}

//视频暂停时，给文本赋值
function setTimeOfText() {
    var time = palyer.getCurrentTime();
    if (hour_text.attr('readonly') == undefined) {
        hour_text.val(converTime(time, 1));
    }
    if (min_text.attr('readonly') == undefined) {
        min_text.val(converTime(time, 2));
    }
    sends_text.val(converTime(time, 3));
}

function setPageNum(page) {
    page_text.val(page);
    $('#sustain_page_input').val(page);
}

//秒钟化为时分秒
function converSends(sens) {
    sens = parseInt(sens);
    var hour = Math.floor(sens / 3600);
    var other = sens % 3600;
    var min = Math.floor(sens / 60);
    var msend = other % 60;
    if (hour < 10) {
        hour = '0' + hour;
    }
    if (min < 10) {
        min = '0' + min;
    }
    if (msend < 10) {
        msend = '0' + msend;
    }
    return hour + ':' + min + ':' + msend;
}

//时分秒转化成秒
function converTimeSends(time) {
    var data = time.split(':');
    var hour = parseInt(data[0]);
    var min = parseInt(data[1]);
    var msend = parseInt(data[2]);
    return judyTime(hour, min, msend);
}

//type 1=x小时 2=分钟 3=秒
function converTime(sens, type) {
    var val = '00';
    sens = parseInt(sens);
    if (type == 1) {
        val = Math.floor(sens / 3600);
    } else if (type == 2) {
        var other = sens % 3600;
        val = Math.floor(sens / 60);
    } else if (type == 3) {
        var other = sens % 3600;
        val = other % 60;
    }
    val = parseInt(val);
    if (val < 10) {
        val = '0' + val;
    }
    return val;
}

//检测是否可以插入数据
function judyRelation(time, pdfnum) {
    var rowList = sanfen_div_contain.children('div.row');
    if (rowList.length == 0) {
        return true;
    }
    var objList = getPushData(rowList);
    var flag = true;
    $.each(objList,
    function(index, item) {
        var video = item.video;
        var pdf = item.pdf;
        if (time < video) {
            if (pdfnum >= pdf) {
                flag = false;
                alert('pdf页数不应大于最小时长选中页数');
                return flag; //break
            }
            return true; //continue
        } else if (time > video) {
            if (pdfnum <= pdf) {
                flag = false;
                alert('pdf页数不应小于最大时长选中页数');
                return flag; //break
            }
        } else {
            flag = false;
            alert('选中时长已存在');
            return flag; //break
        }
    });
    return flag;
}

function getPushData(rowList) {
    //遍历row
    var objList = [];
    rowList.each(function(index, item) {
        var input_list = $(item).find('input.form-control');
        var video = $(input_list[0]).val();
        video = converTimeSends(video);
        var pdf = $(input_list[1]).val();
        pdf = parseInt(pdf);

        var title = $(input_list[2]).val();
        var description = $(input_list[3]).val();
        objList.push(objData(video, pdf, title, description));
    });
    return objList;
}

function judySumitBefor() {
    var rowList = sanfen_div_contain.children('div.row');
    if (rowList.length == 0) {
        alert('请先添加对应关系');
        return '';
    }
    var objList = getPushData(rowList);
    //按时长升序
    objList.sort(function(a, b) {
        var a_video = a.video;
        var b_video = b.video;
        if (a_video < b_video) {
            return - 1;
        } else if (a_video > b_video) {
            return 1;
        } else {
            return 0;
        }
    });
    return objList;
}

function judyAddBefor() {
    var rowList = sanfen_div_contain.children('div.row');
    if (rowList.length == 0) {
        return [];
    }
    var objList = getPushData(rowList);
    //按页数升序
    objList.sort(function(a, b) {
        var a_pdf = a.pdf;
        var b_pdf = b.pdf;
        if (a_pdf < b_pdf) {
            return - 1;
        } else if (a_pdf > b_pdf) {
            return 1;
        } else {
            return 0;
        }
    });
    return objList;
}

//判断添加前，pdf及页数对应是否正确
function judyAddTrue(video, pdf) {
    console.log(pdf);
    var objList = judyAddBefor();
    if (objList.length == 0) {
        return true;
    }
    var flag = true;
    var tips = '';
    for (var i = 0; i < objList.length; i++) {
        var item = objList[i];
        var item_pdf = item.pdf;
        var item_video = item.video;
        if (item_pdf < pdf) {
            if (item_video > video) {
                tips = '当前播放时长小于之前页数的时长';
                flag = true;
                break;
            }
        } else if (pdf == item.pdf) {
            tips = '当前页数已存在';
            flag = false;
            break;
        } else {
            if (item_video < video) {
                tips = '当前播放时长大于之后页数的时长';
                flag = true;
                break;
            }
        }
    }
    if ( !! tips) {
        alert(tips);
    }
    return flag;
}

//生成uuid
function guid() {
    function S4() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}
