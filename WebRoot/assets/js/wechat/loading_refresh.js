//var listStartTime, isFirst,dropload;
function getNowFormatDate() {
	//是当前时间比现实时间多1s,便于取到最新数据
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    month = repairZero(month);
    var strDate = date.getDate();
    strDate = repairZero(strDate);
    var hours = date.getHours();
    hours = repairZero(hours);
    var minutes = date.getMinutes();
    minutes = repairZero(minutes);
    var seconds = date.getSeconds()+1;
    seconds = repairZero(seconds);
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + hours + seperator2 + minutes + seperator2 + seconds;
    return currentdate;
}

function repairZero(num) {
    if (num < 10) {
        return num = "0" + num;
    }
    return num;
}

/**
 * buildListCallBack 生成list的callback 每个调用都要实现的方法
 * //每个调用都要实现的方法
function buildListAndStratTime(data){
	var list=data.list;
	if(!!list&&list.length>0){
		listStartTime=list[list.length-1].dateTime;
	}
	return list;
} 
 * buildUlCallBack  生成ul的callback； 每个调用都要实现的方法
 * 
 * 在外部调用刷新的方法
 * dropload.opts.loadUpFn(dropload);
 * 调用加载更多的方法
 * dropload.opts.loadDownFn(dropload);
 */
function dataDropload(div_id,ul_id, request_url, buildListCallBack, buildUlCallBack) {
    var div=$('#'+div_id);
    var ul=$('#' + ul_id);
    var time=getNowFormatDate();
    var dropload=div.dropload({
        scrollArea: window,
        domUp: {
            domClass: 'dropload-up',
            domRefresh: '<div class="dropload-refresh">↓下拉刷新</div>',
            domUpdate: '<div class="dropload-update">↑释放更新</div>',
            domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
        },
        domDown: {
            domClass: 'dropload-down',
            domRefresh: '<div class="dropload-refresh">↑上拉加载更多</div>',
            domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
            domNoData: '<div class="dropload-noData">暂无更多数据</div>'
        },
        loadUpFn: function(me) {
            //向上刷新
            var startTime = getNowFormatDate();
            var dateTimeSmpt=new Date().getTime();
            $.getJSON(request_url, {
                'startTime': startTime,
                'size': 10,
                'action': 'refresh',
                'dateTimeSmpt':dateTimeSmpt
            },
            function(data) {
                if (data.status == 'success') {
                    //回调1
                    var list = buildListCallBack(data);
                    ul.empty();
                    if ( !! list && list.length > 0) {
                    	me.isFirst=false;
                        //组合li 回调2
                        buildUlCallBack(ul,list);
                    }
                    // 每次数据加载完，必须重置
                    me.resetload();
                    // 解锁
                    me.unlock();
                    me.noData(false);
                } else {
                    // 每次数据加载完，必须重置,无更多数据
                    me.resetload();
                }
            });
        },
        loadDownFn: function(me) {
            //往下滑动加载更多
            if (!me.listStartTime) {
                //多数不用
            	me.listStartTime = getNowFormatDate();
            }
            if(me.isFirst==undefined){
            	me.isFirst = true;
            }
            var dateTimeSmpt=new Date().getTime();
            $.getJSON(request_url, {
                'startTime': me.listStartTime,
                'size': 10,
                'action': 'downMore',
                'isFirst':me.isFirst,
                'dateTimeSmpt':dateTimeSmpt
            },
            function(data) {
                if (data.status == 'success') {
                    var list = buildListCallBack(data);
                    if ( !! list && list.length > 0) {
                    	me.isFirst=false;
                        //组合li 回调2
                    	buildUlCallBack(ul,list);
                    } else {
                    	if(!me.isFirst){
                    		//当不是首次加载到数据，隐藏无数据
                    		me.$domDown.addClass('hide');
                    	}
                    	 // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
                    }
                    // 每次数据加载完，必须重置
                    me.resetload();
                } else {
                	if(!me.isFirst){
                		//当不是首次加载到数据，隐藏无数据
                		me.$domDown.addClass('hide');
                	}
                	 // 锁定
                    me.lock();
                    // 无数据
                    me.noData();
                    me.resetload();
                }
            });
        },
        threshold: 40
    });
    return dropload;
}