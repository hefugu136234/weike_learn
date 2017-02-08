function clearContent(parent) {
	parent.empty();
	parent.append('<option value="">请选择</option>');
}

function changeProvince(uuid) {
	clearContent($('#city'));
	clearContent($('#hospital'));
	if (!!uuid) {
		$.getJSON('/f/city/' + uuid, function(data) {
			var city = $('#city');
			console.log(data)
			if (data.itemList) {
				$.each(data.itemList, function(index, item) {
					var option = '<option value=' + item.uuid + '>' + item.name
							+ '</option>';
					city.append(option);
				});
			} else {
				alert("城市数据加载错误");
			}
		});
	}
}

function changeCity(uuid) {
	clearContent($('#hospital'));
	if (!!uuid) {
		$.getJSON('/f/hospital/' + uuid, function(data) {
			var hosipital = $('#hospital');
			console.log(data);
			if (data.itemList) {
				$.each(data.itemList, function(index, item) {
					console.log(item);
					var option = '<option value=' + item.uuid + '>' + item.name
							+ '</option>';
					hosipital.append(option);
				});
			}
		});
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
