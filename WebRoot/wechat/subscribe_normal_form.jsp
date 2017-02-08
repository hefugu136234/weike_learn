<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta content="template language" name="keywords" />
    <meta content="author" name="author" />
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" name="viewport" />
    <meta content="yes" name="mobile-web-app-capable" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
    <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style" />
    <title>知了云盒公测预约</title>
    <link rel="stylesheet" media="all" href="/assets/css/app/default.css" />
    <link rel="stylesheet" media="all" href="/assets/css/app/subscribe.css" />
  </head>

  <body>
    <div id="floatForm">
      <div class="floatFormBox" id="step1">
        <div class="formMain">
          <div class="lineTitle">
            预约申请
          </div>
          <div class="tips">
            请填写您的手机号以及验证码用来预约申请
          </div>
          <div class="form-group">
            <div class="label_tag">
              手机号
            </div>
            <div class="input_box">
              <div class="input_code">
                <input type="text" id="userMobile" class="form-control" placeholder="请输入手机号" required="required" />
              </div>
              <button name="button" id="codeBtn">获取验证码</button>
            </div>
          </div>
          <div class="form-group">
            <div class="label_tag">
              验证码
            </div>
            <div class="input_box">
              <input type="text" id="validCode" class="form-control" placeholder="请输入验证码" required="required"/>
            </div>
          </div>
          <div class="p10">
            <button name="button" id="nextBtn" class="btn-full">
              <span>下一步</span>
            </button>
          </div>
        </div>
      </div>
      <div class="floatFormBox hide" id="step2">
        <div class="formMain">
          <div class="lineTitle">填写用户信息</div>
          <div class="tips">请务必填写您的真实信息，这些信息会影响到您的申请结果</div>
          <div class="form-group">
          <input type="hidden" id="valid_code" value=""/>
            <div class="label_tag"><span class='dot'>*</span>姓　名</div>
            <div class="input_box">
              <input type="text" name="name" id="userName" class="form-control" placeholder="请输入姓名" required="required" />
            </div>
          </div>
          <div class="form-group">
            <div class="label_tag"><span class='dot'>*</span>所在省</div>
            <div class="input_box">
              <select name="province" id="province" class="form-control" onchange="changeProvince(this.value);">
                <option value="">请选择</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <div class="label_tag"><span class='dot'>*</span>所在市</div>
            <div class="input_box">
              <select name="city" id="city" class="form-control" onchange="changeCity(this.value);">
                <option value="">请选择</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <div class="label_tag"><span class='dot'>*</span>医　院</div>
            <div class="input_box">
              <select name="hospital" id="hospital" class="form-control">
                <option value="">请选择</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <div class="label_tag"><span class='dot'>*</span>科　室</div>
            <div class="input_box">
              <select name="department" id="userDepartment" class="form-control">
                <option value="">请选择</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <div class="label_tag"><span class='dot'>*</span>职　称</div>
            <div class="input_box">
              <select name="job_title" id="userJobTitle" class="form-control">
                <option value="">请选择职称</option>
                <option value="医师">医师</option>
                <option value="主治医师">主治医师</option>
                <option value="副主任医师">副主任医师</option>
                <option value="主任医师">主任医师</option>
                <option value="护师">护师</option>
                <option value="药师">药师</option>
                <option value="技师">技师</option>
              </select>
            </div>
          </div>
          <div class="p10">
            <input type="button" value="提交申请" id="submit_Btn" class="btn-full" />
          </div>
        </div>
      </div>
      <div class="floatFormBox hide" id="step3">
        <div class="formMain successInfo">
          <div class="lineTitle">申请成功</div>
          <p class="img">
            <img alt="" width="40%" src="/assets/img/app/feedback_success.png" />
          </p>
          <p>您的申请已经成功提交</p>
          <p>我们将会在24小时内审核您的信息</p>
          <p>审核通过后会通过短信发送给您邀请码</p>
          <p>请您耐心等待</p>
          <p class="btn">
            <a class="btn-full" href="/f/wx/subscribe/entrance">我知道了</a>
          </p>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="/assets/js/jquery.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/subscribe_common.js"></script>
    <script type="text/javascript" src="/assets/js/wechat/subscribe_wx.js"></script>
  </body>
</html>
