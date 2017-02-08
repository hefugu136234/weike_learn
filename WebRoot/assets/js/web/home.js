$(document).ready(function () {
  var nextStep = $('#nextBtn');
  var submitStep = $('#submitNormal');
  var submitStep_b = $('#submitBrand');
  var finishStep = $('#finishBtn');

  var validCodeBtn = $('#codeBtn');
  var validCodeInput = $('#validCode');

  var nameInput = $('#userName');
  var mobileInput = $('#userMobile');
  var passwordInput = $('#userPassword');
  var provinceSelect = $('#userProvince');
  var citySelect = $('#userCity');
  var hospitalSelect = $('#userHospital');
  var departmentSelect = $('#userDepartment');
  var jobTitleSelect = $('#userJobTitle');

  var userName_b = $('#userName_b').val();
  var userTel = $('#userTel').val();
  var companyName = $('#companyName').val();
  var brandName = $('#brandName').val();
  var position = $('#position').val();
  var companyEmail = $('#companyEmail').val();
  var companyAddress = $('#companyAddress').val();

  var countWait = 60;

  //加载省的数据
  $.getJSON('/f/list/province',function(data){
	  if(data.status=='success'){
		  buildOption(data,$('#province'),'省份数据加载出错');
	  }else{
		  if(!!data.message){
				alert(data.message);
			}else{
				alert("省份数据加载出错");
			}
	  }
  });
  //加载科室的数据
  $.getJSON('/f/list/department',function(data){
	  if(data.status=='success'){
		  buildOption(data,$('#userDepartment'),'科室数据加载出错');
	  }else{
		  if(!!data.message){
				alert(data.message);
			}else{
				alert("科室数据加载出错");
			}
	  }
  });

  validCodeBtn.unbind('click');
  validCodeBtn.bind('click', function (event) {
    event.preventDefault();
    var objThis = $(this);

    if(!checkMobile(mobileInput)){
    	return false;
    }

    objThis.attr('disabled', true);  // 点击后，获取验证码按钮失效，开机计时。

    $.post('/f/subscribe/code/send',{mobile:mobileInput.val()},function(data){
    	if(data.status=='success'){
    		alert('验证码发送成功');
    		countTime(objThis);
    	}else{
    		 objThis.attr('disabled', false);
    		if(!!data.message){
				alert(data.message);
			}else{
				alert("发送失败");
			}
    	}
    });
  });

  // 控制隐藏和显示
  nextStep.on('click', function (event) {
    event.preventDefault();
    if(!checkMobile(mobileInput)){
    	return false;
    }
    if(!validCodeInput.val()){
    	alert('请填写验证码');
    	return false;
    }
    $.post('/f/subscribe/code/valid',
    		{mobile:mobileInput.val(),code:validCodeInput.val()},
    	function(data){
    			if(data.status=='success'){
    				 $('#step1').hide();
    		         $('#step2').show();
    		         console.log(data.valid_code);
    		         $('#valid_code').val(data.valid_code);
    			}else{
    				if(!!data.message){
    					alert(data.message);
    				}else{
    					alert("验证失败");
    				}
    			}
    		});

//  finishStep.click(function () {
//    $('#floatForm').hide();
//    location.href = "/f/index";
//  });

  // 提交表单信息至后台
  submitStep.bind('click', function (event) {
    event.preventDefault();
    var postData = {
      name: nameInput.val(),
      mobile: mobileInput.val(),
      password: passwordInput.val(),
      province: provinceSelect.val(),
      city: citySelect.val(),
      hospital: hospitalSelect.val(),
      department: departmentSelect.val(),
      job_title: jobTitleSelect.val()
    };
    $.ajax({
      url: '',
      method: 'POST',
      data: postData,
      dataType: 'json',
      success: function (data) {
        if (data.status == 'success') {
          $('#step2').hide();
          $('#step3').show();
        }
      }
    });
  });

  // 提交品牌客户表单信息至后台
  submitStep_b.bind('click', function (event) {
    event.preventDefault();
    var postData = {
      name: userName_b,
      tel: userTel,
      companyName: companyName,
      brandName: brandName,
      position: position,
      companyEmail: companyEmail,
      companyAddress: companyAddress
    };
    $.ajax({
      url: '',
      method: 'POST',
      data: postData,
      dataType: 'json',
      success: function (data) {
        if (data.status == 'success') {
          $('#step2').hide();
          $('#step3').show();
        }
      }
    });
  });


});

});

function checkMobile(mobileInput){
	var flag=true;
	var mobile=mobileInput.val();
	var regPhone = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/; //验证手机号码
    if (mobile == '') {
      alert('请填写您的手机号码！');
      flag= false;
    }
    else if (mobile != '' && !regPhone.test(mobile)) {
      alert('请填写正确的手机号码！');
      mobileInput.val('');
      mobileInput.focus();
      flag= false;
    }
    return flag;
}
var countWait = 60;
//计时
function countTime(obj) {
  if (countWait == 0) {
    clearTimeout(timer);
    $(obj).attr("disabled", false);
    $(obj).removeClass("disabled");
    $(obj).html("获取验证码");
    countWait = 60;
  } else {
    $(obj).attr("disabled", true);
    $(obj).addClass("disabled");
    $(obj).html(countWait + "秒后重新获取");
    countWait--;
    var timer = setTimeout(function () {
      countTime(obj);
    }, 1000)
  }
}

function buildOption(vo,elem,msg){
		var itemList=vo.itemList;
		if(!!itemList){
			$.each(itemList, function(index, item) {
				var option = '<option value='+item.uuid+'>' + item.name
						+ '</option>';
				elem.append(option);
			});
		}else{
			alert(msg);
		}
}

