$(function() {
	// 点击学习
	$('#course_study').click(function(e) {
		e.preventDefault();
		var uuid = $('#course_uuid').val();
		var $this = $(this);
		$.post('/f/web/course/study/action', {
			uuid : uuid
		}, function(data) {
			if (isSuccess(data)) {
				$this.hide();
				var score = data.message;
				$('#user_package_status').find('span.num').html(score);
				$('#user_package_status').removeClass('hide');
			} else {
				if (data.message == 'not login') {
					alert("请登录后学习课程");
				} else {
					alert(data.message);
				}
			}
		});
	});

	var course_studyStatus=parseInt($('#course_studyStatus').val());
	var course_uuid = $('#course_uuid').val();
	var $package_cover=$('#package_cover');
	var $package_coverLabel=$('#package_coverLabel');
	var $package_coverDes=$('#package_coverDes');
	var $package_coverTime=$('#package_coverTime');
	var $package_coverNum=$('#package_coverNum');
	var $package_coverScore=$('#package_coverScore');
	var $examine_start=$('#examine_start');
	
	
	$examine_start.click(function(e){
		var uuid=$package_cover.data('uuid');
		var course_url = '/f/web/course/package/detail/' + course_uuid;
		$.getJSON('/f/web/normalCollect/questionnaire',{
			  uuid:uuid,
			  redirect_uri:course_url
		  },function(data){
			  if(isSuccess(data)){
				  $package_cover.modal('hide');
				  location.href=data.message;
			  }else{
				  if(data.message=='not login'){
					  alert("请先登录，再参与考试");
				  }else{
					  alert(data.message);
				  }
			  }
		  });
	});
	
	var buildModal=function(data){
		$package_cover.data('uuid',data.uuid);
		$package_coverLabel.html(data.name);
		$package_coverDes.html(data.desc);
		$package_coverTime.html(data.examineTime+'分钟');
		$package_coverNum.html(data.examineNum+'题');
		$package_coverScore.html('≥'+data.qualifiedScore+'');
		$package_cover.modal('show');
	};

	var examineUrlFun=function(uuid,course_url){
		$.getJSON('/f/web/course/chapter/examine/detail/'+uuid,
				function(data){
			if(isSuccess(data)){
				buildModal(data);
			}else{
				alert(data.message);
			}
		});
		
	};

	var itemExamineFun=function(logined,disableClick,uuid,course_url){
		if(logined==0){
			  alert("请先登录，再参与考试");
		  }else{
			  if(course_studyStatus==0){
				  alert("请先点击学习本课程");
					return ;
			  }
			  if(disableClick==0){
				  alert("请先通过前一章节的考试");
			  }else{
				  examineUrlFun(uuid,course_url);
			  }
		  }
	};

	var itemResourceFun=function(disableClick,url){
		 if(disableClick==0){
			  //不能点击
			  alert("请先通过前一章节的考试");
		  }else if(disableClick==1){
			  location.href=url;
		  }
	};

	var itemClickFun = function(item, logined, disableClick, course_url) {
		var $item = $(item);
		var type = $item.data('type');
		var uuid = $item.data('uuid');
		$item.find('a').click(function(e){
			e.preventDefault();
			var url='/f/web/course/learn/'+uuid;
			 if(type=='resource'){
				 itemResourceFun(disableClick,url);
			  }else if(type=='examine'){
				  itemExamineFun(logined,disableClick,uuid,course_url);
			  }
		});
	};

	var eachItemsFun = function(row_item, course_url) {
		var $row_item = $(row_item);
		var logined = $row_item.data("logined");
		var disableClick = $row_item.data("disableclick");
		var item_data = $row_item.find('div.item.data');
		if (isItemList(item_data)) {
			item_data.each(function(index, item) {
				itemClickFun(item, logined, disableClick, course_url,
						course_url);
			});
		}
	};

	var category_list = $('div.category-list.col-2');
	if (isItemList(category_list)) {
		var course_url = '/f/web/course/package/detail/' + course_uuid;
		category_list.each(function(index, item) {
			eachItemsFun(item, course_url);
		});
	}

});
