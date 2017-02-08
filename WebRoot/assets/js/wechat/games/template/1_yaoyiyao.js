$(document).ready(function() {

    var lotteryUuid = $('#uuid').val();
    var lotteryRules = $('#rules').val();
    
    //初始化游戏规则
    $('#info').append(lotteryRules);
    
    //初始化游戏
    /*$.getJSON('/api/webchat/game/init', {
    	uuid : lotteryUuid
    }, function(data){
    	//test
    	console.log(data);
    	if('success' != data['status']){
    		if(!!data['message']){
    			alert(data['message']);
    		}else{
    			alert(data['status']);
    		}
    	}
    })*/

    //shake 配置
    var myShakeEvent = new Shake({
        threshold: 10, // optional shake strength threshold
        timeout: 1000 // optional, determines the frequency of event generation
    });
    var bgMedia = $('#bg_music');
    var bgMusic = bgMedia.attr('src');
    var firstTime = true;
    myShakeEvent.start();
    window.addEventListener('shake', shakeEventDidOccur, false);
    //window.addEventListener('click', shakeEventDidOccur, false);
    
    function shakeEventDidOccur() {
        if (!firstTime) {
            $('#envelopes_index').fadeIn();
            $('#envelopes_result').fadeOut();
        }

        //摇完之后出现的效果
        if (!bgMusic) {
            bgMedia.attr('src', '/assets/wechat.mp3');
        }

        setTimeout(function() {
            bgMedia.play();
        }, 1000);

        //开始游戏，请求数据
        $.getJSON('/api/webchat/game/play?timestamp=' + new Date().getTime(),{
        	uuid: lotteryUuid
        }, function(data) {
            console.log(data);
            var envelopes_tips = $('#envelopes_tips');
            var envelopes_intro = $('#envelopes_intro');
            var code = data['times'];
            var winning = $('#winning');
            var winning_no = $('#winning_no');
            if (code >= 0) {
                var award = data['award'];
                if (!! award) {
                    result = '恭喜您，抽中了' + data['award']['name'] + '，还有' + data.times + '次机会！';
                    winning.show();
                    winning_no.hide();
                } else {
                    result = '很遗憾，未抽中，还有' + data.times + '次机会！';
                    winning.hide();
                    winning_no.show();
                }
                envelopes_tips.html(result);
            } else if (code == -1) {
                envelopes_tips.html('您已经没有抽奖机会了');
                winning.hide();
                winning_no.show();
            } else if (code == -2) {
                envelopes_tips.html('活动还未开始');
                winning.hide();
                winning_no.show();
            } else if (code == -3) {
                envelopes_tips.html('活动已经结束');
                winning.hide();
                winning_no.show();
            } else if (code == -4) {
                envelopes_tips.html('活动无效');
                winning.hide();
                winning_no.show();
            } else if (code == -5) {
                envelopes_tips.html('活动参数错误');
                winning.hide();
                winning_no.show();
            } else {
            	envelopes_tips.html('您已经没有抽奖机会了');
                winning.hide();
                winning_no.show();
            }

            //随机显示内容
            var float_intro_div = $('#float_intro_div');
            var content = ['知了盒子带回家，让它成为您的客厅教授', '知了云盒已经有100多个外科视频啦', '2016播客秀已经整合升级到知了云盒啦！', '知了云盒是一个可以随时随地进行收看的医学可视化学习分享平台', '您可以通过手机微信公众号或通过知了盒子在电视上收看知了云盒学术内容哦！'];
            var randomNum = Math.round(Math.random() * 5) % 5;
            float_intro_div.html(content[randomNum]);
            envelopes_intro.show();

            $('#envelopes_index').find('.title').removeClass('active');
            $('#envelopes_index').find('.gift_box').addClass('active');

            setTimeout(function() {
                $('#envelopes_index').fadeOut();
                $('#envelopes_result').fadeIn();
            }, 1000);
            firstTime = false;
        });
    }

    //展示活动规则
    $('#btn_rule').click(function() {
        $('.float_info').fadeOut();
        $('#info_rule').fadeIn();
    });

    //查看抽奖记录
    $('#btn_record').click(function() {
        var record_list_ul = $('#record_list_ul');
        $.getJSON('/api/webchat/game/record?timestamp=' + new Date().getTime(), {
        	uuid: lotteryUuid
        }, function(data) {
        	console.log(data);
            if (data.status == 'success') {
                record_list_ul.empty();
                var record_list = data.items;
                $.each(record_list, function(index, item) {
                	var isWin = '未抽中';
                	var ticket = '';
                    if("YES" == item.isWinner){
                    	isWin = item.awardName;
                    	if(0 == item['status']){
                    		ticket = item['exchangeCode'];
                    	}else{
                    		ticket = '已兑换';
                    	}
            		}
                    var date = item['createDate'];
                    //var li = '<li><span class="date">' + date + '</span>' + isWin + '</li>';
                    var li = '<li><span>' + date + '</span>&nbsp;<span>' + isWin + '</span>&nbsp;<span>' + ticket + '</span></li>';
                    record_list_ul.append(li);
                });
            }
            $('.float_info').fadeOut();
            $('#info_record').fadeIn();
        });
    });
    
    $('.float_info').find('.close').click(function() {
        $(this).parents('.float_info').fadeOut();
    });
});

function timeStamp2String(time) {
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
    var hour = datetime.getHours();
    var minute = datetime.getMinutes();
    var second = datetime.getSeconds();
    return repairZero(year) + "-" + repairZero(month) + "-" + repairZero(date) + " " + repairZero(hour) + ":" + repairZero(minute) + ":" + repairZero(second);
}

function repairZero(num) {
    if (num < 10) {
        return num = "0" + num;
    }
    return num;
}