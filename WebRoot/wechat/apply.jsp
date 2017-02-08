<%@page import="com.lankr.tv_cloud.web.api.webchat.vo.ApplyVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>申请VIP</title>

    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
  </head>

  <body>
    <div class="page">
      <div class="content">
        <form name="applyForm" id="applyForm">
          <input type="hidden" name="token" value="${requestScope.token}"/>
          <div class="default-form">
            <label class="form-group">
              <div class="form-label">用户姓名：</div>
              <div class="form-input">
                <input type="text" name="applyName" id="applyName" class="form-control" placeholder="用户姓名不得为空" value="${requestScope.info.name}"/>
              </div>
            </label>
            <label class="form-group">
              <div class="form-label">手机号码：</div>
              <div class="form-input">
                <input type="tel" id="mobile" name="mobile" class="form-control" placeholder="请输入手机号码" value="${requestScope.info.phone}"/>
              </div>
            </label>
          </div>
          <div class="default-form mt10">
            <label class="form-group form-select">
              <div class="form-label">所在省份：</div>
              <div class="form-input">
                <select name="province" id="province" class="form-control" onchange="changeProvince(this.value);">
                  <option value="">请选择</option>
                  <c:if test="${not empty requestScope.info.proList}">
                    <c:forEach var="item" items="${requestScope.info.proList}">
    				          <option value="${item.uuid}">${item.name}</option>
    			          </c:forEach>
    			        </c:if>
                </select>
              </div>
            </label>
            <label class="form-group form-select">
              <div class="form-label">所在城市：</div>
              <div class="form-input">
                <select name="city" id="city" class="form-control" onchange="changeCity(this.value);">
                  <option value="">请选择</option>
                  <c:if test="${not empty requestScope.info.cityList}">
                    <c:forEach var="item" items="${requestScope.info.cityList}">
    				          <option value="${item.uuid}">${item.name}</option>
    			          </c:forEach>
    			        </c:if>
                </select>
              </div>
            </label>
            <label class="form-group form-select">
              <div class="form-label">所在医院：</div>
              <div class="form-input">
                <select name="hosipital" id="hosipital" class="form-control">
                  <option value="">请选择</option>
                  <c:if test="${not empty requestScope.info.hosList}">
                    <c:forEach var="item" items="${requestScope.info.hosList}">
    				          <option value="${item.uuid}">${item.name}</option>
    			          </c:forEach>
    			        </c:if>
                </select>
              </div>
            </label>
            <label class="form-group form-select">
              <div class="form-label">所在科室：</div>
              <div class="form-input">
                <select name="office" id="office" class="form-control">
                  <option value="">请选择</option>
                  <c:if test="${not empty requestScope.info.depList}">
                    <c:forEach var="item" items="${requestScope.info.depList}">
    				          <option value="${item.uuid}">${item.name}</option>
    			          </c:forEach>
    			        </c:if>
                </select>
              </div>
            </label>
            <label class="form-group">
              <div class="form-label">职　　称：</div>
              <div class="form-input">
                <input type="text" name="professor" id="professor" class="form-control" maxlength="80" value="${requestScope.info.professor}"/>
              </div>
            </label>
          </div>
          <div class="p10">
            <button type="submit" id="submitBtn" class="button btn-full btn-cyan btn-radius">提 交</button>
          </div>
        </form>
      </div>
    </div>
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
     <script type="text/javascript" src="/assets/js/wechat/region.js"></script>
    <script type="text/javascript">
    $(document).ready(function(){
    	var provinceUUid = '${requestScope.info.proUUid}';
    	if(!!provinceUUid){
    		$('#province').val(provinceUUid);
    	}
    	var cityUUid = '${requestScope.info.cityUUid}';
    	if(!!cityUUid){
    		$('#city').val(cityUUid);
    	}
    	var hosUUid = '${requestScope.info.hosUUid}';
    	if(!!hosUUid){
    		$('#hosipital').val(hosUUid);
    	}
    	var depUUid = '${requestScope.info.depUUid}';
    	if(!!depUUid){
    		$('#office').val(depUUid);
    	}

    	var applyName=$('#applyName').val();
    	if(!!applyName){
    		$('#applyName').attr('readonly','readonly');
    	}

    	var mobile=$('#mobile').val();
    	if(!!mobile){
    		$('#mobile').attr('readonly','readonly');
    	}

      $('#applyForm').submit(function(event){
    	  event.preventDefault();
    	  var $form = $(this);
        var regPhone = /^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$/; //验证手机号码
        var mobileInput = $('#mobile');

        if ($('#applyName').val() == ''){
          alert('请填写您的姓名！');
          return false;
        }
        if (mobileInput.val() == ''){
          alert('请填写您的手机号码！');
          return false;
        }
        else if (mobileInput.val() != '' && !regPhone.test(mobileInput.val())){
          alert('请填写正确的手机号码！');
          mobileInput.val('');
          mobileInput.focus();
          return false;
        }
        if ($('#province').val() == ''){
          alert('请选择省份！');
          return false;
        }
        if ($('#city').val() == ''){
          alert('请选择城市！');
          return false;
        }
        if ($('#hosipital').val() == ''){
          alert('请选择医院！');
          return false;
        }
        if ($('#office').val() == ''){
          alert('请选择科室！');
          return false;
        }

        $.post('/api/webchat/apply/invite', $form.serialize(), function(data) {
        	if(data.status=='success'){
        		window.location.href = '/api/webchat/apply/finsh/page';
        	}else{
        		alert(data);
        	}
		})

      });
    });

    function buildOption(vo,elem,msg){
    	var jsonObject = JSON.parse(vo);
		if(!!jsonObject){
			var itemList=jsonObject.itemList;
			if(!!itemList){
				$.each(itemList, function(index, item) {
					var option = '<option value='+item.uuid+'>' + item.name
							+ '</option>';
					elem.append(option);
				});
			}else{
				alert(msg);
			}
		}else{
			alert(msg);
		}
    }
    </script>

  </body>
</html>
