<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="template language" name="keywords" />
<meta content="author" name="author" />
<meta
  content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"
  name="viewport" />
<meta content="yes" name="mobile-web-app-capable" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="telephone=no" name="format-detection" />
<meta content="email=no" name="format-detection" />
<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
<title>${requestScope.name}</title>
<link href="/assets/css/app/font.css" rel="stylesheet">
<link href="/assets/css/app/default.css" rel="stylesheet">
<link href="/assets/css/app/shop.css" rel="stylesheet">
<script type="text/javascript" src="/assets/js/jquery.js"></script>
<script type="text/javascript" src="/assets/js/wechat/common_region.js"></script>
<script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
</head>

<body>
  <div class="page">
    <div class="content">
      <header class="top-bar">
        <div class="crumb-nav">
          <a href="/api/webchat/my/center" class="logo icon-logo"></a>
          <a href="/api/webchat/wx/jifen/shop">积分商场</a>
          ${requestScope.name}
        </div>
      </header>

      <div class="product-img-group">
        <div class="img"><img src="${requestScope.cover}" alt=""></div>
      </div>

      <div class="product-name">
        <h5 class="name">${requestScope.name}</h5>
      </div>

      <div class="icon-title bold white mt10 mb-1 clearfix">
        <h5 class="tt bfL">礼品介绍</h5>
      </div>

      <div class="product-intro">${requestScope.description}</div>
    </div>

    <div class="foot-statistics-bar">
      <div class="price">所需积分：<span class="num">${requestScope.integral}</span></div>
      <div class="btns">
        <a href="javascript:;" class="btn btn-cyan" id="dealclick">兑 换</a>
      </div>
    </div>

    <input id="shop_uuid" type="hidden" value="${requestScope.uuid}"/>
  </div>

  <div class="page hidden-full-page" id="add_address">
    <div class="content">
      <header class="top-bar">
        <div class="crumb-nav">
          <a href="javascript:;" class="logo icon-logo" id="close_address"></a>
          新增收货地址
        </div>
      </header>

      <div class="default-form">
        <label class="form-group">
          <div class="form-label">收件人姓名：</div>
          <div class="form-input">
            <input type="text" name="name" id="name" class="form-control" placeholder="收件人姓名不得为空">
          </div>
        </label>

        <label class="form-group">
          <div class="form-label">手机号码：</div>
          <div class="form-input">
            <input type="tel" name="phone" id="phone" class="form-control" placeholder="请填写您的手机号码" value="">
          </div>
        </label>
      </div>

      <div class="default-form mt10">
        <label class="form-group form-select">
          <div class="form-label">所在省份：</div>
          <div class="form-input">
            <select name="province" id="province" class="form-control" onchange="changeProvince(this.value);">
              <option value="">请选择</option>
            </select>
          </div>
        </label>
        <label class="form-group form-select">
          <div class="form-label">所在城市：</div>
          <div class="form-input">
            <select name="city" id="city" class="form-control" onchange="changeCity(this.value);">
              <option value="">请选择</option>
            </select>
          </div>
        </label>
        <label class="form-group form-select">
          <div class="form-label">所在区域：</div>
          <div class="form-input">
            <select name="district" id="district" class="form-control">
              <option value="">请选择</option>
            </select>
          </div>
        </label>
        <label class="form-group">
          <div class="form-label">详细地址：</div>
          <div class="form-input">
            <input type="text" name="address" id="address" class="form-control" maxlength="150" placeholder="请填写您的详细地址">
          </div>
        </label>
      </div>

      <div class="p10">
        <button type="button" id="address_submit" class="button btn-full btn-cyan btn-radius">保 存</button>
      </div>

    </div>
  </div>


  <script type="text/javascript">
    $(document).ready(function() {
    	$('#dealclick').click(function(){
    		var uuid = $('#shop_uuid').val();
    		$.get('/api/webchat/wx/jifen/exchange/need/address',{'uuid':uuid, 'datetime': new Date().getTime()},function(data){
    			//console.log(data);
    			if(data.status=='success'){
    				location.href='/api/webchat/wx/jifen/shop/exchange/comfirm/'+uuid;
    			}else if(data.status=='error'){
    				//编辑省信息
    				buildOp(data.itemList);
    				$('#add_address').show();
    			}else{
    				if(data.message=='not login'){
    					var url='/api/webchat/wx/jifen/shop/detail/'+uuid
    					loginAlert(url);
    				}else{
    					alert(data.message);
    				}
    			}
    		});
    	});

      $('#close_address').click(function(){
        $('#add_address').hide();
      });

      $('#address_submit').on('click', function(){
        var name = $('#name').val();
        var phone = $('#phone').val();
        var province = $('#province').val();
        var city = $('#city').val();
        var district = $('#district').val();
        var address = $('#address').val();
        var regPhone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;

        if (name == ''){
          alert('请填写收件人姓名！');
          return false;
        }
        if (phone == ''){
          alert('请填写收件人的手机号码！');
          return false;
        }
        if (phone == ''){
          alert('请填写收件人的手机号码！');
          return false;
        } else if (phone != '' && !regPhone.test(phone)){
          alert('请填写正确的手机号码！');
          return false;
        }
        if (province == ''){
          alert('请选择所在省份！');
          return false;
        }
        if (city == ''){
          alert('请选择所在城市！');
          return false;
        }
        if (address == ''){
          alert('请填写详细地址！');
          return false;
        }

        $.post(
          '/api/webchat/shop/address/add/data',
          {
            'name': name,
            'phone': phone,
            'cityUuid': city,
            'districtUuid': district,
            'address': address
          },
          function(data) {
            if(data.status=='success'){
              var uuid = $('#shop_uuid').val();
              window.location.href = '/api/webchat/wx/jifen/shop/exchange/comfirm/'+uuid;;
            }else{
              if(!!data.message){
                alert(data.message);
              }else{
                alert("地址添加失败！");
              }
            }
          }
        )

      });
    });

    function buildOp(itemList){
    	var province=$('#province');
    	if(!!itemList&&itemList.length>0){
    		$.each(itemList,function(index,item){
    			var option = '<option value=' + item.uuid + '>' + item.name
				+ '</option>';
    			province.append(option);
    		});
    	}
    }
  </script>
  <!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
</body>
</html>
