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
    <title>普通作品</title>
    <link href="/assets/css/app/font.css" rel="stylesheet">
    <link href="/assets/css/app/default.css" rel="stylesheet">
    <link href="/assets/css/app/activity_apply.css" rel="stylesheet">
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/all_common.js"></script>
  </head>

  <body>
    <div class="page">
      <div class="content">
        <header class="top-bar">
          <div class="crumb-nav">
            <a href="javascript:history.back(-1);" class="logo icon-logo"></a>
            普通作品
          </div>
        </header>

        <div id="act_apply_form">
          <input type="hidden" id="activityUuid" value="${requestScope.activityUuid}"/>
          <div class="default-form">
            <label class="form-group form-select">
              <div class="form-label">学科一级分类</div>
              <div class="form-input">
                <select id="categroy_one" class="form-control" onchange="changeOne(this.value);">
                  <option value="">请选择</option>
                </select>
              </div>
            </label>

            <!-- 学科二级分类 -->
            <label id="category_two_lable" class="form-group form-select hide">
              <div class="form-label">学科二级分类</div>
              <div class="form-input">
                <select id="categroy_two" class="form-control" onchange="changeTwo(this.value);">
                  <option value="">请选择</option>
                </select>
              </div>
            </label>

            <!-- 学科三级分类 -->
            <label id="category_three_lable" class="form-group form-select hide">
              <div class="form-label">学科三级分类</div>
              <div class="form-input">
                <select id="categroy_three" class="form-control" onchange="changeThree(this.value);">
                  <option value="">请选择</option>
                </select>
              </div>
            </label>

            <!-- 已选中学科分类 -->
            <label id="selected_category_lable" class="form-group hide">
              <div class="form-label">作品归属学科</div>
              <div class="form-input">
                <strong><span id="selected_category_name" style="color: red"></span></strong>
              </div>
              <input type="hidden" id="selected_category_uuid" value=""/>
            </label>

          </div>

          <div class="default-form mt10">
            <label class="form-group">
              <div class="form-label">作品名称</div>
              <div class="form-input">
                <input type="text" id="pro_name" name="" maxlength="100" class="form-control" placeholder="请输入作品名称">
              </div>
            </label>
            <label class="form-group">
              <textarea rows="4" placeholder="请输入作品简介" id="pro_desc" class="form-control" maxlength="150"></textarea>
            </label>
          </div>

          <div class="icon-title mt10 mb-1 white bold clearfix">
            <h5 class="tt bfL">交付方式</h5>
          </div>
          <ul class="upload-method">
            <li class="active" data-pay="2">
              <span class="icon icon-checked"></span>
              <span class="icon icon-cloud"></span>
              网盘上传
            </li>
            <li data-pay="1">
              <span class="icon icon-checked"></span>
              <span class="icon icon-truck"></span>
              快递交付
            </li>
          </ul>
          <input type="hidden" id="pay_method">

          <div class="default-form">
            <div class="form-group protocol">
              <input type="checkbox" checked="checked" name="protocol" id="protocol_act">
              <a href="javascript:;" class="link open-protocol">作品授权知情同意书</a>
            </div>
          </div>

          <div class="p10">
            <button type="button" id="submitBtn" class="button btn-full btn-cyan btn-radius">提交报名表</button>
          </div>
        </div>

      </div>
    </div>

    <div class="page" id="act_apply_feedback">
      <div class="content">
        <header class="top-bar">
          <div class="crumb-nav">
            <a href="/api/webchat/my/center" class="logo icon-logo"></a>
            报名申请成功
          </div>
        </header>

        <div class="act-apply-feedback">
          <div class="tag">
            <p><img src="/assets/img/app/activity/icon_success.png" width="10%"></p>
            <p>恭喜您! 您的报名申请成功!</p>
          </div>
          <div class="info">
            <p class="tt">您的作品编号为：</p>
            <p id="oupsCode" class="serial-num"></p>
          </div>
          <div class="info">
            <p class="tt">您的作品学科分类为：</p>
            <p id="oupsClassify" class="serial-num"></p>
          </div>
          <div class="info white-bg">
            <p class="tt">请将您的作品文件改名为：</p>
            <p id="oupsName" class="label">《5127<span id="proName"></span>》</p>
          </div>
          <div class="info" id="method_express">
            <p class="tt">收件人：知了云盒项目组 4008062533</p>
            <p class="label">地址：上海市长宁区天山路310号12楼</p>
            <p class="remark">(或将网盘上的作品下载地址，发送至:<span>bd@z-box.com.cn</span>)</p>
          </div>
          <div class="info hide" id="method_cloud">
            <p class="tt">将您网盘上的作品下载地址发到邮箱：</p>
            <p class="label">bd@z-box.com.cn</p>
            <p class="remark">请在邮箱中留下您的联系方式</p>
            <p class="remark">或将快递发到以下地址：<span>上海市长宁区天山路310号12楼</span></p>
            <p class="remark">收件人：知了云盒项目组 4008062533</p>
          </div>
        </div>
        <div class="act-feedback-tips hide">我们会在收到后，通过短信或者微信推送方式通知您的作品上传及审核状态，请注意查收。</div>

        <div class="act-btn-group clearfix">
          <a href="/api/webchat/my/center" class="button btn-cyan btn-radius">确 认</a>
          <a href="/api/webchat/index" class="button btn-cyan-line btn-radius">进入知了云盒</a>
        </div>
      </div>
    </div>

    <div class="page white-bg" id="act_apply_protocol">
      <div class="content">
        <div class="protocol-modal">
          <h5 class="tt">作品授权知情同意书</h5>
          <p>授权声明：</p>
          <p>您需保证您是本作品作者本人。</p>
          <p>您同意将您研究领域内的知识通过”知了云盒平台” [包括但不限于PC、平板、手机、电视、机顶盒等全部终端、客户端产品以及延伸的相关学术会议，直播等（下称“知了云盒”）]进行公开分享（包括但不仅限于播放，转发分享）。您同意”知了云盒”使用本次手术、授课等形式作品（包括包含的相关资料，包含但不仅包含幻灯、视频、文献等）及由此使用您的肖像，并允许本作品在“知了云盒”中使用。本授权知情同意书自签署之日起生效。</p>
          <p>作品：</p>
          <p>在本作品授权知情同意书中所称的“作品”应当包括下列任何的组成或全部：</p>
          <p>1.通过”知了云盒”的员工、授权或制作方等第三方拍摄制作的手术或授课视频及您在手术或讲课过程中您做出的任何或全部的陈述、手术操作以及讲课时所涉及的课件资料。</p>
          <p>2.通过任何载体的音频、视频或文献资料，由”知了云盒”通过其员工、代理商和/或代表所制作的影像、肖像、声音。</p>
          <p>用途：</p>
          <p>您同意作品用途如下：</p>
          <p>1.“知了云盒”客户端包括但不限于PC、平板、手机、电视、机顶盒等全部终端、客户端产品（下称“知了云盒”）；</p>
          <p>2.“知了云盒”相关或授权/合作的APP或微信等线上平台；</p>
          <p>3.“知了云盒”相关的线下会议；</p>
          <p>4.在与”知了云盒”合作且与”知了云盒”相关的第三方学术杂志（如，中国实用内科杂志等）刊登；</p>
          <p>5.经您同意后的其他用途。</p>
          <p>授权：</p>
          <p>1.您，兹可授权”知了云盒”及其受让人、被许可人、代理、授权第三方在有效期限内有权为任何其他合法目的将您的姓名、肖像、类似肖像的形象、图片、图像或照片影像、声音（合称为“肖像”）以任何合理合法的形式和方式用于相关媒体，包括但不限于合成的表现形式，但”知了云盒”可告知您该使用。您放弃对最终版本，包括与之有关的书面文件进行审核的权利，并免除”知了云盒”、其受让人、被许可人、代理或法定代表人因本授权而受到任何索赔，包括但不限于因诽谤、侵犯个人隐私、侵犯道德权利、宣传权利或版权而引起的索赔。本授权的行使完全由”知了云盒”自主决定。</p>
          <p>2.您进一步确认，您对带有您的肖像、影像、声音的任何作品不拥有版权权益，您愿意将该等权利全部转让给”知了云盒”，包括全球范围内的版权（无论是否已被登记）转让，并授权”知了云盒”及其受让人、被许可人、代理或法定代表人有权在全球范围对作品的版权和相关权利进行登记和续展，包括但不限于为任何目的对肖像、影像、声音制作副本、复制、复印、发行、展示、许可、改编、制作衍生作品以及进行其他使用。如果本转让无法实施，您同意放弃对”知了云盒”及其受让人、被许可人、代理或法定代表人执行上述权利和利益。并且，您将上述权利和利益，连同无限制分许可的权利排他地、不可撤销地、永久地免费授权给”知了云盒”。所有该等权利可由”知了云盒”完全转让。</p>
          <p>3.您特此授予”知了云盒”及其授权的任何第三方对作品进行使用和复制的权利。您授权”知了云盒”及其所有的被许可人和受让人不受限的版权、出版和使用作品及其任何复制品或改编作品的权利，并可以进行编辑。您知道包含作品的材料可能会引起媒体人士做出采访请求。您了解”知了云盒”可自行决定作品的选择、编辑、以及翻译成不同语言，并且”知了云盒”可以自行决定使用的目的，但使用目的应当是合法的。您同意任何及所有由”知了云盒”或其委托的第三方拍摄的影像和/或照片属于”知了云盒”或其受让人、被许可人的财产。</p>
          <p>免责：</p>
          <p>您特此免除”知了云盒”及其所有的被许可人和受让人、代理或法定代表人，在授权范围内的任何改变、或组合使用、或其他任何形式造成的无论是有意或其他可能原因而发生在出版、销售或使用作品过程中的任何责任。您特此放弃任何您可能需要进行检查或批准作品的使用和/或复制的权利。</p>
        </div>

        <div class="p10">
          <a href="javascript:;" class="button btn-full btn-cyan btn-radius close-page-btn">已阅读并同意协议</a>
        </div>
      </div>
    </div>
    <script type="text/javascript">
      $(function() {
    	  //加载一级分类的数据
  		  var showCategory='麻醉:消化内科:外科:呼吸:肿瘤:急诊';
  		  $.getJSON('/api/webchat/opus/category/root',{'showCategory':showCategory},
  				function(data){
  			  if(data.status=='success'&&!!data.itemList){
  				 categroy_one=$('#categroy_one');
  				 initDataCategory(categroy_one,data.itemList);
  			  }
  		  });

        $('#pay_method').val($('.upload-method').find('li.active').data('pay'));

        $('.upload-method').find('li').each(function(){
          var $that = $(this);
          $that.click(function(){
            $that.siblings('li').removeClass('active');
            $that.addClass('active');
            $('#pay_method').val($that.data('pay'));

            if ($('#pay_method').val() == '1'){
              $('#method_cloud').hide();
              $('#method_express').show();
            }
            else if ($('#pay_method').val() == '2'){
              $('#method_express').hide();
              $('#method_cloud').show();
            }
          });
        });

        $('.open-protocol').click(function(){
          $('#act_apply_protocol').show();
        });

        $('.close-page-btn').click(function(){
          $(this).parents('.page').hide();
        });

        $('#submitBtn').click(function(){
          var selected_category_uuid=$('#selected_category_uuid').val();
          var pro_name=$('#pro_name').val();
          var pro_desc=$('#pro_desc').val();
          var pay_method=$('#pay_method').val();
          var activityUuid=$('#activityUuid').val();
          if(selected_category_uuid == ''){
            alert('请选择学科！');
            return false;
          }
          if(pro_name == ''){
            alert('请填写作品名称！');
            return false;
          }
          if(pro_desc == ''){
            alert('请填写作品简介！');
            return false;
          }

          var protocol_check = $('#protocol_act').is(':checked');
          if (protocol_check == false){
            alert('请选择用户协议！');
            return false;
          }

          $.post(
            '/api/webchat/activity/opus/appilcation/save',
            {
            'activityUuid':activityUuid,
            'name':pro_name,
            'categoryUuid' : selected_category_uuid,
             'desc' : pro_desc,
              'sendType' : pay_method
            }, function(data) {
              if (data.status == 'success') {
            	  $('#oupsCode').text(data.codeNum);
            	  var oupsName_text='《'+data.codeNum+data.name+'》';
            	  $('#oupsName').text(oupsName_text);
                $('#oupsClassify').html(data.categoryName);
                $('#actApplyFeedback').show();
              } else {
                if (!!data.message) {
                  alert(data.message);
                } else {
                  alert('提交申请失败！');
                }
              }
            }
          );

        });
      });

      //给分类填充数据
      function initDataCategory(select,itemList){
    	  $.each(itemList,function(index,item){
    		  var option=' <option value="'+item.uuid+'">'+item.name+'</option>';
    		  select.append(option);
    	  });
      }

      //分类一产生变化
      function changeOne(val){
    	  if(!!val){
    		  //显示选中学科
    		  //$('#selected_category_name').text($("#categroy_one").find("option:selected").text());
    		  $('#selected_category_uuid').val(val);
    		  //变化categroy_two
    		  $.getJSON('/api/webchat/opus/category/second/'+val,function(data){
    			  console.log(data);
    			  if(data.status=='success'&&!!data.itemList){
    				  var categroy_two=$("#categroy_two");
    				  categroy_two.empty();
    				  categroy_two.append('<option value="">请选择</option>');
    				  initDataCategory(categroy_two,data.itemList);
    				  $('#category_two_lable').removeClass('hide').show();
    				  $('#category_three_lable').hide();
    			  }else{
    				  $('#category_two_lable').hide();
    				  $('#category_three_lable').hide();
    			  }
    		  });
    	  }else{
    		  $('#category_two_lable').hide();
    		  $('#category_three_lable').hide();
    		 // $('#selected_category_lable').hide();
    		  //$('#selected_category_name').text('');
    		  $('#selected_category_uuid').val('');
    	  }
      }

      //分类二变化
      function changeTwo(val){
    	  if(!!val){
    		 //var value=$("#categroy_one").find("option:selected").text()+'→'+$("#categroy_two").find("option:selected").text();
    		// $('#selected_category_name').text(value);
   		     $('#selected_category_uuid').val(val);
   		  $.getJSON('/api/webchat/opus/category/second/'+val,function(data){
			  if(data.status=='success'&&!!data.itemList){
				  var categroy_three=$("#categroy_three");
				  categroy_three.empty();
				  categroy_three.append('<option value="">请选择</option>');
				  initDataCategory(categroy_three,data.itemList);
				  $('#category_three_lable').removeClass('hide').show();
			  }else{
				  $('#category_three_lable').hide();
			  }
		  });
    	  }else{
    		  //$('#selected_category_name').text($("#categroy_one").find("option:selected").text());
    		  $('#selected_category_uuid').val($("#categroy_one").val());
    	  }
      }

      function changeThree(val){
    	  if(!!val){
    		// var value=$("#categroy_one").find("option:selected").text()+'→'+$("#categroy_two").find("option:selected").text();
    		// $('#selected_category_name').text(value);
   		     $('#selected_category_uuid').val(val);
    	  }else{
    		 // $('#selected_category_name').text($("#categroy_one").find("option:selected").text());
    		  $('#selected_category_uuid').val($("#categroy_two").val());
    	  }
      }
    </script>
    <!-- 加入分享 -->
	<%@ include file="/wechat/common/base_share.jsp"%>
  </body>
</html>
